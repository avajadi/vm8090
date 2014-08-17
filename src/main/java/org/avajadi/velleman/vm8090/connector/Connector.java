package org.avajadi.velleman.vm8090.connector;

import java.util.ArrayList;
import java.util.List;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

import org.avajadi.velleman.vm8090.Cmd;
import org.avajadi.velleman.vm8090.Packet;
import org.avajadi.velleman.vm8090.PacketListener;

public class Connector implements SerialPortEventListener {
	private SerialPort serialPort;
	private String portDev = null;
	private final List<PacketListener> packetListeners = new ArrayList<PacketListener>();

	public void connect() throws DeviceNotFoundException {
		String[] portNames = SerialPortList.getPortNames();
		Packet probe = new Packet(Cmd.FIRMWARE_VERSION);
		for (String portName : portNames) {
			serialPort = new SerialPort(portName);
			try {
				serialPort.openPort();// Open serial port
				serialPort.setParams(SerialPort.BAUDRATE_19200,
						SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
						SerialPort.PARITY_NONE);// Set params. Also you can set
												// params by this string:
												// serialPort.setParams(9600, 8,
												// 1,
												// 0);
				serialPort.writeBytes(probe.toBytes());
				byte[] response = serialPort.readBytes(7);
				if (response[1] == 0x71) {
					setPortDev(portName);
					serialPort.addEventListener(this);
					return;
				}
			} catch (SerialPortException ex) {
				System.out.println(ex);
				
			}
		}
		throw new DeviceNotFoundException("No vm8090 found");
	}

	public void send(Packet packet) throws SerialPortException {
		serialPort.writeBytes(packet.toBytes());
	}

	public static void main(String[] args) throws DeviceNotFoundException, SerialPortException {
		Connector connector = new Connector();
		connector.connect();
		System.out.println(connector.getPortDev());
		connector.addPacketListener(new PacketListener(){

			public void processPacket(Packet packet) {
				System.out.println("Packet arrived");
				System.out.println(packet);
			}});
	}

	public String getPortDev() {
		return portDev;
	}

	public void setPortDev(String portDev) {
		this.portDev = portDev;
	}

	public void serialEvent(SerialPortEvent serialPortEvent) {
		//TODO Handle other types of events (assuming type 1 atm)
		int byteCount = serialPortEvent.getEventValue();
		while( byteCount > 6 ) {
			byte[] data;
			try {
				data = serialPort.readBytes(7);
				byteCount -= 7;
				Packet packet = new Packet(data);
				for( PacketListener packetListener : getPacketListeners() ) {
					packetListener.processPacket(packet);
				}
			} catch (SerialPortException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private List<PacketListener> getPacketListeners() {
		return packetListeners;
	}
	
	public void addPacketListener( PacketListener listener ) {
		packetListeners.add(listener);
	}

	public void disconnect() {
		try {
			serialPort.purgePort(1);
			serialPort.closePort();// Close serial port
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
