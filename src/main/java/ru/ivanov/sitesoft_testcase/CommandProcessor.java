/**
 * Тестовое задание от sitesoft для Java
 */
package ru.ivanov.sitesoft_testcase;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

		if ("quit".equals(args[0])) {
			return false;
		}

        switch (args[0]) {
            case "add" -> processAdd(args);
            case "attributes" -> getAttributes(args);
            case "change" -> processChange(args);
            case "content" -> getContent(args);
            case "document" -> getDocument(args);
            case "documents" -> getDocumentsList();
            case "initialization" -> createDatabase();
            case "remove" -> processRemove(args);
            default -> System.out.println(UNKNOWN_COMMAND);
        }
        return true;
	}

	private void getContent(String[] args) {
		if (3 > args.length) {
			System.out.println(TOO_FEW_ARGUMENTS);
			return;
		}
		
		try (FileOutputStream outputStream = new FileOutputStream(args[2])) {
			try (InputStream documentContent = documentsDomain.getDocumentContent(Long.parseLong(args[1]))) {
				if (null != documentContent) {
					byte[] buffer = new byte[1024 * 1024];				
					int read;
					
					while (0 < (read = documentContent.read(buffer))) {
						outputStream.write(buffer, 0, read);
					}
				}
			}
		} catch (NumberFormatException | SQLException | IOException e) {
			e.printStackTrace();
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

        if (args[1].equals("attribute")) {
            try {
                changeAttribute(args);
            } catch (NumberFormatException | SQLException e) {
                e.printStackTrace();
            }
        } else {
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
		
		Integer newInteger = 6 < args.length && !args[6].isEmpty() ? Integer.parseInt(args[6]) : null;

		documentsDomain.updateAttribute(new DocumentAttribute(attribute.id(), attribute.documentId(), args[3], args[4], args[5], newInteger));
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
		System.out.println(MessageFormat.format("id: {0}; documentId: {1}; name: {2}; type: {3}; string: {4}; integer: {5}.", attribute.id(), attribute.documentId(), attribute.name(), attribute.type(), attribute.stringValue(), attribute.integerValue()));
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

		Integer newInteger = 6 < args.length && !args[6].isEmpty() ? Integer.parseInt(args[6]) : null;
		final DocumentAttribute attribute = documentsDomain.createDocumentAttribute(Long.parseLong(args[2]), args[3], args[4], args[5], newInteger);
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
		
		try (final FileInputStream inputStream = new FileInputStream(args[4])) {
			final Document document = documentsDomain.createDocument(args[2], args[3]);
			documentsDomain.addDocument(document, inputStream);
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e1) {
			e1.printStackTrace();
		}		
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
		System.out.println(MessageFormat.format("id: {0}; name: {1}, type: {2}.", document.id(), document.name(), document.type()));
	}

	private static final String TOO_FEW_ARGUMENTS = "Too few arguments.";
	private static final String UNKNOWN_COMMAND = "Unknown command.";
	
	private final DocumentsDomain documentsDomain;
}
