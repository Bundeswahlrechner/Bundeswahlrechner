package edu.kit.iti.formal.mandatsverteilung.importer;

import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.wahlberechnung.BerechnungsfabrikMitSperrklauselpruefer;
import edu.kit.iti.formal.mandatsverteilung.wahlberechnung.Bundeswahlgesetz2013Fabrik;

public class WahlkreiseParserTest {

	private Bundestagswahl toTest;

	@Before
	public void setUp() throws Exception {
		BundeslandParserTest bt = new BundeslandParserTest();
		bt.setUp();
		InputStream in = KandidatenParserTest.class
				.getResourceAsStream("/kerg.csv");
		TestDatenBereitsteller.in1 = in;
		CSVWahlkreiseUndStimmenParser cwp = new CSVWahlkreiseUndStimmenParser();
		cwp.setzeDaten(TestDatenBereitsteller.in1, TestDatenBereitsteller.b);
	}

	private void setzeDateienUndBerechne(String s) throws Exception {
		InputStream in = KandidatenParserTest.class.getResourceAsStream(s);
		InputStream kerg = in;
		in = KandidatenParserTest.class.getResourceAsStream("/bundesL.csv");
		InputStream bundesl = in;
		in = KandidatenParserTest.class
				.getResourceAsStream("/Tab23_Wahlbewerber_a.csv");
		InputStream bewerber = in;
		Bundestagswahl result = new Bundestagswahl();
		BundeslaenderParser bp = new CSVBundeslaenderParser();
		bp.parseBundeslaenderDatei(bundesl, result);
		CSVWahlkreiseUndStimmenParser wp = new CSVWahlkreiseUndStimmenParser();
		wp.setzeDaten(kerg, result);
		wp.parseWahlkreise();
		KandidatenParser kp = new CSVKandidatenParser();
		kp.parseKandidatenDatei(bewerber, result);
		kerg = KandidatenParserTest.class.getResourceAsStream(s);
		wp.reinitialisiere(kerg);
		wp.parseStimmen();
		BerechnungsfabrikMitSperrklauselpruefer f = new Bundeswahlgesetz2013Fabrik();
		f.erstelleBerechnungsverbund(result);
		toTest = result;
	}

	@After
	public void tearDown() throws Exception {
		TestDatenBereitsteller.reinitialisiere();
		toTest = null;
	}

	/**
	 * In dieser Datei wurde die Farbe der NPD auf weiß gesetzt.
	 * 
	 * @throws Exception
	 *             falls etwas schiefläuft. Manchmal wirft dieser Test eine
	 *             ArrayIndexOutOfBoundsException?!
	 */
	@Test
	public void testeFarbenDecoding() throws Exception {
		System.out.println("Datei 4:");
		this.setzeDateienUndBerechne("/kerg4.csv");
		assertTrue(Color.white.equals(toTest.getPartei("NPD").getFarbe()));

	}

	/**
	 * In dieser Datei wurden die Erststimmen der Linken in einer Zelle durch
	 * 'a' ersetzt.
	 * 
	 * @throws Exception
	 *             Im Erfolgsfall eine IOException.
	 */
	@Test(expected = IOException.class)
	public void testeFehlerhafteDatei1() throws Exception {
		System.out.println("Fehlerhafte Datei 1:");
		try {
			this.setzeDateienUndBerechne("/kerg1.csv");
		} catch (IOException e) {
			Assert.assertNotNull("Die Message der IOException ist null!",
					e.getMessage());
			System.out.println("Exception abgefangen: " + e.getMessage());
			throw new IOException("funktioniert");
		}
	}

	/**
	 * In dieser Datei wurde eine Wahlkreisnummer durch 's' ersetzt.
	 * 
	 * @throws Exception
	 *             Im Erfolgsfall eine IOException.
	 */
	@Test(expected = IOException.class)
	public void testeFehlerhafteDatei2() throws Exception {
		System.out.println("Fehlerhafte Datei 2:");
		try {
			this.setzeDateienUndBerechne("/kerg2.csv");
		} catch (IOException e) {
			Assert.assertNotNull("Die Message der IOException ist null!",
					e.getMessage());
			System.out.println("Exception abgefangen: " + e.getMessage());
			throw new IOException("funktioniert");
		}
	}

	/**
	 * In der 3. Fehlerhaften Datei wurde eine Zeile kürzer als die gehört
	 * zu-Zelle gemacht (Anhand dieser Zelle unterscheidet der Parser, um welche
	 * Art Zeile (Wahlkreis-/Bundesland-/...) es sich handelt.
	 * 
	 * @throws Exception
	 *             wenn der Parser korrekt arbeitet, eine IOException, sonst
	 *             schlägt der Test fehl
	 */
	@Test(expected = IOException.class)
	public void testeFehlerhafteDatei3() throws Exception {
		System.out.println("Fehlerhafte Datei 3:");
		try {
			this.setzeDateienUndBerechne("/kerg3.csv");
		} catch (IOException e) {
			Assert.assertNotNull("Die Message der Exception ist null!",
					e.getMessage());
			System.out.println("Exception abgefangen: " + e.getMessage());
			throw new IOException("funktioniert");
		}
	}

	/**
	 * In der 5. Fehlerhaften Datei wurde in das 'gehört zu:' Feld ein Buchstabe
	 * eingetragen.
	 * 
	 * @throws Exception
	 *             wenn der Parser korrekt arbeitet, eine IOException, sonst
	 *             schlägt der Test fehl
	 */
	@Test(expected = IOException.class)
	public void testeFehlerhafteDatei5() throws Exception {
		System.out.println("Fehlerhafte Datei 5:");
		try {
			this.setzeDateienUndBerechne("/kerg5.csv");
		} catch (IOException e) {
			Assert.assertNotNull("Die Message der Exception ist null!",
					e.getMessage());
			System.out.println("Exception abgefangen: " + e.getMessage());
			throw new IOException("funktioniert");
		}
	}

	/**
	 * In der 6. Fehlerhaften Datei taucht ein Bundesland auf, dass es nicht in
	 * der Bundesländerdatei gab.
	 * 
	 * @throws Exception
	 *             wenn der Parser korrekt arbeitet, eine IOException, sonst
	 *             schlägt der Test fehl
	 */
	@Test(expected = IOException.class)
	public void testeFehlerhafteDatei6() throws Exception {
		System.out.println("Fehlerhafte Datei 6:");
		try {
			this.setzeDateienUndBerechne("/kerg6.csv");
		} catch (IOException e) {
			Assert.assertNotNull("Die Message der Exception ist null!",
					e.getMessage());
			System.out.println("Exception abgefangen: " + e.getMessage());
			throw new IOException("funktioniert");
		}
	}

	/**
	 * In der 7. Fehlerhaften Datei taucht eine Partei auf, die es nicht in der
	 * Kandidatendatei gab.
	 * 
	 * @throws Exception
	 *             wenn der Parser korrekt arbeitet, eine IOException, sonst
	 *             schlägt der Test fehl
	 */
	@Test(expected = IOException.class)
	public void testeFehlerhafteDatei7() throws Exception {
		System.out.println("Fehlerhafte Datei 7:");
		try {
			this.setzeDateienUndBerechne("/kerg7.csv");
		} catch (IOException e) {
			Assert.assertNotNull("Die Message der Exception ist null!",
					e.getMessage());
			System.out.println("Exception abgefangen: " + e.getMessage());
			throw new IOException("funktioniert");
		}
	}

	/**
	 * In der 8. Fehlerhaften Datei wurden die Erststimmen der unabhängigen
	 * Bewerber durch Buchstaben ersetzt.
	 * 
	 * @throws Exception
	 *             wenn der Parser korrekt arbeitet, eine IOException, sonst
	 *             schlägt der Test fehl
	 */
	@Test(expected = IOException.class)
	public void testeFehlerhafteDatei8() throws Exception {
		System.out.println("Fehlerhafte Datei 8:");
		try {
			this.setzeDateienUndBerechne("/kerg8.csv");
		} catch (IOException e) {
			Assert.assertNotNull("Die Message der Exception ist null!",
					e.getMessage());
			System.out.println("Exception abgefangen: " + e.getMessage());
			throw new IOException("funktioniert");
		}
	}

	/**
	 * In dieser Datei ist für die NPD ein Farben-String vorhanden, dieser ist
	 * jedoch Fehlerhaft.
	 * 
	 * @throws Exception
	 *             Wenn das Farben-Decoding nicht funktioniert
	 */
	@Test
	public void testeFehlerhaftesFarbenDecoding() throws Exception {
		System.out.println("Datei 9:");
		this.setzeDateienUndBerechne("/kerg9.csv");
	}
}
