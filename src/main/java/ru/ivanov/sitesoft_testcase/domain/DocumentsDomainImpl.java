/**
 * Тестовое задание от sitesoft для Java
 */
package ru.ivanov.sitesoft_testcase.domain;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.sqlite.SQLiteException;

/**
 * Класс для работы с хранилищем объектов
 * 
 * @author Alexander Ivanov
 *
 */
public class DocumentsDomainImpl implements DocumentsDomain {

	private static final String SELECT_DOCUMENTS = "SELECT id, name, type FROM documents ORDER BY id ASC;";
	private static final String INSERT_INTO_DOCUMENTS = "INSERT INTO documents (name,type) VALUES (?,?);";
	private static final String UPDATE_DOCUMENT_CONTENT = "UPDATE documents SET content=? WHERE id=?";
	private static final String GET_ATTRIBUTES_FOR_DOCUMENT = "SELECT id,documentId,name,type,stringValue,integerValue FROM documentAttributes where documentId=?";
	private static final String GET_DOCUMENT_CONTENT = "SELECT content FROM documents WHERE id=?;";
	private static final String INSERT_DOCUMENT_ATTRIBUTE = "INSERT INTO documentAttributes (documentId,name,type,stringValue,integerValue) VALUES (?,?,?,?,?);";
	private static final String GET_DOCUMENT_ATTRIBUTE_BY_ID = "SELECT id,documentId,name,type,stringValue,integerValue FROM documentAttributes WHERE id=?;";
	private static final String GET_DOCUMENT_BY_ID = "SELECT id, name, type FROM documents WHERE id=?;";
	private static final String UPDATE_ATTRIBUTE = "UPDATE documentAttributes SET name=?,type=?,stringValue=?,integerValue=? WHERE id=?;";
	private static final String DELETE_ATTRIBUTE = "DELETE FROM documentAttributes WHERE id=?;";
	private static final String DELETE_ATTRIBUTES_FOR_DOCUMENT = "DELETE FROM documentAttributes WHERE documentId=?;";
	private static final String DELETE_DOCUMENT = "DELETE FROM documents WHERE id=?;";
	private static final String DROP_TABLE_DOCUMENTS = "DROP TABLE 'documents';";
	private static final String DROP_TABLE_DOCUMENT_ATTRIBUTES = "DROP TABLE 'documentAttributes';";
	private static final String CREATE_TABLE_DOCUMENTS = "CREATE TABLE 'documents' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' TEXT NOT NULL, 'type' CHAR(50) NOT NULL, 'content' BLOB);";
	private static final String CREATE_TABLE_DOCUMENT_ATTRIBUTES = "CREATE TABLE 'documentAttributes' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'documentId' INTEGER NOT NULL, 'name' TEXT NOT NULL, 'type' CHAR(50) NOT NULL, 'stringValue' TEXT, 'integerValue' INTEGER);";

	private final String databaseUrl;

	public DocumentsDomainImpl(String databaseName) {
		databaseUrl = "jdbc:sqlite:" + databaseName;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#addAttribute(ru.ivanov.sitesoft_testcase.domain.DocumentAttribute)
	 */
	@Override
	public long addAttribute(DocumentAttribute attribute) throws SQLException {
		final long documentId = attribute.documentId();
		
		if (0 > documentId) {
			throw new IllegalArgumentException("no document id");
		}
		
		if (null == getDocument(documentId)) {
			throw new IllegalArgumentException("no document with this documentId");
		}
		
		final var name = attribute.name();
		
		if (null == name || name.isEmpty()) {
			throw new IllegalArgumentException("attribute name is undefined");
		}
		
		final var type = attribute.type();
		
		if (null == type || type.isEmpty()) {
			throw new IllegalArgumentException("attribute type is undefined");
		}

        try (var connection = DriverManager.getConnection(databaseUrl);
			 var preparedStatement = connection.prepareStatement(INSERT_DOCUMENT_ATTRIBUTE)){
            preparedStatement.setLong(1, documentId);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, type);
            preparedStatement.setString(4, attribute.stringValue());
            final var integerValue = attribute.integerValue();

            if (null != integerValue) {
                preparedStatement.setInt(5, integerValue);
            } else {
                preparedStatement.setNull(5, Types.INTEGER);
            }

            preparedStatement.executeUpdate();

            final var resultSet = preparedStatement.getGeneratedKeys();
            return resultSet.getLong(1);
        }

    }
	
	/*
	 * (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#addDocument(ru.ivanov.sitesoft_testcase.domain.Document, java.io.FileInputStream)
	 */
	@Override
	public void addDocument(Document document, InputStream inputStream) throws SQLException, IOException {
		try (var connection = DriverManager.getConnection(databaseUrl)) {
			connection.setAutoCommit(false);
			setDocumentContentInt(connection, addDocument(connection, document), inputStream);
			connection.commit();
		}
	}
	
	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#createDatabase()
	 */
	@Override
	public void createDatabase() throws SQLException {
		try (var connection = DriverManager.getConnection(databaseUrl);
			 var statement = connection.createStatement()) {
			try {
				statement.execute(DROP_TABLE_DOCUMENTS);
			} catch (SQLiteException e) {
				// if no such table exists
				System.out.println("Warning: " + e.getMessage());
			}

			try {
				statement.execute(DROP_TABLE_DOCUMENT_ATTRIBUTES);
			} catch (SQLiteException e) {
				// if no such table exists
				System.out.println("Warning: " + e.getMessage());
			}

			statement.execute(CREATE_TABLE_DOCUMENTS);
			statement.execute(CREATE_TABLE_DOCUMENT_ATTRIBUTES);
		}
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#createDocument()
	 */
	@Override
	public Document createDocument(String name, String type) {
        return new Document(-1, name, type);
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#createDocumentAttribute()
	 */
	@Override
	public DocumentAttribute createDocumentAttribute(long documentId, String name, String type, String stringValue, Integer integerValue) {
        return new DocumentAttribute(-1, documentId, name, type, stringValue, integerValue);
	}
	
	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#getDocument(long)
	 */
	@Override
	public Document getDocument(long documentId) throws SQLException {
        try (var connection = DriverManager.getConnection(databaseUrl);
			 var preparedStatement = connection.prepareStatement(GET_DOCUMENT_BY_ID)) {
            preparedStatement.setLong(1, documentId);
            final var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
				return getDocument(resultSet);
            }
        }

        return null;
	}
	
	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#getDocumentAttribute(long)
	 */
	@Override
	public DocumentAttribute getDocumentAttribute(long attributeId) throws SQLException {
		try (var connection = DriverManager.getConnection(databaseUrl);
			 var preparedStatement = connection.prepareStatement(GET_DOCUMENT_ATTRIBUTE_BY_ID)) {
			preparedStatement.setLong(1, attributeId);
			final var resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return getAttribute(resultSet);
			}
		}
		
		return null;
	}
	
	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#getDocumentAttributes(long)
	 */
	@Override
	public List<DocumentAttribute> getDocumentAttributes(long documentId) throws SQLException {
		var result = new ArrayList<DocumentAttribute>();

		try (var connection = DriverManager.getConnection(databaseUrl);
			 var preparedStatement = connection.prepareStatement(GET_ATTRIBUTES_FOR_DOCUMENT)) {
			preparedStatement.setLong(1, documentId);
			final var resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				var attribute = getAttribute(resultSet);
				result.add(attribute);
			}

			return result;
		}
	}

	/**
	 * Получить атрибут из текущей строки resultSet
	 * @param resultSet resultSet
	 * @return атрибут
	 * (@throws) SQLException
	 */
	private DocumentAttribute getAttribute(final ResultSet resultSet) throws SQLException {
        return new DocumentAttribute(resultSet.getLong(1), resultSet.getLong(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), (Integer) resultSet.getObject(6));
	}

	@Override
	public void putDocumentContentIntoOutputStream(long id, OutputStream outputStream) throws SQLException, IOException {
		try (var connection = DriverManager.getConnection(databaseUrl);
			 var preparedStatement = connection.prepareStatement(GET_DOCUMENT_CONTENT)) {
			preparedStatement.setLong(1, id);
			var resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				var is = resultSet.getBinaryStream(1);
				var buffer = new byte[4 * 1024];

				if (null != is) {
					int read;

					while (0 < (read = is.read(buffer))) {
						outputStream.write(buffer, 0, read);
					}
				}
			} else {
				throw new IOException("No such document");
			}
		}
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#getDocumentsList()
	 */
	@Override
	public List<Document> getDocumentsList() throws SQLException {
		try (var connection = DriverManager.getConnection(databaseUrl);
			 var statement = connection.createStatement()) {
			var result = new ArrayList<Document>();
			final var resultSet = statement.executeQuery(SELECT_DOCUMENTS);

			while (resultSet.next()) {
				var document = getDocument(resultSet);
				result.add(document);
			}

			return result;
		}
	}

	/**
	 * Получить документ из текущей строки resultSet
	 * @param resultSet результат запроса к базе данных
	 * @return документ
	 * (@throws) SQLException
	 */
	private Document getDocument(final ResultSet resultSet) throws SQLException {
        return new Document(resultSet.getLong(1), resultSet.getString(2), resultSet.getString(3));
	}
	
	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#removeAttribute(long)
	 */
	@Override
	public void removeAttribute(long attributeId) throws SQLException {
		try (var connection = DriverManager.getConnection(databaseUrl);
			 var preparedStatement = connection.prepareStatement(DELETE_ATTRIBUTE)) {
			preparedStatement.setLong(1, attributeId);
			preparedStatement.executeUpdate();
		}
	}
	
	private long addDocument(Connection connection, Document document) throws SQLException {
		final var name = document.name();
		
		if (null == name || name.isEmpty()) {
			throw new IllegalArgumentException("document name is undefined");
		}
		
		final var type = document.type();
		
		if (null == type || type.isEmpty()) {
			throw new IllegalArgumentException("document type is undefined");
		}

		try (var preparedStatement = connection.prepareStatement(INSERT_INTO_DOCUMENTS)) {
			preparedStatement.setString(1, name);
			preparedStatement.setString(2, type);
			preparedStatement.executeUpdate();

			var resultSet = preparedStatement.getGeneratedKeys();
			return resultSet.getLong(1);
		}
	}
	
	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#setDocumentContent(long, java.io.InputStream)
	 */
	@Override
	public void setDocumentContent(long documentId, InputStream inputStream) throws SQLException, IOException {
		try (var connection = DriverManager.getConnection(databaseUrl)) {
			setDocumentContentInt(connection, documentId, inputStream);
		}
	}

	private void setDocumentContentInt(Connection connection, long documentId, InputStream inputStream) throws SQLException, IOException {

		var os = new ByteArrayOutputStream();
		var buffer = new byte[4 * 1024];
		int len;

		while (0 < (len = inputStream.read(buffer))) {
			os.write(buffer, 0, len);
		}

		final var byteArray = os.toByteArray();
		setDocumentContent(connection, documentId, byteArray);
	}

	private void setDocumentContent(Connection connection, long documentId, final byte[] bytes) throws SQLException {
		try (var preparedStatement = connection.prepareStatement(UPDATE_DOCUMENT_CONTENT)) {
			preparedStatement.setBytes(1, bytes);
			preparedStatement.setLong(2, documentId);
			preparedStatement.executeUpdate();
		}
	}
	
	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#updateAttribute(ru.ivanov.sitesoft_testcase.domain.DocumentAttribute)
	 */
	@Override
	public void updateAttribute(DocumentAttribute attribute) throws SQLException {
		final var name = attribute.name();
		
		if (null == name || name.isEmpty()) {
			throw new IllegalArgumentException("attribute name is undefined");
		}
		
		final var type = attribute.type();
		
		if (null == type || type.isEmpty()) {
			throw new IllegalArgumentException("attribute type is undefined");
		}

        try (var connection = DriverManager.getConnection(databaseUrl);
			 var preparedStatement = connection.prepareStatement(UPDATE_ATTRIBUTE)){
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, type);
            preparedStatement.setString(3, attribute.stringValue());
            final var integerValue = attribute.integerValue();

            if (null != integerValue) {
                preparedStatement.setInt(4, integerValue);
            } else {
                preparedStatement.setNull(4, Types.INTEGER);
            }

            preparedStatement.setLong(5, attribute.id());
            preparedStatement.executeUpdate();
        }
    }
	
	@Override
	public void removeDocument(long documentId) throws SQLException {
		try (var connection = DriverManager.getConnection(databaseUrl);
			 var preparedStatement = connection.prepareStatement(DELETE_ATTRIBUTES_FOR_DOCUMENT);
			 var preparedStatement2 = connection.prepareStatement(DELETE_DOCUMENT)){
			connection.setAutoCommit(false);
			preparedStatement.setLong(1, documentId);
			preparedStatement.execute();

			preparedStatement2.setLong(1, documentId);
			preparedStatement2.execute();
			connection.commit();
		}
	}
}
