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

	String getName();
	String getType();
	void setId(long id);

	void setName(String name);
	void setType(String type);
}
