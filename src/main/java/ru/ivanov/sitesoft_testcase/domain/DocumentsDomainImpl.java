/**
 * Тестовое задание от sitesoft для Java
 */
package ru.ivanov.sitesoft_testcase.domain;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
	public DocumentsDomainImpl(String databaseName) throws SQLException {
		connection = DriverManager.getConnection("jdbc:sqlite:" + databaseName);
		statement = connection.createStatement();
	}
	
	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#cleanup()
	 */
	@Override
	public void cleanup() throws SQLException {
		connection.close();
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#addAttribute(ru.ivanov.sitesoft_testcase.domain.DocumentAttribute)
	 */
	@Override
	public long addAttribute(DocumentAttribute attribute) throws SQLException {
		final long documentId = attribute.getDocumentId();
		
		if (0 > documentId) {
			throw new IllegalArgumentException("no document id");
		}
		
		if (null == getDocument(documentId)) {
			throw new IllegalArgumentException("no document with this documentId");
		}
		
		final String name = attribute.getName();
		
		if (null == name || name.isEmpty()) {
			throw new IllegalArgumentException("attribute name is undefined");
		}
		
		final String type = attribute.getType();
		
		if (null == type || type.isEmpty()) {
			throw new IllegalArgumentException("attribute type is undefined");
		}
		
		final PreparedStatement preparedStatement = connection.prepareStatement(INSERT_DOCUMENT_ATTRIBUTE);
		preparedStatement.setLong(1, documentId);
		preparedStatement.setString(2, name);
		preparedStatement.setString(3, type);
		preparedStatement.setString(4, attribute.getStringValue());		
		final Integer integerValue = attribute.getIntegerValue();
		
		if (null != integerValue) {
			preparedStatement.setInt(5, integerValue);
		} else {
			preparedStatement.setNull(5, Types.INTEGER);
		}
		
		preparedStatement.executeUpdate();
		
		final ResultSet resultSet = preparedStatement.getGeneratedKeys();
		long result = resultSet.getLong(1);
		return result;
		
 	}
	
	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#createDatabase()
	 */
	@Override
	public void createDatabase() throws SQLException {
		try {
			statement.execute("DROP TABLE 'documents';");
		} catch (SQLiteException e) {
			// if no such table exists
		}
		
		try {
			statement.execute("DROP TABLE 'documentAttributes';");
		} catch (SQLiteException e) {
			// if no such table exists
		}
		
		statement.execute("CREATE TABLE 'documents' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, '_index' CHAR(50) NOT NULL, 'name' TEXT NOT NULL, 'type' CHAR(50) NOT NULL, 'content' BLOB);");
		statement.execute("CREATE TABLE 'documentAttributes' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'documentId' INTEGER NOT NULL, 'name' TEXT NOT NULL, 'type' CHAR(50) NOT NULL, 'stringValue' TEXT, 'integerValue' INTEGER);");
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#createDocument()
	 */
	@Override
	public Document createDocument() {
		final Document document = new DocumentImpl();
		document.setId(-1L);
		return document;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#createDocumentAttribute()
	 */
	@Override
	public DocumentAttribute createDocumentAttribute() {
		final DocumentAttribute attribute = new DocumentAttributeImpl();
		attribute.setId(-1);
		attribute.setDocumentId(-1);
		return attribute;
	}
	
	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#getDocument(long)
	 */
	@Override
	public Document getDocument(long documentId) throws SQLException {
		final PreparedStatement preparedStatement = connection.prepareStatement(GET_DOCUMENT_BY_ID);
		preparedStatement.setLong(1, documentId);
		final ResultSet resultSet = preparedStatement.executeQuery();
		
		if (resultSet.next()) {
			Document document = getDocument(resultSet);
			return document;
		}
		
		return null;
	}
	
	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#getDocumentAttribute(long)
	 */
	@Override
	public DocumentAttribute getDocumentAttribute(long attributeId) throws SQLException {
		final PreparedStatement preparedStatement = connection.prepareStatement(GET_DOCUMENT_ATTRIBUTE_BY_ID);
		preparedStatement.setLong(1, attributeId);
		final ResultSet resultSet = preparedStatement.executeQuery();
		
		if (resultSet.next()) {
			DocumentAttribute attribute = getAttribute(resultSet);
			return attribute;
		}
		
		return null;
	}
	
	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#getDocumentAttributes(long)
	 */
	@Override
	public List<DocumentAttribute> getDocumentAttributes(long documentId) throws SQLException {
		List<DocumentAttribute> result = new ArrayList<>();
		final PreparedStatement preparedStatement = connection.prepareStatement(GET_ATTRIBUTES_FOR_DOCUMENT);
		preparedStatement.setLong(1, documentId);
		final ResultSet resultSet = preparedStatement.executeQuery();
		
		while (resultSet.next()) {
			DocumentAttribute attribute = getAttribute(resultSet);
			result.add(attribute);
		}
		
		return result;
	}

	/**
	 * Получить атрибут из текущей строки resultSet
	 * @param resultSet
	 * @return атрибут
	 * @throws SQLException
	 */
	private DocumentAttribute getAttribute(final ResultSet resultSet) throws SQLException {
		DocumentAttribute attribute = createDocumentAttribute();
		attribute.setId(resultSet.getLong(1));
		attribute.setDocumentId(resultSet.getLong(2));
		attribute.setName(resultSet.getString(3));
		attribute.setType(resultSet.getString(4));
		attribute.setString(resultSet.getString(5));
		attribute.setInteger((Integer) resultSet.getObject(6));
		return attribute;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#getDocumentContent(long)
	 */
	@Override
	public InputStream getDocumentContent(long id) throws SQLException {
		final PreparedStatement preparedStatement = connection.prepareStatement(GET_DOCUMENT_CONTENT);
		preparedStatement.setLong(1, id);
		final ResultSet resultSet = preparedStatement.executeQuery();
		
		if (resultSet.next()) {
			return resultSet.getBinaryStream(1);
		}
		
		return null;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#getDocumentsList()
	 */
	@Override
	public List<Document> getDocumentsList() throws SQLException {
		List<Document> result = new ArrayList<>();
		final ResultSet resultSet = statement.executeQuery("SELECT id, _index, name, type FROM documents ORDER BY _index ASC;");
		
		while (resultSet.next()) {
			Document document = getDocument(resultSet);
			result.add(document);
		}
		
		return result;
	}

	/**
	 * Получить документ из текущей строки resultSet
	 * @param resultSet
	 * @return документ
	 * @throws SQLException
	 */
	private Document getDocument(final ResultSet resultSet) throws SQLException {
		Document document = createDocument();
		document.setId(resultSet.getLong(1));
		document.setIndex(resultSet.getString(2));
		document.setName(resultSet.getString(3));
		document.setType(resultSet.getString(4));
		return document;
	}
	
	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#removeAttribute(long)
	 */
	@Override
	public void removeAttribute(long attributeId) throws SQLException {
		final PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ATTRIBUTE);
		preparedStatement.setLong(1, attributeId);
		preparedStatement.executeUpdate();
	}
	
	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#addDocument(ru.ivanov.sitesoft_testcase.domain.Document)
	 */
	@Override
	public long addDocument(Document document) throws SQLException {
		final String index = document.getIndex();
		
		if (null == index || index.isEmpty()) {
			throw new IllegalArgumentException("document index is undefined");
		}
		
		final String name = document.getName();
		
		if (null == name || name.isEmpty()) {
			throw new IllegalArgumentException("document name is undefined");
		}
		
		final String type = document.getType();
		
		if (null == type || type.isEmpty()) {
			throw new IllegalArgumentException("document type is undefined");
		}
		
		final PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTO_DOCUMENTS);
		preparedStatement.setString(1, index);
		preparedStatement.setString(2, name);
		preparedStatement.setString(3, type);
		preparedStatement.executeUpdate();
		
		final ResultSet resultSet = preparedStatement.getGeneratedKeys();
		long result = resultSet.getLong(1);
		return result;
	}
	
	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#setDocumentContent(long, java.io.InputStream)
	 */
	@Override
	public void setDocumentContent(long documentId, InputStream inputStream) throws SQLException, IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024 * 1024];
		int len = 0;
		
		while (0 < (len = inputStream.read(buffer))) {
			os.write(buffer, 0, len);
		}
		
		final byte[] byteArray = os.toByteArray();
		setDocumentContent(documentId, byteArray);
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#setDocumentContent(long, byte[])
	 */
	@Override
	public void setDocumentContent(long documentId, final byte[] bytes) throws SQLException {
		final PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_DOCUMENT_CONTENT);
		preparedStatement.setBytes(1, bytes);
		preparedStatement.setLong(2, documentId);
		preparedStatement.executeUpdate();
	}
	
	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#updateAttribute(ru.ivanov.sitesoft_testcase.domain.DocumentAttribute)
	 */
	@Override
	public void updateAttribute(DocumentAttribute attribute) throws SQLException {
		final String name = attribute.getName();
		
		if (null == name || name.isEmpty()) {
			throw new IllegalArgumentException("attribute name is undefined");
		}
		
		final String type = attribute.getType();
		
		if (null == type || type.isEmpty()) {
			throw new IllegalArgumentException("attribute type is undefined");
		}
		
		final PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ATTRIBUTE);
		preparedStatement.setString(1, name);
		preparedStatement.setString(2, type);
		preparedStatement.setString(3, attribute.getStringValue());
		final Integer integerValue = attribute.getIntegerValue();
		
		if (null != integerValue) {
			preparedStatement.setInt(4, integerValue);
		} else {
			preparedStatement.setNull(4, Types.INTEGER);
		}

		preparedStatement.setLong(5, attribute.getId());
		preparedStatement.executeUpdate();
	}
	
	@Override
	public void removeDocument(long documentId) throws SQLException {
		final boolean oldAutoCommit = connection.getAutoCommit();
		
		try {
			connection.setAutoCommit(false);
			final PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ATTRIBUTES_FOR_DOCUMENT);
			preparedStatement.setLong(1, documentId);
			preparedStatement.execute();
			
			final PreparedStatement preparedStatement2 = connection.prepareStatement(DELETE_DOCUMENT);
			preparedStatement2.setLong(1, documentId);
			preparedStatement2.execute();
			connection.commit();
		} finally {
			connection.setAutoCommit(oldAutoCommit);
		}
	}
	
	private Connection connection;
	private Statement statement;
	private static final String INSERT_INTO_DOCUMENTS = "INSERT INTO documents (_index,name,type) VALUES (?,?,?);";
	private static final String UPDATE_DOCUMENT_CONTENT = "UPDATE documents SET content=? WHERE id=?";
	private static final String GET_ATTRIBUTES_FOR_DOCUMENT = "SELECT id,documentId,name,type,stringValue,integerValue FROM documentAttributes where documentId=?";
	private static final String GET_DOCUMENT_CONTENT = "SELECT content FROM documents WHERE id=?;";
	private static final String INSERT_DOCUMENT_ATTRIBUTE = "INSERT INTO documentAttributes (documentId,name,type,stringValue,integerValue) VALUES (?,?,?,?,?);";
	private static final String GET_DOCUMENT_ATTRIBUTE_BY_ID = "SELECT id,documentId,name,type,stringValue,integerValue FROM documentAttributes WHERE id=?;";
	private static final String GET_DOCUMENT_BY_ID = "SELECT id, _index, name, type FROM documents WHERE id=?;";
	private static final String UPDATE_ATTRIBUTE = "UPDATE documentAttributes SET name=?,type=?,stringValue=?,integerValue=? WHERE id=?;";
	private static final String DELETE_ATTRIBUTE = "DELETE FROM documentAttributes WHERE id=?;";
	private static final String DELETE_ATTRIBUTES_FOR_DOCUMENT = "DELETE FROM documentAttributes WHERE documentId=?;";
	private static final String DELETE_DOCUMENT = "DELETE FROM documents WHERE id=?;";
}
