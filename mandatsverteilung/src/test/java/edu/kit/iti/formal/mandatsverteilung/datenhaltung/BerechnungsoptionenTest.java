package edu.kit.iti.formal.mandatsverteilung.datenhaltung;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BerechnungsoptionenTest {

	private Berechnungsoptionen berechnungsoptionenDefault;
	private Berechnungsoptionen berechnungsoptionenManuell;

	@Before
	public void setUp() throws Exception {
		berechnungsoptionenManuell = new Berechnungsoptionen("Fabrik", 7, 0.23,
				"Wahlverfahren", "Zuteilungsverfahren");
		berechnungsoptionenDefault = new Berechnungsoptionen();
	}

	@After
	public void tearDown() throws Exception {
		berechnungsoptionenManuell = null;
		berechnungsoptionenDefault = null;
	}

	@Test
	public void testGetFabrik() {
		assertEquals("Fabrik", berechnungsoptionenManuell.getFabrik());
		assertEquals("Bundeswahlgesetz 2013",
				berechnungsoptionenDefault.getFabrik());
	}

	@Test
	public void testGetGrundmandate() {
		assertEquals(7, berechnungsoptionenManuell.getGrundmandate());
		assertEquals(3, berechnungsoptionenDefault.getGrundmandate());
	}

	@Test
	public void testGetProzenthuerde() {
		assertEquals(0.23, berechnungsoptionenManuell.getProzenthuerde(),
				0.0001);
		assertEquals(0.05, berechnungsoptionenDefault.getProzenthuerde(),
				0.0001);
	}

	@Test
	public void testGetWahlverfahren() {
		assertEquals("Wahlverfahren",
				berechnungsoptionenManuell.getWahlverfahren());
		assertEquals("Bundeswahlgesetz 2013",
				berechnungsoptionenDefault.getWahlverfahren());
	}

	@Test
	public void testGetZuteilungsverfahren() {
		assertEquals("Zuteilungsverfahren",
				berechnungsoptionenManuell.getZuteilungsverfahren());
		assertEquals("Sainte-Lague Iterativ",
				berechnungsoptionenDefault.getZuteilungsverfahren());
	}
}
