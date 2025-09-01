// src/com/webradio/ui/WebRadioUI.java
package com.webradio.ui;

import javax.swing.*;
//import javax.swing.event.ListSelectionEvent;
//import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import com.webradio.model.*;
import com.webradio.audio.AudioPlayer;

public class WebRadioUI {
    private RadioModel radioModel;
    private AudioPlayer audioPlayer;
    private JFrame frame;
    private JList<Radio> radioList;
    private DefaultListModel<Radio> listModel;
    private JLabel statusLabel;
    private Radio currentlyPlaying;

    public WebRadioUI() {
        radioModel = new RadioModel();
        audioPlayer = new AudioPlayer();

        // 👇 Ajouter le listener ici
        audioPlayer.setPlaybackListener(new AudioPlayer.PlaybackListener() {
            @Override
            public void onStart(Radio radio) {
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("♪ " + radio.getName());
                });
            }

            @Override
            public void onStop(Radio radio) {
                SwingUtilities.invokeLater(() -> {
                    if (currentlyPlaying == radio) {
                        statusLabel.setText("--");
                    }
                });
            }
        });
    }

    public void createUI() {
        DarkTheme.apply();

        frame = new JFrame("WebRadio Player");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Stoppe la radio si elle joue
                stopRadio();
                // Ferme la fenêtre
                frame.dispose();
                System.exit(0); // quitte proprement
            }
        });

        frame.setLayout(new BorderLayout());

        // Barre de statut
        statusLabel = DarkTheme.createStyledLabel("--");
        frame.add(statusLabel, BorderLayout.NORTH);

        // Panneau principal
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Barre d'outils
        mainPanel.add(createToolbar(), BorderLayout.NORTH);

        // Liste des radios
        mainPanel.add(createRadioListPanel(), BorderLayout.CENTER);

        // Panneau de contrôle
        mainPanel.add(createControlPanel(), BorderLayout.SOUTH);

        frame.add(mainPanel, BorderLayout.CENTER);

        // Configuration finale
        frame.setSize(450, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel createToolbar() {
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbar.setBackground(DarkTheme.BACKGROUND);

        JButton addButton = DarkTheme.createStyledButton("➕ Ajouter Radio");
        addButton.addActionListener(e -> showAddDialog());
        toolbar.add(addButton);

        return toolbar;
    }

    private JScrollPane createRadioListPanel() {
        listModel = new DefaultListModel<>();
        radioList = new JList<>(listModel);
        updateRadioList();

        // Clic simple pour jouer
        radioList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    int index = radioList.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        Radio selected = listModel.getElementAt(index);
                        if (selected != currentlyPlaying) {
                            playRadio(selected);
                        }
                    }
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    showContextMenu(e);
                }
            }
        });

        return new JScrollPane(radioList);
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBackground(DarkTheme.BACKGROUND);

        JButton prevBtn = DarkTheme.createControlButton("⏮");
        JButton playBtn = DarkTheme.createControlButton("▶");
        JButton nextBtn = DarkTheme.createControlButton("⏭");
        JButton stopBtn = DarkTheme.createControlButton("⏹");

        prevBtn.addActionListener(e -> previousRadio());
        playBtn.addActionListener(e -> playSelectedRadio());
        nextBtn.addActionListener(e -> nextRadio());
        stopBtn.addActionListener(e -> stopRadio());

        panel.add(prevBtn);
        panel.add(playBtn);
        panel.add(nextBtn);
        panel.add(stopBtn);

        return panel;
    }

    private void updateRadioList() {
        listModel.clear();
        for (Radio radio : radioModel.getRadios()) {
            listModel.addElement(radio);
        }
    }

    private void playRadio(Radio radio) {
        audioPlayer.playRadio(radio);
        currentlyPlaying = radio;
    }

    private void stopRadio() {
        audioPlayer.stopRadio();
        currentlyPlaying = null;
        statusLabel.setText("--");
    }

    private void playSelectedRadio() {
        Radio selected = radioList.getSelectedValue();
        if (selected != null) {
            playRadio(selected);
        }
    }

    private void previousRadio() {
        int current = radioList.getSelectedIndex();
        if (current > 0) {
            radioList.setSelectedIndex(current - 1);
        }
    }

    private void nextRadio() {
        int current = radioList.getSelectedIndex();
        if (current < listModel.getSize() - 1) {
            radioList.setSelectedIndex(current + 1);
        }
    }

    private void showAddDialog() {
        JTextField nameField = new JTextField(20);
        JTextField urlField = new JTextField(30);

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 5));
        panel.add(new JLabel("Nom:"));
        panel.add(nameField);
        panel.add(new JLabel("URL:"));
        panel.add(urlField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Ajouter une radio",
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String url = urlField.getText().trim();
            if (!name.isEmpty() && !url.isEmpty()) {
                radioModel.addRadio(name, url);
                updateRadioList();
            }
        }
    }

    private void showContextMenu(MouseEvent e) {
        int index = radioList.locationToIndex(e.getPoint());
        if (index >= 0) {
            radioList.setSelectedIndex(index);

            JPopupMenu popup = new JPopupMenu();

            JMenuItem editItem = new JMenuItem("✏️ Modifier");
            editItem.addActionListener(ev -> showEditDialog(index));
            popup.add(editItem);

            JMenuItem deleteItem = new JMenuItem("🗑️ Supprimer");
            deleteItem.addActionListener(ev -> deleteRadio(index));
            popup.add(deleteItem);

            popup.show(radioList, e.getX(), e.getY());
        }
    }

    private void showEditDialog(int index) {
        Radio radio = radioModel.getRadios().get(index);

        JTextField nameField = new JTextField(radio.getName(), 20);
        JTextField urlField = new JTextField(radio.getUrl(), 30);

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 5));
        panel.add(new JLabel("Nom:"));
        panel.add(nameField);
        panel.add(new JLabel("URL:"));
        panel.add(urlField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Modifier la radio",
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String url = urlField.getText().trim();
            if (!name.isEmpty() && !url.isEmpty()) {
                radioModel.updateRadio(index, name, url);
                updateRadioList();
            }
        }
    }

    private void deleteRadio(int index) {
        Radio radio = radioModel.getRadios().get(index);
        int result = JOptionPane.showConfirmDialog(frame,
                "Supprimer '" + radio.getName() + "' ?",
                "Confirmation", JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            radioModel.deleteRadio(index);
            updateRadioList();
        }
    }
}