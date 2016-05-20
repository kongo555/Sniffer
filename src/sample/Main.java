package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jnetpcap.PcapIf;
import sample.sniffer.Device;
import sample.sniffer.Packet;
import sample.sniffer.Sniffer;
import sample.view.DeviceMenuController;
import sample.view.SnifferMenuController;

import java.io.IOException;
import java.util.List;

public class Main extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    private Sniffer sniffer;

    private ObservableList<Device> deviceList = FXCollections.observableArrayList();
    private ObservableList<Packet> packetList = FXCollections.observableArrayList();

    public Main() {
        sniffer = new Sniffer(this);
        sniffer.captureDevicesList();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("MySniffer");

        initRootLayout();
        showDeviceMenu();
    }

    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showDeviceMenu() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/DeviceMenu.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();

            //
            rootLayout.setCenter(personOverview);

            // Give the controller access to the main app.
            DeviceMenuController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showSnifferMenu(Device device) {
        //sniffer.capturePackets(device);
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/SnifferMenu.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(personOverview);

            // Give the controller access to the main app.
            SnifferMenuController controller = loader.getController();
            controller.setMainApp(this, sniffer, device);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Device> getDeviceList(){
        return deviceList;
    }

    public void setDeviceList(List<PcapIf> allDevs){
        for (PcapIf dev: allDevs) {
            deviceList.add(new Device(dev.getName(), dev));
        }
    }

    public ObservableList<Packet> getPacketList() {
        return packetList;
    }

    public void setPacketList(ObservableList<Packet> packetList) {
        this.packetList = packetList;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
