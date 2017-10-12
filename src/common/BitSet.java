package common;

import java.util.ArrayList;
import java.util.Optional;

/**
 * A collection of bits
 * 
 * @author Ezekiel Elin
 */
public class BitSet {
	private ArrayList<Byte> bits;
	
	/**
	 * The length of last byte in the bits array
	 * Values should be: 1-8 (inclusive)
	 * 0 should not exist (the byte wouldn't be in the array)
	 * 8 represents a full last-bit
	 */
	private int trailingLength;
	
	public BitSet() {
		bits = new ArrayList<>();
		trailingLength = 8;
	}
	
	public BitSet(byte[] bites) {
		bits = new ArrayList<>();
		for(byte b: bites) {
			bits.add(b);
		}
		trailingLength = 8;
	}
	
	/**
	 * Insert a byte at an address
	 * @param bite The byte
	 * @param byteAddress The byte-addressing address
	 */
	public void insertByte(byte bite, int byteAddress) {
		bits.add(byteAddress, bite);
	}
	
	/**
	 * Overrwrite a byte at an address
	 * @param bite The byte
	 * @param byteAddress The byte-addressing address
	 */
	public void setByte(byte bite, int byteAddress) {
		bits.set(byteAddress, bite);
	}
	
	/**
	 * Get a byte
	 * @param byteAddress The byte-addressing address
	 * @return The byte
	 */
	public Optional<Byte> getByte(int byteAddress) {
		if (bits.size() > byteAddress) {
			return Optional.of((byte) bits.get(byteAddress));
		} else {
			return Optional.empty();
		}
				
	}
	
	/**
	 * Set a specific bit, left-indexed
	 * @param bit
	 * @param bitAddress
	 */
	public void setBit(int bit, BitIndex bitAddress) {
		byte bite = getByte(bitAddress.getByte()).orElse((byte) 0);
		bite = BitTools.setBit(7 - bitAddress.getBit(), bite, bit);
		this.setByte(bite, bitAddress.getByte());
	}
	
	/**
	 * Append a single bit to the BitSet
	 * @param bit The bit
	 */
	public void append(int bit) {
		if (trailingLength == 8) {
			bits.add((byte) 0);
			trailingLength = 0;
		}
		
		int lastIndex = bits.size() - 1;
		byte data = bits.get(lastIndex);
		data = BitTools.setBit(7 - trailingLength, data, bit);
		bits.set(lastIndex, data);
		
		trailingLength += 1;
	}
	
	public void append(Character cbit) {
		if (cbit == '0') {
			this.append(0);
		} else if (cbit == '1') {
			this.append(1);
		} else {
			System.out.println("Encountered unexpected bit character: " + cbit + ", expected 1 or 0");
		}
	}
	
	/**
	 * Append a byte of data to the BitSet
	 * @param bite The byte to append
	 */
	public void append(byte bite) {
		if (trailingLength == 8) {
			bits.add(bite);
		} else {
			byte fits = (byte) (bite >> (8 - trailingLength));
			byte last = bits.get(bits.size() - 1);
			bits.set(bits.size() - 1, (byte) (last | fits));
			byte biteRightSide = (byte) (bite << trailingLength);
			bits.add(biteRightSide);
		}
	}
	
	/**
	 * Append a string of 1 and 0 characters to the BitSet
	 * @param str A string containing just 1 and 0 characters
	 */
	public void append(String str) {
		for(int i = 0; i < str.length(); i++) {
			Character c = str.charAt(i);
			this.append(c);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < bits.size() - 1; i++) {
			sb.append(NumberTools.numberToBinaryString(bits.get(i), 8));
		}
		if (trailingLength > 0) {
			sb.append(NumberTools.numberToBinaryString(bits.get(bits.size() - 1) >> (7 - trailingLength), trailingLength));
		}
		return sb.toString();
	}
	
	/**
	 * Get the bytes
	 * @return the bytes
	 */
	public byte[] bytes() {
		byte[] res = new byte[bits.size()];
		for(int i = 0; i < bits.size(); i++) {
		    res[i] = bits.get(i).byteValue();
		}
		return res;
	}
	
	/**
	 *  Get the length of the last byte
	 *  @return The length of the last byte
	 */
	public int getTrailingLength() {
		return trailingLength;
	}
	
	public int getByteCount() {
		return this.bits.size();
	}
	
	/**
	 * Get index of next free bit
	 * @return The byte-bit index (from the left)
	 */
	public long getNextByteIndex() {
		return this.bits.size();
	}
	
	public void byteAlign() {
		this.trailingLength = 8;
	}
}
