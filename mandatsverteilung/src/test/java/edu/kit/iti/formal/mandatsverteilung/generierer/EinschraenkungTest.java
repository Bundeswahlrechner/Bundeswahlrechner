package edu.kit.iti.formal.mandatsverteilung.generierer;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;

public class EinschraenkungTest extends Einschraenkung {

    private Einschraenkung[] con;
    private Einschraenkung testCon;

    @Before
    public void setUp() {
        this.con = new Einschraenkung[3];
        this.con[0] = new MandatszahlAbsolut(5, 10, "Testpartei");
        this.testCon = this.con[0];
    }

    @After
    public void tearDown() {
        this.con = null;
    }

    @Test
    public void testMinDist() {
        int numberOfCases = 5;
        int[] values1 = { -10, -5, 0, 100, 1000 };
        int[] values2 = { -10, 50, -30, 4, 1 };
        int[] plusminuses = { 1, 2, 5, 1000, 3 };
        int[] expected = { 0, 53, 25, 0, 996 };
        int result[] = new int[numberOfCases];
        for (int i = 0; i < numberOfCases; i++) {
            result[i] = this.testCon.minDistance(values1[i], values2[i],
                    plusminuses[i]);
            assertEquals(expected[i], result[i]);
        }
    }

    @Override
    int ueberpruefeErgebnis(Bundestagswahl b) {
        return 0;
    }

}
