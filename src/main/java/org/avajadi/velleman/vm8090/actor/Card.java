package org.avajadi.velleman.vm8090.actor;

public class Card {
	private final Relay[] relays = new Relay[NUMBER_OF_RELAYS];
	public static final int NUMBER_OF_RELAYS = 8;

	public Card() {
		for (int i = 0; i < NUMBER_OF_RELAYS; i++) {
			relays[i] = new Relay();
		}
	}

	public Relay getRelay(int index) {
		if (index > NUMBER_OF_RELAYS || index < 1) {
			throw new IndexOutOfBoundsException(String.format(
					"Relay index must be between 1 and %d", NUMBER_OF_RELAYS));
		}
		return relays[index - 1];
	}
}
