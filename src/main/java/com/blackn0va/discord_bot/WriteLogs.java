package com.blackn0va.discord_bot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WriteLogs {

    public static String logPath = "";
    public static String logFile = "log.txt";
    //get the working directory of the program
    public static String workingDir = System.getProperty("user.dir");

    public static void writeLog(String log) {
        //determine os
        String os = System.getProperty("os.name").toLowerCase();
        if(os.contains("win"))
        {
            logPath = workingDir + "\\" + logFile;

        }
        else if(os.contains("nix") || os.contains("nux") || os.contains("aix"))
        {
            logPath = workingDir  + "/" +  logFile;
        }
 
        // check os if windows or linux, if windows check if file exists on desktop
            try {
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
        

    }
}
