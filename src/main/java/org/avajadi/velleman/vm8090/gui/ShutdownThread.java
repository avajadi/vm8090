package org.avajadi.velleman.vm8090.gui;

public class ShutdownThread extends Thread {
	private SimpleControlPanel application;
	
	public ShutdownThread( SimpleControlPanel application ) {
		this.application = application;
	}

	@Override
	public void run() {
		application.cleanup();
	}
}
