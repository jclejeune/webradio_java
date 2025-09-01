// src/com/webradio/audio/AudioPlayer.java
package com.webradio.audio;

import com.webradio.model.Radio;

public class AudioPlayer {
    private volatile boolean isPlaying = false;
    private Process currentProcess;
    private Thread playbackThread;
    private Radio currentRadio;

    public interface PlaybackListener {
        void onStart(Radio radio);

        void onStop(Radio radio);
    }

    private PlaybackListener listener;

    public void setPlaybackListener(PlaybackListener listener) {
        this.listener = listener;
    }

    public void playRadio(Radio radio) {
        stopRadio();

        currentRadio = radio;
        isPlaying = true;

        playbackThread = new Thread(() -> {
            try {
                System.out.println("▶ Lecture: " + radio.getName());
                System.out.println("🌐 URL: " + radio.getUrl());

                ProcessBuilder pb = new ProcessBuilder(
                        "vlc", "--intf", "dummy", "--play-and-exit", radio.getUrl());
                currentProcess = pb.start();

                // Ici on considère que VLC commence à jouer immédiatement
                if (listener != null)
                    listener.onStart(radio);

                currentProcess.waitFor();

            } catch (InterruptedException e) {
                System.out.println("⏹ Lecture interrompue");
            } catch (Exception e) {
                System.err.println("❌ Erreur de lecture: " + e.getMessage());
            } finally {
                isPlaying = false;
                if (listener != null)
                    listener.onStop(radio);
            }
        });

        playbackThread.start();
    }

    public void stopRadio() {
        if (!isPlaying)
            return;

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