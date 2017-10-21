package common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * A collection of bits
 * 
 * @author Ezekiel Elin
 */
public class BitSet {
	/**
	 * The bytes
	 */
	private ArrayList<Byte> byteArray = new ArrayList<>();
	public final String configurationBytes;
	
	/**
	 * The maximum byte count allowed
	 * 
	 * Use Long.MAX_VALUE to represent uncapped
	 */
	private long maxByteCount = Long.MAX_VALUE;
	
	/**
	 * The length of last byte in the bits array
	 * Values should be: 1-8 (inclusive)
	 * 0 should not exist (the byte wouldn't be in the array)
	 * 8 represents a full last-bit
	 */
	private int trailingLength = 8;
	
	/**
	 * Create an empty BitSet object
	 */
	public BitSet() {
		this.configurationBytes = null;
	}
	
	/**
	 * Create a BitSet object from a single byte
	 * @param b The byte
	 */
	public BitSet(byte b) {
		this.appendByte(b);
		this.configurationBytes = null;
	}
	
	/**
	 * Create a BitSet object from an array of bytes
	 * @param bites The bytes
	 */
	public BitSet(byte[] bites) {
		this.appendBytes(bites);
		this.configurationBytes = null;
	}
	
	/**
	 * Create a BitSet object from a file
	 * @param string The path to the file
	 */
	public BitSet(String string) {
		byte[] bites;
		try {
			bites = Files.readAllBytes(Paths.get(string));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			bites = new byte[0];
		}
		
		StringBuilder builder = new StringBuilder();
		builder.append(NumberTools.numberToBinaryString(bites[3], 8));
		builder.append(NumberTools.numberToBinaryString(bites[2], 8));
		builder.append(NumberTools.numberToBinaryString(bites[1], 8));
		builder.append(NumberTools.numberToBinaryString(bites[0], 8));
		this.configurationBytes = builder.toString();
		
		this.appendBytes(Arrays.copyOfRange(bites, 4, bites.length));
	}

	/**
	 * Insert a byte at an address
	 * @param bite The byte
	 * @param byteAddress The byte-addressing address
	 */
	public void insertByte(byte bite, int byteAddress) {
		if (byteAddress < this.getMaxByteCount()) {
			byteArray.add(byteAddress, bite);
		} else {
			throw new IndexOutOfBoundsException("Byte " + byteAddress + " is too large");
		}
	}
	
	/**
	 * Overwrite a byte at an address
	 * @param bite The byte
	 * @param byteAddress The byte-addressing address
	 */
	public void setByte(byte bite, int byteAddress) {
		byteArray.set(byteAddress, bite);
	}
	
	/**
	 * Get a byte
	 * @param byteAddress The byte-addressing address
	 * @return The byte
	 */
	public Optional<Byte> getByte(int byteAddress) {
		if (byteArray.size() > byteAddress) {
			return Optional.of((byte) byteArray.get(byteAddress));
		} else {
			return Optional.empty();
		}
	}
	
	/**
	 * Remove a byte
	 * @param byteAddress The byte-addressing address
	 */
	public void removeByte(int byteAddress) {
		if (byteArray.size() > byteAddress) {
			byteArray.remove(byteAddress);
		} else {
			throw new IndexOutOfBoundsException("Byte " + byteAddress + " is too large");
		}
	}
	
	/**
	 * Set a specific bit
	 * @param bit The bit
	 * @param bitAddress The bit address
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
			if (this.getByteCount() < this.getMaxByteCount()) {
				byteArray.add((byte) 0);
				trailingLength = 0;
			} else {
				throw new IndexOutOfBoundsException("Cannot add new byte: exceeded max byte count");
			}
		}
		
		int lastIndex = byteArray.size() - 1;
		byte data = byteArray.get(lastIndex);
		data = BitTools.setBit(trailingLength, data, bit);
		byteArray.set(lastIndex, data);
		
		trailingLength += 1;
	}
	
	/**
	 * Append a single bit to the BitSet
	 * @param cbit The bit
	 */
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
			if (this.getByteCount() < this.getMaxByteCount()) {
				byteArray.add(bite);
			} else {
				throw new IndexOutOfBoundsException("Cannot add new byte: exceeded max byte count");
			}
		} else {
			byte forCurrentByte = (byte) (bite << trailingLength);
			
			byte lastByte = byteArray.get(byteArray.size() - 1);
			byteArray.set(byteArray.size() - 1, (byte) (lastByte | forCurrentByte));
			byte newByte = (byte) ((bite & 0xFF) >>> (8 - trailingLength));
			byteArray.add(newByte);
		}
	}
	
	/**
	 * Append a list of bytes to the BitSet
	 * @param bytes The bytes to append
	 */
	public void appendBytes(byte[] bytes) {
		for(byte b: bytes) {
			if (this.getByteCount() < this.getMaxByteCount()) {
				this.byteArray.add(new Byte(b));
			} else {
				throw new IndexOutOfBoundsException("Cannot add new byte: exceeded max byte count");
			}
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
		for(int i = byteArray.size() - 1; i >= 0; i--) {
			sb.append(NumberTools.numberToBinaryString(byteArray.get(i).byteValue(), 8));
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
		int maxwidth = Integer.toHexString(this.byteArray.size() - 1).length();
		System.out.println(maxwidth);
		for(int i = this.byteArray.size() - 1; i >= 0; i--) {
			sb.append("0x" + NumberTools.numberToHexString(i, maxwidth) + " ");
			byte b = this.byteArray.get(i).byteValue();
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
		byte[] res = new byte[byteArray.size()];
		for(int i = 0; i < byteArray.size(); i++) {
		    res[i] = byteArray.get(i).byteValue();
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
		return this.byteArray.size();
	}
	
	/**
	 * Get index of next free bit
	 * @return The byte-bit index (from the left)
	 */
	public long getNextByteIndex() {
		return this.byteArray.size();
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

	/**
	 * Get the max byte count
	 * @return The max byte count
	 */
	public long getMaxByteCount() {
		return maxByteCount;
	}

	/**
	 * Set the max byte count
	 * @param maxByteCount The new max byte count
	 */
	public void setMaxByteCount(long maxByteCount) {
		this.maxByteCount = maxByteCount;
		
		if (this.getByteCount() > this.maxByteCount) {
			System.err.println("Currently exceeding new byte count quota (have " + this.getByteCount() + ", " + this.maxByteCount + " allowed)");
		}
	}
}
