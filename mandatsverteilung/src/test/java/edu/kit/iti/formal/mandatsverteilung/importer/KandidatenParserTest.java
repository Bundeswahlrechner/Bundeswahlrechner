package edu.kit.iti.formal.mandatsverteilung.importer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundesland;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Kandidat;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Kreiswahlvorschlag;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Landesliste;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Partei;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Wahlkreis;

public class KandidatenParserTest {

	@Before
	public void setUp() throws Exception {
		TestDatenBereitsteller.b = new Bundestagswahl();
		this.setzeDateien("/Tab23_Wahlbewerber_a.csv");
	}

	private void setzeDateien(String s) throws IOException {
		InputStream in = KandidatenParserTest.class
				.getResourceAsStream("/kerg.csv");
		TestDatenBereitsteller.in1 = in;
		in = KandidatenParserTest.class.getResourceAsStream("/bundesL.csv");
		TestDatenBereitsteller.in2 = in;
		in = KandidatenParserTest.class.getResourceAsStream(s);
		TestDatenBereitsteller.in3 = in;
		TestDatenBereitsteller.cbp.parseBundeslaenderDatei(
				TestDatenBereitsteller.in2, TestDatenBereitsteller.b);
		TestDatenBereitsteller.cwp.setzeDaten(TestDatenBereitsteller.in1,
				TestDatenBereitsteller.b);
		TestDatenBereitsteller.cwp.parseWahlkreise();
		TestDatenBereitsteller.ckp.parseKandidatenDatei(
				TestDatenBereitsteller.in3, TestDatenBereitsteller.b);
		TestDatenBereitsteller.in1.close();
		TestDatenBereitsteller.in2.close();
		TestDatenBereitsteller.in3.close();

	}

	@After
	public void tearDown() throws Exception {
		TestDatenBereitsteller.reinitialisiere();
	}

	@Test
	public void testeAutomatischerKandidatenParserBasisfall() throws Exception {
		WahldatenImporter importer = TestDatenBereitsteller
				.getStandardImporter();
		importer.setFabrik(new CSVParserFabrikMitAutomatischenKandidaten());
		importer.importieren();
		importer = TestDatenBereitsteller.getStandardImporter();
		importer.setFabrik(new CSVParserFabrikMitAutomatischenKandidaten(160));
		importer.importieren();
	}

	@Test(expected = IOException.class)
	public void testeFehlerhafteDatei1() throws IOException {
		try {
			this.setzeDateien("/Tab23_Wahlbewerber_a1.csv");
		} catch (IOException e) {
			Assert.assertNotNull("Die Message der IOException ist null!",
					e.getMessage());
			throw new IOException("funktioniert");
		}
	}

	@Test(expected = IOException.class)
	public void testeFehlerhafteDatei2() throws IOException {
		try {
			this.setzeDateien("/Tab23_Wahlbewerber_a2.csv");
		} catch (IOException e) {
			Assert.assertNotNull("Die Message der IOException ist null!",
					e.getMessage());
			throw new IOException("funktioniert");
		}
	}

	@Test(expected = IOException.class)
	public void testeFehlerhafteDatei3() throws IOException {
		try {
			this.setzeDateien("/Tab23_Wahlbewerber_a3.csv");
		} catch (IOException e) {
			Assert.assertNotNull("Die Message der IOException ist null!",
					e.getMessage());
			throw new IOException("funktioniert");
		}
	}

	/**
	 * In dieser Datei wurde ein Bundesland für die Landesliste angegeben, aber
	 * keine Nummer auf der Landesliste. (Erwünschtes Verhalten: Ignoriere
	 * diesen Eintrag dann)
	 * 
	 * @throws IOException
	 */
	@Test
	public void testeFehlerhafteDatei4() throws IOException {
		this.setzeDateien("/Tab23_Wahlbewerber_a4.csv");
	}

	/**
	 * In dieser Datei wurde ein Buchstabe als Landeslistennummer angegeben
	 * 
	 * @throws IOException
	 */
	@Test(expected = IOException.class)
	public void testeFehlerhafteDatei5() throws IOException {
		try {
			this.setzeDateien("/Tab23_Wahlbewerber_a5.csv");
		} catch (IOException e) {
			Assert.assertNotNull("Die Message der IOException ist null!",
					e.getMessage());
			throw new IOException("funktioniert");
		}
	}

	/**
	 * In dieser Datei wurde als Wahlkreisnummer 4009 angegeben.
	 * 
	 * @throws IOException
	 */
	@Test(expected = IOException.class)
	public void testeFehlerhafteDatei6() throws IOException {
		try {
			this.setzeDateien("/Tab23_Wahlbewerber_a6.csv");
		} catch (IOException e) {
			Assert.assertNotNull("Die Message der IOException ist null!",
					e.getMessage());
			throw new IOException("funktioniert");
		}
	}

	@Test
	public void testKandidatenErstellung() throws Exception {
		Bundestagswahl toTest = TestDatenBereitsteller.getStandardBTW();
		toTest.getWahlverfahren().berechneWahlergebnis();
		Iterator<Bundesland> ib = toTest.bundeslaenderIterator();
		Bundesland cur;
		while (ib.hasNext()) {
			cur = ib.next();
			Iterator<Wahlkreis> iw = cur.wahlkreisIterator();
			Wahlkreis w;
			while (iw.hasNext()) {
				w = iw.next();
				for (Kreiswahlvorschlag k : w.getWahlkreisBewerber()) {
					System.out.println(k.toString());
				}
			}
		}
		Iterator<Partei> ip = toTest.parteienIterator();
		Partei p;
		while (ip.hasNext()) {
			p = ip.next();
			ib = toTest.bundeslaenderIterator();
			while (ib.hasNext()) {
				cur = ib.next();
				Landesliste ll = p.getLandesliste(cur);
				if (ll != null) {
					Iterator<Kandidat> ik = ll.mandateIterator();
					Kandidat k;
					while (ik.hasNext()) {
						k = ik.next();
						System.out.println(k.toString());
					}
				}
			}
		}
	}

}
