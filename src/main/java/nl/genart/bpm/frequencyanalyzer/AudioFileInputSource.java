package nl.genart.bpm.frequencyanalyzer;

import ddf.minim.*;
import ddf.minim.analysis.*;
//test
class AudioFileInputSource implements AudioInputSource {
  Minim minim;
  FFT fftLeft, fftRight, fftMixed;
  AudioPlayer player;
  String filePath;
  int bufferSize = 1024; // Default setting

  public AudioOutputMode channelOutput = AudioOutputMode.MONO; // Default setting

  public void setAudioOutputMode(AudioOutputMode mode) {
    this.channelOutput = mode;
  }

  public AudioFileInputSource(Minim minim, int size, String filePath) {
    this.minim = minim;
    this.filePath = filePath;
    this.bufferSize = size;
  }

  @Override
    public void init() {
    this.player = minim.loadFile(filePath, this.bufferSize); // Load the file with a buffer size of 2048
    this.fftLeft = new FFT(this.player.bufferSize(), this.player.sampleRate());
    this.fftRight = new FFT(this.player.bufferSize(), this.player.sampleRate());
    this.fftMixed = new FFT(this.player.bufferSize(), this.player.sampleRate());
    this.fftLeft.logAverages(22, 3);
    this.fftRight.logAverages(22, 3);
    this.fftMixed.logAverages(22, 3);
  }

  @Override
    public void start() {
    if (player != null) {
      player.play();
      //player.loop(); // Loop the audio if needed
    }
  }

  @Override
    public void stop() {
    if (player != null) {
      player.pause(); // Pause playback
    }
  }

  @Override
    public void close() {
    if (player != null) {
      player.close(); // Close and release the audio file
      player = null;
    }
  }

  @Override
    public float[] getLeftChannelBuffer() {
    return player != null ? player.left.toArray() : new float[0];
  }

  @Override
    public float[] getRightChannelBuffer() {
    return player != null ? player.right.toArray() : new float[0];
  }

  @Override
    public float[] getAudioBuffer() {
    // This method could return a mono mix or just the left channel, depending on your needs.
    // For simplicity, returning the left channel.
    return getLeftChannelBuffer();
  }

  // Implementing performFFT, getBand, and specSize methods
  @Override
    public void performFFT() {

    if (player.isPlaying()) {
      switch (this.channelOutput) {
      case MONO:
        fftMixed.forward(player.mix);
        break;
      case STEREO:
        fftLeft.forward(player.left);
        fftRight.forward(player.right);
        break;
      }
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
    return fftMixed.specSize();
  }
  
  @Override
    public int avgSize() {
    return fftMixed.avgSize();
  }
  
    @Override
    public boolean isMonitoring() {
    // there is no monitoring for audio files
    return false;
  }
  
  @Override
    public void enableMonitoring() {
    //dont do anyting
  }
  
  @Override
    public void disableMonitoring() {
    //dont do anyting
  }
}
