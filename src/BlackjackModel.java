package blackjack;

import java.util.ArrayList;
import java.util.Random;

import deckOfCards.*;

/**
 * This is the implementation of a game of blackjack
 * 
 * @author ryleyhaynes
 */
public class BlackjackModel {

	private ArrayList<Card> dealerCards; // list representing the dealers hand
	private ArrayList<Card> playerCards; // list representing the players hand
	private Deck deck; // The deck of cards used for the blackjack game

	/**
	 * Default Constructor Instantiates the instance variables
	 */
	public BlackjackModel() {
		dealerCards = new ArrayList<>();
		playerCards = new ArrayList<>();
		deck = new Deck();
	}

	/**
	 * Getter method for the dealers cards.
	 * 
	 * @return a copy of the list of cards in the dealers hand.
	 */
	public ArrayList<Card> getDealerCards() {
		ArrayList<Card> dealerCopy = new ArrayList<>(dealerCards);
		return dealerCopy;
	}

	/**
	 * Setter method for the dealers cards.
	 * 
	 * @param dealerCards is a list of cards that dealers hand is set to.
	 */
	public void setDealerCards(ArrayList<Card> dealerCards) {
		this.dealerCards = dealerCards;
	}

	/**
	 * Getter method for the players cards.
	 * 
	 * @return a copy of the list of cards in the players hand.
	 */
	public ArrayList<Card> getPlayerCards() {
		ArrayList<Card> playerCopy = new ArrayList<>(playerCards);
		return playerCopy;
	}

	/**
	 * Setter method for the players cards.
	 * 
	 * @param playerCards is a list of cards that the players hand is set to.
	 */
	public void setPlayerCards(ArrayList<Card> playerCards) {
		this.playerCards = playerCards;
	}

	/**
	 * Getter method for the deck of cards.
	 * 
	 * @return the deck of cards used in the game of blackjack.
	 */
	public Deck getDeck() {
		return deck;
	}

	/**
	 * Setter method for the deck of cards.
	 * 
	 * @param deck is the set to the deck of cards used in the blackjack game.
	 */
	public void setDeck(Deck deck) {
		this.deck = deck;
	}

	/**
	 * Method creating a deck of cards, and then pre-shuffles the deck.
	 * 
	 * @param random is the value used to shuffle the deck, after the deck is
	 *               instantiated.
	 */
	public void createAndShuffleDeck(Random random) {
		deck = new Deck();
		deck.shuffle(random);
	}

	/**
	 * Method that gives the dealer their initial two cards from the deck. Ensures
	 * the dealer has no cards in their hand, and then uses a loop to give the
	 * dealer two cards from the deck.
	 */
	public void initialDealerCards() {
		dealerCards.clear();
		for (int i = 0; i < 2; i++) {
			dealerCards.add(deck.dealOneCard());
		}
	}

	/**
	 * Method that gives the dealer their initial two cards from the deck. Ensures
	 * the player has no cards in their hand, and then uses a loop to give the
	 * dealer two cards from the deck.
	 */
	public void initialPlayerCards() {
		playerCards.clear();
		for (int i = 0; i < 2; i++) {
			playerCards.add(deck.dealOneCard());
		}
	}

	/**
	 * Method that gives the player one card from the deck.
	 */
	public void playerTakeCard() {
		playerCards.add(deck.dealOneCard());
	}

	/**
	 * Method that gives the player one card from the deck.
	 */
	public void dealerTakeCard() {
		dealerCards.add(deck.dealOneCard());
	}

	/**
	 * Method that calculates the values of possible hands.
	 * 
	 * @param hand is the hand of cards (dealer or player) that values are
	 *             calculated for.
	 * @return a list of Integers with the possible values of the hand. (if two
	 *         viable hands are available because of the dual value of an ace, then
	 *         two values could be returned in the list).
	 */
	public static ArrayList<Integer> possibleHandValues(ArrayList<Card> hand) {
		ArrayList<Integer> handValues = new ArrayList<>();
		Integer score = 0;
		Integer highAceScore = 0;

		for (Card card : hand) {
			Rank currentCard = card.getRank();
			if (currentCard == Rank.ACE) {
				score += currentCard.getValue();
				highAceScore += 11;
			} else {
				score += currentCard.getValue();
				highAceScore += currentCard.getValue();
			}
		}

		handValues.add(score);
		if (score != highAceScore && highAceScore <= 21) {
			handValues.add(highAceScore);
		}

		return handValues;
	}

	/**
	 * Method that assesses a hands impact on the game.
	 * 
	 * @param hand is the hand of cards (dealer or player) that values are
	 *             calculated for.
	 * @return an enumerated type of HandAssessment detailing the result of a hand
	 *         on the game (Bust, Insufficient Cards, Natural Blackjack, or Normal).
	 */
	public static HandAssessment assessHand(ArrayList<Card> hand) {
		Integer blackJack = 21;
		Integer count = possibleHandValues(hand).size();
		boolean hasAce = false;
		boolean hasTenValue = false;

		for (Integer i : possibleHandValues(hand)) {
			if (i > blackJack) {
				count--;
			}
		}

		if (count == 0) {
			return HandAssessment.BUST;
		} else if (hand == null || hand.size() < 2) {
			return HandAssessment.INSUFFICIENT_CARDS;
		} else if (hand.size() == 2) {
			for (Card card : hand) {
				if (card.getRank() == Rank.ACE) {
					hasAce = true;
				} else if (card.getRank().getValue() == 10) {
					hasTenValue = true;
				}
			}
			if (hasAce && hasTenValue) {
				return HandAssessment.NATURAL_BLACKJACK;
			}
		}
		return HandAssessment.NORMAL;
	}

	/**
	 * Method that looks at the dealers and players hand, determining the result of
	 * a game.
	 * 
	 * @return an enumerated type of GameResult (Player Won, Player Lost, Push).
	 */
	public GameResult gameAssessment() {
		int playerValue = 0;
		int dealerValue = 0;

		for (Integer i : possibleHandValues(playerCards)) {
			if (i > playerValue) {
				playerValue = i;
			}
		}

		for (Integer j : possibleHandValues(dealerCards)) {
			if (j > dealerValue) {
				dealerValue = j;
			}
		}
		
		if (assessHand(playerCards) == HandAssessment.NATURAL_BLACKJACK
				&& assessHand(dealerCards) != HandAssessment.NATURAL_BLACKJACK) {
			return GameResult.NATURAL_BLACKJACK;
		} else if (assessHand(playerCards) == HandAssessment.NATURAL_BLACKJACK
				&& assessHand(dealerCards) == HandAssessment.NATURAL_BLACKJACK) {
			return GameResult.PUSH;
		} else if (assessHand(playerCards) == HandAssessment.BUST) {
			return GameResult.PLAYER_LOST;
		} else if (assessHand(dealerCards) == HandAssessment.BUST) {
			return GameResult.PLAYER_WON;
		} else if (playerValue > dealerValue) {
			return GameResult.PLAYER_WON;
		} else if (playerValue < dealerValue) {
			return GameResult.PLAYER_LOST;
		} else {
			return GameResult.PUSH;
		}
	}

	/**
	 * Method that defines the logic for the instances in which the dealer should
	 * draw another card
	 * 
	 * @return true if the dealer should draw another card, false if the dealer
	 *         should not draw another card.
	 */
	@SuppressWarnings("unlikely-arg-type")
	public boolean dealerShouldTakeCard() {
		Integer value = 0;
		for (Integer i : possibleHandValues(dealerCards)) {
			if (i > value) {
				value = i;
			}
		}
		if (value >= 18) {
			return false;
		} else if (dealerCards.contains(Rank.ACE)) {
			if (value >= 18) {
				return false;
			} else {
				return true;
			}
		} else if (value >= 17) {
			return false;
		} else {
			return true;
		}
	}
}
