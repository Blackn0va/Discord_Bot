/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.blackn0va.discord_bot;

import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.fasterxml.jackson.core.filter.TokenFilter;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import scala.util.parsing.combinator.token.Tokens.Token;

/**
 *
 * @author Black
 */
public class Main {

    public static String openaitoken = "";
    public static JDA bauplan;
    public static String RegelnChannelID = "";
    public static String RegelnPostID = "";
    public static String status = "";
    public static String token = "";
    public static String desktopPath = "";
    public static String GPTChannelID = "";
    public static String SCNewsChannelID = "";
    public static String answer = "";
    public static String RSSNews = "";
    public static String RSIStatus = "";
    public static String workingDir = System.getProperty("user.dir");
    public static String os = System.getProperty("os.name").toLowerCase();

    public static String TokenFile = "";

    // Public Strings für Berechtigungen
    public static String RegelnAkzeptiert = "";

    /**
     * @param args the command line arguments
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        // rssNews.getNews();
        // Betriebssystem bestimmen, wegen der unterschiedlichen Pfade
        System.out.println("Erkanntes Betriebssystem: " + os);
        WriteLogs.writeLog("Erkanntes Betriebssystem: " + os);

        // rssNews.getNews();

        if (os.contains("win")) {
            try { // Betriebssystem ist Windows basiert

                TokenFile = workingDir + "\\" + "Token.txt";

                // if file not exists, create it
                if (!new File(TokenFile).exists()) {
                    new File(TokenFile).createNewFile();
                    // insert 6 lines in the file
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter(TokenFile))) {
                        bw.write("token");
                        bw.newLine();
                        bw.write("openaitoken");
                        bw.newLine();
                        bw.write("Regeln_Akzeptiert_Gruppen_Name");
                        bw.newLine();

                    }
                } else {
                    File file = new File(TokenFile);
                    // line one of the file is token and line 2 is openaitoken
                    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                        token = br.readLine();
                        openaitoken = br.readLine();
                        RegelnAkzeptiert = br.readLine();

                    }
                }

            } catch (IOException e) {
                System.out.println("Exception caught =" + e.getMessage());
            }
        } else if (os.contains("nix") || os.contains("aix") || os.contains("nux")) {
            try { // Betriebssystem ist Linux/Unix basiert

                TokenFile = workingDir + "/" + "Token.txt";

                if (!new File(TokenFile).exists()) {
                    new File(TokenFile).createNewFile();
                    // insert 6 lines in the file
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter(TokenFile))) {
                        bw.write("token");
                        bw.newLine();
                        bw.write("openaitoken");
                        bw.newLine();
                        bw.write("Regeln_Akzeptiert_Gruppen_Name");
                        bw.newLine();
                    }
                } else {
                    try (BufferedReader br = new BufferedReader(new FileReader(TokenFile))) {
                        token = br.readLine();
                        openaitoken = br.readLine();
                        RegelnAkzeptiert = br.readLine();
                    }
                }

            } catch (IOException e) {
                System.out.println("Exception caught =" + e.getMessage());
                WriteLogs.writeLog("Exception caught =" + e.getMessage());
            }

        }

        // JDA bauplan = JDABuilder.createDefault(token).build();
        bauplan = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                        GatewayIntent.GUILD_EMOJIS_AND_STICKERS, GatewayIntent.MESSAGE_CONTENT,
                        GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES,
                        GatewayIntent.DIRECT_MESSAGE_TYPING, GatewayIntent.GUILD_WEBHOOKS,
                        GatewayIntent.GUILD_VOICE_STATES)
                .build();

        bauplan.getPresence().setStatus(OnlineStatus.ONLINE);

        //
        bauplan.awaitReady();

        // Listener starten
        bauplan.addEventListener(new NachrichtenReaction());
        bauplan.addEventListener(new GiveRole());

        try {
            GPTChannelID = bauplan.getTextChannelsByName("chatgpt", true).get(0).getId();
            SCNewsChannelID = bauplan.getTextChannelsByName("📣rsi-news", true).get(0).getId();

            // System.out.println(RegelnAkzeptiert);

        } catch (Exception e) {
        }

        // start Timer for News and Status
        rssNews.startTimer();
        statusfeed.startTimer();

        // initial Status
        statusfeed.getStatus();

        // initial News
        rssNews.getPatchNotes();

        // WriteLogs.startTimer();

    }

}
