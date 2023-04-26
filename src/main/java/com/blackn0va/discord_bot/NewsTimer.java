package com.blackn0va.discord_bot;

import java.util.Timer;
import java.util.TimerTask;

public class NewsTimer {

    public static void Starten() {

        try {
            // make a timer all 1 Hours if it tiks, then get the news
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        rssNews.getPatchNotes();
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            }, 0, 3600000);

 

        } catch (Exception e) {
            System.out.println(e);
        }

    }
}