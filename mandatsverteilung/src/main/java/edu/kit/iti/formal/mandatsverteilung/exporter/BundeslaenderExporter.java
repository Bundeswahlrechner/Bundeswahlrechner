/**
 *  Interface für das exportieren von BundesländerDateien.
 */
package edu.kit.iti.formal.mandatsverteilung.exporter;


/**
 * @author Pascal
 * 
 */
public interface BundeslaenderExporter {

	/**
	 * Funktion zum exportieren einer Bundesland Datei der aktuellen Wahl,
	 * die wieder eingelesen werden kann.
	 * @param dateiname
	 *            der zu expotierenden BundeslandDatei
	 */
	public void exportiereBundeslaender(String dateiname);
}
