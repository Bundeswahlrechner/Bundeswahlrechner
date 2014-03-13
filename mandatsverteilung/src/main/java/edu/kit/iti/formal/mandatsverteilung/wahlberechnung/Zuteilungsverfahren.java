package edu.kit.iti.formal.mandatsverteilung.wahlberechnung;

import java.util.Collection;

/**
 * Klassen, die das Interface Zuteilungsverfahren implementieren, stellen die
 * benötigten Methoden zur Verteilung eines Kontingents auf mehrere Objekte die
 * das Interface {@link Zuteilbar} implementieren zur Verfügung. Die Verteilung
 * erfolgt auf Basis des Zuteilungswertes.
 * 
 * @version 1.0
 */
public interface Zuteilungsverfahren extends Cloneable {

    /**
     * Verteilt das zuvor spezifizierte Kontingent auf die übergebenen
     * Zuteilungsobjekte.
     * <p>
     * Beachte: Vor Benutzung dieser Methode müssen ein Kontingent gesetzt und
     * Zuteilungsobjekte spezifiziert werden.
     */
    public void berechneZuteilung();

    /**
     * Ermöglicht das Abfragen des Zuteilungsergbnisses der Methode
     * berechneZuteilung() für das übergebene Zuteilungsobjekt.
     * 
     * @param objekt
     *            das Objekt, für das das Zuteilungsergenis bestimmt werden soll
     * @return das Zuteilungsergenis oder {@code null}, wenn das Objekt nicht
     *         Teil der zuvor übergebenen Zuteilungsobjekte war
     */
    public int getZuteilungsergebnis(Zuteilbar objekt);

    /**
     * Setzt das Kontingent, das verteilt werden soll.
     * 
     * @param kontingent
     *            das zu verteilende Kontingent
     */
    public void setZuteilungskontingent(int kontingent);

    /**
     * Setzt die Zuteilungsobjekte, auf die das Kontingent verteilt werden soll.
     * 
     * @param objekte
     *            die Zuteilungsobjekte, auf die verteilt wird
     */
    public void setZuteilungsobjekte(Collection<? extends Zuteilbar> objekte);

    /**
     * Gibt den Namen des {@code Zuteilungsverfahren}s als String zurück
     * 
     * @return Name des {@code Zuteilungsverfahren}s
     */
    @Override
    public String toString();

    public Zuteilungsverfahren clone();
}
