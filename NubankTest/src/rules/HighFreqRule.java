package rules;

import main.Account;
import main.Transaction;

import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class HighFreqRule implements Rules {
    @Override
    public boolean applyRule(AtomicReference<Account> accountAtomicReference,
                             String ruleString,
                             final Transaction newTransaction) {
        AtomicBoolean resp = new AtomicBoolean(false);
        AtomicInteger numberOfTransactionsInHighFrequency = new AtomicInteger(0);
        Date newTransactionTime = newTransaction.getTime();

        Account singularAccount = accountAtomicReference.get();
        CopyOnWriteArrayList<Transaction> transactions = singularAccount.getTransactions();
        for (int transactionIterator = 0;
             transactionIterator < transactions.size()
                     && !resp.get();
             transactionIterator++) {
            Date transactionTime = transactions.get(transactionIterator).getTime();
            long diffInMillies = Math.abs(transactionTime.getTime()
                    - newTransactionTime.getTime());
            long diff = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
            numberOfTransactionsInHighFrequency.incrementAndGet();
            if (diff <= 2 && numberOfTransactionsInHighFrequency.get() >= 3) resp.set(true);
        }

        return !resp.get();
    }

    @Override
    public String getRuleString() {
        return Constants.HIGH_FREQ_SMALL_INTERVAL;
    }
}

