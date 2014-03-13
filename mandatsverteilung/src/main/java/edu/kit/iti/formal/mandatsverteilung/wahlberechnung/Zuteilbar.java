package edu.kit.iti.formal.mandatsverteilung.wahlberechnung;

/**
 * Klassen, die dieses Interface implementieren, können von Zuteilungsverfahren benutzt werden.
 */
public interface Zuteilbar {

	/**
	 * @return Zahlenwert, den das Objekt im Rahmen eines Zuteilungsverfahrens repräsentiert.
	 */
	public int getZuteilungswert();

}
