/**
 * 
 */
package edu.kit.iti.formal.mandatsverteilung.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Berechnungsoptionen;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.wahlberechnung.BenutzerdefinierteFabrikMitSperrklauselpruefer;
import edu.kit.iti.formal.mandatsverteilung.wahlberechnung.Berechnungsfabrik;
import edu.kit.iti.formal.mandatsverteilung.wahlberechnung.Bundeswahlgesetz2013Fabrik;
import edu.kit.iti.formal.mandatsverteilung.wahlberechnung.BundeswahlgesetzOhneAusgleichsmandateFabrik;
import edu.kit.iti.formal.mandatsverteilung.wahlberechnung.Wahlverfahren;
import edu.kit.iti.formal.mandatsverteilung.wahlberechnung.Zuteilungsverfahren;

/**
 * Ein Fenster, in dem der Benutzer verschiedene Einstellungen für die
 * Berechnung einer {@link Bundestagswahl} vornehmen kann.
 */
public class BerechnungsOptionenFenster extends JDialog {

    /**
     * Namen für die Berechnungs-Fabriken. Werden auch von den
     * Berechnungsoptionen verwendet, daher öffentlich.
     */
    public static final String benutzerdefiniertFabrikName = "Benutzerdefiniert";
    public static final String bundeswahlgesetz2013FabrikName = "Bundeswahlgesetz 2013";
    public static final String bundeswahlgesetzOhneAusgleichsmandateFabrikName = "Bundeswahlgesetz ohne Ausgleichsmandate";

    /**
     * see java.io.Serializable
     */
    private static final long serialVersionUID = 1L;

    /**
     * JComboBox, in der die {@link Berechnungsfabrik} ausgewählt werden kann.
     */
    public JComboBox<String> fabrikAuswahl;

    /**
     * JSpinner, in dem die Höhe der Grundmandatsklausel eingestellt werden kann
     */
    public JSpinner grundmandateAuswahl;

    /**
     * Die zugehörige {@linkGUI}
     */
    private final GUI gui;

    /**
     * Button zur Neuberechnung
     */
    protected JButton neuBerechnenButton;

    /**
     * JSpinner, in dem die Höhe der Prozenthürde eingestellt werden kann
     */
    public JSpinner prozenthuerdeAuswahl;

    /**
     * JComboBox, in der das {@link Wahlverfahren} ausgewählt werden kann.
     */
    public JComboBox<String> wahlverfahrenAuswahl;

    /**
     * JComboBox, in der das {@link Zuteilungsverfahren} ausgewählt werden kann.
     */
    public JComboBox<String> zuteilungsverfahrenAuswahl;

    /**
     * Konstruktor für gespeicherte Optionen, die geladen werden
     * 
     * @param gui
     *            zugehörige {@link GUI}
     */
    public BerechnungsOptionenFenster(GUI gui, boolean modal) {
        super(gui, modal);
        this.gui = gui;
        fensterInitialisieren();
        komponentenHinzufuegen();
        berechnungsoptionenWiederherstellen();
        fensterAnzeigen();
    }

    /**
     * Speichert die in der GUI gewählten Berechnungsoptionen in der Wahl ab.
     */
    private void berechnungsoptionenSpeichern() {
        String fabrik = (String) fabrikAuswahl.getSelectedItem();
        String wahlverfahren = (String) wahlverfahrenAuswahl.getSelectedItem();
        String zuteilungsverfahren = (String) zuteilungsverfahrenAuswahl
                .getSelectedItem();
        int grundmandate = (int) grundmandateAuswahl.getValue();
        double prozenthuerde = (double) prozenthuerdeAuswahl.getValue();
        Berechnungsoptionen berechnungsoptionen = new Berechnungsoptionen(
                fabrik, grundmandate, prozenthuerde, wahlverfahren,
                zuteilungsverfahren);
        this.wahl().setBerechnungsoptionen(berechnungsoptionen);
    }

    /**
     * Setzt der GUI-Elemente gemäß den in der Wahl gespeicherten
     * Berechnungsoptionen.
     */
    private void berechnungsoptionenWiederherstellen() {
        // Berechnungsoptionen der aktuellen Wahl auslesen
        Berechnungsoptionen berechnungsoptionen = this.wahl()
                .getBerechnungsoptionen();
        // GUI an Berechnungsoptionen anpassen
        fabrikAuswahl.setSelectedItem(berechnungsoptionen.getFabrik());
        wahlverfahrenAuswahl.setSelectedItem(berechnungsoptionen
                .getWahlverfahren());
        zuteilungsverfahrenAuswahl.setSelectedItem(berechnungsoptionen
                .getZuteilungsverfahren());
        prozenthuerdeAuswahl.setValue(berechnungsoptionen.getProzenthuerde());
        grundmandateAuswahl.setValue(berechnungsoptionen.getGrundmandate());
    }

    private void fensterAnzeigen() {

        // Fenster beim Schließen löschen
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Fenstergröße anpassen
        this.pack();

        // Fenster sichtbar machen
        this.setVisible(true);
    }

    private void fensterInitialisieren() {
        // Titel des Fenster festlegen
        this.setTitle("Berechnungsoptionen");

        // Mindestgröße des Fensters festlegen
        this.setMinimumSize(new Dimension(350, 200));
    }

    private void komponentenHinzufuegen() {
        // Auswahl der Fabrik
        final JLabel fabrikLabel = new JLabel("Vorlagen: ");
        fabrikAuswahl = new JComboBox<String>();
        fabrikAuswahl.addItem(bundeswahlgesetz2013FabrikName);
        fabrikAuswahl.addItem(bundeswahlgesetzOhneAusgleichsmandateFabrikName);
        fabrikAuswahl.addItem(benutzerdefiniertFabrikName);
        fabrikAuswahl.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ist die aktuell ausgewählte Fabrik die benutzerdefinierte ?
                boolean benutzerdefiniert = fabrikAuswahl.getSelectedItem()
                        .equals(benutzerdefiniertFabrikName);
                // je nachdem weitere Optionen (nicht) auswählbar machen
                weitereOptionenWaehlbar(benutzerdefiniert);
            }
        });

        // Auswahl des Wahlverfahrens
        final JLabel wahlverfahrenLabel = new JLabel("Wahlverfahren: ");
        wahlverfahrenAuswahl = new JComboBox<String>();
        Set<String> verfuegbareWahlverfahren = BenutzerdefinierteFabrikMitSperrklauselpruefer
                .verfuegbareWahlverfahren();
        for (String wahlverfahren : verfuegbareWahlverfahren) {
            wahlverfahrenAuswahl.addItem(wahlverfahren);
        }
        wahlverfahrenAuswahl.setEditable(false);

        // Auswahl des Zuteilungsverfahrens
        final JLabel zuteilungsverfahrenLabel = new JLabel(
                "Zuteilungsverfahren: ");
        zuteilungsverfahrenAuswahl = new JComboBox<String>();
        Set<String> verfuegbareZuteilungsverfahren = BenutzerdefinierteFabrikMitSperrklauselpruefer
                .verfuegbareZuteilungsverfahren();
        for (String zuteilungsverfahren : verfuegbareZuteilungsverfahren) {
            zuteilungsverfahrenAuswahl.addItem(zuteilungsverfahren);
        }
        zuteilungsverfahrenAuswahl.setEditable(false);

        // Auswahl der Sperrklausel
        final JLabel prozenthuerdeLabel = new JLabel("Prozenthürde: ");
        prozenthuerdeAuswahl = new JSpinner(new SpinnerNumberModel(0.00, 0.00,
                1.00, 0.01));

        // Auswahl der Grundmandatszahl
        final JLabel grundmandateLabel = new JLabel("Grundmandate: ");
        grundmandateAuswahl = new JSpinner(new SpinnerNumberModel(0, 0, 299, 1));

        // Button zur erneuten Berechnung
        neuBerechnenButton = new JButton("Neu Berechnen");
        neuBerechnenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                neuBerechnen();
            }
        });

        // Anordnung der Komponenten
        Container grid = new Container();
        grid.setLayout(new GridLayout(5, 2));

        // 1. Zeile
        grid.add(fabrikLabel);
        grid.add(fabrikAuswahl);

        // 2. Zeile
        grid.add(wahlverfahrenLabel);
        grid.add(wahlverfahrenAuswahl);

        // 3. Zeile
        grid.add(zuteilungsverfahrenLabel);
        grid.add(zuteilungsverfahrenAuswahl);

        // 4. Zeile
        grid.add(prozenthuerdeLabel);
        grid.add(prozenthuerdeAuswahl);

        // 5. Zeile
        grid.add(grundmandateLabel);
        grid.add(grundmandateAuswahl);

        // Komponenten zum contentPane hinzufügen
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(grid, BorderLayout.CENTER);
        contentPane.add(neuBerechnenButton, BorderLayout.SOUTH);
    }

    /**
     * Veranlasst Neuberechnung der gerade ausgewählten {@link Bundestagswahl}
     * mit veränderten {@link Berechnungsoptionen} und schließt das
     * {@link BerechnungsOptionenFenster}
     */
    @SuppressWarnings("rawtypes")
    private void neuBerechnen() {
        // Berechnungsoptionen in Wahl speichern
        this.berechnungsoptionenSpeichern();

        // Fabrik gemäß Auswahl erzeugen
        Berechnungsfabrik fabrik = null;
        String ausgewaehlteFabrik = (String) fabrikAuswahl.getSelectedItem();
        switch (ausgewaehlteFabrik) {
        case bundeswahlgesetz2013FabrikName:
            fabrik = new Bundeswahlgesetz2013Fabrik();
            break;
        case bundeswahlgesetzOhneAusgleichsmandateFabrikName:
            fabrik = new BundeswahlgesetzOhneAusgleichsmandateFabrik();
            break;
        case benutzerdefiniertFabrikName:
            double prozenthuerde = (double) prozenthuerdeAuswahl.getValue();
            int grundmandate = (int) grundmandateAuswahl.getValue();
            String wahlverfahren = (String) wahlverfahrenAuswahl
                    .getSelectedItem();
            String zuteilungsverfahren = (String) zuteilungsverfahrenAuswahl
                    .getSelectedItem();
            fabrik = new BenutzerdefinierteFabrikMitSperrklauselpruefer(
                    prozenthuerde, grundmandate, wahlverfahren,
                    zuteilungsverfahren);
        default:
            break;
        }

        if (fabrik == null) {
            // Dieser Fall würde auf einen Programmierfehler hindeuten!
            throw new NullPointerException("Ungültige Fabrikauswahl");
        }

        // Erstelle das Wahlverfahren gemäß der gewählten Fabrik
        Bundestagswahl wahl = this.wahl();
        Wahlverfahren wahlverfahren = fabrik.erstelleBerechnungsverbund(wahl);

        // Führe neue Berechnung des Ergebnisses durch
        try {
            wahlverfahren.berechneWahlergebnis();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(),
                    "Berechnungsfehler", JOptionPane.ERROR_MESSAGE);
        }

        // Neues Ergebnis anzeigen
        gui.ersetzeAktiveWahl(wahl);
        this.setVisible(false);
        gui.setEnabled(true);

        this.dispose();
    }

    private Bundestagswahl wahl() {
        return gui.aktiveWahl();
    }

    private void weitereOptionenWaehlbar(boolean waehlbar) {
        wahlverfahrenAuswahl.setVisible(waehlbar);
        zuteilungsverfahrenAuswahl.setVisible(waehlbar);
        prozenthuerdeAuswahl.setVisible(waehlbar);
        grundmandateAuswahl.setVisible(waehlbar);
    }
}
