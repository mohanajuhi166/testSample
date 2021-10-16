package test;

import main.Account;
import main.Transaction;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import rules.AvailableLimitRule;
import rules.HighFreqRule;
import rules.SimilarTransactionRule;
import rules.Constants;
import service.Logic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

class TransactionTest {

    //This test the logic of the account not having limit to process the transaction
    @Test
    void insufficientLimit() {
        try {
            Logic logic = new Logic(true);
            Account account = new Account(true, 100);
            AtomicReference<Account> accountAtomicReference = new AtomicReference<>();
            logic.createNewAccount(accountAtomicReference, account);

            String isoDatePattern = "yyyy-MM-dd'T'HH:mm:ss.000'Z'";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(isoDatePattern);
            Date date = simpleDateFormat.parse("2019-02-13T11:00:00.000Z");
            Transaction transaction = new Transaction("Test Merchant",
                    new AtomicInteger(200), date);
            boolean availableLimit = new AvailableLimitRule().applyRule(accountAtomicReference,
                    Constants.INSUFF_LIMIT, transaction);
            Assert.assertEquals(false, availableLimit);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //This test when 3 transactions arrives in a 2 minute interval.
    @Test
    void highFrequencySmallInterval() {
        try {
            Logic logic = new Logic(true);
            Account account = new Account(true, 100);
            AtomicReference<Account> accountAtomicReference = new AtomicReference<>();
            logic.createNewAccount(accountAtomicReference, account);

            String isoDatePattern = "yyyy-MM-dd'T'HH:mm:ss.000'Z'";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(isoDatePattern);

            Date date1 = simpleDateFormat.parse("2019-02-13T11:00:00.000Z");
            Transaction transaction1 = new Transaction("Test Merchant",
                    new AtomicInteger(100), date1);
            logic.addTransactionToAccount(accountAtomicReference, transaction1);
            Date date2 = simpleDateFormat.parse("2019-02-13T11:00:01.000Z");
            Transaction transaction2 = new Transaction("Test Merchant",
                    new AtomicInteger(100), date2);
            logic.addTransactionToAccount(accountAtomicReference, transaction2);
            Date date3 = simpleDateFormat.parse("2019-02-13T11:00:30.000Z2019-02-13T11:01:01.000Z");
            Transaction transaction3 = new Transaction("Test Merchant",
                    new AtomicInteger(200), date3);
            logic.addTransactionToAccount(accountAtomicReference, transaction3);

            Date date4 = simpleDateFormat.parse("2019-02-13T11:01:31.000Z");
            Transaction transaction4 = new Transaction("Test Merchant",
                    new AtomicInteger(200), date4);
            boolean highFrequencySmallInterval = new HighFreqRule()
                    .applyRule(accountAtomicReference, Constants.HIGH_FREQ_SMALL_INTERVAL, transaction4);
            Assert.assertEquals(true, !highFrequencySmallInterval);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //This test when 2 transactions arrives in a 2 minute interval and have the same amount and merchant.
    @Test
    void doubleTransaction() {
        try {
            Logic logic = new Logic(true);
            Account account = new Account(true, 1000);
            AtomicReference<Account> accountAtomicReference = new AtomicReference<>();
            logic.createNewAccount(accountAtomicReference, account);
            String isoDatePattern = "yyyy-MM-dd'T'HH:mm:ss.000'Z'";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(isoDatePattern);
            Date date = simpleDateFormat.parse("2019-02-13T11:00:00.000Z");
            Transaction transaction1 = new Transaction("Test Merchant",
                    new AtomicInteger(100), date);
            logic.addTransactionToAccount(accountAtomicReference, transaction1);
            Transaction transaction2 = new Transaction("Test Merchant",
                    new AtomicInteger(100), date);
            boolean similarTransaction = new SimilarTransactionRule()
                    .applyRule(accountAtomicReference,
                            Constants.DOUBLED_TRANSACTION, transaction2);
            Assert.assertEquals(true, !similarTransaction);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
