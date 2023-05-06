package com.blackn0va.discord_bot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class rssNews {

        public static String Version = "";

        public static void getPatchNotes() {

                try {
                        String Version = "";
                        String Live = "";
                        String Persistence = "";

                        // create timestamp now
                        String timestamp = new java.util.Date().toString();

                        Document doc2 = Jsoup.connect("https://robertsspaceindustries.com/patch-notes")
                                        .get();

                        // System.out.println(DatabaseReset);

                        // get the link from post where "Star Citizen Alpha 3.18.1" or greater
                        Version = doc2.select("a[href*=Star-Citizen-Alpha-3]").first().attr("href");

                        // get the text from the post
                        Document doc3 = Jsoup.connect("https://robertsspaceindustries.com" + Version)
                                        .get();

                        Persistence = doc3.select(".rsi-markup .segment .content p").html();
                        Persistence = Persistence
                                        .substring(Persistence.indexOf("Long Term Persistence"),
                                                        Persistence.indexOf("Known Issues"))
                                        .replaceAll("\\<.*?>", "");

                        // try to get get the text from .rsi-markup .segment .content p
                        // if it fails get the text from .rsi-markup .segment .content
                        try {
                                // get the text from .rsi-markup .segment .content p
                                String text = doc3.select(".rsi-markup .segment .content p").html();
                                // get 20 characters after "VERSION" 3.18.1-LIVE.8430497
                                Version = text.substring(text.indexOf("VERSION"), text.indexOf("VERSION") + 40)
                                                .replaceAll("VERSION", "")
                                                .replaceAll("\\<.*?>", "").replaceAll("<span class=\"caps\"", "");
                                ;

                                Live = text.substring(text.indexOf("LIVE"), text.indexOf("LIVE") + 19)
                                                .replaceAll("LIVE", "")
                                                .replaceAll("\\<.*?>", "");
                                ;

                                // remove all bevore "Known Issues"
                                text = text.substring(text.indexOf("Known Issues")).replaceAll("\\<.*?>", "");

                                // remove all after "Feature Updates"
                                text = text.substring(0, text.indexOf("Feature Updates"));

                                // Version: 3.18.1-
                                // Live: .8430497
                                Main.RSSNews = "\nStar Citizen Alpha "
                                                + Version + "LIVE" + Live + "\n\n" + Persistence + "\n" + text + "\n"
                                                + timestamp;

                                FormatNews();

                                Main.RSSNews = "`" + Main.RSSNews + "`";

                                CheckandSaveLink(Version + "LIVE" + Live);
                                // post message to channel

                                // Version: 3.18.1-
                        } catch (Exception e) {
                                // get the text from .rsi-markup .segment .content
                                String text = doc3.select(".rsi-markup .segment .content").html();
                                // get 20 characters after "VERSION" 3.18.1-LIVE.8430497
                                Version = text.substring(text.indexOf("VERSION"), text.indexOf("VERSION") + 40)
                                                .replaceAll("VERSION", "")
                                                .replaceAll("\\<.*?>", "").replaceAll("<span class=\"caps\"", "");
                                ;

                                Live = text.substring(text.indexOf("LIVE"), text.indexOf("LIVE") + 19)
                                                .replaceAll("LIVE", "")
                                                .replaceAll("\\<.*?>", "");
                                ;

                                // remove all bevore "Known Issues"
                                text = text.substring(text.indexOf("Known Issues")).replaceAll("\\<.*?>", "");

                                // remove all after "Feature Updates"
                                text = text.substring(0, text.indexOf("Feature Updates"));

                                // Version: 3.18.1-
                                // Live: .8430497
                                Main.RSSNews = "\nStar Citizen Alpha "
                                                + Version + "LIVE" + Live + "\n\n" + Persistence + "\n" + text + "\n"
                                                + timestamp;

                                FormatNews();

                                Main.RSSNews = "`" + Main.RSSNews + "`";

                                CheckandSaveLink(Version + "LIVE" + Live);
                                // post message to channel

                        }

                } catch (Exception e) {
                        e.printStackTrace();
                }

                //

        }

        public static void FormatNews() {
                try {
                        // make a beatiful message from Main.RSSNews for discord
                        Main.RSSNews = Main.RSSNews.replaceAll("Star Citizen Alpha", "**Star Citizen Alpha**");
                        Main.RSSNews = Main.RSSNews.replaceAll("Known Issues", "**Known Issues**");
                        Main.RSSNews = Main.RSSNews.replaceAll("Feature Updates", "**Feature Updates**");
                        Main.RSSNews = Main.RSSNews.replaceAll("Bug Fixes", "**Bug Fixes**");
                        Main.RSSNews = Main.RSSNews.replaceAll("New Features", "**New Features**");
                        Main.RSSNews = Main.RSSNews.replaceAll("Balance", "**Balance**");
                        Main.RSSNews = Main.RSSNews.replaceAll("Technical", "**Technical**");
                        Main.RSSNews = Main.RSSNews.replaceAll("Gameplay", "**Gameplay**");
                        Main.RSSNews = Main.RSSNews.replaceAll("Ships and Vehicles", "**Ships and Vehicles**");
                        Main.RSSNews = Main.RSSNews.replaceAll("Locations", "**Locations**");
                        Main.RSSNews = Main.RSSNews.replaceAll("Weapons and Items", "**Weapons and Items**");
                        Main.RSSNews = Main.RSSNews.replaceAll("Core Tech", "**Core Tech**");
                        Main.RSSNews = Main.RSSNews.replaceAll("AI", "**AI**");
                        Main.RSSNews = Main.RSSNews.replaceAll("Ships", "**Ships**");
                        Main.RSSNews = Main.RSSNews.replaceAll("Vehicles", "**Vehicles**");
                        Main.RSSNews = Main.RSSNews.replaceAll("Weapons", "**Weapons**");
                        Main.RSSNews = Main.RSSNews.replaceAll("FPS Weapons", "**FPS Weapons**");
                        Main.RSSNews = Main.RSSNews.replaceAll("FPS Gadgets", "**FPS Gadgets**");
                        Main.RSSNews = Main.RSSNews.replaceAll("Long Term Persistence", "**Long Term Persistence**");
                        Main.RSSNews = Main.RSSNews.replaceAll("Starting aUEC", "**Starting aUEC**");
                } catch (Exception e) {
                }

        }

        public static void CheckandSaveLink(String Version) throws IOException {
                // read file from desktop /link.txt on linux /root/link.txt
                try {
                        String os = System.getProperty("os.name").toLowerCase();
                        // if link is in file ignore it, when link is not ini fil ewrite it
                        if (os.contains("win")) {
                                if (!new File(System.getProperty("user.home") + "/Desktop/version.txt").exists()) {
                                        new File(System.getProperty("user.home") + "/Desktop/version.txt")
                                                        .createNewFile();
                                        try (BufferedWriter bw = new BufferedWriter(
                                                        new FileWriter(System.getProperty("user.home")
                                                                        + "/Desktop/version.txt"))) {
                                                bw.write(Version);

                                        }
                                } else {
                                        try (BufferedReader br = new BufferedReader(
                                                        new FileReader(System.getProperty("user.home")
                                                                        + "/Desktop/version.txt"))) {
                                                String line;
                                                while ((line = br.readLine()) != null) {
                                                        if (line.equals(Version)) {
                                                                System.out.println(
                                                                                "Version " + Version + " is in file");
                                                                return;
                                                        }
                                                }
                                        }
                                        try (BufferedWriter bw = new BufferedWriter(
                                                        new FileWriter(System.getProperty("user.home")
                                                                        + "/Desktop/version.txt"))) {

                                                SendMessage.ToNewsChannel();

                                                bw.write(Version);
                                        }
                                }
                        } else {
                                if (!new File("/root/version.txt").exists()) {
                                        new File("/root/version.txt").createNewFile();
                                        try (BufferedWriter bw = new BufferedWriter(
                                                        new FileWriter("/root/version.txt"))) {
                                                SendMessage.ToNewsChannel();
                                                bw.write(Version);
                                        }
                                } else {
                                        try (BufferedReader br = new BufferedReader(
                                                        new FileReader("/root/version.txt"))) {
                                                String line;
                                                while ((line = br.readLine()) != null) {
                                                        if (line.equals(Version)) {
                                                                System.out.println("Version is in File " + Version
                                                                                + " Keine neuen Patchenotes");
                                                                return;
                                                        }
                                                }
                                        }
                                        try (BufferedWriter bw = new BufferedWriter(
                                                        new FileWriter("/root/version.txt"))) {
                                                // foreach server send message to channel where name is #📣rsi-news
                                                SendMessage.ToNewsChannel();
                                                bw.write(Version);
                                        }
                                }
                        }

                } catch (Exception e) {
                        e.printStackTrace();
                }

        }

        // async timer 1 hour tick 3600000
        public static void startTimer() {
                try {
                        new java.util.Timer().schedule(
                                        new java.util.TimerTask() {
                                                @Override
                                                public void run() {
                                                        getPatchNotes();
                                                        startTimer();
                                                }
                                        },
                                        3600000);
                } catch (Exception e) {
                        System.out.println("Error: " + e);
                }

        }

}
