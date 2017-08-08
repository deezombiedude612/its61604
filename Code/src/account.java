import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


public class account {
	private String username;
	private String password;

	
	public account (){
	}
	
	public void setUsername(String username){
		this.username = username;
	}
	public void setPassword(String password){
		this.password = password;
	}
	public String getUsername(){
		return username;
	}
	public String getPassword(){
		return password;
	}
	
	public static int readFromAgent(String username, String password){
        
        String strUsernameA = "agent1";
        String strPasswordA = "123456";
     
        String strUsernameA1 = "agent2";
        String strPasswordA1 = "abcde";
     
        String strUsernameA2 = "agent3";
        String strPasswordA2 = "Abc123!";
     
        String strUsernameA3 = "agent4";
        String strPasswordA3 = "54FR4Z";
        
 
        
		int flag = 1;
                    
			if(username.equals(strUsernameA)){
                            if(password.equals(strPasswordA)){
					flag = 0;
				}
			}
                        else if(username.equals(strUsernameA1)){
				if(password.equals(strPasswordA1)){
					flag = 0;
				}
			}
                        else if(username.equals(strUsernameA2)){
				if(password.equals(strPasswordA2)){
					flag = 0;
				}
			}
                        else if(username.equals(strUsernameA3)){
				if(password.equals(strPasswordA3)){
					flag = 0;
				}
			}
			
		return flag;
	}
	public static int readFromClient(String username, String password) throws IOException{
            	
                String strUsername = "DrQueue";
                String strPassword = "123456";
     
                String strUsername1 = "DonaldT";
                String strPassword1 = "abcde";
     
                String strUsername2 = "SeanLim";
                String strPassword2 = "Abc123!";
     
                String strUsername3 = "Gerrard";
                String strPassword3 = "Safraz";
                
		int flag = 1;

			if(username.equals(strUsername)){
				if(password.equals(strPassword)){
					flag = 0;
				}
			}
                        else if(username.equals(strUsername1)){
				if(password.equals(strPassword1)){
					flag = 0;
				}
			}
                        else if(username.equals(strUsername2)){
				if(password.equals(strPassword2)){
					flag = 0;
				}
			}
                        else if(username.equals(strUsername3)){
				if(password.equals(strPassword3)){
					flag = 0;
				}
			}
		return flag;
	}

}
