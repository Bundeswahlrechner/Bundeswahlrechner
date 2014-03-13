package edu.kit.iti.formal.mandatsverteilung.importer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;

/**
 * Interface für einen Bundesländer-Datei-Parser.
 * 
 * @author Jan
 * @version 1.0
 */
public interface BundeslaenderParser {

	/**
	 * Parst die übergebene Bundesländer-Datei.
	 * 
	 * @param datei
	 *            Datei, die Einwohnerzahlen, Namen etc. von Bundesländer
	 *            enthält.
	 * @param b
	 *            Bundestagswahl, der die Bundesländer hinzugefügt werden
	 * @throws IOException
	 *             wenn beim Parsen etwas schief geht
	 */
	public void parseBundeslaenderDatei(File datei, Bundestagswahl b)
			throws IOException;

	/**
	 * Parst die übergebene Bundesländer-Datei
	 * 
	 * @param in
	 *            InputStream zur Bundesländer-Datei
	 * @param b
	 *            Bundestagswahl, der die Bundesländer hinzugefügt werden
	 * @throws IOException
	 *             wenn beim Parsen etwas schief geht
	 */
	public void parseBundeslaenderDatei(InputStream in, Bundestagswahl b)
			throws IOException;
}
