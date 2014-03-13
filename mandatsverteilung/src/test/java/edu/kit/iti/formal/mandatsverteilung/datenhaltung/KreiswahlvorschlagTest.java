package edu.kit.iti.formal.mandatsverteilung.datenhaltung;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class KreiswahlvorschlagTest {

	/**
	 * Der {@link Kreiswahlvorschlag}, der getestet wird.
	 */
	private Kreiswahlvorschlag kwv;

	/**
	 * Der {@link Wahlkreis}, in dem der getestete {@link Kreiswahlvorschlag}
	 * antritt.
	 */
	private Wahlkreis wk;

	@Before
	public void setUp() {
		wk = new Wahlkreis("WK", 1);
		kwv = new Kreiswahlvorschlag("KWV", wk);
	}

	/**
	 * Prüft, ob die Landesliste bei einem parteilosen Bewerber null ist.
	 */
	@Test
	public void testeGetLandeslisteAlsParteiloserKandidat() {
		assertEquals(
				"Der Kreiswahlvorschlag tritt auf einer Landesliste an, obwohl er als parteiloser Bewerber angelegt wurde.",
				null, kwv.getLandesliste());
	}

	@Test
	public void testeGetParteiAlsParteiloserKandidat() {
		assertEquals(
				"Der Kreiswahlvorschlag gehört einer Partei an, obwohl er als parteiloser Bewerber angelegt wurde.",
				null, kwv.getPartei());
	}

	/**
	 * Prüft, ob der Kreiswahlvorschlag sich als Wahlkreisgewinner erkennt, wenn
	 * er im Wahlkreis als Gewinner gesetzt wurde.
	 */
	@Test
	public void testeKWVAlsWahlkreisgewinner() {
		wk.setGewinner(kwv);
		assertTrue(
				"Der KWV ist kein Wahlkreisgewinner, obwohl er als solcher gesetzt wurde.",
				kwv.istWahlkreisGewinner());
	}

	/**
	 * Prüft die Verteilung von Erststimmen auf den Kreiswahlvorschlag.
	 */
	@Test
	public void testeStimmenvergabe() {
		kwv.setStimmen(10);
		assertEquals(
				"Die erhaltenen Erststimmen des KWV entsprechen nicht den gesetzten.",
				10, kwv.getStimmen());
	}

}
