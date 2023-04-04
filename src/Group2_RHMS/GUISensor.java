/*
    Section: B2
    Members: 
        - Sabah Baothman    2006269         - Shahad Magram     2010332
        - Roaa Altunsi      1914946         - Seham Nahlawi     1915762
        - Rahaf Koshak      2006583         - Arwa Kulib        2019574
 */

package Group2_RHMS;

import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class GUISensor extends javax.swing.JFrame {
   
    // Attributes of Sensor Client Application
    private Socket socket;
    private final int PORT = 8983;
    private static Scanner input;
    private static PrintWriter writeToServer;
    private int temperature;
    private int heartRate;
    private int O2Level;
    
    //--------------------------- Constructor ----------------------------------
    public GUISensor(InetAddress ip) throws IOException{
        socket = new Socket(ip, PORT);  // Create new client socket
        input = new Scanner(System.in);
        writeToServer = new PrintWriter(
            new DataOutputStream(socket.getOutputStream()), true ); 
    }

    
    // GUISensor Constructor 
    public GUISensor() {
        initComponents();
    }

    // This method is called from within the constructor to initialize the GUISensor form
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        t11 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        t11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t11ActionPerformed(evt);
            }
        });
        getContentPane().add(t11, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 510, 180, 40));

        jButton1.setBackground(new java.awt.Color(204, 204, 204));
        jButton1.setText("Next");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 620, 100, 50));

        jLabel1.setBackground(new java.awt.Color(204, 204, 204));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Group2_RHMS/1.png"))); // NOI18N
        jLabel1.setText("jLabel1");
        jLabel1.addHierarchyListener(new java.awt.event.HierarchyListener() {
            public void hierarchyChanged(java.awt.event.HierarchyEvent evt) {
                jLabel1HierarchyChanged(evt);
            }
        });
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 0, 1280, 740));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel1HierarchyChanged(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_jLabel1HierarchyChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel1HierarchyChanged

    private void t11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t11ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        long t1 = Long.parseLong(t11.getText()); // Take execution time from user
        
        if(t1<60){  // The minimum allowed execution time is 60s
            
           // Display Error message to the user
           JOptionPane.showMessageDialog(null,"Oops!\nThe minimum allowed time is 60 seconds");
           System.exit(0);
        }
        GUISensor client = null;
        try {
            // Creates new form GUISensor
            client = new GUISensor(InetAddress.getLocalHost());
        } catch (UnknownHostException ex) {
            Logger.getLogger(GUISensor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GUISensor.class.getName()).log(Level.SEVERE, null, ex);
        }
          
         
        writeToServer.println(t1*1000); // Send exec. time in milliseconds
                
      
        while(true){
                try {
                    // Display the second interface
                    GUIsensor2 gui = new GUIsensor2();
                    gui.setVisible(true);
                    client.cotrolExecution((t1*1000), gui); // Call cotrolExecution() method
                } catch (IOException | InterruptedException ex) {
                    Logger.getLogger(GUISensor.class.getName()).log(Level.SEVERE, null, ex);
                } break;
         
        }
   
    }//GEN-LAST:event_jButton1ActionPerformed

    
    
    public static void main(String args[]) throws UnknownHostException, IOException, InterruptedException {
        
        // Set the Nimbus look and feel
        // If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | 
            javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUISensor.class.getName())
                .log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        // Create and display the form
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUISensor().setVisible(true);
            }
            
        });
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField t11;
    // End of variables declaration//GEN-END:variables


 

    //-------------------------- Control execution -----------------------------
    public void cotrolExecution(long duration, GUIsensor2 gui) throws IOException, InterruptedException{
       
        // Measure finish time starting from the running moment
        long startExec = System.currentTimeMillis();
        long endExec = startExec + duration;
        boolean flag = false;
        
        while(!flag){

            Thread.sleep(5000); // Wait 5 seconds between each measurement
            takeCensoredData();      // Measure censored data
            send_displayCensoredData(gui);  // Send & Display censored data

            if(System.currentTimeMillis() >= endExec){
                flag = true;
                closeConnection(); 
            } 
   
        }        
    }

    
    //------------------------- Generate censored data -------------------------
    public void takeCensoredData(){
        // Generate random censored data
        temperature = (int)(Math.random() * 6 + 36);
        heartRate = (int)(Math.random() * 71 + 50);
        O2Level = (int)(Math.random() * 41 + 60);
    }
    
  
    //------------- Display and send censored data to personal server ----------
    public void send_displayCensoredData(GUIsensor2 gui) throws NumberFormatException{
        
         
        
        Date currentDate = new Date(); // Get date with specific format
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yy");
        
        LocalTime currentTime = LocalTime.now(); // Get time with specific format
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
        
        String tempMsg = "At date: " + dateFormat.format(currentDate)
                        + ", time " + currentTime.format(timeFormat)
                        + ", sensed temperature is " + temperature;
        
        String HRMsg = "At date: " + dateFormat.format(currentDate)
                        + ", time " + currentTime.format(timeFormat)
                        + ", sensed heart rate is " + heartRate;
        
        String O2Msg = "At date: " + dateFormat.format(currentDate)
                        + ", time " + currentTime.format(timeFormat)
                        + ", sensed oxygen saturation is " + O2Level;
        
        //for gui
        String o2 ="At date: " + dateFormat.format(currentDate)
                        + "\ntime " + currentTime.format(timeFormat)
                        + "\nsensed oxygen saturation is " + O2Level;
        
        String tem = "At date: " + dateFormat.format(currentDate)
                        + "\ntime " + currentTime.format(timeFormat)
                        + "\nsensed temperature is " + temperature;
        
        String hr = "At date: " + dateFormat.format(currentDate)
                        + "\ntime " + currentTime.format(timeFormat)
                        + "\nsensed heart rate is " + heartRate;
        
        // Send all messages to personal server
        writeToServer.println(tempMsg);    // Send temperature message
        writeToServer.println(HRMsg);       // Send heartRate message
        writeToServer.println(O2Msg);          // Send O2Level message
        
        // Display all the measured censored data
        System.out.println("\n" + tempMsg + "\n" + HRMsg + "\n" + O2Msg);
        gui.show(o2, hr, tem);
    }
    
    
    //------------------------- Close All Objects ------------------------------
    public void closeConnection() throws IOException{
        input.close();
        writeToServer.close();
        socket.close();
    }
    
}
