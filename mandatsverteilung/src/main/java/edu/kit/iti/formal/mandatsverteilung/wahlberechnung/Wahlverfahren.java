package edu.kit.iti.formal.mandatsverteilung.wahlberechnung;

import java.util.Iterator;
import java.util.List;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Kreiswahlvorschlag;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Wahlkreis;

/**
 * Modelliert ein abstraktes Wahlverfahren mit seinen grundsätzliche
 * Eigenschaften.
 */
public abstract class Wahlverfahren implements Cloneable {

    private Bundestagswahl bundestagswahl;
    private final Zuteilungsverfahren zuteilungsverfahren;

    /**
     * @param bundestagswahl
     * @param zuteilungsverfahren
     */
    protected Wahlverfahren(Bundestagswahl bundestagswahl,
            Zuteilungsverfahren zuteilungsverfahren) {
        super();
        this.bundestagswahl = bundestagswahl;
        this.zuteilungsverfahren = zuteilungsverfahren;
        this.bundestagswahl.setWahlverfahren(this);
    }

    public void berechneWahlergebnis() {
        this.werteErststimmenAus();
        this.werteZweitstimmenAus();
    }

    protected Kreiswahlvorschlag berechneWahlkreisgewinner(Wahlkreis wahlkreis) {
        List<Kreiswahlvorschlag> bewerber = wahlkreis.getWahlkreisBewerber();
        Kreiswahlvorschlag gewinner = bewerber.get(0);
        for (Kreiswahlvorschlag kwv : wahlkreis.getWahlkreisBewerber()) {
            if (kwv.getStimmen() > gewinner.getStimmen()) {
                gewinner = kwv;
            }
        }
        return gewinner;
    }

    @Override
    public abstract Wahlverfahren clone();

    /**
     * @return the bundestagswahl
     */
    protected Bundestagswahl getBundestagswahl() {
        return bundestagswahl;
    }

    /**
     * @return the zuteilungsverfahren
     */
    public Zuteilungsverfahren getZuteilungsverfahren() {
        return zuteilungsverfahren;
    }

    public void setBundestagswahl(Bundestagswahl b) {
        if (bundestagswahl != b) {
            bundestagswahl = b;
            b.setWahlverfahren(this);
        }
    }

    protected void werteErststimmenAus() {
        Iterator<Wahlkreis> iterator = this.getBundestagswahl()
                .wahlkreisIterator();
        while (iterator.hasNext()) {
            Wahlkreis wahlkreis = iterator.next();
            Kreiswahlvorschlag gewinner = this
                    .berechneWahlkreisgewinner(wahlkreis);
            wahlkreis.setGewinner(gewinner);
        }
    }

    protected abstract void werteZweitstimmenAus();
}