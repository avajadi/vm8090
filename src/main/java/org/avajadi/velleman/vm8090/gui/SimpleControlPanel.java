
package org.avajadi.velleman.vm8090.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import jssc.SerialPort;
import jssc.SerialPortException;

import org.avajadi.velleman.vm8090.Cmd;
import org.avajadi.velleman.vm8090.Packet;
import org.avajadi.velleman.vm8090.PacketListener;
import org.avajadi.velleman.vm8090.connector.Connector;
import org.avajadi.velleman.vm8090.connector.DeviceNotFoundException;
import org.avajadi.velleman.vm8090.packet.PacketFactory;
import org.avajadi.velleman.vm8090.packet.RelayStatusEventPacket;

public class SimpleControlPanel implements ActionListener, PacketListener{
	
	private Connector connector;
	private final RelayButton[] relayButtons = new RelayButton[8];
	private final RelayIndicator[] relayIndicators = new RelayIndicator[8];
	private final PacketFactory packetFactory = new PacketFactory();
	private SimpleControlPanel() throws DeviceNotFoundException {
        createAndShowGUI();		
        
		connect();
		Runtime.getRuntime().addShutdownHook(new ShutdownThread(this));

	}
	private void connect() throws DeviceNotFoundException {
		connector = new Connector();
		connector.connect();
		connector.addPacketListener(this);
	}
	
	private void send( Packet packet ) throws SerialPortException {
		connector.send(packet);
	}
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Velleman vm8090 Control Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel relays = new JPanel(new FlowLayout());
        for( int i = 0 ; i < 8 ; i++ ) {
        	relayButtons[i] = new RelayButton(i+1,this);
        	relayIndicators[i] = new RelayIndicator();
        	relayIndicators[i].setState(false);
        	JPanel relay = new JPanel(new GridLayout(2, 1));
        	relay.add(relayIndicators[i]);
        	relay.add(relayButtons[i]);
        	relays.add(relay);
        }
        frame.getContentPane().add(relays);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	try {
					new SimpleControlPanel();
				} catch (DeviceNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
    }
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if( source instanceof RelayButton ) {
			int relayIndex = ((RelayButton)source).getRelayIndex();
			Packet packet = new Packet(Cmd.TOGGLE);
			packet.addRelay(relayIndex);
			try {
				send(packet);
			} catch (SerialPortException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void cleanup() {
		try {
			Packet shutdown = new Packet(Cmd.OFF);
			shutdown.addAllRelays();
			connector.send(shutdown);
			connector.disconnect();
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	} 
	public void processPacket(Packet packet) {
		Packet realPacket = packetFactory.morph(packet);
		if( realPacket instanceof RelayStatusEventPacket ) {
			List<Boolean> relayStates = ((RelayStatusEventPacket)realPacket).getRelaysStates();
			for( int i = 0 ; i < relayStates.size();i++) {
				relayIndicators[i].setState(relayStates.get(i));
			}
		}
	}
}