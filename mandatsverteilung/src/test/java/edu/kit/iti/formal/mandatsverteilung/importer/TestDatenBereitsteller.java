package edu.kit.iti.formal.mandatsverteilung.importer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.wahlberechnung.Bundeswahlgesetz2013;
import edu.kit.iti.formal.mandatsverteilung.wahlberechnung.BundeswahlgesetzSperrklauselpruefer;
import edu.kit.iti.formal.mandatsverteilung.wahlberechnung.SainteLagueIterativ;
import edu.kit.iti.formal.mandatsverteilung.wahlberechnung.WahlverfahrenMitSperrklausel;
import edu.kit.iti.formal.mandatsverteilung.wahlberechnung.Zuteilungsverfahren;

public class TestDatenBereitsteller {
    static Bundestagswahl b = new Bundestagswahl();
    static CSVBundeslaenderParser cbp = new CSVBundeslaenderParser();
    static CSVKandidatenParser ckp = new CSVKandidatenParser();
    static CSVWahlkreiseUndStimmenParser cwp = new CSVWahlkreiseUndStimmenParser();
    static File f1;
    static File f2;
    static File f3;
    static InputStream in1;
    static InputStream in2;
    static InputStream in3;

    public static Bundestagswahl getStandardBTW() throws IOException {
        InputStream in = KandidatenParserTest.class
                .getResourceAsStream("/kerg.csv");
        InputStream kerg = in;
        in = KandidatenParserTest.class.getResourceAsStream("/bundesL.csv");
        InputStream bundesl = in;
        in = KandidatenParserTest.class
                .getResourceAsStream("/Tab23_Wahlbewerber_a.csv");
        InputStream bewerber = in;
        Bundestagswahl result = new Bundestagswahl();
        BundeslaenderParser bp = new CSVBundeslaenderParser();
        bp.parseBundeslaenderDatei(bundesl, result);
        CSVWahlkreiseUndStimmenParser wp = new CSVWahlkreiseUndStimmenParser();
        wp.setzeDaten(kerg, result);
        wp.parseWahlkreise();
        KandidatenParser kp = new CSVKandidatenParser();
        kp.parseKandidatenDatei(bewerber, result);
        kerg.close();
        bewerber.close();
        bundesl.close();
        kerg = KandidatenParserTest.class.getResourceAsStream("/kerg.csv");
        wp.reinitialisiere(kerg);
        wp.parseStimmen();
        Zuteilungsverfahren zt = new SainteLagueIterativ();
        WahlverfahrenMitSperrklausel verfahren = new Bundeswahlgesetz2013(
                result, zt);
        BundeswahlgesetzSperrklauselpruefer spk = new BundeswahlgesetzSperrklauselpruefer();
        spk.setGesamteZweitstimmenzahl(result.berechneZweitstimmen());
        verfahren.setzeSperrklauselpruefer(spk);
        result.setWahlverfahren(verfahren);
        kerg.close();
        return result;
    }

    public static WahldatenImporter getStandardImporter() {
        File kerg = new File("./src/test/resources/kerg.csv");
        File bundesl = new File("./src/test/resources/bundesL.csv");
        File bewerber = new File(
                "./src/test/resources/Tab23_Wahlbewerber_a.csv");
        WahldatenImporter importer = new CSVWahldatenImporter(kerg, bewerber,
                bundesl);
        return importer;
    }

    public static Bundestagswahl importiereTestdaten(InputStream stimmen,
            InputStream stimmen2, InputStream bundeslaender,
            InputStream wahlbewerber) throws IOException {
        InputStream kerg = stimmen;
        InputStream kerg2 = stimmen2;
        InputStream bundesl = bundeslaender;
        InputStream bewerber = wahlbewerber;
        Bundestagswahl result = new Bundestagswahl();
        BundeslaenderParser bp = new CSVBundeslaenderParser();
        bp.parseBundeslaenderDatei(bundesl, result);
        CSVWahlkreiseUndStimmenParser wp = new CSVWahlkreiseUndStimmenParser();
        wp.setzeDaten(kerg, result);
        wp.parseWahlkreise();
        KandidatenParser kp = new CSVKandidatenParser();
        kp.parseKandidatenDatei(bewerber, result);
        kerg = stimmen;
        wp.reinitialisiere(kerg2);
        wp.parseStimmen();
        Zuteilungsverfahren zt = new SainteLagueIterativ();
        WahlverfahrenMitSperrklausel verfahren = new Bundeswahlgesetz2013(
                result, zt);
        BundeswahlgesetzSperrklauselpruefer spk = new BundeswahlgesetzSperrklauselpruefer();
        spk.setGesamteZweitstimmenzahl(result.berechneZweitstimmen());
        verfahren.setzeSperrklauselpruefer(spk);
        result.setWahlverfahren(verfahren);
        kerg.close();
        bewerber.close();
        bundesl.close();
        return result;
    }

    static void reinitialisiere() {
        TestDatenBereitsteller.b = new Bundestagswahl();
        TestDatenBereitsteller.cbp = new CSVBundeslaenderParser();
        TestDatenBereitsteller.ckp = new CSVKandidatenParser();
        TestDatenBereitsteller.cwp = new CSVWahlkreiseUndStimmenParser();
    }

    @Test
    public void testeBundestagswahl() throws IOException {
        Bundestagswahl zuTesten = getStandardBTW();
        zuTesten.bundeslaenderAnzahl();
    }
}
