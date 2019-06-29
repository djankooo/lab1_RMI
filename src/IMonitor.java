import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IMonitor extends Remote {
    public void setReadings(int readings) throws RemoteException;
    public void setInput(ISensor o) throws RemoteException;
    public int getId() throws RemoteException;
    public void setId(int id) throws RemoteException;
}