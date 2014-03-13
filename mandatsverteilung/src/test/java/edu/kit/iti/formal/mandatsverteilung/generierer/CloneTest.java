package edu.kit.iti.formal.mandatsverteilung.generierer;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.importer.TestDatenBereitsteller;
import edu.kit.iti.formal.mandatsverteilung.wahlberechnung.Bundeswahlgesetz2013;
import edu.kit.iti.formal.mandatsverteilung.wahlberechnung.BundeswahlgesetzSperrklauselpruefer;
import edu.kit.iti.formal.mandatsverteilung.wahlberechnung.SainteLagueHoechstzahl;
import edu.kit.iti.formal.mandatsverteilung.wahlberechnung.Zuteilungsverfahren;

public class CloneTest {
    private Bundestagswahl btw;
    private Bundeswahlgesetz2013 verfahren;
    private Bundeswahlgesetz2013 verfahrenKlon;
    private Zuteilungsverfahren zt;
    private Zuteilungsverfahren ztKlon;

    @Before
    public void setUp() throws Exception {
        btw = TestDatenBereitsteller.getStandardBTW();
        zt = new SainteLagueHoechstzahl();
        ztKlon = new SainteLagueHoechstzahl();
        verfahren = new Bundeswahlgesetz2013(btw, zt);
        BundeswahlgesetzSperrklauselpruefer spk = new BundeswahlgesetzSperrklauselpruefer();
        spk.setGesamteZweitstimmenzahl(btw.berechneZweitstimmen());
        verfahren.setzeSperrklauselpruefer(spk);
    }

    @After
    public void tearDown() throws Exception {
        btw = null;
        verfahren = null;
        zt = null;
    }

    @Test(timeout = 5000)
    public void testClone() {
        Bundestagswahl klon = btw.clone();
        verfahrenKlon = new Bundeswahlgesetz2013(klon, ztKlon);
        BundeswahlgesetzSperrklauselpruefer spk = new BundeswahlgesetzSperrklauselpruefer();
        spk.setGesamteZweitstimmenzahl(klon.berechneZweitstimmen());
        verfahrenKlon.setzeSperrklauselpruefer(spk);
        verfahrenKlon.berechneWahlergebnis();
    }

    @Test
    public void testeLinkedList() throws Exception {
        Bundestagswahl b = TestDatenBereitsteller.getStandardBTW();
        int anzahlWahlen = 100;
        Bundestagswahl bs[] = new Bundestagswahl[anzahlWahlen];
        bs[0] = b.clone();
        for (int i = 1; i < anzahlWahlen; i++) {
            bs[i] = bs[i - 1].clone();
        }
        JFrame j = new JFrame("Test1");
        j.setVisible(true);
        JDialog f = new JDialog(j, "Test", true);
        f.setVisible(true);
    }

}
