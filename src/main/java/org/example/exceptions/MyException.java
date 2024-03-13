package org.example.exceptions;

/**
 * Класс кастомного исключения
 */
public class MyException extends Exception{
    public MyException(String message) {
        super(message);
    }
}
