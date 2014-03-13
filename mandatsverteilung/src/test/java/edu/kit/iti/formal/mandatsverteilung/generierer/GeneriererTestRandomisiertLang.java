package edu.kit.iti.formal.mandatsverteilung.generierer;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Partei;
import edu.kit.iti.formal.mandatsverteilung.importer.TestDatenBereitsteller;

public class GeneriererTestRandomisiertLang {

    private String verbotenePartei = null;

    private Einschraenkung getRandomEinschraenkung(Bundestagswahl referenz) {
        Random ran = new Random();
        Einschraenkung result;
        double rand = ran.nextDouble();
        if (rand < 0.33) {
            int refSitzzahl = referenz.getSitzzahl();
            double bias = ran.nextDouble() * 0.5;
            bias -= 0.25;
            // bias zwischen 0.25 und -0.25
            int wert = (int) (refSitzzahl * (1 + bias));
            if (wert < referenz.getSitzzahl()) {
                wert = referenz.getSitzzahl();
            }
            int abweichung = (int) (ran.nextDouble() * 0.15 * refSitzzahl);
            System.out.println("SitzzahlEinschraenkung erstellt: Wert = "
                    + wert + ", Abweichung = " + abweichung);
            result = new SitzzahlEinschraenkung(wert, abweichung);
        } else if (rand < 0.67) {
            int parteienZahl = referenz.parteienAnzahl();
            Iterator<Partei> it = referenz.parteienIterator();
            int schritte = ran.nextInt(parteienZahl - 1);
            Partei p = null;
            for (int i = 0; i <= schritte; i++) {
                p = it.next();
            }
            // Endlosschleife bei 1 Partei
            while (p.getName().equals(verbotenePartei)
                    || p.getOberverteilungSitzzahl() == 0) {
                it = referenz.parteienIterator();
                schritte = ran.nextInt(parteienZahl - 1);
                for (int i = 0; i <= schritte; i++) {
                    p = it.next();
                }
            }
            verbotenePartei = p.getName();
            // Absolut
            int refSitzzahl = p.getOberverteilungSitzzahl();
            double bias = ran.nextDouble() * 0.5;
            bias -= 0.25;
            int wert = (int) (refSitzzahl * (1 + bias));
            int abweichung = (int) (ran.nextDouble() * 0.75 * refSitzzahl);
            String name = p.getName();
            System.out.println("Absolute Einschraenkung erstellt: " + name
                    + ", Wert = " + wert + ", Abweichung = " + abweichung);
            result = new MandatszahlAbsolut(wert, abweichung, name);

        } else {
            // Relativ
            int parteienZahl = referenz.parteienAnzahl();
            Iterator<Partei> it = referenz.parteienIterator();
            int schritte = ran.nextInt(parteienZahl - 1);
            Partei p = null;
            for (int i = 0; i <= schritte; i++) {
                p = it.next();
            }
            // Endlosschleife bei 1 Partei
            while (p.getName().equals(verbotenePartei)
                    || p.getOberverteilungSitzzahl() == 0) {
                it = referenz.parteienIterator();
                schritte = ran.nextInt(parteienZahl - 1);
                for (int i = 0; i <= schritte; i++) {
                    p = it.next();
                }
            }
            verbotenePartei = p.getName();
            double prozentual = (double) p.getOberverteilungSitzzahl()
                    / referenz.getSitzzahl();
            double bias = ran.nextDouble() * 0.5;
            bias -= 0.25;
            int wert = (int) (prozentual * (1 + bias));
            int abweichung = (int) (ran.nextDouble() * 0.15 * prozentual);
            String name = p.getName();
            System.out.println("Relative Einschraenkung erstellt: " + name
                    + ", Wert = " + wert + ", Abweichung = " + abweichung);
            result = new MandatszahlRelativ(wert, abweichung, name);
        }
        return result;
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testeGenerierungRandomisiert() throws Exception {
        Random ran = new Random();
        Bundestagswahl referenz = TestDatenBereitsteller.getStandardBTW();
        referenz.getWahlverfahren().berechneWahlergebnis();
        for (int i = 0; i < 100; i++) {
            long curTime = System.currentTimeMillis();
            RandomisierterGenerierer cur = new RandomisierterGenerierer(
                    referenz);
            int zweiConstr = ran.nextInt(5);
            Einschraenkung e1 = this.getRandomEinschraenkung(referenz);
            cur.addEinschraenkung(e1);
            Einschraenkung e2 = null;
            if (0 == zweiConstr) {
                e2 = this.getRandomEinschraenkung(referenz);
                cur.addEinschraenkung(e2);
            }
            try {
                cur.generiere();
            } catch (NoSuchElementException e) {
                System.out.println("Generierung fehlgeschlagen");
            } catch (IllegalArgumentException e) {
                System.out.println("Generierung fehlgeschlagen");
            }
            long nextTime = System.currentTimeMillis();
            System.out.println("Generierung Nr " + i
                    + " abgeschlossen. Dauer: " + (nextTime - curTime));

        }

    }

}
