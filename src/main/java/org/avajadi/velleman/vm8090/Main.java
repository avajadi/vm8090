package org.avajadi.velleman.vm8090;

import jssc.SerialPort;
import jssc.SerialPortException;

public class Main {

	public static void main(String[] args) throws SerialPortException {
		Packet versionPacket = new Packet(Cmd.FIRMWARE_VERSION);
		Packet testPacket = new Packet(Cmd.ON);
		// 04 11 01 00 00 ea 0f
		testPacket.addRelay(0);//00000001
		SerialPort serialPort = new SerialPort("/dev/ttyACM4");
		try {
			serialPort.openPort();// Open serial port
			serialPort.setParams(SerialPort.BAUDRATE_19200,
					SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);// Set params. Also you can set
											// params by this string:
											// serialPort.setParams(9600, 8, 1,
											// 0);
			serialPort.writeBytes(versionPacket.toBytes());
			serialPort.writeBytes(testPacket.toBytes());// Write data to port
			System.out.println("Wrote test packet\n" + testPacket);
			while (true) {
				byte[] response = serialPort.readBytes(7);
				Packet packet = new Packet(response);
				System.out.println(packet);
			}
		} catch (SerialPortException ex) {
			System.out.println(ex);
		}finally {
			serialPort.purgePort(1);
			serialPort.closePort();// Close serial port
		}
	}
}