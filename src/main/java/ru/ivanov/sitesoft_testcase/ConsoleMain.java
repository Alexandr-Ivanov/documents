/**
 * Тестовое задание от sitesoft для Java
 */
package ru.ivanov.sitesoft_testcase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

import ru.ivanov.sitesoft_testcase.domain.DocumentsDomain;
import ru.ivanov.sitesoft_testcase.domain.DocumentsDomainImpl;
import ru.ivanov.sitesoft_testcase.util.StringSplitter;

/**
 * Класс запуска
 * @author Alexandr Ivanov
 *
 */
public class ConsoleMain {
	private static final String PROMPT = "Documents>";

	/**
	 * @param args
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws SQLException, IOException {
		domain = new DocumentsDomainImpl("Documents");
		CommandProcessor commandProcessor = new CommandProcessor(domain);
		
		if (commandProcessor.process(args)) {
			bufferedReader = new BufferedReader(new InputStreamReader(System.in));
			String[] command = readCommand();
			
			while (commandProcessor.process(command)) {
				command = readCommand();
			}
		}
		
		domain.cleanup();
	}

	/**
	 * Прочитать очередную команду с консоли
	 * @return массив строк, составляющих введённую команду
	 * @throws IOException
	 */
	private static String[] readCommand() throws IOException {
		System.out.print(PROMPT);
		final String line = bufferedReader.readLine();
		
		if (line.isEmpty()) {
			return new String[0];
		}
		
		List<String> result = StringSplitter.splitLine(line);
		return result.toArray(new String[result.size()]);
	}

	private static BufferedReader bufferedReader;
	private static DocumentsDomain domain;
}
