package com.blackn0va.discord_bot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import net.dv8tion.jda.api.entities.Activity;

public class statusfeed {

    public static void getStatus() {

        try {
            String Degraded = "";
            String operational = "";
            String Outage = "";

            Document doc = Jsoup.connect("https://status.robertsspaceindustries.com/")
                    .get();

            try {
                // get the value of the first
                // div.system.flex.flex-row.justify-between.degraded-performance
                Degraded = doc.select("div.system.flex.flex-row.justify-between.degraded-performance").text();
                operational = doc.select("div.system.flex.flex-row.justify-between.operational").text();
                Outage = doc.select("div.system.flex.flex-row.justify-between.partial-outage").text();

                // System.out.println(Degraded + " \n" + operational + " \n" + outage);

            } catch (Exception e) {

            }

            Main.bauplan.getPresence()
                    .setActivity(Activity.playing(operational
                            .replaceAll("Platform Operational", "|Platform ist Operational_______________|")
                            .replaceAll("Persistent Universe Operational",
                                    "|Persistente Universum ist Operational____|")
                            .replaceAll("Electronic Access Operational", "|Electronic Access ist Operational_______|")
                            + Degraded + Outage));

        } catch (Exception e) {
        }

    }

    // async timer 10 minutes tick 600000
    public static void startTimer() {
        try {
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            getStatus();
                            startTimer();
                        }
                    },
                    600000);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

    }

}
