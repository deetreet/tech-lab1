package org.example.client.models;

/**
 * Интерфейс билдера адреса
 */
public interface IAddressBuilder {
    IPassportDataBuilder withAddress(String address);
}
