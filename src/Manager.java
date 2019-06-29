import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Manager extends Application implements EventHandler<ActionEvent>{

    private int id;

    static ArrayList<IMonitor> monitors = new ArrayList<>();
    static ArrayList<ISensor> sensors = new ArrayList<>();

    Registry reg;
    IRejestrMonitorow rejMon;


    public Manager() throws RemoteException, NotBoundException {
        this.id = 0;
        reg = LocateRegistry.getRegistry(2000);
        rejMon = (IRejestrMonitorow) reg.lookup("namiastkaRejestru");
    }

    public ISensor createSensor() throws RemoteException {
        ISensor s = new Sensor(id++);
        System.out.println("Dodano sensor nr : " + s.getId());
        return s;
    }

    public void getMonitors() throws RemoteException {
        monitors = rejMon.getMonitors();
        monitorsObservableTable.addAll(monitors);
    }

    GridPane pane = new GridPane();

    Label monitorsTabLabel = new Label("Monitory");
    Label sensorsTabLabel = new Label("Sensory");

    // Komponenty tabeli klienta
    private TableView monitorsTableView = new TableView();
    private ObservableList<IMonitor> monitorsObservableTable = FXCollections.observableArrayList(monitors);

    private TableView sesnorsTableView = new TableView();
    private ObservableList<ISensor> sesnorsObservableTable = FXCollections.observableArrayList(sensors);

    private TableColumn<Sensor, Integer> idSensorsTCol = new TableColumn<>("ID");
    private TableColumn<Sensor, Integer> idSensorsValueTCol = new TableColumn<>("Value");

    private TableColumn<Sensor, String> idMonitorsTCol = new TableColumn<>("ID");

    // Przyciski obsługujące operacje na elementach tabeli grup
    private Button addMonitorButton = new Button("Add monitor");
    private Button bindButton = new Button("Connect");
    private Button addSensorButton = new Button("Add sensor");

    private TextField monitorTF = new TextField("monitor");
    private TextField sensorTF = new TextField("sensor");

    public static void main(String args[]) throws RemoteException, IOException, NotBoundException {
        launch(args);
    }

    public void start(Stage primaryStage) throws RemoteException, NotBoundException {

        primaryStage.setTitle("Manager");

        pane.setPadding(new Insets(25, 25, 25, 25));

        pane.setHgap(10);
        pane.setVgap(10);

        pane.add(monitorsTabLabel, 0, 0);
        pane.add(sensorsTabLabel, 1, 0);

        pane.add(monitorsTableView, 0, 1);
        pane.add(sesnorsTableView,1,1);


        idSensorsTCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idMonitorsTCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        sesnorsTableView.setItems(sesnorsObservableTable);
        sesnorsTableView.getColumns().add(idSensorsTCol);

        monitorsTableView.setItems(monitorsObservableTable);
        monitorsTableView.getColumns().add(idMonitorsTCol);

        pane.add(addMonitorButton, 0, 2);
        pane.add(addSensorButton, 1, 2);
        pane.add(monitorTF, 0,3);
        pane.add(sensorTF, 1,3);
        pane.add(bindButton, 2,3);

        addSensorButton.setOnAction(event -> {
            try {
                sesnorsObservableTable.clear();
                sensors.add(createSensor());
                sesnorsObservableTable.addAll(sensors);
                for(ISensor s : sensors){
                    System.out.println(s.getId());
                }

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });

        addMonitorButton.setOnAction(event -> {
            monitorsObservableTable.clear();
            try {
                getMonitors();
                for(IMonitor s : monitors){
                    System.out.println(s.getId());
                }

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });

        bindButton.setOnAction(event -> {
            try {
                int wybranySensorIndex   = Integer.parseInt(sensorTF.getText());
                int wybranyMonitorIndex  = Integer.parseInt(monitorTF.getText());

                System.out.println("wybranySensor txt : " + wybranySensorIndex + " - wybranyMonitor txt : " + wybranyMonitorIndex);

                ISensor sensnam = (ISensor) UnicastRemoteObject.exportObject(this.sensors.get(wybranySensorIndex), 0);//namiastka sensora
                this.monitors.get(wybranyMonitorIndex).setInput(sensnam);
                sensnam.setOutput(this.monitors.get(wybranyMonitorIndex));

                System.out.println("wybranySensor id : " + sensnam.getId() + " - wybranyMonitor id : " + this.monitors.get(wybranyMonitorIndex).getId());
                System.out.println("this.monitors.get(wybranyMonitorIndex).se");

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });

        primaryStage.setScene(new Scene(pane));
        primaryStage.show();

    }

    @Override
    public void handle(ActionEvent event) {

    }
}
