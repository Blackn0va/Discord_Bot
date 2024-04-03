package com.blackn0va.discord_bot;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GiveRole extends ListenerAdapter {
    // Erstellen Sie eine Warteschlange für Ereignisse, bei denen eine Reaktion
    // hinzugefügt wird
    Queue<GenericMessageReactionEvent> AddeventQueue = new LinkedList<>();
    // Erstellen Sie eine Warteschlange für Ereignisse, bei denen eine Reaktion
    // entfernt wird
    Queue<GenericMessageReactionEvent> RemoveeventQueue = new LinkedList<>();

    // Diese Methode wird aufgerufen, wenn der Bot bereit ist
    @Override
    public void onReady(ReadyEvent ereignis) {

        // Ein Log-Eintrag wird erstellt, dass der Bot jetzt online ist
        WriteLogs.writeLog("Der Bot ist jetzt online!\n");
        // Eine Nachricht wird auf der Konsole ausgegeben, dass der Bot jetzt online ist
        System.out.println("Der Bot ist jetzt online!\n");

        // Für jede Gilde, in der der Bot Mitglied ist
        for (Guild guild : ereignis.getJDA().getGuilds()) {
            // Die Mitglieder der Gilde werden geladen
            guild.loadMembers().onSuccess(members -> {
                // Für jedes Mitglied der Gilde
                for (Member member : members) {
                    // Der effektive Name des Mitglieds wird auf der Konsole ausgegeben
                    System.out.println(member.getEffectiveName());
                    // Ein Log-Eintrag wird erstellt mit dem effektiven Namen des Mitglieds
                    WriteLogs.writeLog(member.getEffectiveName());
                }
            });
        }
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent ereignis) {
        // Fügen Sie das Ereignis zur Warteschlange hinzu
        reactionAddQueue(ereignis);
        // Verarbeiten Sie das Ereignis
        addEvent();
    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent ereignis) {
        // Fügen Sie das Ereignis zur Warteschlange hinzu
        reactionRemoveQueue(ereignis);
        // Verarbeiten Sie das Ereignis
        removeEvent();
    }

    // Methode zum Hinzufügen von Ereignissen zur Warteschlange
    public void reactionAddQueue(GenericMessageReactionEvent ereignis) {
        AddeventQueue.add(ereignis);
    }

    // Methode zum Hinzufügen von Ereignissen zur Warteschlange
    public void reactionRemoveQueue(GenericMessageReactionEvent ereignis) {
        RemoveeventQueue.add(ereignis);
    }

    // Diese Methode verarbeitet Ereignisse aus der Warteschlange
    public void addEvent() {
        // Zuerst wird überprüft, ob die Warteschlange nicht leer ist
        if (!AddeventQueue.isEmpty()) {
            // Das Ereignis wird aus der Warteschlange entfernt
            GenericMessageReactionEvent ereignis = AddeventQueue.remove();

            // Der Benutzer, der die Reaktion ausgelöst hat, wird ermittelt
            User user = ereignis.getUser();
            if (user != null) {
                // Es wird überprüft, ob der Benutzer kein Bot ist
                if (!user.isBot()) {
                    // Es wird überprüft, ob der Kanalname "regeln" enthält
                    if (ereignis.getChannel().getName().contains("regeln")) {
                        // Es wird überprüft, ob das Emoji der Reaktion "✅" ist
                        if (ereignis.getReaction().getEmoji().getName().equals("✅")) {
                            // Ein Log-Eintrag wird erstellt, dass die Regeln akzeptiert wurden
                            WriteLogs.permissions(
                                    "Regeln wurden akzeptiert von: " + ereignis.getUser().getName() + " auf: "
                                            + ereignis.getGuild().getName());
                            // Eine Nachricht wird auf der Konsole ausgegeben, dass die Regeln akzeptiert
                            // wurden
                            System.out
                                    .println("Regeln wurden akzeptiert von: " + ereignis.getUser().getName() + " auf: "
                                            + ereignis.getGuild().getName());
                            try {
                                // Das Mitglied, das die Reaktion ausgelöst hat, wird ermittelt
                                Member member = ereignis.getMember();
                                // Die Rolle "Regeln akzeptiert" wird gesucht
                                List<Role> roles = ereignis.getGuild().getRolesByName("Regeln akzeptiert", true);
                                // Es wird überprüft, ob das Mitglied und die Rolle existieren
                                if (member != null && roles != null && !roles.isEmpty()) {
                                    // Die Rolle wird dem Mitglied zugewiesen
                                    ereignis.getGuild().addRoleToMember(member, roles.get(0)).queue();
                                } else {
                                    // Ein Log-Eintrag wird erstellt, dass das Mitglied oder die Rolle nicht
                                    // gefunden wurden
                                    WriteLogs.permissions("Mitglied oder Rolle konnte nicht gefunden werden");
                                    // Eine Nachricht wird auf der Konsole ausgegeben, dass das Mitglied oder die
                                    // Rolle nicht gefunden wurden
                                    System.out.println("Mitglied oder Rolle konnte nicht gefunden werden");
                                }
                            } catch (Exception e) {
                                // Ein Log-Eintrag wird erstellt, dass ein Fehler beim Zuweisen der Rolle
                                // aufgetreten ist
                                WriteLogs.permissions("Fehler beim zuweisen der Rolle: " + e.getMessage());
                                // Eine Nachricht wird auf der Konsole ausgegeben, dass ein Fehler beim Zuweisen
                                // der Rolle aufgetreten ist
                                System.out.println("Fehler beim zuweisen der Rolle: " + e.getMessage());
                            }
                        }

                    }
                }
            }
        }
    }

    // Diese Methode verarbeitet Ereignisse aus der Warteschlange
    public void removeEvent() {
        // Zuerst wird überprüft, ob die Warteschlange nicht leer ist
        if (!RemoveeventQueue.isEmpty()) {
            // Das Ereignis wird aus der Warteschlange entfernt
            GenericMessageReactionEvent ereignis = RemoveeventQueue.remove();

            // Der Benutzer, der die Reaktion entfernt hat, wird ermittelt
            User user = ereignis.getUser();
            if (user != null) {
                // Es wird überprüft, ob der Benutzer kein Bot ist
                if (!user.isBot()) {
                    // Es wird überprüft, ob der Kanalname "regeln" enthält
                    if (ereignis.getChannel().getName().contains("regeln")) {
                        // Es wird überprüft, ob das Emoji der Reaktion "✅" ist
                        if (ereignis.getReaction().getEmoji().getName().equals("✅")) {
                            // Ein Log-Eintrag wird erstellt, dass die Regeln nicht akzeptiert wurden
                            WriteLogs.permissions("Regeln wurden nicht akzeptiert von: " + ereignis.getUser().getName()
                                    + " auf: " + ereignis.getGuild().getName());
                            // Eine Nachricht wird auf der Konsole ausgegeben, dass die Regeln nicht
                            // akzeptiert wurden
                            System.out.println("Regeln wurden nicht akzeptiert von: " + ereignis.getUser().getName()
                                    + " auf: " + ereignis.getGuild().getName());
                            try {
                                // Das Mitglied, das die Reaktion entfernt hat, wird ermittelt
                                Member member = ereignis.getMember();
                                // Die Rolle "Regeln akzeptiert" wird gesucht
                                List<Role> roles = ereignis.getGuild().getRolesByName("Regeln akzeptiert", true);
                                // Es wird überprüft, ob das Mitglied und die Rolle existieren
                                if (member != null && roles != null && !roles.isEmpty()) {
                                    // Die Rolle wird dem Mitglied entzogen
                                    ereignis.getGuild().removeRoleFromMember(member, roles.get(0)).queue();
                                } else {
                                    // Ein Log-Eintrag wird erstellt, dass das Mitglied oder die Rolle nicht
                                    // gefunden wurden
                                    WriteLogs.permissions("Mitglied oder Rolle konnte nicht gefunden werden");
                                    // Eine Nachricht wird auf der Konsole ausgegeben, dass das Mitglied oder die
                                    // Rolle nicht gefunden wurden
                                    System.out.println("Mitglied oder Rolle konnte nicht gefunden werden");
                                }
                            } catch (Exception e) {
                                // Ein Log-Eintrag wird erstellt, dass ein Fehler beim Entfernen der Rolle
                                // aufgetreten ist
                                WriteLogs.permissions("Fehler beim entfernen der Rolle: " + e.getMessage());
                                // Eine Nachricht wird auf der Konsole ausgegeben, dass ein Fehler beim
                                // Entfernen der Rolle aufgetreten ist
                                System.out.println("Fehler beim entfernen der Rolle: " + e.getMessage());
                            }

                        }

                    }
                }
            }

        }

    }

}
