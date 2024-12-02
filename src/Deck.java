package deckOfCards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * This is the deck of cards used in the blackjack game, and the actions that
 * can be done to the deck.
 */
public class Deck {

	private ArrayList<Card> deckOfCards; // A list representing the deck of cards

	/**
	 * Default Constructor.
	 * Creates a deck by assigning every rank and suit to a
	 * card, such that there is one card of each rank per suit, and then adding that
	 * card to the deck list.
	 */
	public Deck() {
		deckOfCards = new ArrayList<>();
		for (Suit s : Suit.values()) {
			for (Rank r : Rank.values()) {
				Card card = new Card(r, s);
				deckOfCards.add(card);
			}

		}
	}
	/**
	 * Shuffle method.
	 * @param randomNumberGenerator the number by which the deck uses to shuffle itself randomly.
	 */
	public void shuffle(Random randomNumberGenerator) {
		Collections.shuffle(deckOfCards, randomNumberGenerator);
	}

	/**
	 * Method that deals a single card from the deck.
	 * @return Card dealt, which is the first card in the deck (removes the card dealt from the deck).
	 */
	public Card dealOneCard() {

		Card dealt = new Card(deckOfCards.get(0).getRank(), deckOfCards.get(0).getSuit());
		deckOfCards.remove(0);
		return dealt;

	}

}
