package com.blackn0va.discord_bot;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class DiscordGiveRole extends ListenerAdapter {
    // Erstellen Sie eine Warteschlange für Ereignisse, bei denen eine Reaktion
    // hinzugefügt wird
    Queue<GenericMessageReactionEvent> ReactionAddeventQueue = new LinkedList<>();
    // Erstellen Sie eine Warteschlange für Ereignisse, bei denen eine Reaktion
    // entfernt wird
    Queue<GenericMessageReactionEvent> ReactionRemoveQueue = new LinkedList<>();
    Map<Long, Integer> currentPageNums = new HashMap<>();

    // Erstellen Sie eine Warteschlange für ButtonInteractionEvent
    Queue<ButtonInteractionEvent> buttonClickQueue = new LinkedList<>();

    @Override
    public void onReady(ReadyEvent ereignis) {
        ereignis.getJDA().getGuilds().forEach(guild -> {
            // register command only for guild where ID is 686179587411148820 and
            // 284015562382901248
            if (guild.getId().equals("686179587411148820") || guild.getId().equals("284015562382901248")) {
                // Register slash command
                // command play Link to a youtube video
                guild.upsertCommand("play", "Play a youtube video")
                        .addOption(OptionType.STRING, "link", "Link to a youtube video", true)
                        .queue();
                // next
                guild.upsertCommand("next", "Next page")
                        .queue();
                // stop
                guild.upsertCommand("stop", "Stop the audio")
                        .queue();
                WriteLogs.writeLog("Befehl echo wurde hinzugefügt.");
            }
        });
    }

    // Methode zum Hinzufügen von Ereignissen zur Warteschlange
    public void reactionAddQueue(GenericMessageReactionEvent ereignis) {
        ReactionAddeventQueue.add(ereignis);
    }

    // Methode zum Hinzufügen von Ereignissen zur Warteschlange
    public void reactionRemoveQueue(GenericMessageReactionEvent ereignis) {
        ReactionRemoveQueue.add(ereignis);
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        // Fügen Sie das Event zur Warteschlange hinzu
        buttonClickQueue.add(event);
        // Verarbeiten Sie das Event
        ProcessButtonClickEvent();
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String commandName = event.getName();
        if (commandName.equals("play")) {
            event.deferReply().setEphemeral(true).queue(interactionHook -> {
                String link = event.getOption("link").getAsString();
                String voiceChannelId = null;
                Member member;
                AudioChannel channel;

                GuildVoiceState voiceState = event.getMember().getVoiceState();
                if (voiceState != null) {
                    channel = voiceState.getChannel();
                    if (channel != null) {
                        voiceChannelId = channel.getId();
                        WriteLogs.writeLog("Voice ChannelID: " + voiceChannelId);
                        member = event.getMember();

                        try {
                            String outputPath = Main.outputPath;
                            AudioDownload audioDownload = new AudioDownload();
                            audioDownload.downloadAudio(link, outputPath, voiceChannelId,
                                    channel.getGuild().getAudioManager());
                            WriteLogs.writeLog("Audio wird abgespielt: " + link);

                            interactionHook.editOriginal("Audio wird abgespielt: " + link)
                                    .setActionRow(
                                            Button.primary("volume_up", "Lauter"),
                                            Button.primary("volume_down", "Leiser"),
                                            Button.primary("next", "Nächster Track"),
                                            Button.danger("stop", "Stop"))
                                    .queue();
                        } catch (Exception e) {
                            WriteLogs.writeLog("Fehler beim Laden der Audiodatei: " + e.getMessage());
                            interactionHook.editOriginal("Fehler beim Laden der Audiodatei: " + e.getMessage()).queue();
                        }
                    } else {
                        WriteLogs.writeLog("Voice Channel ist null");
                    }
                } else {
                    WriteLogs.writeLog("VoiceState ist null");
                }
            });
        } else if (commandName.equals("next")) {
            event.deferReply().queue();
            try {
                GuildVoiceState voiceState = event.getMember().getVoiceState();
                if (voiceState != null) {
                    AudioChannel channel = voiceState.getChannel();
                    if (channel != null) {
                        AudioPlayer player = new AudioPlayer(channel.getGuild().getAudioManager());
                        player.skipTrack();
                    }
                }
            } catch (Exception e) {
                WriteLogs.writeLog("Fehler beim Überspringen des Tracks: " + e.getMessage());
            }
        } else if (commandName.equals("stop")) {
            event.deferReply().queue();
            try {
                GuildVoiceState voiceState = event.getMember().getVoiceState();
                if (voiceState != null) {
                    AudioChannel channel = voiceState.getChannel();
                    if (channel != null) {
                        channel.getGuild().getAudioManager().closeAudioConnection();
                    }
                }
            } catch (Exception e) {
                WriteLogs.writeLog("Fehler beim Stoppen der Wiedergabe: " + e.getMessage());
            }
        }
    }

    // Methode zum Verarbeiten von Button-Klick-Events
    public void ProcessButtonClickEvent() {
        // Entfernen Sie das nächste Event aus der Warteschlange
        ButtonInteractionEvent event = buttonClickQueue.remove();
        // Überprüfen Sie, ob ein Event vorhanden ist
        if (event != null) {
            // Get the ID of the clicked button
            String buttonId = event.getComponentId();

            // check if button Regeln Akzeptieren is klicked
            if (buttonId.equals("RegelnAkzeptieren")) {
                WriteLogs.permissions("Regeln wurden akzeptiert von: " + event.getUser().getName()
                        + " auf: " + event.getGuild().getName());
                try {
                    Member member = event.getMember();
                    // Die Rolle "Regeln akzeptiert" wird gesucht
                    List<Role> roles = event.getGuild().getRolesByName("Regeln akzeptiert", true);

                    // Es wird überprüft, ob das Mitglied und die Rolle existieren
                    if (member != null) {
                        // Die Rolle wird dem Mitglied gegeben
                        event.getGuild().addRoleToMember(member, roles.get(0)).queue();
                    } else {
                        // Ein Log-Eintrag wird erstellt, dass das Mitglied oder die Rolle nicht
                        // gefunden wurden
                        WriteLogs.permissions("Mitglied oder Rolle konnte nicht gefunden werden");
                    }

                } catch (Exception e) {
                    // Ein Log-Eintrag wird erstellt, dass ein Fehler beim Entfernen der Rolle
                    // aufgetreten ist
                    WriteLogs.permissions("Fehler beim entfernen der Rolle: " + e.getMessage());
                }
                event.deferEdit().queue();

            } else if (buttonId.equals("RegelnAblehnen")) {
                // Ein Log-Eintrag wird erstellt, dass die Regeln nicht akzeptiert wurden
                WriteLogs.permissions("Regeln wurden nicht akzeptiert von: " + event.getUser().getName()
                        + " auf: " + event.getGuild().getName());

                try {
                    // Das Mitglied, das die Reaktion entfernt hat, wird ermittelt
                    Member member = event.getMember();
                    // Die Rolle "Regeln akzeptiert" wird gesucht
                    List<Role> roles = event.getGuild().getRolesByName("Regeln akzeptiert", true);
                    // Es wird überprüft, ob das Mitglied und die Rolle existieren
                    if (member != null && roles != null && !roles.isEmpty()) {
                        // Die Rolle wird dem Mitglied entzogen
                        event.getGuild().removeRoleFromMember(member, roles.get(0)).queue();
                    } else {
                        // Ein Log-Eintrag wird erstellt, dass das Mitglied oder die Rolle nicht
                        // gefunden wurden
                        WriteLogs.permissions("Mitglied oder Rolle konnte nicht gefunden werden");
                    }
                } catch (Exception e) {
                    // Ein Log-Eintrag wird erstellt, dass ein Fehler beim Entfernen der Rolle
                    // aufgetreten ist
                    WriteLogs.permissions("Fehler beim entfernen der Rolle: " + e.getMessage());
                }
                event.deferEdit().queue();

            } else if (buttonId.equals("volume_up")) {
                try {
                    event.deferEdit().queue();
                    AudioPlayer.player.setVolume(AudioPlayer.player.getVolume() + 10);
                } catch (Exception e) {
                    WriteLogs.writeLog("Fehler beim Erhöhen der Lautstärke: " + e.getMessage());
                }

            } else if (buttonId.equals("volume_down")) {
                try {
                    event.deferEdit().queue();
                    AudioPlayer.player.setVolume(AudioPlayer.player.getVolume() - 10);
                } catch (Exception e) {
                    WriteLogs.writeLog("Fehler beim Verringern der Lautstärke: " + e.getMessage());
                }
            } else if (buttonId.equals("next")) {
                try {
                    event.deferEdit().queue();
                    GuildVoiceState voiceState = event.getMember().getVoiceState();
                    if (voiceState != null) {
                        AudioChannel channel = voiceState.getChannel();
                        if (channel != null) {
                            AudioPlayer player = new AudioPlayer(channel.getGuild().getAudioManager());
                            player.skipTrack();
                        }
                    }
                } catch (Exception e) {
                    WriteLogs.writeLog("Fehler beim Überspringen des Tracks: " + e.getMessage());
                }
            } else if (buttonId.equals("stop")) {
                try {
                    event.deferEdit().queue();
                    GuildVoiceState voiceState = event.getMember().getVoiceState();
                    if (voiceState != null) {
                        AudioChannel channel = voiceState.getChannel();
                        if (channel != null) {
                            channel.getGuild().getAudioManager().closeAudioConnection();
                        }
                    }
                } catch (Exception e) {
                    WriteLogs.writeLog("Fehler beim Stoppen der Wiedergabe: " + e.getMessage());
                }
            }
        }
    }
}
