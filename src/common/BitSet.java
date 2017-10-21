package common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;

/**
 * A collection of bits
 * 
 * @author Ezekiel Elin
 */
public class BitSet {
	private ArrayList<Byte> bits = new ArrayList<>();
	
	/**
	 * The length of last byte in the bits array
	 * Values should be: 1-8 (inclusive)
	 * 0 should not exist (the byte wouldn't be in the array)
	 * 8 represents a full last-bit
	 */
	private int trailingLength = 8;
	
	public BitSet() {
		
	}
	
	public BitSet(byte b) {
		this.appendByte(b);
	}
	
	public BitSet(byte[] bites) {
		this.appendBytes(bites);
	}
	
	public BitSet(String string) {
		byte[] bites;
		try {
			bites = Files.readAllBytes(Paths.get(string));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			bites = new byte[0];
		}
		this.appendBytes(bites);
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
	 * Remove a byte
	 * @param byteAddress The byte-addressing address
	 */
	public void removeByte(int byteAddress) {
		if (bits.size() > byteAddress) {
			bits.remove(byteAddress);
		}
	}
	
	/**
	 * Set a specific bit
	 * @param bit
	 * @param bitAddress
	 */
	public void setBit(int bit, BitIndex bitAddress) {
		byte bite = getByte(bitAddress.getByte()).orElse((byte) 0);
		bite = BitTools.setBit(bitAddress.getBit(), bite, bit);
		this.setByte(bite, bitAddress.getByte());
	}
	
	/**
	 * Append a single bit to the BitSet
	 * @param bit The bit
	 */
	public void appendBit(int bit) {
		if (trailingLength == 8) {
			bits.add((byte) 0);
			trailingLength = 0;
		}
		
		int lastIndex = bits.size() - 1;
		byte data = bits.get(lastIndex);
		data = BitTools.setBit(trailingLength, data, bit);
		bits.set(lastIndex, data);
		
		trailingLength += 1;
	}
	
	public void appendBit(char cbit) {
		if (cbit == '0') {
			this.appendBit(0);
		} else if (cbit == '1') {
			this.appendBit(1);
		} else {
			System.out.println("Encountered unexpected bit character: " + cbit + ", expected 1 or 0");
		}
	}
	
	/**
	 * Append a byte of data to the BitSet
	 * @param bite The byte to append
	 */
	public void appendByte(byte bite) {
		if (trailingLength == 8) {
			bits.add(bite);
		} else {
			byte forCurrentByte = (byte) (bite << trailingLength);
			
			byte lastByte = bits.get(bits.size() - 1);
			bits.set(bits.size() - 1, (byte) (lastByte | forCurrentByte));
			byte newByte = (byte) ((bite & 0xFF) >>> (8 - trailingLength));
			bits.add(newByte);
		}
	}
	
	/**
	 * Append a list of bytes to the BitSet
	 * @param bytes The bytes to append
	 */
	public void appendBytes(byte[] bytes) {
		for(byte b: bytes) {
			this.bits.add(new Byte(b));
		}
	}
	
	/**
	 * Append a string of 1 and 0 characters to the BitSet
	 * The characters will be appended sequentially with increasing bit-addresses
	 * @param str A string containing just 1 and 0 characters
	 */
	public void appendStringBlock(String str) {
		String reversed = new StringBuilder(str).reverse().toString();
		for(int i = 0; i < reversed.length(); i++) {
			char c = reversed.charAt(i);
			this.appendBit(c);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
//		if (trailingLength > 0) {
//			sb.append(NumberTools.numberToBinaryString(bits.get(bits.size() - 1).byteValue(), trailingLength));
//		}
		for(int i = bits.size() - 1; i >= 0; i--) {
			sb.append(NumberTools.numberToBinaryString(bits.get(i).byteValue(), 8));
		}
		return sb.toString();
	}
	
	/**
	 * Format the BitSet by byte
	 * @param start The byte index to start at
	 * @param end The byte index to end at
	 * @return The formatted string
	 */
	public String toByteString(int start, int end) {
		StringBuilder sb = new StringBuilder();
		int maxwidth = Integer.toHexString(this.bits.size() - 1).length();
		System.out.println(maxwidth);
		for(int i = this.bits.size() - 1; i >= 0; i--) {
			sb.append("0x" + NumberTools.numberToHexString(i, maxwidth) + " ");
			byte b = this.bits.get(i).byteValue();
			sb.append(NumberTools.numberToBinaryString(b, 8));
			sb.append("\n");
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
	
	/**
	 * Get the number of bytes
	 * 
	 * This will also be the byte index for the next byte suing getByte()
	 * @return The byte count
	 */
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
	
	/**
	 * Fill in the trailing byte
	 */
	public void byteAlign() {
		this.trailingLength = 8;
	}

	/**
	 * Write n bytes from a String
	 * @param firstByte The first byte to write
	 * @return The bytes
	 */
	public void writeBytes(int firstByte, String bytes) {
		if (bytes.length() % 8 != 0) {
			throw new IllegalArgumentException("Byte string must be divisible by 8");
		}
		
		int byteCount = bytes.length() / 8;
		
		int start = bytes.length() - 8;
		int end = start + 8;
		
		for(int i = 0; i < byteCount; i++) {
			String bite = bytes.substring(start, end);
			byte biteval = (byte) NumberTools.binaryStringToNumber(bite);
			this.setByte(biteval, firstByte + i);
			
			start -= 8;
			end -= 8;
		}
	}
	
	/**
	 * Read n bytes as a String
	 * @param firstByte The first byte to read
	 * @param width The number of bytes
	 * @return The bytes
	 */
	public String readBytes(int firstByte, int width) {
		StringBuilder returnString = new StringBuilder();
		
		for(int i = width - 1; i >= 0; i--) {
			Optional<Byte> bite = this.getByte(firstByte + i);
			if (bite.isPresent()) {
				returnString.append(NumberTools.numberToBinaryString(bite.get(), 8));
			} else {
				throw new IndexOutOfBoundsException("Byte " + (firstByte + i) + " is out of bounds");
			}
		}
		
		return returnString.toString();
	}
	
	/**
	 * Read an instruction as a String
	 * @param firstByte The first byte of the instruction
	 * @return The bytes
	 */
	public String readInstruction(int firstByte) {
		return this.readBytes(firstByte, 4);
	}
}
