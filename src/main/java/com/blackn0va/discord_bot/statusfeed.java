package com.blackn0va.discord_bot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import net.dv8tion.jda.api.entities.Activity;

public class statusfeed {

    public static void getStatus() {

        try {
            String Degraded = "";
            String operational = "";
            String outage = "";

            Document doc = Jsoup.connect("https://status.robertsspaceindustries.com/")
                    .get();

            try {
                // get the value of the first
                // div.system.flex.flex-row.justify-between.degraded-performance
                Degraded = doc.select("div.system.flex.flex-row.justify-between.degraded-performance").text();
                operational = doc.select("div.system.flex.flex-row.justify-between.operational").text();
                outage = doc.select("div.system.flex.flex-row.justify-between.partial-outage").text();

                // System.out.println(Degraded + " \n" + operational + " \n" + outage);

            } catch (Exception e) {

            }

            Main.RSIStatus = "Operational: " + operational + "\n" + "Eingeschränkt: " + Degraded + "\n" + "Ausfälle: "
                    + outage;
            Main.bauplan.getPresence().setActivity(Activity.playing(Degraded + " |" + operational + " | " + outage));

            // change in discord the "about me text"

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
