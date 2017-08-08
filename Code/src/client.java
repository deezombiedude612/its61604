import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class client extends Thread{

	private String message;
	private int port;
	private String ip = "localhost";
	

	public client(String message, int port){
		this.message = message;
		this.port = port;
	}
	
	//connect to server
	public void run(){
		try{
			Socket socket = new Socket(ip, port);
                       
			//sends message
			socket.getOutputStream().write(message.getBytes());
			socket.close();
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}

}
