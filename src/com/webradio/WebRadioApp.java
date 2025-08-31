package com.webradio;

import javax.swing.SwingUtilities;
import com.webradio.ui.WebRadioUI;

public class WebRadioApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new WebRadioUI().createUI();
        });
    }
}