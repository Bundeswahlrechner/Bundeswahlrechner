package edu.kit.iti.formal.mandatsverteilung.generierer;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;

public abstract class Einschraenkung {
	/**
	 * Wie sehr die Einschränkung gewichtet wird. Höheres Gewicht führt zu
	 * größerem Abstand.
	 */
	protected double gewichtung;
	/**
	 * Der Wert der Einschränkung, z.B. wie viele Sitze die BTW haben soll.
	 */
	protected int wert;
	/**
	 * Die erlaubte Abweichung vom Wert, also das "Plusminus". Sollte in der
	 * Regel positiv sein.
	 */
	protected int abweichung;

	/**
	 * Überprüft die übergebene Stimmverteilung/Bundestagswahl auf Konformität
	 * mit der Einschränkung. Gibt 0 zurück, wenn die Einschränkung erfüllt ist,
	 * sonst eine größer werdende natürliche Zahl, je schwerer die Bedingung
	 * verletzt ist
	 * 
	 * @param b
	 *            Bundestagswahl mit der Stimmverteilung
	 * @return Ergebnis
	 */
	abstract int ueberpruefeErgebnis(Bundestagswahl b);

	protected int minDistance(int value1, int value2, int plusminus) {
		// Methode: Berechne Abstand zwischen value1 und value2, ziehe dann
		// plusminus ab. Wenn > 0, nimm das, sonst gib 0 zurück
		int dist = Math.abs(value1 - value2);
		int result = dist - plusminus;
		if (result < 0) {
			return 0;
		} else {
			return result;
		}
	}

}
