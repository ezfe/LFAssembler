package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import common.Label;

public class LabelTests {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testHashCode() {
		Label l = new Label("someLabel");
		Label l2 = new Label("someLabel2");
		assertNotEquals("The hashcodes should not be equal", l.hashCode(), l2.hashCode());
	}

	@Test
	public void testLabel() {
		Label l = new Label("TheValue");
		assertEquals("TheValue", l.getToken());
	}
}
