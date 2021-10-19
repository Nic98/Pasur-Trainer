package compositeScoringRules;

import ch.aplu.jcardgame.Hand;
import scoringRules.ScoringRule;

import java.util.ArrayList;

/**
 * Composite Pattern used.
 * CompositeScoringRule is the class that contains all the sub-elements(scoring rules),
 * It has the ability to sum up the total score by calling the method calculateScore
 * in its sub-elements(scoring rules).
 *
 * In the Pasur case, the composite pattern is actually a structure storing all the
 * scoring rules with no more further complex leaf structure.
 * Therefore, it is fine to not implement the ScoringRule interface for the current case,
 * however for future development, there may be a rule including several sub-rules,
 * then the composite pattern will come to use.
 *
 * Also, it has the method to add/remove (multiple)rules in future implementation.
 *
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
    

    /**
     * Method to get all the rules in the composite structure
     * may need the method in future implementation
     * @return an array list of scoring rule that the game is using
     */
    public ArrayList<ScoringRule> getAllRules() {
        return this.allRules;
    }
}
