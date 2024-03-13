package org.example.account.entities;

import lombok.Getter;
import lombok.Setter;
import org.example.account.models.Account;
import org.example.bank.models.Bank;
import org.example.client.entities.Client;
import org.example.exceptions.MyException;

import java.time.LocalDate;

/**
 * Класс депозитного счета
 */
@Setter
public class DepositAccount extends Account {
    @Getter
    /**
     * Период депозита
     */
    private Integer period;
    /**
     * Дата открытия депозита
     */
    private LocalDate startTime;
    /**
     * Процент начисления при низком балансе
     */
    private Double lowBalancePercent;
    /**
     * Процент начисления при среднем балансе
     */
    private Double middleBalancePercent;
    /**
     * Процент начисления при высоком балансе
     */
    private Double highBalancePercent;
    /**
     * Уровень низкого баланса
     */
    private Integer lowBalanceLevel;
    /**
     * Уровень среднего баланса
     */
    private Integer middleBalanceLevel;

    public DepositAccount(Bank _bank, Client _client, Double _balance, Double _moneyToCharge) {
        super(_bank, _client, _balance, _moneyToCharge);

        period = bank.getDepositPeriod();
        startTime = LocalDate.now();
        lowBalancePercent = bank.getDepositLowBalancePercent();
        middleBalancePercent = bank.getDepositMiddleBalancePercent();
        highBalancePercent = bank.getDepositHighBalancePercent();
        lowBalanceLevel = bank.getDepositLowBalanceLevel();
        middleBalanceLevel = bank.getDepositMiddleBalanceLevel();
    }

    /**
     * Метод начисления процентов
     * @param withWithdraw - флаг, указывающий на необходимость начисления процентов на баланс
     */
    public void chargeInterest(Boolean withWithdraw) {
        if (balance < lowBalanceLevel) {
            moneyToCharge += ((lowBalancePercent / 100) * balance);
        } else if (balance >= lowBalanceLevel && balance < middleBalanceLevel) {
            moneyToCharge += ((middleBalancePercent / 100) * balance);
        } else {
            moneyToCharge += ((highBalancePercent / 100) * balance);
        }

        if (withWithdraw) {
            balance += moneyToCharge;
        }
    }

    /**
     * Метод снятия денег со счета
     * @param amount - сумма, которую необходимо снять
     * @throws MyException - исключение, возникающее при попытке снять сумму, превышающую баланс
     */
    @Override
    public void withdraw(Double amount) throws MyException {
        if (checkClientSuspicion(amount)){
            throw new MyException("Suspicious client");
        } else if (balance - amount < 0) {
            throw new MyException("Low balance");
        } else if (!startTime.plusDays(period).isAfter(LocalDate.now())){
            throw new MyException("Deposit period not expired");
        } else {
            balance -= amount;
        }
    }

    /**
     * Метод внесения денег на счет
     * @param amount - сумма, которую необходимо внести
     */
    @Override
    public void deposit(Double amount) {
        balance += amount;
    }

    /**
     * Метод перевода денег на другой счет
     * @param amount - сумма, которую необходимо перевести
     * @param destinationAccountId - id счета, на который необходимо перевести деньги
     * @param destinationBankId - id банка, в котором находится счет, на который необходимо перевести деньги
     * @throws MyException - исключение, возникающее при попытке перевести сумму, превышающую баланс
     */
    @Override
    public void transfer(Double amount, Integer destinationAccountId, Integer destinationBankId) throws MyException {
        if (checkClientSuspicion(amount)){
            throw new MyException("Suspicious client");
        } else if (balance - amount < 0) {
            throw new MyException("Low balance");
        } else if (!startTime.plusDays(period).isAfter(LocalDate.now())){
            throw new MyException("Deposit period not expired");
        } else {
            saveState(destinationAccountId);
            balance -= amount;

            bank.transferMoney(amount, id, destinationAccountId, destinationBankId);
        }
    }
}
