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
public class DocumentImpl implements Document {

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.Document#getId()
	 */
	@Override
	public long getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.Document#getIndex()
	 */
	@Override
	public String getIndex() {
		return index;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.Document#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.Document#getType()
	 */
	@Override
	public String getType() {
		return type;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.Document#setId(long)
	 */
	@Override
	public void setId(long id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.Document#setIndex(java.lang.String)
	 */
	@Override
	public void setIndex(String index) {
		this.index = index;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.Document#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see ru.ivanov.sitesoft_testcase.Document#setType(java.lang.String)
	 */
	@Override
	public void setType(String type) {
		this.type = type;
	}

	private long id;
	private String index;
	private String name;
	private String type;
}
