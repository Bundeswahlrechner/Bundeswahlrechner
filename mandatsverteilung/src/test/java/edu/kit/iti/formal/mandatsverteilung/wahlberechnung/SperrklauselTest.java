package edu.kit.iti.formal.mandatsverteilung.wahlberechnung;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundesland;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Kandidat;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Kreiswahlvorschlag;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Landesliste;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Partei;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Wahlkreis;

/**
 * Testfälle für den {@link BundeswahlgesetzSperrklauselpruefer}.
 * 
 * @author tillneuber
 * @version 1.0
 * 
 */
@RunWith(value = Parameterized.class)
public class SperrklauselTest {

	@Parameters(name = "Wahlverfahren: {0}")
	public static Iterable<Object[]> verfahren() {
		Object[][] verfahren = new Object[][] { { 0 }, { 1 } };
		return Arrays.asList(verfahren);
	}

	/**
	 * Die {@link Bundestagswahl}, die für den Test verwendet wird.
	 */
	private Bundestagswahl bt;

	/**
	 * Gibt den aktuellen Testdurchgang an - bei 0 wird
	 * {@link Bundeswahlgesetz2013} verwendet, bei 1
	 * {@link BundeswahlgesetzOhneAusgleichsmandate}.
	 */
	private final int durchgang;

	/**
	 * Enthält die {@link Partei}en, bei denen geprüft wird, ob sie die
	 * Sperrklausel überwinden.
	 */
	private Partei[] parteien;

	/**
	 * Das verwendete {@link WahlverfahrenMitSperrklausel}.
	 */
	private WahlverfahrenMitSperrklausel verfahren;

	public SperrklauselTest(int durchgang) {
		this.durchgang = durchgang;
		verfahren = null;
	}

	@Before
	public void setUp() throws Exception {
		// Lege eine neue Bundestagswahl für den Testfall an.
		bt = new Bundestagswahl();

		// Parteien anlegen
		parteien = new Partei[4];
		parteien[0] = new Partei("Partei a");
		parteien[1] = new Partei("Partei b");
		parteien[2] = new Partei("Partei c");
		parteien[2].setNationaleMinderheit(true);
		parteien[3] = new Partei("Partei d");
		for (int i = 0; i < parteien.length; i++) {
			bt.addPartei(parteien[i]);
		}

		// Bundesländer anlegen
		Bundesland[] bl = new Bundesland[2];
		bl[0] = new Bundesland("Bundesland 0", "BL0", 5000);
		bl[1] = new Bundesland("Bundesland 1", "BL1", 5000);
		for (int i = bl.length - 1; i >= 0; i--) {
			bt.addBundesland(bl[i]);
		}

		// Landeslisten anlegen
		for (int i = 0; i < parteien.length; i++) {
			for (int j = 0; j < bl.length; j++) {
				parteien[i].setLandesliste(bl[j], new Landesliste());
				// Lege einige Kandidaten an
				for (int k = 0; k < 10; k++) {
					parteien[i].getLandesliste(bl[j]).addKandidat(
							new Kandidat(Integer.toString(k)), k + 1);
				}
			}
		}

		// Wahlkreise anlegen
		Wahlkreis[] wahlkreise = new Wahlkreis[4];
		for (int i = 0; i < wahlkreise.length; i++) {
			wahlkreise[i] = new Wahlkreis(Integer.toString(i), i, bl[i / 2]);
			bl[i / 2].addWahlkreis(wahlkreise[i]);
			for (int j = 0; j < parteien.length; j++) {
				// Setze die Zweitstimmen: nur Partei a erhält genügend
				// Zweitstimmen zum Überwinden der 5%-Klausel
				if (j == 0) {
					wahlkreise[i].setParteiZweitstimmen(parteien[j], 90);
				} else {
					wahlkreise[i].setParteiZweitstimmen(parteien[j], 4);
				}
				// Kreiswahlvorschläge für jede Partei in Wahlkreis anlegen;
				// Partei b gewinnt alle Direktmandate
				Kreiswahlvorschlag kwv = new Kreiswahlvorschlag("KWV in WK: "
						+ i + " - Partei " + j, wahlkreise[i]);
				kwv.setPartei(parteien[j]);
				if (j == 1) {
					kwv.setStimmen(10);
				} else {
					kwv.setStimmen(5);
				}
			}
		}
		if (durchgang == 0) {
			verfahren = new Bundeswahlgesetz2013(bt, new SainteLagueIterativ());
		} else {
			verfahren = new BundeswahlgesetzOhneAusgleichsmandate(bt,
					new SainteLagueIterativ());
		}

	}

	/**
	 * Prüft, ob die zurückgegebene Höhe der Grundmandatsklausel mit der
	 * gesetzten Höhe übereinstimmt.
	 */
	@Test
	public void testeGesetzteGrundmandate() {
		BundeswahlgesetzSperrklauselpruefer spk = new BundeswahlgesetzSperrklauselpruefer(
				0.95, 10);
		assertEquals(
				"Die zurückgegebene Höhe der Grundmandatsklausel stimmt nicht mit der Gesetzten überein.",
				10, spk.getGrundmandate());
	}

	/**
	 * Prüft, ob die zurückgegebene Höhe der Prozenthürde mit der gesetzten Höhe
	 * übereinstimmt.
	 */
	@Test
	public void testeGesetzteProzenthuerde() {
		BundeswahlgesetzSperrklauselpruefer spk = new BundeswahlgesetzSperrklauselpruefer(
				0.95, 10);
		assertEquals(
				"Die zurückgegebene Höhe der Prozenthürde stimmt nicht mit der Gesetzten überein.",
				0.95, spk.getProzenthuerde(), 0.00001);
	}

	/**
	 * Prüft, ob das WahlverfahrenMitSperrklausel einen gesetzten
	 * Sperrklauselprüfer korrekt zurückgibt.
	 */
	@Test
	public void testeSperrklauselprueferRueckgabe() {
		BundeswahlgesetzSperrklauselpruefer spk = new BundeswahlgesetzSperrklauselpruefer(
				0.95, 10);
		spk.setGesamteZweitstimmenzahl(bt.berechneZweitstimmen());
		verfahren.setzeSperrklauselpruefer(spk);
		assertEquals(
				"Das Wahlverfahren gibt einen falschen Sperrklauselprüfer zurück.",
				spk, verfahren.getSperrklauselpruefer());
	}

	/**
	 * Testet den {@link BundeswahlgesetzSperrklauselpruefer}, wenn die
	 * Gesamtzweitstimmenzahl nicht gesetzt wurde.
	 */
	@Test
	public void testeSperrklauselprueferWennGesamteZweitstimmenzahlNichtGesetztWurde() {
		boolean exceptionGeworfen = false;
		BundeswahlgesetzSperrklauselpruefer spk = new BundeswahlgesetzSperrklauselpruefer(
				0.95, 10);
		verfahren.setzeSperrklauselpruefer(spk);
		try {
			verfahren.berechneWahlergebnis();
		} catch (IllegalStateException e) {
			exceptionGeworfen = true;
		}
		assertTrue(
				"Es wurde keine Excption geworfen, obwohl die Gesamtzweitstimmenzahl im Sperrklauselprüfer nicht gesetzt wurde.",
				exceptionGeworfen);
	}

	/**
	 * Testet den {@link BundeswahlgesetzSperrklauselpruefer} mit 4 Parteien,
	 * von denen jeweils eine
	 * <ul>
	 * <li>die 5%-Hürde überwindet
	 * <li>3 Grundmandate erringt,
	 * <li>eine nationale Minderheit repräsentiert
	 * <li>keines der für den Einzug in den Bundestag nötigen Kritierien erfüllt
	 * </ul>
	 * <p>
	 * Beachte: Die Parteien erfüllen jeweils nur ein Kriterium, Kombinationen
	 * werden nicht überprüft.
	 */
	@Test
	public void testeStandardSperrklauselfaelle() {
		BundeswahlgesetzSperrklauselpruefer spk = new BundeswahlgesetzSperrklauselpruefer();
		spk.setGesamteZweitstimmenzahl(bt.berechneZweitstimmen());
		verfahren.setzeSperrklauselpruefer(spk);
		verfahren.berechneWahlergebnis();
		assertEquals(
				"Partei a hat die Sperrklausel nicht überwunden, erhält aber mehr als 5% der Gesamtstimmen.",
				true, parteien[0].isSperrklauselUeberwunden());
		assertEquals(
				"Partei b hat die Sperrklausel nicht überwunden, erhält aber mehr als 3 Direktmandate.",
				true, parteien[1].isSperrklauselUeberwunden());
		assertEquals(
				"Partei c hat die Sperrklausel nicht überwunden, ist aber nationale Minderheit.",
				true, parteien[2].isSperrklauselUeberwunden());
		assertEquals(
				"Partei d hat die Sperrklausel überwunden, erfüllt aber keines des dafür nötigen Kriterien.",
				false, parteien[3].isSperrklauselUeberwunden());
	}

	/**
	 * Testet den {@link BundeswahlgesetzSperrklauselpruefer} mit veränderten
	 * Hürden. Von den 4 Parteien des
	 * {@link Bundeswahlgesetz2013SperrklauselTest#testeStandardSperrklauselfaelle()}
	 * -Tests scheitern jetzt 3 an der Sperrklausel.
	 */
	@Test
	public void testeStandardSperrklauselfaelleBeiVeraendertenHuerden() {
		BundeswahlgesetzSperrklauselpruefer spk = new BundeswahlgesetzSperrklauselpruefer(
				0.95, 10);
		spk.setGesamteZweitstimmenzahl(bt.berechneZweitstimmen());
		verfahren.setzeSperrklauselpruefer(spk);
		verfahren.berechneWahlergebnis();
		assertEquals(
				"Partei a hat die Sperrklausel überwunden, erhält aber weniger als 95% der Gesamtstimmen.",
				false, parteien[0].isSperrklauselUeberwunden());
		assertEquals(
				"Partei b hat die Sperrklausel überwunden, erhält aber weniger als 10 Direktmandate.",
				false, parteien[1].isSperrklauselUeberwunden());
		assertEquals(
				"Partei c hat die Sperrklausel nicht überwunden, ist aber nationale Minderheit.",
				true, parteien[2].isSperrklauselUeberwunden());
		assertEquals(
				"Partei d hat die Sperrklausel überwunden, erfüllt aber keines des dafür nötigen Kriterien.",
				false, parteien[3].isSperrklauselUeberwunden());
	}

	/**
	 * Testet den {@link BundeswahlgesetzSperrklauselpruefer} mit veränderten
	 * Hürden nachdem er gecloned wurde.
	 */
	@Test
	public void testeStandardSperrklauselfaelleMitBeiVeraendertenHuerdenNachClone() {
		BundeswahlgesetzSperrklauselpruefer spk = new BundeswahlgesetzSperrklauselpruefer(
				0.95, 10);
		spk.setGesamteZweitstimmenzahl(bt.berechneZweitstimmen());
		BundeswahlgesetzSperrklauselpruefer copy = spk.clone();
		verfahren.setzeSperrklauselpruefer(copy);
		verfahren.berechneWahlergebnis();
		assertEquals(
				"Partei a hat die Sperrklausel überwunden, erhält aber weniger als 95% der Gesamtstimmen.",
				false, parteien[0].isSperrklauselUeberwunden());
		assertEquals(
				"Partei b hat die Sperrklausel überwunden, erhält aber weniger als 10 Direktmandate.",
				false, parteien[1].isSperrklauselUeberwunden());
		assertEquals(
				"Partei c hat die Sperrklausel nicht überwunden, ist aber nationale Minderheit.",
				true, parteien[2].isSperrklauselUeberwunden());
		assertEquals(
				"Partei d hat die Sperrklausel überwunden, erfüllt aber keines des dafür nötigen Kriterien.",
				false, parteien[3].isSperrklauselUeberwunden());
	}

	/**
	 * Testet den {@link BundeswahlgesetzSperrklauselpruefer} mit veränderten
	 * Hürden nachdem das Wahlverfahren zudem er gehört geclonet wurde.
	 */
	@Test
	public void testeStandardSperrklauselfaelleMitBeiVeraendertenHuerdenNachWahlverfahrenClone() {
		BundeswahlgesetzSperrklauselpruefer spk = new BundeswahlgesetzSperrklauselpruefer(
				0.95, 10);
		spk.setGesamteZweitstimmenzahl(bt.berechneZweitstimmen());
		verfahren.setzeSperrklauselpruefer(spk);
		WahlverfahrenMitSperrklausel copy = (WahlverfahrenMitSperrklausel) verfahren
				.clone();
		copy.berechneWahlergebnis();
		assertEquals(
				"Partei a hat die Sperrklausel überwunden, erhält aber weniger als 95% der Gesamtstimmen.",
				false, parteien[0].isSperrklauselUeberwunden());
		assertEquals(
				"Partei b hat die Sperrklausel überwunden, erhält aber weniger als 10 Direktmandate.",
				false, parteien[1].isSperrklauselUeberwunden());
		assertEquals(
				"Partei c hat die Sperrklausel nicht überwunden, ist aber nationale Minderheit.",
				true, parteien[2].isSperrklauselUeberwunden());
		assertEquals(
				"Partei d hat die Sperrklausel überwunden, erfüllt aber keines des dafür nötigen Kriterien.",
				false, parteien[3].isSperrklauselUeberwunden());
	}
}
