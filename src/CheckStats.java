package com.first;

import org.pcap4j.core.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

import com.formdev.flatlaf.FlatLightLaf;
import org.pcap4j.packet.Packet;

public class CheckStats extends javax.swing.JFrame {

    private PcapHandle ha = null;
    private List<Packet> p1 = new ArrayList<>();
    private int i;

    public CheckStats(List<Packet> p, int index, PcapHandle handle) {
        // Set FlatLaf look and feel
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }
        p1 = p;
        i = index;
        ha = handle;
        initComponents();
        populateStatistics();
    }

    private void populateStatistics() {
        try {
            PcapStat stat = ha.getStats();
            DefaultListModel<String> model = new DefaultListModel<>();
            model.addElement("Packets Received: " + stat.getNumPacketsReceived());
            model.addElement("Packets Dropped: " + stat.getNumPacketsDropped());
            model.addElement("Packets Dropped by Interface: " + stat.getNumPacketsDroppedByIf());
            jList1.setModel(model);
        } catch (PcapNativeException | NotOpenException e) {
            JOptionPane.showMessageDialog(this, "Error fetching statistics: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initComponents() {
        setTitle("Packet Statistics");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 240)); // Light background

        JLabel headerLabel = new JLabel("STATISTICS", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(new Color(50, 50, 50));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.add(headerLabel, BorderLayout.CENTER);

        // Statistics List
        jList1 = new JList<>();
        JScrollPane scrollPane = new JScrollPane(jList1);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Packet Statistics"));

        // Footer Panel
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton backButton = new JButton("Back");
        backButton.setToolTipText("Return to Packet Details");
        backButton.addActionListener(evt -> {
            new PacketDetails(p1, i, ha).setVisible(true);
            this.setVisible(false);
        });

        footerPanel.add(backButton);

        // Add Panels to Frame
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    private JList<String> jList1;

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            // For testing, instantiate with empty packet list
            new CheckStats(new ArrayList<>(), 0, null).setVisible(true);
        });
    }
}