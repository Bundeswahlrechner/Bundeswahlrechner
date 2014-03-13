package edu.kit.iti.formal.mandatsverteilung.wahlberechnung;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;

public class BundeswahlgesetzOhneAusgleichsmandateFabrikTest {

	private Bundestagswahl bundestagswahl;
	private BerechnungsfabrikMitSperrklauselpruefer fabrik;

	@Before
	public void setUp() throws Exception {
		fabrik = new BundeswahlgesetzOhneAusgleichsmandateFabrik();
		bundestagswahl = new Bundestagswahl();
	}

	@After
	public void tearDown() throws Exception {
		fabrik = null;
		bundestagswahl = null;
	}

	@Test
	public void testErstelleBerechnungsverbund() {
		WahlverfahrenMitSperrklausel wahlverfahren = fabrik
				.erstelleBerechnungsverbund(bundestagswahl);
		assertTrue(wahlverfahren instanceof BundeswahlgesetzOhneAusgleichsmandate);
		assertTrue(wahlverfahren.getZuteilungsverfahren() instanceof SainteLagueHoechstzahl);
		assertTrue(wahlverfahren.getSperrklauselpruefer() instanceof BundeswahlgesetzSperrklauselpruefer);
	}
}
