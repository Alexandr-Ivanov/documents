/**
 * Тестовое задание от sitesoft для Java
 */
package ru.ivanov.sitesoft_testcase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.ivanov.sitesoft_testcase.domain.Document;
import ru.ivanov.sitesoft_testcase.domain.DocumentAttribute;
import ru.ivanov.sitesoft_testcase.domain.DocumentAttributeImpl;
import ru.ivanov.sitesoft_testcase.domain.DocumentImpl;
import ru.ivanov.sitesoft_testcase.domain.DocumentsDomain;

/**
 * @author Alexandr Ivanov
 *
 */
public class DocumentsDomainTestImpl implements DocumentsDomain {

	public byte[] content;
	public DocumentAttribute attribute;
	public long attributeId;
	public long documentId;
	public String calledMethod;

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#cleanup()
	 */
	@Override
	public void cleanup() throws SQLException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#addAttribute(ru.ivanov.sitesoft_testcase.domain.DocumentAttribute)
	 */
	@Override
	public long addAttribute(DocumentAttribute attribute) throws SQLException {
		maxDocumentAttributeId++;
		attribute.setId(maxDocumentAttributeId);
		this.attribute = attribute;
		calledMethod = "addAttribute";
		return maxDocumentAttributeId;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#createDatabase()
	 */
	@Override
	public void createDatabase() throws SQLException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#createDocument()
	 */
	@Override
	public Document createDocument() {
		// TODO Auto-generated method stub
		return new DocumentImpl();
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#createDocumentAttribute()
	 */
	@Override
	public DocumentAttribute createDocumentAttribute() {
		return new DocumentAttributeImpl();
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#getDocument(long)
	 */
	@Override
	public Document getDocument(long documentId) throws SQLException {
		calledMethod = "getDocument";
		this.documentId = documentId;
		return null;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#getDocumentAttribute(long)
	 */
	@Override
	public DocumentAttribute getDocumentAttribute(long attributeId) throws SQLException {
		final DocumentAttribute documentAttribute = createDocumentAttribute();
		documentAttribute.setId(attributeId);
		documentAttribute.setName("name");
		documentAttribute.setType("type");
		return documentAttribute;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#getDocumentAttributes(long)
	 */
	@Override
	public List<DocumentAttribute> getDocumentAttributes(long documentId) throws SQLException {
		calledMethod = "getDocumentAttributes";
		this.documentId = documentId; 
		List<DocumentAttribute> attributes = new ArrayList<>();
		final DocumentAttribute attribute = createDocumentAttribute();
		attribute.setDocumentId(documentId);
		attribute.setId(1);
		attribute.setInteger(2);
		attribute.setName("name");
		attribute.setString("string");
		attribute.setType("type");
		attributes.add(attribute);
		return attributes;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#getDocumentContent(long)
	 */
	@Override
	public InputStream getDocumentContent(long id) throws SQLException {
		calledMethod = "getDocumentContent";
		return new ByteArrayInputStream(content);
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#getDocumentsList()
	 */
	@Override
	public List<Document> getDocumentsList() throws SQLException {
		final Document document = createDocument();
		document.setIndex("index");
		document.setName("name");
		document.setType("type");
		addDocument(document);
		calledMethod = "getDocumentsList";
		return Collections.unmodifiableList(documents);
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#removeAttribute(long)
	 */
	@Override
	public void removeAttribute(long attributeId) throws SQLException {
		calledMethod = "removeAttribute";
		this.attributeId = attributeId;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#addDocument(ru.ivanov.sitesoft_testcase.domain.Document)
	 */
	@Override
	public long addDocument(Document document) throws SQLException {
		calledMethod = "addDocument";
		maxDocumentId++;
		document.setId(maxDocumentId);
		documents.add(document);
		return maxDocumentId;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#setDocumentContent(long, java.io.InputStream)
	 */
	@Override
	public void setDocumentContent(long documentId, InputStream inputStream) throws SQLException, IOException {
		// TODO Auto-generated method stub
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024 * 1024];
		int read;
		
		while (0 < (read = inputStream.read(buffer))) {
			outputStream.write(buffer, 0, read);
		};
		
		content = outputStream.toByteArray();
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#setDocumentContent(long, byte[])
	 */
	@Override
	public void setDocumentContent(long documentId, byte[] bytes) throws SQLException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#updateAttribute(ru.ivanov.sitesoft_testcase.domain.DocumentAttribute)
	 */
	@Override
	public void updateAttribute(DocumentAttribute attribute) throws SQLException {
		calledMethod = "updateAttribute";
		this.attribute = attribute; 
	}

	@Override
	public void removeDocument(long documentId) throws SQLException {
		calledMethod = "removeDocument";
		this.documentId = documentId;
	}

	@Override
	public void addDocument(Document document, FileInputStream inputStream) throws SQLException, IOException {
		setDocumentContent(addDocument(document), inputStream);
	}

	private int maxDocumentId;
	private int maxDocumentAttributeId;
	private List<Document> documents = new ArrayList<>();
}
