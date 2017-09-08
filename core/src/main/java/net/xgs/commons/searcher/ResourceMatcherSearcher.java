package net.xgs.commons.searcher;

import java.util.LinkedHashSet;
import java.util.Set;

import net.xgs.commons.searcher.FileSearcher.FileEntry;
import net.xgs.commons.utils.Matcher;

public class ResourceMatcherSearcher {

	public static Set<String> getFiles(String[] packageNames,
			final String pattern) {
		String[] patterns = new String[] { pattern };
		return getFiles(packageNames, patterns);
	}

	public static Set<String> getFiles(String[] packageNames, final String[] patterns) {
		final Set<String> fileSet = new LinkedHashSet<String>();

		FileSearcher finder = new FileSearcher() {

			@Override
			public void visitFileEntry(FileEntry file) {
				if (isMatch(file, patterns)) {
					String fileName = file.getQualifiedFileName();
					fileSet.add(fileName);
				}
			}

		};

		finder.lookupClasspath(packageNames);

		return fileSet;
	}

	private static boolean isMatch(FileEntry file, String[] patterns) {
		return !file.isDirectory() && Matcher.matchName(file.getName(), patterns);
	}

}