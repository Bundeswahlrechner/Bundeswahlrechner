package edu.kit.iti.formal.mandatsverteilung.datenhaltung;

import edu.kit.iti.formal.mandatsverteilung.gui.BerechnungsOptionenFenster;

/**
 * Kapselt die gewählten Berechnungsoptionen gemäß Memento-Entwurfsmuster.
 */
public class Berechnungsoptionen implements Cloneable {

    /**
     * Name der vom Benutzer gewählten Fabrik.
     */
    private final String fabrik;

    /**
     * Höhe der vom Benutzer gewählten Anzahl an hinreichenden Grundmandaten.
     */
    private final int grundmandate;

    /**
     * Höhe der vom Benutzer gewählten Prozenthürde.
     */
    private final double prozenthuerde;

    /**
     * Name des vom Benutzer gewählten Wahlverfahrens.
     */
    private final String wahlverfahren;

    /**
     * Name des vom Benutzer gewählten Zuteilungsverfahrens.
     */
    private final String zuteilungsverfahren;

    /**
     * Erzeugt ein Memento-Objekt mit den Standard-Berechnungsoptionen.
     */
    public Berechnungsoptionen() {
        super();
        fabrik = BerechnungsOptionenFenster.bundeswahlgesetz2013FabrikName;
        grundmandate = 3;
        prozenthuerde = 0.05;
        wahlverfahren = "Bundeswahlgesetz 2013";
        zuteilungsverfahren = "Sainte-Lague Iterativ";
    }

    /**
     * Erzeugt ein Memento-Objekt für die übergebenen Berechnungsoptionen.
     * 
     * @param fabrik
     *            - Name der vom Benutzer gewählten Fabrik.
     * @param grundmandate
     *            - Höhe der vom Benutzer gewählten Anzahl an hinreichenden
     *            Grundmandaten.
     * @param prozenthuerde
     *            - Höhe der vom Benutzer gewählten Prozenthürde.
     * @param wahlverfahren
     *            - Name des vom Benutzer gewählten Wahlverfahrens.
     * @param zuteilungsverfahren
     *            - Name des vom Benutzer gewählten Zuteilungsverfahrens.
     */
    public Berechnungsoptionen(String fabrik, int grundmandate,
            double prozenthuerde, String wahlverfahren,
            String zuteilungsverfahren) {
        super();
        this.fabrik = fabrik;
        this.grundmandate = grundmandate;
        this.prozenthuerde = prozenthuerde;
        this.wahlverfahren = wahlverfahren;
        this.zuteilungsverfahren = zuteilungsverfahren;
    }

    @Override
    public Berechnungsoptionen clone() {
        Berechnungsoptionen result = new Berechnungsoptionen(fabrik,
                grundmandate, prozenthuerde, wahlverfahren, zuteilungsverfahren);
        return result;
    }

    /**
     * @return Name der vom Benutzer gewählten Fabrik.
     */
    public String getFabrik() {
        return fabrik;
    }

    /**
     * @return Höhe der vom Benutzer gewählten Anzahl an hinreichenden
     *         Grundmandaten.
     */
    public int getGrundmandate() {
        return grundmandate;
    }

    /**
     * @return Höhe der vom Benutzer gewählten Prozenthürde.
     */
    public double getProzenthuerde() {
        return prozenthuerde;
    }

    /**
     * @return Name des vom Benutzer gewählten Wahlverfahrens.
     */
    public String getWahlverfahren() {
        return wahlverfahren;
    }

    /**
     * @return Name des vom Benutzer gewählten Zuteilungsverfahrens.
     */
    public String getZuteilungsverfahren() {
        return zuteilungsverfahren;
    }
}
