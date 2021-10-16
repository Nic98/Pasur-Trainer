package scoringRules;

import ch.aplu.jcardgame.Hand;

/**
 * The combination patterns (factory+strategy) learnt form workshop 8.
 *
 * Strategy pattern used
 * Interface for calculating scores base on
 * picked cards and surs in the current round.
 *
 * More rules will implement this interface.
 * (Polymorphism)
 *
 * Not using abstract instead using interface
 * more specific reason will include in the report.
 *
 * We only want the class have a clear purpose that is
 * to calculate the score by different rules.
 *

 */
public interface ScoringRule {
    /**
     *
     * @param pickedCards Current picked cards in the round
     * @param surs Current surs earned in the round
     * @return based on the picked cards and surs calculate the score
     */
    int calculateScore(Hand pickedCards, Hand surs);
}
