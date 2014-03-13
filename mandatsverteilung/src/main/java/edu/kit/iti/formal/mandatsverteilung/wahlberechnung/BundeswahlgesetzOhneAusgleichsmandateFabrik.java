package edu.kit.iti.formal.mandatsverteilung.wahlberechnung;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;

/**
 * Erzeugt einen Verbund aus BundeswahlgesetzOhneAusgleichsmandate mit passendem
 * Sperrklauselprüfer und Zuteilungsverfahren
 */
public class BundeswahlgesetzOhneAusgleichsmandateFabrik extends
		BerechnungsfabrikMitSperrklauselpruefer {

	@Override
	protected Sperrklauselpruefer erstelleSperrklauselpruefer(
			Bundestagswahl bundestagswahl) {
		BundeswahlgesetzSperrklauselpruefer skp = new BundeswahlgesetzSperrklauselpruefer();
		skp.setGesamteZweitstimmenzahl(bundestagswahl.berechneZweitstimmen());
		return skp;
	}

	@Override
	protected WahlverfahrenMitSperrklausel erstelleWahlverfahren(
			Bundestagswahl bundestagswahl,
			Zuteilungsverfahren zuteilungsverfahren) {
		return new BundeswahlgesetzOhneAusgleichsmandate(bundestagswahl,
				zuteilungsverfahren);
	}

	@Override
	protected Zuteilungsverfahren erstelleZuteilungsverfahren() {
		return new SainteLagueHoechstzahl();
	}
}
