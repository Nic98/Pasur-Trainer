package strategy;

import ch.aplu.jcardgame.Hand;
import pasur.Rank;

public class AceStrategy implements IScoringStrategy{

    private static final int SCORING = 1;

    @Override
    public int calculateScore(Hand pickedCards, Hand surs) {
        int numberOfAces = pickedCards.getNumberOfCardsWithRank(Rank.ACE) +
                surs.getNumberOfCardsWithRank(Rank.ACE);
        return numberOfAces * SCORING;
    }
}
