package com.blackn0va.discord_bot;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import net.dv8tion.jda.api.entities.Activity;

public class statusfeed {

    public static void getStatus() throws IOException{

        try {
            String Status = "";

            Document doc = Jsoup.connect("https://status.robertsspaceindustries.com/")
                    .get();

            try {
                Elements components = doc.select("div.component");
                for (Element component : components) {
                    String componentName = component.text();
                    String componentStatus = component.select("span.component-status").attr("data-status");

                    //Status += "|" + componentName + " ist " + (componentStatus.equals("operational") ? "Operational" : componentStatus) + "_______________|";

                    Status += "|" + componentName +  "|" ;
                }

                //System.out.println(Status);

            } catch (Exception e) {

            }

            System.out.println("Serverstatus: " + Status);
            Main.bauplan.getPresence()
                    .setActivity(Activity.customStatus(Status));

            System.out.println("Serverstatus wurde aktualisiert. " + Status);
            WriteLogs.writeLog("Serverstatus wurde aktualisiert. " + Status);

            Status = "";

        } catch (Exception e) {
        }

    }


}