package com.blackn0va.discord_bot;

import java.io.IOException;
import java.util.List;

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
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent ereignis) {

        // if the reaction is from a bot, then the reaction is not further processed
        User user = ereignis.getUser();
        if (user != null && user.isBot()) {
            return;
        } else {
            if (ereignis.getChannel().getName().contains("regeln")) {
                WriteLogs.writeLog("Regeln wurden akzeptiert von: " + ereignis.getUser().getName());
                try {
                    Member member = ereignis.getMember();
                    List<Role> roles = ereignis.getGuild().getRolesByName("Regeln akzeptiert", true);
                    if (member != null && roles != null && !roles.isEmpty()) {
                        ereignis.getGuild().addRoleToMember(member, roles.get(0)).queue();
                    } else {
                        WriteLogs.writeLog("Mitglied oder Rolle konnte nicht gefunden werden");
                    }
                } catch (Exception e) {
                    WriteLogs.writeLog("Fehler beim zuweisen der Rolle: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent ereignis) {

        User user = ereignis.getUser();
        if (user != null && user.isBot()) {
            return;
        } else {
            if (ereignis.getChannel().getName().contains("regeln")) {
                WriteLogs.writeLog("Regeln wurden nicht akzeptiert von: " + ereignis.getUser().getName());
                try {
                    Member member = ereignis.getMember();
                    List<Role> roles = ereignis.getGuild().getRolesByName("Regeln akzeptiert", true);
                    if (member != null && roles != null && !roles.isEmpty()) {
                        ereignis.getGuild().removeRoleFromMember(member, roles.get(0)).queue();
                    } else {
                        WriteLogs.writeLog("Mitglied oder Rolle konnte nicht gefunden werden");
                    }
                } catch (Exception e) {
                    WriteLogs.writeLog("Fehler beim entfernen der Rolle: " + e.getMessage());
                }
            }
        }

    }

}
