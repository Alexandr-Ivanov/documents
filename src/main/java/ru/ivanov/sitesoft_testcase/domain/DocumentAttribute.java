/**
 * Тестовое задание от sitesoft для Java
 */
package ru.ivanov.sitesoft_testcase.domain;

/**
 * интерфейс атрибута документа
 * 
 * @author Alexandr Ivanov
 *
 */
public interface DocumentAttribute {
	long getId();
	long getDocumentId();
	String getName();
	String getType();
	String getStringValue();
	Integer getIntegerValue();
	void setId(long id);
	void setDocumentId(long documentId);
	void setName(String name);
	void setType(String type);
	void setString(String value);
	void setInteger(Integer value);
}
