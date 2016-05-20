package sample.sniffer;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.jnetpcap.PcapIf;

/**
 * Created by root on 14.01.16.
 */
public class Device {
    private final StringProperty name;
    private final PcapIf dev;

    public Device(String name, PcapIf dev) {
        this.name = new SimpleStringProperty(name);
        this.dev = dev;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public PcapIf getDev() {
        return dev;
    }
}
