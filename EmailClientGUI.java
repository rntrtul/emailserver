import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EmailClientGUI implements ActionListener {
    private JFrame frame = null;
    private JPanel panel1 = null;
    private JPanel panel2 = null;
    private JPanel panel3 = null;
    
    private JLabel titlesLabel = null;
    
    private EmailClientPane eMailClientPane = null;
    
    private JButton compose = null;
    private JButton getMail = null;
    private JButton delete  = null;
    private JButton inBox   = null;
    private JButton outBox  = null;
    private JButton serverShutdown = null;
    
    private int currentScreen = -1; 

    public EmailClientGUI() {   
        frame = new JFrame("Bloor CI Email Client Version 2017.0");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocation(100, 50);
        frame.setResizable(false);
        // frame.addWindowListener(new WindowEventHandler());

        Container contentPane = frame.getContentPane();
        BoxLayout contentPaneLayout = new BoxLayout(contentPane, BoxLayout.Y_AXIS);
        contentPane.setLayout(contentPaneLayout);
        
        panel1 = new JPanel();
        panel2 = new JPanel();
        panel3 = new JPanel();
        
        contentPane.add(panel1);
        contentPane.add(panel2);
        contentPane.add(panel3);
        
        // set up the first panel
        FlowLayout panel1Layout = new FlowLayout(FlowLayout.LEFT);
        panel1.setLayout(panel1Layout);
        
        titlesLabel = new JLabel(" From       Date Received                 Subject");
        titlesLabel.setFont(new Font("Courier New", Font.BOLD, 14));
        panel1.add(titlesLabel);
        
        // set up the second panel
        FlowLayout panel2Layout = new FlowLayout(FlowLayout.LEFT);
        panel2.setLayout(panel2Layout);
        currentScreen = Globals.RECEIVER_ID;
        eMailClientPane = new EmailClientPane(currentScreen);
        panel2.add(eMailClientPane.getSplitPane());
        

        // set up the third panel
        FlowLayout panel3Layout = new FlowLayout(FlowLayout.CENTER);
        panel3.setLayout(panel3Layout);
        panel3.setPreferredSize(new Dimension(800, 40));

        compose = new JButton("Compose");
        getMail = new JButton("Get Mail");
        inBox   = new JButton("InBox");
        outBox  = new JButton("OutBox");
        delete  = new JButton("Delete");
        serverShutdown = new JButton("Server Shutdown");

        compose.addActionListener(this);
        getMail.addActionListener(this);
        inBox.addActionListener(this);
        outBox.addActionListener(this);
        delete.addActionListener(this);
        serverShutdown.addActionListener(this);
        
        panel3.add(compose);
        panel3.add(getMail);
        panel3.add(inBox);
        panel3.add(outBox);
        panel3.add(delete);
        panel3.add(serverShutdown);
        
        frame.pack();
        frame.setVisible(true);
    }
    
    public void actionPerformed(ActionEvent event) {
        Object buttonPressed = event.getSource();
        
        if (buttonPressed == compose) {
            EmailClientComposeMessage c = new EmailClientComposeMessage();
        }
        else if (buttonPressed == delete) {
            int itemIndex = eMailClientPane.getItemIndex();
            String message = Globals.STR_NULL + 
                             Globals.DELETE_MESSAGE + 
                             Globals.boxMessages[itemIndex].substring(Globals.SENDER_POS, Globals.FIRST_RECORD_MARKER_POS) + 
                             Globals.FIRST_RECORD_MARKER + 
                             Utils.leftPad(NetIO.myUserName(), Globals.SENDER_LEN, '0') +        // use the subject field to identify who sent the delete request
                             Globals.END_OF_SUBJECT_MARKER +
                             "Dummy Message Body";            // dummy subject and text so that the server can do a setMessage() if needed

            System.out.println(message);
            System.out.println(message.length());
            
            int errorCode = NetIO.sendRequest(message, Globals.SERVER_IP_ADDRESS);        
            if (errorCode == Globals.NET_OK) {
                updateBoxMessages(itemIndex);
                panel1.remove(titlesLabel);
                switch(currentScreen) {
                    case Globals.RECEIVER_ID : titlesLabel = new JLabel(" From       Date Received                 Subject");
                                               break;
                    case Globals.SENDER_ID   : titlesLabel = new JLabel(" To         Date Received                 Subject");
                                               break;
                }
                titlesLabel.setFont(new Font("Courier New", Font.BOLD, 14));
                panel1.add(titlesLabel);

                panel2.remove(eMailClientPane.getSplitPane());
                eMailClientPane = new EmailClientPane(currentScreen);
                panel2.add(eMailClientPane.getSplitPane());
                frame.pack();
                frame.setVisible(true);              
            }
            else {
                JOptionPane.showMessageDialog (null, 
                                               "Message not delivered", 
                                               "ICS Bloor CI", 
                                               JOptionPane.ERROR_MESSAGE);
            }
        }
        else if (buttonPressed == inBox || buttonPressed == getMail) {
            for (int i = 0; i < Globals.boxMessages.length; i++) {
                Globals.boxMessages[i] = Globals.EMPTY_CLIENT_MESSAGE; //Globals.STR_NULL;
            }
            
            int error = MailTransfers.EmailClientRequestAllMail(Globals.RECEIVER_ID);
            if (error == Globals.PROCESS_OK) {
                panel1.remove(titlesLabel);
                titlesLabel = new JLabel(" From       Date Received                 Subject");
                titlesLabel.setFont(new Font("Courier New", Font.BOLD, 14));
                panel1.add(titlesLabel);

                panel2.remove(eMailClientPane.getSplitPane());
                currentScreen = Globals.RECEIVER_ID;
                eMailClientPane = new EmailClientPane(currentScreen);
                panel2.add(eMailClientPane.getSplitPane());
                frame.pack();
                frame.setVisible(true); 
            }
            else if (Globals.DEBUG_ON) {
                System.out.println("Error loading boxMessages: Globals.boxMessages[] is now not consistent with display");
            }
        }
        else if (buttonPressed == outBox) {
            for (int i = 0; i < Globals.boxMessages.length; i++) {
                Globals.boxMessages[i] = Globals.EMPTY_CLIENT_MESSAGE; //Globals.STR_NULL;
            }
            
            int error = MailTransfers.EmailClientRequestAllMail(Globals.SENDER_ID);
            if (error == Globals.PROCESS_OK) {
                panel1.remove(titlesLabel);
                titlesLabel = new JLabel(" To         Date Received                 Subject");
                titlesLabel.setFont(new Font("Courier New", Font.BOLD, 14));
                panel1.add(titlesLabel);
                
                panel2.remove(eMailClientPane.getSplitPane());
                currentScreen = Globals.SENDER_ID;
                eMailClientPane = new EmailClientPane(currentScreen);
                panel2.add(eMailClientPane.getSplitPane());
                frame.pack();
                frame.setVisible(true); 
            }
            else if (Globals.DEBUG_ON) {
                System.out.println("Error loading boxMessages: Globals.boxMessages[] is now not consistent with display");
            }
        }
        else if (buttonPressed == serverShutdown) {
            System.out.println("Server shutdown in process...");
            int errorCode = NetIO.sendRequest("" + Globals.SERVER_SHUTDOWN, Globals.SERVER_IP_ADDRESS);
            if (errorCode == 0) {
                System.out.println("...successful. All files closed.");
                frame.dispose();
            }
            else {
                System.out.println("...error in transmission. Not able to shutdown server. Client still running");
            }
        }
    }
    
    private static void updateBoxMessages(int itemIndex) {
        int i = itemIndex;
        for (i = itemIndex; i < (Globals.boxMessages.length - 1) && !Globals.boxMessages[i].equals(Globals.EMPTY_CLIENT_MESSAGE); i++) {
            Globals.boxMessages[i] = Globals.boxMessages[i + 1];
        }
        Globals.boxMessages[i] = Globals.EMPTY_CLIENT_MESSAGE;       
    }
}
