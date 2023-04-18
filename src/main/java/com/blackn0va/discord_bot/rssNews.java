package com.blackn0va.discord_bot;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class rssNews {

    public static void getNews() {

        try {

            Document doc = Jsoup.connect("https://robertsspaceindustries.com/comm-link//19229-Star-Citizen-Alpha-3181")
                    .get();
            // String rss = doc.body().text();

            // Get the text from div.content and remove the html tags
            String text = doc.select("div.content").html();
            String Version = "";
            String DatabaseReset = "";

            // remove htl tags
            text = text.replaceAll("\\<.*?>", "");

            // get the version number "VERSION 3.*"
            Version = text.substring(text.indexOf("VERSION"), text.indexOf("VERSION") + 27);

            // get 4 Characters after "Database Reset:"
            DatabaseReset = text.substring(text.indexOf("Database Reset:") + 15, text.indexOf("Database Reset:") + 19);

            // remove all after "Feature Updates"
            text = text.substring(0, text.indexOf("Feature Updates"));

            // remove all bevore "Known Issues"
            text = text.substring(text.indexOf("Known Issues"));

            NachrichtenReaction.RSSNews = "\nStar Citizen Alpha " + Version.replace("VERSION", "") + "\n\n"
                    + "Database Reset: " + DatabaseReset + "\n\n" + text;

        }

        catch (Exception e) {
            e.printStackTrace();
        }

    }

}
