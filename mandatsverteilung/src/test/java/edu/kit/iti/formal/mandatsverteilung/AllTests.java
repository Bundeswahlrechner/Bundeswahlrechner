package edu.kit.iti.formal.mandatsverteilung;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AllTestsExceptGUI.class,
		edu.kit.iti.formal.mandatsverteilung.gui.AllTests.class })
public class AllTests {

}
