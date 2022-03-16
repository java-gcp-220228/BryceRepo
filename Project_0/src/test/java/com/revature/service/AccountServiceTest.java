package com.revature.service;

import com.revature.dao.AccountDao;
import com.revature.exception.AccountNotFoundException;
import com.revature.exception.ClientNotFoundException;
import com.revature.model.Account;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AccountServiceTest {

    // Positive
    @Test
    public void testGetAllAccounts() throws SQLException, IOException {
        // Arrange
        AccountDao mockedObject = mock(AccountDao.class);

        List<Account> fakeAccounts = new ArrayList<>();
        fakeAccounts.add(new Account("10975310-142a-4894-a5a2-6b98b24ea107", "A", "check",
                10000, 1646294400000L, 1646899200000L));
        //fakeAccounts.add(new Account("2", "Test", "Test123", 15));
        //fakeAccounts.add(new Account("3", "John", "Doe", 30));

        // Whenever the code in the Service layer calls the getAllAccounts() method
        // for the dao layer, then return the list of accounts
        // we have specified above
        when(mockedObject.getAllAccounts()).thenReturn(fakeAccounts);

        AccountService accountService = new AccountService(mockedObject);

        // Act
        List<Account> actual = accountService.getAllAccounts();

        // Assert
        List<Account> expected = new ArrayList<>(fakeAccounts);
        Assertions.assertEquals(expected, actual);
    }

    // Positive test is also known as the
    // "Happy path"
    // The "user" is utilizing this method correctly
    @Test
    public void testGetAccountById_positiveTest() throws SQLException, AccountNotFoundException, IOException, ClientNotFoundException {
        // Arrange
        AccountDao mockDao = mock(AccountDao.class);

        // Mocking the return value for id 10
        when(mockDao.getClientAccountById(eq("10975310-142a-4894-a5a2-6b98b24ea107"), eq("0d16f72e-c745-4e7b-8ed0-8647a598a2e2"))).thenReturn(
                new Account("10975310-142a-4894-a5a2-6b98b24ea107", "A", "check", 10000, 1646294400000L, 1646899200000L));

        AccountService accountService = new AccountService(mockDao);

        // Act
        Account actual = accountService.getClientAccountById("10975310-142a-4894-a5a2-6b98b24ea107", "0d16f72e-c745-4e7b-8ed0-8647a598a2e2");

        // Assert
        Account expected = new Account("10975310-142a-4894-a5a2-6b98b24ea107", "A", "check", 10000, 1646294400000L, 1646899200000L);

        Assertions.assertEquals(expected, actual);
    }

    // Negative test
    @Test
    public void test_getAccountById_accountDoesNotExist() {
        // Arrange
        AccountDao mockDao = mock(AccountDao.class);

        AccountService accountService = new AccountService(mockDao);

        // Act + Assert

        // The test case will pass if we encounter this exception
        // (AccountNotFoundException)
        Assertions.assertThrows(AccountNotFoundException.class, () -> {
            accountService.getClientAccountById("1", "2");
        });
    }

    // Negative test
    @Test
    public void test_getAccountById_invalidId() throws SQLException, AccountNotFoundException, IOException {
        // Arrange
        AccountDao mockDao = mock(AccountDao.class);

        AccountService accountService = new AccountService(mockDao);

        // Act
        try {
            accountService.getClientAccountById("abc", "def");

            fail(); // We only reach this line if no exception is caught
        } catch(IllegalArgumentException e) {
            String expectedMessage = "Id provided for account must be a valid int";
            String actualMessage = e.getMessage();

            Assertions.assertEquals(expectedMessage, actualMessage);

        } catch(FileNotFoundException e) {
            String expectedMessage = "Property file not found: " + e;
            String actualMessage = e.getMessage();

            Assertions.assertEquals(expectedMessage, actualMessage);
        } catch (ClientNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Negative
    @Test
    public void test_getAccountById_sqlExceptionFromDao() throws SQLException, IOException {
        // Arrange
        AccountDao mockDao = mock(AccountDao.class);

        when(mockDao.getClientAccountById(anyString(), anyString())).thenThrow(SQLException.class);

        AccountService accountService = new AccountService(mockDao);

        // Act + Assert
        Assertions.assertThrows(SQLException.class, () -> {
            accountService.getClientAccountById("5", "6");
        });
    }
}
