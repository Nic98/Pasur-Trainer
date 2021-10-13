package scoringRules;

import ch.aplu.jcardgame.Hand;
import pasur.Rank;

public class AceRule implements ScoringRule {

    /**
     * Scoring constant can be changed in future implementation
     */
    private static final int SCORE = 1;

    /**
     * 1 points per Ace scored
     * @param pickedCards Current picked cards in the round
     * @param surs        Current surs earned in the round
     * @return based on the picked cards and surs calculate the score
     */
    @Override
    public int calculateScore(Hand pickedCards, Hand surs) {
        int nAces =  pickedCards.getNumberOfCardsWithRank(Rank.ACE) +
                surs.getNumberOfCardsWithRank(Rank.ACE);
        return nAces * SCORE;
    }
}
