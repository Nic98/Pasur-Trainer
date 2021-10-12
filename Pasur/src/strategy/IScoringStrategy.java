package strategy;

import ch.aplu.jcardgame.Hand;
public interface IScoringStrategy {

    default int calculateScore(Hand pickedCards, Hand surs) {
        return 0;
    }
}
