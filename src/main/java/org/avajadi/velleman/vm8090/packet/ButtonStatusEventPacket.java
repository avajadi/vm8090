package org.avajadi.velleman.vm8090.packet;

import org.avajadi.velleman.vm8090.Cmd;
import org.avajadi.velleman.vm8090.Packet;

public class ButtonStatusEventPacket extends Packet {

	public ButtonStatusEventPacket() {
		super(Cmd.EVENT_BUTTON_STATUS);
	}

	public ButtonStatusEventPacket(Packet packet) {
		super(packet);
		if(packet.getCmd() != Cmd.EVENT_BUTTON_STATUS.byteValue)
			throw new IllegalArgumentException();
	}

	public ButtonStatusEventPacket(byte[] bytes) {
		super(bytes);
		if(bytes[1] != Cmd.EVENT_BUTTON_STATUS.byteValue)
			throw new IllegalArgumentException();
	}

}
