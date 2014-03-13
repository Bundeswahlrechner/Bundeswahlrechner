package edu.kit.iti.formal.mandatsverteilung.wahlberechnung;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;

/**
 * Erzeugt einen Verbund aus benutzerdefiniertem Wahlverfahren mit
 * benutzerdefinierter Sperrklausel und benutzerdefiniertem Zuteilungsverfahren
 */
public class BenutzerdefinierteFabrikMitSperrklauselpruefer extends
		BerechnungsfabrikMitSperrklauselpruefer {

	private static final Map<String, Class<?>> wahlverfahrenMap;
	private static final Map<String, Class<?>> zuteilungsverfahrenMap;

	static {
		Map<String, Class<?>> map = new TreeMap<String, Class<?>>();
		map.put("Bundeswahlgesetz 2013", Bundeswahlgesetz2013.class);
		map.put("Variante ohne Ausgleichsmandate",
				BundeswahlgesetzOhneAusgleichsmandate.class);
		wahlverfahrenMap = Collections.unmodifiableMap(map);
	}
	static {
		Map<String, Class<?>> map = new TreeMap<String, Class<?>>();
		map.put("Sainte-Lague Höchstzahl", SainteLagueHoechstzahl.class);
		map.put("Sainte-Lague Rangmaßzahl", SainteLagueRangmasszahl.class);
		map.put("Sainte-Lague Iterativ", SainteLagueIterativ.class);
		map.put("D'Hondt", DHondt.class);
		map.put("Hare-Niemeyer", HareNiemeyer.class);
		zuteilungsverfahrenMap = Collections.unmodifiableMap(map);
	}

	/**
	 * Verfügbare Wahlverfahren für die Anzeige gegenüber dem Benutzer
	 * 
	 * @return verfügbare Wahlverfahren
	 */
	public static Set<String> verfuegbareWahlverfahren() {
		return wahlverfahrenMap.keySet();
	}

	/**
	 * Verfügbare Zuteilungsverfahren für die Anzeige gegenüber dem Benutzer
	 * 
	 * @return verfügbare Zuteilungsverfahren
	 */
	public static Set<String> verfuegbareZuteilungsverfahren() {
		return zuteilungsverfahrenMap.keySet();
	}

	/**
	 * Die Parameter, die vom Benutzer in der GUI ausgewählt werden
	 */
	private final int grundmandate;
	private final double prozenthuerde;
	private final String wahlverfahren;
	private final String zuteilungsverfahren;

	/**
	 * @param prozenthuerde
	 *            Prozenthürde
	 * @param grundmandate
	 *            Grundmandatsanzah
	 * @param wahlverfahren
	 *            Wahlverfahren-Name gemäß listeWahlverfahren()
	 * @param zuteilungsverfahren
	 *            Zuteilungsverfahren gemäß listeZuteilungsverfahren()
	 */
	public BenutzerdefinierteFabrikMitSperrklauselpruefer(double prozenthuerde,
			int grundmandate, String wahlverfahren, String zuteilungsverfahren) {
		super();
		this.prozenthuerde = prozenthuerde;
		this.grundmandate = grundmandate;
		this.wahlverfahren = wahlverfahren;
		this.zuteilungsverfahren = zuteilungsverfahren;
	}

	@Override
	public WahlverfahrenMitSperrklausel erstelleBerechnungsverbund(
			Bundestagswahl bundestagswahl) {
		Zuteilungsverfahren zuteilungsverfahren = this
				.erstelleZuteilungsverfahren();
		WahlverfahrenMitSperrklausel wahlverfahren = this
				.erstelleWahlverfahren(bundestagswahl, zuteilungsverfahren);
		Sperrklauselpruefer sperrklauselpruefer = this
				.erstelleSperrklauselpruefer(bundestagswahl);
		wahlverfahren.setzeSperrklauselpruefer(sperrklauselpruefer);
		return wahlverfahren;
	}

	@Override
	protected Sperrklauselpruefer erstelleSperrklauselpruefer(
			Bundestagswahl bundestagswahl) {
		BundeswahlgesetzSperrklauselpruefer skp = new BundeswahlgesetzSperrklauselpruefer(
				prozenthuerde, grundmandate);
		skp.setGesamteZweitstimmenzahl(bundestagswahl.berechneZweitstimmen());
		return skp;
	}

	@Override
	protected WahlverfahrenMitSperrklausel erstelleWahlverfahren(
			Bundestagswahl bundestagswahl,
			Zuteilungsverfahren zuteilungsverfahren) {

		Class<?> WahlverfahrenKlasse = wahlverfahrenMap.get(wahlverfahren);
		WahlverfahrenMitSperrklausel gewaehltesVerfahren = null;
		try {
			Class<?>[] params = { Bundestagswahl.class,
					Zuteilungsverfahren.class };
			Constructor<?> constructor = WahlverfahrenKlasse
					.getConstructor(params);
			gewaehltesVerfahren = (WahlverfahrenMitSperrklausel) (constructor
					.newInstance(bundestagswahl, zuteilungsverfahren));
		} catch (InstantiationException | IllegalAccessException
				| NoSuchMethodException | SecurityException
				| IllegalArgumentException | InvocationTargetException
				| NullPointerException e) {
			throw new IllegalStateException(
					"Wahlverfahren konnte nicht erzeugt werden.");
		}
		return gewaehltesVerfahren;
	}

	@Override
	protected Zuteilungsverfahren erstelleZuteilungsverfahren() {
		Class<?> ZuteilungsverfahrenKlasse = zuteilungsverfahrenMap
				.get(zuteilungsverfahren);
		Zuteilungsverfahren gewaehltesVerfahren = null;
		try {
			gewaehltesVerfahren = (Zuteilungsverfahren) (ZuteilungsverfahrenKlasse
					.newInstance());
		} catch (InstantiationException | IllegalAccessException
				| NullPointerException e) {
			throw new IllegalStateException(
					"Zuteilungsverfahren konnte nicht erzeugt werden.");
		}
		return gewaehltesVerfahren;
	}
}
