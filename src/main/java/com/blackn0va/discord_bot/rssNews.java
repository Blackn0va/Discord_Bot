package com.blackn0va.discord_bot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class rssNews {
        public static String os = System.getProperty("os.name").toLowerCase();
        public static String VersionFile = "";
        public static String workingDir = System.getProperty("user.dir");

        public static String RSSNews = "";
        public static String Persistence = "";
        public static String Live = "";
        public static String Version = "";
        public static String Link = "";
        public static String baseUrl = "https://robertsspaceindustries.com/patch-notes";
        public static String Patchlink = "https://robertsspaceindustries.com";

        // main

        public static void GetLatestPatchLink() throws IOException {
                // get the latest link from baseurl
                Document doc = Jsoup.connect(baseUrl).get();
                Elements links = doc.select("a[href]");
                for (int i = 0; i < links.size(); i++) {
                        if (links.get(i).text().contains("Alpha")) {
                                Link = links.get(i).attr("href");
                                Patchlink = "https://robertsspaceindustries.com" + Link;
                                break;
                        }
                }
                CheckandSaveLink(null);

        }

        public static void CheckandSaveLink(String Version) throws IOException {
                // get the applications working directory
                workingDir = System.getProperty("user.dir");

                // check if the version file exists
                File file = new File(workingDir + "\\Version.txt");
                if (!file.exists()) {
                        file.createNewFile();
                }

                // read the version file
                BufferedReader br = new BufferedReader(new FileReader(file));
                String st;
                while ((st = br.readLine()) != null) {
                        VersionFile = st;
                }
                br.close();

                // check if the PatchLink file is the same as the latest PatchLink
                if (!VersionFile.equals(Patchlink)) {
                        // if not save the latest PatchLink to the PatchLink file
                        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                        writer.write(Patchlink);
                        writer.close();
                        System.out.println("Neuer Patch wurde gefunden!");
                        GetPatchNotes();

                } else {
                        //GetPatchNotes();

                        System.out.println("Kein Neuer Patch gefunden!");
                }

        }

        public static void GetPatchNotes() throws IOException {
                // get the text from .rsi-markup .segment .content
                Document doc = Jsoup.connect(Patchlink).get();
                Elements links = doc.select(".rsi-markup .segment .content");
                for (int i = 0; i < links.size(); i++) {
                        RSSNews = links.get(i).html();

                }

                RSSNews = RSSNews.replaceAll("<li>", "°");

                // remove html tags
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



                List<String> splitStrings = splitString(RSSNews, 1900);
                List<String> finalStrings = new ArrayList<>();
                for (String s : splitStrings) {
                        finalStrings.addAll(splitString(s, 1900));
                }

                for (String s : finalStrings) {
                        // Posten auf discord
                        SendMessage.ToNewsChannel("```prolog\n"  + s + "\n```");
                }

        }

        public static List<String> splitString(String input, int length) {
                List<String> result = new ArrayList<>();
                int index = 0;
                while (index < input.length()) {
                        result.add(input.substring(index, Math.min(index + length, input.length())));
                        index += length;
                }
                return result;
        }

}
