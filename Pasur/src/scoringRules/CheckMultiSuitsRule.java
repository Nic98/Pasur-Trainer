package scoringRules;

import ch.aplu.jcardgame.Hand;
import pasur.Suit;

/**
 * The general rule to check the number of target suit cards
 * and calculate the number of the cards, if the number is larger
 * than the required number, then player will earn score
 */
public class CheckMultiSuitsRule implements ScoringRule{

    /**
     * Scoring&Counting&Target constant can be changed in future implementation
     */
    private final int TARGET_SCORE;
    private final int N_SUITS_REQUIRED;
    private final Suit TARGET_SUIT;

    /**
     * Constructor
     */
    public CheckMultiSuitsRule(int score, int nRequried, Suit suit) {
        this.TARGET_SCORE = score;
        this.N_SUITS_REQUIRED = nRequried;
        this.TARGET_SUIT = suit;
    }

    /**
     * 7 points scored if the player has more than 7 of clubs
     * @param pickedCards Current picked cards in the round
     * @param surs        Current surs earned in the round
     * @return based on the picked cards and surs calculate the score
     */
    @Override
    public int calculateScore(Hand pickedCards, Hand surs) {
        int nSuits = pickedCards.getNumberOfCardsWithSuit(TARGET_SUIT) +
                surs.getNumberOfCardsWithSuit(TARGET_SUIT);
        if (nSuits >= N_SUITS_REQUIRED) {
            return TARGET_SCORE;
        }
        return 0;
    }
}
