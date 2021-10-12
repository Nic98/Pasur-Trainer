package strategy;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import pasur.Rank;
import pasur.Suit;

/**
 * Not completed
 */
public class ClassTwoStrategy implements IScoringStrategy {
    private static final int SCORING = 2;

    @Override
    public int calculateScore(Hand pickedCards, Hand surs) {
        Card pickedClubsTwo = pickedCards.getCard(Suit.CLUBS, Rank.TWO);
        return 0;
    }
}
