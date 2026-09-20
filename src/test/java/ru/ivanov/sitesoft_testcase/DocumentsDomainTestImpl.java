/**
 * Тестовое задание от sitesoft для Java
 */
package ru.ivanov.sitesoft_testcase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.ivanov.sitesoft_testcase.domain.Document;
import ru.ivanov.sitesoft_testcase.domain.DocumentAttribute;
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
	public void cleanup() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#addAttribute(ru.ivanov.sitesoft_testcase.domain.DocumentAttribute)
	 */
	@Override
	public long addAttribute(DocumentAttribute attribute) {
		maxDocumentAttributeId++;
		this.attribute = new DocumentAttribute(maxDocumentAttributeId, attribute.documentId(), attribute.name(), attribute.type(), attribute.stringValue(), attribute.integerValue());
		calledMethod = "addAttribute";
		return maxDocumentAttributeId;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#createDatabase()
	 */
	@Override
	public void createDatabase() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#createDocument()
	 */
	@Override
	public Document createDocument(String name, String type) {
		// TODO Auto-generated method stub
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
	public Document getDocument(long documentId) {
		calledMethod = "getDocument";
		this.documentId = documentId;
		return null;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#getDocumentAttribute(long)
	 */
	@Override
	public DocumentAttribute getDocumentAttribute(long attributeId) {
        return new DocumentAttribute(attributeId, 1, "name", "type", "", null);
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#getDocumentAttributes(long)
	 */
	@Override
	public List<DocumentAttribute> getDocumentAttributes(long documentId) {
		calledMethod = "getDocumentAttributes";
		this.documentId = documentId; 
		List<DocumentAttribute> attributes = new ArrayList<>();
		final DocumentAttribute attribute = new DocumentAttribute(1, documentId, "name", "type", "string", 2);
		attributes.add(attribute);
		return attributes;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#getDocumentContent(long)
	 */
	@Override
	public InputStream getDocumentContent(long id) {
		calledMethod = "getDocumentContent";
		return new ByteArrayInputStream(content);
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#getDocumentsList()
	 */
	@Override
	public List<Document> getDocumentsList() {
		calledMethod = "getDocumentsList";
		return Collections.unmodifiableList(documents);
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#removeAttribute(long)
	 */
	@Override
	public void removeAttribute(long attributeId) {
		calledMethod = "removeAttribute";
		this.attributeId = attributeId;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#addDocument(ru.ivanov.sitesoft_testcase.domain.Document)
	 */
	@Override
	public long addDocument(Document document) {
		calledMethod = "addDocument";
		maxDocumentId++;
		documents.add(new Document(maxDocumentId, document.name(), document.type()));
		return maxDocumentId;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#setDocumentContent(long, java.io.InputStream)
	 */
	@Override
	public void setDocumentContent(long documentId, InputStream inputStream) throws IOException {
		// TODO Auto-generated method stub
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024 * 1024];
		int read;
		
		while (0 < (read = inputStream.read(buffer))) {
			outputStream.write(buffer, 0, read);
		}
		
		content = outputStream.toByteArray();
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#setDocumentContent(long, byte[])
	 */
	@Override
	public void setDocumentContent(long documentId, byte[] bytes) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentsDomain#updateAttribute(ru.ivanov.sitesoft_testcase.domain.DocumentAttribute)
	 */
	@Override
	public void updateAttribute(DocumentAttribute attribute) {
		calledMethod = "updateAttribute";
		this.attribute = attribute; 
	}

	@Override
	public void removeDocument(long documentId) {
		calledMethod = "removeDocument";
		this.documentId = documentId;
	}

	@Override
	public void addDocument(Document document, FileInputStream inputStream) throws IOException {
		setDocumentContent(addDocument(document), inputStream);
	}

	private int maxDocumentId;
	private int maxDocumentAttributeId;
	private final List<Document> documents = new ArrayList<>();
}
