package tests;

import static org.junit.Assert.*;

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
		byte a = (byte) 0b11001111;
		byte b = (byte) 0b10111000;
		byte c = (byte) 0b11011100;
		byte[] bytes = new byte[]{a, b, c};
		bits = new BitSet(bytes);

		assertEquals("The first bytes should be equal", bits.getByte(0), a);
		assertEquals("The first bytes should be equal", bits.getByte(1), b);
		assertEquals("The first bytes should be equal", bits.getByte(2), c);
	}

	@Test
	public void testInsertByte() {
		for(int i = 0; i < 8 * 10; i++) {
			bits.append(i % 2);
		}
		
		byte a = bits.getByte(3);
		byte a2 = (byte) 0b11110101;
		byte b = bits.getByte(4);
		
		assertEquals(bits.getByte(3), a);
		assertEquals(bits.getByte(4), b);

		bits.insertByte(a2, 4);
		
		assertEquals(bits.getByte(3), a);
		assertEquals(bits.getByte(4), a2);
		assertEquals(bits.getByte(5), b);
	}

	@Test
	public void testSetByte() {
		fail("Not yet implemented");
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
		fail("Not yet implemented");
	}

	@Test
	public void testGetTrailingLength() {
		assertEquals("The trailing length should start as 8", (Integer)8, bits.getTrailingLength());
		bits.append(0);
		assertEquals("The trailing length should be 1", (Integer)1, bits.getTrailingLength());
		bits.append(1);
		assertEquals("The trailing length should be 2", (Integer)2, bits.getTrailingLength());		
		for(int i = 0; i < 68; i++) {
			bits.append(i % 2);
			assertEquals("The trailing length should be "+i + 3, (Integer)(i + 3), bits.getTrailingLength());
		}
	}

}
