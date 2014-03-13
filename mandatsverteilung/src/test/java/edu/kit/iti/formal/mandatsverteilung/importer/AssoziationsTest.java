package edu.kit.iti.formal.mandatsverteilung.importer;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Kreiswahlvorschlag;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Landesliste;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Partei;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Wahlkreis;
import edu.kit.iti.formal.mandatsverteilung.generierer.Einschraenkung;
import edu.kit.iti.formal.mandatsverteilung.generierer.MandatszahlRelativ;
import edu.kit.iti.formal.mandatsverteilung.generierer.RandomisierterGenerierer;
import edu.kit.iti.formal.mandatsverteilung.wahlberechnung.Wahlverfahren;

public class AssoziationsTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * Testet auf verschiedene Arten beschaffte Bundestagswahlen darauf, ob alle
     * Assoziationen gesetzt sind.
     */
    @Test
    public void test() throws IOException {
        ArrayList<Bundestagswahl> zuTesten = new ArrayList<>();
        zuTesten.add(TestDatenBereitsteller.getStandardBTW());
        for (int i = 0; i < 5; i++) {
            zuTesten.add(zuTesten.get(i).clone());
        }

        RandomisierterGenerierer rg = new RandomisierterGenerierer(
                zuTesten.get(5));
        rg.addEinschraenkung(new MandatszahlRelativ(20, 5, "SPD"));
        zuTesten.add(rg.generiere());

        // ... und weitere BTWs
        for (int i = 0; i < zuTesten.size(); i++) {
            Wahlverfahren curVerf = zuTesten.get(i).getWahlverfahren();
            curVerf.berechneWahlergebnis();
        }
        for (Bundestagswahl b : zuTesten) {
            System.out.println("Nächste Wahl:");
            this.testeBTW(b);
        }
        Bundestagswahl generiert = zuTesten.get(zuTesten.size() - 1);
        Einschraenkung ei = new MandatszahlRelativ(20, 5, "SPD");
        try {
            Class<?> c = ei.getClass();
            Class<?> params[] = new Class[1];
            params[0] = Bundestagswahl.class;
            Method dist = c.getDeclaredMethod("ueberpruefeErgebnis", params);
            dist.setAccessible(true);
            int result = (int) dist.invoke(ei, generiert);
            assertTrue("Generierung war falsch/zu schlecht!", result == 0);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void testeBTW(Bundestagswahl b) {
        Assert.assertTrue("Die BTW hat keine 16 Bundesländer!",
                b.bundeslaenderAnzahl() == 16);
        Assert.assertTrue("Die BTW hat keine 299 Wahlkreise!",
                b.wahlkreisAnzahl() == 299);
        int counter = 0;
        Iterator<Wahlkreis> iw = b.wahlkreisIterator();
        Wahlkreis cur;
        while (iw.hasNext()) {
            cur = iw.next();
            counter++;
            Partei curPartei;
            boolean zweitstimmenFunktionieren = false;
            Iterator<Partei> ip = b.parteienIterator();
            while (ip.hasNext()) {
                curPartei = ip.next();
                int stimmen;
                Landesliste parteiLL = curPartei.getLandesliste(cur
                        .getBundesland());
                if (parteiLL != null) {
                    stimmen = parteiLL.getWahlkreisZweitstimmen(cur);
                } else {
                    stimmen = 0;
                }
                Assert.assertTrue("Negative Partei-Zweitstimmen", stimmen >= 0);
                if (cur.getParteiZweitstimmen(curPartei) > 0) {
                    zweitstimmenFunktionieren = true;
                }
                /*
                 * 
                 * System.out.println("Die Partei " + curPartei.getName() +
                 * 
                 * " hat im Wahlkreis Nr. " + cur.getNummer() + " " + stimmen +
                 * 
                 * " Stimmen geholt");
                 */
            }
            Assert.assertTrue(
                    "Wahlkreiszweitstimmen nicht gesetzt im Wahlkreis "
                            + cur.getNummer(), zweitstimmenFunktionieren);
            Assert.assertTrue("Ein Wahlkreis ist null!", cur != null);
            Assert.assertTrue("getWahlkreis funktioniert nicht richtig!",
                    cur == b.getWahlkreis(cur.getNummer()));
            List<Kreiswahlvorschlag> bewerber = cur.getWahlkreisBewerber();
            Assert.assertTrue(bewerber.size() >= 4);
            Assert.assertTrue(cur.getBundesland() == b.getBundeslandAbk(cur
                    .getBundesland().getAbkuerzung()));
            boolean erststimmenFunktionieren = false;
            for (Kreiswahlvorschlag k : bewerber) {
                if (k.getStimmen() > 0) {
                    erststimmenFunktionieren = true;
                }
                Assert.assertTrue(
                        "Ein Kreiswahlvorschlag im Wahlkreis ist null!",
                        k != null);
                Landesliste ll = k.getLandesliste();
                if (ll != null) {
                    Assert.assertTrue(k.getLandeslistenplatz() > 0);
                    Assert.assertTrue(k.getLandeslistenplatz() < 300);
                    // System.out.println(ll.getPartei().toString()
                    // + " im Wahlkreis Nr. " + cur.getNummer());
                    /*
                     * 
                     * Assert.assertTrue(
                     * 
                     * "Eine Partei hat laut Landesliste in einem Wahlkreis keine Zweitstimmen erhalten! (könnte passieren)"
                     * 
                     * , ll.getWahlkreisZweitstimmen(cur) > 0);
                     */
                    /*
                     * 
                     * if (ll.getWahlkreisZweitstimmen(cur) == 0) {
                     * System.out.println("Partei: " + ll.getPartei().getName()
                     * 
                     * + ", Wahlkreis: " + cur.getName() + ", " +
                     * 
                     * cur.getNummer()); }
                     */
                    Assert.assertTrue(ll.getPartei() != null);
                    /*
                     * * System.out.println(ll.getPartei().getName() +
                     * " (Landesliste) vs. " + k.getPartei().getName() +
                     * " (Partei)");
                     */
                    Assert.assertTrue(
                            "Ein KWV hat eine andere Partei auf der Landesliste als er selbst (theoretisch möglich)",
                            ll.getPartei().getName()
                                    .equals(k.getPartei().getName()));

                }
            }
            Assert.assertTrue("Die Erststimmen aller " + bewerber.size()
                    + " KWVs im Wahlkreis Nr. " + cur.getNummer() + " sind 0!",
                    erststimmenFunktionieren);
        }
        Assert.assertTrue(
                "Der WahlkreisIterator iteriert nicht über 299 Wahlkreise (sondern mehr/weniger)!",
                counter == 299);
        System.out.println("Läuft durch");
    }
}
