package scoringRules;

import ch.aplu.jcardgame.Hand;
import pasur.Rank;
import pasur.Suit;

public class DiamondTenRule implements ScoringRule{

    /**
     * Scoring constant can be changed in future implementation
     */
    private static final int SCORE = 3;

    /**
     * 3 points scored if the player has ten of diamonds
     * @param pickedCards Current picked cards in the round
     * @param surs        Current surs earned in the round
     * @return based on the picked cards and surs calculate the score
     */
    @Override
    public int calculateScore(Hand pickedCards, Hand surs) {
        if (pickedCards.getCard(Suit.DIAMONDS, Rank.TEN) != null ||
                surs.getCard(Suit.DIAMONDS, Rank.TEN) != null) {
            return SCORE;
        }

        return 0;
    }
}
