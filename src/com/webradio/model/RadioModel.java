// src/com/webradio/model/RadioModel.java
package com.webradio.model;

import java.io.*;
import java.util.*;

public class RadioModel {
    private static final String RADIOS_FILE = "radios.txt";
    private List<Radio> radios;
    
    public RadioModel() {
        loadRadios();
    }
    
    public List<Radio> getRadios() {
        return new ArrayList<>(radios);
    }
    
    public void addRadio(String name, String url) {
        radios.add(new Radio(name, url));
        saveRadios();
    }
    
    public void updateRadio(int index, String name, String url) {
        if (index >= 0 && index < radios.size()) {
            radios.get(index).setName(name);
            radios.get(index).setUrl(url);
            saveRadios();
        }
    }
    
    public void deleteRadio(int index) {
        if (index >= 0 && index < radios.size()) {
            radios.remove(index);
            saveRadios();
        }
    }
    
    private void loadRadios() {
        radios = new ArrayList<>();
        try {
            File file = new File(RADIOS_FILE);
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split("\\|", 2);
                        if (parts.length == 2) {
                            radios.add(new Radio(parts[0], parts[1]));
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur chargement radios: " + e.getMessage());
        }
        
        // Radios par défaut
        if (radios.isEmpty()) {
            radios.add(new Radio("Radio FIP", "http://direct.fipradio.fr/live/fip-midfi.mp3"));
            radios.add(new Radio("France Inter", "http://direct.franceinter.fr/live/franceinter-midfi.mp3"));
            radios.add(new Radio("Shonan Beach FM", "http://shonanbeachfm.out.airtime.pro:8000/shonanbeachfm_a"));
            saveRadios();
        }
    }
    
    private void saveRadios() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RADIOS_FILE))) {
            for (Radio radio : radios) {
                writer.write(radio.getName() + "|" + radio.getUrl());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erreur sauvegarde radios: " + e.getMessage());
        }
    }
}