package tests;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import common.BitSet;
import common.NumberTools;

public class BitSetTests {

	BitSet bits = new BitSet();
	
	@Before
	public void setUp() throws Exception {
		bits = new BitSet();
	}

	@Test
	public void testBitSet() {
		
	}

	@Test
	public void testBitSetByteArray() {
		byte[] bites = new byte[3];
		new Random().nextBytes(bites);
		bits = new BitSet(bites);

		assertEquals("The first bytes should be equal", (byte)bits.getByte(0).get(), bites[0]);
		assertEquals("The first bytes should be equal", (byte)bits.getByte(1).get(), bites[1]);
		assertEquals("The first bytes should be equal", (byte)bits.getByte(2).get(), bites[2]);
	}

	@Test
	public void testInsertByte() {
		byte[] bites = new byte[10];
		new Random().nextBytes(bites);
		bits = new BitSet(bites);
		
		byte a = bits.getByte(3).get();
		byte a2 = (byte) 0b11110101;
		byte b = bits.getByte(4).get();
		
		assertEquals((byte)bits.getByte(3).get(), a);
		assertEquals((byte)bits.getByte(4).get(), b);

		bits.insertByte(a2, 4);
		
		assertEquals((byte)bits.getByte(3).get(), a);
		assertEquals((byte)bits.getByte(4).get(), a2);
		assertEquals((byte)bits.getByte(5).get(), b);
	}

	@Test
	public void testSetByte() {
		byte[] bites = new byte[10];
		new Random().nextBytes(bites);
		bits = new BitSet(bites);
		
		byte a = bits.getByte(3).get();
		byte b = bits.getByte(4).get();
		byte b2 = (byte) 0b11110101;
		byte c = bits.getByte(5).get();
		
		assertEquals((byte)bits.getByte(3).get(), a);
		assertEquals((byte)bits.getByte(4).get(), b);
		assertEquals((byte)bits.getByte(5).get(), c);

		bits.setByte(b2, 4);
		
		assertEquals((byte)bits.getByte(3).get(), a);
		assertEquals((byte)bits.getByte(4).get(), b2);
		assertEquals((byte)bits.getByte(5).get(), c);
	}

	@Test
	public void testGetByte() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetBit() {
		fail("Not yet implemented");
	}

	@Test
	public void testAppendInteger() {
		fail("Not yet implemented");
	}

	@Test
	public void testAppendCharacter() {
		fail("Not yet implemented");
	}

	@Test
	public void testAppendByteEven() {
		byte[] bites = new byte[10];
		new Random().nextBytes(bites);
		bits = new BitSet(bites);
				
		byte[] moreBites = new byte[3];
		new Random().nextBytes(moreBites);
		
		bits.appendByte(moreBites[0]);
		bits.appendByte(moreBites[1]);
		bits.appendByte(moreBites[2]);
		assertEquals((byte)bits.getByte(0).get(), bites[0]);
		assertEquals((byte)bits.getByte(9).get(), bites[9]);
		assertEquals((byte)bits.getByte(10).get(), moreBites[0]);
		assertEquals((byte)bits.getByte(11).get(), moreBites[1]);
		assertEquals((byte)bits.getByte(12).get(), moreBites[2]);
	}
	
	@Test
	public void testAppendByteUneven() {
		//TODO: Test when there isn't byte-aligned # of bits
		byte[] bites = new byte[3];
		new Random().nextBytes(bites);
		
		System.out.println(NumberTools.numberToBinaryString(bites[0], 8));
		System.out.println(NumberTools.numberToBinaryString(bites[1], 8));
		System.out.println(NumberTools.numberToBinaryString(bites[2], 8));
		
		bits = new BitSet(bites[0]);
		
		System.out.println(bits);
		
		bits.appendBit(((bites[1] >> 0) & 1));
		bits.appendBit(((bites[1] >> 1) & 1));
		bits.appendBit(((bites[1] >> 2) & 1));
		bits.appendBit(((bites[1] >> 3) & 1));
		bits.appendBit(((bites[1] >> 4) & 1));
			
		System.out.println(bits);
		
		assertEquals((byte)bits.getByte(0).get(), bites[0]);
		assertEquals((byte)(bits.getByte(1).get() & 0b00011111), (byte)(bites[1] & 0b00011111));
		
		bits.appendByte(bites[2]);
	
		System.out.println(bits);
		
		/* what the bytes should be */
		byte b0 = bites[0];
		byte b1 = (byte)((bites[1] & 0b00011111) | (bites[2] << 5));
		byte b2 = (byte)((bites[2] >> (8 - 5)) & (0b00000111));
		
		assertEquals(b0, (byte)bits.getByte(0).get());
		assertEquals(b1, (byte)bits.getByte(1).get());
		assertEquals(b2, (byte)(bits.getByte(2).get() & 0b00000111));
	}

	@Test
	public void testAppendString() {
		fail("Not yet implemented");
	}

	@Test
	public void testToString() {
		fail("Not yet implemented");
	}

	@Test
	public void testBytes() {
		byte[] bites = new byte[20];
		new Random().nextBytes(bites);
		bits = new BitSet(bites);
		
		byte[] found = bits.bytes();
		
		for(int i = 0; i < 20; i++) {
			assertEquals("The " + i + " byte doesn't match", found[i], bites[i]);
		}
	}

	@Test
	public void testGetTrailingLength() {
		/* This behavior is kinda undefined so not tested */
		/* assertEquals("The trailing length should start as 8", (Integer)8, bits.getTrailingLength()); */
		for(int i = 0; i < 100; i++) {
			bits.appendBit(0);
			assertEquals("The trailing length is wrong", 1, bits.getTrailingLength());
			bits.appendBit(1);
			assertEquals("The trailing length is wrong", 2, bits.getTrailingLength());		
			bits.appendBit(0);
			assertEquals("The trailing length is wrong", 3, bits.getTrailingLength());		
			bits.appendBit(1);
			assertEquals("The trailing length is wrong", 4, bits.getTrailingLength());		
			bits.appendBit(1);
			assertEquals("The trailing length is wrong", 5, bits.getTrailingLength());		
			bits.appendBit(0);
			assertEquals("The trailing length is wrong", 6, bits.getTrailingLength());		
			bits.appendBit(0);
			assertEquals("The trailing length is wrong", 7, bits.getTrailingLength());		
			bits.appendBit(0);
			assertEquals("The trailing length is wrong", 8, bits.getTrailingLength());
		}
	}

}
