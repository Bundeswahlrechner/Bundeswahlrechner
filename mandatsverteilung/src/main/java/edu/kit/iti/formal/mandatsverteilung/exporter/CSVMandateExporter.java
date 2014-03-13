/**
 * Exporter, der die Sitzverteilung im Bundestag einer Wahl 
 * in CSV-Format exportiert.
 */
package edu.kit.iti.formal.mandatsverteilung.exporter;

import java.util.ArrayList;
import java.util.Iterator;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Partei;

/**
 * @author Pascal
 * 
 */
public class CSVMandateExporter implements MandateExporter {
	/**
	 * Bundestagswahl, die momentan in der gui aktiv ist.
	 */
	private final Bundestagswahl aktiveWahl;

	public CSVMandateExporter(Bundestagswahl aktiveWahl) {
		this.aktiveWahl = aktiveWahl;
	}

	@Override
	public void exportiereMandate(String dateiname) {
		CSVDateiWriter writer = new CSVDateiWriter(dateiname);
		ArrayList<String[]> mandateListe = new ArrayList<String[]>();
		Iterator<Partei> p = aktiveWahl.eingezogeneParteien().iterator();
		Partei curP;
		String[] zeile1;
		String[] zeile2;
		if (aktiveWahl.getParteiloseMandatstraeger() == 0) {
			zeile1 = new String[aktiveWahl.eingezogeneParteien().size() + 1];
			zeile2 = new String[aktiveWahl.eingezogeneParteien().size() + 1];
		} else {
			zeile1 = new String[aktiveWahl.eingezogeneParteien().size() + 2];
			zeile2 = new String[aktiveWahl.eingezogeneParteien().size() + 2];
		}

		zeile1[0] = "Eingezogene Parteien";
		zeile2[0] = "Sitzzahl im Bundestag";
		int i = 1;
		while (p.hasNext()) {
			curP = p.next();
			zeile1[i] = curP.getName();
			zeile2[i] = Integer.toString(curP.getOberverteilungSitzzahl());
			i++;
		}
		if (aktiveWahl.getParteiloseMandatstraeger() != 0) {
			zeile1[zeile1.length - 1] = "Parteilose";
			zeile2[zeile2.length - 1] = Integer.toString(aktiveWahl
					.getParteiloseMandatstraeger());
		}
		mandateListe.add(zeile1);
		mandateListe.add(zeile2);
		writer.schreibeDatei(mandateListe);
	}

}
