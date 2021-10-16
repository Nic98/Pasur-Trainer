package scoringRules;

import ch.aplu.jcardgame.Hand;
import pasur.Rank;

public class ChecknRanksRule implements ScoringRule{

    private final int SCORE_PER_TARGET;
    private final Rank TARGET_RANK;

    public ChecknRanksRule(int score, Rank rank) {
        this.SCORE_PER_TARGET  = score;
        this.TARGET_RANK = rank;
    }

    @Override
    public int calculateScore(Hand pickedCards, Hand surs) {
        int nTargets = pickedCards.getNumberOfCardsWithRank(TARGET_RANK) +
                surs.getNumberOfCardsWithRank(TARGET_RANK);

        return nTargets * SCORE_PER_TARGET;
    }

}
