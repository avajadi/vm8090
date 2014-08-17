package org.avajadi.velleman.vm8090.packet;


import static org.testng.AssertJUnit.assertEquals;
import org.avajadi.velleman.vm8090.Cmd;
import org.avajadi.velleman.vm8090.Packet;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class PacketFactoryTest {
	private PacketFactory packetFactory;

	@BeforeMethod
	public void setup() {
		packetFactory = new PacketFactory();
	}

	@Test
	public void testGetNew() {
		Packet original = new RelayStatusEventPacket();
		Packet factoried = packetFactory.getNew(original.toBytes());
		assertEquals(RelayStatusEventPacket.class, factoried.getClass());
		assertEquals(original, factoried);
	}
	
	@Test
	public void testGetNewUnknown() {
		Packet original = new Packet((byte)0x99);
		Packet factoried = packetFactory.getNew(original.toBytes());
		assertEquals(Packet.class, factoried.getClass());
		assertEquals(original, factoried);
	}
	
	@Test
	public void testGetForEmptyArray() {
		Packet original = new Packet(new byte[8]);
		Packet factoried = packetFactory.getNew(original.toBytes());
		assertEquals(Packet.class, factoried.getClass());
		assertEquals(original, factoried);
	}

	@Test
	public void verifyMorphPacket() {
		Packet original = new Packet(Cmd.EVENT_RELAY_STATUS);
		Packet morphed = packetFactory.morph( original );
		assertEquals(RelayStatusEventPacket.class, morphed.getClass());
		assertEquals(new RelayStatusEventPacket(), morphed);
	}
}
