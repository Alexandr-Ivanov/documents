/**
 * Тестовое задание от sitesoft для Java
 */
package ru.ivanov.sitesoft_testcase.domain;

/**
 * Класс, реализующий интерфейс атрибута документа
 * 
 * @author Alexandr Ivanov
 *
 */
public class DocumentAttribute {

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentAttribute#getId()
	 */
	public long getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentAttribute#getDocumentId()
	 */
	public long getDocumentId() {
		return documentId;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentAttribute#getName()
	 */
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentAttribute#getType()
	 */
	public String getType() {
		return type;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentAttribute#getStringValue()
	 */
	public String getStringValue() {
		return stringValue;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentAttribute#getIntegerValue()
	 */
	public Integer getIntegerValue() {
		return integerValue;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentAttribute#setId(long)
	 */
	public void setId(long id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentAttribute#setDocumentId(long)
	 */
	public void setDocumentId(long documentId) {
		this.documentId = documentId;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentAttribute#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentAttribute#setType(java.lang.String)
	 */
	public void setType(String type) {
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentAttribute#setString(java.lang.String)
	 */
	public void setString(String value) {
		this.stringValue = value;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentAttribute#setInteger(java.lang.Integer)
	 */
	public void setInteger(Integer value) {
		this.integerValue = value;
	}

	private long id;
	private long documentId;
	private String name;
	private String type;
	private String stringValue;
	private Integer integerValue;
}
