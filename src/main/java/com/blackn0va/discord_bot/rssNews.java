package com.blackn0va.discord_bot;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class rssNews {

    public static void getPatchNotes() {

        try {
            String Version = "";
            String Live = "";

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
            Version = text.substring(text.indexOf("VERSION"), text.indexOf("VERSION") + 40).replaceAll("VERSION", "")
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
                    + Version + "LIVE" + Live + "\n\n" + text;

            // post message to channel

        } catch (Exception e) {
            e.printStackTrace();
        }

        //

    }

}
