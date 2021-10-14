package pasur;

/**
 * @author Alireza Ostovar
 * 29/09/2021
 */

import ch.aplu.jcardgame.*;
import logWriter.LogWriter;

import java.util.*;

public abstract class Player
{
    private static final int TARGET_VALUE = 11;

    /**
     * NEW ADDED ATTRIBUTES
     * scoreFromLastRound: the score earned from the last round (not include the score from the current picked cards and surs)
     * score: the overall score through the whole game
     * logWriter: write logs in the "pasur.log" file
     */
    protected int scoreFromLastRound;
    protected int score;
    private LogWriter logWriter = LogWriter.getLogWriterInstance();

    protected int id;


    /**
     * Comment added for better understanding:
     * The cards that currently the player holds
     */
    protected Hand hand;
    /**
     * Comment added for better understanding:
     * The cards picked up/selected by the player
     * will be used to calculate score in the current deal-card round
     */
    protected Hand pickedCards;
    /**
     * Comment added for better understanding:
     * The cards that achieve surs (Game event)
     * will be used to calculate score
     */
    protected Hand surs;

    protected Player(int id)
    {
        this.id = id;
    }

    /**
     *
     * @param pool current pool
     * @return the played card and the set of cards this player wants to pick up from the pool.
     */
    public final Map.Entry<Card, Set<Card>> playCard(Hand pool)
    {
        Card playedCard = selectToPlay();
        Set<Card> cardsToPick = null;
        if(playedCard != null)
        {
            // System.out.println(toString() + " plays " + Pasur.toString(playedCard));
            logWriter.writeLog(toString() + " plays " + Pasur.toString(playedCard));
            cardsToPick = pickCards(pool, playedCard);
        }

        return new AbstractMap.SimpleEntry<>(playedCard, cardsToPick);
    }

    /**
     * Comment added for better understanding:
     * @param pool The pool in the middle of the table
     * @param playedCard The card that the player dealt
     * @return The method will return a set of cards that picked
     * by the player after the player dealt with a card.
     */
    protected Set<Card> pickCards(Hand pool, Card playedCard)
    {
        List<Card> poolCards = pool.getCardList();

        Set<Card> cardsToPick = new HashSet<>();
        /**
         * Comment added for better understanding:
         * A Jack will pick all the number cards and itself
         */
        if(playedCard.getRank() == Rank.JACK)
        {
            for(int i = 0, len = poolCards.size(); i < len; i++)
            {
                Card card = poolCards.get(i);
                if(card.getRank() != Rank.KING && card.getRank() != Rank.QUEEN)
                {
                    // a jack picks any card except kings and queens
                    cardsToPick.add(card);
                }
            }
        /**
         * Comment added for better understanding:
         * A King/Queen will pick themselves respectively
         */
        }else if(playedCard.getRank() == Rank.KING || playedCard.getRank() == Rank.QUEEN)
        {
            Card candidateCardToPick = null;
            for(int i = 0, len = poolCards.size(); i < len; i++)
            {
                Card card = poolCards.get(i);
                if(card.getRank() == playedCard.getRank())
                {
                    // a king/queen can only pick another king/queen
                    candidateCardToPick = card;
                    if(candidateCardToPick.getSuit() == Suit.CLUBS)
                    {
                        // a club is preferred as it can help us to get to 7 clubs which will give us 7 points at the end
                        break;
                    }
                }
            }
            if(candidateCardToPick != null)
            {
                cardsToPick.add(candidateCardToPick);
            }
        /**
         * Comment added for better understanding:
         * A numeric card will get all the possible solutions for:
         * Summing up with cards in the pool to 11,
         * and choose the best solution for picking up the cards in the pool
         */
        }else
        {
            // the played card is a numeric card, so we need to see what are the potential sets of cards we can pick with it
            List<Set<Card>> candidateSetsOfCardsToPick = new ArrayList<>();
            int targetValue = TARGET_VALUE - playedCard.getValue();
            if(!poolCards.isEmpty())
            {
                findSetsOfCardsSummingToTarget(poolCards, targetValue, candidateSetsOfCardsToPick);
                if(!candidateSetsOfCardsToPick.isEmpty())
                {
                    Set<Card> bestSet = chooseBestCandidateSetToPick(candidateSetsOfCardsToPick);
                    for(Card card : bestSet)
                    {
                        cardsToPick.add(card);
                    }
                }
            }
        }

        return cardsToPick;
    }

    /**
     * Comment added for better understanding:
     * Method for get the best pick if a numeric card is played
     */
    protected Set<Card> chooseBestCandidateSetToPick(List<Set<Card>> candidateSetsOfCardsToPick)
    {
        double valueGivenTo10ofDiamond = 3;
        double valueGivenTo2ofClubs = 2;
        double valueGivenToAce = 1;
        double valueGivenToJack = 1;
        double valueGivenToAcardOfClubs = 0.1; // helps to go towards collecting 7 cards of clubs
        Set<Card> setWithMaxValue = null;
        double maxValue = -1;
        for(Set<Card> set : candidateSetsOfCardsToPick)
        {
            double setValue = 0;
            for(Card card : set)
            {
                if(card.getRank() == Rank.TEN && card.getSuit() == Suit.DIAMONDS)
                {
                    setValue += valueGivenTo10ofDiamond;
                }else if(card.getRank() == Rank.TWO && card.getSuit() == Suit.CLUBS)
                {
                    setValue += valueGivenTo2ofClubs;
                }else if(card.getRank() == Rank.ACE)
                {
                    setValue += valueGivenToAce;
                }else if(card.getRank() == Rank.JACK)
                {
                    setValue += valueGivenToJack;
                }

                if(card.getSuit() == Suit.CLUBS)
                {
                    setValue += valueGivenToAcardOfClubs;
                }
            }

            if(setValue > maxValue)
            {
                maxValue = setValue;
                setWithMaxValue = set;
            }
        }

        return setWithMaxValue;
    }

    /**
     * Comment added for better understanding:
     * Method for get all the solutions/combination that the current played card
     * will sum up with the pool cards to 11
     */
    private void findSetsOfCardsSummingToTarget(List<Card> cards, int targetValue, List<Set<Card>> setsOfCards)
    {
        _findSetsOfCardsSummingToTarget(cards, setsOfCards, targetValue, new ArrayList<>());
    }

    /**
     * Comment added for better understanding:
     * Method for get all the solutions/combination that the current played card
     * will sum up with the pool cards to 11
     */
    private void _findSetsOfCardsSummingToTarget(List<Card> cards, List<Set<Card>> setsOfCards, int targetValue,
                                                 List<Card> partial)
    {
        int sum = 0;
        for(int i = 0, len = partial.size(); i < len; i++)
        {
            sum += partial.get(i).getValue();
        }

        if (sum == targetValue)
        {
            Set<Card> set = new HashSet<>(partial.size());
            set.addAll(partial);
            setsOfCards.add(set);
        }

        if (sum >= targetValue)
        {
            return;
        }

        for(int i = 0; i < cards.size(); i++)
        {
            ArrayList<Card> remainingCards = new ArrayList<>();
            Card card = cards.get(i);
            for (int j = i + 1; j < cards.size(); j++)
            {
                remainingCards.add(cards.get(j));
            }

            List<Card> partialCards = new ArrayList<>(partial);
            partialCards.add(card);

            _findSetsOfCardsSummingToTarget(remainingCards, setsOfCards, targetValue, partialCards);
        }
    }

    public Hand getHand()
    {
        return hand;
    }

    public void setHand(Hand hand)
    {
        this.hand = hand;
    }

    public Hand getPickedCards()
    {
        return pickedCards;
    }

    public void setPickedCards(Hand pickedCards)
    {
        this.pickedCards = pickedCards;
    }

    public Hand getSurs()
    {
        return surs;
    }

    public void setSurs(Hand surs)
    {
        this.surs = surs;
    }

    public void reset()
    {
        hand.removeAll(false);
        pickedCards.removeAll(false);
        surs.removeAll(false);
    }

    public String toString()
    {
        return "Player" + id;
    }

    /**
     * EDITED PART:
     * Right now the getScore will always return 0,
     * so that in the game we can only see 0 point scored by each player,
     * even if the player has satisfied with the scoring rule.
     *
     *
     */
    public int getScore()
    {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScoreFromLastRound() {
        return scoreFromLastRound;
    }

    public void setScoreFromLastRound(int scoreFromLastRound) {
        this.scoreFromLastRound = scoreFromLastRound;
    }


    abstract Card selectToPlay();
}
