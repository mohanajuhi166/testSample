package rules;

import main.Account;
import main.Transaction;

import java.util.concurrent.atomic.AtomicReference;

public interface Rules {
    boolean applyRule(AtomicReference<Account> accountAtomicReference,
                               String ruleString,
                               final Transaction newTransaction);

    String getRuleString();
}
