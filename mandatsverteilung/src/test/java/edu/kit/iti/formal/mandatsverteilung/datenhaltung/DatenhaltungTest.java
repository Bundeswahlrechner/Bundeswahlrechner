package edu.kit.iti.formal.mandatsverteilung.datenhaltung;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DatenhaltungTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private Bundestagswahl bundestagswahl;
	private Kreiswahlvorschlag einzigerWahlkreisgewinnerDerPartei1InLand1;
	private Bundesland land1;
	private Bundesland land2;
	private Landesliste landeslistePartei1Land1;
	private Landesliste landeslistePartei1Land2;
	private Landesliste landeslistePartei2Land1;
	private Partei partei1;
	private Partei partei2;
	private Wahlkreis wahlkreis11;
	private Wahlkreis wahlkreis12;
	private Wahlkreis wahlkreis21;
	private Wahlkreis wahlkreis22;

	@Before
	public void setUp() throws Exception {
		// Erzeuge Bundestagswahl
		bundestagswahl = new Bundestagswahl();

		// Füge Bundesländer hinzu
		land1 = new Bundesland("Land 1", "L1", 1000);
		land2 = new Bundesland("Land 2", "L2", 2000);
		bundestagswahl.addBundesland(land1);
		bundestagswahl.addBundesland(land2);

		// Füge Parteien hinzu
		partei1 = new Partei("Partei 1");
		partei2 = new Partei("Partei 2");
		bundestagswahl.addPartei(partei1);
		bundestagswahl.addPartei(partei2);

		// Füge Wahlkreise hinzu
		wahlkreis11 = new Wahlkreis("Wahlkreis 1.1", 11);
		wahlkreis12 = new Wahlkreis("Wahlkreis 1.2", 12);
		land1.addWahlkreis(wahlkreis11);
		land1.addWahlkreis(wahlkreis12);
		wahlkreis21 = new Wahlkreis("Wahlkreis 2.1", 21);
		wahlkreis22 = new Wahlkreis("Wahlkreis 2.2", 22);
		land2.addWahlkreis(wahlkreis21);
		land2.addWahlkreis(wahlkreis22);

		// Füge Landeslisten hinzu
		landeslistePartei1Land1 = new Landesliste();
		partei1.setLandesliste(land1, landeslistePartei1Land1);
		landeslistePartei1Land2 = new Landesliste();
		partei1.setLandesliste(land2, landeslistePartei1Land2);
		landeslistePartei2Land1 = new Landesliste();
		partei2.setLandesliste(land1, landeslistePartei2Land1);

		// Setze Wahlkreiszweitstimmen
		landeslistePartei1Land1.setWahlkreiszweitstimmen(wahlkreis11, 110);
		landeslistePartei1Land1.setWahlkreiszweitstimmen(wahlkreis12, 120);
		landeslistePartei1Land2.setWahlkreiszweitstimmen(wahlkreis21, 210);
		landeslistePartei1Land2.setWahlkreiszweitstimmen(wahlkreis22, 220);
		landeslistePartei2Land1.setWahlkreiszweitstimmen(wahlkreis11, 110);
		landeslistePartei2Land1.setWahlkreiszweitstimmen(wahlkreis12, 120);

		// Erzeuge Direktkandidaten
		Kreiswahlvorschlag kreiswahlvorschlag111 = new Kreiswahlvorschlag(
				"Kreiswahlvorschlag 1 in Wahlkreis 1 in Bundesland 1",
				wahlkreis11);
		@SuppressWarnings("unused")
		Kreiswahlvorschlag kreiswahlvorschlag112 = new Kreiswahlvorschlag(
				"Kreiswahlvorschlag 2 in Wahlkreis 1 in Bundesland 1",
				wahlkreis11);
		@SuppressWarnings("unused")
		Kreiswahlvorschlag kreiswahlvorschlag121 = new Kreiswahlvorschlag(
				"Kreiswahlvorschlag 1 in Wahlkreis 2 in Bundesland 1",
				wahlkreis12);
		Kreiswahlvorschlag kreiswahlvorschlag122 = new Kreiswahlvorschlag(
				"Kreiswahlvorschlag 2 in Wahlkreis 2 in Bundesland 1",
				wahlkreis12);
		@SuppressWarnings("unused")
		Kreiswahlvorschlag kreiswahlvorschlag211 = new Kreiswahlvorschlag(
				"Kreiswahlvorschlag 1 in Wahlkreis 1 in Bundesland 2",
				wahlkreis21);
		@SuppressWarnings("unused")
		Kreiswahlvorschlag kreiswahlvorschlag221 = new Kreiswahlvorschlag(
				"Kreiswahlvorschlag 1 in Wahlkreis 2 in Bundesland 2",
				wahlkreis22);

		// Setze Wahlkreisgewinner in Land 1, nicht in Land 2
		wahlkreis11.setGewinner(kreiswahlvorschlag111);
		wahlkreis12.setGewinner(kreiswahlvorschlag122);

		// Der Gewinner von Wahlkreis11 tritt für die Partei1 an.
		// Der Gewinner von Wahlkreis12 tritt als unabhängiger an.
		kreiswahlvorschlag111.setPartei(partei1);
		einzigerWahlkreisgewinnerDerPartei1InLand1 = kreiswahlvorschlag111;

		// Partei Oberverteilungssitzzahl für Parteivergleich
		partei1.setOberverteilungSitzzahl(3000);
		partei2.setOberverteilungSitzzahl(300);
	}

	@After
	public void tearDown() throws Exception {
		bundestagswahl = null;
		land1 = null;
		land2 = null;
		partei1 = null;
		partei2 = null;
		wahlkreis11 = null;
		wahlkreis12 = null;
		wahlkreis21 = null;
		wahlkreis22 = null;
		landeslistePartei1Land1 = null;
		landeslistePartei1Land2 = null;
		landeslistePartei2Land1 = null;
		einzigerWahlkreisgewinnerDerPartei1InLand1 = null;
	}

	@Test
	public void testeAnzahlWahlkreise() throws Exception {
		assertEquals(2, land1.anzahlWahlkreise());
		assertEquals(2, land2.anzahlWahlkreise());
	}

	@Test
	public void testeBerechneZweitstimmen() throws Exception {
		assertEquals(890, bundestagswahl.berechneZweitstimmen());
	}

	@Test
	public void testeBundeslaenderAnzahl() throws Exception {
		assertEquals(2, bundestagswahl.bundeslaenderAnzahl());
	}

	@Test
	public void testeBundeslandDoppeltHinzufuegen() throws Exception {
		// Füge "Land 2" ein weiteres mal hinzu
		assertFalse(bundestagswahl.addBundesland(land2));
	}

	@Test
	public void testeBundeslandMitGleichemName() throws Exception {
		// Erzeuge ein weiteres Land mit anderer Abkürzung aber gleichem Namen
		// wie "Land 1"
		Bundesland land = new Bundesland("Land 1", "LX", 3000);
		assertFalse(bundestagswahl.addBundesland(land));
	}

	@Test
	public void testeBundeslandMitGleichemNameUndAbkuerzung() throws Exception {
		// Erzeuge ein weiteres Land mit gleicher Abkürzung und gleichem Namen
		// wie "Land 1"
		Bundesland land = new Bundesland("Land 1", "L1", 3000);
		assertFalse(bundestagswahl.addBundesland(land));
	}

	@Test
	public void testeBundeslandMitGleicherAbkuerzung() throws Exception {
		// Erzeuge ein weiteres Land mit anderem Namen aber gleicher Abkürzung
		// wie "Land 2"
		Bundesland land = new Bundesland("Weiteres Land", "L2", 3000);
		assertFalse(bundestagswahl.addBundesland(land));
	}

	@Test
	public void testeBundeslandVergleich() throws Exception {
		assertTrue(land1.compareTo(null) == 0);
		assertTrue(land1.compareTo(land2) < 0);
		assertTrue(land1.compareTo(land1) == 0);
		assertTrue(land2.compareTo(land1) > 0);
	}

	@Test
	public void testeEingezogeneParteien() throws Exception {
		assertEquals(2, bundestagswahl.eingezogeneParteien().size());
	}

	@Test
	public void testeGetAbgeordnete() throws Exception {
		assertEquals(2, bundestagswahl.getAbgeordnete().size());
		;
	}

	@Test
	public void testeGetBundeslandAbk() throws Exception {
		assertEquals(land1, bundestagswahl.getBundeslandAbk("L1"));
		assertEquals(land2, bundestagswahl.getBundeslandAbk("L2"));
	}

	@Test
	public void testeGetBundeslandName() throws Exception {
		assertEquals(land1, bundestagswahl.getBundeslandName("Land 1"));
		assertEquals(land2, bundestagswahl.getBundeslandName("Land 2"));
	}

	@Test
	public void testeGetPartei() throws Exception {
		assertEquals(partei1, bundestagswahl.getPartei("Partei 1"));
		assertEquals(partei2, bundestagswahl.getPartei("Partei 2"));
	}

	@Test
	public void testeGetWahlkreiszweitstimmen() throws Exception {
		assertEquals(110,
				landeslistePartei1Land1.getWahlkreisZweitstimmen(wahlkreis11));
		assertEquals(120,
				landeslistePartei1Land1.getWahlkreisZweitstimmen(wahlkreis12));
		assertEquals(210,
				landeslistePartei1Land2.getWahlkreisZweitstimmen(wahlkreis21));
		assertEquals(220,
				landeslistePartei1Land2.getWahlkreisZweitstimmen(wahlkreis22));
		assertEquals(110,
				landeslistePartei2Land1.getWahlkreisZweitstimmen(wahlkreis11));
		assertEquals(120,
				landeslistePartei2Land1.getWahlkreisZweitstimmen(wahlkreis12));
	}

	@Test
	public void testeGetWahlkreiszweitstimmenAndereWahlkreise()
			throws Exception {
		assertEquals(0,
				landeslistePartei1Land1.getWahlkreisZweitstimmen(wahlkreis21));
		assertEquals(0,
				landeslistePartei1Land1.getWahlkreisZweitstimmen(wahlkreis22));
		assertEquals(0,
				landeslistePartei1Land2.getWahlkreisZweitstimmen(wahlkreis11));
		assertEquals(0,
				landeslistePartei1Land2.getWahlkreisZweitstimmen(wahlkreis12));
		assertEquals(0,
				landeslistePartei2Land1.getWahlkreisZweitstimmen(wahlkreis21));
		assertEquals(0,
				landeslistePartei2Land1.getWahlkreisZweitstimmen(wahlkreis22));
	}

	@Test
	public void testeKonstruktor() throws Exception {
		assertNotNull("Bundestagswahl wurde nicht erzeugt", bundestagswahl);
	}

	@Test
	public void testeLandeslisteGetBundesland() throws Exception {
		assertEquals(land1, landeslistePartei1Land1.getBundesland());
		assertEquals(land2, landeslistePartei1Land2.getBundesland());
		assertEquals(land1, landeslistePartei2Land1.getBundesland());
	}

	@Test
	public void testeLandeslisteGetPartei() throws Exception {
		assertEquals(partei1, landeslistePartei1Land1.getPartei());
		assertEquals(partei1, landeslistePartei1Land2.getPartei());
		assertEquals(partei2, landeslistePartei2Land1.getPartei());
	}

	@Test
	public void testeLandeslisteGetZuteilungswert() throws Exception {
		assertEquals(230, landeslistePartei1Land1.getZuteilungswert());
		assertEquals(430, landeslistePartei1Land2.getZuteilungswert());
		assertEquals(230, landeslistePartei2Land1.getZuteilungswert());
	}

	@Test(expected = NoSuchElementException.class)
	public void testeLandeslistenMandateIteratorNextException()
			throws Exception {
		Iterator<Kandidat> it = landeslistePartei1Land1.mandateIterator();
		it.next(); // sollte Exception werfen
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testeLandeslistenMandateIteratorRemove() throws Exception {
		Iterator<Kandidat> it = landeslistePartei1Land1.mandateIterator();
		it.remove(); // sollte Exception werfen
	}

	@Test
	public void testeParteienAnzahl() throws Exception {
		assertEquals(2, bundestagswahl.parteienAnzahl());
	}

	@Test
	public void testeParteiGetZuteilungswert() throws Exception {
		assertEquals(660, partei1.getZuteilungswert());
		assertEquals(230, partei2.getZuteilungswert());
	}

	@Test
	public void testeParteiVergleich() throws Exception {
		assertTrue(partei1.compareTo(null) == 0);
		assertTrue(partei1.compareTo(partei2) < 0);
		assertTrue(partei1.compareTo(partei1) == 0);
		assertTrue(partei2.compareTo(partei1) > 0);
	}

	@Test
	public void testeWahlkreisgewinnerIterator() throws Exception {
		Iterator<Kreiswahlvorschlag> it = partei1
				.wahlkreisgewinnerIterator(land1);
		assertTrue(it.hasNext());
		assertEquals(einzigerWahlkreisgewinnerDerPartei1InLand1, it.next());
		assertFalse(it.hasNext());
	}

	@Test(expected = NoSuchElementException.class)
	public void testeWahlkreisgewinnerIteratorNextException() throws Exception {
		Iterator<Kreiswahlvorschlag> it = partei1
				.wahlkreisgewinnerIterator(land1);
		assertTrue(it.hasNext());
		it.next();
		assertFalse(it.hasNext());
		it.next(); // sollte Exception werfen
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testeWahlkreisgewinnerIteratorRemove() throws Exception {
		Iterator<Kreiswahlvorschlag> it = partei1
				.wahlkreisgewinnerIterator(land1);
		it.remove(); // sollte Exception werfen
	}

	@Test(expected = IllegalStateException.class)
	public void testeWahlkreisgewinnerIteratorStateException() throws Exception {
		// Iterator Konstruktor sollte Exception werfen, da Gewinner in Land 2
		// nicht gesetzt wurden
		Iterator<Kreiswahlvorschlag> it = partei1
				.wahlkreisgewinnerIterator(land2);
		it.next();
	}

	@Test
	public void testeWahlkreisIterator() throws Exception {
		Iterator<Wahlkreis> it = land1.wahlkreisIterator();
		assertTrue(it.hasNext());
		it.next();
		assertTrue(it.hasNext());
		it.next();
		assertFalse(it.hasNext());
	}

	@Test(expected = NoSuchElementException.class)
	public void testeWahlkreisIteratorNextException() throws Exception {
		Iterator<Wahlkreis> it = land1.wahlkreisIterator();
		assertTrue(it.hasNext());
		it.next();
		assertTrue(it.hasNext());
		it.next();
		assertFalse(it.hasNext());
		it.next(); // sollte Exception werfen
	}

	@Test
	public void testeWahlkreisVergleich() throws Exception {
		assertTrue(wahlkreis22.compareTo(null) == 0);
		assertTrue(wahlkreis11.compareTo(wahlkreis12) < 0);
		assertTrue(wahlkreis22.compareTo(wahlkreis22) == 0);
		assertTrue(wahlkreis21.compareTo(wahlkreis12) > 0);
	}

	@Test
	public void testeWeiteresBundesland() throws Exception {
		Bundesland land3 = new Bundesland("Land 3", "L3", 3000);
		assertTrue(bundestagswahl.addBundesland(land3));
	}
}
