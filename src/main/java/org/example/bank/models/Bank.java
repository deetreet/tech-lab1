package org.example.bank.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.account.models.Account;
import org.example.client.entities.Client;
import org.example.common.models.IBankListener;
import org.example.common.models.INotifyer;
import org.example.exceptions.MyException;

import java.util.HashMap;
import java.util.Stack;

/**
 * Класс банка
 */
@Getter
@AllArgsConstructor
public class Bank implements IBank, INotifyer {
    /**
     * Центральный банк, к которому привязан банк
     */
    private CentralBank centralBank;
    /**
     * Идентификатор банка
     */
    private Integer id;
    /**
     * Список клиентов банка
     */
    private HashMap<Integer, Client> clients;
    /**
     * Список счетов банка
     */
    private HashMap<Integer, Account> accounts;
    /**
     * Список подписчиков банка
     */
    private Stack<IBankListener> subscribers;
    /**
     * Проценты по дебету
     */
    private Double debitPercent;
    /**
     * Проценты по депозиту при низком балансе
     */
    private Double depositLowBalancePercent;
    /**
     * Проценты по депозиту при среднем балансе
     */
    private Double depositMiddleBalancePercent;
    /**
     * Проценты по депозиту при высоком балансе
     */
    private Double depositHighBalancePercent;
    /**
     * Уровень баланса для низкого баланса
     */
    private Integer depositLowBalanceLevel;
    /**
     * Уровень баланса для среднего баланса
     */
    private Integer depositMiddleBalanceLevel;
    /**
     * Комиссия по кредиту
     */
    private Double creditComission;
    /**
     * Лимит переводов подозрительных транзакций
     */
    private Double suspiciousTransferLimit;
    /**
     * Лимит кредита
     */
    private Integer creditLimit;
    /**
     * Период депозита
     */
    private Integer depositPeriod;

    /**
     * Метод перевода денег
     * @param amount - сумма
     * @param sourceAccountId - идентификатор счета отправителя
     * @param destinationAccountId - идентификатор счета получателя
     * @param destinationBankId - идентификатор банка получателя
     */
    @Override
    public void transferMoney(Double amount, Integer sourceAccountId, Integer destinationAccountId, Integer destinationBankId) {
        centralBank.transferMoney(amount, sourceAccountId, destinationAccountId, destinationBankId);
    }

    /**
     * Метод начисления процентов
     * @param withWithdraw - начислять ли проценты на баланс
     */
    @Override
    public void chargeInterest(Boolean withWithdraw) {
        for (Account account : accounts.values()) {
            account.chargeInterest(withWithdraw);
        }
    }

    /**
     * Метод снятия денег
     * @param amount - сумма
     * @param accountId - идентификатор счета
     * @throws MyException - исключение
     */
    public void withdrawMoney(Double amount, Integer accountId) throws MyException {
        try{
            accounts.get(accountId).withdraw(amount);
        } catch (MyException ex) {
            throw ex;
        }
    }

    /**
     * Метод зачисления денег
     * @param amount
     * @param accountId
     */
    public void depositMoney(Double amount, Integer accountId){
        accounts.get(accountId).deposit(amount);
    }

    /**
     * Метод получения баланса
     * @param accountId - идентификатор счета
     * @return - баланс
     */
    public Double showBalance(Integer accountId){
        return accounts.get(accountId).getBalance();
    }

    /**
     * Метод перевода денег
     * @param sourceAccountId - идентификатор счета отправителя
     * @param destinationAccountId - идентификатор счета получателя
     */
    public void cancelTransaction(Integer sourceAccountId, Integer destinationAccountId){
        accounts.get(sourceAccountId).restoreState();
        accounts.get(destinationAccountId).restoreState();

    }

    /**
     * Метод добавления счета
     * @param account - счет
     */
    public void addAccount(Account account){
        accounts.put(account.getId(), account);
    }

    /**
     * Метод добавления клиента
     * @param client - клиент
     */
    public void addClient(Client client){
        clients.put(client.getId(), client);
    }

    /**
     * Метод изменения процентов
     * @param percent - процент
     */
    public void changeDebitPercent(Double percent){
        debitPercent = percent;
        notify("Debit percent changed to " + percent.toString() + "%");
    }

     /**
     * Метод изменения процентов
     * @param percent - процент
     */
    public void changeDepositLowBalancePercent(Double percent){
        depositLowBalancePercent = percent;
        notify("Deposit low balance percent changed to " + percent.toString() + "%");
    }

    /**
     * Метод изменения процентов
     * @param percent - процент
     */
    public void changeDepositMiddleBalancePercent(Double percent){
        depositMiddleBalancePercent = percent;
        notify("Deposit middle balance percent changed to " + percent.toString() + "%");
    }

    /**
     * Метод изменения процентов
     * @param percent - процент
     */
    public void changeDepositHighBalancePercent(Double percent){
        depositHighBalancePercent = percent;
        notify("Deposit high balance percent changed to " + percent.toString() + "%");
    }

    /**
     * Метод изменения уровня баланса
     * @param percent - процент
     */
    public void changeCreditPercent(Double percent){
        creditComission = percent;
        notify("Credit percent changed to " + percent.toString() + "%");
    }

    /**
     * Метод изменения лимита переводов подозрительных транзакций
     * @param limit - лимит
     */
    public void changeSuspiciousTransferLimit(Double limit){
        suspiciousTransferLimit = limit;
        notify("Suspicious transfer limit changed to " + limit.toString());
    }

    /**
     * Метод изменения лимита кредита
     * @param subscriber - подписчик
     */
    @Override
    public void subscribe(IBankListener subscriber) {
        subscribers.push(subscriber);
    }

    /**
     * Метод оповещения подписчиков
     * @param message - сообщение
     */
    @Override
    public void notify(String message) {
        for (IBankListener sub : subscribers){
            sub.listenBank(message);
        }
    }
}
