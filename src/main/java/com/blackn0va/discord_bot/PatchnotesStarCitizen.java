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
import java.util.concurrent.ConcurrentLinkedQueue;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.ArrayList;
import java.util.List;

public class PatchnotesStarCitizen {
        public static ConcurrentLinkedQueue<Message> starCitizenQueue = new ConcurrentLinkedQueue<>();

        // Hauptmethode
        public static void GetLatestPatchLink() throws IOException {
                // Verbindung zur baseUrl herstellen und das HTML-Dokument abrufen
                Document doc = Jsoup.connect(Main.StarCitizenBaseUrl).get();
                // Alle Elemente mit dem Attribut "href" aus dem Dokument auswählen
                Elements links = doc.select("a[href]");
                // Durch jedes "href"-Element iterieren
                for (int i = 0; i < links.size(); i++) {
                        // Überprüfen, ob der Text des Elements das Wort "Alpha" enthält
                        if (links.get(i).text().contains("Alpha")) {
                                // Wenn ja, den Wert des "href"-Attributs abrufen und in Link speichern
                                Main.StarCitizenLink = links.get(i).attr("href");
                                // Den vollständigen Patchlink erstellen und in Patchlink speichern
                                Main.StarCitizenPatchlink = "https://robertsspaceindustries.com" + Main.StarCitizenLink;
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
                Main.workingDir = System.getProperty("user.dir");

                File file = null;

                if (Main.os.contains("win")) {
                        file = new File(Main.workingDir + "\\StarCitizen_Version.txt");
                        if (!file.exists()) {
                                file.createNewFile();
                        }
                } else if (Main.os.contains("nix") || Main.os.contains("nux") || Main.os.contains("aix")) {
                        file = new File(Main.workingDir + "/StarCitizen_Version.txt");
                        if (!file.exists()) {
                                file.createNewFile();
                        }
                }

                System.out.println("Patchlink: " + file);
                // Lesen der Versionsdatei
                BufferedReader br = new BufferedReader(new FileReader(file));
                String st;
                while ((st = br.readLine()) != null) {
                        // Speichern Sie jede Zeile in der Variablen VersionFile
                        Main.StarCitizenVersionFile = st;
                }
                br.close();

                // Überprüfen, ob die PatchLink-Datei mit dem neuesten PatchLink übereinstimmt
                if (!Main.StarCitizenVersionFile.equals(Main.StarCitizenPatchlink)) {
                        // Wenn nicht, speichern Sie den neuesten PatchLink in der PatchLink-Datei
                        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                        writer.write(Main.StarCitizenPatchlink);
                        writer.close();
                        System.out.println("Neuer Star Citizen Patch wurde gefunden!");
                        // Abrufen der Patchnotizen
                        GetPatchNotes();

                } else {
                        // Wenn die PatchLink-Datei mit dem neuesten PatchLink übereinstimmt, geben Sie
                        // eine Meldung aus
                        System.out.println("Kein Neuer Star Citizen Patch gefunden!");
                        GetPatchNotes();
                }
        }

        public static void GetPatchNotes() throws IOException {
                // Verbindung zum Patchlink herstellen und das HTML-Dokument abrufen
                Document doc = Jsoup.connect(Main.StarCitizenPatchlink).get();
                // Alle Elemente mit den Klassen ".rsi-markup", ".segment" und ".content" aus
                // dem Dokument auswählen
                Elements links = doc.select(".rsi-markup .segment .content");
                // Durch jedes dieser Elemente iterieren
                for (int i = 0; i < links.size(); i++) {
                        // Den HTML-Inhalt des aktuellen Elements abrufen und inMain.StarCitizenRSSNews
                        // speichern
                        Main.StarCitizenRSSNews = links.get(i).html();
                }

                // Entfernen von unnötigen Teilen des HTML-Inhalts
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("<li>", "°");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("\\<.*?\\>", "");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("&nbsp;", "");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("&amp;", "&");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("&quot;", "\"");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("&apos;", "'");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("&lt;", "<");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("&gt;", ">");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("&cent;", "¢");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("&pound;", "£");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("&yen;", "¥");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("&euro;", "€");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("&copy;", "©");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("&reg;", "®");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("&trade;", "™");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("&times;", "×");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("&divide;", "÷");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("&para;", "¶");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("&sect;", "§");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("&brvbar;", "¦");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("&bull;", "•");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("&hellip;", "…");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("&prime;", "′");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("&Prime;", "″");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("&oline;", "‾");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("&frasl;", "⁄");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("&ndash;", "–");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("&mdash;", "—");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("&lsquo;", "‘");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("&rsquo;", "’");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("&sbquo;", "‚");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("&ldquo;", "“");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("&rdquo;", "”");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("&bdquo;", "„");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("&dagger;", "†");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("</span", "");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("Back to top", "");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("Star Citizen Alpha",
                                "**Star Citizen Alpha**");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("Known Issues", "**Known Issues**");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("Feature Updates", "**Feature Updates**");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("Bug Fixes", "**Bug Fixes**");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("New Features", "**New Features**");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("Balance", "**Balance**");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("Technical", "**Technical**");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("Gameplay", "**Gameplay**");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("Ships and Vehicles",
                                "**Ships and Vehicles**");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("Locations", "**Locations**");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("Weapons and Items",
                                "**Weapons and Items**");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("Core Tech", "**Core Tech**");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("AI", "**AI**");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("Ships", "**Ships**");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("Vehicles", "**Vehicles**");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("Weapons", "**Weapons**");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("FPS Weapons", "**FPS Weapons**");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("FPS Gadgets", "**FPS Gadgets**");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("Long Term Persistence",
                                "**Long Term Persistence**");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("Starting aUEC", "**Starting aUEC**");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("Shops and Shopping",
                                "**Shops and Shopping**");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("Ships", "**Ships**");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("Vehicles", "**Vehicles**");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("Weapons", "**Weapons**");
                Main.StarCitizenRSSNews = Main.StarCitizenRSSNews.replaceAll("Star Citizen Patch",
                                "**Star Citizen Patch**");

                // Teilen Sie denMain.StarCitizenRSSNews-String in kleinere Strings auf, die
                // nicht länger
                // als
                // 1200 Zeichen sind
                List<String> splitStrings = splitString(Main.StarCitizenRSSNews, 1200);
                // Erstellen Sie eine neue Liste, um die endgültigen Strings zu speichern

                // Durchlaufen Sie jeden String in splitStrings
                for (String s : splitStrings) {
                        // Teilen Sie jeden String erneut auf und fügen Sie die resultierenden Strings
                        // zu finalStrings hinzu
                        Main.StarCitizenFinalStrings.addAll(splitString(s, 1200));
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

        public static List<String> getPages() throws IOException {
                // Abrufen des neuesten Patch-Links
                GetLatestPatchLink();

                // Umwandlung der Patchnotizen in einen String
                String patchnotes = Main.StarCitizenFinalStrings.toString();
                // Festlegen der maximalen Länge einer Seite
                int length = 1200;

                // Erstellen einer Liste zur Speicherung der Seiten
                List<String> result = new ArrayList<>();
                int index = 0;
                // Aufteilen der Patchnotizen in Seiten
                while (index < patchnotes.length()) {
                        // Hinzufügen einer Seite zur Liste
                        result.add(patchnotes.substring(index, Math.min(index + length, patchnotes.length())));
                        // Aktualisieren des Index für die nächste Seite
                        index += length;
                }

                // Rückgabe der Liste der Seiten
                return result;
        }

        // Methode zum Abrufen einer Seite
        public static String getPage(int pageNum) {
                // Überprüfen, ob die Seite bereits im Cache ist
                if (Main.StarCitizenPageCache.containsKey(pageNum)) {
                        // Wenn ja, die Seite aus dem Cache zurückgeben
                        return Main.StarCitizenPageCache.get(pageNum);
                } else {
                        // Wenn nicht, die Seite abrufen
                        String page = Main.StarCitizenPatchPages.get(pageNum - 1);
                        // Die Seite im Cache speichern
                        Main.StarCitizenPageCache.put(pageNum, page);
                        // Die Seite zurückgeben
                        return page;
                }
        }

        public static void GetStarCitizenPatchnotes() {
                System.out.println("Prüfung auf neue StarCitizen Patches...");

                try {
                        Main.StarCitizenPatchPages = null;
                        
                        Main.StarCitizenPatchPages = PatchnotesStarCitizen.getPages();
                        System.out.println("StarCitizen Patchnotes Seiten: " + Main.StarCitizenPatchPages.size());

                        // Textkanal über dessen ID abrufen
                        TextChannel channel = Main.bauplan.getTextChannelById(Main.StarCitizenPatchChannelID);

                        // Letzte Nachricht im Kanal abrufen
                        List<Message> messages = channel.getHistory().retrievePast(1).complete();

                        // Prüfen, ob die letzte Nachricht die ersten Wörter der aktuellen Seite enthält
                        if (!messages.isEmpty()) {
                                Message letzteNachricht = messages.get(0);
                                List<MessageEmbed> eingebetteteInhalte = letzteNachricht.getEmbeds();
                                if (!eingebetteteInhalte.isEmpty()) {
                                        MessageEmbed eingebetteterInhalt = eingebetteteInhalte.get(0);
                                        String beschreibungEingebetteterInhalt = eingebetteterInhalt.getDescription();
                                        String footer = eingebetteterInhalt.getFooter().getText();
                                        String[] splitFooter = footer.split(" ");
                                        if (splitFooter.length > 1) {
                                                String MessageID = letzteNachricht.getId();
                                                Main.StarCitizencurrentPageNum = Integer.parseInt(footer.split(" ")[1]); // Extrahieren
                                                                                                                         // der
                                                                                                                         // aktuellen
                                                // Seitennummer aus dem Footer
                                                // ersten 10 wörter
                                                String ersteWoerter = PatchnotesStarCitizen
                                                                .getPage(Main.StarCitizencurrentPageNum)
                                                                .substring(0, 50);

                                                // prüfen ob text schon gespostet wurde
                                                if (beschreibungEingebetteterInhalt.contains(ersteWoerter)) {
                                                        try {
                                                                System.out.println(
                                                                                "StarCitizen Patche bereits gepostet");

                                                                // editiere die seite auf die gleiche wie sie aktuell
                                                                // ist
                                                                DiscordSendMessage.editMessageEmbeds(
                                                                                Main.StarCitizenPatchChannelID,
                                                                                "Star Citizen Patchnotes!",
                                                                                PatchnotesStarCitizen.getPage(1), 1,
                                                                                MessageID);
                                                        } catch (Exception e) {
                                                                System.out.println(
                                                                                "Fehler beim Abrufen der StarCitizen Patchnotes: 1");
                                                        }

                                                }
                                        } else {
                                                try {
                                                        System.out.println("StarCitizen Patch wird gepostet");
                                                        // Wenn die aktuelle Seite die erste Seite ist, wird die
                                                        // Nachricht
                                                        // gesendet
                                                        DiscordSendMessage.sendPaginatedMessage(
                                                                        Main.StarCitizenPatchChannelID,
                                                                        "Star Citizen Patchnotes!",
                                                                        PatchnotesStarCitizen
                                                                                        .getPage(Main.StarCitizencurrentPageNum));
                                                } catch (Exception e) {
                                                        System.out.println(
                                                                        "Fehler beim Abrufen der StarCitizen Patchnotes: 2");
                                                }

                                        }

                                } else {
                                        try {
                                                System.out.println("StarCitizen Patch wird gepostet");
                                                // Wenn die aktuelle Seite die erste Seite ist, wird die Nachricht
                                                // gesendet
                                                DiscordSendMessage.sendPaginatedMessage(Main.StarCitizenPatchChannelID,
                                                                "Star Citizen Patchnotes!",
                                                                PatchnotesStarCitizen.getPage(
                                                                                Main.StarCitizencurrentPageNum));
                                        } catch (Exception e) {
                                                System.out.println("Fehler beim Abrufen der StarCitizen Patchnotes: 3");
                                        }

                                }
                        }

                } catch (Exception e) {
                        System.out.println("Fehler beim Abrufen der StarCitizen Patchnotes: 4");
                }

        }

}