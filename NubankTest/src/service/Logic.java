package service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.Account;
import main.ConsoleInput;
import main.OutputFormat;
import main.Transaction;
import rules.Constants;
import rules.Rules;
import rules.RulesFactory;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class Logic {

    //Gson library to parse JSON to Java Objects and vice versa
    private static final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES)
            .create();

    //Constructor for testing by JUNIT
    public Logic(boolean testing) {
    }

    /**
     * All the logic of the business rules. Returns a String in JSON format from Object parse.
     *
     * @param inputObject            allowed object
     * @param accountAtomicReference account reference
     *                               {@link ConsoleInput }
     * @return possible object is
     * {@link String }
     */
    public String checkInputObject(ConsoleInput inputObject,
                                   AtomicReference<Account> accountAtomicReference) {
        try {

            /**
             * Extract account and transaction information from input
             * */
            Account newAccount = inputObject.getAccount();
            Transaction newTransaction = inputObject.getTransaction();

            if (newAccount != null) {
                return dataAccountPresent(newAccount, accountAtomicReference);
            } else {
                return accountRules(newTransaction, accountAtomicReference);
            }
        } catch (Exception e) {
            System.out.println("Exception has occurred in account & transaction flow:"
                    + e.getMessage());
        }
        return null;
    }

    private OutputFormat formatOutput(final Account account,
                                      LinkedList<String> violations) {

        return account == null || (violations != null
                && violations.contains(Constants.ACC_NOT_INIT))
                ? new OutputFormat(new Object(),
                violations)
                : new OutputFormat(new Account(account.isActiveCard(),
                account.getAvailableLimit()), violations);

    }

    public String dataAccountPresent(final Account newAccount,
                                     final AtomicReference<Account> accountAtomicReference) {

        LinkedList<String> violations = new LinkedList<>();
        if (accountAtomicReference.get() == null) {
            createNewAccount(accountAtomicReference, newAccount);
        } else {
            violations =
                    addViolationToAccount
                            (Constants.ACC_ALRDY_INIT);
        }
        return gson.toJson(formatOutput(accountAtomicReference.get(), violations));
    }

    public String accountRules(final Transaction newTransaction,
                               final AtomicReference<Account> accountAtomicReference) {

        LinkedList<String> violations = new LinkedList<>();
        if (accountAtomicReference.get() == null) {
            OutputFormat notInitializedAccount = accountNotInitialized(violations);
            return gson.toJson(notInitializedAccount);
        } else {

            return applyAccountRules(newTransaction, accountAtomicReference, violations);
        }

    }

    /**
     * Finds an account
     */
    public Account getAccount(final AtomicReference<Account> accountAtomicReference) {
        return accountAtomicReference.get();
    }

    /**
     * creates a new account
     * Account to create
     */
    public void createNewAccount(AtomicReference<Account> accountAtomicReference,
                                 final Account account) {
        accountAtomicReference.compareAndSet(null, account);
        accountAtomicReference.get().setTransactions(new CopyOnWriteArrayList<>());
    }

    /**
     * Create a violation when the account is not initialized
     * <p>
     * {@link Integer }
     *
     * @return possible object is
     * {@link Account }
     */
    public OutputFormat accountNotInitialized(LinkedList<String> violations) {
        Account notInitializedAccount = new Account();
        violations.add(Constants.ACC_NOT_INIT);
        return new OutputFormat(notInitializedAccount, violations);
    }

    /**
     * Checks if the card is active
     *
     * @param account allowed object is
     *                {@link Account }
     * @return boolean
     * possible object is
     * {@link boolean }
     */
    public boolean checkIfCardActive(final Account account) {
        return account.isActiveCard();
    }

    /**
     * @param violation
     * @return
     */
    public LinkedList<String> addViolationToAccount
    (final String violation) {
        LinkedList<String> violations = new LinkedList<>();
        violations.add(violation);
        return violations;
    }

    /**
     * Checks if in a 2 minutes interval are 3 transactions
     *
     * @return possible object is
     * {@link boolean }
     */
    public void addTransactionToAccount(final AtomicReference<Account> accountAtomicReference,
                                        final Transaction transaction) {
        accountAtomicReference.get().addTransaction(transaction);
    }

    public String applyAccountRules(final Transaction newTransaction,
                                    final AtomicReference<Account> accountAtomicReference,
                                    LinkedList<String> violations) {
        Collection<Rules> targetRule = RulesFactory.getAllRulesAndApply()
                .orElseThrow(() -> new IllegalArgumentException("Invalid Rule"));

        for (Rules rule : targetRule) {
            if (!rule.applyRule(accountAtomicReference,
                    rule.getRuleString(),
                    newTransaction)) {

                addViolationToAccount(
                        rule.getRuleString());

                return gson.toJson(formatOutput(accountAtomicReference.get(), violations));
            }
        }
        return gson.toJson(formatOutput(accountAtomicReference.get(), violations));
    }
}
