/**
 * Тестовое задание от sitesoft для Java
 */
package ru.ivanov.sitesoft_testcase.util;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Alexandr Ivanov
 *
 */
public class StringSplitterTest {
	@Test
	public void tabTest() {
		final List<String> splitLine = StringSplitter.splitLine("first\tsecond"); // with tabs
		Assert.assertEquals("first", splitLine.get(0));
		Assert.assertEquals("must be two strings", 2, splitLine.size());
	}
	
	@Test
	public void quotationMarkTest() {
		final List<String> splitLine = StringSplitter.splitLine("\"first second\" third ");
		Assert.assertEquals("first second", splitLine.get(0));
		Assert.assertEquals("third", splitLine.get(1));
		Assert.assertEquals("number of strings", 2, splitLine.size());
		
	}
}
