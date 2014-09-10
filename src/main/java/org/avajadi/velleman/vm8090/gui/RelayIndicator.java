package org.avajadi.velleman.vm8090.gui;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class RelayIndicator extends JLabel {
	private static final long serialVersionUID = -6810011530785920603L;
	private final static Icon ICON_ON = new ImageIcon(getImage("icons/on.png"));
	private final static Icon ICON_OFF = new ImageIcon(getImage("icons/off.png"));

	public void setState(boolean state) {
		setIcon(state ? ICON_ON : ICON_OFF);
		repaint();
	}

	public static Image getImage(final String pathAndFileName) {
		final URL url = Thread.currentThread().getContextClassLoader()
				.getResource(pathAndFileName);
		return Toolkit.getDefaultToolkit().getImage(url);
	}
}
