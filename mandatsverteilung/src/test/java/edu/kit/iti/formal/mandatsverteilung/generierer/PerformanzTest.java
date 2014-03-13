package edu.kit.iti.formal.mandatsverteilung.generierer;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.importer.TestDatenBereitsteller;

public class PerformanzTest {
    private final int fall = 0;
    private int gesamt = 0;
    private int success = 0;

    public void testeBasisfall() throws Exception {
        Bundestagswahl ausgangsWahl = TestDatenBereitsteller.getStandardBTW();
        RandomisierterGenerierer rg = new RandomisierterGenerierer(ausgangsWahl);
        rg.addEinschraenkung(new SitzzahlEinschraenkung(600, 300));
        Bundestagswahl neu = rg.generiere();
        assertTrue("Der Basisfall beim Generierer funktioniert nicht!",
                neu.getSitzzahl() <= 900 && neu.getSitzzahl() >= 300);
    }

    @Test
    public void testePerformanz() throws Exception {
        int size = 781;
        while (size < 1000) {
            for (int i = 0; i < 100; i++) {
                this.testFall(size);
            }
            System.out.println("Größe: " + size + ", Erfolge: " + success
                    + " Gesamt: " + gesamt + " Prozent: "
                    + (((double) success / (double) gesamt) * 100) + "%");
            size += 10;
            gesamt = 0;
            success = 0;
        }

    }

    public void testFall(int size) throws IOException {
        Bundestagswahl ausgangsWahl = TestDatenBereitsteller.getStandardBTW();
        RandomisierterGenerierer rg = new RandomisierterGenerierer(ausgangsWahl);
        switch (fall) {
        case 0:
            rg.addEinschraenkung(new SitzzahlEinschraenkung(size, 5));
            break;
        case 1:
            rg.addEinschraenkung(new MandatszahlAbsolut((193 + size - 781), 5,
                    "SPD"));
        case 2:
            rg.addEinschraenkung(new MandatszahlRelativ(
                    (30 + ((size - 781) / 10)), 5, "CDU"));
        default:
            assertTrue(false);
            break;
        }
        try {
            Bundestagswahl neu = rg.generiere();
            success++;
            if (!(neu.getSitzzahl() >= size - 5 && neu.getSitzzahl() <= size + 5)) {
                System.out.println("FAIL");
            }
        } catch (Exception e) {
            System.out.println("EXCEPTION");
        }
        gesamt++;
    }
}