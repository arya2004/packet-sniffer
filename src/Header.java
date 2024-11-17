package com.first;

import org.pcap4j.core.*;
import org.pcap4j.packet.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

import com.formdev.flatlaf.FlatLightLaf;

public class Header extends javax.swing.JFrame {

    private PcapHandle ha = null;
    private List<Packet> p1 = new ArrayList<>();
    private int i;

    public Header(List<Packet> p, int index, PcapHandle handle) {
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
        jLabel4.setText(String.valueOf(p.get(index).getHeader()));
    }

    private void initComponents() {
        setTitle("Packet Header");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(500, 300);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 240)); // Light gray background

        JLabel headerLabel = new JLabel("PACKET HEADER", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(new Color(50, 50, 50));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.add(headerLabel, BorderLayout.NORTH);

        // Packet Header Details
        jLabel4 = new JLabel("", SwingConstants.CENTER); // Display header details dynamically
        jLabel4.setFont(new Font("Arial", Font.PLAIN, 14));
        jLabel4.setBorder(BorderFactory.createTitledBorder("Header Details"));
        headerPanel.add(jLabel4, BorderLayout.CENTER);

        // Footer Panel with Back Button
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton backButton = new JButton("Back");
        backButton.setToolTipText("Return to Packet Details");
        backButton.addActionListener(evt -> {
            new PacketDetails(p1, i, ha).setVisible(true);
            this.setVisible(false);
        });

        footerPanel.add(backButton);

        // Add Panels to Frame
        add(headerPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    private JLabel jLabel4;

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            // For testing, instantiate with empty packet list
            new Header(new ArrayList<>(), 0, null).setVisible(true);
        });
    }
}
