package edu.kit.iti.formal.mandatsverteilung;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
		edu.kit.iti.formal.mandatsverteilung.datenhaltung.AllTests.class,
		edu.kit.iti.formal.mandatsverteilung.wahlberechnung.AllTests.class,
		edu.kit.iti.formal.mandatsverteilung.exporter.CSVExporterTest.class,
		edu.kit.iti.formal.mandatsverteilung.importer.AllTests.class,
		edu.kit.iti.formal.mandatsverteilung.generierer.AllTests.class })
public class AllTestsExceptGUI {
}
