package scoringRulesFactory;

import pasur.Rank;
import pasur.Suit;
import scoringRules.*;

/**
 * The combination patterns (factory+strategy) learnt form workshop 8.
 *
 * Factory pattern used & Singleton pattern used
 * There is only one factory needed in the whole game project.
 * The class is used for creating different scoring rules
 * without coupling the client code (Pasur) to concrete scoring rule classes.
 * Also, it improves future development while new scoring rule added.
 */
public class ScoringRuleFactory {

    private ScoringRule scoringRule;

    /**
     * Making a Singleton Factory
     */
    private static ScoringRuleFactory ruleFactory = null;

    private ScoringRuleFactory() {};

    public static ScoringRuleFactory getFactoryInstance() {
        if (ruleFactory == null) {
            ruleFactory = new ScoringRuleFactory();
        }
        return ruleFactory;
    }

    /**
     * Factory method for creating new scoring rule respectively
     * by receiving the names of the scoring rule
     * if the name of the scoring rule is not implemented
     * it will return a null object with type ScoringRule
     *
     * @param ruleName Name of the scoring rule
     * @return a ScoringRule type object
     */
    public ScoringRule createNewScoringRule(String ruleName) {

        switch (ruleName) {
            case "SevenPlusClubsRule":
                scoringRule = new SevenPlusClubsRule();
                break;
            case "DiamondTenRule":
                // scoringRule = new DiamondTenRule();
                scoringRule = new CheckCardExistRule(3, Suit.DIAMONDS, Rank.TEN);
                break;
            case "ClubTwoRule":
                //scoringRule = new ClubTwoRule();
                scoringRule = new CheckCardExistRule(2, Suit.CLUBS, Rank.TWO);
                break;
            case "AceRule":
                scoringRule = new AceRule();
                // scoringRule = new ChecknRanksRule(1, Rank.ACE);
                break;
            case "JackRule":
                scoringRule = new JackRule();
                //scoringRule = new ChecknRanksRule(1, Rank.JACK);
                break;
            case "SurRule":
                scoringRule = new SurRule();
                break;
        }

        return scoringRule;
    }
}
