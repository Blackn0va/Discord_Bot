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

            // get the first flex flex-row justify-between operational from doc
            Platform = doc.select("div.flex.flex-row.justify-between.operational").first().text();
            if (Platform.contains("Operational")) {
                Platform = "Platform: Operational :white_check_mark:";

            } else {
                Platform = "Platform: Degraded Performance :x:";

            }
            // get the second flex flex-row justify-between operational from doc
            Persistent = doc.select("div.flex.flex-row.justify-between.operational").get(1).text();
            if (Persistent.contains("Operational")) {
                Persistent = "Persistent: Operational :white_check_mark:";

            } else {
                Persistent = "Persistent: Degraded Performance :x:";

            }

            // get the third flex flex-row justify-between operational from doc
            Electronic = doc.select("div.flex.flex-row.justify-between.operational").get(2).text();
            if (Electronic.contains("Operational")) {
                Electronic = "Electronic Access: Operational :white_check_mark:";

            } else {
                Electronic = "Electronic Access: Degraded Performance :x:";

            }

            NachrichtenReaction.Status = "\n" + Platform + "\n" + Persistent + "\n" + Electronic;
            Main.bauplan.getPresence().setActivity(Activity.playing(Platform));


        } catch (Exception e) {
        }

    }

    public static void test() {
System.err.println("test");
    }



    //async timer 10 minutes tick 600000
    public static void startTimer() {
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        getStatus();
                        startTimer();
                    }
                },
                600000
        );
    }


}
 