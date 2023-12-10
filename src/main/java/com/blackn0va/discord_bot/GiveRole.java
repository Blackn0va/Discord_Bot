package com.blackn0va.discord_bot;

import java.util.List;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;

import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GiveRole extends ListenerAdapter {

    @Override
    public void onReady(ReadyEvent ereignis) {

        WriteLogs.writeLog("Der Bot ist jetzt online!\n");
        System.out.println("Der Bot ist jetzt online!\n");

        for (Guild guild : ereignis.getJDA().getGuilds()) {
            guild.loadMembers().onSuccess(members -> {
                for (Member member : members) {
                    System.out.println(member.getEffectiveName());
                }
            });
        }
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent ereignis) {
        User user = ereignis.getUser();
        if (user != null) {
            if (!user.isBot()) {
                if (ereignis.getChannel().getName().contains("regeln")) {
                    WriteLogs.writeLog("Regeln wurden akzeptiert von: " + ereignis.getUser().getName() + " auf: "
                            + ereignis.getGuild().getName());
                    System.out.println("Regeln wurden akzeptiert von: " + ereignis.getUser().getName() + " auf: "
                            + ereignis.getGuild().getName());
                    try {
                        Member member = ereignis.getMember();
                        List<Role> roles = ereignis.getGuild().getRolesByName("Regeln akzeptiert", true);
                        if (member != null && roles != null && !roles.isEmpty()) {
                            ereignis.getGuild().addRoleToMember(member, roles.get(0)).queue();
                        } else {
                            WriteLogs.writeLog("Mitglied oder Rolle konnte nicht gefunden werden");
                            System.out.println("Mitglied oder Rolle konnte nicht gefunden werden");
                        }
                    } catch (Exception e) {
                        WriteLogs.writeLog("Fehler beim zuweisen der Rolle: " + e.getMessage());
                        System.out.println("Fehler beim zuweisen der Rolle: " + e.getMessage());
                    }
                }
            }
        }

    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent ereignis) {
        User user = ereignis.getUser();
        if (user != null) {
            if (!user.isBot()) {
                if (ereignis.getChannel().getName().contains("regeln")) {
                    WriteLogs.writeLog("Regeln wurden nicht akzeptiert von: " + ereignis.getUser().getName()
                            + " auf: " + ereignis.getGuild().getName());
                    System.out.println("Regeln wurden nicht akzeptiert von: " + ereignis.getUser().getName()
                            + " auf: " + ereignis.getGuild().getName());
                    try {
                        Member member = ereignis.getMember();
                        List<Role> roles = ereignis.getGuild().getRolesByName("Regeln akzeptiert", true);
                        if (member != null && roles != null && !roles.isEmpty()) {
                            ereignis.getGuild().removeRoleFromMember(member, roles.get(0)).queue();
                        } else {
                            WriteLogs.writeLog("Mitglied oder Rolle konnte nicht gefunden werden");
                            System.out.println("Mitglied oder Rolle konnte nicht gefunden werden");
                        }
                    } catch (Exception e) {
                        WriteLogs.writeLog("Fehler beim entfernen der Rolle: " + e.getMessage());
                        System.out.println("Fehler beim entfernen der Rolle: " + e.getMessage());
                    }
                }
            }
        }
    }

}
