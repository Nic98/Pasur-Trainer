package strategy;

import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;
import java.util.List;

public class CompositeStrategy implements IScoringStrategy {

    // polymorphism
    // save all needed strategies
    private final List<IScoringStrategy> scoringStrategies = new ArrayList<IScoringStrategy>();

    @Override
    public int calculateScore(Hand pickedCards, Hand surs) {
        int totalScore = 0;
        for (IScoringStrategy strategy: scoringStrategies) {
            totalScore += strategy.calculateScore(pickedCards, surs);
        }
        return totalScore;
    }

    public void addStrategy(IScoringStrategy strategy) {
        scoringStrategies.add(strategy);
    }

    public void removeStrategy(IScoringStrategy strategy) {
        scoringStrategies.remove(strategy);
    }
}
