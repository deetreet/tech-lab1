package org.example.client.models;

/**
 * Интерфейс строителя адреса
 */
public interface INameBuilder {
    ISurnameBuilder withName(String name);
}
