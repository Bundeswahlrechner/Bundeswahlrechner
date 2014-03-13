package edu.kit.iti.formal.mandatsverteilung.importer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundesland;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Kandidat;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Landesliste;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Partei;

public class AutomatischerKandidatenParser implements KandidatenParser {
    private final int n;

    public AutomatischerKandidatenParser() {
        n = 150;
    }

    public AutomatischerKandidatenParser(int n) {
        this.n = n;
    }

    private Kandidat erzeugeKandidat(Partei p, Landesliste ll, int platz) {
        StringBuilder sb = new StringBuilder();
        sb.append("Partei");
        sb.append(p.getName());
        sb.append("Bundesland");
        sb.append(ll.getBundesland().getAbkuerzung());
        sb.append("Kandidat");
        sb.append(platz);
        Kandidat result = new Kandidat(sb.toString());
        return result;
    }

    @Override
    public void parseKandidatenDatei(File f, Bundestagswahl b)
            throws IOException {
        this.parseKandidatenDatei(new FileInputStream(f), b);

    }

    @Override
    public void parseKandidatenDatei(InputStream is, Bundestagswahl b)
            throws IOException {
        KandidatenParser ckp = new CSVKandidatenParser();
        ckp.parseKandidatenDatei(is, b);
        Iterator<Bundesland> ib = b.bundeslaenderIterator();
        Bundesland curBundesland;
        while (ib.hasNext()) {
            curBundesland = ib.next();
            Iterator<Partei> ip = b.parteienIterator();
            Partei curPartei;
            while (ip.hasNext()) {
                curPartei = ip.next();
                Landesliste curLL = curPartei.getLandesliste(curBundesland);
                if (curLL != null) {
                    int groesse = curLL.getListengroesse();
                    if (groesse < n) {
                        for (int i = groesse + 1; i < n; i++) {
                            curLL.addKandidat(
                                    this.erzeugeKandidat(curPartei, curLL, i),
                                    i);
                        }
                    }
                }
            }
        }
    }
}
