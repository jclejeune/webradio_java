// src/com/webradio/audio/AudioPlayer.java
package com.webradio.audio;

import com.webradio.model.Radio;

public class AudioPlayer {
    private volatile boolean isPlaying = false;
    private Process currentProcess;
    private Thread playbackThread;
    private Radio currentRadio;
    
    public void playRadio(Radio radio) {
        stopRadio();
        
        currentRadio = radio;
        isPlaying = true;
        
        playbackThread = new Thread(() -> {
            try {
                System.out.println("▶ Lecture: " + radio.getName());
                System.out.println("🌐 URL: " + radio.getUrl());
                
                // Simulation de lecture - Remplacez par une vraie implémentation
                // Exemples d'implémentations possibles:
                
                // Option 1: Avec VLC
                // scoop install vlc
                ProcessBuilder pb = new ProcessBuilder("vlc", "--intf", "dummy", radio.getUrl());
                currentProcess = pb.start();
                currentProcess.waitFor();
                
                // Option 2: Avec mpv
                //scoop install mpv
                // ProcessBuilder pb = new ProcessBuilder("mpv", "--no-video", radio.getUrl());
                // 1currentProcess = pb.start();
                // currentProcess.waitFor();
                
                // Option 3: Avec ffplay (partie de ffmpeg)
                //scoop install ffmpeg
                //le process ne s'arrête pas.
                // ProcessBuilder pb = new ProcessBuilder("ffplay", "-nodisp", "-autoexit", radio.getUrl());
                // currentProcess = pb.start();
                // currentProcess.waitFor();
                
                // Pour la démo, simulation
                while (isPlaying && !Thread.currentThread().isInterrupted()) {
                    Thread.sleep(1000);
                }
                
            } catch (InterruptedException e) {
                System.out.println("⏹ Lecture interrompue");
            } catch (Exception e) {
                System.err.println("❌ Erreur de lecture: " + e.getMessage());
            } finally {
                isPlaying = false;
            }
        });
        
        playbackThread.start();
    }
    
    public void stopRadio() {
        if (!isPlaying) return;
        
        System.out.println("⏹ Arrêt de la lecture");
        isPlaying = false;
        
        if (currentProcess != null && currentProcess.isAlive()) {
            currentProcess.destroyForcibly();
        }
        
        if (playbackThread != null && playbackThread.isAlive()) {
            playbackThread.interrupt();
            try {
                playbackThread.join(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        currentRadio = null;
    }
    
    public boolean isPlaying() {
        return isPlaying;
    }
    
    public Radio getCurrentRadio() {
        return currentRadio;
    }
}