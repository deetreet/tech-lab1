package org.example.account.models;

import org.example.exceptions.MyException;

/**
 * Интерфейс счета
 */
public interface IAccount {
    public void chargeInterest(Boolean withWithdraw);
    public void withdraw (Double amount) throws MyException;
    public void deposit(Double amount);
    public void transfer(Double amount, Integer destinationAccountId, Integer destinationBankId) throws MyException;
    public Double showBalance();
    public Double timeMachine(Integer days);
}
