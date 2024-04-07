package com.blackn0va.discord_bot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;

public class StarCitizenPatchnotes {
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
        public static String Link = "";
        // Die Basis-URL der Patchnotizen
        public static String baseUrl = "https://robertsspaceindustries.com/patch-notes";
        // Der Link zum neuesten Patch
        public static String Patchlink = "https://robertsspaceindustries.com";

        // Hauptmethode
        public static void GetLatestPatchLink() throws IOException {
                // Verbindung zur baseUrl herstellen und das HTML-Dokument abrufen
                Document doc = Jsoup.connect(baseUrl).get();
                // Alle Elemente mit dem Attribut "href" aus dem Dokument auswählen
                Elements links = doc.select("a[href]");
                // Durch jedes "href"-Element iterieren
                for (int i = 0; i < links.size(); i++) {
                        // Überprüfen, ob der Text des Elements das Wort "Alpha" enthält
                        if (links.get(i).text().contains("Alpha")) {
                                // Wenn ja, den Wert des "href"-Attributs abrufen und in Link speichern
                                Link = links.get(i).attr("href");
                                // Den vollständigen Patchlink erstellen und in Patchlink speichern
                                Patchlink = "https://robertsspaceindustries.com" + Link;
                                // Nach dem Finden des ersten Patches die Schleife beenden
                                break;
                        }
                }
                // Überprüfen und speichern des Links
                CheckandSaveLink(null);
        }

        // Methode zum Überprüfen und Speichern des Patchlinks
        public static void CheckandSaveLink(String Version) throws IOException {
                // Abrufen des Arbeitsverzeichnisses der Anwendung
                workingDir = System.getProperty("user.dir");

                // Erstellen einer neuen Datei mit dem Namen "Version.txt" im Arbeitsverzeichnis
                File file = new File(workingDir + "\\Version.txt");
                // Überprüfen, ob die Datei bereits existiert
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
                        System.out.println("Neuer Star Citizen Patch wurde gefunden!");
                        // Abrufen der Patchnotizen
                        GetPatchNotes();

                } else {
                        // Wenn die PatchLink-Datei mit dem neuesten PatchLink übereinstimmt, geben Sie
                        // eine Meldung aus
                        System.out.println("Kein Neuer Star Citizen Patch gefunden!");
                }
        }

        public static void GetPatchNotes() throws IOException {
                // Verbindung zum Patchlink herstellen und das HTML-Dokument abrufen
                Document doc = Jsoup.connect(Patchlink).get();
                // Alle Elemente mit den Klassen ".rsi-markup", ".segment" und ".content" aus
                // dem Dokument auswählen
                Elements links = doc.select(".rsi-markup .segment .content");
                // Durch jedes dieser Elemente iterieren
                for (int i = 0; i < links.size(); i++) {
                        // Den HTML-Inhalt des aktuellen Elements abrufen und in RSSNews speichern
                        RSSNews = links.get(i).html();
                }

                // Entfernen von unnötigen Teilen des HTML-Inhalts
                RSSNews = RSSNews.replaceAll("<li>", "°");
                RSSNews = RSSNews.replaceAll("\\<.*?\\>", "");
                RSSNews = RSSNews.replaceAll("&nbsp;", "");
                RSSNews = RSSNews.replaceAll("&amp;", "&");
                RSSNews = RSSNews.replaceAll("&quot;", "\"");
                RSSNews = RSSNews.replaceAll("&apos;", "'");
                RSSNews = RSSNews.replaceAll("&lt;", "<");
                RSSNews = RSSNews.replaceAll("&gt;", ">");
                RSSNews = RSSNews.replaceAll("&cent;", "¢");
                RSSNews = RSSNews.replaceAll("&pound;", "£");
                RSSNews = RSSNews.replaceAll("&yen;", "¥");
                RSSNews = RSSNews.replaceAll("&euro;", "€");
                RSSNews = RSSNews.replaceAll("&copy;", "©");
                RSSNews = RSSNews.replaceAll("&reg;", "®");
                RSSNews = RSSNews.replaceAll("&trade;", "™");
                RSSNews = RSSNews.replaceAll("&times;", "×");
                RSSNews = RSSNews.replaceAll("&divide;", "÷");
                RSSNews = RSSNews.replaceAll("&para;", "¶");
                RSSNews = RSSNews.replaceAll("&sect;", "§");
                RSSNews = RSSNews.replaceAll("&brvbar;", "¦");
                RSSNews = RSSNews.replaceAll("&bull;", "•");
                RSSNews = RSSNews.replaceAll("&hellip;", "…");
                RSSNews = RSSNews.replaceAll("&prime;", "′");
                RSSNews = RSSNews.replaceAll("&Prime;", "″");
                RSSNews = RSSNews.replaceAll("&oline;", "‾");
                RSSNews = RSSNews.replaceAll("&frasl;", "⁄");
                RSSNews = RSSNews.replaceAll("&ndash;", "–");
                RSSNews = RSSNews.replaceAll("&mdash;", "—");
                RSSNews = RSSNews.replaceAll("&lsquo;", "‘");
                RSSNews = RSSNews.replaceAll("&rsquo;", "’");
                RSSNews = RSSNews.replaceAll("&sbquo;", "‚");
                RSSNews = RSSNews.replaceAll("&ldquo;", "“");
                RSSNews = RSSNews.replaceAll("&rdquo;", "”");
                RSSNews = RSSNews.replaceAll("&bdquo;", "„");
                RSSNews = RSSNews.replaceAll("&dagger;", "†");
                RSSNews = RSSNews.replaceAll("</span", "");
                RSSNews = RSSNews.replaceAll("Back to top", "");
                RSSNews = RSSNews.replaceAll("Star Citizen Alpha", "**Star Citizen Alpha**");
                RSSNews = RSSNews.replaceAll("Known Issues", "**Known Issues**");
                RSSNews = RSSNews.replaceAll("Feature Updates", "**Feature Updates**");
                RSSNews = RSSNews.replaceAll("Bug Fixes", "**Bug Fixes**");
                RSSNews = RSSNews.replaceAll("New Features", "**New Features**");
                RSSNews = RSSNews.replaceAll("Balance", "**Balance**");
                RSSNews = RSSNews.replaceAll("Technical", "**Technical**");
                RSSNews = RSSNews.replaceAll("Gameplay", "**Gameplay**");
                RSSNews = RSSNews.replaceAll("Ships and Vehicles", "**Ships and Vehicles**");
                RSSNews = RSSNews.replaceAll("Locations", "**Locations**");
                RSSNews = RSSNews.replaceAll("Weapons and Items", "**Weapons and Items**");
                RSSNews = RSSNews.replaceAll("Core Tech", "**Core Tech**");
                RSSNews = RSSNews.replaceAll("AI", "**AI**");
                RSSNews = RSSNews.replaceAll("Ships", "**Ships**");
                RSSNews = RSSNews.replaceAll("Vehicles", "**Vehicles**");
                RSSNews = RSSNews.replaceAll("Weapons", "**Weapons**");
                RSSNews = RSSNews.replaceAll("FPS Weapons", "**FPS Weapons**");
                RSSNews = RSSNews.replaceAll("FPS Gadgets", "**FPS Gadgets**");
                RSSNews = RSSNews.replaceAll("Long Term Persistence", "**Long Term Persistence**");
                RSSNews = RSSNews.replaceAll("Starting aUEC", "**Starting aUEC**");
                RSSNews = RSSNews.replaceAll("Shops and Shopping", "**Shops and Shopping**");
                RSSNews = RSSNews.replaceAll("Ships", "**Ships**");
                RSSNews = RSSNews.replaceAll("Vehicles", "**Vehicles**");
                RSSNews = RSSNews.replaceAll("Weapons", "**Weapons**");
                RSSNews = RSSNews.replaceAll("Star Citizen Patch", "**Star Citizen Patch**");

                // Teilen Sie den RSSNews-String in kleinere Strings auf, die nicht länger als
                // 1900 Zeichen sind
                List<String> splitStrings = splitString(RSSNews, 1900);
                // Erstellen Sie eine neue Liste, um die endgültigen Strings zu speichern
                List<String> finalStrings = new ArrayList<>();
                // Durchlaufen Sie jeden String in splitStrings
                for (String s : splitStrings) {
                        // Teilen Sie jeden String erneut auf und fügen Sie die resultierenden Strings
                        // zu finalStrings hinzu
                        finalStrings.addAll(splitString(s, 1900));
                }

                // Durchlaufen Sie jeden String in finalStrings
                for (String s : finalStrings) {
                        // Senden Sie jeden String als Nachricht an einen Discord-Kanal
                        // Die Nachricht wird in einem Codeblock mit der Sprache "prolog" formatiert, um
                        // eine bessere Lesbarkeit zu gewährleisten
                        DiscordSendMessage.toChannel("1101892014351585290", "Star Citizen Update!",
                                        "```prolog\n" + s + "\n```", java.awt.Color.GREEN);
                }

        }

        // Methode zum Aufteilen eines Strings in kleinere Strings einer bestimmten
        // Länge
        public static List<String> splitString(String input, int length) {
                // Erstellen einer Liste zum Speichern der resultierenden Strings
                List<String> result = new ArrayList<>();
                // Initialisieren des Index auf 0
                int index = 0;
                // Durchlaufen des Eingabestrings
                while (index < input.length()) {
                        // Hinzufügen eines Teils des Eingabestrings zur Ergebnisliste
                        // Der Teil beginnt am aktuellen Index und endet am kleineren Wert von "index +
                        // length" und "input.length()"
                        result.add(input.substring(index, Math.min(index + length, input.length())));
                        // Erhöhen des Index um die Länge
                        index += length;
                }
                // Rückgabe der Ergebnisliste
                return result;
        }

}
