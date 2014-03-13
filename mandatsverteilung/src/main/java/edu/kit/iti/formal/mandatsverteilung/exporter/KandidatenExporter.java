/**
 *  Interface für das exportieren von Kandidaten
 */
package edu.kit.iti.formal.mandatsverteilung.exporter;



/**
 * @author Pascal
 * 
 */
public interface KandidatenExporter {
	
	
	/**
	 * exportiert eine Kandidaten Datei der aktuellen Wahl,
	 * die wieder eingelesen werden kann.
	 *@param dateiname
	 *				der zu expotierenden Kandidaten Datei 
	 */
	public void exportiereKandidaten(String Dateiname);
}
