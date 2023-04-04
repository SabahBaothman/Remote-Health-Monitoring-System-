/*
    Section: B2
    Members: 
        - Sabah Baothman    2006269         - Shahad Magram     2010332
        - Roaa Altunsi      1914946         - Seham Nahlawi     1915762
        - Rahaf Koshak      2006583         - Arwa Kulib        2019574
 */

package Group2_RHMS;

import java.io.*;
import java.net.*;

public class PersonalServer {

    private static int serverPort = 8983;
    private static int clientPort = 7456;
    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static InputStreamReader fromClient;
    private static DataOutputStream toServer;
    private static BufferedReader read;
    private static PrintWriter write;

    public static void main(String[] args) throws IOException, InterruptedException {
        
        // Variables
        Socket socket;
        String duration;
        long startExec;
        long endExec;
        String tempMsg;
        String HRMsg;
        String O2Msg;
        

        // Create a server socket  
        serverSocket = new ServerSocket(serverPort);

        // Create TCP socket in the client that takes two parameter
        clientSocket = new Socket("localhost", clientPort);
        toServer = new DataOutputStream(clientSocket.getOutputStream());
        write = new PrintWriter(toServer, true);
        
        // GUI  
        GUIpersonal gui = new GUIpersonal();
        gui.setVisible(true);

        while (true) {
            // Create Socket, Handshaking (accept request comes from client)
            socket = serverSocket.accept();
            // Inform that the client connection is established by disply message
            System.out.println("Server Started....");
            // Convert stream into data so it can be read by the server
            fromClient = new InputStreamReader(socket.getInputStream());
            read = new BufferedReader(fromClient);
           

                
            startExec = System.currentTimeMillis();

            // Take execution duration from the client
            duration = read.readLine();

            // Send duration to Medical Server
            write.println(duration);

            // Calculate end of execution time
            endExec = startExec + Long.parseLong(duration);

            while (true) {

                // Take censored data from client 
                tempMsg = read.readLine();
                HRMsg = read.readLine();
                O2Msg = read.readLine();

                checkState(tempMsg, HRMsg, O2Msg,gui);

                if (System.currentTimeMillis() >= endExec) {
                    break;
                }
            }

            // End of client connection
            System.out.println("\n\n\nConnection is closed.\n----------------------");
                

            

            // Connection close.
            socket.close();

        }
    }

    // --------- Check data comes from clint (normal / low / high) -------------
    public static void checkState(String temp, String heartrate, 
            String oxagen,GUIpersonal gui) throws IOException {
        
        // Var to check state 
        boolean stateT = false, stateH = false, stateO = false;
        // Var message to be print it 
        String printT, printH, printO = "";

        // --Check temperture--
        String t[] = temp.split("^(.+[temperature is ](\\s))");

        String splitT[] = temp.split(", ");
        // Check for match (normal state)
        if (t[1].matches("^([3-3][6-8])")) {
            stateT = true;
            printT = splitT[0] + ", " + splitT[1] + ", "
                    + "Temperature is normal";

        } else {    // Not normal
            printT = splitT[0] + ", " + splitT[1] + ", "
                    + "Temperature is high " + t[1] + ".";
        }

        // --Check heartrate--
        String h[] = heartrate.split("^(.+[heart rate is](\\s))");

        String splitH[] = heartrate.split(", "); // Split message
        // Check for match (normal state)
        if (h[1].matches("([6-9][0-9])|100")) {
            stateH = true;
            printH = splitH[0] + ", " + splitH[1] + ", "
                    + "Heart rate is normal";
        } else if (h[1].matches("[1-1][0-1][0-9]|120")) {   // High 

            printH = splitH[0] + ", " + splitH[1] + ", "
                    + "Heart rate is above normal " + h[1] + ".";
        } else {  // Under
            printH = splitH[0] + ", " + splitH[1] + ", "
                    + "Heart rate is under normal " + h[1] + ".";
        }

        // --Check oxagen--
        String o[] = oxagen.split("^(.+[oxygen saturation is](\\s))");

        String splitO[] = oxagen.split(", "); // Split message
        // Check for match (normal state)
        if (o[1].matches("([9-9][5-9])|100")) {
            stateO = true;
            printO = splitO[0] + ", " + splitO[1] + ", "
                    + "Oxygen Saturation is normal";
        } else { // Low

            printO = splitO[0] + ", " + splitO[1] + ", "
                    + "Oxygen Saturations is low " + o[1] + ".";
        }
        
        // Send state to "sendNotification" to decide action
        sendNotification(stateT, printT, stateH, printH, stateO, printO,gui);

    }
    
    //---- Decide whether to send notifaction to the medical caregiver or no ---
    public static void sendNotification(boolean stateT, String printT, boolean stateH,
            String printH, boolean stateO, String printO,GUIpersonal gui) throws IOException {

        // Display messages
        System.out.println();
        System.out.println((stateT) ? printT : printT
                + " An alert message is sent to the Medical Server.");
        System.out.println((stateH) ? printH : printH
                + " An alert message is sent to the Medical Server.");
        System.out.println((stateO) ? printO : printO
                + " An alert message is sent to the Medical Server.");

        write.println(printT); // Send temperature message
        write.println(printH); // Send heartRate message
        write.println(printO); // Send O2Level message 
        
        // For GUI
        String splitO[] = printO.split(", "); // Split message
        String splitt[] = printT.split(", "); // Split message
        String splith[] = printH.split(", "); // Split message
        printO = splitO[0] + ", \n" + splitO[1] + ", \n" +splitO[2];
        printT = splitt[0] + ", \n" + splitt[1] + ", \n" +splitt[2];
        printH = splith[0] + ", \n" + splith[1] + ", \n" +splith[2];
        // Display messages on GUI
        gui.show( printO, printT, printH);
       
    }

}
