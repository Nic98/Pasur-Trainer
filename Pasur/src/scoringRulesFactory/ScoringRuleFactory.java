package scoringRulesFactory;

import scoringRules.*;

/**
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
                scoringRule = new DiamondTenRule();
                break;
            case "ClubTwoRule":
                scoringRule = new ClubTwoRule();
                break;
            case "AceRule":
                scoringRule = new AceRule();
                break;
            case "JackRule":
                scoringRule = new JackRule();
                break;
            case "SurRule":
                scoringRule = new SurRule();
                break;
        }

        return scoringRule;
    }
}
