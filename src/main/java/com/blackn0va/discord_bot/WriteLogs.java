package com.blackn0va.discord_bot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WriteLogs {
    // determine os if linux or windows, if windows save logfile on Desktop, if
    // linux save in root folder log.txt
    public static String os = System.getProperty("os.name").toLowerCase();
    public static String desktopPath = System.getProperty("user.home") + "/Desktop";
    public static String logPath = "";
    public static String logFile = "log.txt";

    public static void writeLog(String log) {

        // check os if windows or linux, if windows check if file exists on desktop
        // calles lig, else create it and write logs, if os is linux then check if file
        // exists in root folder, else create it and write logs
        if (os.contains("win")) {
            try {
                logPath = desktopPath + "/" + logFile;
                // if file not exists, create it
                if (!new File(logPath).exists()) {
                    new File(logPath).createNewFile();
                    // insert 6 lines in the file
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter(logPath))) {
                        bw.write("Logfile");
                        bw.newLine();
                        bw.write("--------");
                        bw.newLine();
                        bw.write("Logfile created");
                        bw.newLine();
                        bw.write("--------");
                        bw.newLine();                        
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                // if file exists, write logs
                if (new File(logPath).exists()) {
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter(logPath, true))) {
                        bw.newLine();
                        bw.write(log);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
        } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            try {
                logPath = "/root/" + logFile;
                // if file not exists, create it
                if (!new File(logPath).exists()) {
                    new File(logPath).createNewFile();
                    // insert 6 lines in the file
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter(logPath))) {
                        bw.write("Logfile");
                        bw.newLine();
                        bw.write("--------");
                        bw.newLine();
                        bw.write("Logfile created");
                        bw.newLine();
                        bw.write("--------");
                        bw.newLine(); 
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            // if file exists, write logs
            catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }
    }
}
