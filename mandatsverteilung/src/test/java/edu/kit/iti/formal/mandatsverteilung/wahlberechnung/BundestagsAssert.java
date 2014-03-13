package edu.kit.iti.formal.mandatsverteilung.wahlberechnung;

import static org.junit.Assert.assertEquals;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;

public class BundestagsAssert {

	public static void assertUnterverteilung(int expected, Bundestagswahl bt,
			String partei, String bl) {
		assertEquals("Die Unterverteilungssitzzahl der " + partei + " in " + bl
				+ " stimmt nicht mit dem erwarteten Ergebnis überein.",
				expected,
				bt.getPartei(partei).getLandesliste(bt.getBundeslandAbk(bl))
						.getUnterverteilungSitzzahl());

	}

	private BundestagsAssert() {

	}
}
