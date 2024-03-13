package org.example.account.entities;
import lombok.Setter;
import org.example.account.models.Account;
import org.example.bank.models.Bank;
import org.example.client.entities.Client;
import org.example.exceptions.MyException;

/**
 * Класс дебетового счета
 */
@Setter
public class DebitAccount extends Account{
    /**
     * Процент начисления
     */
    private Double percent;

    public DebitAccount(Bank _bank, Client _client, Double _balance, Double _moneyToCharge) {
        super(_bank, _client, _balance, _moneyToCharge);
        percent = bank.getDebitPercent();
    }

    /**
     * Метод начисления процентов
     * @param withWithdraw - флаг, указывающий на необходимость начисления процентов на баланс
     */
    @Override
    public void chargeInterest(Boolean withWithdraw) {
        moneyToCharge += ((percent / 100) * balance);

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
        } else {
            saveState(destinationAccountId);
            balance -= amount;

            bank.transferMoney(amount, id, destinationAccountId, destinationBankId);
        }
    }
}
