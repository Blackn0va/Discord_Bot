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

                        // create timestamp now
                        String timestamp = new java.util.Date().toString();

                        Document doc2 = Jsoup.connect("https://robertsspaceindustries.com/patch-notes")
                                        .get();

                        // get the link from post where "Star Citizen Alpha 3.18.1" or greater
                        Version = doc2.select("a[href*=Star-Citizen-Alpha-3]").first().attr("href");

                        // get the text from the post
                        Document doc3 = Jsoup.connect("https://robertsspaceindustries.com" + Version)
                                        .get();

                        // get the text from div.content and remove the html tags
                        String text = doc3.select("div.content").html();

                        // get 20 characters after "VERSION" 3.18.1-LIVE.8430497
                        Version = text.substring(text.indexOf("VERSION"), text.indexOf("VERSION") + 40)
                                        .replaceAll("VERSION", "")
                                        .replaceAll("\\<.*?>", "").replaceAll("<span class=\"caps\"", "");
                        ;

                        Live = text.substring(text.indexOf("LIVE"), text.indexOf("LIVE") + 19).replaceAll("LIVE", "")
                                        .replaceAll("\\<.*?>", "");
                        ;

                        // remove all bevore "Known Issues"
                        text = text.substring(text.indexOf("Known Issues")).replaceAll("\\<.*?>", "");

                        // remove all after "Feature Updates"
                        text = text.substring(0, text.indexOf("Feature Updates"));

                        // Version: 3.18.1-
                        // Live: .8430497
                        Main.RSSNews = "\nStar Citizen Alpha "
                                        + Version + "LIVE" + Live + "\n\n" + text + "\n" + timestamp;

                                        Main.RSSNews = Main.RSSNews + "\n\n" + "Last Update: "
                                        + timestamp;

                        CheckandSaveLink(Version + "LIVE" + Live);
                        // post message to channel

                } catch (Exception e) {
                        e.printStackTrace();
                }

                //

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
                                                                System.out.println("Version is in file");
                                                                return;
                                                        }
                                                }
                                        }
                                        try (BufferedWriter bw = new BufferedWriter(
                                                        new FileWriter(System.getProperty("user.home")
                                                                        + "/Desktop/version.txt"))) {
                                                Main.bauplan.getTextChannelById(Main.SCNewsChannelID)
                                                                .sendMessage("@Star Citizen #📣rsi-news " + Main.RSSNews)
                                                                .queue();

                                                                Main.RSSNews = "";
                                                bw.write(Version);
                                        }
                                }
                        } else {
                                if (!new File("/root/version.txt").exists()) {
                                        new File("/root/version.txt").createNewFile();
                                        try (BufferedWriter bw = new BufferedWriter(
                                                        new FileWriter("/root/version.txt"))) {
                                                Main.bauplan.getTextChannelById(Main.SCNewsChannelID)
                                                                .sendMessage("@Star Citizen #📣rsi-news " + Main.RSSNews)
                                                                .queue();

                                                                Main.RSSNews = "";
                                                bw.write(Version);
                                        }
                                } else {
                                        try (BufferedReader br = new BufferedReader(
                                                        new FileReader("/root/version.txt"))) {
                                                String line;
                                                while ((line = br.readLine()) != null) {
                                                        if (line.equals(Version)) {
                                                                System.out.println("Keine neuen Patchenotes");
                                                                return;
                                                        }
                                                }
                                        }
                                        try (BufferedWriter bw = new BufferedWriter(
                                                        new FileWriter("/root/version.txt"))) {
                                                Main.bauplan.getTextChannelById(Main.SCNewsChannelID)
                                                                .sendMessage("@Star Citizen #📣rsi-news " + Main.RSSNews)
                                                                .queue();

                                                                Main.RSSNews = "";
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
