package rules;

import main.Account;
import main.Transaction;

import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class SimilarTransactionRule implements Rules {
    @Override
    public boolean applyRule(AtomicReference<Account> accountAtomicReference,
                             String ruleString,
                             Transaction newTransaction) {
        boolean resp = false;
        String newTransactionMerchant = newTransaction.getMerchant();
        Integer newTransactionAmount = newTransaction.getAmount().get();
        Date newTransactionTime = newTransaction.getTime();

        Account singularAccount = accountAtomicReference.get();

        CopyOnWriteArrayList<Transaction> transactions = singularAccount.getTransactions();

        for (int transactionIterator = 0; transactionIterator < transactions.size() && !resp;
             transactionIterator++) {

            Date transactionTime = transactions.get(transactionIterator).getTime();
            long diffInMillies = Math.abs(transactionTime.getTime() - newTransactionTime.getTime());
            long diff = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);

            String transactionMerchant = transactions.get(transactionIterator).getMerchant();
            Integer transactionAmount = transactions.get(transactionIterator).getAmount().get();
            if (newTransactionMerchant.equals(transactionMerchant)
                    && newTransactionAmount.equals(transactionAmount)
                    && diff < 2)
                resp = true;
        }

        return !resp;
    }

    @Override
    public String getRuleString() {
        return Constants.DOUBLED_TRANSACTION;
    }
}
