package edu.kit.iti.formal.mandatsverteilung.wahlberechnung;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
		Bundeswahlgesetz2013BundeswahlleiterDatenTest.class,
		Bundeswahlgesetz2013OhneAusgleichsmandateBundeswahlleiterDatenTest.class,
		Bundeswahlgesetz2013ZuteilungsverfahrenTest.class,
		SperrklauselTest.class, DHondtRandomisierterTest.class,
		DHondtTest.class, HareNiemeyerRandomisierterTest.class,
		HareNiemeyerTest.class, SainteLagueRandomisierterTest.class,
		SainteLagueTest.class,
		BundeswahlgesetzOhneAusgleichsmandateFabrikTest.class,
		BenutzerdefinierteFabrikMitSperrklauselprueferTest.class })
public class AllTests {

}
