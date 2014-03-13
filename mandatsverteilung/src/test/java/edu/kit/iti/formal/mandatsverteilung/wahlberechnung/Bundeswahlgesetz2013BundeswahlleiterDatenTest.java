package edu.kit.iti.formal.mandatsverteilung.wahlberechnung;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundesland;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Kreiswahlvorschlag;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Partei;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Wahlkreis;
import edu.kit.iti.formal.mandatsverteilung.importer.TestDatenBereitsteller;
import edu.kit.iti.formal.mandatsverteilung.wahlberechnung.RepeatRule.Repeat;

/**
 * Testfälle für das {@link Bundeswahlgesetz2013} in denen die Stimmdaten des
 * Bundeswahlleiters importiert werden, aus denen anschließend eine
 * Sitzverteilung errechnet wird. Die Berechnungsergebnisse werden mit den
 * amtlichen Ergebnissen verglichen.
 * 
 * @author tillneuber
 * @version 1.0
 */
public class Bundeswahlgesetz2013BundeswahlleiterDatenTest {

	/** Die {@link Bundestagswahl} auf der die Berechnung ausgeführt wird. */
	private Bundestagswahl bt;

	@Rule
	public RepeatRule repeatRule = new RepeatRule();

	/**
	 * Gibt die abgelaufene Gesamtzeit an.
	 */
	@SuppressWarnings("unused")
	private long timer = 0;

	/** Das getestete {@link Wahlverfahren}. */
	private Bundeswahlgesetz2013 verfahren;

	/** Das für den Test verwendete {@link Zuteilungsverfahren}. */
	private Zuteilungsverfahren zt;

	@Before
	public void setUp() throws Exception {
		bt = TestDatenBereitsteller.getStandardBTW();
		zt = new SainteLagueIterativ();
		verfahren = new Bundeswahlgesetz2013(bt, zt);
		BundeswahlgesetzSperrklauselpruefer spk = new BundeswahlgesetzSperrklauselpruefer();
		spk.setGesamteZweitstimmenzahl(bt.berechneZweitstimmen());
		verfahren.setzeSperrklauselpruefer(spk);
	}

	@After
	public void tearDown() throws Exception {
		bt = null;
		verfahren = null;
		zt = null;
	}

	/**
	 * Überprüfe, ob die Zahl an berechneten Ausgleichsmandaten stimmt.
	 * Vergleichsdaten:
	 * http://www.statistik-bw.de/wahlen/Bundestagswahl_2013/SitzVt.asp
	 * [Zugriff: 03.02.14]
	 */
	@Test
	public void testeAusgleichsmandatstraeger() {
		verfahren.berechneWahlergebnis();
		assertEquals("Die Zahl an Ausgleichsmandaten der CDU stimmt nicht.",
				13, bt.getPartei("CDU").getAusgleichsmandate());
		assertEquals("Die Zahl an Ausgleichsmandaten der SPD stimmt nicht.",
				10, bt.getPartei("SPD").getAusgleichsmandate());
		assertEquals("Die Zahl an Ausgleichsmandaten der Linken stimmt nicht.",
				4, bt.getPartei("DIE LINKE").getAusgleichsmandate());
		assertEquals("Die Zahl an Ausgleichsmandaten der Grünen stimmt nicht.",
				2, bt.getPartei("GRÜNE").getAusgleichsmandate());
	}

	/**
	 * Überprüft die aus den Bundeswahlleiterdaten berechneten
	 * Unterverteilungssitzzahlen der CDU.
	 */
	@Test
	public void testeCDUUnterverteilungsergebnisse() {
		verfahren.berechneWahlergebnis();
		BundestagsAssert.assertUnterverteilung(11, bt, "CDU", "SH");
		BundestagsAssert.assertUnterverteilung(6, bt, "CDU", "MV");
		BundestagsAssert.assertUnterverteilung(5, bt, "CDU", "HH");
		BundestagsAssert.assertUnterverteilung(31, bt, "CDU", "NI");
		BundestagsAssert.assertUnterverteilung(2, bt, "CDU", "HB");
		BundestagsAssert.assertUnterverteilung(9, bt, "CDU", "BB");
		BundestagsAssert.assertUnterverteilung(9, bt, "CDU", "ST");
		BundestagsAssert.assertUnterverteilung(9, bt, "CDU", "BE");
		BundestagsAssert.assertUnterverteilung(63, bt, "CDU", "NW");
		BundestagsAssert.assertUnterverteilung(17, bt, "CDU", "SN");
		BundestagsAssert.assertUnterverteilung(21, bt, "CDU", "HE");
		BundestagsAssert.assertUnterverteilung(9, bt, "CDU", "TH");
		BundestagsAssert.assertUnterverteilung(16, bt, "CDU", "RP");
		BundestagsAssert.assertUnterverteilung(43, bt, "CDU", "BW");
		BundestagsAssert.assertUnterverteilung(4, bt, "CDU", "SL");
		assertEquals("Die CDU hat in Bayern eine Landesliste eingereicht.",
				null,
				bt.getPartei("CDU").getLandesliste(bt.getBundeslandAbk("BY")));
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
				57, bt.getPartei("CSU").getOberverteilungSitzzahl());
		assertEquals(
				"Die Bundestagsgröße stimmt nicht mit der erwarteten Bundestagsgröße überein.",
				632, bt.getSitzzahl());
	}

	/**
	 * Überprüft die aus den Bundeswahlleiterdaten berechneten
	 * Unterverteilungssitzzahlen der CSU.
	 */
	@Test
	public void testeCSUUnterverteilungsergebnisse() {
		verfahren.berechneWahlergebnis();
		BundestagsAssert.assertUnterverteilung(56, bt, "CSU", "BY");
		Iterator<Bundesland> blIt = bt.bundeslaenderIterator();
		while (blIt.hasNext()) {
			Bundesland bl = blIt.next();
			if (!(bl.getAbkuerzung().equals("BY"))) {
				assertEquals("Die CSU hat in " + bl.getName()
						+ " eine Landesliste eingereicht", null,
						bt.getPartei("CSU").getLandesliste(bl));
			}
		}
	}

	/**
	 * Überprüft die aus den Bundeswahlleiterdaten berechneten
	 * Unterverteilungssitzzahlen der Grünen.
	 */
	@Test
	public void testeGRUENEUnterverteilungsergebnisse() {
		verfahren.berechneWahlergebnis();
		BundestagsAssert.assertUnterverteilung(3, bt, "GRÜNE", "SH");
		BundestagsAssert.assertUnterverteilung(1, bt, "GRÜNE", "MV");
		BundestagsAssert.assertUnterverteilung(2, bt, "GRÜNE", "HH");
		BundestagsAssert.assertUnterverteilung(6, bt, "GRÜNE", "NI");
		BundestagsAssert.assertUnterverteilung(1, bt, "GRÜNE", "HB");
		BundestagsAssert.assertUnterverteilung(1, bt, "GRÜNE", "BB");
		BundestagsAssert.assertUnterverteilung(1, bt, "GRÜNE", "ST");
		BundestagsAssert.assertUnterverteilung(4, bt, "GRÜNE", "BE");
		BundestagsAssert.assertUnterverteilung(13, bt, "GRÜNE", "NW");
		BundestagsAssert.assertUnterverteilung(2, bt, "GRÜNE", "SN");
		BundestagsAssert.assertUnterverteilung(5, bt, "GRÜNE", "HE");
		BundestagsAssert.assertUnterverteilung(1, bt, "GRÜNE", "TH");
		BundestagsAssert.assertUnterverteilung(3, bt, "GRÜNE", "RP");
		BundestagsAssert.assertUnterverteilung(9, bt, "GRÜNE", "BY");
		BundestagsAssert.assertUnterverteilung(10, bt, "GRÜNE", "BW");
		BundestagsAssert.assertUnterverteilung(1, bt, "GRÜNE", "SL");
	}

	/**
	 * Überprüft die aus den Bundeswahlleiterdaten berechneten
	 * Unterverteilungssitzzahlen der Linken.
	 */
	@Test
	public void testeLINKEUnterverteilungsergebnisse() {
		verfahren.berechneWahlergebnis();
		BundestagsAssert.assertUnterverteilung(1, bt, "DIE LINKE", "SH");
		BundestagsAssert.assertUnterverteilung(3, bt, "DIE LINKE", "MV");
		BundestagsAssert.assertUnterverteilung(1, bt, "DIE LINKE", "HH");
		BundestagsAssert.assertUnterverteilung(4, bt, "DIE LINKE", "NI");
		BundestagsAssert.assertUnterverteilung(1, bt, "DIE LINKE", "HB");
		BundestagsAssert.assertUnterverteilung(5, bt, "DIE LINKE", "BB");
		BundestagsAssert.assertUnterverteilung(5, bt, "DIE LINKE", "ST");
		BundestagsAssert.assertUnterverteilung(6, bt, "DIE LINKE", "BE");
		BundestagsAssert.assertUnterverteilung(10, bt, "DIE LINKE", "NW");
		BundestagsAssert.assertUnterverteilung(8, bt, "DIE LINKE", "SN");
		BundestagsAssert.assertUnterverteilung(3, bt, "DIE LINKE", "HE");
		BundestagsAssert.assertUnterverteilung(5, bt, "DIE LINKE", "TH");
		BundestagsAssert.assertUnterverteilung(2, bt, "DIE LINKE", "RP");
		BundestagsAssert.assertUnterverteilung(4, bt, "DIE LINKE", "BY");
		BundestagsAssert.assertUnterverteilung(5, bt, "DIE LINKE", "BW");
		BundestagsAssert.assertUnterverteilung(1, bt, "DIE LINKE", "SL");
	}

	/**
	 * Prüft, ob die aus den Bundeswahlleiterdaten berechneten
	 * Oberverteilungssitzzahlen mit den amtlichen Ergebnissen übereinstimmen
	 * und ob die Bundestagsgröße korrekt berechnet wurde.
	 */
	@Test(timeout = 5000)
	public void testeOberverteilungsergebnisse() {
		verfahren.berechneWahlergebnis();
		assertEquals(
				"Die Oberverteilungssitzzahl der CDU stimmt nicht mit dem amtlichen Ergebnis überein.",
				255, bt.getPartei("CDU").getOberverteilungSitzzahl());
		assertEquals(
				"Die Oberverteilungssitzzahl der SPD stimmt nicht mit dem amtlichen Ergebnis überein.",
				193, bt.getPartei("SPD").getOberverteilungSitzzahl());
		assertEquals(
				"Die Oberverteilungssitzzahl der Linken stimmt nicht mit dem amtlichen Ergebnis überein.",
				64, bt.getPartei("DIE LINKE").getOberverteilungSitzzahl());
		assertEquals(
				"Die Oberverteilungssitzzahl der Grünen stimmt nicht mit dem amtlichen Ergebnis überein.",
				63, bt.getPartei("GRÜNE").getOberverteilungSitzzahl());
		assertEquals(
				"Die Oberverteilungssitzzahl der CSU stimmt nicht mit dem amtlichen Ergebnis überein.",
				56, bt.getPartei("CSU").getOberverteilungSitzzahl());
		assertEquals(
				"Die Bundestagsgröße stimmt nicht mit der Bundestagsgröße nach offizieller Berechnung überein.",
				631, bt.getSitzzahl());
		// Überprüfe, ob die Oberverteilungssitzzahl aller anderen Parteien 0
		// ist und ob die Summe der Oberverteilungssitzzahlen der
		// Bundestagsgröße entspricht. Teste hierzu, ob alle
		// Oberverteilungssitzzahlen größer oder gleich 0 sind und in der Summe
		// der amtlichen Bundestagsgröße von 631 entsprechen.
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
				631, verteilteSitze);
	}

	/**
	 * Prüft, ob die aus den Bundeswahlleiterdaten berechneten
	 * Oberverteilungssitzzahlen mit den amtlichen Ergebnissen übereinstimmen
	 * und ob die Bundestagsgröße korrekt berechnet wurde, nachdem die
	 * Berechnung zweimal durchgeführt wurde.
	 */
	@Test(timeout = 5000)
	public void testeOberverteilungsergebnisseNachErneuterBerechnung() {
		verfahren.berechneWahlergebnis();
		// Führe erneute Berechnung auf bereits berechneten Daten aus.
		verfahren.berechneWahlergebnis();
		assertEquals(
				"Die Oberverteilungssitzzahl der CDU stimmt nicht mit dem amtlichen Ergebnis überein.",
				255, bt.getPartei("CDU").getOberverteilungSitzzahl());
		assertEquals(
				"Die Oberverteilungssitzzahl der Linken stimmt nicht mit dem amtlichen Ergebnis überein.",
				64, bt.getPartei("DIE LINKE").getOberverteilungSitzzahl());
		assertEquals(
				"Die Oberverteilungssitzzahl der Grünen stimmt nicht mit dem amtlichen Ergebnis überein.",
				63, bt.getPartei("GRÜNE").getOberverteilungSitzzahl());
		assertEquals(
				"Die Oberverteilungssitzzahl der CSU stimmt nicht mit dem amtlichen Ergebnis überein.",
				56, bt.getPartei("CSU").getOberverteilungSitzzahl());
		assertEquals(
				"Die Bundestagsgröße stimmt nicht mit der Bundestagsgröße nach offizieller Berechnung überein.",
				631, bt.getSitzzahl());
		// Überprüfe, ob die Oberverteilungssitzzahl aller anderen Parteien 0
		// ist und ob die Summe der Oberverteilungssitzzahlen der
		// Bundestagsgröße entspricht. Teste hierzu, ob alle
		// Oberverteilungssitzzahlen größer oder gleich 0 sind und in der Summe
		// der amtlichen Bundestagsgröße von 631 entsprechen.
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
				631, verteilteSitze);
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
				632, bt.getSitzzahl());
	}

	/**
	 * Teste die Gesamtlaufzeit mehrerer Wahlberechnungen.
	 */
	@Test
	@Repeat(times = 1)
	public void testePerformance() {
		long aktuelleStartZeit = System.currentTimeMillis();
		verfahren.berechneWahlergebnis();
		timer += (System.currentTimeMillis() - aktuelleStartZeit);
		// System.out.println("Gesamtlaufzeit: " + timer);
	}

	/**
	 * Überprüft die aus den Bundeswahlleiterdaten berechneten
	 * Unterverteilungssitzzahlen der SPD.
	 */
	@Test
	public void testeSPDUnterverteilungsergebnisse() {
		verfahren.berechneWahlergebnis();
		BundestagsAssert.assertUnterverteilung(9, bt, "SPD", "SH");
		BundestagsAssert.assertUnterverteilung(3, bt, "SPD", "MV");
		BundestagsAssert.assertUnterverteilung(5, bt, "SPD", "HH");
		BundestagsAssert.assertUnterverteilung(25, bt, "SPD", "NI");
		BundestagsAssert.assertUnterverteilung(2, bt, "SPD", "HB");
		BundestagsAssert.assertUnterverteilung(5, bt, "SPD", "BB");
		BundestagsAssert.assertUnterverteilung(4, bt, "SPD", "ST");
		BundestagsAssert.assertUnterverteilung(8, bt, "SPD", "BE");
		BundestagsAssert.assertUnterverteilung(52, bt, "SPD", "NW");
		BundestagsAssert.assertUnterverteilung(6, bt, "SPD", "SN");
		BundestagsAssert.assertUnterverteilung(16, bt, "SPD", "HE");
		BundestagsAssert.assertUnterverteilung(3, bt, "SPD", "TH");
		BundestagsAssert.assertUnterverteilung(10, bt, "SPD", "RP");
		BundestagsAssert.assertUnterverteilung(22, bt, "SPD", "BY");
		BundestagsAssert.assertUnterverteilung(20, bt, "SPD", "BW");
		BundestagsAssert.assertUnterverteilung(3, bt, "SPD", "SL");
	}

	/**
	 * Überprüft stichprobenartig die Wahlkreisgewinner.
	 */
	@Test
	public void testeWahlkreisgewinner() {
		verfahren.berechneWahlergebnis();
		assertEquals("Sütterlin-Waack, Dr. Sabine", bt.getWahlkreis(1)
				.getGewinner().getName());
		assertEquals("Schulze, Dr. Klaus-Peter", bt.getWahlkreis(64)
				.getGewinner().getName());
		assertEquals("Schwartze, Stefan", bt.getWahlkreis(133).getGewinner()
				.getName());
		assertEquals("Herzog, Gustav", bt.getWahlkreis(210).getGewinner()
				.getName());
		assertEquals("Harbarth, Dr. Stephan", bt.getWahlkreis(277)
				.getGewinner().getName());

	}

}
