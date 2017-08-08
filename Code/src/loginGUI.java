import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;


import java.awt.Color;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class loginGUI extends JFrame {

     
	private JLabel welcomeLabel, labName, labPassword, labLoginAs;
	private JTextField tfUsername;
	private JButton btnAgent, btnCustomer, btnAU;
	private JPasswordField tfPassword;


	public loginGUI() {
                

		getContentPane().setBackground(Color.WHITE);
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 350);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		
		welcomeLabel = new JLabel("");
		Image img = new ImageIcon(this.getClass().getResource("wah.png")).getImage().getScaledInstance(342, 136, Image.SCALE_DEFAULT);
		welcomeLabel.setIcon(new ImageIcon(img));
		welcomeLabel.setBounds(48, 24, 377, 136);
		getContentPane().add(welcomeLabel);

		labName = new JLabel("Username: ");
		labName.setFont(new Font("Lato",Font.PLAIN, 18));
		labName.setBounds(67, 153, 104, 16);
		getContentPane().add(labName);
		
		tfUsername = new JTextField();
		tfUsername.setBounds(183, 148, 200, 26); //x-axis, y-axis, width, height
		getContentPane().add(tfUsername);
		tfUsername.setColumns(10);
		
		labPassword = new JLabel("Password: ");
		labPassword.setFont(new Font("Lato",Font.PLAIN, 18));
		labPassword.setBounds(72, 189, 101, 16);
		getContentPane().add(labPassword);
		
		tfPassword = new JPasswordField();
		tfPassword.setBounds(183, 186, 200, 26);
		getContentPane().add(tfPassword);

		//login as either agent of customer
		labLoginAs = new JLabel("Login as: ");
		labLoginAs.setBounds(83, 228, 87, 26);
		labLoginAs.setFont(new Font("Lato",Font.PLAIN, 18));
		getContentPane().add(labLoginAs);
		
		btnAgent = new JButton("Agent");
		btnAgent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					loginAsAgent();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		btnAgent.setBounds(180, 230, 95, 29);
		getContentPane().add(btnAgent);
		
		btnCustomer = new JButton("Client");
		btnCustomer.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					loginAsClient();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnCustomer.setBounds(278, 231, 106, 29);
		getContentPane().add(btnCustomer);
	
        
                btnAU = new JButton("About Us");
		btnAU.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					showMe();

                            } catch (Exception ex) {
                                Logger.getLogger(loginGUI.class.getName()).log(Level.SEVERE, null, ex);
                            }
			}
		});
		
		btnAU.setBounds(230, 270, 95, 29);
		getContentPane().add(btnAU);
        }
	account account;
	agentGUI agent;
	int count = 0;
        int cCount = 0;
        int aCount = 0;
        
        
        
	public void loginAsAgent() throws IOException{
		//System.out.println(account.readFromAgent(tfUsername.getText(), (new String(tfPassword.getPassword()))));
		if (account.readFromAgent(tfUsername.getText(), (new String(tfPassword.getPassword()))) == 0){
			dispose();
			new agentGUI(tfUsername.getText());
                        JOptionPane.showMessageDialog(null, "Login successful!");

		}else{
			JOptionPane.showMessageDialog(null, "Invalid username or password. Please try again");
			tfUsername.setText("");
			tfPassword.setText("");
			count++;
			if(count == 3){
				JOptionPane.showMessageDialog(null, "No more login attempts.\nSystem will now terminate!");
				System.exit(0);
				dispose();
			}
		}
	}
	
	clientGUI client;

	public void loginAsClient() throws IOException{
		if (account.readFromClient(tfUsername.getText(), (new String(tfPassword.getPassword()))) == 0){
			dispose();
			new clientGUI(tfUsername.getText());
			JOptionPane.showMessageDialog(null, "Login successful!");

		}else{
			JOptionPane.showMessageDialog(null, "Invalid username or password. Please try again");
			tfUsername.setText("");
			tfPassword.setText("");
			count++;
			if(count == 3){
				JOptionPane.showMessageDialog(null, "No more login attempts.\nSystem will now terminate!");
				System.exit(0);
				dispose();
			}
		}
	}
        public void showMe() throws Exception{
            int a =0;
            String[] arguments = new String[] {"123"};
            new AUServer().main(arguments);
            JOptionPane.showMessageDialog(null, "Are you ready?");
            a = 1;
            if (a==1){
            String[] argument = new String[] {"123"};
            new AUClient().main(argument);
          //  JOptionPane.showMessageDialog(null, "Wei Liang, Andrew, Henry (WAH). All Rights Reserved. 2017.");
            }
        
       /* System.setProperty("java.rmi.server.hostname","127.0.0.1");
        Registry registry = LocateRegistry.getRegistry("127.0.0.1");
        AUInterface
        showAU = (AUInterface)registry.lookup("ToShowAU");
        System.out.println(showAU.newAU());
        */
        }
        
}	

