package com.blackn0va.discord_bot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;

public class PalworldPatchnotes {
    // Das Betriebssystem, auf dem das Programm läuft
    public static String os = System.getProperty("os.name").toLowerCase();
    // Der Inhalt der Versionsdatei
    public static String VersionFile = "";
    // Das Arbeitsverzeichnis der Anwendung
    public static String workingDir = System.getProperty("user.dir");
    // Der Inhalt des RSS-News-Feeds
    public static String RSSNews = "";
    // Der Inhalt des Persistenz-Strings
    public static String Persistence = "";
    // Der Inhalt des Live-Strings
    public static String Live = "";
    // Die Version der Anwendung
    public static String Version = "";
    // Der Link zur Anwendung
    public static String link = "";
    // Die Basis-URL des News-Feeds
    public static String baseUrl = "https://store.steampowered.com/feeds/news/app/1623730/?cc=DE&l=german&snr=1_2108_9__2107";
    // Der Link zum neuesten Patch
    public static String Patchlink = "";
    // Das Bild der Anwendung
    public static String image = "";
    // Der Titel der Anwendung
    public static String title = "";
    // Die Beschreibung der Anwendung
    public static String description = "";

    // Hauptmethode
    public static void GetLatestPatchLink() throws IOException {
        // Verbindung zur baseUrl herstellen und das HTML-Dokument abrufen
        Document doc = Jsoup.connect(baseUrl).get();
        // Alle Elemente mit dem Tag "item" aus dem Dokument auswählen
        Elements items = doc.select("item");
        // Durch jedes "item"-Element iterieren
        for (Element item : items) {
            // Den Text des "title"-Elements des aktuellen "item"-Elements abrufen
            String title = item.select("title").first().text();
            // Überprüfen, ob der Titel das Wort "Patch" enthält
            if (title.contains("Patch")) {
                // Wenn ja, den Text des "link"-Elements abrufen und in Patchlink speichern
                Patchlink = item.select("link").first().text();
                // Nach dem Finden des ersten Patches die Schleife beenden
                break;
            }
        }
        // Überprüfen und speichern des Links
        CheckandSaveLink(null);
    }

    public static void CheckandSaveLink(String Version) throws IOException {
        // Abrufen des Arbeitsverzeichnisses der Anwendung
        workingDir = System.getProperty("user.dir");

        // Überprüfen, ob die Versionsdatei existiert
        File file = new File(workingDir + "\\PalworldVersion.txt");
        if (!file.exists()) {
            // Wenn nicht, erstellen Sie eine neue Datei
            file.createNewFile();
        }

        // Lesen der Versionsdatei
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        while ((st = br.readLine()) != null) {
            // Speichern Sie jede Zeile in der Variablen VersionFile
            VersionFile = st;
        }
        br.close();

        // Überprüfen, ob die PatchLink-Datei mit dem neuesten PatchLink übereinstimmt
        if (!VersionFile.equals(Patchlink)) {
            // Wenn nicht, speichern Sie den neuesten PatchLink in der PatchLink-Datei
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(Patchlink);
            writer.close();
            System.out.println("Neuer Palworld Patch wurde gefunden!");
            // Abrufen der Patchnotizen
            GetPatchNotes();

        } else {
            // Wenn die PatchLink-Datei mit dem neuesten PatchLink übereinstimmt, geben Sie
            // eine Meldung aus
            System.out.println("Kein Neuer Palworld Patch gefunden!");
        }

    }

    public static void GetPatchNotes() throws IOException {
        // Verbindung zur baseUrl herstellen und das HTML-Dokument abrufen
        Document doc = Jsoup.connect(baseUrl).get();
        // Alle Elemente mit dem Tag "item" aus dem Dokument auswählen
        Elements items = doc.select("item");
        // Durch jedes "item"-Element iterieren
        for (Element item : items) {
            // Den Text des "title"-Elements des aktuellen "item"-Elements abrufen
            title = item.select("title").first().text();
            // Überprüfen, ob der Titel das Wort "Patch" enthält
            if (title.contains("Patch")) {
                // Wenn ja, den Text des "description"-Elements abrufen und in description
                // speichern
                description = item.select("description").first().text();
                // Den Text des "link"-Elements abrufen und in link speichern
                link = item.select("link").first().text();
                // Die URL des "enclosure"-Elements abrufen und in image speichern
                image = item.select("enclosure").attr("url");
                // Nach dem Finden des ersten Patches die Schleife beenden
                break;
            }
        }

        // Entfernen von HTML-Tags aus der Beschreibung
        description.replaceAll("\\<.*?\\>", "");
        RSSNews = title + "\n" + description + "\n";
        RSSNews = RSSNews.replaceAll("<li>", "°");
        RSSNews = RSSNews.replaceAll("<br>", "\n");
        RSSNews = RSSNews.replaceAll("<p>", "\n");
        RSSNews = RSSNews.replaceAll("</p>", "\n");
        RSSNews = RSSNews.replaceAll("<a href=", "Link: ");
        RSSNews = RSSNews.replaceAll("</a>", "");
        RSSNews = RSSNews.replaceAll(">", "");
        RSSNews = RSSNews.replaceAll("&nbsp;", " ");
        RSSNews = RSSNews.replaceAll("&amp;", "&");
        RSSNews = RSSNews.replaceAll("&quot;", "\"");
        RSSNews = RSSNews.replaceAll("&lt;", "<");
        RSSNews = RSSNews.replaceAll("&apos;", "'");
        RSSNews = RSSNews.replaceAll("&gt;", ">");
        RSSNews = RSSNews.replaceAll("▼", "**");
        RSSNews = RSSNews.replaceAll("▲", "**");
        RSSNews = RSSNews.replaceAll("▼New Features", "**New Features**");
        RSSNews = RSSNews.replaceAll("▼Balancing", "**Balancing**");
        RSSNews = RSSNews.replaceAll("▼Bug Fixes", "**Bug Fixes**");
        RSSNews = RSSNews.replaceAll("▼Major Fixes", "**Major Fixes**");
        RSSNews = RSSNews.replaceAll("▼Base related ", "**Base related**");
        RSSNews = RSSNews.replaceAll("▼Others", "**Technical**");
        RSSNews = RSSNews.replaceAll("・Fixed", "**Fixed**");
        RSSNews = RSSNews.replaceAll("・Added", "**Added**");
        RSSNews = RSSNews.replaceAll("・Changed", "**Changed**");
        RSSNews = RSSNews.replaceAll("・Improved", "**Improved**");
        RSSNews = RSSNews.replaceAll("・Optimized", "**Optimized**");
        RSSNews = RSSNews.replaceAll("・Removed", "**Removed**");
        RSSNews = RSSNews.replaceAll("'", "");

        // Teilen der Nachricht in kleinere Teile
        List<String> splitStrings = splitString(RSSNews, 1900);
        // Erstellen einer neuen Liste für die endgültigen Strings
        List<String> finalStrings = new ArrayList<>();
        for (String s : splitStrings) {
            // Hinzufügen der geteilten Strings zur finalStrings-Liste
            finalStrings.addAll(splitString(s, 1900));
        }

        for (String s : finalStrings) {
            // Entfernen von unnötigen Teilen des Titels
            title = title.substring(11);
            // Senden der Nachricht an den Discord-Channel
            DiscordSendMessage.toChannelWithLink("1205879327200378910", title, "```prolog\n" + s + "\n```",
                    java.awt.Color.GREEN, link, image);
        }

    }

    // Methode zum Teilen eines Strings in kleinere Teile
    public static List<String> splitString(String input, int length) {
        // Erstellen einer neuen Liste für die geteilten Strings
        List<String> result = new ArrayList<>();
        // Initialisieren des Index
        int index = 0;
        // Solange der Index kleiner als die Länge des Eingabestrings ist
        while (index < input.length()) {
            // Hinzufügen des geteilten Teils zum Ergebnis
            result.add(input.substring(index, Math.min(index + length, input.length())));
            // Erhöhen des Index um die Länge
            index += length;
        }
        // Rückgabe der Liste der geteilten Strings
        return result;
    }

}
