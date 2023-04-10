package com.blackn0va.discord_bot;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.apptasticsoftware.rssreader.Item;
import com.apptasticsoftware.rssreader.RssReader;

public class rssNews {

    public static void getNews() {

        try {
            // parse rss feed to string
            String rss = "";

            RssReader reader = new RssReader();
            Stream<Item> items = reader.read("https://developertracker.com/star-citizen/rss");
            // get only the last 2 feeds
            items = items.limit(1);

            List<Item> list = items.collect(Collectors.toList());
            for (Item item : list) {
                rss += item.getTitle() + "\n" + item.getDescription();

                // Remove html tags
                rss = rss.replaceAll("<[^>]*>", "");
                rss = rss.replaceAll("&#39;", "'");
                rss = rss.replaceAll("&quot;", "\"");
                rss = rss.replaceAll("&amp;", "&");
                rss = rss.replaceAll("&lt;", "<");
                rss = rss.replaceAll("&gt;", ">");
                rss = rss.replaceAll("&nbsp;", " ");
                rss = rss.replaceAll("&rsquo;", "'");
                rss = rss.replaceAll("&ldquo;", "\"");
                rss = rss.replaceAll("&rdquo;", "\"");
                rss = rss.replaceAll("&hellip;", "...");
                rss = rss.replaceAll("&mdash;", "-");
                rss = rss.replaceAll("&ndash;", "-");
                rss = rss.replaceAll("&eacute;", "é");
                rss = rss.replaceAll("&oacute;", "ó");
                rss = rss.replaceAll("&iacute;", "í");
                rss = rss.replaceAll("&aacute;", "á");
                rss = rss.replaceAll("&uacute;", "ú");
                rss = rss.replaceAll("&ntilde;", "ñ");
                rss = rss.replaceAll("&uuml;", "ü");
                rss = rss.replaceAll("&ouml;", "ö");
                rss = rss.replaceAll("&szlig;", "ß");
                rss = rss.replaceAll("&Auml;", "Ä");
                rss = rss.replaceAll("&Ouml;", "Ö");
                rss = rss.replaceAll("&Uuml;", "Ü");
                rss = rss.replaceAll("&euro;", "€");
                rss = rss.replaceAll("&copy;", "©");
                rss = rss.replaceAll("&reg;", "®");
                rss = rss.replaceAll("&trade;", "™");
                rss = rss.replaceAll("&cent;", "¢");
                rss = rss.replaceAll("&pound;", "£");
                rss = rss.replaceAll("&yen;", "¥");
                rss = rss.replaceAll("&sect;", "§");
                rss = rss.replaceAll("&deg;", "°");
                rss = rss.replaceAll("&plusmn;", "±");
                rss = rss.replaceAll("&sup2;", "²");
                rss = rss.replaceAll("&sup3;", "³");
                rss = rss.replaceAll("&frac14;", "¼");
                rss = rss.replaceAll("&frac12;", "½");
                rss = rss.replaceAll("&frac34;", "¾");
                rss = rss.replaceAll("&times;", "×");
                rss = rss.replaceAll("&divide;", "÷");
                rss = rss.replaceAll("&micro;", "µ");
                rss = rss.replaceAll("&middot;", "·");
                rss = rss.replaceAll("&bull;", "•");
                rss = rss.replaceAll("&hellip;", "…");
                rss = rss.replaceAll("&prime;", "′");
                rss = rss.replaceAll("&Prime;", "″");
                rss = rss.replaceAll("&oline;", "‾");
                rss = rss.replaceAll("&frasl;", "⁄");
                rss = rss.replaceAll("&weierp;", "℘");
                rss = rss.replaceAll("&image;", "ℑ");
                rss = rss.replaceAll("&real;", "ℜ");
                rss = rss.replaceAll("&trade;", "™");
                rss = rss.replaceAll("&alefsym;", "ℵ");
                rss = rss.replaceAll("&larr;", "←");
                rss = rss.replaceAll("&uarr;", "↑");
                rss = rss.replaceAll("&rarr;", "→");
                rss = rss.replaceAll("&darr;", "↓");
                rss = rss.replaceAll("&harr;", "↔");
                rss = rss.replaceAll("&crarr;", "↵");
                rss = rss.replaceAll("&lArr;", "⇐");
                rss = rss.replaceAll("&uArr;", "⇑");
                rss = rss.replaceAll("&rArr;", "⇒");
                rss = rss.replaceAll("&dArr;", "⇓");
                rss = rss.replaceAll("&hArr;", "⇔");
                rss = rss.replaceAll("&forall;", "∀");
                rss = rss.replaceAll("&part;", "∂");
                rss = rss.replaceAll("&exist;", "∃");
                rss = rss.replaceAll("&empty;", "∅");
                rss = rss.replaceAll("&nabla;", "∇");
                rss = rss.replaceAll("&isin;", "∈");
                rss = rss.replaceAll("&notin;", "∉");
                rss = rss.replaceAll("&ni;", "∋");
                rss = rss.replaceAll("&prod;", "∏");
                rss = rss.replaceAll("&sum;", "∑");
                rss = rss.replaceAll("&minus;", "−");
                rss = rss.replaceAll("&lowast;", "∗");
                rss = rss.replaceAll("&radic;", "√");
                rss = rss.replaceAll("&prop;", "∝");
                rss = rss.replaceAll("&infin;", "∞");
                rss = rss.replaceAll("&ang;", "∠");
                rss = rss.replaceAll("&and;", "∧");
                rss = rss.replaceAll("&or;", "∨");
                rss = rss.replaceAll("&cap;", "∩");
                rss = rss.replaceAll("&cup;", "∪");
                rss = rss.replaceAll("&int;", "∫");
                rss = rss.replaceAll("&there4;", "∴");
                rss = rss.replaceAll("&sim;", "∼");
                rss = rss.replaceAll("&cong;", "≅");
                rss = rss.replaceAll("&asymp;", "≈");
                rss = rss.replaceAll("&ne;", "≠");
                rss = rss.replaceAll("&equiv;", "≡");
                rss = rss.replaceAll("&le;", "≤");
                rss = rss.replaceAll("&ge;", "≥");
                rss = rss.replaceAll("&sub;", "⊂");
                rss = rss.replaceAll("&sup;", "⊃");
                rss = rss.replaceAll("&nsub;", "⊄");
                rss = rss.replaceAll("&sube;", "⊆");
                rss = rss.replaceAll("&supe;", "⊇");
                rss = rss.replaceAll("&oplus;", "⊕");
                rss = rss.replaceAll("&otimes;", "⊗");
                rss = rss.replaceAll("&perp;", "⊥");
                rss = rss.replaceAll("&sdot;", "⋅");
                rss = rss.replaceAll("&lceil;", "⌈");
                rss = rss.replaceAll("&rceil;", "⌉");
                rss = rss.replaceAll("&lfloor;", "⌊");
                rss = rss.replaceAll("&rfloor;", "⌋");
                rss = rss.replaceAll("&lang;", "〈");
                rss = rss.replaceAll("&rang;", "〉");
                rss = rss.replaceAll("&loz;", "◊");
                rss = rss.replaceAll("&spades;", "♠");
                rss = rss.replaceAll("&clubs;", "♣");
                rss = rss.replaceAll("&hearts;", "♥");
                rss = rss.replaceAll("&diams;", "♦");
                rss = rss.replaceAll("&OElig;", "Œ");
                rss = rss.replaceAll("&oelig;", "œ");
                rss = rss.replaceAll("&Scaron;", "Š");
                rss = rss.replaceAll("&scaron;", "š");

                // Optional[
                rss = rss.replaceAll("Optional", "");

                // ]Optional[

                // replace [ with nothing
                rss = rss.replaceAll("\\[", "");
                // replace ] with nothing
                rss = rss.replaceAll("\\]", "");


                rss = rss.replaceAll("Originally posted by", "");
                //rss = rss.replaceAll("\n\n", "");

            }

            NachrichtenReaction.RSSNews = rss;

        }

        catch (Exception e) {
            e.printStackTrace();
        }

    }

}
