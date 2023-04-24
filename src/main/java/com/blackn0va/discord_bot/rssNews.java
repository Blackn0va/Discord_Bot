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

        public static String link = "";

        public static void getPatchNotes() {

                try {
                        String Version = "";
                        String Live = "";
 
                        // create timestamp now
                        String timestamp = new java.util.Date().toString();

                        Document doc2 = Jsoup.connect("https://robertsspaceindustries.com/patch-notes")
                                        .get();

                        // get the link from post where "Star Citizen Alpha 3.18.1" or greater
                        link = doc2.select("a[href*=Star-Citizen-Alpha-3]").first().attr("href");

                        // Save link

                        // https://robertsspaceindustries.com/comm-link//19258-Star-Citizen-Alpha-3182
                        // System.out.println("https://robertsspaceindustries.com" + link); // Print
                        // link

                        // get the text from the post
                        Document doc3 = Jsoup.connect("https://robertsspaceindustries.com" + link)
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
                        NachrichtenReaction.RSSNews = "\nStar Citizen Alpha "
                                        + Version + "LIVE" + Live + "\n\n" + text + "\n" + timestamp;

                        NachrichtenReaction.RSSNews = NachrichtenReaction.RSSNews + "\n\n" + "Last Update: "
                                        + timestamp;

                        CheckandSaveLink("https://robertsspaceindustries.com" + link);
                        // post message to channel

                } catch (Exception e) {
                        e.printStackTrace();
                }

                //

        }

        public static void CheckandSaveLink(String link) throws IOException {
                // read file from desktop /link.txt on linux /root/link.txt
                try {
                        String os = System.getProperty("os.name").toLowerCase();
                        // if link is in file ignore it, when link is not ini fil ewrite it
                        if (os.contains("win")) {
                                if (!new File(System.getProperty("user.home") + "/Desktop/link.txt").exists()) {
                                        new File(System.getProperty("user.home") + "/Desktop/link.txt").createNewFile();
                                        try (BufferedWriter bw = new BufferedWriter(
                                                        new FileWriter(System.getProperty("user.home")
                                                                        + "/Desktop/link.txt"))) {
                                                bw.write(link);

                                        }
                                } else {
                                        try (BufferedReader br = new BufferedReader(
                                                        new FileReader(System.getProperty("user.home")
                                                                        + "/Desktop/link.txt"))) {
                                                String line;
                                                while ((line = br.readLine()) != null) {
                                                        if (line.equals(link)) {
                                                                System.out.println("line is in file");
                                                                return;
                                                        }
                                                }
                                        }
                                        try (BufferedWriter bw = new BufferedWriter(
                                                        new FileWriter(System.getProperty("user.home")
                                                                        + "/Desktop/link.txt"))) {
                                                Main.bauplan.getTextChannelById("1099111135896162425")
                                                                .sendMessage("@scnews " + NachrichtenReaction.RSSNews)
                                                                .queue();

                                                NachrichtenReaction.RSSNews = "";
                                                bw.write(link);
                                        }
                                }
                        } else {
                                if (!new File("/root/link.txt").exists()) {
                                        new File("/root/link.txt").createNewFile();
                                        try (BufferedWriter bw = new BufferedWriter(new FileWriter("/root/link.txt"))) {
                                                Main.bauplan.getTextChannelById("1099111135896162425")
                                                                .sendMessage("@scnews " + NachrichtenReaction.RSSNews)
                                                                .queue();

                                                NachrichtenReaction.RSSNews = "";
                                                bw.write(link);
                                        }
                                } else {
                                        try (BufferedReader br = new BufferedReader(new FileReader("/root/link.txt"))) {
                                                String line;
                                                while ((line = br.readLine()) != null) {
                                                        if (line.equals(link)) {
                                                                System.out.println("line is in file");
                                                                return;
                                                        }
                                                }
                                        }
                                        try (BufferedWriter bw = new BufferedWriter(new FileWriter("/root/link.txt"))) {
                                                Main.bauplan.getTextChannelById("1099111135896162425")
                                                                .sendMessage("@scnews " + NachrichtenReaction.RSSNews)
                                                                .queue();

                                                NachrichtenReaction.RSSNews = "";
                                                bw.write(link);
                                        }
                                }
                        }

                } catch (Exception e) {
                        e.printStackTrace();
                }

        }

}
