package edu.kit.iti.formal.mandatsverteilung.datenhaltung;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class LandeslisteTest {

	/**
	 * Das Bundesland in dem die getestete Landesliste liegt.
	 */
	private Bundesland bl;

	@Rule
	public ExpectedException geworfeneException = ExpectedException.none();

	/**
	 * Die getestete Landesliste.
	 */
	private Landesliste ll;

	/**
	 * Die Partei, zu der die getestete Landesliste gehört.
	 */
	private Partei partei;

	@Before
	public void setUp() {
		Bundestagswahl bt = new Bundestagswahl();
		bl = new Bundesland("BL 1", "1", 10);
		bt.addBundesland(bl);
		ll = new Landesliste();
		partei = new Partei("Testpartei");
		bt.addPartei(partei);
		partei.setLandesliste(bl, ll);
	}

	/**
	 * Prüfe ob die Landesliste erkennt, in welchem Bundesland sie liegt.
	 */
	@Test
	public void testeBundeslandZugehoerigkeit() {
		assertEquals("Die Landesliste liefert das falsche Bundesland.", bl,
				ll.getBundesland());
	}

	/**
	 * Besetzt einen Landeslistenplatz mit 2 Kandidaten.
	 */
	@Test
	public void testeDoppelteLandeslistenplatzbesetzung() {
		geworfeneException.expect(IllegalArgumentException.class);
		ll.addKandidat(new Kandidat("Testkandidat 1"), 1);
		ll.addKandidat(new Kandidat("Testkandidat 2"), 1);
	}

	/**
	 * Prüft, ob die Landesliste eine gesetzte Pseudoverteilungssitzzahl korrekt
	 * zurückgibt.
	 */
	@Test
	public void testeGesetztePsedudoverteilung() {
		int pseudoverteilung = 5;
		ll.setPseudoverteilungSitzzahl(pseudoverteilung);
		assertEquals(
				"Die Landesliste liefert die falsche Pseudoverteilungssitzzahl.",
				pseudoverteilung, ll.getPseudoverteilungSitzzahl());
	}

	/**
	 * Prüft, ob die Landesliste eine gesetzte Unterverteilungssitzzahl korrekt
	 * zurückgibt.
	 */
	@Test
	public void testeGesetzteUnterverteilungssitzzahl() {
		int unterverteilung = 5;
		ll.setUnterverteilungSitzzahl(unterverteilung);
		assertEquals(
				"Die Landesliste liefert die falsche Unterverteilungssitzzahl.",
				unterverteilung, ll.getUnterverteilungSitzzahl());
	}

	/**
	 * Fügt der Landesliste mehrere Kandidaten hinzu und prüft, ob diese korrekt
	 * zurückgegeben werden.
	 */
	@Test
	public void testeKandidatenHinzufuegen() {
		List<Kandidat> kandidaten = new LinkedList<Kandidat>();
		kandidaten.add(new Kandidat("Kandidat 1"));
		kandidaten.add(new Kandidat("Kandidat 2"));
		kandidaten.add(new Kandidat("Kandidat 3"));
		for (int i = 0; i < kandidaten.size(); i++) {
			ll.addKandidat(kandidaten.get(i), i + 1);
		}
		Iterator<Kandidat> kandidatenIt = ll.kandidatenIterator();
		int counter = 0;
		while (kandidatenIt.hasNext()) {
			Kandidat kandidat = kandidatenIt.next();
			assertEquals("Der Kandidat auf Landeslistenplatz " + (counter + 1)
					+ " wurde nicht korrekt zurückgegeben.",
					kandidaten.get(counter), kandidat);
			counter++;
		}
		assertEquals(
				"Es wurden nicht alle Landeslistenkandidaten zurückgegeben.",
				3, counter);
	}

	/**
	 * Prüft ob die Landeslistengröße mit der Zahl der gesetzen
	 * Landeslistenkandidaten übereinstimmt.
	 */
	@Test
	public void testeLandeslistengroesse() {
		ll.addKandidat(new Kandidat("Kandidat 1"), 1);
		ll.addKandidat(new Kandidat("Kandidat 2"), 2);
		ll.addKandidat(new Kandidat("Kandidat 3"), 3);
		assertEquals(
				"Die Landeslistengröße entspricht nicht der Zahl der gesetzten Kandidat.",
				3, ll.getListengroesse());
	}

	/**
	 * Prüft ob die Kandidaten, die über die Landesliste in den Bundestag
	 * einziehen korrekt wiedergegeben werden, einer der Kandidaten ein
	 * Direktmandat erringt.
	 */
	@Test
	public void testeLandeslistenmandateMitDirektmandaten() {
		Kandidat k1 = new Kandidat("Kandidat 1");
		// Lege Direktmandatsträger an.
		Wahlkreis wk = new Wahlkreis("Testwahlkreis", 1, bl);
		Kreiswahlvorschlag k2 = new Kreiswahlvorschlag("KWV 2", wk);
		wk.setGewinner(k2);
		Kandidat k3 = new Kandidat("Kandidat 3");
		ll.addKandidat(k1, 1);
		ll.addKandidat(k2, 2);
		ll.addKandidat(k3, 3);
		ll.addKandidat(new Kandidat("Kandidat 4"), 4);
		ll.setUnterverteilungSitzzahl(2);
		Iterator<Kandidat> mandateIt = ll.mandateIterator();
		assertEquals(
				"Der erste Landeslistenmandatsträger wird nicht korrekt ermittelt.",
				k1, mandateIt.next());
		assertEquals(
				"Der zweite Landeslistenmandatsträger wird nicht korrekt ermittelt.",
				k3, mandateIt.next());
		assertFalse("Die Landesliste liefert zu viele Mandatsträger.",
				mandateIt.hasNext());
	}

	/**
	 * Prüft ob die Kandidaten, die über die Landesliste in den Bundestag
	 * einziehen korrekt wiedergegeben werden, falls kein Kandidat ein
	 * Direktmandat erringt.
	 */
	@Test
	public void testeLandeslistenmandateOhneDirektmandate() {
		Kandidat k1 = new Kandidat("Kandidat 1");
		Kandidat k2 = new Kandidat("Kandidat 2");
		ll.addKandidat(k1, 1);
		ll.addKandidat(k2, 2);
		ll.addKandidat(new Kandidat("Kandidat 3"), 3);
		ll.setUnterverteilungSitzzahl(2);
		Iterator<Kandidat> mandateIt = ll.mandateIterator();
		assertEquals(
				"Der erste Landeslistenmandatsträger wird nicht korrekt ermittelt.",
				k1, mandateIt.next());
		assertEquals(
				"Der zweite Landeslistenmandatsträger wird nicht korrekt ermittelt.",
				k2, mandateIt.next());
		assertFalse("Die Landesliste liefert zu viele Mandatsträger.",
				mandateIt.hasNext());
	}

	/**
	 * Prüfe ob die Landesliste erkennt, zu welcher Partei sie gehört.
	 */
	@Test
	public void testeParteiZugehoerigkeit() {
		assertEquals("Die Landesliste liefert die falsche Partei.", partei,
				ll.getPartei());
	}

	/**
	 * Setzt einen Kandidaten auf einen ungültigen Landeslistenplatz.
	 */
	@Test
	public void testeUngueltigerLandeslistenplatz() {
		geworfeneException.expect(IllegalArgumentException.class);
		ll.addKandidat(new Kandidat("Testkandidat 1"), 0);
	}

	/**
	 * Prüft das setzten von Wahlkreiszweitstimmen über die Landesliste und das
	 * anschließende Abfragen der Zweitstimmen über die Landesliste und den
	 * Wahlkreis.
	 */
	@Test
	public void testeWahlkreiszweitstimmenSetzenUeberLL() {
		Wahlkreis wk = new Wahlkreis("Testwahlkreis", 0, bl);
		ll.setWahlkreiszweitstimmen(wk, 10);
		assertEquals(
				"Beim Abrufen über die Landesliste entspricht die Zahl der erhaltenen Wahlkreiszweitstimmennicht der Zahl der gesetzten Wahlkreiszweitstimmen.",
				10, ll.getWahlkreisZweitstimmen(wk));
		assertEquals(
				"Beim Abrufen über den Wahlkreis entspricht die Zahl der erhaltenen Wahlkreiszweitstimmennicht der Zahl der gesetzten Wahlkreiszweitstimmen.",
				10, wk.getParteiZweitstimmen(partei));
	}

	/**
	 * Prüft das setzten von Wahlkreiszweitstimmen über den Wahlkreis und das
	 * anschließende Abfragen der Zweitstimmen über die Landesliste und den
	 * Wahlkreis.
	 */
	@Test
	public void testeWahlkreiszweitstimmenSetzenUeberWahlkreis() {
		Wahlkreis wk = new Wahlkreis("Testwahlkreis", 0, bl);
		wk.setParteiZweitstimmen(partei, 10);
		assertEquals(
				"Beim Abrufen über die Landesliste entspricht die Zahl der erhaltenen Wahlkreiszweitstimmennicht der Zahl der gesetzten Wahlkreiszweitstimmen.",
				10, ll.getWahlkreisZweitstimmen(wk));
		assertEquals(
				"Beim Abrufen über den Wahlkreis entspricht die Zahl der erhaltenen Wahlkreiszweitstimmennicht der Zahl der gesetzten Wahlkreiszweitstimmen.",
				10, wk.getParteiZweitstimmen(partei));
	}

	/**
	 * Prüft das setzten von Wahlkreiszweitstimmen über den Wahlkreis und das
	 * anschließende Abfragen der Zweitstimmen über die Landesliste und den
	 * Wahlkreis.
	 */
	@Test
	public void testeZweitstimmenberechnung() {
		Wahlkreis wk1 = new Wahlkreis("Testwahlkreis 1", 1, bl);
		Wahlkreis wk2 = new Wahlkreis("Testwahlkreis 2", 2, bl);
		wk1.setParteiZweitstimmen(partei, 10);
		ll.setWahlkreiszweitstimmen(wk2, 5);
		assertEquals(
				"Die Summe der auf die Landesliste entfallenen Zweitstimmen entspricht nicht der Summe der gesetzten Zweitstimmen",
				15, ll.berechneZweitstimmen());
	}
}
