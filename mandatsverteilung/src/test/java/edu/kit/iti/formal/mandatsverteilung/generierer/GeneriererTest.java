package edu.kit.iti.formal.mandatsverteilung.generierer;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.importer.TestDatenBereitsteller;

public class GeneriererTest {

	@Test
	public void testeBasisfall() throws Exception {
		Bundestagswahl ausgangsWahl = TestDatenBereitsteller.getStandardBTW();
		RandomisierterGenerierer rg = new RandomisierterGenerierer(ausgangsWahl);
		rg.addEinschraenkung(new SitzzahlEinschraenkung(600, 300));
		Bundestagswahl neu = rg.generiere();
		assertTrue("Der Basisfall beim Generierer funktioniert nicht!",
				neu.getSitzzahl() <= 900 && neu.getSitzzahl() >= 300);
	}

	@Test(timeout = 100000)
	public void testeBasisGenerierung() throws IOException {
		Bundestagswahl ausgangsWahl = TestDatenBereitsteller.getStandardBTW();
		RandomisierterGenerierer rg = new RandomisierterGenerierer(ausgangsWahl);
		rg.addEinschraenkung(new MandatszahlAbsolut(165, 20, "SPD"));
		List<String> p = new ArrayList<>();
		p.add("SPD");
		Bundestagswahl neu = rg.generiere(p);
		neu.getAbgeordnete();
	}

}
