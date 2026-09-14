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
	public static List<String> splitLine(final String line) {
		List<String> result = new ArrayList<>();
		StringBuilder currentToken = new StringBuilder();
		boolean inQuotes = false;
		int i = 0;

		while (i < line.length()) {
			char ch = line.charAt(i);

			if (ch == '"') {
				inQuotes = !inQuotes;
				currentToken.append(ch);
			} else if (!inQuotes && Character.isWhitespace(ch)) {
				if (!currentToken.isEmpty()) {
					add(result, currentToken);
					currentToken.setLength(0);
				}
			} else {
				currentToken.append(ch);
			}
			i++;
		}

		// Add last token if present
		if (!currentToken.isEmpty()) {
			add(result, currentToken);
		}

		return result;
	}

	private static void add(List<String> result, StringBuilder currentToken) {
		String line = currentToken.toString().trim();

		if (line.isEmpty()) {
			return;
		}

		if ('"' == line.charAt(0)) {
			final int index = line.lastIndexOf('"');

			if (1 < index) {
				result.add(line.substring(1, index));
				return;
			}
		}

		result.add(line);
	}

}
