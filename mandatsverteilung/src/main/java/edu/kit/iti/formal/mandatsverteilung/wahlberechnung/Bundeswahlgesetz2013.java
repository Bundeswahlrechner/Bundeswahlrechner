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
 * Zwischen- und Endergebnisse nach dem Bundeswahlgesetz 2013 unter Verwendung
 * eines {@link Zuteilungsverfahren}s und eines {@link Sperrklauselpruefer}s.
 * Die ermittelten Ergebnisse werden in der zu Grunde gelegten
 * {@code Bundestagswahl} gespeichert.
 * 
 * @author tillneuber
 * @version 1.4
 * 
 */
public class Bundeswahlgesetz2013 extends WahlverfahrenMitSperrklausel {

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
	public Bundeswahlgesetz2013(Bundestagswahl bundestagswahl,
			Zuteilungsverfahren zuteilungsverfahren) {
		super(bundestagswahl, zuteilungsverfahren);
	}

	/**
	 * Berechnet in jedem Bundesland die Pseudoverteilungssitze, die auf die
	 * Landeslisten der Parteien entfallen, die die Sperrklausel überwunden
	 * haben und setzt das entsprechende Attribut.
	 */
	private void berechneLandeslistensitze() {
		Iterator<Bundesland> it = this.getBundestagswahl()
				.bundeslaenderIterator();
		Zuteilungsverfahren zt = this.getZuteilungsverfahren();
		// Iteriere über alle Bundesländer
		while (it.hasNext()) {
			Bundesland bl = it.next();
			// Setze das erste Sitzkontingent im aktuell betrachteten Bundesland
			// als Zuteilungskontingent
			zt.setZuteilungskontingent(bl.getErstesSitzkontigent());
			// Baue Liste mit Landeslisten von Parteien auf, die die
			// Sperrklausel überwunden haben; iteriere hierzu über alle
			// Landeslisten des Bundeslandes, überprüfe ob die zugehörige Partei
			// die Sperrklausel überwunden hat und füge die Landesliste ggf. der
			// Liste "erfolgreicheLandeslisten" hinzu.
			Iterator<Landesliste> landeslistenIt = bl.landeslistenIterator();
			List<Landesliste> erfolgreicheLandeslisten = new LinkedList<>();
			while (landeslistenIt.hasNext()) {
				Landesliste ll = landeslistenIt.next();
				// Überprüfe, ob die zur Landesliste gehörende Partei die
				// Sperrklausel überwunden hat.
				if (ll.getPartei().isSperrklauselUeberwunden()) {
					erfolgreicheLandeslisten.add(ll);
				}
			}
			// Setze die erfolgreichen Landeslisten als Zuteilungsobjekte - nur
			// auf diese Landeslisten entfallen Pseudoverteilungssitze
			zt.setZuteilungsobjekte(erfolgreicheLandeslisten);
			zt.berechneZuteilung();
			// Iteriere über die erfolgreichen Landeslisten und setze die
			// Ergebnisse des Zuteilungsverfahrens als Pseudoverteilungssitzzahl
			for (Landesliste ll : erfolgreicheLandeslisten) {
				ll.setPseudoverteilungSitzzahl(zt.getZuteilungsergebnis(ll));
			}
		}
	}

	/**
	 * Berechnet und setzt die Mindessitzzahlen die auf eine Partei entfallen
	 * ausgehend von den Pseudoverteilungssitzzahlen der Landeslisten und den
	 * Wahlkreisgewinnern.
	 */
	private void berechneMindestsitzzahlen() {
		Iterator<Partei> it = this.getBundestagswahl().parteienIterator();
		// Iteriere über alle Parteien und bestimme jeweils die Mindessitzzahl
		while (it.hasNext()) {
			Partei partei = it.next();
			// Mindessitzzahlen müssen nur für Parteien errechnet werden,
			// die die Sperrklausel überwunden haben.
			if (partei.isSperrklauselUeberwunden()) {
				// Iteriere über alle Bundesländer
				Iterator<Bundesland> bundeslandIt = this.getBundestagswahl()
						.bundeslaenderIterator();
				int mindestsitzzahl = 0;
				while (bundeslandIt.hasNext()) {
					Bundesland bl = bundeslandIt.next();
					// Überprüfe, ob die Partei im aktuell betrachteten
					// Bundesland eine Landesliste eingreicht hat - dies ist
					// nicht immer gegeben.
					if (partei.getLandesliste(bl) != null) {
						// Erhöhe die bisher errechnete Mindessitzzahl um das
						// Maximum aus Pseudoverteilungssitzen der Landesliste
						// der aktuellen Partei im aktuellen Bundesland und den
						// Wahlkreisgewinnern der aktuellen Partei im aktuellen
						// Bundesland.
						mindestsitzzahl += Math.max(partei.getLandesliste(bl)
								.getPseudoverteilungSitzzahl(), partei
								.wahlkreisgewinnerInBundesland(bl));
					}
				}
				// Setze die so bestimmte Mindessitzzahl - oder 0, wenn die
				// Partei die Sperrklausel nicht überwunden hat - als
				// Mindessitzzahl der aktuellen Partei.
				partei.setMindestsitzzahl(mindestsitzzahl);
			}
		}
	}

	/**
	 * Berechnet die Oberverteilungsitzzahl jeder Partei nach dem
	 * Bundeswahlgesetz 2013.
	 */
	private void berechneOberverteilung() {
		if (this.getZuteilungsverfahren() instanceof SainteLagueIterativ
				|| this.getZuteilungsverfahren() instanceof SainteLagueHoechstzahl
				|| this.getZuteilungsverfahren() instanceof SainteLagueRangmasszahl) {
			// Deutlich schnellere Berechnungsvariante für SainteLague-Verfahren
			Iterator<Partei> parteienIt = this.getBundestagswahl()
					.parteienIterator();
			// Bestimme den Divisor zur Bestimmung der Oberverteilungssitze.
			double divisor = Double.MAX_VALUE;
			while (parteienIt.hasNext()) {
				Partei partei = parteienIt.next();
				if (partei.isSperrklauselUeberwunden()) {
					// Formel zur Bestimmung des Bundesdivisors nach
					// http://www.wahlrecht.de/bundestag/index.htm [Zugirff:
					// 29.01.14]. Prüfe ob Mindessitzzahl positiv, um negativen
					// Divisor zu verhindern.
					if (partei.getMindestsitzzahl() > 0) {
						divisor = Math.min(
								divisor,
								partei.berechneZweitstimmen()
										/ (partei.getMindestsitzzahl() - 0.5));
					}
				}
			}
			parteienIt = this.getBundestagswahl().parteienIterator();
			// Berechne die Gesamtzahl der verteilten Sitze; dies entspricht der
			// Bundestagsgröße.
			int gesamtsitzzahl = 0;
			while (parteienIt.hasNext()) {
				Partei partei = parteienIt.next();
				if (partei.isSperrklauselUeberwunden()) {
					partei.setOberverteilungSitzzahl((int) Math.round(partei
							.berechneZweitstimmen() / divisor));
					// Erhöhe die Gesamtzahl der verteilten Sitze um die an
					// diese Partei verteilten Sitze.
					gesamtsitzzahl += partei.getOberverteilungSitzzahl();
				} else {
					// Partei hat die Sperrklausel nicht überwunden.
					partei.setOberverteilungSitzzahl(0);
				}
			}
			// Setze die Bundestagsgröße.
			this.getBundestagswahl().setSitzzahl(gesamtsitzzahl);
		} else {
			// Verfahren ist keine SainteLague-Variante
			// Lokale Kopie der Sitzanzahl im Bundestag für die Berechnung.
			int btGroesse = this.getBundestagswahl().getSitzzahl();
			// Die Bundestagsgröße und die entsprechenden
			// Oberverteilungssitzzahlen
			// werden im Folgenden durch das Verfahren der schrittweisen
			// Erhöhung
			// der Bundestagsgröße bestimmt.
			// Bestimme zunächst die initiale Bundestagsgröße. Ziehe hierzu von
			// der
			// inititalen Größe (2 * Wahlkreisanzahl; im Standardfall also 598)
			// die
			// Anzahl der Wahlkreisgewinner ohne erfolgreiche Landesliste ab.
			// Beachte: Die tatsächlich benötigte Bundestagsgröße ist nun immer
			// größer oder gleich der so bestimmten, initialen Größe.
			Iterator<Bundesland> blIt = this.getBundestagswahl()
					.bundeslaenderIterator();
			while (blIt.hasNext()) {
				btGroesse -= wahlkreisgewinnerOhneErfolgreicheLandesliste.get(
						blIt.next()).size();
			}
			// Nehme zunächst an, dass die Bundestagsgröße ausreicht - sollte
			// sich
			// während der nun folgenden Verteilung herausstellen, dass dies
			// nicht
			// der Fall ist (eine Partei erhält weniger Sitze als ihr nach
			// Mindessitzzahl zusteht) wird die Variable auf false gesetzt.
			boolean btGroesseAusreichend = true;
			do {
				Zuteilungsverfahren zt = this.getZuteilungsverfahren();
				// Die aktuell Bundestagsgröße wird auf die Parteien verteilt,
				// setze
				// sie also als Kontingent.
				zt.setZuteilungskontingent(btGroesse);
				Iterator<Partei> parteiIt = this.getBundestagswahl()
						.parteienIterator();
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
				// Gehe davon aus, dass die Bundestagsgröße ausreicht und
				// überprüfe,
				// ob dies tatsächlich der Fall ist.
				btGroesseAusreichend = true;
				// Schleife kann abgebrochen werden, wenn sich die
				// Bundestagsgröße
				// als zu klein herausstellt.
				for (Partei partei : erfolgreicheParteien) {
					partei.setOberverteilungSitzzahl(zt
							.getZuteilungsergebnis(partei));
					if (zt.getZuteilungsergebnis(partei) < partei
							.getMindestsitzzahl()) {
						btGroesseAusreichend = false;
						// Größe reicht nicht aus; erhöhe Bundestagsgröße für
						// nächsten Schleifendurchlauf
						btGroesse++;
						break;
					}
				}
			} while (!btGroesseAusreichend);
			// Setze im Bundestag die bestimmte Bundestagsgröße als Sitzzahl
			this.getBundestagswahl().setSitzzahl(btGroesse);

		}
	}

	/**
	 * Die Methode verteilt die Obverteilungssitze parteiintern auf Landeslisten
	 * nach dem im Bundeswahlgesetz festgelegten Verfahren.
	 */
	private void berechneUnterverteilung() {
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
				// Gibt die Differenz zwischen der zu verteilenden
				// Oberverteilungssitzzahl und den momentan im Rahmen der
				// Berechnung verteilten Sitzen. Die Unterverteilung ist
				// beendet, wenn die Differenz 0 beträgt.
				int diff;
				// Berechne die aktuelle Zahl von Landeslistenplätzen, die
				// aufgrund von fehlenden Kandidaten nicht in Anspruch
				// genommen werden.
				int aktuelleListenerschoepfung = 0;
				// verteilteSitze gibt die bisher auf Landeslisten
				// verteilten Sitze an
				int verteilteSitze = 0;
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
				// Gibt den aktuellen Iterationsschritt an.
				int iteration = 0;
				do {
					// Neuer Iterationsschritt: Setze verteilteSitze und
					// aktuelleLandeslistenerschoepfung zurück.
					verteilteSitze = 0;
					aktuelleListenerschoepfung = 0;
					Zuteilungsverfahren verfahren = this
							.getZuteilungsverfahren();
					verfahren.setZuteilungskontingent(partei
							.getOberverteilungSitzzahl() - iteration);
					verfahren.setZuteilungsobjekte(parteiLandeslisten);
					verfahren.berechneZuteilung();
					Iterator<Bundesland> blIt = this.getBundestagswahl()
							.bundeslaenderIterator();
					while (blIt.hasNext()) {
						Bundesland bl = blIt.next();
						Landesliste ll = partei.getLandesliste(bl);
						if (ll != null) {
							// Berechne die Sitzzahl als Maximum aus
							// Zuteilungsergebnis (Sitze durch Zweitstimmen) und
							// Direktmandaten im aktuellen Bundesland.
							int sitze = Math.max(
									partei.wahlkreisgewinnerInBundesland(bl),
									verfahren.getZuteilungsergebnis(ll));
							// Setze die so berechnete Sitzzahl als
							// Unterverteilungssitzzahl - ggf. wird dieses
							// Ergebnis später überschrieben.
							ll.setUnterverteilungSitzzahl(sitze);
							// Erhöhe die bisher insgesamt verteilten
							// Unterverteilungssitze um die Sitze, die im
							// Bundesland der aktuellen Landesliste vergeben
							// wurden.
							verteilteSitze += sitze;
							// Prüfe, ob die Landesliste genügend Kandidaten hat
							// - korrigiere ggf.
							aktuelleListenerschoepfung += this
									.ueberprufeLandeslistenerschoepfung(ll);
						}
					}
					// Bestimme die Differenz zwischen Oberverteilungssitzen und
					// tatsächlich verteilten Sitzen. Beachte: Diese ist immer
					// <= 0, da initital entweder zu viele Sitze verteilt wurden
					// (d.h. in mind. einem Bundesland gab es Überhangmandate)
					// oder die Zahl der verteilten Sitze gerade der
					// Oberverteilungssitzzahl entspricht. Diese Invariante
					// bleibt auch in den nächsten Iteration erhalten, da von
					// Schritt zu Schritt höchstens 1 Sitz weniger verteilt
					// wird (ergibt sich, da das Zuteilungskontingent des
					// Zuteilungsverfahrens in jedem Schritt um 1 erniedrigt
					// wird) - dies garantiert, dass der Algorithmus immer
					// terminiert.
					diff = partei.getOberverteilungSitzzahl() - verteilteSitze;
					// Überprüfe ob die Invariante diff <= 0 verletzt ist -
					// falls ja, ist die Verwendung des gesetzten
					// Zuteilungsverfahren mit den aktuellen Daten nicht möglich
					// (siehe hierzu: Alabama-Paradoxon)
					if (diff > 0) {
						throw new IllegalArgumentException(
								"Das Auftreten des Alabama-Paradoxons bei der Unterverteilungsberechnung der Partei "
										+ partei.getName()
										+ " macht eine Wahlberechnung unmöglich.\n"
										+ "Ändern Sie das Zuteilungsverfahren oder die Stimmdaten.");
					}
					// Springe um |diff|-Iterationsschritte weiter. In jedem
					// Iterationsschritt wird höchstens 1 Sitz weniger verteilt,
					// gehe deshalb unmittelbar |diff|-Schritt weiter. (diff
					// wird abgezogen, da diff <= 0, s.o.)
					iteration -= diff;
				} while (diff != 0);
				// Ziehe von der Oberverteilungsitzzahl die Zahl an
				// Landeslistenplätzen ab, die aufgrund von Listenerschöpfung
				// unbesetzt bleiben.
				partei.setOberverteilungSitzzahl(partei
						.getOberverteilungSitzzahl()
						- aktuelleListenerschoepfung);
				listenerschoepfung += aktuelleListenerschoepfung;
			} else {
				// Partei hat die Sperrklausel nicht überwunden.
				Iterator<Bundesland> blIt = this.getBundestagswahl()
						.bundeslaenderIterator();
				while (blIt.hasNext()) {
					Landesliste ll = partei.getLandesliste(blIt.next());
					if (ll != null) {
						// Setze Unterverteilung auf 0 - wird bei Neuberechnung
						// von bereits berechneten Daten benötigt.
						ll.setUnterverteilungSitzzahl(0);
					}
				}
			}
		}
		this.getBundestagswahl().setSitzzahl(
				this.getBundestagswahl().getSitzzahl() - listenerschoepfung);
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
	public Bundeswahlgesetz2013 clone() {
		Bundeswahlgesetz2013 result = new Bundeswahlgesetz2013(
				this.getBundestagswahl(), this.getZuteilungsverfahren().clone());
		result.setzeSperrklauselpruefer(spk.clone());
		return result;
	}

	/**
	 * Bestimmt, welche Mandatsträger über ein Direktmandat in den Bundestag
	 * eingezogen sind und setzt bei den jeweiligen {@link Kandidat}en das
	 * entsprechende Attribut.
	 */
	private void setzeAusgleichsmandatstraeger() {
		// Iteriere über alle Parteien, die die Sperrklausel überwunden haben.
		Iterator<Partei> parteienIt = this.getBundestagswahl()
				.parteienIterator();
		while (parteienIt.hasNext()) {
			Partei partei = parteienIt.next();
			if (partei.isSperrklauselUeberwunden()) {
				// Iteriere über alle Bundesländer.
				Iterator<Bundesland> blIt = this.getBundestagswahl()
						.bundeslaenderIterator();
				// Berechne die Gesamtzahl der Ausgleichsmandate, die diese
				// Partei erhalten hat.
				int gesamteAgm = 0;
				while (blIt.hasNext()) {
					Bundesland bl = blIt.next();
					Landesliste ll = partei.getLandesliste(bl);
					// Prüfe, ob die aktuelle Partei in diesem Bundesland eine
					// Landesliste eingereicht hat.
					if (ll != null) {
						// Berechne die Zahl an Ausgleichsmandatsträgern, die
						// von dieser Landesliste kommen
						int agm = ll.getUnterverteilungSitzzahl()
								- Math.max(
										ll.getPseudoverteilungSitzzahl(),
										partei.wahlkreisgewinnerInBundesland(bl));
						// Erhöhe/erniedrige die Zahl, der von der aktuellen
						// Partei insgesamt errungenen Ausgleichsmandate um die
						// Zahl der in diesem Bundesland an die Partei
						// vergebenen Ausgleichsmandate.
						gesamteAgm += agm;
						// Falls es Ausgleichsmandatsträger gibt (also agm > 0
						// gilt): Iteriere über alle Abgeordneten, die von
						// dieser Landesliste kommen; die letzten agm Kandidaten
						// sind Ausgleichsmandatsträger
						if (agm > 0) {
							Iterator<Kandidat> mandatstraeger = ll
									.mandateIterator();
							// Kandidaten, die kein Ausgleichsmandat tragen
							// überspringen
							for (int i = 0; i < (ll
									.getUnterverteilungSitzzahl()
									- partei.wahlkreisgewinnerInBundesland(bl) - agm); i++) {
								mandatstraeger.next();
							}
							// ab dem nächsten Kandidaten bis zum letzten
							// Kandidaten der über die Landesliste einzieht
							// tragen alle ein Ausgleichsmandat
							while (mandatstraeger.hasNext()) {
								Kandidat ausgleichsmandatstraeger = mandatstraeger
										.next();
								// setze Attribut im Kandidaten
								ausgleichsmandatstraeger
										.setAusgleichsmandat(true);
							}
						}
					}
				}
				partei.setAusgleichsmandate(gesamteAgm);
			}
		}
	}

	/**
	 * Berechnet die, auf die Bundesländer entfallenden, ersten Sitzkontingente
	 * basierend auf den Einwohnerzahlen und speichert die Berechnungsergebnisse
	 * im jeweiligen Bundesland.
	 */
	private void setzeErsteSitzkontingente() {
		// Initiale Bundestagsgröße entspricht dem doppelten der Wahlkreisanzahl
		this.getBundestagswahl().setSitzzahl(
				this.getBundestagswahl().wahlkreisAnzahl() * 2);
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
		this.berechneLandeslistensitze();
		this.berechneMindestsitzzahlen();
		this.berechneOberverteilung();
		this.berechneUnterverteilung();
		this.ueberpruefeWahlkreisgewinnerOhneErfolgreicheLandesliste();
		this.setzeAusgleichsmandatstraeger();
		this.ueberpruefeMehrheitsklausel();
	}
}
