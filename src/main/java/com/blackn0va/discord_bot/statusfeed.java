package com.blackn0va.discord_bot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import net.dv8tion.jda.api.entities.Activity;

public class statusfeed {

    public static void getStatus() {

        try {

            String PlatformWork = "";
            String PlatformNotWorking = "";

            Document doc = Jsoup.connect("https://status.robertsspaceindustries.com/")
                    .get();

            try {
                PlatformNotWorking = doc.select("div.system.flex.flex-row.justify-between.degraded-performance").text();

            } catch (Exception e) {
                PlatformWork = doc.select("div.system.flex.flex-row.justify-between.operational").text();

            }

            Main.bauplan.getPresence().setActivity(Activity.playing(PlatformNotWorking + PlatformWork));

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
