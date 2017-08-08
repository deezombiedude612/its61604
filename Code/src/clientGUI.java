import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class clientGUI extends JFrame implements writeGUI{

        boolean stopaudioCapture = false;
        ByteArrayOutputStream byteOutputStream;
        AudioFormat adFormat;
        TargetDataLine targetDataLine;
        AudioInputStream InputStream;
        SourceDataLine sourceLine;
        Graphics g;
        DatagramSocket serverSocket;
        TargetDataLine mic;
        SourceDataLine speaker;
        InetAddress IP = InetAddress.getByName("127.0.0.1");
        ByteArrayOutputStream BAOS;
        AudioFormat audioformat;
        AudioInputStream AIS;
        boolean calling;
	
	private JTextField tfReceivePort, tfListenPort, textFieldClient;
	private JTextArea chatArea;
	private JButton btnConnect, btnDisconnect, btnSend;
	private JLabel portTo, portFrom;
	private JScrollPane scrollPane;
	
	private String username;
	
	String date = new Date().toString();
	String dateWithoutTime = date.substring(0, 10);
	String time = date.substring(0,16);
	BufferedWriter chatFile;
	String messageLog;
        audio1 t = new audio1();
        audio2 t2 = new audio2();

        
        int clientPort = ThreadLocalRandom.current().nextInt(1025, 49150 + 1);
   
/*        //for voice chat 
	public static AudioFormat getAudioFormat(){
	float sampleRate = 22050.0F;
	int sampleSizInbits = 16;
	int channel = 2;
	boolean signed = true;
	boolean bigEndian = true;
	return new AudioFormat(sampleRate, sampleSizInbits, channel, signed, bigEndian);
	}
	TargetDataLine audioInput;	*/

        //Creating Frame
	public clientGUI(String username) throws IOException{
            
                boolean stopaudioCapture = false;
                ByteArrayOutputStream byteOutputStream;
                AudioFormat adFormat;
                TargetDataLine targetDataLine;
                AudioInputStream InputStream;
                SourceDataLine sourceLine;
                Graphics g;
		
                this.username = username;
		
		setTitle("Client: " + username);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setLocationRelativeTo(null);
		/*Set it to absolute layout to allow easier customization*/
		getContentPane().setLayout(null);
		setVisible(true);
		
		
		//To receive Port Number
		tfReceivePort = new JTextField();
		tfReceivePort.setBounds(78, 7, 58, 26);//X-axis, Y-axis, width, height
		getContentPane().add(tfReceivePort);
		tfReceivePort.setColumns(10);
                tfReceivePort.setText(Integer.toString(clientPort));
                tfReceivePort.setEditable(false);

		//Label for portNumber
		portTo = new JLabel("Client Port: ");
		portTo.setFont(new Font("Lato", Font.PLAIN,11));
		portTo.setBounds(10, 12, 78, 16);//X-axis, Y-axis, width, height
		getContentPane().add(portTo);
		
		
		//To Create a connection
		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                            try {
                                btnConnectPerform();
                                
                            } catch (UnknownHostException ex) {
                                Logger.getLogger(clientGUI.class.getName()).log(Level.SEVERE, null, ex);
                            }
			}

		});
		
		btnConnect.setFont(new Font("Lato", Font.PLAIN, 12));
		btnConnect.setBounds(7, 34, 74, 29); //X-axis, Y-axis, width, height
		getContentPane().add(btnConnect);
		//To disconnect a connection and save message log
		btnDisconnect = new JButton("Disconnect");
		btnDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                            try {
                                btnDisconnectPerform();
//				JOptionPane.showMessageDialog(null, "System will now terminate.");
                            } catch (UnknownHostException ex) {
                                Logger.getLogger(clientGUI.class.getName()).log(Level.SEVERE, null, ex);
                            }
			}
		});
		btnDisconnect.setFont(new Font("Lato", Font.PLAIN, 12));
		btnDisconnect.setBounds(73, 34, 92, 29); //X-axis, Y-axis, width, height
		getContentPane().add(btnDisconnect);
		
		
		//To send messages
		btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					btnSentPerform();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnSend.setBounds(370, 246, 66, 29); //X-axis, Y-axis, width, height
		getContentPane().add(btnSend);
		
		//To type in and send messages
		textFieldClient = new JTextField();
		//if Enter button is selected, it will send message
		textFieldClient.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER){
					try {
						btnSentPerform();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		textFieldClient.setBounds(16, 247, 355, 26);
		getContentPane().add(textFieldClient);
		textFieldClient.setColumns(10);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(19, 69, 411, 175);
		getContentPane().add(scrollPane);
		
		chatArea = new JTextArea();
		scrollPane.setViewportView(chatArea);
		chatArea.setEditable(false);
                
		//To view message sent and receive
		textFieldClient.requestFocusInWindow();
                
                label = new JLabel("Voice Message:");
		label.setFont(new Font("Lato", Font.PLAIN, 12));
		label.setBounds(234, 11, 98, 16);
		getContentPane().add(label);
		
		btnStart_Voice = new JButton("Channel 1");
		btnStart_Voice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
           //                 new audioClient(42897);
                         Thread thread = new Thread() {
                        public void run() {
                            //      int newPort1;
                            //    newPort1 = (Integer.parseInt(tfPortC1.getText())) +1;
                            
                            t.callConnect();
			}
		
                 
                        };
                        thread.start();
             
			}
		});
		
		btnStart_Voice.setFont(new Font("Lato", Font.PLAIN, 12));
		btnStart_Voice.setBounds(333, 7, 105, 29);
		getContentPane().add(btnStart_Voice);
                
                btnStart_Voice2 = new JButton("Channel 2");
		btnStart_Voice2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
           //                 new audioClient(42897);
                         Thread thread2 = new Thread() {
                        public void run() {
                      //  new audioServer2().runVOIP();
                      			              
                              //      int newPort1;
                                //    newPort1 = (Integer.parseInt(tfPortC1.getText())) +1;
                

                                t2.callConnect();
			
			
                        }
                        };
                        thread2.start();
             
			}
		});
		
		btnStart_Voice2.setFont(new Font("Lato", Font.PLAIN, 12));
		btnStart_Voice2.setBounds(333, 37, 105, 29);
		getContentPane().add(btnStart_Voice2);
		
/*		btnStop_Voice = new JButton("Stop");
		btnStop_Voice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                            new audioServer().runVOIP();
//				account.calling = false;
			}
		});*/
//		btnStop_Voice.setFont(new Font("Lato", Font.PLAIN, 12));
//		btnStop_Voice.setBounds(383, 7, 58, 29);
//		getContentPane().add(btnStop_Voice);
                }
        
	//Create a Client and Client Server object	
	agentServer server;
	client client;
        //audio play;
	private JLabel label;
	private JButton btnStart_Voice, btnStart_Voice2;
//	private JButton btnStop_Voice;
		
	//Actions perform by buttons
	private void btnConnectPerform() throws UnknownHostException {
		server = new agentServer(this, clientPort);
                client = new client("" +username + " has just joined the chat!", 42897);
                client.start();
		server.start();
                JOptionPane.showMessageDialog(null, "Connection with Agent is established");
                
		
	}
	
	private void btnDisconnectPerform() throws UnknownHostException{
		int selectedOption = JOptionPane.showConfirmDialog(null, "Do you want to save file?", "Choose", JOptionPane.YES_NO_OPTION); 
		if (selectedOption == JOptionPane.YES_OPTION) {
			String date = new Date().toString();
			String dateWithoutTime = date.substring(0, 10);                       
			File file = new File("clientLog.txt");

		}
                client = new client("" +username + " has just left the chat!", 42897);
                client.start();
		tfReceivePort.setText("");
		textFieldClient.setText("");
		chatArea.setText("");
		JOptionPane.showMessageDialog(null, "System will now terminate.");
                System.exit(0);
	}
	
	private void btnSentPerform() throws IOException{
		if(textFieldClient.getText().equals("")){
			return;
		}
		//create client
		client = new client("" +username + ": " + textFieldClient.getText(), 42897);
		chatArea.append("" +username + ": " + textFieldClient.getText() + "\n\r");
		chatFile = new BufferedWriter(new FileWriter("clientLog.txt", true));
		messageLog = textFieldClient.getText();
		chatFile.write(time + "\t" + "" +username + ": " + "\t " + messageLog+"\n");
		chatFile.close();
		textFieldClient.setText("");
		client.start();
		
	}/*
//start Audio
	public void initialise_Audio() throws UnknownHostException{
		try{
			//Audio Format contains the information of the audio
			AudioFormat format = getAudioFormat();
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
			if(!AudioSystem.isLineSupported(info)){
                        System.out.println("Not suported!");
		        JOptionPane.showMessageDialog(null, "This audio is not supported!");
            }
			
			audioInput = (TargetDataLine) AudioSystem.getLine(info);
			audioInput.open(format);
			audioInput.start();
			
//			InetAddress inet = InetAddress.getByName(textFieldIP.getText());
//			client = new Client(textFieldIP.getText(),Integer.parseInt(tfListenPort.getText()));
			client = new client(clientPort);
			
			client.setAudioInput(audioInput);
			client.setDataOut(new DatagramSocket());
//			client.setServerIP(inet);

			//set boolean = true
			account.calling = true;
			
			client.audio();
//			play.start();
			
//did not add btnStart and stop enabled
			
		}catch(LineUnavailableException | SocketException ex){
			ex.printStackTrace();
		}
	}

public SourceDataLine audioOutput;	
//Play Audio	
	public void play_Audio(int port){
		try{
			//Audio Format contains the information of the audio
			AudioFormat format = getAudioFormat();
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
			
			if(!AudioSystem.isLineSupported(info)){
                        System.out.println("Not suported!");
		        JOptionPane.showMessageDialog(null, "This audio is not supported!");
            }
			
			audioOutput = (SourceDataLine) AudioSystem.getLine(info);
			audioOutput.open(format);
			audioOutput.start();
			
			audio play = new audio();
			play.dataInput = new DatagramSocket(port);
			play.audioOutput = audioOutput;
			//set boolean = true
			account.calling = true;
	
			play.start();
			
//did not add btnStart and stop enabled
			
		}catch(LineUnavailableException | SocketException ex){
			ex.printStackTrace();
		}
	}*/
        
 /*       class receiver extends Thread {
        public void run() {
            try {
                serverSocket = new DatagramSocket(8081);
                byte[] buffer = new byte[10000];
                while (calling) {
                    DatagramPacket receiveData = new DatagramPacket(buffer, buffer.length);
                    serverSocket.receive(receiveData);
                    System.out.println("Received packet");
                    try {
                        byte voice[] = receiveData.getData();
                        InputStream BIS = new ByteArrayInputStream(voice);
                        AIS = new AudioInputStream(BIS, audioformat, voice.length / audioformat.getFrameSize());
                        Thread player = new Thread(new player());
                        player.start();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    class recorder extends Thread{
        byte buffer[] = new byte[10000];

        public void run(){
            BAOS = new ByteArrayOutputStream();
            try{
                while(calling){
                    int i = mic.read(buffer,0,buffer.length);
                    if(i>0){
                        DatagramPacket sendData = new DatagramPacket(buffer,buffer.length,IP,8080);
                        serverSocket.send(sendData);
                        System.out.println("Packet sent");
                        BAOS.write(buffer,0,i);
                    }
                }
                BAOS.close();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    class player extends Thread{
        byte buffer[] = new byte[10000];

        public void run(){
            try{
                int i;
                while((i=AIS.read(buffer,0,buffer.length))!= -1){
                    if(i>0){
                        speaker.write(buffer,0,i);
                        System.out.println("Audio played.");
                    }
                }
            }
            catch(Exception e){
            }
        }
    }
    public void end_audio(){
        try {
            speaker.drain();
            speaker.close();
            AIS.close();
            serverSocket.close();
            mic.drain();
            mic.close();
            AIS.close();
        } catch (IOException e) {

        }
    }
    public static AudioFormat getaudioformat(){
        float rate = 16000.0F;
        int samplesize = 16;
        int cc = 1;
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(rate, samplesize, cc, signed, bigEndian);
    }
    public void init_audio(){
        try{
            audioformat = getaudioformat();
            DataLine.Info info_in = new DataLine.Info(TargetDataLine.class,audioformat);
            DataLine.Info info_out = new DataLine.Info(SourceDataLine.class, audioformat);
            if(!AudioSystem.isLineSupported(info_in)){
                System.out.println("Input Info not supported!");
                System.exit(0);
            }
            if(!AudioSystem.isLineSupported(info_out)){
                System.out.println("Output Info not supported!");
                System.exit(0);
            }
            speaker = (SourceDataLine)AudioSystem.getLine(info_out);
            speaker.open(audioformat);
            speaker.start();

            mic = (TargetDataLine) AudioSystem.getLine(info_in);
            mic.open(audioformat);
            mic.start();
        }
        catch(LineUnavailableException e){
            e.printStackTrace();
        }
    }*/
        
	@Override
	public void writeGUI1(String s) {
		chatArea.append(s + textFieldClient.getText() + "\n\r");
	}
	public void writeGUI2(String s) {
		
	}
}
