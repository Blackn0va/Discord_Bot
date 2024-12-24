package com.blackn0va.discord_bot;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import net.dv8tion.jda.api.managers.AudioManager;

public class AudioPlayer {
    private final AudioPlayerManager playerManager;
    public static com.sedmelluq.discord.lavaplayer.player.AudioPlayer player;
    private final TrackScheduler trackScheduler;

    public AudioPlayer(AudioManager audioManager) {
        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
        this.player = playerManager.createPlayer();
        this.trackScheduler = new TrackScheduler(player, audioManager);
        player.addListener(trackScheduler);

        // Standard settings
        player.setFrameBufferDuration(40000);
        player.setVolume(100);

        WriteLogs.writeLog("AudioPlayer initialisiert");
    }

    public void loadAndPlay(AudioManager audioManager, String filePath, String channelId) {
        new Thread(() -> {
            playerManager.loadItem(filePath, new AudioLoadResultHandler() {
                @Override
                public void trackLoaded(AudioTrack track) {
                    WriteLogs.writeLog("Track geladen: " + track.getInfo().title);
                    WriteLogs.writeLog("Starte Wiedergabe des Audios: " + track.getInfo().title);
                    audioManager.openAudioConnection(audioManager.getGuild().getVoiceChannelById(channelId));
                    //add to queue
                    trackScheduler.queue(track);
                    WriteLogs.writeLog("Wiedergabe des Audios gestartet: " + track.getInfo().title);
                }

                @Override
                public void playlistLoaded(AudioPlaylist playlist) {
                    for (AudioTrack track : playlist.getTracks()) {
                        trackScheduler.queue(track);
                    }
                    WriteLogs.writeLog("Playlist geladen: " + playlist.getName());
                }

                @Override
                public void noMatches() {
                    WriteLogs.writeLog("Keine Übereinstimmungen gefunden für " + filePath);
                }

                @Override
                public void loadFailed(FriendlyException exception) {
                    WriteLogs.writeLog("Laden fehlgeschlagen: " + exception.getMessage());
                }
            });

            audioManager.setSendingHandler(new AudioPlayerSendHandler(player));
        }).start();
    }

    // skipTrack
    public void skipTrack() {
        trackScheduler.nextTrack();
    }
}