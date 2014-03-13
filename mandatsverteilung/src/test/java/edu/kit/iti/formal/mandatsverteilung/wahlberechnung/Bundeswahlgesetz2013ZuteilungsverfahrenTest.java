package edu.kit.iti.formal.mandatsverteilung.wahlberechnung;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.importer.TestDatenBereitsteller;

@RunWith(value = Parameterized.class)
public class Bundeswahlgesetz2013ZuteilungsverfahrenTest {

	@Parameters(name = "Verfahren: {0}")
	public static Iterable<Object[]> verfahren() {
		Object[][] verfahren = new Object[][] {
				{ new SainteLagueHoechstzahl() },
				{ new SainteLagueRangmasszahl() },
				{ new SainteLagueIterativ() }, { new DHondt() },
				{ new HareNiemeyer() } };
		return Arrays.asList(verfahren);
	}

	/** Die {@link Bundestagswahl} auf der die Berechnung ausgeführt wird. */
	private Bundestagswahl bt;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	/** Das getestete {@link Wahlverfahren}. */
	private Bundeswahlgesetz2013 verfahren;

	/** Das für den Test verwendete {@link Zuteilungsverfahren}. */
	private Zuteilungsverfahren zt;

	public Bundeswahlgesetz2013ZuteilungsverfahrenTest(Zuteilungsverfahren zt) {
		this.zt = zt;
	}

	@Before
	public void setUp() throws Exception {
		bt = TestDatenBereitsteller.getStandardBTW();
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
	 * Prüft die Berechnungsperformance mit verschiedenen Zuteilungsverfahren.
	 */
	@Test(timeout = 500)
	public void testeBerechnungsperformance() {
		if (zt instanceof HareNiemeyer) {
			thrown.expect(IllegalArgumentException.class);
			// Test mit HareNiemeyer - das Alamaba-Paradoxon verhindert hier
			// eine Wahlberechnung.
			verfahren.berechneWahlergebnis();
		} else {
			// Zuteilungsverfahren, mit dem sich die Daten berechnen lassen.
			verfahren.berechneWahlergebnis();
			assertTrue(true);

		}
	}
}
