package org.example.client.models;

/**
 * Интерфейс строителя фамилии
 */
public interface ISurnameBuilder {
    IAddressBuilder withSurname(String surname);
}
