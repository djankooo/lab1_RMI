import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class Monitor extends Application implements IMonitor, EventHandler<ActionEvent> {

    private int idMonitor;

    private ISensor sensor;
    private int idSensor ;
    private int readings;

    private GridPane pane = new GridPane();

    private HBox h1 = new HBox();
    private HBox h2 = new HBox();
    private HBox h3 = new HBox();
    private HBox h4 = new HBox();

    private Text idMonitorText = new Text("Id monitora : ");
    private Text idSensorText = new Text("Id sensora : ");
    private Text valueSensorText = new Text("Wartosc sensora : ");

    private Label idMonitorLabel;
    private Label idSensorLabel;
    private Label valueSensorLabel;

    private Button startButton = new Button("Start");
    private Button stopButton = new Button("Stop");

    public Monitor(int id) throws RemoteException{
        this.idMonitor = id;
        this.sensor = null;
    }

    public Monitor() throws RemoteException{
        this.sensor = null;
    }

    @Override
    public void setReadings(int readings) throws RemoteException {
        this.readings = readings;
    }

    @Override
    public void setInput(ISensor o) throws RemoteException {
        this.sensor = o;
        this.idSensor = o.getId();

        System.out.println("Monitor " + this.getId() + " connected with " + this.sensor.getId() + " sensor.");

        o.start();
    }

    @Override
    public int getId() throws RemoteException {
        return this.idMonitor;
    }

    @Override
    public void setId(int id) throws RemoteException {
        this.idMonitor = id;

    }

    public static void main(String args[]) throws RemoteException, IOException, AlreadyBoundException, NotBoundException {

        Registry registry = LocateRegistry.getRegistry("localhost",2000);
        IRejestrMonitorow rejMon = (IRejestrMonitorow) registry.lookup("namiastkaRejestru");

        Monitor obj = new Monitor(rejMon.getMonitors().size());
        System.out.println("Dodano monitor nr : " + obj.getId());

        IMonitor namiastkaMonitora = (IMonitor) UnicastRemoteObject.exportObject(obj,0);
        registry.bind(Integer.toString(rejMon.getMonitors().size()), namiastkaMonitora);

        rejMon.register(namiastkaMonitora);

        launch(args);

    }

    public void start(Stage primaryStage) throws RemoteException {

        primaryStage.setTitle("Manager");

        pane.setPadding(new Insets(25, 25, 25, 25));

        pane.setHgap(10);
        pane.setVgap(10);

        startButton.setOnAction(event -> {
            try {
                this.sensor.start();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });

        idMonitorLabel = new Label(Integer.toString(this.idMonitor));
        idSensorLabel = new Label();
        valueSensorLabel = new Label(Integer.toString(this.readings));


        h1.getChildren().addAll(idMonitorText, idMonitorLabel);
        h2.getChildren().addAll(idSensorText,idSensorLabel);
        h3.getChildren().addAll(valueSensorText, valueSensorLabel) ;
        h4.getChildren().addAll(startButton,stopButton);

        pane.add(h1, 0, 0);
        pane.add(h2, 0, 1);
        pane.add(h3, 0, 2);
        pane.add(h4, 0, 4);

        primaryStage.setScene(new Scene(pane));
        primaryStage.show();

    }

    @Override
    public void handle(ActionEvent event) {

    }
}
