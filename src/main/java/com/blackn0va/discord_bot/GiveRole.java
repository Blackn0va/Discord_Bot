/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.blackn0va.discord_bot;

import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;

import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GiveRole extends ListenerAdapter {

    @Override
    public void onReady(ReadyEvent ereignis) {

        var prefix = "!";
        System.out.print("Der Bot ist jetzt online!\n");
        System.out.print("Der Prefix des Bots lautet: " + prefix + "\n");

    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent ereignis) {

        // if the reaction is from a bot, then the reaction is not further processed
        if (ereignis.getUser().isBot()) {
            return;
        } else {
            // Wenn eine Reaktion aus dem Channel "Regeln" gibt
            if (ereignis.getChannel().getId().equals(Main.ChannelID)) {
                // if the reaction is on a Message with id "1090705151662247936"
                if (ereignis.getMessageId().equals(Main.PostID)) {
                    // Wenn die Reaktion "✅" ist
                    if (ereignis.getReaction().getEmoji().getName().equals("✅")) {
                        if (!ereignis.getMember().getRoles()
                                .contains(ereignis.getGuild().getRolesByName("Member", true).get(0))) {
                            try {
                                ereignis.getGuild().addRoleToMember(ereignis.getMember(),
                                        ereignis.getGuild().getRolesByName("Member", true).get(0)).queue();
                                System.out.println(ereignis.getUserId() + " wurde die Rolle Member erteilt!");
                            } catch (Exception e) {
                                System.out.println("Die Berechtigung konnte nicht erteilt werden! \n" + e.toString());
                            }

                        }
                    } else if (ereignis.getReaction().getEmoji().getName().equals("🤪")) {
                        if (!ereignis.getMember().getRoles()
                                .contains(ereignis.getGuild().getRolesByName("openai", true).get(0))) {
                            try {
                                ereignis.getGuild().addRoleToMember(ereignis.getMember(),
                                        ereignis.getGuild().getRolesByName("openai", true).get(0)).queue();
                                System.out.println(ereignis.getUserId() + " wurde die Rolle openai erteilt!");
                            } catch (Exception e) {
                                System.out.println("Die Berechtigung konnte nicht erteilt werden! \n" + e.toString());
                            }

                        }
                    }

                }

            }
        }

    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent ereignis) {
            // Wenn eine Reaktion aus dem Channel "Regeln" gibt
            if (ereignis.getChannel().getId().equals(Main.ChannelID)) {
                // if the reaction is on a Message with id "1090705151662247936"
                if (ereignis.getMessageId().equals(Main.PostID)) {
                    // Wenn die Reaktion "✅" ist
                    if (ereignis.getReaction().getEmoji().getName().equals("✅")) {
                        try {
                            // retrieve
                            ereignis.retrieveMember().queue(member -> {
                                // remove role
                                member.getGuild().removeRoleFromMember(member,
                                        member.getGuild().getRolesByName("Member", true).get(0)).queue();
                            });
                            System.out.println(ereignis.getUserId() + " wurde die Rolle Member entfernt!");
                        } catch (Exception e) {
                            System.out.println("Fehler beim entfernen der Rolle! \n" + e.toString());
                        }

                    } else if (ereignis.getReaction().getEmoji().getName().equals("🤪")) {
                        try {
                            ereignis.retrieveMember().queue(member -> {
                                // remove role
                                member.getGuild().removeRoleFromMember(member,
                                        member.getGuild().getRolesByName("openai", true).get(0)).queue();
                            });
                            System.out.println(ereignis.getUserId() + " wurde die Rolle openai entfernt!");
                        } catch (Exception e) {
                            System.out.println("Fehler beim entfernen der Rolle! \n" + e.toString());
                        }

                    }

                }

            }
        

    }

}
