/**
 * Тестовое задание от sitesoft для Java
 */
package ru.ivanov.sitesoft_testcase;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ru.ivanov.sitesoft_testcase.domain.Document;

/**
 * @author Alexandr Ivanov
 *
 */
public class CommandProcessorTest {
	@Before
	public void setup() {
		domain = new DocumentsDomainTestImpl();
		commandProcessor = new CommandProcessor(domain);
	}
	
	@Test
	public void quitTest() {
		final CommandProcessor commandProcessor = new CommandProcessor(null);
		Assert.assertFalse(commandProcessor.process(new String[] {"quit"}));
	}
	
	@Test
	public void documentsTest() throws SQLException {
		Assert.assertTrue(commandProcessor.process(new String[]{"documents"}));
		Assert.assertEquals("getDocumentsList", domain.calledMethod);
	}
	
	@Test
	public void addDocumentTest() throws SQLException, IOException {
		final Path path = Files.createTempFile(null, null);
		FileOutputStream outputStream = new FileOutputStream(path.toFile());
		outputStream.write(STRING.getBytes());
		outputStream.close();
		Assert.assertTrue(commandProcessor.process(new String[]{"add", "document", INDEX, NAME, TYPE, path.toString()}));
		Assert.assertEquals("addDocument", domain.calledMethod);
		final List<Document> documents = domain.getDocumentsList();
		final Document document = documents.get(documents.size() - 1);
		Assert.assertEquals(INDEX, document.getIndex());
		Assert.assertEquals(NAME, document.getName());
		Assert.assertEquals(TYPE, document.getType());
		Assert.assertTrue(Arrays.equals(path.toString().getBytes(), domain.content));
		
		domain.calledMethod = null;
		Assert.assertTrue(commandProcessor.process(new String[]{"add", "document", INDEX}));
		Assert.assertNull(domain.calledMethod);
	}

	@Test
	public void getDocumentTest() throws SQLException {
		Assert.assertTrue(commandProcessor.process(new String[]{"document", "11"}));
		Assert.assertEquals("getDocument", domain.calledMethod);
		Assert.assertEquals(11, domain.documentId);
		
		domain.calledMethod = null;
		Assert.assertTrue(commandProcessor.process(new String[]{"document"}));
		Assert.assertNull(domain.calledMethod);
	}
	
	@Test
	public void removeDocumentTest() throws SQLException {
		Assert.assertTrue(commandProcessor.process(new String[]{"remove", "document", "12"}));
		Assert.assertEquals("removeDocument", domain.calledMethod);
		Assert.assertEquals(12, domain.documentId);
		
		domain.calledMethod = null;
		Assert.assertTrue(commandProcessor.process(new String[]{"remove", "document"}));
		Assert.assertNull(domain.calledMethod);
	}
	
	@Test
	public void attributesTest() throws SQLException {
		Assert.assertTrue(commandProcessor.process(new String[]{"attributes", "13"}));
		Assert.assertEquals("getDocumentAttributes", domain.calledMethod);
		Assert.assertEquals(13, domain.documentId);
		
		domain.calledMethod = null;
		Assert.assertTrue(commandProcessor.process(new String[]{"attributes"}));
		Assert.assertNull(domain.calledMethod);
	}
	
	@Test
	public void addAttributeTest() throws SQLException {
		Assert.assertTrue(commandProcessor.process(new String[]{"add", "attribute", "14", NAME, TYPE, STRING, "15"}));
		Assert.assertEquals("addAttribute", domain.calledMethod);
		Assert.assertEquals(14, domain.attribute.getDocumentId());
		Assert.assertEquals(NAME, domain.attribute.getName());
		Assert.assertEquals(TYPE, domain.attribute.getType());
		Assert.assertEquals(STRING, domain.attribute.getStringValue());
		Assert.assertEquals(15, domain.attribute.getIntegerValue().intValue());
		
		domain.calledMethod = null;
		Assert.assertTrue(commandProcessor.process(new String[]{"add", "attribute", "14"}));
		Assert.assertNull(domain.calledMethod);
	}
	
	@Test
	public void changeAttributeTest() throws SQLException {
		Assert.assertTrue(commandProcessor.process(new String[]{"change", "attribute", "16", NAME, TYPE, STRING, "17"}));
		Assert.assertEquals("updateAttribute", domain.calledMethod);
		Assert.assertEquals(16, domain.attribute.getId());
		Assert.assertEquals(NAME, domain.attribute.getName());
		Assert.assertEquals(TYPE, domain.attribute.getType());
		Assert.assertEquals(STRING, domain.attribute.getStringValue());
		Assert.assertEquals(17, domain.attribute.getIntegerValue().intValue());
		
		domain.calledMethod = null;
		Assert.assertTrue(commandProcessor.process(new String[]{"change", "attribute", "16"}));
		Assert.assertNull(domain.calledMethod);
	}
	
	@Test
	public void removeAttributeTest() throws SQLException {
		Assert.assertTrue(commandProcessor.process(new String[]{"remove", "attribute", "18"}));
		Assert.assertEquals("removeAttribute", domain.calledMethod);
		Assert.assertEquals(18, domain.attributeId);
		
		domain.calledMethod = null;
		Assert.assertTrue(commandProcessor.process(new String[]{"remove", "attribute"}));
		Assert.assertNull(domain.calledMethod);
	}
	
	private DocumentsDomainTestImpl domain;
	private CommandProcessor commandProcessor;

	private static final String INDEX = "index";
	private static final String NAME = "name";
	private static final String TYPE = "type";
	private static final String STRING = "string";
}
