package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import common.BitTools;

public class BitToolsTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testBitAt() {
		byte b = (byte) 0b10100011;
		assertEquals(1, (int) BitTools.bitAt(0, b));
		assertEquals(1, (int) BitTools.bitAt(1, b));
		assertEquals(0, (int) BitTools.bitAt(2, b));
		assertEquals(0, (int) BitTools.bitAt(3, b));
		assertEquals(0, (int) BitTools.bitAt(4, b));
		assertEquals(1, (int) BitTools.bitAt(5, b));
		assertEquals(0, (int) BitTools.bitAt(6, b));
		assertEquals(1, (int) BitTools.bitAt(7, b));
	}

	@Test
	public void testSetBit() {
		byte b = (byte) 0b10100011;
		BitTools.setBit(0, b, 1);
		assertEquals((byte) 0b10100011, BitTools.setBit(0, b, 1));
		assertEquals((byte) 0b10100111, BitTools.setBit(2, b, 1));
		assertEquals((byte) 0b10100011, BitTools.setBit(4, b, 0));
		assertEquals((byte) 0b10000011, BitTools.setBit(5, b, 0));
	}

	@Test
	public void testSetTrue() {
		byte b = (byte) 0b10100011;
		BitTools.setBit(0, b, 1);
		assertEquals((byte) 0b10100011, BitTools.setTrue(0, b));
		assertEquals((byte) 0b10100111, BitTools.setTrue(2, b));
	}

	@Test
	public void testSetFalse() {
		byte b = (byte) 0b10100011;
		BitTools.setBit(0, b, 1);
		assertEquals((byte) 0b10100011, BitTools.setFalse(4, b));
		assertEquals((byte) 0b10000011, BitTools.setFalse(5, b));
	}

}
