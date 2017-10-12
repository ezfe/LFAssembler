package common;

public class BitTools {
	/**
	 * Get the nth bit from the right
	 * @param index The index (from the right)
	 * @param data The byte
	 * @return The bit
	 */
	public static int bitAt(int index, byte data) {
		return (data >> index) & 1;
	}
	
	/**
	 * Set the nth bit from the right
	 * @param index The index (from the right)
	 * @param data The source byte
	 * @param bit The bit value
	 * @return The modified byte
	 * 
	 */
	public static byte setBit(int index, byte data, int bit) {
		if (bit == 0) {
			return BitTools.setFalse(index, data);
		} else if (bit == 1) {
			return BitTools.setTrue(index, data);
		} else {
			throw new IllegalArgumentException("Illegal bit value: " + bit);
		}
	}
	
	/**
	 * Set the nth bit from the right to 1
	 * @param index The index (from the right)
	 * @param data The source byte
	 * @return The modified byte
	 */
	public static byte setTrue(int index, byte data) {
		return (byte) (data | (1 << index));
	}
	
	/**
	 * Set the nth bit from the right to 0
	 * @param index The index (from the right)
	 * @param data The source byte
	 * @return The modified byte
	 */
	public static byte setFalse(int index, byte data) {
		return (byte) (BitTools.setTrue(index, data) ^ (1 << index));
	}
}
