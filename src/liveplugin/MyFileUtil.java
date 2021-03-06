package liveplugin;

import com.intellij.openapi.util.io.FileUtil;
import com.intellij.util.Function;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class MyFileUtil {
	public static List<String> fileNamesMatching(String regexp, String path) {
		List<File> files = FileUtil.findFilesByMask(Pattern.compile(regexp), new File(path));
		return ContainerUtil.map(files, new Function<File, String>() {
			@Override public String fun(File it) {
				return it.getName();
			}
		});
	}

	public static String asUrl(File file) {
		try {
			return file.toURI().toURL().toString();
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public static List<File> allFilesInDirectory(File dir) {
		LinkedList<File> result = new LinkedList<File>();
		File[] files = dir.listFiles();
		if (files == null) return result;

		for (File file : files) {
			if (file.isFile()) {
				result.add(file);
			} else if (file.isDirectory()) {
				result.addAll(allFilesInDirectory(file));
			}
		}
		return result;
	}

	@Nullable public static File findSingleFileIn(String path, String fileName) {
		List<File> files = allFilesInDirectory(new File(path));
		List<File> result = new ArrayList<File>();
		for (File file : files) {
			if (fileName.equals(file.getName())) {
				result.add(file);
			}
		}
		if (result.size() == 0) return null;
		else if (result.size() == 1) return result.get(0);
		else throw new IllegalStateException("Found several " + fileName + " files under " + path);
	}

	public static String[] readLines(String url) throws IOException {
		return FileUtil.loadTextAndClose(new BufferedReader(new InputStreamReader(new URL(url).openStream()))).split("\n");
	}
}
