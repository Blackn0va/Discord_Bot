Bot Der tolle Dinge macht.

Der Token wird aus einer Datei "token.txt" ausgelesen. Dabei wird unter Linux und Windows Betriesbssystemen unterschieden. Unter linux muss der Token unter

#Linux /root/token.txt

#Windows Desktop/token.txt

Die Datei token.txt muss 6 Zeilen haben: token openaitoken ChannelID PostID GPTChannelID status

Beispielinhalt: 
MzgxOLAKSDLTE4NDEx.G28qtc.Rdmk8JD7fAJKASdhh-osXpxiAsd09flO9ZSMLKIE 
sk-g5ALSKDJLJASHCVZ3BlbkFJXHHNluGJ68MUn1rT8HWX 
1090699813835780096 
1091314958324727908 
1091136753735319642 
Star Citizen Alpha 4.19

Die Zeilen dürfen nicht leer sein, wenn ihr keinen OpenAI API Key habt, schreibt irgendwas rein.

Linux install:

apt install openjdk-17-jdk screen -y

Run Bot: screen -mdS dcbot java -jar Discord_Bot.jar

Commands: !regeln !stream https://www.twitch.tv/8lackn0va !say Mahlzeit !status Star citizen 3.18 !play https://open.spotify.com/track/2YFtpiy2WoAQVQbM1SIwES !News (RSS Feed from Robertspace)

                ToDo:
                    RSS Feed format
                    //

Reactions: Hallo