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
public record DocumentAttribute(long id, long documentId, String name, String type, String stringValue, Integer integerValue) {
}
