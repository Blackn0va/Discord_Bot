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

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import java.awt.Color;

import java.util.ArrayList;
import java.util.List;


public class PatchnotesPalworld {

    // Hauptmethode
    public static void GetLatestPatchLink() throws IOException {
        // Verbindung zur baseUrl herstellen und das HTML-Dokument abrufen
        Document doc = Jsoup.connect(Main.PalworldBaseUrl).get();
        // Alle Elemente mit dem Tag "item" aus dem Dokument auswählen
        Elements items = doc.select("item");
        // Durch jedes "item"-Element iterieren
        for (Element item : items) {
            // Den Text des "title"-Elements des aktuellen "item"-Elements abrufen
            String title = item.select("title").first().text();
            // Überprüfen, ob der Titel das Wort "Patch" enthält
            if (title.contains("Patch")) {
                // Wenn ja, den Text des "link"-Elements abrufen und in Patchlink speichern
                Main.PalworldPatchlink = item.select("link").first().text();
                // Nach dem Finden des ersten Patches die Schleife beenden
                break;
            }
        }
        // Überprüfen und speichern des Links
        CheckandSaveLink(null);
    }

    public static void CheckandSaveLink(String Version) throws IOException {
        File file = null;
        if (Main.os.contains("win")) {
            file = new File(Main.workingDir + "\\Palworld_Version.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
        } else if (Main.os.contains("nix") || Main.os.contains("nux") || Main.os.contains("aix")) {
            file = new File(Main.workingDir + "/Palworld_Version.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
        }

        // Lesen der Versionsdatei
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        while ((st = br.readLine()) != null) {
            // Speichern Sie jede Zeile in der Variablen VersionFile
            Main.PalworldVersionFile = st;
        }
        br.close();

        // Überprüfen, ob die PatchLink-Datei mit dem neuesten PatchLink übereinstimmt
        if (!Main.PalworldVersionFile.equals(Main.PalworldPatchlink)) {
            // Wenn nicht, speichern Sie den neuesten PatchLink in der PatchLink-Datei
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(Main.PalworldPatchlink);
            writer.close();
            System.out.println("Neuer Palworld Patch wurde gefunden!");
            // Abrufen der Patchnotizen
            GetPatchNotes();

        } else {
            // Wenn die PatchLink-Datei mit dem neuesten PatchLink übereinstimmt, geben Sie
            // eine Meldung aus
            System.out.println("Kein Neuer Palworld Patch gefunden!");
            GetPatchNotes();
        }

    }

    public static void GetPatchNotes() throws IOException {
        // Verbindung zur baseUrl herstellen und das HTML-Dokument abrufen
        Document doc = Jsoup.connect(Main.PalworldBaseUrl).get();
        // Alle Elemente mit dem Tag "item" aus dem Dokument auswählen
        Elements items = doc.select("item");
        // Durch jedes "item"-Element iterieren
        for (Element item : items) {
            // Den Text des "title"-Elements des aktuellen "item"-Elements abrufen
            Main.PalworldTitle = item.select("title").first().text();
            // Überprüfen, ob der Titel das Wort "Patch" enthält
            if (Main.PalworldTitle.contains("Patch")) {
                // Wenn ja, den Text des "description"-Elements abrufen und in description
                // speichern
                Main.PalworldDescription = item.select("description").first().text();
                // Den Text des "link"-Elements abrufen und in link speichern
                Main.PalworldLink = item.select("link").first().text();
                // Die URL des "enclosure"-Elements abrufen und in image speichern
                Main.PalworldPicture = item.select("enclosure").attr("url");
                // Nach dem Finden des ersten Patches die Schleife beenden
                break;
            }
        }

        // Entfernen von HTML-Tags aus der Beschreibung
        Main.PalworldDescription.replaceAll("\\<.*?\\>", "");
        Main.PalworldRSSNews = Main.PalworldTitle + "\n" + Main.PalworldDescription + "\n";
        Main.PalworldRSSNews = Main.PalworldRSSNews.replaceAll("<li>", "°");
        Main.PalworldRSSNews = Main.PalworldRSSNews.replaceAll("<br>", "\n");
        Main.PalworldRSSNews = Main.PalworldRSSNews.replaceAll("<p>", "\n");
        Main.PalworldRSSNews = Main.PalworldRSSNews.replaceAll("</p>", "\n");
        Main.PalworldRSSNews = Main.PalworldRSSNews.replaceAll("<a href=", "Link: ");
        Main.PalworldRSSNews = Main.PalworldRSSNews.replaceAll("</a>", "");
        Main.PalworldRSSNews = Main.PalworldRSSNews.replaceAll(">", "");
        Main.PalworldRSSNews = Main.PalworldRSSNews.replaceAll("&nbsp;", " ");
        Main.PalworldRSSNews = Main.PalworldRSSNews.replaceAll("&amp;", "&");
        Main.PalworldRSSNews = Main.PalworldRSSNews.replaceAll("&quot;", "\"");
        Main.PalworldRSSNews = Main.PalworldRSSNews.replaceAll("&lt;", "<");
        Main.PalworldRSSNews = Main.PalworldRSSNews.replaceAll("&apos;", "'");
        Main.PalworldRSSNews = Main.PalworldRSSNews.replaceAll("&gt;", ">");
        Main.PalworldRSSNews = Main.PalworldRSSNews.replaceAll("▼", "**");
        Main.PalworldRSSNews = Main.PalworldRSSNews.replaceAll("▲", "**");
        Main.PalworldRSSNews = Main.PalworldRSSNews.replaceAll("▼New Features", "**New Features**");
        Main.PalworldRSSNews = Main.PalworldRSSNews.replaceAll("▼Balancing", "**Balancing**");
        Main.PalworldRSSNews = Main.PalworldRSSNews.replaceAll("▼Bug Fixes", "**Bug Fixes**");
        Main.PalworldRSSNews = Main.PalworldRSSNews.replaceAll("▼Major Fixes", "**Major Fixes**");
        Main.PalworldRSSNews = Main.PalworldRSSNews.replaceAll("▼Base related ", "**Base related**");
        Main.PalworldRSSNews = Main.PalworldRSSNews.replaceAll("▼Others", "**Technical**");
        Main.PalworldRSSNews = Main.PalworldRSSNews.replaceAll("・Fixed", "**Fixed**");
        Main.PalworldRSSNews = Main.PalworldRSSNews.replaceAll("・Added", "**Added**");
        Main.PalworldRSSNews = Main.PalworldRSSNews.replaceAll("・Changed", "**Changed**");
        Main.PalworldRSSNews = Main.PalworldRSSNews.replaceAll("・Improved", "**Improved**");
        Main.PalworldRSSNews = Main.PalworldRSSNews.replaceAll("・Optimized", "**Optimized**");
        Main.PalworldRSSNews = Main.PalworldRSSNews.replaceAll("・Removed", "**Removed**");
        Main.PalworldRSSNews = Main.PalworldRSSNews.replaceAll("'", "");

        // Teilen der Nachricht in kleinere Teile
        List<String> splitStrings = splitString(Main.PalworldRSSNews, 1200);
        // Durchlaufen Sie jeden String in splitStrings
        for (String s : splitStrings) {
            // Teilen Sie jeden String erneut auf und fügen Sie die resultierenden Strings
            // zu finalStrings hinzu
            Main.PalworldFinalStrings.addAll(splitString(s, 1200));
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

    public static List<String> getPages() throws IOException {
        // Abrufen des neuesten Patch-Links
        GetLatestPatchLink();

        // Umwandlung der Patchnotizen in einen String
        String patchnotes = Main.PalworldFinalStrings.toString();
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
        if (Main.PalworldPageCache.containsKey(pageNum)) {
            // Wenn ja, die Seite aus dem Cache zurückgeben
            return Main.PalworldPageCache.get(pageNum);
        } else {
            // Wenn nicht, die Seite abrufen
            String page = Main.PalworldPatchPages.get(pageNum - 1);
            // Die Seite im Cache speichern
            Main.PalworldPageCache.put(pageNum, page);
            // Die Seite zurückgeben
            return page;
        }
    }

    public static void GetPalworldPatchnotes() {
        System.out.println("Prüfung auf neue Palworld Patches...");

        try {
            Main.PalworldPatchPages = PatchnotesPalworld.getPages();
            System.out.println("Seiten: " + Main.PalworldPatchPages.size());

            // Textkanal über dessen ID abrufen
            TextChannel channel = Main.bauplan.getTextChannelById(Main.PalworldPatchChannelID);

            // Letzte Nachricht im Kanal abrufen
            List<Message> messages = channel.getHistory().retrievePast(1).complete();

            // Prüfen, ob die letzte Nachricht die ersten Wörter der aktuellen Seite enthält
            if (!messages.isEmpty() && Main.PalworldPatchPages.size() > 0) {
                Message letzteNachricht = messages.get(0);
                List<MessageEmbed> eingebetteteInhalte = letzteNachricht.getEmbeds();

                if (!eingebetteteInhalte.isEmpty()) {
                    MessageEmbed eingebetteterInhalt = eingebetteteInhalte.get(0);
                    String beschreibungEingebetteterInhalt = eingebetteterInhalt.getDescription();
                    String footer = eingebetteterInhalt.getFooter().getText();
                    String MessageID = letzteNachricht.getId();
                    String[] splitFooter = footer.split(" ");
                    if (splitFooter.length > 1) {
                        Main.PalworldCurrentPageNum = Integer.parseInt(splitFooter[1]);
                    } else {
                        // Behandlung des Falls, dass das Array nur ein Element hat
                        // ...
                    }
                    // Seitennummer aus dem Footer
                    // ersten 10 wörter
                    if (Main.PalworldCurrentPageNum <= PatchnotesPalworld.getPages().size()) {
                        String ersteWoerter = PatchnotesPalworld.getPage(Main.PalworldCurrentPageNum).substring(0, 50);
                        ersteWoerter = ersteWoerter.replaceAll("\\[", "");
                        ersteWoerter = ersteWoerter.replaceAll("\\]", "");
                        System.out.println("Erste Wörter: " + ersteWoerter);
                        // prüfen ob text schon gespostet wurde
                        if (beschreibungEingebetteterInhalt.contains(ersteWoerter)) {
                            System.out.println("Palworld Patch bereits gepostet");

                            // editiere die seite auf die gleiche wie sie aktuell ist
                            DiscordSendMessage.editMessageEmbeds(Main.PalworldPatchChannelID, "Palworld Patchnotes!",
                                    PatchnotesPalworld.getPage(1), 1, MessageID);
                        } else {
                            // Wenn die letzte Nachricht nicht die ersten Wörter der aktuellen Seite
                            // enthält, senden Sie die aktuelle Seite
                            System.out.println("Palworld Patch wird gepostet");
                            DiscordSendMessage.toChannelWithLink(Main.PalworldPatchChannelID, Main.PalworldTitle,
                                    "```prolog\n" + Main.PalworldPatchPages.get(0).replace("[", "").replace("]", "")
                                            + "\n```",
                                    Color.GREEN, Main.PalworldLink, Main.PalworldPicture);
                        }
                    } else {
                        System.out.println("Palworld Patch wird gepostet");
                        DiscordSendMessage.toChannelWithLink(Main.PalworldPatchChannelID, Main.PalworldTitle,
                                "```prolog\n" + Main.PalworldPatchPages.get(0).replace("[", "").replace("]", "") + "\n```",
                                Color.GREEN, Main.PalworldLink, Main.PalworldPicture);
                    }

                } else {
                    System.out.println("Palworld Patch wird gepostet");
                    DiscordSendMessage.toChannelWithLink(Main.PalworldPatchChannelID, Main.PalworldTitle,
                            "```prolog\n" + Main.PalworldPatchPages.get(0).replace("[", "").replace("]", "") + "\n```",
                            Color.GREEN, Main.PalworldLink, Main.PalworldPicture);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
