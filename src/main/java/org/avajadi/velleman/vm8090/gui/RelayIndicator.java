package org.avajadi.velleman.vm8090.gui;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class RelayIndicator extends JLabel {
	private static final long serialVersionUID = -6810011530785920603L;
	private final static Icon ICON_ON = new ImageIcon("/home/eddie/workspaces/java/vm8090/src/main/resources/on.png");
	private final static Icon ICON_OFF = new ImageIcon("/home/eddie/workspaces/java/vm8090/src/main/resources/off.png");
	public void setState( boolean state ) {
		setIcon(state ? ICON_ON : ICON_OFF );
		repaint();
	}

}
