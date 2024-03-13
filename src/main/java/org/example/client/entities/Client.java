package org.example.client.entities;

import lombok.Getter;
import org.example.account.models.Account;
import org.example.client.models.IAddressBuilder;
import org.example.client.models.IPassportDataBuilder;
import org.example.client.models.ISurnameBuilder;
import org.example.common.models.IBankListener;
import org.example.client.models.INameBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс клиента
 */
@Getter
public class Client implements IBankListener {
    /**
     * Максимальный id
     */
    private static int maxId = 1;
    /**
     * id клиента
     */
    private int id;
    /**
     * Имя клиента
     */
    private String name;
    /**
     * Фамилия клиента
     */
    private String surname;
    /**
     * Адрес клиента
     */
    private String address;
    /**
     * Паспортные данные клиента
     */
    private String passport;
    /**
     * Список счетов клиента
     */
    private HashMap<Integer, Account> accounts;

    private Client(String _name, String _surname, String _address, String _passport) {
        id = maxId++;
        name = _name;
        surname = _surname;
        address = _address;
        passport = _passport;
    }

    /**
     * Метод создания клинта с помощью паттерна Builder
     * @return - объект клиента
     */
    public static INameBuilder builder() {
        return new Client.ClientBuilder();
    }

    /**
     * Класс Builder для создания клиента
     */
    private static class ClientBuilder implements INameBuilder, ISurnameBuilder, IAddressBuilder, IPassportDataBuilder {
        /**
         * Имя клиента
         */
        private String name;
        /**
         * Фамилия клиента
         */
        private String surname;
        /**
         * Адрес клиента
         */
        private String address;
        /**
         * Паспортные данные клиента
         */
        private String passport;

        /**
         * Метод создания имени
         * @param _name - имя
         * @return - объект Builder
         */
        @Override
        public ISurnameBuilder withName(String _name) {
            name = _name;
            return this;
        }

        /**
         * Метод создания фамилии
         * @param _surname - фамилия
         * @return - объект Builder
         */
        @Override
        public IAddressBuilder withSurname(String _surname) {
            surname = _surname;
            return this;
        }

        /**
         * Метод создания адреса
         * @param _address - адрес
         * @return - объект Builder
         */
        @Override
        public IPassportDataBuilder withAddress(String _address) {
            address = _address;
            return this;
        }

        /**
         * Метод создания паспортных данных
         * @param _passportNumber - паспортные данные
         * @return - объект Builder
         */
        @Override
        public IPassportDataBuilder withPassportNumber(String _passportNumber) {
            passport = _passportNumber;
            return this;
        }

        /**
         * Метод создания клиента
         * @return - объект клиента
         */
        @Override
        public Client build() {
            return new Client(name, surname, address, passport);
        }
    }

    /**
     * Метод оповещения клиента о событии в банке через какой-то интерфейс
     * @param message - сообщение
     */
    public void listenBank(String message) {
        // some logic
    }
}

