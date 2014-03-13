package edu.kit.iti.formal.mandatsverteilung.wahlberechnung;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundesland;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Kandidat;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Kreiswahlvorschlag;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Landesliste;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Partei;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Wahlkreis;

/**
 * Die Klasse ermöglicht für eine {@link Bundestagswahl} die Berechnung aller
 * Zwischen- und Endergebnisse nach einer Variante des Bundeswahlgesetzes 2013
 * ohne Ausgleichsmandate unter Verwendung eines {@link Zuteilungsverfahren}s
 * und eines {@link Sperrklauselpruefer}s. Die ermittelten Ergebnisse werden in
 * der zu Grunde gelegten {@code Bundestagswahl} gespeichert.
 * <p>
 * Beachte: Diese Variante des Wahlverfahrens ähnelt dem Bundeswahlgesetz 2009,
 * es wird jedoch nich garantiert, dass die Berechnungsergebnisse identisch
 * sind.
 * 
 * @author tillneuber
 * @version 1.2
 * 
 */
public class BundeswahlgesetzOhneAusgleichsmandate extends
		WahlverfahrenMitSperrklausel {

	/**
	 * Gibt die Wahlkreisgewinner ohne erfolgreiche Landesliste im jeweiligen
	 * Bundesland an.
	 */
	private Map<Bundesland, Collection<Kreiswahlvorschlag>> wahlkreisgewinnerOhneErfolgreicheLandesliste;

	/**
	 * Erzeugt eine neues {@code Wahlverfahren} entsprechend dem
	 * Bundeswahlgesetz 2013.
	 * 
	 * @param bundestagswahl
	 *            die {@link Bundestagswahl} die berechnet werden soll
	 * @param zuteilungsverfahren
	 *            das zur Berechnung verwendete {@link Zuteilungsverfahren}
	 */
	public BundeswahlgesetzOhneAusgleichsmandate(Bundestagswahl bundestagswahl,
			Zuteilungsverfahren zuteilungsverfahren) {
		super(bundestagswahl, zuteilungsverfahren);
		// Initiale Bundestagsgröße entspricht dem doppelten der Wahlkreisanzahl
		this.getBundestagswahl().setSitzzahl(
				this.getBundestagswahl().wahlkreisAnzahl() * 2);
	}

	/**
	 * Berechnet die Oberverteilungsitzzahl jeder Partei.
	 */
	private void berechneOberverteilung() {
		// Lokale Kopie der Sitzanzahl im Bundestag für die Berechnung.
		int btGroesse = this.getBundestagswahl().getSitzzahl();
		// Die Bundestagsgröße und die entsprechenden Oberverteilungssitzzahlen
		// werden im Folgenden durch das Verfahren der schrittweisen Erhöhung
		// der Bundestagsgröße bestimmt.
		// Bestimme zunächst die initiale Bundestagsgröße. Ziehe hierzu von der
		// inititalen Größe (2 * Wahlkreisanzahl; im Standardfall also 598) die
		// Anzahl der Wahlkreisgewinner ohne erfolgreiche Landesliste ab.
		// Beachte: Die tatsächlich benötigte Bundestagsgröße ist nun immer
		// größer oder gleich der so bestimmten, initialen Größe.
		Iterator<Bundesland> blIt = this.getBundestagswahl()
				.bundeslaenderIterator();
		while (blIt.hasNext()) {
			btGroesse -= wahlkreisgewinnerOhneErfolgreicheLandesliste.get(
					blIt.next()).size();
		}
		Zuteilungsverfahren zt = this.getZuteilungsverfahren();
		// Die aktuell Bundestagsgröße wird auf die Parteien verteilt, setze
		// sie also als Kontingent.
		zt.setZuteilungskontingent(btGroesse);
		Iterator<Partei> parteiIt = this.getBundestagswahl().parteienIterator();
		// Baue Liste mit Parteien auf, die die Sperrklausel überwunden
		// haben und setze sie als Zuteilungsobjekte.
		List<Partei> erfolgreicheParteien = new LinkedList<Partei>();
		while (parteiIt.hasNext()) {
			Partei partei = parteiIt.next();
			if (partei.isSperrklauselUeberwunden()) {
				erfolgreicheParteien.add(partei);
			}
		}
		zt.setZuteilungsobjekte(erfolgreicheParteien);
		zt.berechneZuteilung();
		for (Partei partei : erfolgreicheParteien) {
			partei.setOberverteilungSitzzahl(zt.getZuteilungsergebnis(partei));
		}
	}

	/**
	 * Die Methode verteilt die Obverteilungssitze parteiintern auf
	 * Landeslisten.
	 */
	private void berechneUnterverteilung() {
		// Gibt an, wie viele Überhangmandate die Parteien erhalten haben.
		Map<Partei, Integer> ueberhangmandate = new HashMap<Partei, Integer>();
		// Iteriere über alle Parteien
		Iterator<Partei> parteienIt = this.getBundestagswahl()
				.parteienIterator();
		// Berechne die Zahl an Landeslistenplätzen, die aufgrund von
		// Listenerschöpfung nicht in Anspruch genommen werden.
		int listenerschoepfung = 0;
		while (parteienIt.hasNext()) {
			Partei partei = parteienIt.next();
			// Unterverteilungssitzzahl muss nur für Parteien bestimmt werden,
			// die die Sperrklausel überwunden haben
			if (partei.isSperrklauselUeberwunden()) {
				// Berechne die aktuelle Zahl von Landeslistenplätzen, die
				// aufgrund von fehlenden Kandidaten nicht in Anspruch
				// genommen werden.
				int aktuelleListenerschoepfung = 0;
				// verteilteSitze gibt die bisher auf Landeslisten
				// verteilten Sitze an
				// Baue Liste mit allen Landeslisten einer Partei auf.
				Iterator<Bundesland> bundeslaender = this.getBundestagswahl()
						.bundeslaenderIterator();
				List<Landesliste> parteiLandeslisten = new LinkedList<Landesliste>();
				while (bundeslaender.hasNext()) {
					Landesliste ll = partei
							.getLandesliste(bundeslaender.next());
					if (ll != null) {
						parteiLandeslisten.add(ll);
					}
				}
				// Setze aktuelleLandeslistenerschoepfung und
				// Überhangmandate zurück.
				aktuelleListenerschoepfung = 0;
				ueberhangmandate.put(partei, 0);
				Zuteilungsverfahren verfahren = this.getZuteilungsverfahren();
				verfahren.setZuteilungskontingent(partei
						.getOberverteilungSitzzahl());
				verfahren.setZuteilungsobjekte(parteiLandeslisten);
				verfahren.berechneZuteilung();
				Iterator<Bundesland> blIt = this.getBundestagswahl()
						.bundeslaenderIterator();
				while (blIt.hasNext()) {
					Bundesland bl = blIt.next();
					Landesliste ll = partei.getLandesliste(bl);
					if (ll != null) {
						// Bestimme Landeslistensitze.
						int llSitze = verfahren.getZuteilungsergebnis(ll);
						// Prüfe, ob die Partei in diesem Bundesland
						// Überhangmandate errungen hat.
						int aktuelleUeberhangmandate = (partei
								.wahlkreisgewinnerInBundesland(bl) - llSitze) > 0 ? partei
								.wahlkreisgewinnerInBundesland(bl) - llSitze
								: 0;
						ueberhangmandate.put(partei,
								ueberhangmandate.get(partei)
										+ aktuelleUeberhangmandate);
						// Berechne die Gesamtzahl der Abgeordneten der
						// Partei in diesem Bundesland.
						int sitze = llSitze + aktuelleUeberhangmandate;
						// Setze die so berechnete Sitzzahl als
						// Unterverteilungssitzzahl - ggf. wird dieses
						// Ergebnis später überschrieben.
						ll.setUnterverteilungSitzzahl(sitze);
						// Prüfe, ob die Landesliste genügend Kandidaten hat
						// - korrigiere ggf.
						aktuelleListenerschoepfung += this
								.ueberprufeLandeslistenerschoepfung(ll);
					}
				}
				listenerschoepfung += aktuelleListenerschoepfung;
			}
		}
		// Bestimme die Gesamtzahl der vergebenen Überhangmandate und erhöhe den
		// Bundestag entsprechend.
		int ueberhangmandatszahl = 0;
		for (Partei aktuellePartei : ueberhangmandate.keySet()) {
			// Oberverteilungssitzzahl gibt zur leichteren Verwendung in der GUI
			// die Gesamtzahl der Parteisitze an - erhöhe deshalb um die
			// Überhangmandate.
			aktuellePartei.setOberverteilungSitzzahl(aktuellePartei
					.getOberverteilungSitzzahl()
					+ ueberhangmandate.get(aktuellePartei));
			ueberhangmandatszahl += ueberhangmandate.get(aktuellePartei);
		}
		// Erhöhe die Bundestagssitzzahl um die Überhangmandate und ziehe die
		// durch Listenerschöpfung nicht in Anspruch genommenen
		// Landeslistenplätze ab.
		this.getBundestagswahl().setSitzzahl(
				this.getBundestagswahl().getSitzzahl() + ueberhangmandatszahl
						- listenerschoepfung);
	}

	/**
	 * Ermittelt die Wahlkreisgewinner ohne erfolgreiche Landesliste in allen
	 * Bundesländern.
	 */
	private void berechneWahlkreisgewinnerOhneErfolgreicheLandesliste() {
		// Lege neue Datenstruktur an - insbesondere um Fehler bei merfachen
		// Berechnungen auf den gleichen Daten zu verhindern.
		wahlkreisgewinnerOhneErfolgreicheLandesliste = new HashMap<Bundesland, Collection<Kreiswahlvorschlag>>();
		Iterator<Bundesland> blIt = this.getBundestagswahl()
				.bundeslaenderIterator();
		while (blIt.hasNext()) {
			Bundesland bl = blIt.next();
			// Setze die Zahl an Wahlkreisgewinnern ohne erfolgreiche
			// Landesliste im aktuellen Bundesland auf 0.
			wahlkreisgewinnerOhneErfolgreicheLandesliste.put(bl,
					new HashSet<Kreiswahlvorschlag>());
			// Iteriere über die Wahlkreise des aktuellen Bundeslandes und
			// betrachte die Gewinner.
			Iterator<Wahlkreis> wkIt = bl.wahlkreisIterator();
			while (wkIt.hasNext()) {
				Kreiswahlvorschlag gewinner = wkIt.next().getGewinner();
				// Entscheide, ob der Wahlkreisgewinner eine erfolgreiche
				// Landesliste hat oder nicht.
				if (gewinner.getPartei() == null
						|| gewinner.getPartei().isSperrklauselUeberwunden() == false
						|| gewinner.getPartei().getLandesliste(bl) == null) {
					// Wahlkreisgewinner hat keine erfolgreiche Landesliste.
					wahlkreisgewinnerOhneErfolgreicheLandesliste.get(bl).add(
							gewinner);
				}
			}
		}
	}

	@Override
	public BundeswahlgesetzOhneAusgleichsmandate clone() {
		BundeswahlgesetzOhneAusgleichsmandate result = new BundeswahlgesetzOhneAusgleichsmandate(
				this.getBundestagswahl(), this.getZuteilungsverfahren().clone());
		result.setzeSperrklauselpruefer(spk.clone());
		return result;
	}

	/**
	 * Berechnet die, auf die Bundesländer entfallenden, ersten Sitzkontingente
	 * basierend auf den Einwohnerzahlen und speichert die Berechnungsergebnisse
	 * im jeweiligen Bundesland.
	 */
	private void setzeErsteSitzkontingente() {
		// gesetztes Zuteilungsverfahren holen
		Zuteilungsverfahren zt = this.getZuteilungsverfahren();
		Iterator<Bundesland> it = this.getBundestagswahl()
				.bundeslaenderIterator();
		// Setze die Bundestagsgröße als Zuteilungskontingent des Verfahrens; in
		// diesem Schritt entspricht die Bundestagsgröße gerade dem doppelten
		// der Wahlkreisanzahl
		zt.setZuteilungskontingent(this.getBundestagswahl().getSitzzahl());
		// Baue über den Bundesland-Iterator eine Bundesländerliste auf und
		// übergebe diese Liste als Zuteilungsobjekte an das Zuteilungsverfahren
		List<Zuteilbar> bundeslaender = new LinkedList<Zuteilbar>();
		while (it.hasNext()) {
			bundeslaender.add(it.next());
		}
		zt.setZuteilungsobjekte(bundeslaender);
		// Berechne die Zuteilungsergebnisse mittels des Zuteilungsverfahrens.
		// Als Zuteilungswert dienen die Einwohnerzahlen der Bundesländer.
		zt.berechneZuteilung();
		// Erneuere den Bundesländer-Iterator, um erneut über die Länder
		// iterieren zu können
		it = this.getBundestagswahl().bundeslaenderIterator();
		while (it.hasNext()) {
			Bundesland bl = it.next();
			// Setze im aktuell betrachteten Bundesland das vorläufige
			// Sitzkontingent. Dieses ergibt sich als Differenz aus dem
			// Zuteilungsergebnis (im
			// Standardfall: das Zuteilungsergebnis nach Anwendung des
			// Sainte-Lague-Verfahrens) und den Wahlkreisgewinnern dieses
			// Bundeslandes, die auf keiner erfolgreichen Landesliste stehen.
			// Beachte: Das so gesetzte Sitzkontingent muss positiv sein.
			bl.setErstesSitzkontigent(zt.getZuteilungsergebnis(bl)
					- wahlkreisgewinnerOhneErfolgreicheLandesliste.get(bl)
							.size());
			assert (bl.getErstesSitzkontigent() >= 0);
		}
	}

	/**
	 * Überprüft, ob die im Bundeswahlgesetz verankerte Mehrheitsklausel
	 * angewendet werden muss und führt diese ggf. aus.
	 */
	private void ueberpruefeMehrheitsklausel() {
		// Berechne die Zahl an Sitzen, die einer Partei, die mehr als die
		// Hälfte
		// der Zweitstimmen erhält, nach der Mehrheitsklausel zusteht. Nach
		// Wahlrecht ist dies ein Sitz mehr als die Hälfte - sollte die Hälfte
		// keine natürliche Zahl sein, ist nicht spezifiziert, wie
		// "ein Sitz mehr als die Hälfte" zu interpretieren ist.
		int mehrheitssitzzahl = (int) Math.ceil(((double) this
				.getBundestagswahl().getSitzzahl() / (double) 2) + 1);
		Iterator<Partei> parteienIt = this.getBundestagswahl()
				.parteienIterator();
		// Berechne die Zahl an Stimmen, die eine Partei erhalten muss, damit
		// die Mehrheitsklausel anwendung findet. Hier werden eigentlich alle
		// Zweitstimmen berücksichtigt, also auch solche von Wählern, die ihre
		// Erststimmen für einen erfolgreichen Kandidaten abgegeben haben -
		// diese Daten liegen allerdings nicht vor; vereinfacht wird deshalb die
		// gleiche Stimmbasis wie bei der restlichen Berechnung angenommen.
		int mehrheitsstimmzahl = (int) Math.ceil((double) this
				.getBundestagswahl().berechneZweitstimmen() / (double) 2);
		// Iteriere über Parteien, prüfe, ob Mehrheitsklausel angewendet werden
		// muss.
		while (parteienIt.hasNext()) {
			Partei partei = parteienIt.next();
			if (partei.berechneZweitstimmen() >= mehrheitsstimmzahl) {
				if (partei.getOberverteilungSitzzahl() < mehrheitssitzzahl) {
					// Erhöhe Sitzzahl im Bundestag entsprechend.
					this.getBundestagswahl()
							.setSitzzahl(
									(this.getBundestagswahl().getSitzzahl() + mehrheitssitzzahl)
											- partei.getOberverteilungSitzzahl());
					partei.setOberverteilungSitzzahl(mehrheitssitzzahl);
					// Berechne die Unterverteilung neu - eigentlich müsste nur
					// die Unterverteilung der Partei mit der
					// Zweitstimmenmehrheit neu berechnet werden, da dieser Fall
					// aber selten eintritt, ist eine Optimierung an dieser
					// Stelle nicht sinnvoll und würde den Code unnötig
					// unübersichtlicher machen.
					this.berechneUnterverteilung();
				}
			}
		}
	}

	/**
	 * Die Methode erhöht die Oberverteilungssitzzahlen um die Wahlkreisgewinner
	 * ohne erfolgreiche Landesliste, falls diese einer Partei angehören.
	 */
	private void ueberpruefeWahlkreisgewinnerOhneErfolgreicheLandesliste() {
		// Setze die Zahl an parteilosen Mandatsträgern im Bundesgebiet zurück.
		this.getBundestagswahl().setParteiloseMandatstraeger(0);
		// Iteriere über alle Bundesländer.
		Iterator<Bundesland> blIt = this.getBundestagswahl()
				.bundeslaenderIterator();
		while (blIt.hasNext()) {
			Bundesland bl = blIt.next();
			// Setze die Zahl parteiloser Mandatsträger in diesem Bundesland
			// zurück.
			bl.setParteiloseMandatstraeger(0);
			// Betrachte alle Wahlkreisgewinner ohne erfolgreiche Landesliste im
			// jeweiligen Bundesland.
			for (Kreiswahlvorschlag kwv : wahlkreisgewinnerOhneErfolgreicheLandesliste
					.get(bl)) {
				// Erhöhe die Sitzzahl um den bisher unberücksichtigten
				// Wahlkreisgewinner.
				this.getBundestagswahl().setSitzzahl(
						this.getBundestagswahl().getSitzzahl() + 1);
				Partei partei = kwv.getPartei();
				// Falls der Wahlkreisgewinner ohne erfolgreiche Landesliste
				// einer Partei angehört, erhöhe die Oberverteilungssitzzahl
				// dieser Partei um 1.
				if (partei != null) {
					partei.setOberverteilungSitzzahl(partei
							.getOberverteilungSitzzahl() + 1);
					Landesliste ll = partei.getLandesliste(kwv.getWahlkreis()
							.getBundesland());
					if (ll != null) {
						ll.setUnterverteilungSitzzahl(ll
								.getUnterverteilungSitzzahl() + 1);
					}
				} else {
					// KWV ist parteilos.
					bl.setParteiloseMandatstraeger(bl
							.getParteiloseMandatstraeger() + 1);
					this.getBundestagswahl().setParteiloseMandatstraeger(
							this.getBundestagswahl()
									.getParteiloseMandatstraeger() + 1);
				}
			}
		}
	}

	/**
	 * Überprüft, ob es bei der übergebenen {@link Landesliste} zu einer
	 * Listenerschöpfung kam und korrigiert ggf. die Unterverteilungssitzzahl
	 * entsprechend. Anschließend wird die Zahl an nicht in Anspruch genommenen
	 * Landeslistenplätzen zurückgegeben.
	 * 
	 * @param ll
	 *            die {@code Landesliste}, für die die Listenerschöpfung
	 *            ermittelt werden soll
	 * @return die Zahl an verloren gegenangenen Mandaten aufgrund von
	 *         Listenerschöpfung
	 */
	private int ueberprufeLandeslistenerschoepfung(Landesliste ll) {
		// diff: aufgetretene Landeslistenerschöpfung
		int diff = 0;
		// Landeslistenerschöpfung kann nur auftreten, wenn die
		// Unterverteilungssitzzahl größer als die Bewerberzahl der Landesliste
		// ist.
		if (ll.getUnterverteilungSitzzahl() > ll.getListengroesse()) {
			Iterator<Kandidat> mandateIt = ll.mandateIterator();
			// Bestimme die Zahl an Mandaten, die die Landeslistenpartei im
			// Bundesland der Landesliste tatsächlich erhält.
			int mandatszahl = 0;
			// Ermittle Mandatsträger, die über die Landesliste einziehen.
			while (mandateIt.hasNext()) {
				mandatszahl++;
				mandateIt.next();
			}
			// Erhöhe um die Direktmandatsträger
			mandatszahl += ll.getPartei().wahlkreisgewinnerInBundesland(
					ll.getBundesland());
			assert (mandatszahl <= ll.getUnterverteilungSitzzahl());
			// Bestimme die aufgetretene Landeslistenerschoepfung
			diff = ll.getUnterverteilungSitzzahl() - mandatszahl;
			ll.setUnterverteilungSitzzahl(mandatszahl);
		}
		return diff;
	}

	/**
	 * Wertet die Zweitstimmen entsprechend dem Bundeswahlgesetz 2013 aus und
	 * speichert die berechneten Zwischenergebnisse in den entsprechenden
	 * Objekten der gesetzten Bundestagswahl.
	 */
	@Override
	protected void werteZweitstimmenAus() {
		this.ueberpruefeSperrklausel();
		this.berechneWahlkreisgewinnerOhneErfolgreicheLandesliste();
		this.setzeErsteSitzkontingente();
		this.berechneOberverteilung();
		this.berechneUnterverteilung();
		this.ueberpruefeWahlkreisgewinnerOhneErfolgreicheLandesliste();
		this.ueberpruefeMehrheitsklausel();
	}

}
