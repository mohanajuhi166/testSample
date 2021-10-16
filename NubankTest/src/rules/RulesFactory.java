package rules;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RulesFactory {

    static Map<String, Rules> rulesMap = new HashMap<>();

    static {

        rulesMap.put(Constants.CARD_NOT_ACTIVE, new CardNotActiveRule());
        rulesMap.put(Constants.HIGH_FREQ_SMALL_INTERVAL, new HighFreqRule());
        rulesMap.put(Constants.DOUBLED_TRANSACTION, new SimilarTransactionRule());
        rulesMap.put(Constants.INSUFF_LIMIT, new AvailableLimitRule());


    }

    public static Optional<Rules> getRule(String rule) {
        return Optional.ofNullable(rulesMap.get(rule));
    }

    public static Optional<Collection<Rules>> getAllRulesAndApply() {
        return Optional.ofNullable(rulesMap.values());
    }
}
