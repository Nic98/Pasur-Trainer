package compositeScoringRules;

import ch.aplu.jcardgame.Hand;
import scoringRules.ScoringRule;

import java.util.ArrayList;

/**
 * Composite Pattern used.
 * CompositeScoringRule is the class that contains all the sub-elements(scoring rules),
 * It has the ability to sum up the total score by calling the method calculateScore
 * in its sub-elements(scoring rules).
 * Also, it has the method to add or remove rules in future implementation.
 * It works with all sub-elements(scoring rules) only via the component interface (ScoringRule).
 */
public class CompositeScoringRule implements ScoringRule {

    /**
     * Composite all the scoring rules in an Array List
     */
    private final ArrayList<ScoringRule> allRules = new ArrayList<ScoringRule>();

    /**
     * @param pickedCards Current picked cards in the round
     * @param surs        Current surs earned in the round
     * @return based on the picked cards and surs calculate the score
     */
    @Override
    public int calculateScore(Hand pickedCards, Hand surs) {
        int totalScore = 0;
        for (ScoringRule rule : allRules) {
            totalScore += rule.calculateScore(pickedCards, surs);
        }
        return totalScore;
    }

    /**
     * Composite pattern method to remove unwanted scoring rules in the
     * composite structure. (Method for future implementation)
     * @param removedRule scoring rule to remove
     */
    public void removeRule(ScoringRule removedRule) {
        allRules.remove(removedRule);
    }

    /**
     * Composite pattern method to add new scoring rules in the
     * composite structure.
     * @param addedRule new scoring rule to add
     */
    public void addRule(ScoringRule addedRule) {
        allRules.add(addedRule);
    }
}
