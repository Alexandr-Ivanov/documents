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
public class DocumentAttributeImpl implements DocumentAttribute {

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentAttribute#getId()
	 */
	@Override
	public long getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentAttribute#getDocumentId()
	 */
	@Override
	public long getDocumentId() {
		return documentId;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentAttribute#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentAttribute#getType()
	 */
	@Override
	public String getType() {
		return type;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentAttribute#getStringValue()
	 */
	@Override
	public String getStringValue() {
		return stringValue;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentAttribute#getIntegerValue()
	 */
	@Override
	public Integer getIntegerValue() {
		return integerValue;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentAttribute#setId(long)
	 */
	@Override
	public void setId(long id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentAttribute#setDocumentId(long)
	 */
	@Override
	public void setDocumentId(long documentId) {
		this.documentId = documentId;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentAttribute#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentAttribute#setType(java.lang.String)
	 */
	@Override
	public void setType(String type) {
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentAttribute#setString(java.lang.String)
	 */
	@Override
	public void setString(String value) {
		this.stringValue = value;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.domain.DocumentAttribute#setInteger(java.lang.Integer)
	 */
	@Override
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
