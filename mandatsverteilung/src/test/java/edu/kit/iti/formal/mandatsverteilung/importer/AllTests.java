package edu.kit.iti.formal.mandatsverteilung.importer;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AssoziationsTest.class, BundeslandParserTest.class,
		ImporterTest.class, KandidatenParserTest.class,
		TestDatenBereitsteller.class, WahlkreiseParserTest.class })
public class AllTests {

}
