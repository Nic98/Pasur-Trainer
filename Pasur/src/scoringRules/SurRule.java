package scoringRules;

import ch.aplu.jcardgame.Hand;

public class SurRule implements ScoringRule {

    /**
     * Scoring constant can be changed in future implementation
     */
    private static final int SCORE = 5;

    /**
     * 5 points per Jack scored
     * @param pickedCards Current picked cards in the round
     * @param surs        Current surs earned in the round
     * @return based on the picked cards and surs calculate the score
     */
    @Override
    public int calculateScore(Hand pickedCards, Hand surs) {

        return surs.getNumberOfCards() * SCORE;
    }
}
