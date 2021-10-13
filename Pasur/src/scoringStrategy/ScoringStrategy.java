package scoringStrategy;

import ch.aplu.jcardgame.Hand;

/**
 * Strategy pattern used
 * Interface for calculating scores base on
 * picked cards and surs in the current round.
 *
 * More strategies will implement the interface.
 * (Polymorphism)
 */
public interface ScoringStrategy {
    /**
     *
     * @param pickedCards Current picked cards in the round
     * @param surs Current surs earned in the round
     * @return based on the picked cards and surs calculate the score
     */
    int calculateScore(Hand pickedCards, Hand surs);
}
