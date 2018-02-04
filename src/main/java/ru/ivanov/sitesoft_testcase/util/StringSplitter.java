/**
 * 
 */
package ru.ivanov.sitesoft_testcase.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author papa
 *
 */
public class StringSplitter {

	/**
	 * @param line
	 * @return
	 */
	public static List<String> splitLine(final String line) {
		List<String> result = new ArrayList<>();
		
		if (line.startsWith("\"")) {
			final int index = line.indexOf('"', 1);
			
			if (0 < index) {
				result.add(line.substring(1, index));
				
				if (index < line.length() - 1) {
					result.addAll(splitLine(line.substring(index + 1)));
				}
				
				return result;
			}
		}
		
		final String[] splitLine = line.split("\\s", 2);		
		final String first = splitLine[0];
		
		if (!first.isEmpty()) {
			result.add(first);
		}
		
		if (1 < splitLine.length) {
			result.addAll(splitLine(splitLine[1]));
		}
		
		return result;
	}

}
