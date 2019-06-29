import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class RejestrMonitorow implements IRejestrMonitorow {

    public RejestrMonitorow() {
    }

    int licznik = 0;
    private ArrayList<IMonitor> listaMonitorow = new ArrayList<>();

    @Override
    public int register(IMonitor o) throws RemoteException {
        this.listaMonitorow.add(o);
        return licznik++;
    }

    @Override
    public boolean unregister(int id) throws RemoteException {
        this.listaMonitorow.remove(id);
        return false;
    }

    @Override
    public ArrayList<IMonitor> getMonitors() throws RemoteException {
        return listaMonitorow;
    }

    public static void main(String args[]) throws RemoteException, AlreadyBoundException {

            Registry registry = LocateRegistry.createRegistry(2000);
            RejestrMonitorow obj = new RejestrMonitorow();
            IRejestrMonitorow namiastkaRejestru = (IRejestrMonitorow) UnicastRemoteObject.exportObject(obj,2001);
            registry.bind("namiastkaRejestru", namiastkaRejestru);
    }

}
