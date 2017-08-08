import java.io.IOException;
import java.net.Socket;

public class agent extends Thread{
	String message; //what to send 
	String host = "localhost"; // where to sent it, ipAddress
	int port; //what port to send message to
	
	public agent(String message, int port){
		this.message = message;
		this.port = port;
	}
	
	public void run(){
		try{
			//connects to the ServerSocket
			Socket socket = new Socket(host,port);
			
			//sends message to client as bytes
			socket.getOutputStream().write(message.getBytes());
			socket.close();
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}

}
