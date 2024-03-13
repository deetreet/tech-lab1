package org.example.client.models;

import org.example.client.entities.Client;

/**
 * Интерфейс билдера паспортных данных
 */
public interface IPassportDataBuilder {
    IPassportDataBuilder withPassportNumber(String passportNumber);

    Client build();
}
