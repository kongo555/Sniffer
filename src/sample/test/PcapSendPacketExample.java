package sample.test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.JMemoryPacket;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.protocol.JProtocol;
import org.jnetpcap.protocol.lan.Ethernet;

public class PcapSendPacketExample {
    public static void main(String[] args) {
        List<PcapIf> alldevs = new ArrayList<PcapIf>(); // Will be filled with NICs
        StringBuilder errbuf = new StringBuilder(); // For any error msgs

        int r = Pcap.findAllDevs(alldevs, errbuf);
        if (r == Pcap.NOT_OK || alldevs.isEmpty()) {
            System.err.printf("Can't read list of devices, error is %s", errbuf.toString());
            return;
        }
        PcapIf device = alldevs.get(0); // We know we have atleast 1 device

        int snaplen = 64 * 1024; // Capture all packets, no trucation
        int flags = Pcap.MODE_PROMISCUOUS; // capture all packets
        int timeout = 10 * 1000; // 10 seconds in millis
        Pcap pcap = Pcap.openLive(device.getName(), snaplen, flags, timeout, errbuf);

        //
        //ByteBuffer b = createMagicPacket();

        //if (pcap.sendPacket(b) != Pcap.OK) {
        //    System.err.println(pcap.getErr());
        //}

        pcap.close();
    }

    public static byte[] createMagicPacket(byte[] mac){
        byte[] a = new byte[14];
        Arrays.fill(a, (byte) 0xff);

        int n =0;

        byte[] payload = new byte[102];
        for (int i = 0; i < 6; i++) {
            payload[i] = (byte)1;
            n++;
        }
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < mac.length; j++) {
                payload[n] = mac[j];
                n++;
            }
        }
        return a;
    }
}