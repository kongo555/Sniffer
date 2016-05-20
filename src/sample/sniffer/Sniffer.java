package sample.sniffer;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import org.jnetpcap.JBufferHandler;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapHeader;
import org.jnetpcap.PcapIf;
import org.jnetpcap.nio.JBuffer;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import sample.Main;
import sample.view.SnifferMenuController;

import java.util.List;

/**
 * Created by root on 14.01.16.
 */
public class Sniffer {
    private StringBuilder errbuf;
    private List<PcapIf> alldevs;
    private Main mainApp;

    public Sniffer(Main mainApp){
        errbuf = new StringBuilder();
        alldevs = FXCollections.observableArrayList();
        this.mainApp = mainApp;
    }

    public void captureDevicesList(){
        int r = Pcap.findAllDevs(alldevs, errbuf);
        if (r == Pcap.NOT_OK || alldevs.isEmpty()) {
            System.err.printf("Can't read list of devices, error is %s", errbuf
                    .toString());
            return;
        }
        mainApp.setDeviceList(alldevs);
    }

    public void capturePackets(SnifferMenuController controller, Device myDevice){
        PcapIf device = myDevice.getDev();

        int snaplen = 64 * 1024;           // Capture all packets, no trucation
        int flags = Pcap.MODE_PROMISCUOUS; // capture all packets
        int timeout = 10 * 1000;           // 10 seconds in millis
        Pcap pcap =
                Pcap.openLive(device.getName(), snaplen, flags, timeout, errbuf);

        if (pcap == null) {
            System.err.printf("Error while opening device for capture: "
                    + errbuf.toString());
            return;
        }

        ObservableList<Packet> packetList = mainApp.getPacketList();
        PcapPacketHandler<String> jpacketHandler = new PcapPacketHandler<String>() {
            public void nextPacket(PcapPacket packet, String user) {
                //System.out.println(packet.toString());
                packetList.add(new Packet(packet));
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        controller.getPacketTable().refresh();
                    }
                });
                if (!controller.isThreadRunning()) {
                    pcap.breakloop();
                    System.out.println("breakloop");
                }
            }
        };
        pcap.loop(pcap.LOOP_INFINATE, jpacketHandler, "");
        pcap.close();
    }
}
