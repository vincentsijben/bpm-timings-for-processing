package bpm.library.frequencyanalyzer;

import ddf.minim.*;
import ddf.minim.analysis.*;

class MicrophoneInputSource implements AudioInputSource {
  Minim minim;
  AudioInput mic;
  FFT fftLeft, fftRight, fftMixed;
  int bufferSize = 1024; //Default setting

  public AudioOutputMode channelOutput = AudioOutputMode.MONO; // Default setting

  public void setAudioOutputMode(AudioOutputMode mode) {
    this.channelOutput = mode;
  }

  public MicrophoneInputSource(Minim minim, int size) {
    this.minim = minim;
    this.bufferSize = size;
  }

  @Override
    public void init() {
    // Initialize the microphone input. Assuming mono input.
    this.mic = minim.getLineIn(Minim.MONO, this.bufferSize);
    this.fftLeft = new FFT(this.mic.bufferSize(), this.mic.sampleRate());
    this.fftRight = new FFT(this.mic.bufferSize(), this.mic.sampleRate());
    this.fftMixed = new FFT(this.mic.bufferSize(), this.mic.sampleRate());
    this.fftLeft.logAverages(22, 3);
    this.fftRight.logAverages(22, 3);
    this.fftMixed.logAverages(22, 3);
  }

  @Override
    public void start() {
    // The getLineIn method automatically starts capturing, so no action is needed here.
  }

  @Override
    public void stop() {
    // This method would stop processing or capturing audio, if necessary.
  }

  @Override
    public void close() {
    if (mic != null) {
      mic.close(); // Close and release resources
      mic = null;
    }
  }

  @Override
    public float[] getLeftChannelBuffer() {
    // Since the microphone input is mono, return the buffer directly.
    return mic.mix.toArray();
  }

  @Override
    public float[] getRightChannelBuffer() {
    // Since the microphone input is mono, there's no right channel.
    // Return the same buffer or possibly an empty buffer based on your application's needs.
    //return new float[0]; // Or return mic.mix.toArray() if you wish to treat it the same as the left channel.
    return mic.mix.toArray();
  }

  @Override
    public float[] getAudioBuffer() {
    // Return the mixed (mono) audio buffer.
    return mic.mix.toArray();
  }

  //@Override
  //public boolean isStereo() {
  //    // Microphone input is typically mono, so return false.
  //    return false;
  //}

  @Override
    public void performFFT() {
    // Perform FFT analysis on the microphone input.
    switch (this.channelOutput) {
    case MONO:
      fftMixed.forward(mic.mix);
      break;
    case STEREO:
      fftLeft.forward(mic.left);
      fftRight.forward(mic.right);
      break;
    }
  }

  // Implement the other methods required by the AudioInputSource interface...
  @Override
    public FFT getFFT() {
    return fftMixed;
  }
  
   @Override
    public FFT getFFTLeft() {
    return fftLeft;
  }
  
   @Override
    public FFT getFFTRight() {
    return fftRight;
  }
  
  @Override
    public float getBand(int i) {
    // Get the amplitude for a specific frequency band after FFT analysis.
    return fftMixed.getBand(i);
  }
  
  @Override
    public float getBandLeft(int i) {
    // Get the amplitude for a specific frequency band after FFT analysis.
    return fftLeft.getBand(i);
  }
  
  @Override
    public float getBandRight(int i) {
    // Get the amplitude for a specific frequency band after FFT analysis.
    return fftRight.getBand(i);
  }
  
  @Override
    public float getAvg(int i) {
    return fftMixed.getAvg(i);
  }
  
  @Override
    public float getAvgLeft(int i) {
    return fftLeft.getAvg(i);
  }
  
  @Override
    public float getAvgRight(int i) {
    return fftRight.getAvg(i);
  }

  @Override
    public int specSize() {
    // Return the number of frequency bands in the FFT analysis.
    return fftMixed.specSize();
  }
  
  @Override
    public int avgSize() {
    // Return the number of frequency bands in the FFT analysis use by the logAverages function.
    return fftMixed.avgSize();
  }
  
    @Override
    public boolean isMonitoring() {
    return mic.isMonitoring();
  }
  
  @Override
    public void enableMonitoring() {
    mic.enableMonitoring();
  }
  
  @Override
    public void disableMonitoring() {
    mic.disableMonitoring();
  }
}
