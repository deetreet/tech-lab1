package org.example.bank.models;

import org.example.account.models.Account;
import org.example.client.entities.Client;
import org.example.common.models.IBankListener;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Stack;

public class CentralBank implements IBank{
    /**
     * Максимальный id банка
     */
    private static Integer maxBankId = 1;
    /**
     * Список банков
     */
    private HashMap<Integer, Bank> banks;

    public CentralBank() {
        banks = new HashMap<Integer, Bank>();
    }

    /**
     * Метод снятия денег
     * @param amount - сумма
     * @param sourceAccountId - id счета
     * @param destinationAccountId - id счета
     * @param destinationBankId - id банка
     */
    @Override
    public void transferMoney(Double amount, Integer sourceAccountId, Integer destinationAccountId, Integer destinationBankId) {
        Bank destinationBank = banks.get(destinationBankId);
        Account destinationAccount = destinationBank.getAccounts().get(destinationAccountId);

        destinationAccount.deposit(amount);

    }

    /**
     * Метод начисления процентов
     * @param withWithdraw - флаг, указывающий на необходимость начисления процентов на баланс
     */
    @Override
    public void chargeInterest(Boolean withWithdraw) {
        if (LocalTime.now().getHour() == 0 && LocalTime.now().getMinute() == 0) {
            Boolean withwithdraw = false;

            if (LocalDate.now().getDayOfMonth() == 1) {
                withwithdraw = true;
            }

            for (Bank bank : banks.values()) {
                for (Account account : bank.getAccounts().values()) {
                    account.chargeInterest(withWithdraw);
                }
            }
        }
    }

    /**
     * Метод создания банка
     * @param _centralBank - центральный банк
     * @param _debitPercent - процент начисления на дебетовый счёт
     * @param _depositLowBalancePercent - процент начисления при низком балансе
     * @param _depositMiddleBalancePercent - процент начисления при среднем балансе
     * @param _depositHighBalancePercent - процент начисления при высоком балансе
     * @param _depositLowBalanceLevel - уровень низкого баланса
     * @param _depositMiddleBalanceLevel - уровень среднего баланса
     * @param _creditComission - комиссия за пользование кредитом
     * @param _suspiciousTransferLimit - лимит подозрительного перевода
     * @param _creditLimit - лимит кредита
     * @param _depositPeriod - период депозита
     * @return - созданный банк
     */
    public Bank createBank(CentralBank _centralBank,
                           Double _debitPercent,
                           Double _depositLowBalancePercent,
                           Double _depositMiddleBalancePercent,
                           Double _depositHighBalancePercent,
                           Integer _depositLowBalanceLevel,
                           Integer _depositMiddleBalanceLevel,
                           Double _creditComission,
                           Double _suspiciousTransferLimit,
                           Integer _creditLimit,
                           Integer _depositPeriod){
        Bank newBank = new Bank(
            _centralBank,
            maxBankId++,
            new HashMap<Integer, Client>(),
            new HashMap<Integer, Account>(),
            new Stack<IBankListener>(),
            _debitPercent,
            _depositLowBalancePercent,
            _depositMiddleBalancePercent,
            _depositHighBalancePercent,
            _depositLowBalanceLevel,
            _depositMiddleBalanceLevel,
            _creditComission,
            _suspiciousTransferLimit,
            _creditLimit,
            _depositPeriod
        );

        banks.put(newBank.getId(), newBank);

        return newBank;
    }
}
