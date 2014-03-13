package edu.kit.iti.formal.mandatsverteilung.gui;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ BerechnungsOptionenFensterTest.class,
		EinstellungsFensterTest.class, GUITest.class,
		EinWahlDeutschlandAnsichtTest.class, EWAnsichtTest.class,
		ExportFensterTest.class, GeneriereWahldatenFensterTest.class,
		ImportFensterTest.class, MenuleisteTest.class,
		ModifikationsAnsichtTest.class, NWahltest.class, })
public class AllTests {

}
