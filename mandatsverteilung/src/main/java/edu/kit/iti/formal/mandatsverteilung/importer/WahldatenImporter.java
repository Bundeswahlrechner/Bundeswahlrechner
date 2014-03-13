package edu.kit.iti.formal.mandatsverteilung.importer;

import java.io.IOException;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;

/**
 * Allgemeiner Importer für Bundestagswahldateien-Verbünde
 * 
 * @author Jan
 * @version 1.0
 */
public abstract class WahldatenImporter {
    /**
     * Parserfabrik, die die konkreten Parser erstellt.
     */
    protected ParserFabrik fabrik;

    public WahldatenImporter() {
    }

    public WahldatenImporter(ParserFabrik f) {
        fabrik = f;
    }

    protected ParserFabrik getFabrik() {
        return fabrik;
    }

    /**
     * Importiert die vorher gesetzten Dateien. Wird durch die GUI aufgerufen;
     * im Erfolgsfall einzige Schnittstelle zur GUI. Der Controller muss je nach
     * Importauswahl durch den Nutzer entscheiden, welcher konkrete Importer
     * verwendet wird
     * 
     * @return Eine valide Bundestagswahl nach den Dateivorgaben
     * @throws IOException
     *             Falls beim Importieren etwas schief lief
     */
    public abstract Bundestagswahl importieren() throws IOException;

    public void setFabrik(ParserFabrik pf) {
        fabrik = pf;
    }
}
