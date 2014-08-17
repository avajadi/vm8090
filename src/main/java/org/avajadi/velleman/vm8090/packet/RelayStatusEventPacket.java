package org.avajadi.velleman.vm8090.packet;

import java.util.ArrayList;
import java.util.List;

import org.avajadi.velleman.vm8090.Cmd;
import org.avajadi.velleman.vm8090.Packet;

public class RelayStatusEventPacket extends Packet {
	
	public RelayStatusEventPacket() {
		super(Cmd.EVENT_RELAY_STATUS);
	}
	
	public RelayStatusEventPacket(byte[] bytes) {
		super(bytes);
		if(bytes[1] != Cmd.EVENT_RELAY_STATUS.byteValue)
			throw new IllegalArgumentException();
	}


	public RelayStatusEventPacket( Packet packet ) {
		super(packet);
		if(packet.getCmd() != Cmd.EVENT_RELAY_STATUS.byteValue)
			throw new IllegalArgumentException();
	}
	
	public List<Integer> getChangedRelays() {
		byte changed = (byte)(getMask() ^ getParameter(1));
		return getRelays(changed);
	}

	public List<Boolean> getRelaysStates() {
		List<Integer> onRelays = getRelays(getParameter(1));
		List<Boolean> relayStates = new ArrayList<Boolean>(8);
		for( int i = 1 ; i < 9 ; i++ ) {
			relayStates.add( onRelays.contains(i));
		}
		return relayStates;
	}
}
