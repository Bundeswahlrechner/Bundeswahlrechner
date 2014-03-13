package edu.kit.iti.formal.mandatsverteilung.generierer;

import edu.kit.iti.formal.mandatsverteilung.gui.GeneriererBeobachter;

public interface GeneriererSubjekt {
    public void benachrichtigeBeobachter(String neueNachricht);

    public void entferneBeobachter(GeneriererBeobachter b);

    public void registriereBeobachter(GeneriererBeobachter b);
}
