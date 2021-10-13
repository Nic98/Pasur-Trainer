package scoringRulesContext;

import ch.aplu.jcardgame.Hand;
import scoringRules.ScoringRule;

/**
 * Indirection used.
 * The ScoringRuleContext class maintains a reference to the interface
 * of ScoringRule for accessing all the concrete strategies/rules,
 * and the Client(Pasur class) only need to communicate with this class,
 * other than calling (calculateScore) for each strategy/rule at a time.
 * The use of tho class is also a part of the strategy pattern.
 */
public class ScoringRuleContext {

    private final ScoringRule scoringRuleContext;

    /**
     * Constructor
     * @param scoringRuleContext create a scoringRuleContext for Pasur class
     *                           in order to use its method calculateScore
     */
    public ScoringRuleContext(ScoringRule scoringRuleContext) {
        this.scoringRuleContext = scoringRuleContext;
    }

    /**
     *
     * @param pickedCards Current picked cards in the round
     * @param surs Current surs earned in the round
     * @return based on the picked cards and surs calculate the score
     */
    public int calculateScore(Hand pickedCards, Hand surs) {
        return scoringRuleContext.calculateScore(pickedCards, surs);
    }
}
