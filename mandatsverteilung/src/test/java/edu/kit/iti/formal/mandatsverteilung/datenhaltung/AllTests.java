package edu.kit.iti.formal.mandatsverteilung.datenhaltung;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ KandidatTest.class, WahlkreisTest.class,
		KreiswahlvorschlagTest.class, LandeslisteTest.class,
		DatenhaltungTest.class, ParteiTest.class, BerechnungsoptionenTest.class })
public class AllTests {

}
