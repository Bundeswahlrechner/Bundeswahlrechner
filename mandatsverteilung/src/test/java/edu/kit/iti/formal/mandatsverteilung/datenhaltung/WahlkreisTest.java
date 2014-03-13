package edu.kit.iti.formal.mandatsverteilung.datenhaltung;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WahlkreisTest {

	/**
	 * Der {@link Wahlkreis}, der getestet wird.
	 */
	private Wahlkreis wk;

	@Before
	public void setUp() {
		wk = new Wahlkreis("WK 1", 1);
	}

	@After
	public void tearDown() {
		wk = null;
	}

	/**
	 * Vergleicht den Wahlkreis mit {@code null}.
	 */
	@Test
	public void testeCompareMitNull() {
		assertEquals(
				"Ungültiges Ergebnis beim Vergleich eines Wahlkreises mit null.",
				0, wk.compareTo(null));
	}

	/**
	 * Setzt null als Wahlkreisbewerber.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testeNullWahlkreisbewerber() {
		wk.addKWV(null);
	}

	/**
	 * Setzt null als Wahlkreisgewinner.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testeNullWahlkreisgewinner() {
		// Fail bedeutet: Es wurde keine Exception geworfen, obwohl null als
		// Wahlkreisgewinner gesetzt wurde.
		wk.setGewinner(null);
	}

	/**
	 * Setzt einen Wahlkreisgewinner, der nicht als Bewerber in diesem Wahlkreis
	 * antritt.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testeUngueltigenWahlkreisgewinner() {
		// Fail bedeutet: Ein KWV der in einem Wahlkreis nicht als Bewerber
		// gelistet ist kann diesen nicht gewinnen.
		wk.setGewinner(new Kreiswahlvorschlag("KWV", new Wahlkreis("WK 2", 2)));
	}

	/**
	 * Überprüft die mit einem Wahlkreisgewinner assozierten Funktionen.
	 */
	@Test
	public void testeWahlkreisgewinner() {
		Kreiswahlvorschlag kwv = new Kreiswahlvorschlag("KWV", wk);
		wk.addKWV(kwv);
		wk.setGewinner(kwv);
		assertEquals("Der Wahlkreis liefert einen falschen Wahlkreisgewinner.",
				kwv, wk.getGewinner());
	}

	/**
	 * Überprüft den Wahlkreisnamen.
	 */
	@Test
	public void testeWahlkreisname() {
		assertEquals("Der Wahlkreis liefert einen falschen Wahlkreisnamen.",
				"WK 1", wk.getName());
	}

	/**
	 * Überprüft die Wahlkreisnummer.
	 */
	@Test
	public void testeWahlkreisnummer() {
		assertEquals("Der Wahlkreis liefert eine falsche Wahlkreisnummer.", 1,
				wk.getNummer());
	}

	@Test
	public void testeZweitstimmenvergabe() {
		// Lege eine Testpartei mit einer Landesliste an; setze das Bundesland
		// in dem die Landesliste liegt als Bundesland des Wahlkreises.
		Partei partei = new Partei("Test");
		Bundesland bl = new Bundesland("Test", "Test", 1);
		wk.setBundesland(bl);
		partei.setLandesliste(bl, new Landesliste());
		wk.setParteiZweitstimmen(partei, 1);
		assertEquals(
				"Die erhaltenen Zweitstimmen stimmen nicht mit den gesetzten überein.",
				1, wk.getParteiZweitstimmen(partei));
	}

	/**
	 * Verteilt Zweitstimmen auf eine Partei, die keine Landesliste hat.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testeZweitstimmenvergabeAufParteiOhneLandesliste() {
		wk.setParteiZweitstimmen(new Partei("Test"), 1);
	}

}
