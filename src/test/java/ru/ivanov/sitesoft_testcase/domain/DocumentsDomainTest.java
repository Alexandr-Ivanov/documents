/**
 * Тестовое задание от sitesoft для Java
 */
package ru.ivanov.sitesoft_testcase.domain;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * тесты работы хранилища объектов
 * 
 * @author Alexandr Ivanlv
 *
 */
public class DocumentsDomainTest {
	@Before
	public void setup() throws SQLException {
		domain = new DocumentsDomainImpl("test");
		domain.createDatabase();		
	}
	
	@After
	public void teardown() throws SQLException {
		domain.cleanup();
	}
	
	@Test
	public void documentsTest() throws SQLException {
		final List<Document> documents = domain.getDocumentsList();
		Assert.assertEquals("documents quantity in empty database", 0, documents.size());
		
		final Document document = domain.createDocument();
		document.setIndex(INDEX + "2");
		document.setName(NAME + "2");
		document.setType(TYPE + "2");
		domain.addDocument(document);
		
		final List<Document> documents2 = domain.getDocumentsList();
		Assert.assertEquals("we must have 1 document", 01, documents2.size());
		
		final Document document1 = domain.createDocument();
		document1.setIndex(INDEX + "1");
		document1.setName(NAME + "1");
		document1.setType(TYPE + "1");
		domain.addDocument(document1);
		
		final List<Document> documents3 = domain.getDocumentsList();
		Assert.assertEquals("we must have 2 documents", 2, documents3.size());
		
		final Document document2 = documents3.get(0);
		Assert.assertEquals(INDEX + "1", document2.getIndex()); // order by index
	}
	
	@Test
	public void badDocumentTest() throws SQLException {
		final Document document = domain.createDocument();
		document.setName(NAME);
		document.setType(TYPE);
		
		try {
			domain.addDocument(document);
			Assert.fail("empty index");
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("document index is undefined", e.getMessage());
		}
		
		document.setIndex(INDEX);
		document.setName(null);
		
		try {
			domain.addDocument(document);
			Assert.fail("empty name");
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("document name is undefined", e.getMessage());
		}
		
		document.setName(NAME);
		document.setType(null);
		
		try {
			domain.addDocument(document);
			Assert.fail("empty type");
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("document type is undefined", e.getMessage());
		}
	}
	
	@Test
	public void documentContentTest() throws SQLException, IOException {
		Document document = domain.createDocument();
		document.setIndex(INDEX);
		document.setName(NAME);
		document.setType(TYPE);
		long newId = domain.addDocument(document);
		Assert.assertTrue("id must be greater then 0", 0 < newId);
		byte[] bytes = new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
		domain.setDocumentContent(newId, new ByteArrayInputStream(bytes));
		
		InputStream inputStream = domain.getDocumentContent(newId);
		Assert.assertNotNull("content", inputStream);
		
		byte[] newBytes = new byte[bytes.length];
		int len = inputStream.read(newBytes);
		Assert.assertEquals("read bytes number",bytes.length, len);
		Assert.assertArrayEquals("read bytes", bytes, newBytes);
		
		Assert.assertEquals("end of stream", -1, inputStream.read());
	}
	
	@Test
	public void documentAttributeTest() throws SQLException {
		Document document = domain.createDocument();
		document.setIndex(INDEX);
		document.setName(NAME);
		document.setType(TYPE);
		long newId = domain.addDocument(document);
		final List<DocumentAttribute> attributes = domain.getDocumentAttributes(newId);
		Assert.assertTrue("new document has no attributes", attributes.isEmpty());
		
		final DocumentAttribute attribute = domain.createDocumentAttribute();

		try {
			domain.addAttribute(attribute);
			Assert.fail("added attribute without documentId");
		} catch (IllegalArgumentException e) {
		}
		
		try {
			attribute.setDocumentId(newId);
			domain.addAttribute(attribute);
			Assert.fail("added attribute without name, type");
		} catch (Exception e) {
		}
		
		attribute.setName(NAME);
		attribute.setType(TYPE);
		attribute.setString(STRING_VALUE);
		long attributeId = domain.addAttribute(attribute);
		Assert.assertTrue("attribute id is positive", 0 < attributeId);
		
		final DocumentAttribute gainedAttribute = domain.getDocumentAttribute(attributeId);
		Assert.assertNotNull("attribute was found", gainedAttribute);
		Assert.assertEquals(attributeId, gainedAttribute.getId());
		Assert.assertEquals(newId, gainedAttribute.getDocumentId());
		Assert.assertEquals(NAME, gainedAttribute.getName());
		Assert.assertEquals(TYPE, gainedAttribute.getType());
		Assert.assertEquals(STRING_VALUE, gainedAttribute.getStringValue());
		Assert.assertNull(gainedAttribute.getIntegerValue());
		
		final List<DocumentAttribute> attributes2 = domain.getDocumentAttributes(newId);
		Assert.assertEquals("document has one attributes", 1, attributes2.size());
		
		final DocumentAttribute attribute2 = domain.createDocumentAttribute();
		attribute2.setDocumentId(newId);
		attribute2.setName(NAME);
		attribute2.setType(TYPE);
		attribute2.setInteger(1);
		long attribute2Id = domain.addAttribute(attribute2);
		Assert.assertTrue("second id greater", attributeId < attribute2Id);
		
		final DocumentAttribute gainedAttribute2 = domain.getDocumentAttribute(attribute2Id);
		Assert.assertNull(gainedAttribute2.getStringValue());
		Assert.assertEquals(1, gainedAttribute2.getIntegerValue().intValue());
		
		final List<DocumentAttribute> attributes3 = domain.getDocumentAttributes(newId);
		Assert.assertEquals("document has two attributes", 2, attributes3.size());
		
		domain.removeAttribute(attributeId);
		Assert.assertNull("it no longer exists", domain.getDocumentAttribute(attributeId));
		final List<DocumentAttribute> attributes4 = domain.getDocumentAttributes(newId);
		Assert.assertEquals("one attribute remains", 1, attributes4.size());
		
		final DocumentAttribute attribute3 = attributes4.get(0);
		attribute3.setName(NAME_1);
		attribute3.setType(TYPE_1);
		attribute3.setString(STRING_VALUE_1);
		attribute3.setInteger(2);
		domain.updateAttribute(attribute3);
		
		final DocumentAttribute gainedAttribute3 = domain.getDocumentAttribute(attribute3.getId());
		Assert.assertEquals(NAME_1, gainedAttribute3.getName());
		Assert.assertEquals(TYPE_1, gainedAttribute3.getType());
		Assert.assertEquals(STRING_VALUE_1, gainedAttribute3.getStringValue());
		Assert.assertEquals(2, gainedAttribute3.getIntegerValue().intValue());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void unexistingDocument() throws SQLException {
		final Document document = domain.createDocument();
		document.setIndex(INDEX);
		document.setName(NAME);
		document.setType(TYPE);
		long documentId = domain.addDocument(document);
		
		while (null != domain.getDocument(documentId)) { // находим идентификатор, не принадлежащий никакому документу
			documentId++;
		}
		
		DocumentAttribute attribute = domain.createDocumentAttribute();
		attribute.setDocumentId(documentId);
		attribute.setName(NAME);
		attribute.setType(TYPE);
		domain.addAttribute(attribute);
	}
	
	@Test
	public void removeDocumentTest() throws SQLException {
		final Document document = domain.createDocument();
		document.setIndex(INDEX);
		document.setName(NAME);
		document.setType(TYPE);
		long id = domain.addDocument(document);
		Assert.assertNotNull(domain.getDocument(id));
		
		DocumentAttribute attribute = domain.createDocumentAttribute();
		attribute.setDocumentId(id);
		attribute.setName(NAME);
		attribute.setType(TYPE);
		domain.addAttribute(attribute);
		Assert.assertFalse(domain.getDocumentAttributes(id).isEmpty());
		
		domain.removeDocument(id);
		Assert.assertNull(domain.getDocument(id));
		Assert.assertTrue(domain.getDocumentAttributes(id).isEmpty());
	}
	
	private static final String INDEX = "index";
	private static final String NAME = "name";
	private static final String NAME_1 = "name 1";
	private static final String TYPE = "type";
	private static final String TYPE_1 = "type 1";
	private DocumentsDomain domain;
	private static final String STRING_VALUE = "string value";
	private static final String STRING_VALUE_1 = "string value 1";
}
