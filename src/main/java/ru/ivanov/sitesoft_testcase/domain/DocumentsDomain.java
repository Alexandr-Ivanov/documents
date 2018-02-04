/**
 * Тестовое задание от sitesoft для Java
 */
package ru.ivanov.sitesoft_testcase.domain;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
/**
 * Интерфейс для работы с хранилищем объектов
 * 
 * @author Alexandr Ivanov
 *
 */
public interface DocumentsDomain {

	void cleanup() throws SQLException;

	/**
	 * Добавить атрибут в базу
	 * @param attribute
	 * @return идентификатор добавленного атрибута
	 * @throws SQLException
	 */
	long addAttribute(DocumentAttribute attribute) throws SQLException;

	/**
	 * создать необходимые таблицы базы данных
	 * @throws SQLException
	 */
	void createDatabase() throws SQLException;

	/**
	 * создать новый пустой документ
	 * @return новый пустой документ с id = -1
	 */
	Document createDocument();

	/**
	 * Создать новый пустой атрибут
	 * @return новый пустой атрибут с id = -1, documentId = -1 
	 */
	DocumentAttribute createDocumentAttribute();

	/**
	 * Получить документ по идентификатору
	 * @param documentId
	 * @return документ или null, если документа с таким идентификатором нет в базе
	 * @throws SQLException
	 */
	Document getDocument(long documentId) throws SQLException;

	/**
	 * Получить атрибут по id
	 * @param attributeId
	 * @return атрибут или null, если атрибута с таким id нет в базе
	 * @throws SQLException
	 */
	DocumentAttribute getDocumentAttribute(long attributeId) throws SQLException;

	/**
	 * Получить атрибуты документа
	 * @param documentId
	 * @return список атрибутов
	 * @throws SQLException
	 */
	List<DocumentAttribute> getDocumentAttributes(long documentId) throws SQLException;

	/**
	 * Получить содержимое документа
	 * @param id
	 * @return содержимое документа
	 * @throws SQLException
	 */
	InputStream getDocumentContent(long id) throws SQLException;

	/**
	 * получить список документов, отсортированных по индексу
	 * @return список документов
	 * @throws SQLException
	 */
	List<Document> getDocumentsList() throws SQLException;

	/**
	 * Удалить атрибут
	 * @param attributeId
	 * @throws SQLException 
	 */
	void removeAttribute(long attributeId) throws SQLException;

	/**
	 * Добавить документ в базу данных
	 * @param document
	 * @return ID нового документа 
	 * @throws SQLException
	 */
	long addDocument(Document document) throws SQLException;

	/**
	 * Присвоить содержимое документу
	 * @param documentId
	 * @param inputStream
	 * @throws SQLException
	 * @throws IOException
	 */
	void setDocumentContent(long documentId, InputStream inputStream) throws SQLException, IOException;

	/**
	 * Присвоить содержимое документу
	 * @param documentId
	 * @param bytes
	 * @throws SQLException
	 */
	void setDocumentContent(long documentId, byte[] bytes) throws SQLException;

	/**
	 * Изменить атрибут в базе данных
	 * @param attribute
	 * @throws SQLException 
	 */
	void updateAttribute(DocumentAttribute attribute) throws SQLException;

	/**
	 * Удалить документ из базы данных
	 * @param documentId
	 * @throws SQLException
	 */
	void removeDocument(long documentId) throws SQLException;

}