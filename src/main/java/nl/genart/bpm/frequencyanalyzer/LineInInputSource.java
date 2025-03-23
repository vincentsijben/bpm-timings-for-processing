package bpm.library.frequencyanalyzer;

import ddf.minim.*;
import ddf.minim.analysis.*;

class LineInInputSource implements AudioInputSource {
  Minim minim;
  AudioInput lineIn;
  FFT fftLeft, fftRight, fftMixed;
  int bufferSize = 1024; //Default setting
  
  private AudioOutputMode channelOutput = AudioOutputMode.MONO; // Default setting

  public void setAudioOutputMode(AudioOutputMode mode) {
    this.channelOutput = mode;
  }

  public LineInInputSource(Minim minim, int size) {
    this.minim = minim;
    this.bufferSize = size;
  }

  @Override
    public void init() {
    // Assuming a stereo input for line-in
    this.lineIn = this.minim.getLineIn(Minim.MONO, this.bufferSize);
    this.fftLeft = new FFT(this.lineIn.bufferSize(), this.lineIn.sampleRate());
    this.fftRight = new FFT(this.lineIn.bufferSize(), this.lineIn.sampleRate());
    this.fftMixed = new FFT(this.lineIn.bufferSize(), this.lineIn.sampleRate());
    this.fftLeft.logAverages(22, 3);
    this.fftRight.logAverages(22, 3);
    this.fftMixed.logAverages(22, 3);
  }

  @Override
    public void start() {
    // Line-in starts automatically with getLineIn, but you might want to add logic here if needed
    //this.lineIn.enableMonitoring();
  }

  @Override
    public void stop() {
  }

  @Override
    public float[] getLeftChannelBuffer() {
    return this.lineIn != null ? lineIn.left.toArray() : new float[0];
  }

  @Override
    public float[] getRightChannelBuffer() {
    return lineIn != null ? lineIn.right.toArray() : new float[0];
  }

  @Override
    public void close() {
    if (lineIn != null) {
      lineIn.close(); // Closes the audio input and releases resources
      lineIn = null;
    }
  }

  @Override
    public float[] getAudioBuffer() {
    // Mix down the stereo signal to mono for a unified buffer
    float[] left = getLeftChannelBuffer();
    float[] right = getRightChannelBuffer();
    float[] mixed = new float[left.length];
    for (int i = 0; i < left.length; i++) {
      mixed[i] = (left[i] + right[i]) / 2; // Simple average mix of left and right
    }
    return mixed;
  }

  @Override
    public void performFFT() {
    switch (this.channelOutput) {
    case MONO:
      fftMixed.forward(lineIn.mix);
      break;
    case STEREO:
      fftLeft.forward(lineIn.left);
      fftRight.forward(lineIn.right);
      break;
    }
  }
  
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
    return fftMixed.getBand(i);
  }

  @Override
    public float getBandLeft(int i) {
    return fftLeft.getBand(i);
  }

  @Override
    public float getBandRight(int i) {
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
    // Assuming all FFT objects have the same specSize
    return fftMixed.specSize();
  }
  
  @Override
    public int avgSize() {
    // Assuming all FFT objects have the same avgSize
    return fftMixed.avgSize();
  }
  
  @Override
    public boolean isMonitoring() {
    return lineIn.isMonitoring();
  }
  
  @Override
    public void enableMonitoring() {
    lineIn.enableMonitoring();
  }
  
  @Override
    public void disableMonitoring() {
    lineIn.disableMonitoring();
  }
}
