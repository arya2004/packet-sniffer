package com.first;

import org.pcap4j.core.*;
import org.pcap4j.core.PcapNetworkInterface.*;
import java.util.*;
import javax.swing.*;
import org.pcap4j.packet.*;
import java.awt.*;
import java.util.List;

import com.formdev.flatlaf.FlatLightLaf;

public class ViewPackets extends javax.swing.JFrame {

    PcapNetworkInterface device;
    List<Packet> p = new ArrayList<>();
    int index;
    PcapHandle handle = null;
    private JLabel jLabel7;
    private JLabel jLabel6;

    public ViewPackets(List<PcapNetworkInterface> d, int i) {
        // Set FlatLaf look and feel
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }
        initComponents();
        jLabel7.setText(d.get(i).getDescription());
        index = i;
        device = d.get(i);
    }

    private void initComponents() {
        setTitle("Packet Viewer");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 240)); // Light background color for header

        JLabel headerLabel = new JLabel("PACKET VIEWER", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(new Color(50, 50, 50)); // Dark text for contrast
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.add(headerLabel, BorderLayout.CENTER);

        jLabel7 = new JLabel("", SwingConstants.LEFT);
        jLabel7.setFont(new Font("Arial", Font.PLAIN, 14));
        jLabel7.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 10));
        headerPanel.add(jLabel7, BorderLayout.SOUTH);

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> jList1 = new JList<>(listModel);
        jList1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jList1.setToolTipText("Select a packet to view details");
        jList1.addListSelectionListener(evt -> {
            index = jList1.getSelectedIndex();
            if (index >= 0) {
                jLabel6.setText("Packet " + index + ": " + p.get(index).getHeader());
            }
        });

        JScrollPane listScrollPane = new JScrollPane(jList1);
        listScrollPane.setBorder(BorderFactory.createTitledBorder("Captured Packets"));
        mainPanel.add(listScrollPane, BorderLayout.CENTER);

        // Footer Panel
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel footerInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel packetLabel = new JLabel("Selected Packet: ");
        jLabel6 = new JLabel("");
        jLabel6.setFont(new Font("Arial", Font.PLAIN, 14));
        footerInfoPanel.add(packetLabel);
        footerInfoPanel.add(jLabel6);

        footerPanel.add(footerInfoPanel, BorderLayout.NORTH);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton startCaptureButton = new JButton("Start Capturing!");
        startCaptureButton.setToolTipText("Start capturing packets on the selected interface");
        startCaptureButton.addActionListener(evt -> {
            try {
                int snaplen = 65536; // Maximum length of bytes to receive from a packet
                int timeout = 150;  // Timeout in millis
                handle = device.openLive(snaplen, PromiscuousMode.PROMISCUOUS, timeout);
            } catch (PcapNativeException e) {
                e.printStackTrace();
            }

            PacketListener listener = packet -> {
                listModel.addElement(packet.toString());
                p.add(packet);
            };

            try {
                int max = 100;
                handle.loop(max, listener);
            } catch (InterruptedException | PcapNativeException | NotOpenException e) {
                e.printStackTrace();
            }
        });

        JButton detailedViewButton = new JButton("Detailed View");
        detailedViewButton.setToolTipText("View detailed information of the selected packet");
        detailedViewButton.addActionListener(evt -> {
            if (index >= 0) {
                new PacketDetails(p, index, handle).setVisible(true);
                this.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a packet.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton exitButton = new JButton("Exit");
        exitButton.setToolTipText("Exit the application");
        exitButton.addActionListener(evt -> System.exit(0));

        buttonPanel.add(startCaptureButton);
        buttonPanel.add(detailedViewButton);
        buttonPanel.add(exitButton);

        footerPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add panels to frame
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }
}
