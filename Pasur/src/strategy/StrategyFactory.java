package strategy;

public class StrategyFactory {
    private static StrategyFactory strategyFactory = null;

    private IScoringStrategy scoringStrategy;

    private StrategyFactory() {

    }

    public static StrategyFactory getInstance() {
        if (strategyFactory == null) {
            strategyFactory = new StrategyFactory();
        }
        return strategyFactory;
    }

    public IScoringStrategy createStrategy(String type) {
        switch (type) {
            case "ace":
                scoringStrategy = new AceStrategy();
                break;
            case "clubsTwo":
                scoringStrategy = new ClassTwoStrategy();
                break;
            // case
        }
        return scoringStrategy;
    }
}
