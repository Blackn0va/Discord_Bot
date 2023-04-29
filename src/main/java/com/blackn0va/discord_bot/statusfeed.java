package com.blackn0va.discord_bot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import net.dv8tion.jda.api.entities.Activity;

public class statusfeed {

    public static void getStatus() {

        try {

            String Platform = "";
            String Persistent = "";
            String Electronic = "";

            Document doc = Jsoup.connect("https://status.robertsspaceindustries.com/")
                    .get();

            // try to get the system flex flex-row justify-between degraded-performance, if
            // this is not found in doc then try system flex flex-row justify-between
            // operational
            try {
                Platform = doc.select("div.system.flex.flex-row.justify-between.degraded-performance").text();
            } catch (Exception e) {
                Platform = doc.select("div.system.flex.flex-row.justify-between.operational").text();
            }

            // NachrichtenReaction.Status = "\n" + Platform + "\n" + Persistent + "\n" +
            // Electronic;
            Main.bauplan.getPresence().setActivity(Activity.playing(Platform));

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
