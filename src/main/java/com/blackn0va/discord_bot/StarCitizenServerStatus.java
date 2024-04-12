package com.blackn0va.discord_bot;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import net.dv8tion.jda.api.entities.Activity;

public class StarCitizenServerStatus {

    // Methode zum Abrufen des Serverstatus
    public static void getStatus() throws IOException {
        try {
            // Initialisieren des Status-Strings
            String Status = "";

            // Verbindung zur Status-URL herstellen und das HTML-Dokument abrufen
            Document doc = Jsoup.connect("https://status.robertsspaceindustries.com/")
                    .get();

            try {
                // Alle Elemente mit der Klasse "component" aus dem Dokument auswählen
                Elements components = doc.select("div.component");
                // Durch jedes "component"-Element iterieren
                for (Element component : components) {
                    // Den Text des aktuellen "component"-Elements abrufen
                    String componentName = component.text();
                    // Den Wert des "data-status"-Attributs des "component-status"-Elements abrufen
                    String componentStatus = component.select("span.component-status").attr("data-status");

                    // Den Namen und den Status des Komponenten zum Status-String hinzufügen
                    Status += "|" + componentName + "|";

                }

            } catch (Exception e) {
                // Fehlerbehandlung
            }

            Status = Status.replaceAll("Persistent Universe", "PU");
            Status = Status.replaceAll("Arena Commander", "AC");
            Status = Status.replaceAll("Platform", "Web");

            // green dot on operational
            Status = Status.replaceAll("Operational", "✅");
            // red dot on degraded performance
            Status = Status.replaceAll("Degraded Performance", "🔴");
            // yellow dot on partial outage
            Status = Status.replaceAll("Partial Outage", "🟡");
            // red dot on major outage
            Status = Status.replaceAll("Major Outage", "🔴");
            // yellow dot on performance issues
            Status = Status.replaceAll("Performance Issues", "🟡");
            // yellow dot on partial service outage
            Status = Status.replaceAll("Partial Service Outage", "🟡");
            // red dot on service outage
            Status = Status.replaceAll("Service Outage", "🔴");
            // yellow dot on service disruption
            Status = Status.replaceAll("Service Disruption", "🟡");
            // yellow dot on service degradation
            Status = Status.replaceAll("Service Degradation", "🟡");

            // Ausgabe des Serverstatus
            System.out.println("Serverstatus: " + Status);
            // Setzen des Serverstatus als Aktivität des Discord-Bots
            DiscordBot.bauplan.getPresence()
                    .setActivity(Activity.customStatus(Status));

            // Ausgabe einer Meldung, dass der Serverstatus aktualisiert wurde
            System.out.println("Serverstatus wurde aktualisiert. " + Status);
            // Schreiben der Meldung in die Logs
            WriteLogs.writeLog("Serverstatus wurde aktualisiert. " + Status);

            // Zurücksetzen des Status-Strings
            Status = "";

        } catch (Exception e) {
            // Fehlerbehandlung
        }
    }

}