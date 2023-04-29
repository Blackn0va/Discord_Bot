package com.blackn0va.discord_bot;
import java.io.IOException;


public class Translate {

    public static void translate(String text) throws IOException {

        //we have 200 hadanit, 1 unit hadanit = 275 aUEC

        //String text = "Hello world!"



     }


     public static void calcHadanite(){
            
            //we have 200 hadanit, 1 unit hadanit = 275 aUEC, calculate the sum
            int hadanit = 200;
            int aUEC = 275;
            int sum = hadanit * aUEC;
            

            System.out.println("The sum is: " + sum);
    

            //
      

     }
    

     //async timer tick 10 mins
        public static void startTimer() {
            try {
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                calcHadanite();
                                startTimer();
                            }
                        },
                        600000);
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }



}
