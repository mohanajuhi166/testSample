package rules;

import main.Account;
import main.Transaction;

import java.util.concurrent.atomic.AtomicReference;

public class AvailableLimitRule implements  Rules {
    @Override
    public boolean applyRule(AtomicReference<Account> accountAtomicReference, String ruleString,
                      Transaction transaction) {
        Account singularAccount = accountAtomicReference.get();

        int newLimit = getNewAvailableLimit(accountAtomicReference, transaction);
        if (newLimit > 0) {
            singularAccount.setAvailableLimit(newLimit);
            addTransactionToAccount(accountAtomicReference, transaction);
            return true;
        } else {
            return false;
        }
    }

    public void addTransactionToAccount(final AtomicReference<Account> accountAtomicReference,
                                        final Transaction transaction) {
        accountAtomicReference.get().addTransaction(transaction);
    }

    public int getNewAvailableLimit(final AtomicReference<Account> accountAtomicReference,
                                    final Transaction transaction) {
        int availableLimit = accountAtomicReference.get().getAvailableLimit();
        int newLimit = availableLimit - transaction.getAmount().get();
        return newLimit;
    }

    @Override
    public String getRuleString() {
        return Constants.INSUFF_LIMIT;
    }
}
