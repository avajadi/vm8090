package org.avajadi.velleman.vm8090;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Packet {

	public static final byte ETX = 0xF;

	public static final byte STX = 0x4;

	private final byte[] bytes = new byte[7];
	public Packet( byte cmd ) {
		bytes[0] = STX;
		bytes[1] = cmd;
		bytes[2] = 0;
		bytes[3] = 0;
		bytes[4] = 0;
		bytes[6] = ETX;
	}
	public Packet( byte[] bytes ) {
		for( int i = 0 ; i < 7 ; i++ ) {
			this.bytes[i] = bytes[i];
		}
	}

	public Packet(Cmd command) {
		this(command.byteValue);
	}
	
	public Packet(Packet packet ) {
		this(packet.bytes);
	}
	
	public void addAllRelays() {
		bytes[2] = (byte)0xFF;
	}

	/**
	 * Add a relay to be addressed by this command 
	 * @param relayIndex 1-based (1-8)
	 */
	public void addRelay( int relayIndex ) {
		if(relayIndex <1 || relayIndex >8)
			throw new IllegalArgumentException(String.format("relayIndex %d not between 1 and 8",relayIndex));
		
		byte mask = (byte)Math.pow(2, relayIndex-1);
		System.out.println("Adding relay " + relayIndex);
		System.out.println(String.format("ORing %02x with %02x,  giving %02x",mask,bytes[2], mask|bytes[2]));
		bytes[2] |= mask;
	}
	
	protected void checksum() {
		// -(STX + CMD + MASK + PARAM1 + PARAM2)
		bytes[5] = (byte)(-(bytes[0] + bytes[1] + bytes[2] + bytes[3] + bytes[4]));
	}
	
	public void clearRelays() {
		bytes[2] = 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Packet other = (Packet) obj;
		checksum();
		other.checksum();
		if (!Arrays.equals(bytes, other.bytes))
			return false;
		return true;
	}
	
	public byte getCmd() {
		return bytes[1];
	}

	public byte getMask() {
		return bytes[2];
	}


	/**
	 * Get parameter 1 or 2 from this packet
	 * @param parameter the data held in the requested parameter
	 * @return the parameter data
	 */
	public byte getParameter( int parameter ) {
		if( parameter < 1 || parameter > 2 )
			throw new IllegalArgumentException(String.format("Value out of bounds for parameter: %d", parameter));
		return bytes[2+parameter];
	}
	
	public List<Integer> getRelays() {
		return getRelays(getMask());
	}
	
	public List<Integer> getRelays(byte mask) {
		List<Integer> relays = new ArrayList<Integer>(8);
		for( int i = 1 ; i < 9 ; i++ ) {
			if( (mask & (byte)1) != 0 )
				relays.add(i);				
			mask = (byte)(mask >> 1);
		}
		return relays;
	}
	
	@Override
	public int hashCode() {
		checksum();
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(bytes);
		return result;
	}

	public void setParameter(int parameter, byte value) {
		if( parameter < 1 || parameter > 2 )
			throw new IllegalArgumentException(String.format("Value out of bounds for parameter: %d", parameter));
		bytes[2+parameter] = value;
	}
	
	public byte[] toBytes () {
		checksum();
		return bytes;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		Cmd command = null;
		sb.append("Command: ");
		try{
			command = Cmd.valueOf(getCmd());
			sb.append( command );
		}catch(IllegalArgumentException e ) {
			sb.append("unknown");
		}
		sb.append(String.format("[%02x]\n", getCmd()));
		String maskString = Integer.toBinaryString(bytes[2]);
		if( maskString.length() > 8 ) {
			maskString = maskString.substring(maskString.length() - 8);
		}
		
		sb.append(String.format("Mask: %8s\n", maskString).replace(" ", "0"));
		sb.append("Relays:");
		for( int relay : getRelays() ) {
			sb.append(" " + relay);
		}
		sb.append("\n");
		sb.append(String.format("Parameter 1: %02x\n", bytes[3]));
		sb.append(String.format("Parameter 2: %02x\n", bytes[4]));
		sb.append("RAW:");
		for (byte b : bytes) {
			sb.append(String.format("%02X ", b));
		}
		sb.append("\n");
		return sb.toString();
	}
}
