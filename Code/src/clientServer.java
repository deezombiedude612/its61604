import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class clientServer extends Thread{
	ServerSocket serverSocket;
	int port;
	writeGUI write; 
        
	
	//no WritetoGUI
	public clientServer(writeGUI write, int port){
		this.write = write;
		this.port = port;
		
		try{
			serverSocket = new ServerSocket(port);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void run(){

		Socket client; //connect to the port

		try{
			//establish and keep on accepting connection until terminate
			while((client = serverSocket.accept()) != null){

					//get String from socket
					BufferedReader read = new BufferedReader(new InputStreamReader(client.getInputStream()));
					
					String message = read.readLine();
							write.writeGUI1("" +message);
				}
		
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
