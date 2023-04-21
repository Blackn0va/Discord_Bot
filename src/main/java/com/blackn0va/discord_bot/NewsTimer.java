package com.blackn0va.discord_bot;

import java.util.Timer;
import java.util.TimerTask;

public class NewsTimer {

    public static void Starten() {

        try {
//make a timer all 6 Hours if it tiks, then get the news 21600000
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    //rssNews.getPatchNotes();
                    //System.out.println("NewsTimer: News updated");

                }
            }, 0, 20);

        } catch (Exception e) {
            System.out.println(e);
        }

    }
}