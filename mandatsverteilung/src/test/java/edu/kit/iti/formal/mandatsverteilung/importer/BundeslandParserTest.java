package edu.kit.iti.formal.mandatsverteilung.importer;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BundeslandParserTest {
    BundeslaenderParser cbp;
    InputStream ist;

    @Test
    public void printBundeslaender() throws Exception {
        cbp.parseBundeslaenderDatei(ist, TestDatenBereitsteller.b);
        String[] abk = { "SH", "BW", "BY", "HH", "HB", "NI", "NW", "HE", "RP",
                "SL", "BE", "BB", "MV", "SN", "ST", "TH" };
        for (String s : abk) {
            System.out.println(TestDatenBereitsteller.b.getBundeslandAbk(s)
                    .toString());
        }
    }

    @Before
    public void setUp() throws Exception {
        InputStream in = KandidatenParserTest.class
                .getResourceAsStream("/bundesL.csv");
        ist = in;
        ParserFabrik pf = new CSVParserFabrik();
        cbp = pf.erzeugeBundeslaenderParser();
    }

    @After
    public void tearDown() throws Exception {
        ist = null;
        cbp = null;
        TestDatenBereitsteller.reinitialisiere();
    }

    @Test(expected = IOException.class)
    public void testeFehlerhafteDatei1() throws IOException {
        InputStream in = KandidatenParserTest.class
                .getResourceAsStream("/bundesL1.csv");
        TestDatenBereitsteller.in1 = in;
        ist = TestDatenBereitsteller.in1;
        try {
            cbp.parseBundeslaenderDatei(ist, TestDatenBereitsteller.b);
        } catch (IOException e) {
            assertNotNull(
                    "Die Message der IOException ist null statt Fehler in Zelle + ImportFehler",
                    e.getMessage());
            throw new IOException("funktioniert");
        }
    }

    @Test(expected = IOException.class)
    public void testeFehlerhafteDatei2() throws IOException {
        InputStream in = KandidatenParserTest.class
                .getResourceAsStream("/bundesL2.csv");
        TestDatenBereitsteller.in1 = in;
        ist = TestDatenBereitsteller.in1;
        try {
            cbp.parseBundeslaenderDatei(ist, TestDatenBereitsteller.b);
        } catch (IOException e) {
            assertNotNull(
                    "Die Message der IOException ist null statt Fehler in Zelle + ImportFehler",
                    e.getMessage());
            throw new IOException("funktioniert");
        }
    }

    @Test(expected = IOException.class)
    public void testeFehlerhafteDatei3() throws IOException {
        InputStream in = KandidatenParserTest.class
                .getResourceAsStream("/bundesL3.csv");
        TestDatenBereitsteller.in1 = in;
        ist = TestDatenBereitsteller.in1;
        try {
            cbp.parseBundeslaenderDatei(ist, TestDatenBereitsteller.b);
        } catch (IOException e) {
            assertNotNull(
                    "Die Message der IOException ist null statt Fehler in Zelle + ImportFehler",
                    e.getMessage());
            throw new IOException("funktioniert");
        }
    }
}
