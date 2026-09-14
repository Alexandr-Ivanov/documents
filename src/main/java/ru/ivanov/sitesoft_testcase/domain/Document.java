/**
 * Тестовое задание от sitesoft для Java
 */
package ru.ivanov.sitesoft_testcase.domain;

/**
 * Класс, реализующий интерфейс документа
 * 
 * @author Alexandr Ivanov
 *
 */
public class Document {

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.Document#getId()
	 */
	public long getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.Document#getName()
	 */
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.Document#getType()
	 */
	public String getType() {
		return type;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.Document#setId(long)
	 */
	public void setId(long id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.Document#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.Document#setType(java.lang.String)
	 */
	public void setType(String type) {
		this.type = type;
	}

	private long id;
    private String name;
	private String type;
}
