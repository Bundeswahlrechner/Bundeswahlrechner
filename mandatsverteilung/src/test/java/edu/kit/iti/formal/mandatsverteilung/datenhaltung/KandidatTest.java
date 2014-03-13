package edu.kit.iti.formal.mandatsverteilung.datenhaltung;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Tests für die Klasse Kandidat.
 */
@RunWith(value = Parameterized.class)
public class KandidatTest {

	@Parameters
	public static Iterable<Object[]> verfahren() {
		Object[][] kandiaten = new Object[][] {
				{ new Kandidat("Mustermann, Dr. Erika") },
				{ new Kreiswahlvorschlag("Mustermann, Dr. Erika",
						new Wahlkreis("WK", 1)) } };
		return Arrays.asList(kandiaten);
	}

	Kandidat kandidat;

	public KandidatTest(Kandidat kandidat) {
		this.kandidat = kandidat;
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		kandidat = null;
	}

	@Test
	public void testeHatAusgleichsmandat() throws Exception {
		assertFalse(kandidat.hatAusgleichsmandat());
	}

	@Test
	public void testeListenplatz() throws Exception {
		assertEquals(-1, kandidat.getLandeslistenplatz());
	}

	@Test
	public void testeParteiName() throws Exception {
		assertEquals("parteilos", kandidat.parteiName());
	}

	/**
	 * Test method for
	 * {@link edu.kit.iti.formal.mandatsverteilung.datenhaltung.Kandidat#getName()}
	 * .
	 */
	@Test
	public void testGetName() {
		String name = kandidat.getName();
		assertEquals("Falscher Name zurückgegeben.", "Mustermann, Dr. Erika",
				name);
	}

	/**
	 * Test method for
	 * {@link edu.kit.iti.formal.mandatsverteilung.datenhaltung.Kandidat#istWahlkreisGewinner()}
	 * .
	 */
	@Test
	public void testIstWahlkreisGewinner() {
		boolean gewonnen = kandidat.istWahlkreisGewinner();
		assertFalse(
				"Der Kandidat ist Wahlkreisgewinner obwohl er nicht als solcher gesetzt wurde.",
				gewonnen);
	}

}
