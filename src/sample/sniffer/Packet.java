package sample.sniffer;

import javafx.beans.property.*;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Tcp;

/**
 * Created by root on 14.01.16.
 */
public class Packet {
    private static Tcp tcp = new Tcp();
    private Ip4 ip = new Ip4();
    private static Ethernet ethernet = new Ethernet();

    private final PcapPacket pcapPacket;

    private final LongProperty no;
    private final IntegerProperty size;
    private final LongProperty time;
    private final StringProperty source;
    private final StringProperty destination;
    private final String details;

    public Packet(PcapPacket pcapPacket) {
        this.pcapPacket = pcapPacket;

        this.no = new SimpleLongProperty(pcapPacket.getFrameNumber()-1);
        this.time = new SimpleLongProperty(pcapPacket.getCaptureHeader().timestampInMillis());
        if(pcapPacket.hasHeader(ethernet)){
            this.source = new SimpleStringProperty(FormatUtils.mac(ethernet.source()));
            this.destination = new SimpleStringProperty(FormatUtils.mac(ethernet.destination()));
        }
        /*else if (pcapPacket.hasHeader(ip)) {
            this.source = new SimpleStringProperty(FormatUtils.ip(ip.source()));
            this.destination = new SimpleStringProperty(FormatUtils.ip(ip.destination()));
        }*/
        else if (pcapPacket.hasHeader(tcp)) {
            this.source = new SimpleStringProperty(new Integer(tcp.source()).toString());
            this.destination = new SimpleStringProperty(new Integer(tcp.destination()).toString());
            //FormatUtils.ip()
        }
        else{
            this.source = new SimpleStringProperty("");
            this.destination = new SimpleStringProperty("");
        }
        this.size = new SimpleIntegerProperty(pcapPacket.size());
        this.details = pcapPacket.toString();
    }

    public LongProperty noProperty() {
        return no;
    }

    public LongProperty timeProperty() {
        return time;
    }

    public StringProperty sourceProperty() {
        return source;
    }

    public StringProperty destinationProperty() {
        return destination;
    }

    public IntegerProperty sizeProperty() {
        return size;
    }

    public String getDetails() {
        return details;
    }
}
