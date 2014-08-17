package org.avajadi.velleman.vm8090.packet;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.avajadi.velleman.vm8090.Packet;

public class PacketFactory {

	private final Map<Byte,Class<? extends Packet>> byType = new HashMap<Byte,Class<? extends Packet>>();
	
	public PacketFactory() {
		byType.put((byte)0x51, RelayStatusEventPacket.class);
		byType.put((byte)0x50, ButtonStatusEventPacket.class);
	}
	
	@SuppressWarnings("unchecked")
	public Packet getNew(byte[] packetData) {
		Class<? extends Packet> packetClass = byType.get(packetData[1]);
		if( packetClass == null ) {
			return new Packet(packetData);
		}
		try {
			Constructor<? extends Packet> constructor = null;
			for(Constructor<?> con : packetClass.getConstructors() ) {
				Class[] types = con.getParameterTypes();
		        if(types.length!=1)
		            continue;
		        if( types[0].equals(byte[].class)) {
		        	constructor = (Constructor<? extends Packet>) con;
		        }
			}
			return constructor.newInstance(packetData);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Packet(packetData);
	}

	public Packet morph(Packet original) {
		return getNew(original.toBytes());
	}
}
