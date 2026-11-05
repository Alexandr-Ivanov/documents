/**
 * Тестовое задание от sitesoft для Java
 */
package ru.ivanov.sitesoft_testcase;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
class CommandProcessor {
	private static final String INVALID_DOCUMENT_ID_FORMAT = "Invalid document ID format: {0}. It must be a whole number.";
	private static final String INVALID_DOCUMENT_ATTRIBUTE_ID_FORMAT = "Invalid document attribute ID format: {0}. It must be a whole number.";

	private static final String TOO_FEW_ARGUMENTS = "Too few arguments.";
	private static final String UNKNOWN_COMMAND = "Unknown command.";

	private final DocumentsDomain documentsDomain;

	CommandProcessor(DocumentsDomain domain) {
		this.documentsDomain = domain;
	}

	/**
	 * Обработать команду
	 * @param args input string split by spaces
	 * @return true, пока работа не закончена, false, если команда на выход
	 */
	boolean process(String[] args) {
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
			documentsDomain.putDocumentContentIntoOutputStream(Long.parseLong(args[1]), outputStream);
		} catch (NumberFormatException | SQLException | IOException e) {
			System.out.println(e.getMessage());
		}
	}

	private void createDatabase() {
		try {
			documentsDomain.createDatabase();
		} catch (SQLException e) {
			System.out.println("Failed to initialize the database: " + e.getMessage());
		}
	}

	private void processChange(String[] args) {
		if (2 > args.length) {
			System.out.println(TOO_FEW_ARGUMENTS);
			return;
		}

		switch (args[1]) {
            case "attribute" -> changeAttribute(args);
			case "content" -> changeContent(args);
            default -> System.out.println(UNKNOWN_COMMAND);
		}
	}

	private void changeContent(String[] args) {
		if (4 > args.length) {
			System.out.println(TOO_FEW_ARGUMENTS);
			return;
		}

		var fileName = args[3];

		try (var inputStream = new FileInputStream(fileName)){
            documentsDomain.setDocumentContent(Long.parseLong(args[2]), inputStream);
		} catch (NumberFormatException _) {
			System.out.println(MessageFormat.format(INVALID_DOCUMENT_ID_FORMAT, args[2]));
		} catch (FileNotFoundException _) {
            System.out.println("File not found: " + fileName);
        } catch (IOException | SQLException e) {
            System.out.println("Failed to change content: " + e.getMessage());
        }
    }

	private void changeAttribute(String[] args) {
		if (6 > args.length) {
			System.out.println(TOO_FEW_ARGUMENTS);
			return;
		}

        try {
            final DocumentAttribute attribute = documentsDomain.getDocumentAttribute(Long.parseLong(args[2]));

            if (null == attribute) {
                System.out.println(MessageFormat.format("No attribute with id={0}.", args[2]));
                return;
            }

            Integer newInteger = 6 < args.length && !args[6].isEmpty() ? Integer.parseInt(args[6]) : null;

            documentsDomain.updateAttribute(new DocumentAttribute(attribute.id(), attribute.documentId(), args[3], args[4], args[5], newInteger));
        } catch (SQLException e) {
            System.out.println("Failed to change attribute: " + e.getMessage());
        } catch (NumberFormatException _) {
			System.out.println(MessageFormat.format(INVALID_DOCUMENT_ATTRIBUTE_ID_FORMAT, args[2]));
        }
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
		} catch (NumberFormatException _) {
			System.out.println(MessageFormat.format(INVALID_DOCUMENT_ATTRIBUTE_ID_FORMAT, args[1]));
		} catch (SQLException e) {
            System.out.println(MessageFormat.format("Failed to get attributes for document with Id {0}: {1}", args[1], e.getMessage()));
        }
    }

	private void printAttribute(DocumentAttribute attribute) {
		System.out.println(MessageFormat.format("id: {0}; documentId: {1}; name: {2}; type: {3}; string: {4}; integer: {5}.", attribute.id(), attribute.documentId(), attribute.name(), attribute.type(), attribute.stringValue(), attribute.integerValue()));
	}

	private void processRemove(String[] args) {
		if (2 > args.length) {
			System.out.println(TOO_FEW_ARGUMENTS);
			return;
		}
		
		switch (args[1]) {
		case "attribute" -> processRemoveAttribute(args);
		case "document" -> processRemoveDocument(args);
		default -> System.out.println(UNKNOWN_COMMAND);
		}		
	}

	private void processRemoveAttribute(String[] args) {
		if (3 > args.length) {
			System.out.println(TOO_FEW_ARGUMENTS);
			return;
		}

        try {
            documentsDomain.removeAttribute(Long.parseLong(args[2]));
        } catch (NumberFormatException _) {
            System.out.println(MessageFormat.format("Invalid document attribute ID format: {0}. It must be a natural number.", args[2]));
        } catch (SQLException e) {
			System.out.println("Failed to remove attribute with Id " + args[2]);
        }
    }

	private void processRemoveDocument(String[] args) {
		if (3 > args.length) {
			System.out.println(TOO_FEW_ARGUMENTS);
			return;
		}

        try {
            documentsDomain.removeDocument(Long.parseLong(args[2]));
        } catch (SQLException e) {
			System.out.println("Failed to remove document with Id " + args[2]);
        } catch (NumberFormatException e) {
			System.out.println(MessageFormat.format(INVALID_DOCUMENT_ID_FORMAT, args[2]));
        }
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
		} catch (NumberFormatException e) {
			System.out.println(MessageFormat.format(INVALID_DOCUMENT_ID_FORMAT, args[1]));
		} catch (SQLException e) {
            System.out.println("Failed to get document with Id " + args[1]);
        }
    }

	private void processAdd(String[] args) {
		if (2 > args.length) {
			System.out.println(TOO_FEW_ARGUMENTS);
			return;
		}
		
		switch (args[1]) {
		case "document" -> processAddDocument(args);
		case "attribute" -> processAddAttribute(args);
		default -> System.out.println(UNKNOWN_COMMAND);
		}
	}

	private void processAddAttribute(String[] args) {
		if (6 > args.length) {
			System.out.println(TOO_FEW_ARGUMENTS);
			return;
		}

        try {
			Integer newInteger = 6 < args.length && !args[6].isEmpty() ? Integer.parseInt(args[6]) : null;

			try {
				final DocumentAttribute attribute = documentsDomain.createDocumentAttribute(Long.parseLong(args[2]), args[3], args[4], args[5], newInteger);
				documentsDomain.addAttribute(attribute);
			} catch (NumberFormatException _) {
				System.out.println(MessageFormat.format(INVALID_DOCUMENT_ID_FORMAT, args[2]));
			} catch (SQLException e) {
				System.out.println(MessageFormat.format("Failed to add attribute to document with Id {0}: {1}", args[2], e.getMessage()));
			}
		} catch (NumberFormatException _) {
			System.out.println("Wrong format: " + args[6] + ". It must be a whole number.");
		}

    }

	/**[
	 * @param args Input string split by spaces
	 * {@code @throw} SQLException
     */
	private void processAddDocument(String[] args) {
		if (5 > args.length) {
			System.out.println(TOO_FEW_ARGUMENTS);
			return;
		}
		
		try (final FileInputStream inputStream = new FileInputStream(args[4])) {
			final Document document = documentsDomain.createDocument(args[2], args[3]);
			documentsDomain.addDocument(document, inputStream);
		} catch (IOException | SQLException e) {
			System.out.println("Failed to add new document: " + e.getMessage());
		}		
	}

	private void getDocumentsList() {
		try {
			final List<Document> documents = documentsDomain.getDocumentsList();
			
			for (Document document : documents) {
				printDocument(document);
			}
		} catch (SQLException e) {
			System.out.println("Failed to get a list of documents: " + e.getMessage());
		}
	}

	/**
	 * @param document Document to print out
	 */
	private void printDocument(Document document) {
		System.out.println(MessageFormat.format("id: {0}; name: {1}, type: {2}.", document.id(), document.name(), document.type()));
	}
}
