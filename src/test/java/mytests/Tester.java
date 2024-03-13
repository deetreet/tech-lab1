package mytests;

import org.example.account.entities.DebitAccount;
import org.example.account.models.Account;
import org.example.bank.models.Bank;
import org.example.bank.models.CentralBank;
import org.example.client.entities.Client;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class Tester {
    private CentralBank centralBank = new CentralBank();
    private Bank sber = centralBank.createBank(centralBank ,
            4.0,
            2.0,
            3.1,
            4.1,
            1000,
            10000,
            5.3,
            1000.0,
            4000,
            30);
    private Client goodClient = Client.builder()
            .withName("Ivan")
            .withSurname("Ivanov")
            .withAddress("Kronverkskiy, 49")
            .withPassportNumber("12-12-123132")
            .build();
    private Client suspiciousClient = Client.builder()
            .withName("Ivan")
            .withSurname("Ivanov")
            .withAddress(null)
            .withPassportNumber(null)
            .build();

    @Test
    public void tryToDepositMoney_ShouldReturnBalanceEqualsParameter() {
        Account account = new DebitAccount(sber, goodClient, 1000.0, 0.0);
        sber.addAccount(account);

        account.deposit(1000.0);

        assertEquals(account.getBalance(), 2000.0);
    }

    @Test
    public void tryToTransferMoney_ShouldReturnBalanceEqualsParameter() {
        Account account1 = new DebitAccount(sber, goodClient, 1000.0, 0.0);
        Account account2 = new DebitAccount(sber, suspiciousClient, 0.0, 0.0);
        sber.addAccount(account1);
        sber.addAccount(account2);

        try {
            account1.transfer(100.0, account2.getId(), sber.getId());
        } catch (Exception e) {
            assertEquals(e.getMessage(), "");
        }
        assertEquals(account2.getBalance(), 100.0);
    }

    @Test
    public void tryToWithdrawZeroBalance_ShouldThrowMyException() {
        Account account = new DebitAccount(sber, suspiciousClient, 0.0, 0.0);
        sber.addAccount(account);

        try {
            account.withdraw(100.0);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Low balance");
            return;
        }
        assertFalse(true);
    }

    @Test
    public void tryToWithdrawSuspiciousAccount_ShouldThrowMyException() {
        Account account = new DebitAccount(sber, suspiciousClient, 20000.0, 0.0);
        sber.addAccount(account);

        try {
            account.withdraw(10000.0);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Suspicious client");
            return;
        }
        assertFalse(true);
    }

    @Test
    public void tryToChangePercents_ShouldNotifyClients() {
        Client mockClient = mock(Client.class);
        sber.subscribe(mockClient);

        sber.changeCreditPercent(5.0);

        verify(mockClient).listenBank(any(String.class));
    }
}
