package com.blackn0va.discord_bot;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class rssNews {

    public static void getPatchNotes() {

        try {
            String Version = "";
            String Live = "";

            String DatabaseReset = "";
            String DatabaseResetTruefalse = "";

            Document doc2 = Jsoup.connect("https://robertsspaceindustries.com/patch-notes")
                    .get();

            // get the link from post where "Star Citizen Alpha 3.18.1" or greater
            String link = doc2.select("a[href*=Star-Citizen-Alpha-3]").first().attr("href");

            // get the text from the post
            Document doc3 = Jsoup.connect("https://robertsspaceindustries.com" + link)
                    .get();

            // get the text from div.content and remove the html tags
            String text = doc3.select("div.content").html();

            // get 20 characters after "VERSION" 3.18.1-LIVE.8430497
            Version = text.substring(text.indexOf("VERSION"), text.indexOf("VERSION") + 40).replaceAll("VERSION", "");
                    

            // remove html tags
            Version = Version.replaceAll("\\<.*?>", "").replaceAll("<span class=\"caps\"", "");

            Live = text.substring(text.indexOf("LIVE"), text.indexOf("LIVE") + 19).replaceAll("LIVE", "");

            // remove all html tags
            Live = Live.replaceAll("\\<.*?>", "");

            DatabaseReset = text.substring(text.indexOf("Database Reset:") + 15, text.indexOf("Database Reset:") + 19);
            

            // remove htl tags
            text = text.replaceAll("\\<.*?>", "");

            // remove all after "Feature Updates"
            text = text.substring(0, text.indexOf("Feature Updates"));

            // remove all bevore "Known Issues"

            text = text.substring(text.indexOf("Known Issues"));

            //Version: 3.18.1-
            //Live: .8430497
            NachrichtenReaction.RSSNews = "\nStar Citizen Alpha "
                    + Version + "LIVE" + Live + ".\n\n"
                    + "Database Reset: " + DatabaseReset + "\n\n" + text;
 

        } catch (Exception e) {
            e.printStackTrace();
        }

        //

    }

}
