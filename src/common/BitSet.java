package common;

import java.util.ArrayList;

public class BitSet {
	private ArrayList<Byte> bits;
	private Integer trailingLength;
	
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
	 * Append a single bit to the BitSet
	 * @param bit The bit
	 */
	public void append(Integer bit) {
		if (trailingLength == 8) {
			bits.add((byte) 0);
			trailingLength = 0;
		}
		
		Integer lastIndex = bits.size() - 1;
		byte data = bits.get(lastIndex);
		Integer setAt = 7 - trailingLength;
		data = BitTools.setBit(setAt, data, bit);
		bits.set(lastIndex, data);
		
		trailingLength += 1;
	}
	
	public void append(Character c) {
		if (c == '0') {
			this.append(0);
		} else if (c == '1') {
			this.append(1);
		} else {
			System.out.println("Encountered unexpected bit character: " + c + ", expected 1 or 0");
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
	 * Get an iterator for the bytes
	 * @return
	 */
	public byte[] bytes() {
		byte[] res = new byte[bits.size()];
		for(int i = 0; i < bits.size(); i++) {
		    res[i] = bits.get(i).byteValue();
		}
		return res;
	}
	
	public Integer getTrailingLength() {
		return trailingLength;
	}
}
