import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ISensor extends Remote {



    public void start() throws RemoteException;
    public void stop() throws RemoteException;
    public int getId()throws RemoteException;
    public void setOutput(IMonitor o) throws RemoteException;
}