package edu.kit.iti.formal.mandatsverteilung.generierer;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.importer.TestDatenBereitsteller;

public class PerformanzTestZwei {
    public void testeBasisfall() throws Exception {
        Bundestagswahl ausgangsWahl = TestDatenBereitsteller.getStandardBTW();
        RandomisierterGenerierer rg = new RandomisierterGenerierer(ausgangsWahl);
        rg.addEinschraenkung(new SitzzahlEinschraenkung(600, 300));
        Bundestagswahl neu = rg.generiere();
        assertTrue("Der Basisfall beim Generierer funktioniert nicht!",
                neu.getSitzzahl() <= 900 && neu.getSitzzahl() >= 300);
    }

    @Test
    public void testeBasisGenerierung() throws IOException {
        for (int size = 641; size < 900; size = size + 25) {
            int gesamt = 0;
            int success = 0;
            for (int i = 0; i < 100; i++) {
                Bundestagswahl ausgangsWahl = TestDatenBereitsteller
                        .getStandardBTW();
                RandomisierterGenerierer rg = new RandomisierterGenerierer(
                        ausgangsWahl);
                rg.addEinschraenkung(new SitzzahlEinschraenkung(size, 5));
                rg.addEinschraenkung(new MandatszahlAbsolut(255, 5, "CDU"));
                rg.addEinschraenkung(new MandatszahlRelativ(20, 3, "SPD"));
                try {
                    Bundestagswahl neu = rg.generiere();
                    success++;
                    if (!(neu.getSitzzahl() >= size - 5 && neu.getSitzzahl() <= size + 5)) {
                        System.out.println("FAIL");
                    }
                    if (Math.abs(neu.getPartei("CDU")
                            .getOberverteilungSitzzahl() - 255) > 5) {
                        System.out.println("Partei-FAIL");
                    }
                    if (Math.abs(((double) neu.getPartei("SPD")
                            .getOberverteilungSitzzahl() / (double) neu
                            .getSitzzahl()) - 20.0) > 3) {
                        System.out.println("Partei2-FAIL");
                    }
                } catch (Exception e) {
                    System.out.println("EXCEPTION");
                }
                gesamt++;
            }
            System.out.println("Erhöhung: " + size + " Erfolge: " + success
                    + " Gesamt: " + gesamt + " Prozent: "
                    + (((double) success / (double) gesamt) * 100) + "%");
        }
    }
}
