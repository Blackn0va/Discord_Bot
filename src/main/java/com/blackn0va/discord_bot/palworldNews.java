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

public class palworldNews {
    public static String os = System.getProperty("os.name").toLowerCase();
    public static String VersionFile = "";
    public static String workingDir = System.getProperty("user.dir");

    public static String RSSNews = "";
    public static String Persistence = "";
    public static String Live = "";
    public static String Version = "";
    public static String link = "";
    public static String baseUrl = "https://store.steampowered.com/feeds/news/app/1623730/?cc=DE&l=german&snr=1_2108_9__2107";
    public static String Patchlink = "";
    public static String image = "";
    public static String title = "";
    public static String description = "";
    

    // main

    public static void GetLatestPatchLink() throws IOException {
        // get the latest hyperlink from baseurl where title contains 'Patch'
        Document doc = Jsoup.connect(baseUrl).get();
        Elements items = doc.select("item");
        for (Element item : items) {
            String title = item.select("title").first().text();
            if (title.contains("Patch")) {
                Patchlink = item.select("link").first().text();
                break;  // stop after finding the first patch
            }
        }
        CheckandSaveLink(null);
    }


    public static void CheckandSaveLink(String Version) throws IOException {
        // get the applications working directory
        workingDir = System.getProperty("user.dir");

        // check if the version file exists
        File file = new File(workingDir + "\\PalworldVersion.txt");
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
            System.out.println("Neuer Palworld Patch wurde gefunden!");
            GetPatchNotes();

        } else {
            // GetPatchNotes();

            System.out.println("Kein Neuer Palworld Patch gefunden!");
        }

    }

    public static void GetPatchNotes() throws IOException {
        Document doc = Jsoup.connect(baseUrl).get();
        Elements items = doc.select("item");
        for (Element item : items) {
            title = item.select("title").first().text();
            if (title.contains("Patch")) {
                description = item.select("description").first().text();
                link = item.select("link").first().text();
                image = item.select("enclosure").attr("url");
                break;  // stop after finding the first patch
            }
        }
  
        //remove html maker from description
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

        

        List<String> splitStrings = splitString(RSSNews, 1900);
        List<String> finalStrings = new ArrayList<>();
        for (String s : splitStrings) {
            finalStrings.addAll(splitString(s, 1900));
        }

        for (String s : finalStrings) {
            // Posten auf discord
            //SendMessage.ToNewsChannel("```prolog\n" + s + "\n```");
            //remove Date from title 2024/02/07
            title = title.substring(11);
            
            SendMessage.toChannelWithLink("1205879327200378910", title, "```prolog\n" + s + "\n```", java.awt.Color.GREEN, link, image);
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
