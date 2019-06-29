import java.rmi.*;
import java.util.ArrayList;

public interface IRejestrMonitorow extends Remote {
    public int register(IMonitor o) throws RemoteException;
    public boolean unregister(int id) throws RemoteException;
    public ArrayList<IMonitor> getMonitors() throws RemoteException;
}
