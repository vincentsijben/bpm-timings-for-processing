
package bpm.library.frequencyanalyzer;

//https://github.com/benfry/processing4/wiki/Library-Basics
import bpm.library.InfoPanel;
import processing.core.*;
import processing.event.KeyEvent;
import ddf.minim.*;
import ddf.minim.analysis.*;

public class FrequencyAnalyzer {

  private PApplet parent;
  private Minim minim;
  AudioInputSource currentInputSource;
  private AudioPlayer audioPlayer;
  private String file;
  // private int bandsPerOctave;

  private InfoPanel infoPanel;
  private AudioInputMode currentInputMode;
  private AudioOutputMode currentOutputMode;

  private boolean enableKeyPress;
  private boolean keyPressedActionTaken;
  private float durationResetMaxValue;
  private int startTime;
  private float maxVal;

  public FrequencyAnalyzer(PApplet parent) {
    this.parent = parent;
    this.infoPanel = new InfoPanel(parent);
    

    this.enableKeyPress = true;
    this.keyPressedActionTaken = false;
    this.durationResetMaxValue = 0.0f;
    this.startTime = 0;
    this.maxVal = 0.1f; //avoid NaN when using maxVal in map() in the first frame.

    parent.registerMethod("draw", this);
    parent.registerMethod("post", this);
    parent.registerMethod("keyEvent", this);
    parent.registerMethod("dispose", this);
  }

  public FrequencyAnalyzer addMinim(Minim minim) {
    this.minim = minim;
    this.setAudioInputMode(AudioInputMode.MICROPHONE);
    
    
    return this;
  }

  //public FrequencyAnalyzer setBandsPerOctave(int bandsPerOctave) {
  //  this.bandsPerOctave = bandsPerOctave;
  //  //this.fft.logAverages(22, bandsPerOctave ); // 3 results in 30 bands. 1 results in 10 etc.
  //  return this;
  //}

  public FrequencyAnalyzer setFile(String file) {
    this.file = file;
    return this;
  }

  public FrequencyAnalyzer resetMaxValueDuration(float duration) {
    this.durationResetMaxValue = duration;
    return this;
  }

  public FrequencyAnalyzer debug() {
    this.debugInfo();
    return this;
  }

  public FrequencyAnalyzer setAudioOutputMode(AudioOutputMode mode) {
    currentInputSource.setAudioOutputMode(mode);
    this.currentOutputMode = mode;
    return this;
  }

  public FrequencyAnalyzer setAudioInputMode(AudioInputMode newMode) {
    if (newMode == this.currentInputMode) return this;
    if (currentInputSource != null) {
      currentInputSource.close();
      currentInputSource = null;
    }

    // Initialize the new input source based on the selected mode
    switch (newMode) {
    case MICROPHONE:
      currentInputSource = new MicrophoneInputSource(minim); // Assuming a MicrophoneSource class exists
      break;
    case LINE_IN:
      currentInputSource = new LineInInputSource(minim);
      break;
    case AUDIO_FILE:
    if (this.file == null) System.out.println("no audio file was set");
    
      //currentInputSource = new AudioFileInputSource(minim, "https://github.com/vincentsijben/bpm-timings-for-processing/raw/main/assets/infraction_music_-_ritmo.mp3"); // Assuming an AudioFileSource class exists
      currentInputSource = new AudioFileInputSource(minim, this.file); // Assuming an AudioFileSource class exists
      //"stereotest.mp3"
    
      break;
    }

    // Start the new input source
    if (currentInputSource != null) {
      currentInputSource.init(); // Initialize the new source
      currentInputSource.start();
    }

    // Update the current mode
    currentInputMode = newMode;
    return this;
  }










  //////////////////////////////////////////////////////////////
  //                    FFT ANALYSIS
  //////////////////////////////////////////////////////////////

  public float[] getAudioBuffer() {
    return this.currentInputSource.getAudioBuffer();
  }
  public float[] getLeftChannelBuffer() {
    return this.currentInputSource.getLeftChannelBuffer();
  }
  public float[] getRightChannelBuffer() {
    return this.currentInputSource.getRightChannelBuffer();
  }


  public  void performFFT() {
    this.currentInputSource.performFFT();
  }
  public   FFT  getFFT() {
    return this.currentInputSource.getFFT();
  }
  public   FFT  getFFTLeft() {
    return this.currentInputSource.getFFTLeft();
  }
  public   FFT  getFFTRight() {
    return this.currentInputSource.getFFTRight();
  }

  public   float getBand(int i) {
    return this.currentInputSource.getBand(i);
  }
  public   float getBandLeft(int i) {
    return this.currentInputSource.getBandLeft(i);
  }
  public   float getBandRight(int i) {
    return this.currentInputSource.getBandRight(i);
  }
  public   int specSize() {
    return this.currentInputSource.specSize();
  }
  public   int avgSize() {
    return this.currentInputSource.avgSize();
  }
  public float getAvgRaw(int index) {
    return this.currentInputSource.getAvg(index);
  }
  public float getAvgRawLeft(int index) {
    return this.currentInputSource.getAvgLeft(index);
  }
  public float getAvgRawRight(int index) {
    return this.currentInputSource.getAvgRight(index);
  }
  ////normalize the average for the given index
  //public float getAvg(int index) {
  //  return this.getAvg(index, this.maxVal);
  //}

  ////set a new max value for the given index and constrain the result between 0 and 1
  //public float getAvg(int index, float max) {
  //  if (max <= 0.1f) return 0.0f;
  //  if (this.minim == null) return 0;
  //  return PApplet.constrain(PApplet.map(fft.getAvg(index), 0, max, 0, 1), 0, 1);
  //}

  public void resetMaxValue() {
    this.maxVal = 0.1f;
  }









  //////////////////////////////////////////////////////////////
  //                    KEYBOARD + MOUSE
  //////////////////////////////////////////////////////////////
  public FrequencyAnalyzer disableKeyPress() {
    this.enableKeyPress = false;
    this.infoPanel.enableKeyPress = false;
    return this;
  }

  private void toggleMuteOrMonitoring() {
    if (currentInputSource.isMonitoring()) currentInputSource.disableMonitoring();
    else currentInputSource.enableMonitoring();
    
    //todo: add muted toggle?
    
  }

  public void keyEvent(KeyEvent event) {
    if (this.enableKeyPress) {
      // Removed KeyEvent.TYPE because p2d or p3d don't register TYPE
      if (event.getAction() == KeyEvent.PRESS) this.onKeyPress(event);
      else if (event.getAction() == KeyEvent.RELEASE) this.onKeyRelease(event);
    }
  }

  private void onKeyPress(KeyEvent event) {
    if (event.isControlDown()) {

      //handle long press events, only works in default renderer, not in P2D or P3D
      //if (event.getKey() == '0' ) System.out.println("CTRL+0 is longpressed");

      // handle single press events
      if (event.getKey() == '1' && !this.keyPressedActionTaken) {
        this.setAudioInputMode(AudioInputMode.AUDIO_FILE);
        this.keyPressedActionTaken = true; // Set the flag to true to avoid repeating the action
      }
      if (event.getKey() == '2'  && !this.keyPressedActionTaken) {
        this.setAudioInputMode(AudioInputMode.MICROPHONE);
        this.keyPressedActionTaken = true; // Set the flag to true to avoid repeating the action
      }
      if (event.getKey() == '3'  && !this.keyPressedActionTaken) {
        this.setAudioInputMode(AudioInputMode.LINE_IN);
        this.keyPressedActionTaken = true; // Set the flag to true to avoid repeating the action
      }
      if (event.getKeyCode() == 'M' && !this.keyPressedActionTaken) {
        this.toggleMuteOrMonitoring();
        this.keyPressedActionTaken = true; // Set the flag to true to avoid repeating the action
      }
      if (event.getKeyCode() == 'R' && !this.keyPressedActionTaken) {
        this.resetMaxValue();
        this.keyPressedActionTaken = true; // Set the flag to true to avoid repeating the action
      }
    }
  }

  private void onKeyRelease(KeyEvent event) {
    // Reset the flag when the key is released, allowing for the action to be taken on the next key press
    this.keyPressedActionTaken = false;
  }










  //////////////////////////////////////////////////////////////
  //                    INFOPANEL
  //////////////////////////////////////////////////////////////
  public FrequencyAnalyzer showInfoPanel() {
    this.infoPanel.show = true;
    return this;
  }

  public FrequencyAnalyzer setInfoPanelY(int y) {
    this.infoPanel.y = y;
    return this;
  }

  public FrequencyAnalyzer setInfoPanelKey(char keyboardKey) {
    this.infoPanel.keyboardKey = keyboardKey;
    return this;
  }

  public void draw() {
    if (this.infoPanel.show) {
      PGraphics overlay = this.infoPanel.overlay;
      overlay.beginDraw();
      overlay.fill(200, 127);
      overlay.noStroke();
      overlay.rect(0, 0, overlay.width, overlay.height);
      for (int i = 0; i < this.avgSize(); i++) {
        float xR = (i * overlay.width) / this.avgSize();
        float yR = 100;



        overlay.fill(255);
        if (this.currentOutputMode == AudioOutputMode.STEREO) {
          overlay.rect(xR, yR, overlay.width / this.avgSize()/2, PApplet.lerp(0, -100, this.getAvgRawLeft(i)));
          overlay.rect(xR + overlay.width / this.avgSize()/2, yR, overlay.width / this.avgSize()/2, PApplet.lerp(0, -100, this.getAvgRawRight(i)));
        } else {
          overlay.rect(xR, yR, overlay.width / this.avgSize(), PApplet.lerp(0, -100, this.getAvgRaw(i)));
        }

        overlay.fill(255, 0, 0);
        overlay.textAlign(PApplet.CENTER, PApplet.CENTER);
        if (this.currentOutputMode == AudioOutputMode.STEREO) {
          overlay.textSize(10);
          overlay.text(PApplet.round(PApplet.lerp(0, maxVal, this.getAvgRawLeft(i))), xR + (overlay.width / this.avgSize() / 4), yR - 20);
          overlay.text(PApplet.round(PApplet.lerp(0, maxVal, this.getAvgRawRight(i))), xR + (overlay.width / this.avgSize() / 4*3), yR - 20);
        } else {
          overlay.textSize(14);
          overlay.text(PApplet.round(PApplet.lerp(0, maxVal, this.getAvgRaw(i))), xR + (overlay.width / this.avgSize() / 2), yR - 20);
        }
        overlay.textSize(8);
        overlay.text(i, xR + (overlay.width / this.avgSize() / 2), yR-6);
      }
      overlay.fill(255);
      overlay.textSize(25);
      overlay.textAlign(PApplet.LEFT);
      overlay.text(PApplet.round(this.parent.frameRate), 20, 30);
      overlay.textAlign(PApplet.CENTER);
      overlay.text("maxVal: " + PApplet.round(maxVal), this.parent.width/2, 30);
      overlay.textAlign(PApplet.LEFT);
      String s = "selected mode: " + this.currentInputMode;
      float posX = overlay.width-overlay.textWidth(s)-10;
      overlay.text(s, posX, 30);
      if (this.currentInputMode == AudioInputMode.AUDIO_FILE && audioPlayer != null) overlay.text("muted: " + audioPlayer.isMuted(), posX, 60);
      //else overlay.text("monitoring: " + (audioInput.isMonitoring() ? "on": "off"), posX, 60);
      else overlay.text("monitoring: " + (currentInputSource.isMonitoring() ? "on": "off"), posX, 60);
      overlay.endDraw();
      this.parent.image(overlay, this.infoPanel.x, this.infoPanel.y, this.infoPanel.w, this.infoPanel.h); // Draw the overlay onto the main canvas
    }
  }










  public void post() {
    // https://github.com/benfry/processing4/wiki/Library-Basics
    // you cant draw in post() but its perfect for the fft analysis:
    if (this.minim != null) {
      this.performFFT();

      //determine max value to normalize all average values
      //for (int i = 0; i < fft.avgSize(); i++) if (fft.getAvg(i) > this.maxVal) this.maxVal = fft.getAvg(i);
      //if (this.durationResetMaxValue > 0.0f) {
      //  if (parent.millis() - this.startTime > this.durationResetMaxValue) {
      //    this.resetMaxValue();
      //    startTime = parent.millis();
      //  }
      //}
    }
  }

  public void dispose() {
    //might not be necessary, but just in case
    if (currentInputSource != null) currentInputSource.close();
    if (this.minim != null) this.minim.stop();
  }

  private void debugInfo() {
    long millis = System.currentTimeMillis();
    java.util.Date date = new java.util.Date(millis);
    System.out.println(date);

    System.out.println("Your OS name -> " + System.getProperty("os.name"));
    System.out.println("Your OS version -> " + System.getProperty("os.version"));
    System.out.println("Your OS Architecture -> " + System.getProperty("os.arch"));
    if (minim != null) {
      System.out.println("MONO -> " + minim.getLineIn(Minim.MONO));
      System.out.println("STEREO -> " + minim.getLineIn(Minim.STEREO));
      //System.out.println("buffer size: " + this.audioInput.bufferSize());
      //System.out.println( "sample rate: " + this.audioInput.sampleRate());
    }
  }
}
