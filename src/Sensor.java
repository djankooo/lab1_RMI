import java.rmi.RemoteException;

public class Sensor implements ISensor, Runnable{

    Thread thread;
    private IMonitor monitor;
    private boolean running;
    private int x = (int) Math.random() * 100;


    private final int id;

    public Sensor(int id) throws RemoteException{
        this.id = id;
        System.out.println("konstruktor sensora id = " + this.id);
        this.monitor = null;
        this.running = false;
    }

    @Override
    public void start() throws RemoteException {
        running = true;
        thread  = new Thread(){
            public void run(){
                while(running){
                    try {
                        System.out.println("Wiadomosc z sensora nr : " + this.getId());
                        monitor.setReadings(x++);
                    } catch (RemoteException e1) {
                        e1.printStackTrace();
                    }
                    try {
                        this.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }
    @Override
    public void stop() throws RemoteException {
        running = false;
    }

    @Override
    public int getId() throws RemoteException{
        return this.id;
    }

    @Override
    public void setOutput(IMonitor o) throws RemoteException {
        this.monitor = o;

        System.out.println("Połączono sensr "+this.id + " z monitorem: " + this.monitor.getId());
    }
    @Override
    public void run() {
        running = true;
    }
}
