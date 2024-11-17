package com.first.src;



import java.util.*;

import com.first.ViewPackets;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.util.NifSelector;
import org.pcap4j.core.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

import com.formdev.flatlaf.FlatLightLaf;

public class Main extends javax.swing.JFrame {

    int index;
    List<PcapNetworkInterface> device = null;

    public Main() {
        // Set FlatLaf look and feel
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }
        initComponents();
    }

    private void initComponents() {
        setTitle("Network Packet Sniffer");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        JLabel headerLabel = new JLabel("NETWORK PACKET SNIFFER", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.add(headerLabel, BorderLayout.CENTER);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> jList1 = new JList<>(listModel);
        jList1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jList1.addListSelectionListener(evt -> {
            index = jList1.getSelectedIndex();
            if (index >= 0) {
                jLabel6.setText(device.get(index).getDescription());
            }
        });

        JScrollPane listScrollPane = new JScrollPane(jList1);
        mainPanel.add(listScrollPane, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(2, 1));
        JLabel jLabel1 = new JLabel("Selected Interface:");
        jLabel6 = new JLabel("");
        jLabel6.setFont(new Font("Arial", Font.PLAIN, 14));
        infoPanel.add(jLabel1);
        infoPanel.add(jLabel6);
        mainPanel.add(infoPanel, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton jButton1 = new JButton("Check Available Interfaces");
        jButton1.addActionListener(evt -> {
            try {
                device = Pcaps.findAllDevs();
                listModel.clear();
                for (int i = 0; i < device.size(); i++) {
                    listModel.addElement((i + 1) + ". Name: " + device.get(i).getName() + " | Description: " + device.get(i).getDescription());
                }
            } catch (PcapNativeException e) {
                e.printStackTrace();
            }
        });
        buttonPanel.add(jButton1);

        JButton jButton2 = new JButton("View Packets");
        jButton2.addActionListener(evt -> {
            if (index >= 0) {
                new ViewPackets(device, index).setVisible(true);
                this.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "Please select an interface.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPanel.add(jButton2);

        JButton jButton3 = new JButton("Exit");
        jButton3.addActionListener(evt -> System.exit(0));
        buttonPanel.add(jButton3);

        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

    private JLabel jLabel6;
}
