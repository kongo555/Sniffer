package sample.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import sample.Main;
import sample.sniffer.Device;

public class DeviceMenuController {
    private Main mainApp;

    @FXML
    private TableView<Device> devicesTable;
    @FXML
    private TableColumn<Device, String> nameColumn;

    @FXML
    private void initialize() {
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
    }

    @FXML
    private void handleStart() {
        Device selectedDevice = devicesTable.getSelectionModel().getSelectedItem();
        if (selectedDevice != null) {
            mainApp.showSnifferMenu(selectedDevice);
        } else {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Device Selected");
            alert.setContentText("Please select a device in the table.");

            alert.showAndWait();
        }
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        devicesTable.setItems(mainApp.getDeviceList());
    }
}
