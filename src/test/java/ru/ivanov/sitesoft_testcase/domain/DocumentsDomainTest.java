/**
 * Тестовое задание от sitesoft для Java
 */
package ru.ivanov.sitesoft_testcase.domain;

import java.io.*;
import java.sql.SQLException;

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
	public void documentsTest() throws SQLException, IOException {
		final var documents = domain.getDocumentsList();
		Assert.assertEquals("documents quantity in empty database", 0, documents.size());
		
		final var document = domain.createDocument(NAME + "2", TYPE + "2");

        domain.addDocument(document, new ByteArrayInputStream(CONTENT));
		
		final var documents2 = domain.getDocumentsList();
		Assert.assertEquals("we must have 1 document", 1, documents2.size());
		
		final var document1 = domain.createDocument(NAME + "1", TYPE + "1");
		domain.addDocument(document1, new ByteArrayInputStream(CONTENT));
		
		final var documents3 = domain.getDocumentsList();
		Assert.assertEquals("we must have 2 documents", 2, documents3.size());
		
		final var document2 = documents3.getLast();
		Assert.assertEquals(NAME + "1", document2.name());
	}
	
	@Test
	public void badDocumentTest() throws SQLException, IOException {
		var document = domain.createDocument(null,TYPE);
		
		try {
			domain.addDocument(document, new ByteArrayInputStream(CONTENT));
			Assert.fail("empty name");
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("document name is undefined", e.getMessage());
		}
		
		document = domain.createDocument(NAME, null);
		
		try {
			domain.addDocument(document, new ByteArrayInputStream(CONTENT));
			Assert.fail("empty type");
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("document type is undefined", e.getMessage());
		}
	}
	
	@Test
	public void documentContentTest() throws SQLException, IOException {
		var document = domain.createDocument(NAME, TYPE);
		domain.addDocument(document, new ByteArrayInputStream(CONTENT));
		var documents = domain.getDocumentsList();
		var newId = documents.getLast().id();

        var outputStream = new ByteArrayOutputStream();
		domain.putDocumentContentIntoOutputStream(newId, outputStream);
		Assert.assertNotNull("content", outputStream);
		
		var newBytes = outputStream.toByteArray();
        Assert.assertEquals("read bytes number", CONTENT.length, newBytes.length);
		Assert.assertArrayEquals("read bytes", CONTENT, newBytes);
	}
	
	@Test
	public void documentAttributeTest() throws SQLException, IOException {
		var document = domain.createDocument(NAME, TYPE);
        domain.addDocument(document, new ByteArrayInputStream(CONTENT));
		var documents = domain.getDocumentsList();
		var newId = documents.getLast().id();
		final var attributes = domain.getDocumentAttributes(newId);
		Assert.assertTrue("new document has no attributes", attributes.isEmpty());
		
		var attribute = domain.createDocumentAttribute(-1, null, null, null, null);

		try {
			domain.addAttribute(attribute);
			Assert.fail("added attribute without documentId");
		} catch (IllegalArgumentException _) {
		}
		
		try {
			attribute = domain.createDocumentAttribute(newId, null, null, null, null);
			domain.addAttribute(attribute);
			Assert.fail("added attribute without name, type");
		} catch (Exception _) {
		}

		attribute = domain.createDocumentAttribute(newId, NAME, TYPE, STRING_VALUE, null);
		long attributeId = domain.addAttribute(attribute);
		Assert.assertTrue("attribute id is positive", 0 < attributeId);
		
		final var gainedAttribute = domain.getDocumentAttribute(attributeId);
		Assert.assertNotNull("attribute was found", gainedAttribute);
		Assert.assertEquals(attributeId, gainedAttribute.id());
		Assert.assertEquals(newId, gainedAttribute.documentId());
		Assert.assertEquals(NAME, gainedAttribute.name());
		Assert.assertEquals(TYPE, gainedAttribute.type());
		Assert.assertEquals(STRING_VALUE, gainedAttribute.stringValue());
		Assert.assertNull(gainedAttribute.integerValue());
		
		final var attributes2 = domain.getDocumentAttributes(newId);
		Assert.assertEquals("document has one attributes", 1, attributes2.size());
		
		final var attribute2 = domain.createDocumentAttribute(newId, NAME, TYPE, null, 1);
		long attribute2Id = domain.addAttribute(attribute2);
		Assert.assertTrue("second id greater", attributeId < attribute2Id);
		
		final var gainedAttribute2 = domain.getDocumentAttribute(attribute2Id);
		Assert.assertNull(gainedAttribute2.stringValue());
		Assert.assertEquals(1, gainedAttribute2.integerValue().intValue());
		
		final var attributes3 = domain.getDocumentAttributes(newId);
		Assert.assertEquals("document has two attributes", 2, attributes3.size());
		
		domain.removeAttribute(attributeId);
		Assert.assertNull("it no longer exists", domain.getDocumentAttribute(attributeId));
		final var attributes4 = domain.getDocumentAttributes(newId);
		Assert.assertEquals("one attribute remains", 1, attributes4.size());
		
		final var attribute3 = attributes4.getFirst();
		domain.updateAttribute(new DocumentAttribute(attribute3.id(), attribute3.documentId(), NAME_1, TYPE_1, STRING_VALUE_1, 2));
		
		final var gainedAttribute3 = domain.getDocumentAttribute(attribute3.id());
		Assert.assertEquals(NAME_1, gainedAttribute3.name());
		Assert.assertEquals(TYPE_1, gainedAttribute3.type());
		Assert.assertEquals(STRING_VALUE_1, gainedAttribute3.stringValue());
		Assert.assertEquals(2, gainedAttribute3.integerValue().intValue());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void unexistingDocument() throws SQLException, IOException {
		final var document = domain.createDocument(NAME, TYPE);
		domain.addDocument(document, new ByteArrayInputStream(CONTENT));
		long documentId = domain.getDocumentsList().getLast().id();

		
		while (null != domain.getDocument(documentId)) { // находим идентификатор, не принадлежащий никакому документу
			documentId++;
		}
		
		var attribute = domain.createDocumentAttribute(documentId, NAME, TYPE, "", null);
		domain.addAttribute(attribute);
	}
	
	@Test
	public void removeDocumentTest() throws SQLException, IOException {
		final var document = domain.createDocument(NAME, TYPE);
		domain.addDocument(document, new ByteArrayInputStream(CONTENT));
		long id = domain.getDocumentsList().stream().filter(doc -> NAME.equals(doc.name())).findFirst().map(Document::id).orElse(0L);

		Assert.assertNotEquals(0, id);
		
		var attribute = domain.createDocumentAttribute(id, NAME, TYPE, "", null);
		domain.addAttribute(attribute);
		Assert.assertFalse(domain.getDocumentAttributes(id).isEmpty());
		
		domain.removeDocument(id);
		Assert.assertNull(domain.getDocument(id));
		Assert.assertTrue(domain.getDocumentAttributes(id).isEmpty());
	}

	private static final byte[] CONTENT = new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9};
    private static final String NAME = "name";
	private static final String NAME_1 = "name 1";
	private static final String TYPE = "type";
	private static final String TYPE_1 = "type 1";
	private DocumentsDomain domain;
	private static final String STRING_VALUE = "string value";
	private static final String STRING_VALUE_1 = "string value 1";
}
