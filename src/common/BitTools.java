package common;

public class BitTools {
	public static Integer bitAt(Integer index, Byte data) {
		return (data >> index) & 1;
	}
	
	public static Byte setBit(Integer index, Byte data, Integer bit) {
		if (bit == 0) {
			return BitTools.setFalse(index, data);
		} else if (bit == 1) {
			return BitTools.setTrue(index, data);
		} else {
			System.err.println("setBit requires bit to be 1 or 0. Passed " + bit);
			return data;
		}
	}
	
	public static Byte setTrue(Integer index, Byte data) {
		return (byte) (data | (1 << index));
	}
	
	public static Byte setFalse(Integer index, Byte data) {
		return (byte) (BitTools.setTrue(index, data) ^ (1 << index));
	}
}
