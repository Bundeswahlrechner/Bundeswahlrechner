package edu.kit.iti.formal.mandatsverteilung.wahlberechnung;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Kandidat;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Kreiswahlvorschlag;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Partei;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Wahlkreis;
import edu.kit.iti.formal.mandatsverteilung.importer.TestDatenBereitsteller;

public class Bundeswahlgesetz2013OhneAusgleichsmandateBundeswahlleiterDatenTest {

	private Bundestagswahl bt;
	private BundeswahlgesetzOhneAusgleichsmandate verfahren;
	private Zuteilungsverfahren zt;

	@Before
	public void setUp() throws Exception {
		bt = TestDatenBereitsteller.getStandardBTW();
		zt = new SainteLagueIterativ();
		verfahren = new BundeswahlgesetzOhneAusgleichsmandate(bt, zt);
		BundeswahlgesetzSperrklauselpruefer spk = new BundeswahlgesetzSperrklauselpruefer();
		spk.setGesamteZweitstimmenzahl(bt.berechneZweitstimmen());
		verfahren.setzeSperrklauselpruefer(spk);
	}

	/**
	 * Fügt einen CSU Kreiswahlvorschlag in einem Wahlkreis in Baden-Württemberg
	 * hinzu und lässt diesen dort ein Direktmandat erringen. Anschließend wird
	 * das berechnete Ergebnis überprüft.
	 */
	@Test
	public void testeCSUDirektmandatInBW() {
		Wahlkreis wk = bt.getBundeslandAbk("BW").wahlkreisIterator().next();
		Kreiswahlvorschlag kwv = new Kreiswahlvorschlag("Test-KWV", wk);
		kwv.setPartei(bt.getPartei("CSU"));
		kwv.setStimmen(100000000);
		verfahren.berechneWahlergebnis();
		assertEquals(
				"Die Oberverteilungssitzzahl der CSU stimmt nicht mit dem erwarteten Ergebnis überein.",
				53, bt.getPartei("CSU").getOberverteilungSitzzahl());
		assertEquals(
				"Die Bundestagsgröße stimmt nicht mit der erwarteten Bundestagsgröße überein.",
				603, bt.getSitzzahl());
	}

	/**
	 * Prüft ob Landeslistenschöpfung korrekt behandelt wird.
	 */
	@Test
	public void testeLandeslistenerschoepfung() {
		// Setze die Zweitstimmenzahl der CDU-Landesliste in NW hoch, um
		// Landeslistenschöpfung zu erreichen.
		bt.getBundeslandAbk("NW").wahlkreisIterator().next()
				.setParteiZweitstimmen(bt.getPartei("CDU"), 1000000000);
		verfahren.berechneWahlergebnis();
		int mandatszahl = 0;
		Iterator<Kandidat> mandateIt = bt.getPartei("CDU")
				.getLandesliste(bt.getBundeslandAbk("NW")).mandateIterator();
		while (mandateIt.hasNext()) {
			mandatszahl++;
			mandateIt.next();
		}
		assertTrue(
				"Die Landesliste der CDU entsendet mehr Kandidaten als sie enthält.",
				bt.getPartei("CDU").getLandesliste(bt.getBundeslandAbk("NW"))
						.getListengroesse() >= mandatszahl);
	}

	/**
	 * Prüft, ob die aus den Bundeswahlleiterdaten berechneten
	 * Oberverteilungssitzzahlen kleiner sind, als die Ergebnisse nach
	 * Berechnung durch das Bundeswahlgesetz 2013.
	 */
	@Test(timeout = 5000)
	public void testeOberverteilungsergebnisse() {
		verfahren.berechneWahlergebnis();
		assertTrue("Die Oberverteilungssitzzahl der CDU stimmt nicht.",
				255 >= bt.getPartei("CDU").getOberverteilungSitzzahl());
		assertTrue("Die Oberverteilungssitzzahl der Linken stimmt nicht.",
				64 >= bt.getPartei("DIE LINKE").getOberverteilungSitzzahl());
		assertTrue("Die Oberverteilungssitzzahl der Grünen stimmt nicht.",
				63 >= bt.getPartei("GRÜNE").getOberverteilungSitzzahl());
		assertTrue("Die Oberverteilungssitzzahl der CSU stimmt nicht.",
				56 >= bt.getPartei("CSU").getOberverteilungSitzzahl());
		assertTrue("Die Bundestagsgröße stimmt nicht.", 631 >= bt.getSitzzahl());
		// Überprüfe, ob die Oberverteilungssitzzahl aller anderen Parteien 0
		// ist und ob die Summe der Oberverteilungssitzzahlen der
		// Bundestagsgröße entspricht. Teste hierzu, ob alle
		// Oberverteilungssitzzahlen größer oder gleich 0 sind und in der Summe
		// der Bundestagsgröße entsprechen.
		Iterator<Partei> parteienIt = bt.parteienIterator();
		int verteilteSitze = 0;
		while (parteienIt.hasNext()) {
			Partei partei = parteienIt.next();
			assertTrue(
					"Eine Partei hat eine negative Oberverteilungssitzzahl.",
					0 <= partei.getOberverteilungSitzzahl());
			verteilteSitze += partei.getOberverteilungSitzzahl();
		}
		assertEquals(
				"Eine Partei hat mehr Oberverteilungssitze erhalten als ihr zustehen.",
				bt.getSitzzahl(), verteilteSitze);
	}

	/**
	 * Fügt einen parteilosen Kreiswahlvorschlag in einem Wahlkreis in
	 * Baden-Württemberg hinzu und lässt diesen dort ein Direktmandat erringen.
	 * Anschließend wird das berechnete Ergebnis überprüft.
	 */
	@Test
	public void testeParteilosenWahlkreisgewinner() {
		Wahlkreis wk = bt.getBundeslandAbk("BW").wahlkreisIterator().next();
		Kreiswahlvorschlag kwv = new Kreiswahlvorschlag("Test-KWV", wk);
		kwv.setStimmen(100000000);
		verfahren.berechneWahlergebnis();
		assertEquals(
				"Die Zahl an parteilosen Wahlkreisgewinnern ist nicht korrekt.",
				1, bt.getParteiloseMandatstraeger());
		assertEquals(
				"Die Zahl an parteilosen Wahlkreisgewinnern in BW ist nicht korrekt.",
				1, bt.getBundeslandAbk("BW").getParteiloseMandatstraeger());
		assertEquals(
				"Die Bundestagsgröße stimmt nicht mit der erwarteten Bundestagsgröße überein.",
				603, bt.getSitzzahl());
	}

}
