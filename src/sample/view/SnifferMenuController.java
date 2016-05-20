package sample.view;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import sample.Main;
import sample.sniffer.Device;
import sample.sniffer.Packet;
import sample.sniffer.Sniffer;

/**
 * Created by root on 14.01.16.
 */
public class SnifferMenuController {
    private Main mainApp;
    private Sniffer sniffer;
    private Device device;
    private Thread thread;
    private boolean threadRunning;

    @FXML
    private TableView<Packet> packetTable;
    @FXML
    private TableColumn<Packet, Number> noColumn;
    @FXML
    private TableColumn<Packet, Number> timeColumn;
    @FXML
    private TableColumn<Packet, String> sourceColumn;
    @FXML
    private TableColumn<Packet, String> destinationColumn;
    @FXML
    private TableColumn<Packet, Number> sizeColumn;
    @FXML
    private TextArea detailsArea;

    @FXML
    private void initialize() {
        noColumn.setCellValueFactory(cellData -> cellData.getValue().noProperty());
        timeColumn.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        sourceColumn.setCellValueFactory(cellData -> cellData.getValue().sourceProperty());
        destinationColumn.setCellValueFactory(cellData -> cellData.getValue().destinationProperty());
        sizeColumn.setCellValueFactory(cellData -> cellData.getValue().sizeProperty());

        showPacketDetails(null);

        // Listen for selection changes and show the person details when changed.
        packetTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPacketDetails(newValue));
    }

    private void showPacketDetails(Packet packet) {
        if (packet != null) {
            detailsArea.setText(packet.getDetails());
        } else {
            detailsArea.setText("");
        }
    }

    @FXML
    private void handleStart() {
        if (!threadRunning) {
            packetTableUpdate();
            detailsArea.setText("Running!");
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Start");
            alert.setHeaderText("Sniffer already running");

            alert.showAndWait();
        }
    }

    @FXML
    private void handleStop() {
        if (threadRunning) {
            thread = null;
            threadRunning = false;
            detailsArea.setText("Stop!");
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Stop");
            alert.setHeaderText("Sniffer not running");
            //alert.setContentText("");

            alert.showAndWait();
        }
    }

    @FXML
    private void handleClear() {
        mainApp.getPacketList().clear();
        detailsArea.setText("Clear!");
    }


    public void packetTableUpdate(){
        threadRunning = true;
        SnifferMenuController controller = this;
        Task<Void> task = new Task<Void>() {
            @Override protected Void call() throws Exception {
                Thread thisThread = Thread.currentThread();
                while (thread == thisThread) {
                    sniffer.capturePackets(controller, device);
                }
                return null;
            }
        };
        thread = new Thread(task);
        thread.start();
    }


    public void setMainApp(Main mainApp, Sniffer sniffer, Device device) {
        this.mainApp = mainApp;
        this.sniffer = sniffer;
        this.device = device;
        // Add observable list data to the table
        packetTable.setItems(mainApp.getPacketList());
    }

    public boolean isThreadRunning() {
        return threadRunning;
    }

    public TableView<Packet> getPacketTable() {
        return packetTable;
    }
}
