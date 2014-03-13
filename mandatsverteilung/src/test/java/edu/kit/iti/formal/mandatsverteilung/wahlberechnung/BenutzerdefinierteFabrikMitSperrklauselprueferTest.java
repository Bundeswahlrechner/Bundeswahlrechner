package edu.kit.iti.formal.mandatsverteilung.wahlberechnung;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;

public class BenutzerdefinierteFabrikMitSperrklauselprueferTest {

	private Bundestagswahl bundestagswahl;
	private BenutzerdefinierteFabrikMitSperrklauselpruefer fabrik;
	private BenutzerdefinierteFabrikMitSperrklauselpruefer ungueltigeFabrik;

	@Before
	public void setUp() throws Exception {
		fabrik = new BenutzerdefinierteFabrikMitSperrklauselpruefer(0.11, 8,
				"Variante ohne Ausgleichsmandate", "D'Hondt");
		ungueltigeFabrik = new BenutzerdefinierteFabrikMitSperrklauselpruefer(
				0.11, 8, "Unbekannt", "Unbekannt");
		bundestagswahl = new Bundestagswahl();
	}

	@After
	public void tearDown() throws Exception {
		fabrik = null;
		bundestagswahl = null;
		ungueltigeFabrik = null;
	}

	@Test
	public void testErstelleBerechnungsverbundBundestagswahl() {
		WahlverfahrenMitSperrklausel wahlverfahren = fabrik
				.erstelleBerechnungsverbund(bundestagswahl);
		assertTrue(wahlverfahren instanceof BundeswahlgesetzOhneAusgleichsmandate);
		assertTrue(wahlverfahren.getZuteilungsverfahren() instanceof DHondt);
		assertTrue(wahlverfahren.getSperrklauselpruefer() instanceof BundeswahlgesetzSperrklauselpruefer);
	}

	@Test(expected = IllegalStateException.class)
	public void testErstelleUngueltigesWahlverfahren() throws Exception {
		assertNull(ungueltigeFabrik.erstelleWahlverfahren(bundestagswahl,
				new HareNiemeyer()));
	}

	@Test(expected = IllegalStateException.class)
	public void testErstelleUngueltigesZuteilungsverfahren() throws Exception {
		assertNull(ungueltigeFabrik.erstelleZuteilungsverfahren());
	}

	@Test
	public void testVerfuegbareWahlverfahren() {
		Set<String> wahlverfahren = BenutzerdefinierteFabrikMitSperrklauselpruefer
				.verfuegbareWahlverfahren();
		assertTrue(wahlverfahren.size() >= 1);
	}

	@Test
	public void testVerfuegbareZuteilungsverfahren() {
		Set<String> zuteilungsverfahren = BenutzerdefinierteFabrikMitSperrklauselpruefer
				.verfuegbareZuteilungsverfahren();
		assertTrue(zuteilungsverfahren.size() >= 1);
	}

}
