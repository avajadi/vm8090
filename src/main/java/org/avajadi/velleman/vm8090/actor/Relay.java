package org.avajadi.velleman.vm8090.actor;

public class Relay {

	enum State {
		OFF, ON
	}

	public State state = State.OFF;

	private int timer = 60;

	public int getTimer() {
		return timer;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}
}
