/**
 * Interface für das exportieren von Stimmdaten
 */
package edu.kit.iti.formal.mandatsverteilung.exporter;


/**
 * @author Pascal
 * 
 */
public interface StimmenExporter {

	/**
	 * exportiert eine Stimmen und Wahlkreis Datei der aktuellen Wahl, 
	 * die wieder eingelesen kann.
	 * 
	 * @param dateiname
	 *            der zu expotierenden Stimmen Datei
	 *
	 */
	public void exportiereStimmen(String dateiname);
}
