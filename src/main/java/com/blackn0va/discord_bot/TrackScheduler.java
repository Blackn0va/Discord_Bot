package com.blackn0va.discord_bot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.managers.AudioManager;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter {
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;
    private final AudioManager audioManager;

    public TrackScheduler(AudioPlayer player, AudioManager audioManager) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
        this.audioManager = audioManager;
    }

    public void queue(AudioTrack track) {
        if (!player.startTrack(track, true)) {
            queue.offer(track);
        }
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            nextTrack();
        } else {
            audioManager.closeAudioConnection();
        }

        // Lösche die Datei nach dem Abspielen
        File file = new File(track.getIdentifier());
        if (file.exists()) {
            file.delete();
        }
    }

    public void nextTrack() {
        AudioTrack nextTrack = queue.poll();
        if (nextTrack != null) {
            player.startTrack(nextTrack, false);
        } else {
            audioManager.closeAudioConnection();
        }
    }
}