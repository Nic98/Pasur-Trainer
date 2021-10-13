package scoringRules;

import ch.aplu.jcardgame.Hand;
import pasur.Suit;

public class SevenPlusClubsRule implements ScoringRule {

    /**
     * Scoring&Counting constant can be changed in future implementation
     */
    private static final int SCORE = 7;
    private static final int N_CLUBS_REQUIRED = 7;

    /**
     * 7 points scored if the player has more than 7 of clubs
     * @param pickedCards Current picked cards in the round
     * @param surs        Current surs earned in the round
     * @return based on the picked cards and surs calculate the score
     */
    @Override
    public int calculateScore(Hand pickedCards, Hand surs) {
        int nClubs = pickedCards.getNumberOfCardsWithSuit(Suit.CLUBS) +
                surs.getNumberOfCardsWithSuit(Suit.CLUBS);
        if (nClubs >= N_CLUBS_REQUIRED) {
            return SCORE;
        }
        return 0;
    }
}
