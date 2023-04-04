/*
    Section: B2
    Members: 
        - Sabah Baothman    2006269         - Shahad Magram     2010332
        - Roaa Altunsi      1914946         - Seham Nahlawi     1915762
        - Rahaf Koshak      2006583         - Arwa Kulib        2019574
 */

package Group2_RHMS;

import java.io.*;
import java.io.IOException;
import java.net.*;

public class MedicalServer {
    
    public static void main(String[] args) throws IOException {
        
        Socket socket;
        InputStreamReader reader;
        BufferedReader bufferR;
        ServerSocket serverSkt;
        
        String duration; long startExec; long endExec;
        String tem; String hr; String ox;
      
        
        serverSkt = new ServerSocket(7456);
        // GUI
        GUImedical gui =new GUImedical();
            gui.setVisible(true);
         
        // Server always on
        while(true){
            
                socket = serverSkt.accept();    // Handshaking
                // Convert stream into data so it can be read by the server
                reader = new InputStreamReader(socket.getInputStream());
                bufferR = new BufferedReader(reader);

                
                    
                startExec = System.currentTimeMillis();
                        
                // Take execution duration from the personal server
                duration = bufferR.readLine();
                        
                // Calculate end of execution time
                endExec = startExec + Long.parseLong(duration);
                
                // Persistent TCP connection
                while(true){
                        
                    tem = bufferR.readLine();   // Read tempreture msg
                    hr = bufferR.readLine();    // Read heart rate msg
                    ox = bufferR.readLine();    // Read oxagen msg
                        
                    // Print the masages
                    System.out.println("\n"+tem+"\n"+hr+"\n"+ox);
                                                
                    String action = check(tem.substring(tem.length()-3),
                        hr.substring(hr.length()-4),ox.substring(ox.length()-3),gui);
                    gui.show(tem, hr, ox,action); // Display on GUI
                           
                    if(System.currentTimeMillis() >= endExec+10000) // Take into account the delay
                        break;
                        
                }
                
                
                // End of client connection
                System.out.println("\n\n\nConnection is closed.\n----------------------");
                
                // Close connection
                socket.close();
                reader.close();
                bufferR.close();
                
                
        }
        
    } 
     
    
    
    
    
    public static String check(String tem, String hr, String ox,GUImedical gui){
        // tem = temperature, hr = heart rate, ox oxagen
        String action ="";
        // Flags to indicate abnormal
        boolean ta=false,ha=false,oa=false;
         
        // Temperature, heart rate and oxygen values
        int t=0,h=0,o=0;
         
        // Check if tempruter is abnormal
        if(java.lang.Character.isDigit(tem.charAt(0))){
            t=Integer.parseInt(tem.substring(0, tem.length()-1));
            ta=true;
        }
         
        // Check if heart rate is abnormal
        if(java.lang.Character.isDigit(hr.charAt(0))||hr.charAt(0)==' '){
            h=(hr.charAt(0)==' '?Integer.parseInt(hr.substring(1, hr.length()-1)):
                    Integer.parseInt(hr.substring(0, hr.length()-1)));
            ha=true;
        }
         
        // Check if oxagen is abnormal
        if(java.lang.Character.isDigit(ox.charAt(0))){
            o=Integer.parseInt(ox.substring(0, ox.length()-1));
            oa=true;
        }
         
        // If all values are abnormal
        if(ta&&ha&&oa){
            // If the temperature exceeds 39 and heart rate is above 100 and oxygen is below 95
            if(t>=39&&h>100&&o<95){
                action="ACTION: Send an ambulance to the patient!";
            }
             
            // If the temperature is between 38 and 38.9, and the heart rate is between 95 and 98, and oxygen is below 80
            if(t>=38&&t<39&&h>=95&&h<=98&&o<80){
                action= "ACTION: Call the patient's family!";
            }
        } else // Otherwise
            action="ACTION: Warning, advise patient to make a checkup appointment!";
         
        // Display action on GUI
        System.out.println(action);
        return action;
        
     }
    
}
