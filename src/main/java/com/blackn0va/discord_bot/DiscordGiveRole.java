package com.blackn0va.discord_bot;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

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

    // Diese Methode wird aufgerufen, wenn der Bot bereit ist
    @Override
    public void onReady(ReadyEvent ereignis) {

        // Ein Log-Eintrag wird erstellt, dass der Bot jetzt online ist
        WriteLogs.writeLog("Der Bot ist jetzt online!\n");

        // Für jede Gilde, in der der Bot Mitglied ist
        for (Guild guild : ereignis.getJDA().getGuilds()) {
            // Die Mitglieder der Gilde werden geladen
            guild.loadMembers().onSuccess(members -> {
                // zähle alle member und gebe sie aus
                WriteLogs.writeLog("Members in " + guild.getName() + ": " + members.size());

                guild.updateCommands().addCommands(
                        Commands.slash("echoo", "Antwortet mit der Nachricht")
                                .addOption(OptionType.STRING, "text", "Die Nachricht, die der Bot senden soll", true))
                        .queue();
            });
        }
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
        // erhalte den Namen des Befehls
        String commandName = event.getName();
        // Überprüfe, ob der Befehl "echoo" ist
        if (commandName.equals("echoo")) {
            event.deferReply().setEphemeral(true).queue(interactionHook -> {
                // Hole den Text aus den Optionen
                String text = event.getOption("text").getAsString();
                // Antwort mit dem Text
                interactionHook.editOriginal("```" + text + "```").queue();
            });
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

            } else if (buttonId.equals("back") || buttonId.equals("next")) {
                // Hole die aktuelle Seitennummer aus der Map
                Long messageId = event.getInteraction().getMessageIdLong();
                Main.StarCitizencurrentPageNum = currentPageNums.getOrDefault(messageId, 1);

                // Berechne die neue Seitennummer
                int newPageNum = buttonId.equals("back") ? Main.StarCitizencurrentPageNum - 1
                        : Main.StarCitizencurrentPageNum + 1;

                // Prüfe, ob die neue Seitennummer gültig ist
                if (newPageNum >= 1 && newPageNum <= Main.StarCitizenPatchPages.size()) {
                    // Aktualisiere die aktuelle Seitennummer in der Map
                    currentPageNums.put(messageId, newPageNum);

                    // Erstelle einen neuen Embed mit der neuen Seite
                    EmbedBuilder embed = new EmbedBuilder()
                            .setTitle("Star Citizen Patch") // Setze hier den Titel
                            .setDescription("```prolog\n"
                                    + Main.StarCitizenPatchPages.get(newPageNum - 1).replace("[", "").replace("]", "")
                                    + "\n```")
                            .setColor(Color.GREEN) // Setze hier die Farbe
                            .setTimestamp(java.time.Instant.now())
                            .setFooter("Seite " + newPageNum + " von " + Main.StarCitizenPatchPages.size(),
                                    Main.IconURL);

                    // Aktualisiere die Nachricht mit dem neuen Embed
                    event.getInteraction().editMessageEmbeds(embed.build()).queue();
                } else if (newPageNum < 1) {
                    // Wenn die neue Seitennummer kleiner als 1 ist, mache nichts
                    event.deferEdit().queue();
                } else {
                    // Wenn die neue Seitennummer nicht gültig ist, sende eine Fehlermeldung
                    event.deferEdit().queue();
                }
            }
        }
    }
}
