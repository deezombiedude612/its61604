import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.io.*;
import javax.swing.JOptionPane;

public class AU extends UnicastRemoteObject implements AUInterface{
    public AU() throws RemoteException{
        
    }

    
    public String newAU() throws RemoteException{
    
       return "\nVersion 2.8.1\nThis feature is brought to you by RMI.\nWei Liang, Andrew, Henry (WAH). \nAll Rights Reserved. 2017.";
    }
    
}
