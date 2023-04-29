Bot Der tolle Dinge macht, openai, rssnews, Regeln, berechtigungen setzen beim akzeptieren der Regeln usw..<br/>
Läuft auf Linux, Windows

Der Token wird aus einer Datei "token.txt" ausgelesen. Dabei wird unter Linux und Windows Betriesbssystemen unterschieden. Unter linux muss der Token unter

#Linux /root/token.txt

#Windows Desktop/token.txt

Die Datei token.txt muss 7 Zeilen haben:<br/> 
token (Discord Token)<br/>
openaitoken (Openai API Token)<br/>
ChannelID (Channelid where is the post for rules)<br/>
PostID (PostID for accept Rules)<br/>
GPTChannelID (ChannelID for openai chat) <br/>
status (Status for the Bot) <br/>
scnewsChannelID <br/>

Beispielinhalt: <br/>
MzgxOLAKSDLTE4NDEx.G28qtc.Rdmk8JD7fAJKASdhh-osXpxiAsd09flO9ZSMLKIE <br/>
sk-g5ALSKDJLJASHCVZ3BlbkFJXHHNluGJ68MUn1rT8HWX <br/>
1090699813835780096 <br/>
1091314958324727908 <br/>
1091136753735319642 <br/>
Star Citizen Alpha 4.19 <br/>
1091136753735319642<br/>

Die Zeilen dürfen nicht leer sein, wenn ihr keinen OpenAI API Key habt, schreibt irgendwas rein.

Linux install:

apt install openjdk-17-jdk screen -y

Run Bot: screen -mdS dcbot java -jar Discord_Bot.jar

Commands: <br/>!regeln <br/>!stream https://www.twitch.tv/8lackn0va <br/>!say Mahlzeit <br/>!status Star citizen 3.18 <br/>!play https://open.spotify.com/track/2YFtpiy2WoAQVQbM1SIwES <br/>!News (RSS Feed from Robertspace)

                ToDo:
                    RSS Feed format
                    //

Reactions: Hallo
