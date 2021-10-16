package pasur;

/**
 * @author Alireza Ostovar
 * 29/09/2021
 */

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import compositeScoringRules.CompositeScoringRule;
import config.Configuration;
import logWriter.LogWriter;
import playerFactory.PlayerFactory;
import scoringRules.ScoringRule;
import scoringRulesContext.ScoringRuleContext;
import scoringRulesFactory.ScoringRuleFactory;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class Pasur
{
    public static final String VERSION = "1.0";
    public static final String ON_RESET = "onReset";
    public static final String ON_UPDATE_SCORE = "onUpdateScore";
    public static final String ON_CARD_TRANSFER = "onCardTransfer";
    public static final String ON_GAME_END = "onGameEnd";

    // used for the simulation
    private static final Random random = new Random(Configuration.getInstance().getSeed());

    private static final int SCORE_TO_WIN = 62;
    private static final int N_HAND_CARDS = 4;
    private final int nPlayers;

    private boolean paused = true;
    private boolean gameStarted = false;

    /**
     * NEW ADDED ATTRIBUTE:
     * Initially set to false at the first round,
     * recording the state of the current game,
     * when a round is finished it will be set to true,
     * and when new round starts, it will be set to false again.
     */
    private boolean isCurrentRoundFinished;

    /**
     * NEW ADDED ATTRIBUTE/CLASSES:
     * ruleFactory for creating all the scoring rules for Pasur instantiation
     * allRules for containing all the scoring rules for Pasur access them at once
     * rulesContext for communicating with all the scoring rules for Pasur access the rule methods
     * logWriter for write logs in the "pasur.log" file
     * playerFactory for creating different class of player in future development
     */
    // static final: only exist by itself and will not be changed or modified
    private static final ScoringRuleFactory ruleFactory = ScoringRuleFactory.getFactoryInstance();

    // may remove or add more rules in future development
    private CompositeScoringRule allRules = new CompositeScoringRule();

    // as the rulesContext is actually the composite structure, it may be modified in the future implementation
    private ScoringRuleContext rulesContext;

    // static final: only exist by itself and will not be changed or modified
    private static final LogWriter logWriter = LogWriter.getLogWriterInstance();

    // static final: only exist by itself and will not be changed or modified
    private static final PlayerFactory playerFactory = PlayerFactory.getPlayerFactoryInstance();

    /**
     * Comment added for better understanding:
     * A class to bundle information about the card suits and ranks.
     */
    private final Deck deck;
    /**
     * Comment added for better understanding:
     * The deck initially with 52 cards,
     * dealing cards to the pool and the players,
     * a round is finished until the deck is empty.
     */
    private Hand deckHand;
    /**
     * Comment added for better understanding:
     * Represent the cards in the pool.
     */
    private final Hand poolHand;
    private final Player[] players;

    private PropertyChangeSupport propertyChangePublisher = new PropertyChangeSupport(this);

    public Pasur(int nPlayers) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException,
            InstantiationException {
        // Instantiate players
        this.nPlayers = nPlayers;
        players = new Player[nPlayers];

        /**
         * DELETE PART:
         *
         * Class<?> clazz;
         * clazz = Class.forName(Configuration.getInstance().getPlayer0class());
         * players[0] = (Player) clazz.getConstructor(int.class).newInstance(0);
         *
         * clazz = Class.forName(Configuration.getInstance().getPlayer1class());
         * players[1] = (Player) clazz.getConstructor(int.class).newInstance(1);
         *
         */


        /**
         * CHANGED PART:
         * Now the program will instantiate all classes
         * of players by the playerFactory, and not
         * create by hard code one by one.
         */
        for (int i = 0; i <nPlayers; i++) {
            Class<?> clazz = Class.forName(Configuration.getInstance().getAllPlayerClasses().get(i));
            players[i] = playerFactory.createNewPlayer(i, clazz);
        }

        deck = new Deck(Suit.values(), Rank.values(), "cover", suit -> Rank.getCardValuesArray());

        poolHand = new Hand(deck);

        for (int i = 0; i < nPlayers; i++)
        {
            Player player = players[i];

            Hand hand = new Hand(deck);
            player.setHand(hand);

            // set the picked cards for this player
            Hand pickedCards = new Hand(deck);
            player.setPickedCards(pickedCards);

            // set the sur cards for this player
            Hand surCards = new Hand(deck);
            player.setSurs(surCards);
        }

        /**
         * UPDATED PART:
         * Instantiate all the scoring rules
         * Adding more rules can simply input a new String (name of the scoring rule)
         */
        ScoringRule SevenPlusClubsRule = ruleFactory.createNewScoringRule("SevenPlusClubsRule");
        ScoringRule DiamondTenRule =ruleFactory.createNewScoringRule("DiamondTenRule");
        ScoringRule ClubTwoRule =ruleFactory.createNewScoringRule("ClubTwoRule");
        ScoringRule AceRule =ruleFactory.createNewScoringRule("AceRule");
        ScoringRule JackRule =ruleFactory.createNewScoringRule("JackRule");
        ScoringRule SurRule =ruleFactory.createNewScoringRule("SurRule");


        /**
         * UPDATED PART:
         * Put all the rules into the composite structure
         * then Pausr can call the calculateScore method at once
         * no need to get each rule class and call the method respectively
         * For future development, if there is new scoring rule,
         * just call the addRule method to add it to the composite structure
         */
        allRules.addRule(SevenPlusClubsRule);
        allRules.addRule(DiamondTenRule);
        allRules.addRule(ClubTwoRule);
        allRules.addRule(AceRule);
        allRules.addRule(JackRule);
        allRules.addRule(SurRule);

        /**
         * UPDATED PART:
         * Pasur class can just use the context class to communicate with one rule interface
         * then is able to access all the scoring rules, instead access them one by one
         *
         * Use of combination of indirection and composite pattern
         *
         * the client(pausr) can directly access all the rules at once through the
         * context which is also the composite structure.
         */
        rulesContext = new ScoringRuleContext(allRules);

    }

    public synchronized void pauseGame()
    {
        try {
            wait();
        } catch (InterruptedException ex) {
            ex.getStackTrace();
        }
    }

    public synchronized void resumeGame()
    {
        paused = false;
        notifyAll();
    }

    public void play()
    {
        if(gameStarted)
            return;
        gameStarted = true;

        logWriter.writeLog("Game starts...");

        Player winner = null;

        int currentStartingPlayerPos = 0; // players should alternate for starting each round of game
        Player lastPlayerWhoPickedAcard = players[0];
        int roundOfGame = 0;
        List<Card> cardList = new ArrayList<>(1);
        /**
         * Comment added for better understanding:
         * Simulation starts here, while there is no winner keep playing the game
         */
        while(winner == null)
        {

            roundOfGame++;
            logWriter.writeLog("Round " + roundOfGame + " of the game starts...");
            boolean isFirstRound = true;
            reset();

            if (paused) {
                pauseGame();
            }

            while (!deckHand.isEmpty())
            {
                /**
                 * UPDATED PART:
                 * set the attribute to false when a round starts
                 */
                isCurrentRoundFinished = false;

                if (paused) {
                    pauseGame();
                }

                dealingOutToPlayers(currentStartingPlayerPos);

                if(isFirstRound)
                {
                    dealingOutToPool();
                    isFirstRound = false;
                }

                boolean isLastRound = deckHand.isEmpty();

                for(int i = 0; i < N_HAND_CARDS; i++)
                {
                    if (paused) {
                        pauseGame();
                    }

                    for(int j = 0, k = currentStartingPlayerPos; j < nPlayers; j++)
                    {
                        if (paused) {
                            pauseGame();
                        }

                        Player player = players[k];
                        Map.Entry<Card, Set<Card>> playedCard_cardsToPick = player.playCard(poolHand);
                        Card playedCard = playedCard_cardsToPick.getKey();
                        Set<Card> cardsToPick = playedCard_cardsToPick.getValue();

                        cardList.clear();
                        cardList.add(playedCard);
                        transfer(cardList, poolHand, false);
                        playedCard.setVerso(false);

                        if(!cardsToPick.isEmpty())
                        {
                            lastPlayerWhoPickedAcard = player;

                            cardList.clear();
                            for(Card card : cardsToPick)
                            {
                                if (paused) {
                                    pauseGame();
                                }

                                cardList.add(card);
                            }

                            cardList.add(playedCard);
                            transfer(cardList, player.getPickedCards(), false);
                            for(Card card : cardList)
                            {
                                card.setVerso(true);
                            }

                            logWriter.writeLog(player.toString() + " picks " + toString(cardList));
                            if(isAsur(playedCard, isLastRound))
                            {
                                // player has a sur. If the other players have a sur this sur will be used to remove one of their surs.
                                // otherwise it will be added as a sur for this player

                                logWriter.writeLog(player.toString() + " scores a sur");
                                int nOtherPlayersWithSure = 0;
                                for(int r = 0; r < nPlayers; r++)
                                {
                                    if(player != players[r] && !players[r].getSurs().isEmpty())
                                    {
                                        nOtherPlayersWithSure++;
                                    }
                                }

                                if(nOtherPlayersWithSure == nPlayers - 1)
                                {
                                    // other players have surs, so they lose one of their surs
                                    for(int r = 0; r < nPlayers; r++)
                                    {
                                        Player otherPlayer = players[r];
                                        if(player != otherPlayer)
                                        {
                                            Card surCard = otherPlayer.getSurs().get(otherPlayer.getSurs().getNumberOfCards() - 1);
                                            cardList.clear();
                                            cardList.add(surCard);
                                            transfer(cardList, otherPlayer.getPickedCards(), false);
                                            surCard.setVerso(true);
                                        }
                                    }
                                }else
                                {
                                    // other players don't have surs, so we add this as a sur for this player
                                    playedCard.setVerso(false);
                                    cardList.clear();
                                    cardList.add(playedCard);
                                    transfer(cardList, player.getSurs(), false);
                                }
                            }
                        }else
                        {
                            logWriter.writeLog(player.toString() + " picks " + toString(cardsToPick));
                            // the played card of the player can't pick any card, so we have to leave it at the pool
                        }

                        k++;
                        if(k == nPlayers)
                            k = 0;

                        updateScores();
                    }
                }

                if(isLastRound)
                {
                    // give remaining cards in the pool to the last player who picked up a card

                    List<Card> poolCards = poolHand.getCardList();
                    if(!poolCards.isEmpty())
                        logWriter.writeLog(lastPlayerWhoPickedAcard + " picks " + toString(poolCards) + " at the end of this round of game");
                    cardList.clear();
                    for(int i = 0; i < poolCards.size(); i++)
                    {
                        if (paused) {
                            pauseGame();
                        }

                        Card card = poolCards.get(i);
                        cardList.add(card);
                        card.setVerso(true);
                    }
                    transfer(cardList, lastPlayerWhoPickedAcard.getPickedCards(), false);
                }
            }

            /**
             * UPDATED PART:
             * set the attribute to true when a round is finished
             */
            isCurrentRoundFinished = true;
            updateScores();

            currentStartingPlayerPos++;
            if(currentStartingPlayerPos == nPlayers)
                currentStartingPlayerPos = 0;

            logWriter.writeLog("Round " + roundOfGame + " of the game ends...");

            List<Player> playersWithEnoughScore = null;
            for(int i = 0; i < nPlayers; i++)
            {
                Player player = players[i];
                if(player.getScore() >= SCORE_TO_WIN)
                {
                    if(playersWithEnoughScore == null)
                        playersWithEnoughScore = new ArrayList<>();

                    playersWithEnoughScore.add(player);
                }
            }

            if(playersWithEnoughScore == null)
            {
                continue;
            }else
            {
                if(playersWithEnoughScore.size() > 1)
                {
                    // there are more than one player with the score above the threshold
                    playersWithEnoughScore.sort((o1, o2) -> -Integer.compare(o1.getScore(), o2.getScore()));
                    if(playersWithEnoughScore.get(0).getScore() == playersWithEnoughScore.get(1).getScore())
                    {
                        // the score of the top two players are the same, so we have to play another round
                        continue;
                    }
                }

                // we have a winner
                winner = playersWithEnoughScore.get(0);
            }
        }

        logWriter.writeLog("Game ends...");
        String winningText = winner.toString() + " is the winner!";

        propertyChangePublisher.firePropertyChange(ON_GAME_END, null, winningText);

        logWriter.writeLog(winningText);

        /**
         * Close the file when the game ends.
         */
        logWriter.closeFile();
    }

    private boolean isAsur(Card playedCard, boolean isLastRound)
    {
        if(poolHand.isEmpty())
        {
            // pool has become empty, potentially a sur
            if(!isLastRound && playedCard.getRank() != Rank.JACK)
            {
                // it is only a sur if the played card is not a jack and also we are not in the last round of play
                return true;
            }
        }

        return false;
    }

    /**
     * Reset to start a new round of the game
     */
    private void reset()
    {
        for(int i = 0; i < nPlayers; i++)
        {
            Player player = players[i];
            player.reset();
        }

        poolHand.removeAll(false);

        deckHand = deck.toHand(false);
        deckHand.setVerso(true);

        updateScores();

        propertyChangePublisher.firePropertyChange(ON_RESET, null, null);
    }

    private void updateScores()
    {
        String scoreString = "";
        for (int i = 0; i < nPlayers; i++)
        {
            if(i != 0)
                scoreString += "        ";

            Player player = players[i];

            /**
             * cuurScore: the score earned from the current picked cards and surs.
             *
             * Calculate the score will earned from the picked cards and surs
             * if the round at the moment just started, the currScore will be 0,
             * as there is neither card in the pickedCard nor surs.
             */
            int currScore = rulesContext.calculateScore(player.pickedCards, player.surs);

            /**
             * Add the currScore up with the overall game score
             * and set it as the overall game score.
             *
             * **
             * Here not updates the scoreFromLastRound attribute,
             * as the attribute always represents the score from last round
             */
            player.setScore(player.getScoreFromLastRound() + currScore);

            /**
             * If the current round is finished,
             *
             * **
             * Here updates the scoreFromLastRound attribute,
             * to the overall game score (which is the last round score),
             * as the round is finished.
             */
            if (isCurrentRoundFinished) {
                player.setScoreFromLastRound(player.getScore());
            }

            /**
             * Always print out the overall game score
             */
            scoreString += player.toString() + " = " + player.getScore() + " (" + player.getSurs().getNumberOfCards() + " Surs)";
        }

        propertyChangePublisher.firePropertyChange(ON_UPDATE_SCORE, null, scoreString);
//        scoreLabel.setText(scoreString);
        logWriter.writeLog("Total Running Scores: " + scoreString);
    }

    private void dealingOutToPlayers(int currentStartingPlayerPos)
    {
        logWriter.writeLog("Dealing out to players...");
        List<Card> cardList = new ArrayList<>(1);
        for (int i = 0, k = currentStartingPlayerPos; i < nPlayers; i++)
        {
            Player player = players[k];
            Hand hand = player.getHand();

            for (int j = 0; j < N_HAND_CARDS; j++)
            {
                if (paused) {
                    pauseGame();
                }

                // in a real game we should shuffle the cards at the very beginning of the game and
                // take the cards from the bottom of the deck for dealing, but here we do not shuffle the cards and take cards from random positions
                // in the deck so that we can reproduce the same result for the simulation every time (for marking purposes)
                Card card = randomCard(deckHand);
                cardList.clear();
                cardList.add(card);
                card.setVerso(false);  // Show the face
                transfer(cardList, hand, true);
            }

            k++;
            if(k == nPlayers)
                k = 0;

            logWriter.writeLog(player.toString() + " hand: " + toString(player.getHand().getCardList()));
        }
    }

    private void dealingOutToPool()
    {
        logWriter.writeLog("Dealing out to pool...");
        List<Card> cardList = new ArrayList<>(1);
        for (int i = 0; i < N_HAND_CARDS; i++)
        {
            if (paused) {
                pauseGame();
            }

            // in a real game we should shuffle the cards at the very beginning of the game and
            // take the cards from the bottom of the deck for dealing, but here we do not shuffle the cards and take cards from random positions
            // in the deck so that we can reproduce the same result for the simulation every time (for marking purposes)
            Card card = randomCard(deckHand);
            if(card.getRank() == Rank.JACK)
            {
                // jack cannot be in the pool. In a real game we should place the jack in a random place in the deck
                i--;
            }else {
                cardList.clear();
                cardList.add(card);
                card.setVerso(false);  // Show the face
                transfer(cardList, poolHand, true);
            }
        }

        logWriter.writeLog("Pool: " + toString(poolHand.getCardList()));
    }

    private void transfer(List<Card> cards, Hand h, boolean sortAfterTransfer)
    {
        if(cards.isEmpty())
            return;

        boolean doDraw = !sortAfterTransfer;

        propertyChangePublisher.firePropertyChange(ON_CARD_TRANSFER, null, new Object[]{cards, h, doDraw});

        for(int i = 0; i < cards.size(); i++)
        {
            Card c = cards.get(i);
            c.removeFromHand(doDraw);
            h.insert(c, doDraw);
        }

        if(sortAfterTransfer)
        {
            h.sort(Hand.SortType.RANKPRIORITY, true);
        }
    }

    private String toString(Collection<Card> cards)
    {
        Hand h = new Hand(deck); // Clone to sort without changing the original hand
        for (Card c : cards)
        {
            h.insert(c.getSuit(), c.getRank(), false);
        }
        h.sort(Hand.SortType.RANKPRIORITY, false);

        return "[" + h.getCardList().stream().map(Pasur::toString).collect(Collectors.joining(", ")) + "]";
    }

    public int getnPlayers()
    {
        return nPlayers;
    }

    public Deck getDeck()
    {
        return deck;
    }

    public Hand getDeckHand()
    {
        return deckHand;
    }

    public Hand getPoolHand()
    {
        return poolHand;
    }

    public Player[] getPlayers()
    {
        return players;
    }

    public boolean isPaused()
    {
        return paused;
    }

    public void setPaused(boolean paused)
    {
        this.paused = paused;
    }

    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener)
    {
        propertyChangePublisher.addPropertyChangeListener(propertyChangeListener);
    }

    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener)
    {
        propertyChangePublisher.removePropertyChangeListener(propertyChangeListener);
    }

    public static String toString(Card c)
    {
        return ((Rank)c.getRank()).canonical() + "-" + ((Suit)c.getSuit()).canonical();
    }


    public static Card randomCard(Hand hand)
    {
        int x = random.nextInt(hand.getNumberOfCards());
        return hand.get(x);
    }
}
