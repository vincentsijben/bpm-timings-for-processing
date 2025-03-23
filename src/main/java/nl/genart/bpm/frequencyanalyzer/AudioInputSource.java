package nl.genart.bpm.frequencyanalyzer;

import ddf.minim.analysis.*;

public interface AudioInputSource {
    // Initialize the audio source
    void init();

    // Start processing the audio input
    void start();

    // Stop processing the audio input
    void stop();

    // Cleanup resources if necessary
    void close();
    
    // Get the current raw audio buffer
    float[] getAudioBuffer();
    float[] getLeftChannelBuffer();
    float[] getRightChannelBuffer();

    void performFFT(); // Perform FFT analysis on the current audio buffer
    
    FFT getFFT();
    FFT getFFTLeft();
    FFT getFFTRight();
    
    float getAvg(int i); // Get the amplitude for a specific frequency band in the logAverages function
    float getAvgLeft(int i); // Get the amplitude for a specific frequency band in the logAverages function
    float getAvgRight(int i); // Get the amplitude for a specific frequency band in the logAverages function
    
    float getBand(int i); // Get the amplitude for a specific frequency band
    float getBandLeft(int i); // Get the amplitude for a specific frequency band
    float getBandRight(int i); // Get the amplitude for a specific frequency band
    int specSize(); // Get the number of bands in the frequency spectrum (1000 ish)
    int avgSize(); // Get the number of bands in the frequency spectrum used by the logAverages function (30 ish)
    void setAudioOutputMode(AudioOutputMode mode);
    boolean isMonitoring(); 
    void disableMonitoring();
    void enableMonitoring();
    
}
