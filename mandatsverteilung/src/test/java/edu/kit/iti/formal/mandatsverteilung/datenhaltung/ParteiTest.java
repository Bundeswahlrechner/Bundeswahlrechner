package edu.kit.iti.formal.mandatsverteilung.datenhaltung;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ParteiTest {

	private Partei partei1;
	private Partei partei2;

	@Before
	public void setUp() throws Exception {
		partei1 = new Partei("Partei 1");
		partei2 = new Partei("Partei 2");
	}

	@After
	public void tearDown() throws Exception {
		partei1 = null;
		partei2 = null;
	}

	@Test
	public void testeFarbe() throws Exception {
		partei1.setFarbe(new Color(100, 50, 25));
		assertEquals(new Color(100, 50, 25), partei1.getFarbe());
	}

	@Test
	public void testeGetName() throws Exception {
		assertEquals("Partei 1", partei1.getName());
		assertEquals("Partei 2", partei2.getName());
	}

	@Test
	public void testeIsNationaleMinderheitInitialWert() throws Exception {
		assertFalse(partei1.isNationaleMinderheit());
		assertFalse(partei2.isNationaleMinderheit());
	}

	@Test
	public void testeIsSperrklauselUeberwundenInitialWert() throws Exception {
		assertFalse(partei1.isSperrklauselUeberwunden());
		assertFalse(partei2.isSperrklauselUeberwunden());
	}

	@Test
	public void testeNationaleMinderheit() throws Exception {
		partei1.setNationaleMinderheit(true);
		partei2.setNationaleMinderheit(false);
		assertTrue(partei1.isNationaleMinderheit());
		assertFalse(partei2.isNationaleMinderheit());
	}

	@Test
	public void testeSperrklauselUeberwunden() throws Exception {
		partei1.setSperrklauselUeberwunden(false);
		partei2.setSperrklauselUeberwunden(true);
		assertFalse(partei1.isSperrklauselUeberwunden());
		assertTrue(partei2.isSperrklauselUeberwunden());
	}

	@Test
	public void testeToString() throws Exception {
		assertEquals("Partei 1", partei1.toString());
		assertEquals("Partei 2", partei2.toString());
	}

}
