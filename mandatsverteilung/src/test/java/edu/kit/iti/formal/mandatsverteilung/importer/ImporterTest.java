package edu.kit.iti.formal.mandatsverteilung.importer;

import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Kreiswahlvorschlag;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Partei;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Wahlkreis;

public class ImporterTest {
    private Bundestagswahl btw;

    @Test
    public void gibStimmenBeispielAus() {
        Partei first = btw.getPartei("SPD");
        Iterator<Wahlkreis> iw = btw.wahlkreisIterator();
        Wahlkreis cur;
        while (iw.hasNext()) {
            cur = iw.next();
            System.out.println("Die SPD hat im Wahlkreis " + cur.getNummer()
                    + " " + cur.getParteiZweitstimmen(first)
                    + " Zweitstimmen errungen");
            for (Kreiswahlvorschlag k : cur.getWahlkreisBewerber()) {
                if (k.getPartei() != null) {
                    if (k.getPartei().getName().equals("SPD")) {
                        System.out
                                .println("Der Kandidat der SPD hat im Wahlkreis "
                                        + cur.getNummer()
                                        + " "
                                        + k.getStimmen()
                                        + " Erststimmen errungen");
                        break;
                    }
                }
            }
        }
    }

    @Before
    public void setUp() throws Exception {
        btw = TestDatenBereitsteller.getStandardBTW();
    }

    @After
    public void tearDown() throws Exception {
        btw = null;
    }

    @Test
    public void testeCSVImporter() throws Exception {
        WahldatenImporter importer = TestDatenBereitsteller
                .getStandardImporter();
        assertTrue("Falsche Parserfabrik im CSVWahldatenImporter",
                importer.getFabrik() instanceof CSVParserFabrik);
        importer.importieren();
    }
}
