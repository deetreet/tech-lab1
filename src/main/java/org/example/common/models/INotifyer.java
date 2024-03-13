package org.example.common.models;

/**
 * Интерфейс оповещателя
 */
public interface INotifyer {
    public void subscribe(IBankListener subscriber);
    public void notify(String message);
}
