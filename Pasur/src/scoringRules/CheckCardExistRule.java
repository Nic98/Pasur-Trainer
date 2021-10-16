package scoringRules;

import ch.aplu.jcardgame.Hand;
import pasur.Rank;
import pasur.Suit;

public class CheckCardExistRule implements ScoringRule{

    private static int SCORE_PER_TARGET;
    private static Suit TARGET_SUIT;
    private static Rank TARGET_RANK;

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
        System.out.println(TARGET_SUIT);
        System.out.println(TARGET_RANK);
        if (pickedCards.getCard(TARGET_SUIT, TARGET_RANK) != null ||
                surs.getCard(TARGET_SUIT, TARGET_RANK) != null) {
            return SCORE_PER_TARGET;
        }
        return 0;
    }
}
