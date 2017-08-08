import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AUInterface extends Remote{
	
	public String newAU() throws RemoteException;
}
