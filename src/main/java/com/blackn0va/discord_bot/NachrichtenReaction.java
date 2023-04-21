package com.blackn0va.discord_bot;

import net.dv8tion.jda.api.entities.Activity;
/**
 *
 * @author Black
 */
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class NachrichtenReaction extends ListenerAdapter {
    public static String answer = "";
    public static String fehler = "";
    public static String RSSNews = "";
    public static String regeln = "\u00A7 1. Sei respektvoll gegen\u00FCber anderen Nutzern hier im Discord - eine h\u00F6fliche und angenehme Atmosph\u00E4re muss gewahrt werden.\n\n\u00A7 2. Alle Formen der Beleidigungen, Diskriminierung, Rassismus oder Antisemitismus und jegliche andere Formen des deutschen Rechts, die diesem widersprechen, sind hier in Text- oder Sprachkan\u00E4len strengstens untersagt.\n\n\u00A7 3. Werbung f\u00FCr eigene oder fremde Inhalte ist hier untersagt. Damit ist auch gemeint, den eigenen oder einen fremden Discord oder \u00C4hnliches zu bewerben. Auch in Privatnachrichten ist dies untersagt. \n\n\u00A7 4. In Chats d\u00FCrfen nur Inhalte diskutiert werden, die Themengerecht sind in den jeweiligen Kan\u00E4len. Als Community - Sprache gilt ausschlie\u00DFlich deutsch. Fremde Sprachen in Wort und Schrift werden hier nicht geduldet und untersagt. Du musst Dich so gut es geht bem\u00FChen, einigerma\u00DFen die geltende deutsche Grammatik einzuhalten.\n \n\u00A7 5. Trolling und Spamming sind komplett zu unterlassen, dazu unterl\u00E4sst Du es auch unn\u00F6tige Diskussionen zu starten, die nichts mit irgendeinem Thema hier im Discord zu tun haben.\n\n\u00A7 6. Soundboards oder Stimmverzerrer oder die Aufnahme von Gespr\u00E4chen sind untersagt, letzteres ist auch gesetzlich im \u00DCbrigen verboten.\n\n\u00A7 7. Das dauerhafte unn\u00F6tige Verlassen und Betreten eines Sprachkanals ist zu unterlassen. \n\n\u00A7 8. St\u00F6rger\u00E4usche wie Handy, Fernseher und andere Dinge, die im Hintergrund zu h\u00F6ren sind, sind zu unterlassen.\n\u00A7 9. In den Sprach Channel zu kommen und dauerhaft nichts zu sagen oder sich dann stumm zu schalten ist sehr unh\u00F6flich und sollte unterlassen werden. Ansonsten gehe daf\u00FCr in den AFK/Pause Channel, dort ist es erlaubt. \n Wenn du die Regeln gelesen hast, dann klicke auf das ✅\n\n Wenn Du mit ChatGPT schreiben möchtest, klicke auf die 🤪 ";

@Override
public void onReady(ReadyEvent event) {
    System.out.println("Bot ist bereit");
 

}

    @Override
    public void onMessageReceived(MessageReceivedEvent ereignis) {

        if (ereignis.isFromGuild()) {
            // Wenn es ein Bot ist, dann wird die Nachricht nicht weiter verarbeitet
            if (ereignis.getAuthor().isBot()) {
                return;
            } else { // wenn der Chat ein Admin ist
                if (ereignis.getChannel().getId().equals(Main.GPTChannelID)) {

                    // if content do not start with ! then send message ignore Case sensitive
                    if (!ereignis.getMessage().getContentStripped().startsWith("!")) {
                        openai.getAnswer(ereignis.getMessage().toString());

                        // openai.getAnswer(ereignis.getMessage().toString());
                        if (answer.contains("You exceeded your current quota")) {
                            // answer = "Ich habe keine Antwort gefunden";
                        } else {
                            ereignis.getChannel().sendTyping().queue();
                            ereignis.getChannel().sendMessage(answer + ereignis.getAuthor().getAsMention()).queue();
                        }
                    }

                    // rssNews.getNews();
                    // ereignis.getChannel().sendMessage(RSSNews +
                    // ereignis.getAuthor().getAsMention()).queue();
                }
                if (ereignis.getMember().getRoles()
                        .contains(ereignis.getGuild().getRolesByName("Admin", true).get(0))) {
                    if (ereignis.getMessage().getContentStripped().startsWith("!say")) {
                        ereignis.getMessage().delete().queue();
                        // Nachricht senden
                        ereignis.getChannel().sendMessage(ereignis.getMessage().getContentStripped().substring(5))
                                .queue();
                    } else if (ereignis.getMessage().getContentStripped().startsWith("!stream")) {
                        ereignis.getMessage().delete().queue();
                        // Nachricht senden
                        ereignis.getChannel().sendMessage(ereignis.getMessage().getContentStripped().substring(8))
                                .queue();
                        // change status from bauplan and stream twitch
                        ereignis.getJDA().getPresence().setActivity(
                                Activity.streaming("stream", ereignis.getMessage().getContentStripped().substring(8)));

                    } else if (ereignis.getMessage().getContentStripped().startsWith("!play")) {
                        // Reaktion hinzufügen
                        ereignis.getMessage().addReaction(Emoji.fromUnicode("\uD83E\uDD73")).queue();

                        
                         //STream Audio with Lavaplayer and JDA
                         


                        
                        
                         } else if (ereignis.getMessage().getContentStripped().startsWith("!regeln")) {
                        ereignis.getMessage().delete().queue();
                        // Nachricht senden
                        ereignis.getChannel().sendTyping().queue();
                        ereignis.getChannel().sendMessage("@everyone " + regeln).queue();

                    } else if (ereignis.getMessage().getContentStripped().startsWith("!news")) {
                        // Nachricht senden
                        rssNews.getPatchNotes();
                        if (RSSNews == null) {
                            ereignis.getChannel().sendTyping().queue();
                            ereignis.getChannel().sendMessage("Keine News").queue();
                        } else
                            ereignis.getChannel().sendTyping().queue();

                        ereignis.getChannel().sendMessage("@everyone " + RSSNews).queue();

                    }  else if (ereignis.getMessage().getContentStripped().startsWith("!commands")) {
                        // Nachricht löschen
                        ereignis.getMessage().delete().queue();
                        ereignis.getChannel().sendTyping().queue();

                        // Nachricht senden
                        ereignis.getChannel()
                                .sendMessage("!say Hallo \n" + "!stream Twitch Link \n"
                                        + "!play Link Spotify \n"
                                        + "!regeln \n" + "!news \n" + "!commands \n"
                                        + ereignis.getAuthor().getAsMention())
                                .queue();

                    } else if (ereignis.getMessage().getContentStripped().equals("hallo")) {
                        try {
                            // Reaktion hinzufügen
                            ereignis.getMessage()
                                    .addReaction(Emoji
                                            .fromUnicode("\uD83D\uDE00"))
                                    .queue();

                            ereignis.getChannel().sendTyping().queue();
                            ereignis.getChannel().sendMessage("Hallo " + ereignis.getAuthor().getAsMention()).queue();

                            return;
                        } catch (Exception e) {
                            System.out.println("Fehler beim Reagieren auf die Nachricht");
                        }

                    }
                }
            }
        }

    }
 
 

    

}
