package org.avajadi.velleman.vm8090.gui;

import java.awt.event.ActionListener;

import javax.swing.JButton;

public class RelayButton extends JButton {
	private static final long serialVersionUID = -9156867239376089120L;

	private int relayIndex;
	
	public RelayButton(int relayIndex, ActionListener actionListener ) {
		super(String.format("%d", relayIndex));
		this.relayIndex = relayIndex;
		addActionListener(actionListener);
	}
	
	
	public int getRelayIndex() {
		return relayIndex;
	}
}
