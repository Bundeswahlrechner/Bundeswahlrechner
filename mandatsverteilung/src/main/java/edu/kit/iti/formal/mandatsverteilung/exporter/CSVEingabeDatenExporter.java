/**
 *  Exporter, der die Eingabedaten einer Wahl exportiert
 */
package edu.kit.iti.formal.mandatsverteilung.exporter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundesland;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Kandidat;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Kreiswahlvorschlag;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Landesliste;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Partei;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Wahlkreis;

/**
 * @author Pascal
 * 
 */
public class CSVEingabeDatenExporter implements StimmenExporter,
		KandidatenExporter, BundeslaenderExporter {

	/**
	 * Zu exportierende Bundestagswahl
	 */
	private Bundestagswahl btw;
	
	public CSVEingabeDatenExporter(Bundestagswahl aktiveWahl) {
		this.btw = aktiveWahl;
	}

	@Override
	public void exportiereBundeslaender(String dateiname) {
		CSVDateiWriter writer = new CSVDateiWriter(dateiname);
		ArrayList<String[]> bundeslandListe = new ArrayList<String[]>();
		String [] ersteZeile = {"Bundesland", "Abkürzung", "Einwohnerzahl"};
		bundeslandListe.add(ersteZeile);
		
		Iterator<Bundesland> bl = btw.bundeslaenderIterator();
		Bundesland curB;
		while(bl.hasNext()) {
			curB = bl.next();
			String[] zeile = {curB.getName(), curB.getAbkuerzung()
					, String.valueOf(curB.getEinwohnerzahl())};
			bundeslandListe.add(zeile);
		}
		writer.schreibeDatei(bundeslandListe);
		
	}

	@Override
	public void exportiereKandidaten(String dateiname) {
		ArrayList<String[]> kandidatenListe;
		CSVDateiWriter writer = new CSVDateiWriter(dateiname);
		kandidatenListe = new ArrayList<String[]>();
		String [] ersteZeile = {"Name, Vornamen(n)", "", "Partei"
				, "Bewerber im Wahlkreis", "Bewerber auf Landesliste"
				, "Landeslisten-Platz"};
		kandidatenListe.add(ersteZeile); 
		Iterator<Wahlkreis> wk = btw.wahlkreisIterator();
		Wahlkreis curW;
		while (wk.hasNext()) {
			curW = wk.next();
			List<Kreiswahlvorschlag> kwv = curW.getWahlkreisBewerber();
			for (int i = 0; i < kwv.size(); i++) {
				Kreiswahlvorschlag curKwv = kwv.get(i);
				String  landesliste = "";
				if (curKwv.getLandesliste() != null){
					landesliste = curKwv.getLandesliste().getBundesland().getAbkuerzung();
				}
				String landeslistenplatz;
				if (curKwv.getLandeslistenplatz() < 0) {
					landeslistenplatz = "";
				} else {
					landeslistenplatz = String.valueOf(curKwv.getLandeslistenplatz());
				}
				String parteiName = "Anderer KWV";
				if (curKwv.getPartei() != null) {
					parteiName = curKwv.getPartei().getName();
				}
					String [] zeile = {curKwv.getName(), ""
							, parteiName
							,String.valueOf(curKwv.getWahlkreis().getNummer())
							, landesliste, landeslistenplatz};
					kandidatenListe.add(zeile);
				
			}
		}
		
		Iterator<Partei> p = btw.parteienIterator();
		Iterator<Kandidat> k;
		Partei curP;
		Bundesland curB;
		Kandidat curK;
		Landesliste ll;
		boolean istKreiswahlvorschlag = false;
		while (p.hasNext()) {
			curP = p.next();
			Iterator<Bundesland> bl = btw.bundeslaenderIterator();
			while (bl.hasNext()) {
				curB = bl.next();;
				if (curP.getLandesliste(curB) != null) {
					ll = curP.getLandesliste(curB);
					k = ll.kandidatenIterator();
					while (k.hasNext()) {
						curK = k.next();
						String landesliste = "";
						if (curK.getLandesliste() != null) {
							landesliste = curK.getLandesliste().getBundesland().getAbkuerzung();
						}
						String [] zeile = {curK.getName(), "", curP.getName(), ""
							, landesliste
							, String.valueOf(curK.getLandeslistenplatz())};
						
						istKreiswahlvorschlag = false;
						for (int i = 0; i < kandidatenListe.size(); i++) {
							if (kandidatenListe.get(i)[0] == curK.getName()) {
								istKreiswahlvorschlag = true;
								break;
							} 
						}
						if (istKreiswahlvorschlag == false) {
							kandidatenListe.add(zeile);
						}
					}
				}
			}
		}	
		writer.schreibeDatei(kandidatenListe);
	}

	
	@Override
	public void exportiereStimmen(String dateiname) {
		CSVDateiWriter writer = new CSVDateiWriter(dateiname);
		ArrayList<String[]> stimmenListe = new ArrayList<String[]>();
		String[] leer = new String [0];
		
		stimmenListe.add(leer);
		stimmenListe.add(leer);
		ArrayList<String> zeile = new ArrayList<String>();
		for (int i = 0; i < 19; i++) {
			if (i == 2) {
				String s = "gehört";
				zeile.add(s);
			} else {
				zeile.add("");
			}
		}
		
		
		Iterator<Partei> p = btw.parteienIterator();
		Partei curP = null;
		while (p.hasNext()) {
			curP = p.next();
				zeile.add(curP.getName());
				zeile.add(curP.getFarbe().toString());
				zeile.add("");
				zeile.add("");
		}
		zeile.add("Übrige");
		zeile.add("");
		zeile.add("");
		zeile.add("");
		zeile.add("");
		int spaltenanzahl = zeile.size();
		String [] array = zeile.toArray(new String[]{});
		stimmenListe.add(array);
		zeile = new ArrayList<String>();
		
		for (int k = 0; k < 19; k++) {
			if (k == 2) {
				String s = "zu";
				zeile.add(s);
			} else {
				zeile.add("");
			}
		}
		int count = 19;
		while (count < spaltenanzahl - 3) {
			zeile.add("Erststimmen");
			zeile.add("");
			zeile.add("Zweitstimmen");
			zeile.add("");
			count = count + 4;
		}
		zeile.add("");
		
		array = zeile.toArray(new String[]{});
		stimmenListe.add(array);		
		stimmenListe.add(leer);
		
		Iterator<Bundesland> bl = btw.bundeslaenderIterator();
		Bundesland curB;
		Wahlkreis curWk;
		int blId = 0;
		
		while (bl.hasNext()) {
			curB = bl.next();
			blId++;
			
			Iterator<Wahlkreis> wk = curB.wahlkreisIterator();
			while (wk.hasNext()) {
				zeile = new ArrayList<String>();
				curWk = wk.next();
				zeile.add(Integer.toString(curWk.getNummer()));
				zeile.add(curWk.getName());
				zeile.add(Integer.toString(blId));
				for (int i = 0; i < 16; i++) {
						zeile.add("");
					}
				Iterator<Partei> parteien = btw.parteienIterator();
				List<Kreiswahlvorschlag> wkBewerber = curWk.getWahlkreisBewerber();
				while (parteien.hasNext()) {
					curP = parteien.next();
					String erststimmen = null;
					String zweitstimmen = null;
					for (int i = 0; i < wkBewerber.size(); i++) {
						if (wkBewerber.get(i).getPartei() == curP) {
							erststimmen = Integer.toString(wkBewerber.get(i).getStimmen());
						}
					}
					zweitstimmen = Integer.toString(curWk.getParteiZweitstimmen(curP));
					zeile.add(erststimmen);
					zeile.add("");
					zeile.add(zweitstimmen);
					zeile.add("");
				}
				String unabhängig = null;
				int vergleich = 0;
				for (int i = 0; i < wkBewerber.size(); i++) {
					if (wkBewerber.get(i).getPartei() == null 
							&& wkBewerber.get(i).getStimmen() > vergleich) {
						vergleich = wkBewerber.get(i).getStimmen();
						unabhängig = Integer.toString(wkBewerber.get(i).getStimmen());
					}
				}
				zeile.add(unabhängig);
				zeile.add("");
				zeile.add("");
				zeile.add("");
				zeile.add("");
				array = zeile.toArray(new String[]{});
				stimmenListe.add(array);
			}
			String [] blZeile = {Integer.toString(blId), curB.getName(), "99"};
			stimmenListe.add(blZeile);
			stimmenListe.add(leer);
		}
		String[] BgZeile = {"99", "Bundesgebiet", ""};
		stimmenListe.add(BgZeile);
		writer.schreibeDatei(stimmenListe);
	}

}
