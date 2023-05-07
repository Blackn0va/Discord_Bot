package com.blackn0va.discord_bot;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class SendMessage {
    public static void ToNewsChannel() {
        try {
            // foreach server send message to channel where name is #📣rsi-news
            for (Guild guild : Main.bauplan.getGuilds()) {
                for (TextChannel channel : guild.getTextChannels()) {
                    if (channel.getName().equals("📣rsi-news")) {
                        channel.sendMessage("@Star Citizen #📣rsi-news"
                                + Main.RSSNews).queue();
                    }
                }
            }

            Main.RSSNews = "";
        } catch (Exception e) {
            System.out.println("Error: " + e);
            WriteLogs.writeLog("Error: " + e);
        }

    }

    // async send message to channel
    public static void ToChannel(String channelID, String message) {
        try {
            Main.bauplan.getTextChannelById(channelID).sendMessage(message).queue();
        } catch (Exception e) {
            System.out.println("Error: " + e);
            WriteLogs.writeLog("Error: " + e);
        }

    }

    // async send message to User
    public static void ToUser(String userID, String message) {
        try {
            Main.bauplan.getUserById(userID).openPrivateChannel().queue((channel) -> {
                channel.sendMessage(message).queue();
            });
        } catch (Exception e) {
            System.out.println("Error: " + e);
            WriteLogs.writeLog("Error: " + e);
        }

    }

}
