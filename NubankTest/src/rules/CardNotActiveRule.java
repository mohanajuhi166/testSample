package rules;

import main.Account;
import main.Transaction;

import java.util.concurrent.atomic.AtomicReference;

public class CardNotActiveRule implements Rules {
    @Override
    public boolean applyRule(AtomicReference<Account> accountAtomicReference,
                             String ruleString,
                             final Transaction newTransaction) {
        return accountAtomicReference.get().isActiveCard();
    }

    @Override
    public String getRuleString() {
        return Constants.CARD_NOT_ACTIVE;
    }
}
