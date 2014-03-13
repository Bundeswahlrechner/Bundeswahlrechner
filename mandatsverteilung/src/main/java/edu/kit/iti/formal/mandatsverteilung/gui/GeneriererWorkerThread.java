package edu.kit.iti.formal.mandatsverteilung.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.NoSuchElementException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextField;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.generierer.RandomisierterGenerierer;

public class GeneriererWorkerThread extends Thread {
    private final GUI gui;
    private final List<String> parteien;
    private final RandomisierterGenerierer r;
    private final GeneriereWahldatenFenster toDispose;

    public GeneriererWorkerThread(RandomisierterGenerierer rg, GUI gui,
            GeneriereWahldatenFenster toDispose, List<String> l) {
        r = rg;
        this.gui = gui;
        this.toDispose = toDispose;
        parteien = l;
    }

    private void handleException() {
        System.out.println("Exception handled");
        toDispose.setVisible(false);
        final JDialog j = new JDialog(toDispose, "Fehler aufgetreten", true);
        j.add(new JTextField("Bei der Generierung trat ein Fehler auf"));
        JButton ok = new JButton("OK");
        j.add(ok);
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                j.setVisible(false);
                j.dispose();
            }
        });
        j.setMinimumSize(new Dimension(400, 400));
        j.setPreferredSize(new Dimension(400, 400));
        j.setVisible(true);
    }

    @Override
    public void run() {
        Bundestagswahl result = null;
        try {
            result = r.generiere(parteien);
        } catch (NoSuchElementException e) {
            this.handleException();
        } catch (IllegalArgumentException e) {
            this.handleException();
        }
        synchronized (this) {
            if (result != null) {
                gui.neueWahl(result);
            }
            toDispose.setVisible(false);
            toDispose.dispose();
        }
    }
}
