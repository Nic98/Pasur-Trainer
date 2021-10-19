package scoringRules;

import ch.aplu.jcardgame.Hand;
import pasur.Rank;
import pasur.Suit;

/**
 * The general rule to check one card is in the player's
 * pickedHand or surs deck, then player will earn score
 */
public class CheckCardExistRule implements ScoringRule{

    /**
     * Scoring&TargetSuit&TargetRank constant can be changed in future implementation
     */
    private final int SCORE_PER_TARGET;
    private final Suit TARGET_SUIT;
    private final Rank TARGET_RANK;

    /**
     * Constructor
     */
    public CheckCardExistRule(int score, Suit suit, Rank rank) {
        this.SCORE_PER_TARGET = score;
        this.TARGET_SUIT = suit;
        this.TARGET_RANK = rank;
    }

    /**
     * @param pickedCards Current picked cards in the round
     * @param surs        Current surs earned in the round
     * @return based on the picked cards and surs calculate the score
     */
    @Override
    public int calculateScore(Hand pickedCards, Hand surs) {
        if (pickedCards.getCard(TARGET_SUIT, TARGET_RANK) != null ||
                surs.getCard(TARGET_SUIT, TARGET_RANK) != null) {
            return SCORE_PER_TARGET;
        }
        return 0;
    }
}
