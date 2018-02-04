/**
 * Тестовое задание от sitesoft для Java
 */
package ru.ivanov.sitesoft_testcase.domain;
/**
 * интерфейс документа
 * 
 * @author Alexandr Ivanov
 *
 */
public interface Document {
	long getId();
	String getIndex();
	String getName();
	String getType();
	void setId(long id);
	void setIndex(String index);
	void setName(String name);
	void setType(String type);
}
