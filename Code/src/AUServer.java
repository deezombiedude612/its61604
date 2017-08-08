import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;


public class AUServer{
        public static void main (String[] args) throws Exception{
            
            Registry registry = LocateRegistry.createRegistry(1099);
            AUInterface showAU = new AU();
           // System.setProperty("java.rmi.server.hostname","127.0.0.1");
            registry.rebind("ToShowAU", showAU);
            System.out.printf("RMI server is successfully started!");
    }
}