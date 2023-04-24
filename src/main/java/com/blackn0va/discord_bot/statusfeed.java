package com.blackn0va.discord_bot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class statusfeed {

    public static void getStatus() {

        try {
            String Platform = "";
            String Persistent = "";
            String Electronic = System.getProperty("os.name").toLowerCase();

            // create timestamp now
            String timestamp = new java.util.Date().toString();

            Document doc = Jsoup.connect("https://status.robertsspaceindustries.com/")
                    .get();

            // get the first flex flex-row justify-between operational from doc
            Platform = doc.select("div.flex.flex-row.justify-between.operational").first().text();

            // get the second flex flex-row justify-between operational from doc
            Persistent = doc.select("div.flex.flex-row.justify-between.operational").get(1).text();

            // get the third flex flex-row justify-between operational from doc
            Electronic = doc.select("div.flex.flex-row.justify-between.operational").get(2).text();

            System.out.println(Platform); // Print Platform
            System.out.println(Persistent); // Print Persistent
            System.out.println(Electronic); // Print Electronic


 
            
        } catch (Exception e) {
        }

    }

}
