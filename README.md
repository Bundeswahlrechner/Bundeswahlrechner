Bundeswahlrechner
=================

##License

Released under GPL Version 3, see LICENSE file.

Bedienung
=========

Nachdem das Programm geöffnet wurde, muss zunächst eine Wahl geladen werden. Per Klick auf "Wahl erstellen" werden automatisch die Daten der Bundestagswahl 2013 geladen (Über "Importieren" können andere Wahlen geladen werden, die benötigten Dateien werden bspw. vom Bundeswahlleiter zur Verfügung gestellt. Nachdem der Nutzer die Daten seinen Wünschen entsprechend verändert hat, kann er sie über "Exportieren" exportieren und später wieder importieren. Es ist ebenfalls möglich, die .csv-Dateien direkt zu verändern.). Es können mehrere Wahlen gleichzeitig geöffnet werden, (beispielsweise um sie zu vergleichen oder verschiedene Varianten des Generierers auszuprobieren) zwischen diesen kann mittels Tabs gewechselt werden. Geöffnete Wahlen können mittels "Wahl entfernen" geschlossen werden, und mittels "Wahl duplizieren" wird eine Sicherungskopie der Wahl geöffnet.

Sobald eine Wahl geladen ist wird automatisch die Sitzverteilung berechnet. Es ist eine Vielzahl von Ansichten verfügbar um die Ergebnisse anzuzeigen. Links kann von der Tabelle zu einem Kuchen- und einem Stabdiagramm gewechselt werden, das Wahlweise die Ergebnisse auf Bundes-, Länder- oder Wahlkreisebene anzeigt. Ebenso gibt es Deutschlandkarten, auf denen die Ergebnisse der einzelnen Parteien angezeigt werden kann. Eine detaillierte Wahlkreiskarte öffnet sich auf Wunsch im Browser ("Zeige Wahlkreiskarte"). Unter "Einstellungen" können die Farben der Parteien angepasst werden. In allen Diagrammen können die genauen Zahlen angezeigt werden, indem der Mauszeiger auf den entsprechenden Balken o.ä. gefahren wird. Grafiken können unter "Exportieren" ebenfalls exportiert werden. Per Rechtsklick auf ein Diagramm stehen für dieses weitere Optionen bereit.

Sind mehrere Wahlen geöffnet, kann man mit "Wahlen vergleichen" Unterschiede in der Sitzzahl visualisieren lassen. Wir empfehlen, dies nur mit 2 oder 3 geöffneten Wahlen zu tun, da automatisch alle Wahlen verglichen werden. Über "Zwischenergebnisse" wird eine kurze Übersicht über den Ablauf der Berechnung gegeben. Zur vertiefenden Lektüre sei das Bundeswahlgesetz oder Wikipedia empfohlen.

Unter "Berechnungsoptionen" kann das verwendete Berechnungsverfahren angepasst werden. Prinzipiell kann jedes Wahlverfahren eingebaut werden, derzeit unterstützen wir ein Abschalten der Ausgleichsmandate, das Verwenden der 3 Sitzzuteilungsverfahren D'Hondt, Hare-Niemeyer und das derzeit in der BRD verwendete Sainte-Lague, das auf 3 verschiedene Weisen berechnet werden kann - das Ergebnis ist hierbei das gleiche. Des Weiteren kann man die 5%-Hürde auf einen beliebigen Wert setzen oder mit 0% ganz abschaffen, sowie die 3 Direktmandate Regel ändern.

Unter "Wahldaten generieren" steht eine experimentelle Funktionalität zur Verfügung, die versucht, eine Stimmverteilung zu finden, die gegebenen Restriktionen genügt. Da es sehr kompliziert ist die Funktion, die Wählerstimmen auf Bundestagssitze abbildet, zu "invertieren" (umzudrehen), verwenden wir dafür einen Optimierungsalgorithmus. Dieser nimmt zunächst die derzeit geöffnete und ausgewählte Wahl als Grundlage, um den gegebenen Restriktionen zu genügen, d.h. der Nutzer kann den Suchalgorithmus durch geeignetes Modifizieren der Stimmdaten bereits vorher in eine gewisse Richtung lenken. Aufgrund des einfachen Suchalgorithmus beschränken wir uns auf eine Restriktion der Größe des gesamten Bundestages, eine gewisse Sitzzahl X +-Y Sitze, wobei Y ein Toleranzwert ist. Zusätzlich kann eine Restriktion für das Ergebnis einer Partei, in absoluten Sitzen oder einem relativen Anteil an Sitzen eingestellt werden, wieder mit einem Wert X +-Y.
Diese Funktionalität ist so realisiert, dass wir zunächst den Abstand eines Wahlergebnisses von den Restriktionen bewerten (eine sog. "Fitnessfunktion"). Dann verändern wir die Stimmergebnisse ein kleines bisschen auf zufällige Weise und berechnen das Ergebnis neu. Wenn unser Abstand von den Restriktionen kleiner wurde, nehmen wir diese neue Stimmverteilung als Grundlage, ansonsten mit einer gewissen Wahrscheinlichkeit, die mit der Zeit immer größer wird, die alte. Dies wiederholen wir, bis wir eine valide Stimmverteilung gefunden haben oder der Nutzer abbricht. (Das Verfahren nennt sich Simulated Annealing und basiert auf dem Abkühlen von Metallen in der Natur, deren Atome versuchen, einen energetisch möglichst günstigen Zustand zu erreichen)
Technisch versierte Nutzer können den Generierer auch mit einer beliebigen Anzahl Restriktionen laufen lassen oder neue Restriktionen implementieren (bspw. "Partei A mindestens X Sitze" bzw. "Partei B maximal Y Sitze", was derzeit etwas umständlich ist.), oder auch einen verbesserten Suchalgorithmus zu implementieren. Die "zufällige leichte Veränderung" der Wahl kann ebenfalls  sehr unterschiedlich aufgefasst werden und ist in der derzeitigen Form nicht sonderlich realistisch, sondern ein reines Zahlenspiel. Mögliche Erweiterungen wären ein Bevorzugen von "etablierten Parteien" beim Verschieben von Zweitstimmen zwischen Parteien sowie eine starke Bevorzugung der aussichtsreichen Kandidaten beim Verschieben von Erststimmen, derzeit werden gleichverteilt zufällig neue Gewinner in den Wahlkreisen gezogen.
