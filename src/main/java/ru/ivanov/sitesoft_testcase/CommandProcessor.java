/**
 * Тестовое задание от sitesoft для Java
 */
package ru.ivanov.sitesoft_testcase;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;

import ru.ivanov.sitesoft_testcase.domain.Document;
import ru.ivanov.sitesoft_testcase.domain.DocumentAttribute;
import ru.ivanov.sitesoft_testcase.domain.DocumentsDomain;

/**
 * Класс для обработки команд
 * @author Alexandr Ivanov
 *
 */
public class CommandProcessor {



	public CommandProcessor(DocumentsDomain domain) {
		this.documentsDomain = domain;
	}

	/**
	 * Обработать команду
	 * @param args
	 * @return true, пока работа не закончена, false, если команда на выход
	 */
	public boolean process(String[] args) {
		if (0 == args.length) {
			return true;
		}
		
		switch (args[0]) {
		case "add":
			processAdd(args);
			return true;
		
		case "attributes":
			getAttributes(args);
			return true;
		
		case "change":
			processChange(args);
			return true;
			
		case "document":
			getDocument(args);
			return true;
			
		case "documents":
			getDocumentsList();
			return true;
			
		case "initialization":
			createDatabase();
			return true;
			
		case "quit":
			return false;

		case "remove":
			processRemove(args);
			return true;
		default:
			System.out.println(UNKNOWN_COMMAND);
			return true;
		}
	}

	private void createDatabase() {
		try {
			documentsDomain.createDatabase();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void processChange(String[] args) {
		if (2 > args.length) {
			System.out.println(UNKNOWN_COMMAND);
			return;
		}
		
		switch (args[1]) {
		case "attribute":
			try {
				changeAttribute(args);
			} catch (NumberFormatException | SQLException e) {
				e.printStackTrace();
			}
			
			break;
			
		default:
			System.out.println(UNKNOWN_COMMAND);
		}
	}

	private void changeAttribute(String[] args) throws NumberFormatException, SQLException {
		if (6 > args.length) {
			System.out.println(TOO_FEW_ARGUMENTS);
			return;
		}
		
		final DocumentAttribute attribute = documentsDomain.getDocumentAttribute(Long.parseLong(args[2]));
		
		if (null == attribute) {
			System.out.println(MessageFormat.format("No attribute with id={0}.", args[2]));
			return;
		}
		
		attribute.setName(args[3]);
		attribute.setType(args[4]);
		attribute.setString(args[5]);
		
		if (6 < args.length && !args[6].isEmpty()) {
			attribute.setInteger(Integer.parseInt(args[6]));
		} else {
			attribute.setInteger(null);
		}
		
		documentsDomain.updateAttribute(attribute);
	}

	private void getAttributes(String[] args) {
		if (2 > args.length) {
			System.out.println(TOO_FEW_ARGUMENTS);
			return;
		}
		
		try {
			final List<DocumentAttribute> documentAttributes = documentsDomain.getDocumentAttributes(Long.parseLong(args[1]));
			
			for (DocumentAttribute attribute : documentAttributes) {
				printAttribute(attribute);
			}
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		}
	}

	private void printAttribute(DocumentAttribute attribute) {
		System.out.println(MessageFormat.format("id: {0}; documentId: {1}; name: {2}; type: {3}; string: {4}; integer: {5}.", attribute.getId(), attribute.getDocumentId(), attribute.getName(), attribute.getType(), attribute.getStringValue(), attribute.getIntegerValue()));
	}

	private void processRemove(String[] args) {
		if (2 > args.length) {
			System.out.println(UNKNOWN_COMMAND);
			return;
		}
		
		switch (args[1]) {
		case "attribute":
			try {
				processRemoveAttribute(args);
			} catch (SQLException | NumberFormatException e) {
				e.printStackTrace();
			}
			
			break;
						
		case "document":
			try {
				processRemoveDocument(args);
			} catch (SQLException | NumberFormatException e) {
				e.printStackTrace();
			}
			
			break;
			
		default:
			System.out.println(UNKNOWN_COMMAND);	
		}		
	}

	private void processRemoveAttribute(String[] args) throws NumberFormatException, SQLException {
		if (3 > args.length) {
			System.out.println(TOO_FEW_ARGUMENTS);
			return;
		}
		
		documentsDomain.removeAttribute(Long.parseLong(args[2]));
	}

	private void processRemoveDocument(String[] args) throws SQLException, NumberFormatException {
		if (3 > args.length) {
			System.out.println(TOO_FEW_ARGUMENTS);
			return;
		}
		
		documentsDomain.removeDocument(Long.parseLong(args[2]));
	}

	private void getDocument(String[] args) {
		if (2 > args.length) {
			System.out.println(TOO_FEW_ARGUMENTS);
			return;
		}

		try {
			final Document document = documentsDomain.getDocument(Long.parseLong(args[1]));
			
			if (null == document) {
				System.out.println(MessageFormat.format("No document with id = {0}.", args[1]));
			} else {
				printDocument(document);
			}
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		}
	}

	private void processAdd(String[] args) {
		if (2 > args.length) {
			System.out.println(UNKNOWN_COMMAND);
			return;
		}
		
		switch (args[1]) {
		case "document":
			try {
				processAddDocument(args);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			break;
			
		case "attribute":
			try {
				processAddAttribute(args);
			} catch (NumberFormatException | SQLException e) {
				e.printStackTrace();
			}
			
			break;
			
		default:
			System.out.println(UNKNOWN_COMMAND);	
		}
	}

	private void processAddAttribute(String[] args) throws SQLException, NumberFormatException {
		if (6 > args.length) {
			System.out.println(TOO_FEW_ARGUMENTS);
			return;
		}
		
		final DocumentAttribute attribute = documentsDomain.createDocumentAttribute();
		attribute.setDocumentId(Long.parseLong(args[2]));
		attribute.setName(args[3]);
		attribute.setType(args[4]);
		attribute.setString(args[5]);
		
		if (6 < args.length && !args[6].isEmpty()) {
			attribute.setInteger(Integer.parseInt(args[6]));
		}
		
		documentsDomain.addAttribute(attribute);
	}

	/**[
	 * @param args
	 * @throws SQLException 
	 */
	private void processAddDocument(String[] args) throws SQLException {
		if (5 > args.length) {
			System.out.println(TOO_FEW_ARGUMENTS);
			return;
		}
		
		final Document document = documentsDomain.createDocument();
		document.setIndex(args[2]);
		document.setName(args[3]);
		document.setType(args[4]);
		documentsDomain.addDocument(document);
	}

	private void getDocumentsList() {
		try {
			final List<Document> documents = documentsDomain.getDocumentsList();
			
			for (Document document : documents) {
				printDocument(document);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param document
	 */
	private void printDocument(Document document) {
		System.out.println(MessageFormat.format("id: {0}; index: {1}; name: {2}, type: {3}.", document.getId(), document.getIndex(), document.getName(), document.getType()));
	}

	private static final String TOO_FEW_ARGUMENTS = "Too few arguments.";
	private static final String UNKNOWN_COMMAND = "Unknown command.";
	
	private DocumentsDomain documentsDomain;
}
