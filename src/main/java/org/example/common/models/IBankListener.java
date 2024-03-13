package org.example.common.models;

/**
 * Интерфейс подписчика банка
 */
public interface IBankListener {
    void listenBank(String message);
}
