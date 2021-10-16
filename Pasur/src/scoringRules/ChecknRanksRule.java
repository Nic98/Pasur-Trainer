package scoringRules;

import ch.aplu.jcardgame.Hand;
import pasur.Rank;

/**
 * The general rule to check the number of target rank card
 * and multiply by the score per target, then player will earn score
 */
public class ChecknRanksRule implements ScoringRule {

    /**
     * Scoring&Counting&Target constant can be changed in future implementation
     */
    private final int SCORE_PER_TARGET;
    private final Rank TARGET_RANK;

    /**
     * Constructor
     */
    public ChecknRanksRule(int score, Rank rank) {
        this.SCORE_PER_TARGET  = score;
        this.TARGET_RANK = rank;
    }

    /**
     * @param pickedCards Current picked cards in the round
     * @param surs        Current surs earned in the round
     * @return based on the picked cards and surs calculate the score
     */
    @Override
    public int calculateScore(Hand pickedCards, Hand surs) {
        int nTargets = pickedCards.getNumberOfCardsWithRank(TARGET_RANK) +
                surs.getNumberOfCardsWithRank(TARGET_RANK);

        return nTargets * SCORE_PER_TARGET;
    }

}
