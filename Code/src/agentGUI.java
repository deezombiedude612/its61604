import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;

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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;


public class agentGUI extends JFrame implements writeGUI {
        
	private JPanel contentPane;
	private JTextField textField;
	private JLabel lblClient1, lblClient2;
	private JButton btn1, btn2, btnAll;
	private JScrollPane scrollClient1;
	private JTextArea chatAreaC1, chatAreaC2;

	agent agent1, agent2;
	agentServer agentServer;
	private JButton btnDisconnectC2;
	private JButton btnConnectC2;
	private JTextField tfPortC1;
	private JTextField tfPortC2;
	private JLabel lbSendTo;

	String date = new Date().toString();
	String dateWithoutTime = date.substring(0, 10);
	String time = date.substring(0, 16);
	BufferedWriter chatFile;
	String messageLog;
        //int agentPort = ThreadLocalRandom.current().nextInt(1025, 49150 + 1);
        int agentPort = 42897;
        audio1 ta = new audio1();
        audio2 ty = new audio2();
        
        boolean stopaudioCapture = false;
        ByteArrayOutputStream byteOutputStream;
        AudioFormat adFormat;
        TargetDataLine targetDataLine;
        AudioInputStream InputStream;
        SourceDataLine sourceLine;
        Graphics g;
        DatagramSocket clientSocket;
        DatagramSocket clientSocket2;
        
/*	public static AudioFormat getAudioFormat(){
            float sampleRate = 22050.0F;
            int sampleSizInbits = 16;
            int channel = 2;
            boolean signed = true;
            boolean bigEndian = true;
            return new AudioFormat(sampleRate, sampleSizInbits, channel, signed, bigEndian);
	}*/
        
//	public SourceDataLine audioOutput;
//	public TargetDataLine audioInput;

	private String username;

	public agentGUI(String username) throws IOException {
		this.username = username;
		setTitle("" + username);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 480, 520);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setVisible(true);

		lblClient1 = new JLabel("Client 1");
		lblClient1.setBounds(84, 37, 61, 16);
		contentPane.add(lblClient1);

		scrollClient1 = new JScrollPane();
		scrollClient1.setBounds(22, 109, 424, 213);
		contentPane.add(scrollClient1);

		chatAreaC1 = new JTextArea();
		scrollClient1.setViewportView(chatAreaC1);
		chatAreaC1.setEditable(false);

		lblClient2 = new JLabel("Client 2");
		lblClient2.setBounds(308, 37, 61, 16);
		contentPane.add(lblClient2);


		// to type in message by agent
		textField = new JTextField();
		textField.setBounds(11, 331, 446, 35);
		contentPane.add(textField);
		textField.setColumns(10);

		// Button to send to specific clients

		lbSendTo = new JLabel("Send to:");
		lbSendTo.setFont(new Font("Lato", Font.PLAIN, 12));
		lbSendTo.setBounds(235, 375, 61, 16);
		contentPane.add(lbSendTo);

		btn1 = new JButton("1");
		btn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					btnSendClient1();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btn1.setBounds(286, 368, 58, 29);
		contentPane.add(btn1);

		btn2 = new JButton("2");
		btn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					btnSendClient2();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btn2.setBounds(343, 368, 58, 29);
		contentPane.add(btn2);

		btnAll = new JButton("All");
		btnAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					btnSendAll();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnAll.setBounds(400, 368, 58, 29);
		contentPane.add(btnAll);

		// Client 1
		JLabel lbPortC1 = new JLabel("Listen from Port: ");
		lbPortC1.setBounds(12, 9, 118, 16);
		contentPane.add(lbPortC1);


		JButton btnConnectC1 = new JButton("Connect");
		btnConnectC1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnConnectClient1();
                                
			}
		});

		btnConnectC1.setBounds(49, 80, 81, 29);
		contentPane.add(btnConnectC1);

		JButton btnDisconnectC1 = new JButton("Disconnect");
		btnDisconnectC1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDisconnect1();

			}
		});
		btnDisconnectC1.setBounds(124, 80, 102, 29);
		contentPane.add(btnDisconnectC1);

		tfPortC1 = new JTextField();
		tfPortC1.setBounds(151, 4, 61, 26);
		contentPane.add(tfPortC1);
		tfPortC1.setColumns(10);


		// Client 2
		JLabel lbPortC2 = new JLabel("Listen From Port:");
		lbPortC2.setBounds(235, 12, 111, 16);
		contentPane.add(lbPortC2);


		btnConnectC2 = new JButton("Connect");
		btnConnectC2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnConnectClient2();
                               
			}
		});

		btnDisconnectC2 = new JButton("Disconnect");
		btnDisconnectC2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDisconnect2();
			}
		});
		btnDisconnectC2.setBounds(349, 80, 102, 29);
		contentPane.add(btnDisconnectC2);

		btnConnectC2.setBounds(273, 79, 81, 29);
		contentPane.add(btnConnectC2);

		tfPortC2 = new JTextField();
		tfPortC2.setColumns(10);
		tfPortC2.setBounds(380, 6, 61, 26);
		contentPane.add(tfPortC2);
                

                // have a break, have a kit kat //
        
                JLabel lblVoiceMessage = new JLabel("Voice Message:");
		lblVoiceMessage.setFont(new Font("Lato", Font.PLAIN, 13));
		lblVoiceMessage.setBounds(16, 415, 98, 16);
		contentPane.add(lblVoiceMessage);
		

                
		JButton btnStart_1_Voice = new JButton("Start (1)");
		btnStart_1_Voice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                            //      int newPort1;
                            //    newPort1 = (Integer.parseInt(tfPortC1.getText())) +1;
                            
                           
                            ta.callConnect();
                            
                            //      captureAudio();
                            //                new audioServer().runVOIP(42897);
                            //initialise_Audio("127.0.0.1", Integer.parseInt(tfPortC1.getText()));
			}
		});
		btnStart_1_Voice.setFont(new Font("Lato", Font.PLAIN, 13));
		btnStart_1_Voice.setBounds(110, 412, 87, 29);
		contentPane.add(btnStart_1_Voice);
		
		JButton btnStart_2_Voice = new JButton("Stop (1)");
		btnStart_2_Voice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                            //            int newPort2;
                            //           newPort2 = (Integer.parseInt(tfPortC2.getText())) +1;
                      
                            ta.callDisconnect();
                            //                        end_audio();
//                                        sourceLine.drain();
//                                       sourceLine.close();
//                            InputStream.close();
//                       targetDataLine.drain();
//                     targetDataLine.close();
//                      clientSocket.close();
//             new audioServer().runVOIP(42897);
//initialise_Audio("127.0.0.1", Integer.parseInt(tfPortC2.getText()));
//					initialise_Audio(InetAddress.getByName(tfIPC2.getText()), Integer.parseInt(AgentPortC2.getText()));
			}
		});
		btnStart_2_Voice.setFont(new Font("Lato", Font.PLAIN, 13));
		btnStart_2_Voice.setBounds(197, 412, 94, 29);
		contentPane.add(btnStart_2_Voice);
                
		// Number 2
                
                JButton btnStart_5_Voice = new JButton("Start (2)");
		btnStart_5_Voice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                            //      int newPort1;
                            //    newPort1 = (Integer.parseInt(tfPortC1.getText())) +1;
                            
                            
                            ty.callConnect();
                            //captureAudio2();
                            //                new audioServer().runVOIP(42897);
                            //initialise_Audio("127.0.0.1", Integer.parseInt(tfPortC1.getText()));
			}
		});
		btnStart_5_Voice.setFont(new Font("Lato", Font.PLAIN, 13));
		btnStart_5_Voice.setBounds(290, 412, 87, 29);
		contentPane.add(btnStart_5_Voice);
		
		JButton btnStart_10_Voice = new JButton("Stop (2)");
		btnStart_10_Voice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                            //            int newPort2;
                            //           newPort2 = (Integer.parseInt(tfPortC2.getText())) +1;
                            //                    stopaudioCapture = true;
                            
                            ty.callDisconnect();
                            //                          sourceLine.drain();
                            //                        sourceLine.close();
                            //                            InputStream.close();
                            //                         targetDataLine.drain();
                            //                          targetDataLine.close();
                            //                            InputStream.close();
                            //                  end_audio();
                            // clientSocket2.close();
                            //             new audioServer().runVOIP(42897);
                            //initialise_Audio("127.0.0.1", Integer.parseInt(tfPortC2.getText()));
//					initialise_Audio(InetAddress.getByName(tfIPC2.getText()), Integer.parseInt(AgentPortC2.getText()));
			}
		});
		btnStart_10_Voice.setFont(new Font("Lato", Font.PLAIN, 13));
		btnStart_10_Voice.setBounds(377, 412, 94, 29);
		contentPane.add(btnStart_10_Voice);
                
	/*	JButton btnStop_1_Voice = new JButton("Stop (1)");
		btnStop_1_Voice.setFont(new Font("Lato", Font.PLAIN, 13));
		btnStop_1_Voice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                            new audioServer().runVOIP();
				//account.calling = false;
			}
		});
		btnStop_1_Voice.setBounds(197, 412, 81, 29);
		contentPane.add(btnStop_1_Voice);
		
		JButton btnStop_2_Voice = new JButton("Stop (2)");
		btnStop_2_Voice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                            new audioServer().runVOIP();
				//account.calling = false;
			}
		});
		btnStop_2_Voice.setFont(new Font("Lato", Font.PLAIN, 13));
		btnStop_2_Voice.setBounds(376, 412, 81, 29);
		contentPane.add(btnStop_2_Voice);*/
		
	/*	btnStartall_Voice = new JButton("Playback (1)");
		btnStartall_Voice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 try {
                          //      int newPort1;
                          //      newPort1 = (Integer.parseInt(tfPortC1.getText())) +1;
                         //       int newPort2;
                        //        newPort2 = (Integer.parseInt(tfPortC2.getText())) +1;
                                
                                     playAudio();
                           //          new audioServer().runVOIP(42897);
//					initialise_Audio("127.0.0.1", Integer.parseInt(tfPortC1.getText()));
//					initialise_Audio("127.0.0.1", Integer.parseInt(tfPortC2.getText()));
//					 initialise_Audio(InetAddress.getByName(tfIPC1.getText()), Integer.parseInt(AgentPortC1.getText()));
//					 initialise_Audio(InetAddress.getByName(tfIPC2.getText()), Integer.parseInt(AgentPortC2.getText()));
				} catch (NumberFormatException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnStartall_Voice.setFont(new Font("Lato", Font.PLAIN, 13));
		btnStartall_Voice.setBounds(141, 444, 117, 29);
		contentPane.add(btnStartall_Voice);
                
                		btnStartall_Voice2 = new JButton("Playback (2)");
		btnStartall_Voice2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 try {
                          //      int newPort1;
                          //      newPort1 = (Integer.parseInt(tfPortC1.getText())) +1;
                         //       int newPort2;
                        //        newPort2 = (Integer.parseInt(tfPortC2.getText())) +1;
                                
                                     playAudio2();
                           //          new audioServer().runVOIP(42897);
//					initialise_Audio("127.0.0.1", Integer.parseInt(tfPortC1.getText()));
//					initialise_Audio("127.0.0.1", Integer.parseInt(tfPortC2.getText()));
//					 initialise_Audio(InetAddress.getByName(tfIPC1.getText()), Integer.parseInt(AgentPortC1.getText()));
//					 initialise_Audio(InetAddress.getByName(tfIPC2.getText()), Integer.parseInt(AgentPortC2.getText()));
				} catch (NumberFormatException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnStartall_Voice2.setFont(new Font("Lato", Font.PLAIN, 13));
		btnStartall_Voice2.setBounds(311, 444, 117, 29);
		contentPane.add(btnStartall_Voice2);
		
		btnStopall_Voice = new JButton("Stop (All)");
		btnStopall_Voice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 account.calling = false;
			}
		});
		btnStopall_Voice.setFont(new Font("Lato", Font.PLAIN, 13));
		btnStopall_Voice.setBounds(324, 444, 117, 29);
		contentPane.add(btnStopall_Voice);
*/
	}


	clientServer clientServer1;
        clientServer clientServer2;
	agent agent;
        private JButton btnStartall_Voice;
        private JButton btnStartall_Voice2;
//	private JButton btnStopall_Voice;

	

	private void btnConnectClient1() {
		// receive connection to give to agent server
		clientServer1 = new clientServer(this, agentPort);
		clientServer1.start();
                //play_Audio(Integer.parseInt(String.valueOf(agentPort)));
		JOptionPane.showMessageDialog(null, "Connection with Client is established");
         //       new audioServer().runVOIP(42897);
	}

	private void btnConnectClient2() {
		clientServer2 = new clientServer(this, agentPort);
		clientServer2.start();
               // play_Audio(Integer.parseInt(String.valueOf(agentPort)));
		JOptionPane.showMessageDialog(null, "Connection with Client is established");
          //      new audioServer().runVOIP(42897);
	}

	private void btnSendClient1() throws IOException {
                if(textField.getText().equals("")){
			return;
		}
		agent1 = new agent(textField.getText(), Integer.parseInt(tfPortC1.getText()));
		chatAreaC1.append("Agent: " + textField.getText() + "\n\r");
		chatFile = new BufferedWriter(new FileWriter("agentLog1.txt", true));
		messageLog = textField.getText();
		chatFile.write(time + "\t" + "Agent:" + "\t " + messageLog + "\n");
		System.out.println(messageLog);
		chatFile.close();
		textField.setText("");
		agent1.start();

	}

	private void btnSendClient2() throws IOException {
                if(textField.getText().equals("")){
		return;
		}
		agent2 = new agent(textField.getText(), Integer.parseInt(tfPortC2.getText()));
		chatAreaC1.append("Agent: " + textField.getText() + "\n\r");
		chatFile = new BufferedWriter(new FileWriter("agentLog2.txt", true));
		messageLog = textField.getText();
		chatFile.write(time + "\t" + "Agent:" + "\t " + messageLog + "\n");
		System.out.println(messageLog);
		chatFile.close();
		textField.setText("");
		agent2.start();
                

	}

	private void btnSendAll() throws IOException {
		if (textField.getText().equals("")) {
			return;
		}
		agent1 = new agent(textField.getText(), Integer.parseInt(tfPortC1.getText()));
		agent2 = new agent(textField.getText(), Integer.parseInt(tfPortC2.getText()));
                
                chatAreaC1.append("Agent: " + textField.getText() + "\n\r");
		chatFile = new BufferedWriter(new FileWriter("agentLog1.txt", true));
                
		chatFile = new BufferedWriter(new FileWriter("agentLog2.txt", true));

		messageLog = textField.getText();
		System.out.println(messageLog);
		chatFile.write(time + "\t" + "Agent:" + "\t " + messageLog + "\n");
		chatFile.close();
		textField.setText("");
		agent1.start();
		agent2.start();

	}

	private void btnDisconnect1() {
                
               /*
                
                // Recipient's email ID needs to be mentioned.
                String to = "sarfraznawaz.brohi@taylors.edu.my";

                // Sender's email ID needs to be mentioned
                String from = "sarfraznawaz.brohi@taylors.edu.my";

            mail from localhost
                String host = "localhost";
                
                // Get system properties
                Properties properties = System.getProperties();

                // Setup mail server
                properties.setProperty("mail.smtp.host", host);

                // Get the default Session object.
                Session session = Session.getDefaultInstance(properties);*/
                
		int selectedOption = JOptionPane.showConfirmDialog(null, "Do you want to save file?", "Choose",
				JOptionPane.YES_NO_OPTION);
		if (selectedOption == JOptionPane.YES_OPTION) {
			String date = new Date().toString();
			String dateWithoutTime = date.substring(0, 10);
		
			File file = new File("agentLog1.txt");
		}
                 /*try {
                 // Create a default MimeMessage object.
                MimeMessage message = new MimeMessage(session);

                // Set From: header field of the header.
                message.setFrom(new InternetAddress(from));

                // Set To: header field of the header.
                message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));

                // Set Subject: header field
                message.setSubject("Agent Log 1");

                // Create the message part 
                BodyPart messageBodyPart = new MimeBodyPart();

                // Fill the message
                messageBodyPart.setText("Dear Mr Safraz, as attached.");
         
                // Create a multipar message
                Multipart multipart = new MimeMultipart();

                // Set text message part
                multipart.addBodyPart(messageBodyPart);

                // Part two is attachment
                messageBodyPart = new MimeBodyPart();
                String filename = "agentLog1.txt";
                DataSource source = new FileDataSource(filename);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(filename);
                multipart.addBodyPart(messageBodyPart);

                // Send the complete message parts
                message.setContent(multipart );

                // Send message
                Transport.send(message);
                
             }catch (MessagingException mex) {
                mex.printStackTrace();
        }*/
   
                agent1 = new agent("Agent has ended the conversation! ", Integer.parseInt(tfPortC1.getText()));
                agent1.start();
		tfPortC1.setText("");
		//chatAreaC1.setText("");
                
	}

	private void btnDisconnect2() {

		int selectedOption = JOptionPane.showConfirmDialog(null, "Do you want to save file?", "Choose",
				JOptionPane.YES_NO_OPTION);
		if (selectedOption == JOptionPane.YES_OPTION) {
			String date = new Date().toString();
			String dateWithoutTime = date.substring(0, 10);
			File file = new File("agentLog2.txt");
                        
		
		}
		agent2 = new agent("Agent has ended the conversation! ", Integer.parseInt(tfPortC2.getText()));
                agent2.start();
		tfPortC2.setText("");
		
                
	}
        //start Audio
/*		public void initialise_Audio(String ip, int port){
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
				
				audio play = new audio();
        //				client = new Client(textFieldIP.getText(),Integer.parseInt(tfListenPort.getText()));
				agent = new agent(port);
				
				agent.setAudioInput(audioInput);
				agent.setDataOut(new DatagramSocket());


				//set boolean = true
				account.calling = true;
				agent.audio();
        //				play.start();
				
        //did not add btnStart and stop enabled
				
			}catch(LineUnavailableException | UnknownHostException | SocketException ex){
				ex.printStackTrace();
			}
		}
        //play Audio		
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
        
       private void captureAudio() {
    try {
        adFormat = getAudioFormat();
        DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, adFormat);
        targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
        targetDataLine.open(adFormat);
        targetDataLine.start();

        Thread captureThread = new Thread(new CaptureThread());
        captureThread.start();
    } catch (Exception e) {
        StackTraceElement stackEle[] = e.getStackTrace();
        for (StackTraceElement val : stackEle) {
            System.out.println(val);
        }
        System.exit(0);
    }
}

private void playAudio() {
    try {
        byte audioData[] = byteOutputStream.toByteArray();
        InputStream byteInputStream = new ByteArrayInputStream(audioData);
        AudioFormat adFormat = getAudioFormat();
        InputStream = new AudioInputStream(byteInputStream, adFormat, audioData.length / adFormat.getFrameSize());
        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, adFormat);
        sourceLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
        sourceLine.open(adFormat);
        sourceLine.start();
        Thread playThread = new Thread(new PlayThread());
        playThread.start();
        
    } catch (Exception e) {
        System.out.println(e);
        System.exit(0);
    }
}

private AudioFormat getAudioFormat() {
    float sampleRate = 16000.0F;
    int sampleInbits = 16;
    int channels = 1;
    boolean signed = true;
    boolean bigEndian = false;
    return new AudioFormat(sampleRate, sampleInbits, channels, signed, bigEndian);
}

 class CaptureThread extends Thread {

    byte tempBuffer[] = new byte[10000];

    public void run() {

        byteOutputStream = new ByteArrayOutputStream();
        stopaudioCapture = false;
        try {
            clientSocket = new DatagramSocket(8786);
            InetAddress IPAddress = InetAddress.getByName("127.0.0.1");
            while (!stopaudioCapture) {
                int cnt = targetDataLine.read(tempBuffer, 0, tempBuffer.length);
                if (cnt > 0) {
                    DatagramPacket sendPacket = new DatagramPacket(tempBuffer, tempBuffer.length, IPAddress, 9786);
                    clientSocket.send(sendPacket);
                    byteOutputStream.write(tempBuffer, 0, cnt);
                }
            }
            byteOutputStream.close();
            byteOutputStream.toByteArray();
           // DataOutputStream outFile = new DataOutputStream(new FileOutputStream(byteOutputStream + "temp"));
           // outFile.writeBytes("WAVE");  
            
        } catch (Exception e) {
            System.out.println("CaptureThread::run()" + e);
            System.exit(0);
        }
    }
}

class PlayThread extends Thread {

    byte tempBuffer[] = new byte[10000];

    public void run() {
        try {
            int cnt;
            while ((cnt = InputStream.read(tempBuffer, 0, tempBuffer.length)) != -1) {
                if (cnt > 0) {
                   sourceLine.write(tempBuffer, 0, cnt);
                }
            }
            //                sourceLine.drain();
            //             sourceLine.close();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
    }
}

// Looooooooong Break 

 private void captureAudio2() {
    try {
        adFormat = getAudioFormat();
        DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, adFormat);
        targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
        targetDataLine.open(adFormat);
        targetDataLine.start();

        Thread captureThread2 = new Thread(new CaptureThread2());
        captureThread2.start();
    } catch (Exception e) {
        StackTraceElement stackEle[] = e.getStackTrace();
        for (StackTraceElement val : stackEle) {
            System.out.println(val);
        }
        System.exit(0);
    }
}

private void playAudio2() {
    try {
        byte audioData[] = byteOutputStream.toByteArray();
        InputStream byteInputStream = new ByteArrayInputStream(audioData);
        AudioFormat adFormat = getAudioFormat();
        InputStream = new AudioInputStream(byteInputStream, adFormat, audioData.length / adFormat.getFrameSize());
        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, adFormat);
        sourceLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
        sourceLine.open(adFormat);
        sourceLine.start();
        Thread playThread2 = new Thread(new PlayThread2());
        playThread2.start();
        
    } catch (Exception e) {
        System.out.println(e);
        System.exit(0);
    }
}


class CaptureThread2 extends Thread {

    byte tempBuffer[] = new byte[10000];

    public void run() {

        byteOutputStream = new ByteArrayOutputStream();
        stopaudioCapture = false;
        try {
            clientSocket2 = new DatagramSocket(8787);
            InetAddress IPAddress = InetAddress.getByName("127.0.0.1");
            while (!stopaudioCapture) {
                int cnt = targetDataLine.read(tempBuffer, 0, tempBuffer.length);
                if (cnt > 0) {
                    DatagramPacket sendPacket = new DatagramPacket(tempBuffer, tempBuffer.length, IPAddress, 9787);
                    clientSocket2.send(sendPacket);
                    byteOutputStream.write(tempBuffer, 0, cnt);
                }
            }
            byteOutputStream.close();
            byteOutputStream.toByteArray();
           // DataOutputStream outFile = new DataOutputStream(new FileOutputStream(byteOutputStream + "temp"));
           // outFile.writeBytes("WAVE");  
            
        } catch (Exception e) {
            System.out.println("CaptureThread::run()" + e);
            System.exit(0);
        }
    }
}

class PlayThread2 extends Thread {

    byte tempBuffer[] = new byte[10000];

    public void run() {
        try {
            int cnt;
            while ((cnt = InputStream.read(tempBuffer, 0, tempBuffer.length)) != -1) {
                if (cnt > 0) {
                   sourceLine.write(tempBuffer, 0, cnt);
                }
            }
            //                sourceLine.drain();
            //             sourceLine.close();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
    }
}
         public void end_audio(){
        try {
            sourceLine.drain();
            sourceLine.close();
            InputStream.close();
            targetDataLine.drain();
            targetDataLine.close();
            InputStream.close();

        } catch (IOException e) {

        }
   }
	@Override
	public void writeGUI1(String s) {
		chatAreaC1.append(s + "\n\r");
	}

	public void writeGUI2(String s) {
		chatAreaC2.append(s + "\n\r");
	}
}