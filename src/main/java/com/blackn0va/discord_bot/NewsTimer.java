package com.blackn0va.discord_bot;

import java.util.Timer;
import java.util.TimerTask;

public class NewsTimer {

    public static void Starten() {

        try {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    rssNews.getNews();
                }
            }, 0, 1000 * 60 * 60 * 24);
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}