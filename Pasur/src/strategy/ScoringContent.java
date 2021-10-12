package strategy;

import ch.aplu.jcardgame.Hand;

/**
 * Indirection context class for calling the strategy for calculating score
 */
public class ScoringContent {
    private final IScoringStrategy scoringStrategy;
    
    public ScoringContent(IScoringStrategy scoringStrategy) {
        this.scoringStrategy = scoringStrategy;
    }
    
    public int calculateScore(Hand pickedCards, Hand surs)  {
        return scoringStrategy.calculateScore(pickedCards, surs);
    }
}
