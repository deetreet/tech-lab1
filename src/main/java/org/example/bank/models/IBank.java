package org.example.bank.models;

/**
 * Интерфейс банка
 */
public interface IBank {
    public void transferMoney(Double amount, Integer sourceAccountId, Integer destinationAccountId, Integer destinationBankId);
    public void chargeInterest(Boolean withWithdraw);
}
