package edu.kit.iti.formal.mandatsverteilung.importer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;

/**
 * Interface für einen Kandidaten-Datei-Parser
 * 
 * @author Jan
 * @version 1.0
 */
public interface KandidatenParser {
	/**
	 * Parst die übergebene Kandidaten-Datei.
	 * 
	 * @param datei
	 *            Datei, die Namen, Landeslistenplätze etc. der Kandidaten
	 *            enthält
	 * @param b
	 *            Bundestagswahl, die geändert wird
	 * @throws IOException
	 *             , falls beim parsen etwas schiefgeht.
	 */
	public void parseKandidatenDatei(File datei, Bundestagswahl b)
			throws IOException;

	/**
	 * Parst die übergebene Kandidaten-Datei.
	 * 
	 * @param in
	 *            InputStream, der Namen, Landeslistenplätze etc. der Kandidaten
	 *            enthält
	 * @param b
	 *            Bundestagswahl, die geändert wird
	 * @throws IOException
	 *             , falls beim Parsen etwas schiefgeht.
	 */
	public void parseKandidatenDatei(InputStream in, Bundestagswahl b)
			throws IOException;
}
