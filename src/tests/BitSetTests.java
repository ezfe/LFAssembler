package tests;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import common.BitSet;

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
	public void testAppendByte() {
		fail("Not yet implemented");
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
			bits.append(0);
			assertEquals("The trailing length is wrong", 1, bits.getTrailingLength());
			bits.append(1);
			assertEquals("The trailing length is wrong", 2, bits.getTrailingLength());		
			bits.append(0);
			assertEquals("The trailing length is wrong", 3, bits.getTrailingLength());		
			bits.append(1);
			assertEquals("The trailing length is wrong", 4, bits.getTrailingLength());		
			bits.append(1);
			assertEquals("The trailing length is wrong", 5, bits.getTrailingLength());		
			bits.append(0);
			assertEquals("The trailing length is wrong", 6, bits.getTrailingLength());		
			bits.append(0);
			assertEquals("The trailing length is wrong", 7, bits.getTrailingLength());		
			bits.append(0);
			assertEquals("The trailing length is wrong", 8, bits.getTrailingLength());
		}
	}

}
