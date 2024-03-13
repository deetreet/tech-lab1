package org.example.account.models;

import lombok.Getter;
import org.example.bank.models.Bank;
import org.example.client.entities.Client;
import org.example.exceptions.MyException;

import java.util.Stack;

/**
 * Абстрактный класс счета
 */
@Getter
public abstract class Account implements IAccount {
    /**
     * Банк, в котором открыт счет
     */
    protected Bank bank;
    /**
     * Максимальный id
     */
    protected static Integer maxId = 1;
    /**
     * id счета
     */
    protected Integer id;
    /**
     * Клиент, которому принадлежит счет
     */
    protected Client client;
    /**
     * Баланс счета
     */
    protected Double balance;
    /**
     * Сумма, которую необходимо начислить
     */
    protected Double moneyToCharge;
    /**
     * Порог подозрительности клиента
     */
    protected Double suspiciousTransferLimit;
    /**
     * История состояний счета
     */
    protected Stack<AccountMemento> history;

    public Account(Bank _bank, Client _client, Double _balance, Double _moneyToCharge) {
        bank = _bank;
        id = maxId++;
        client = _client;
        balance = _balance;
        moneyToCharge = _moneyToCharge;
        suspiciousTransferLimit = bank.getSuspiciousTransferLimit();
        history = new Stack<AccountMemento>();
    }

    /**
     * Вложенный класс снимков состояния счета
     */
    @Getter
    protected class AccountMemento {
        /**
         * Баланс счета
         */
        private Double balance;
        /**
         * Сумма, которую необходимо начислить
         */
        private Double moneyToCharge;
        /**
         * id счета, на который необходимо перевести деньги
         */
        private Integer destinationAccountId;

        public AccountMemento(Double _balance, Double _moneyToCharge, Integer _destinationAccountId) {
            balance = _balance;
            moneyToCharge = _moneyToCharge;
            destinationAccountId = _destinationAccountId;
        }
    }

    /**
     * Метод начисления процентов
     * @param sumToOperate - сумма, на которую необходимо начислить проценты
     * @return - сумма, на которую начислены проценты
     */
    public Boolean checkClientSuspicion(Double sumToOperate) {
        if ((client.getAddress() == null || client.getPassport() == null)
                && sumToOperate > suspiciousTransferLimit) {
            return true;
        }

        return false;
    }

    /**
     * Метод начисления процентов
     * @param destinationAccountId - id счета, на который необходимо перевести деньги
     */
    public void saveState(Integer destinationAccountId) {
        AccountMemento snapshot = new AccountMemento(balance, moneyToCharge, destinationAccountId);
        history.push(snapshot);
    }

    /**
     * Метод начисления процентов
     */
    public void restoreState() {
        balance = history.pop().getBalance();
        moneyToCharge = history.pop().getMoneyToCharge();
    }

    /**
     * Метод начисления процентов
     * @return - сумма, на которую начислены проценты
     */
    public Double showBalance() {
        return balance;
    }

    /**
     * Метод начисления процентов
     * @param days - количество дней, на которые необходимо перемотать время
     * @return - сумма, на которую начислены проценты
     */
    public Double timeMachine(Integer days) {
        Boolean withwithdraw = false;
        Double tmpBalance = balance;

        for (int i = 0; i < days; i++) {
            chargeInterest(withwithdraw);
        }

        Double tmpVar = balance;
        balance = tmpBalance;
        return tmpVar;
    }
}
