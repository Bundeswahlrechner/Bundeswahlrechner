/**
 * Interface für das expotieren von Ergebnissen
 */
package edu.kit.iti.formal.mandatsverteilung.exporter;

/**
 * @author Pascal
 * 
 */
public interface MandateExporter {

	/**
	 * 
	 * @param dateiname
	 *            der zu expotierenden Datei
	 */
	public void exportiereMandate(String dateiname);
}
