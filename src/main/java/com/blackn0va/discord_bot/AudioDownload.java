package com.blackn0va.discord_bot;

import net.dv8tion.jda.api.managers.AudioManager;
import java.io.File;

public class AudioDownload {

    public void downloadAudio(String link, String basePath, String voiceChannelId, AudioManager channel) {
        new Thread(() -> {
            try {
                WriteLogs.writeLog("Starte Download des Audios: " + link);

                // Ermitteln des nächsten verfügbaren Dateinamens
                String nextOutputPath = getNextAvailableFileName(basePath);

                WriteLogs.writeLog("Speichere Audio unter: " + nextOutputPath);

                // Download audio from YouTube
                ProcessBuilder pb = new ProcessBuilder("youtube-dl", "-f bestaudio", "--extract-audio", "--audio-format",
                        "wav", link, "-o",
                        nextOutputPath,
                        "--no-continue", "--no-part");
                Process process = pb.start();

                // Warten, bis der Download abgeschlossen ist
                int exitCode = process.waitFor();
                if (exitCode == 0) {
                    WriteLogs.writeLog("Download abgeschlossen: " + nextOutputPath);
                    AudioManager audioManager = channel.getGuild().getAudioManager();
                    AudioPlayer player = new AudioPlayer(audioManager);
                    player.loadAndPlay(audioManager, nextOutputPath, voiceChannelId);
                    

                } else {
                    WriteLogs.writeLog("Fehler beim Downloaden des Audios: Prozess beendete mit Code " + exitCode);
                }

            } catch (Exception e) {
                WriteLogs.writeLog("Fehler beim Downloaden des Audios: " + e.getMessage());
            }
        }).start();
    }

    private synchronized String getNextAvailableFileName(String basePath) {
        int counter = 1;
        String filePath;
        do {
            filePath = basePath + counter + ".wav";
            counter++;
        } while (new File(filePath).exists());
        return filePath;
    }
}