import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

public class agentServer extends Thread{


	private ServerSocket agentSocket;
	private int port ;
	private writeGUI write;
	
	public agentServer(writeGUI write, int port){
		this.write = write;
		this.port = port;
		try{
			agentSocket = new ServerSocket(port);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void run(){
		Socket agent;
		try{
			while((agent = agentSocket.accept()) != null){
//				InputStream input = agent.getInputStream();
				BufferedReader read = new BufferedReader(new InputStreamReader(agent.getInputStream()));
				
				String message = read.readLine();
				if(read!= null){
					write.writeGUI1("Agent: " + message);
				}
			}
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public agentServer(){
		try{
			agentSocket = new ServerSocket(port);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
}
