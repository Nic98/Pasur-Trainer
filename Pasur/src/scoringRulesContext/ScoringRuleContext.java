package scoringRulesContext;

import ch.aplu.jcardgame.Hand;
import compositeScoringRules.CompositeScoringRule;
import scoringRules.ScoringRule;

/**
 * Structure learnt form https://refactoring.guru/design-patterns/strategy
 * Strategy pattern used.
 * Indirection used.
 *
 * The ScoringRuleContext class maintains a reference to the interface
 * of ScoringRule for accessing all the concrete strategies/rules,
 * and the Client(Pasur class) only need to communicate with this class,
 * other than calling (calculateScore) for each strategy/rule at a time.
 *
 * The use of tho class is a combination of strategy pattern and indirection principle.
 */
public class ScoringRuleContext {

    private static CompositeScoringRule scoringRuleContext;
    /**
     * Constructor
     * @param scoringRuleContext create a scoringRuleContext for Pasur class
     *                           in order to use its method calculateScore
     */

    public ScoringRuleContext(CompositeScoringRule scoringRuleContext) {this.scoringRuleContext = scoringRuleContext;};
    /**
     *
     * @param pickedCards Current picked cards in the round
     * @param surs Current surs earned in the round
     * @return based on the picked cards and surs calculate the score
     */
    public int calculateScore(Hand pickedCards, Hand surs) {
        return scoringRuleContext.calculateScore(pickedCards, surs);
    }

    /**
     * Handling Pasur class adding a new scoring rule into the composite structure
     * @param newAddedRule new added scoring rule
     */
    public void addOneRule(ScoringRule newAddedRule) {
        this.scoringRuleContext.addRule(newAddedRule);
    }

    /**
     * Handling Pasur class adding a set of new rules into the composite structure,
     * for future development, there may be a complete MODE C of game, that combine
     * the set of rules A form MODE A and set of rules B from MODE B,
     * the MODE C combine the rules for A and B,
     * then it's easy to just call the below method.
     * @param newSetRules new added set of scoring rules
     */
    public void addOneSetRules(CompositeScoringRule newSetRules) {
        this.scoringRuleContext.addRule(newSetRules);
    }

    /**
     * Handling Pasur class remove a scoring rule from the composite structure
     * @param removedRule target scoring rule to be deleted
     */
    public void removeOneRule(ScoringRule removedRule) {
        this.scoringRuleContext.removeRule(removedRule);
    }

    /**
     * Handling Pasur class remove a set of rules from the composite structure,
     * for future development, there may be a complete MODE of game,
     * that don't one part of the rules from the current MODE,
     * then it's easy to just call the below method.
     * @param removedSetRules set of scoring rules needed to be deleted
     */
    public void removeOneSetRules(CompositeScoringRule removedSetRules) {
        this.scoringRuleContext.removeRule(removedSetRules);
    }
}
