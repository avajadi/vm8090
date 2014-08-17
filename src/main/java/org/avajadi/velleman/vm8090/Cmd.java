package org.avajadi.velleman.vm8090;

public enum Cmd {
	ON(0x11), 
	OFF(0x12), 
	TOGGLE(0x14), 
	SET_BUTTON_MODE(0x21), 
	START_RELAY_TIMER(0x41), 
	SET_RELAY_TIMER_DELAY(0x42),
	QUERY_RELAY_STATUS(0x18),
	QUERY_TIMER_DELAY(0x44),
	QUERY_BUTTON_MODE(0x22),
	EVENT_BUTTON_STATUS(0x50),
	EVENT_RELAY_STATUS(0x51),
	RESET_FACTORY_DEFAULTS(0x66),
	QUERY_JUMPER_STATUS(0x70),
	FIRMWARE_VERSION(0x71);
	public byte byteValue;

	private Cmd(final int byteValue) {
		this.byteValue = (byte) byteValue;
	}

	public static Cmd valueOf(byte byteValue) {
		for (Cmd cmd : values()) {
			if (cmd.byteValue == byteValue)
				return cmd;
		}
		throw new IllegalArgumentException(String.format(
				"No such command: %02x", byteValue));
	}

	public static void main(String[] args) {
		System.out.println(Cmd.valueOf((byte) 0x12));
	}
}
