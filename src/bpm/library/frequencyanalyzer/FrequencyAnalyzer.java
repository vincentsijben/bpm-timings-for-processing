/*
  todo: if left and right channel can be left (drums) and right (vocal), consider https://stackoverflow.com/questions/40050731/how-to-make-two-fft-objects-for-the-left-and-right-channel-with-the-minim-librar
 todo: check https://stackoverflow.com/questions/20408388/how-to-filter-fft-data-for-audio-visualisation
 todo: check https://www.ee.columbia.edu/~dpwe/e4896/index.html
 
 todo: check whatsup with problems with minim and audio input: https://code.compartmental.net/minim/audioinput_class_audioinput.html
 todo: infopanel adjustable
 todo: adjustable keys for each infopanel
 todo: test in processing 3, windows and mac
 todo: test without minim installed
 todo: in examples refer to audio settings in macos/windows for linein
 todo: show hotkeys in InfoPanel
 
 debate:
 1. reset maxVal every x seconds? we end up with huge spikes or maybe build a condition around it (dont map when maxVal = 0.00001f)
 or let the user decide when to call a resetMaxValue function
 2. Should there be only 1 maxVal for all frequencies together or per frequency band?
 */

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
  private AudioPlayer audioPlayer;
  private AudioInput audioInput;
  private AudioBuffer audioBuffer;
  private int bandsPerOctave;
  private FFT fft;
  private InfoPanel infoPanel;
  private InputMode mode;

  private boolean enableKeyPress;
  private boolean keyPressedActionTaken;
  private float durationResetMaxValue;
  private int startTime;
  private float maxVal;

  public FrequencyAnalyzer(PApplet parent) {
    this.parent = parent;
    this.minim = new Minim(parent);
    this.audioInput = minim.getLineIn(Minim.MONO);
    this.audioBuffer = audioInput.mix;
    this.bandsPerOctave = 3;
    this.fft = new FFT(this.audioInput.bufferSize(), this.audioInput.sampleRate());
    this.fft.logAverages(22, this.bandsPerOctave); // 3 results in 30 bands. 1 results in 10 etc.
    this.infoPanel = new InfoPanel(parent);
    this.mode = InputMode.MONO;

    this.enableKeyPress = true;
    this.keyPressedActionTaken = false;
    this.durationResetMaxValue = 0.0f;
    this.startTime = 0;
    this.maxVal = 0.000001f; //avoid NaN when using maxVal in map() in the first frame.

    parent.registerMethod("draw", this);
    parent.registerMethod("post", this);
    parent.registerMethod("keyEvent", this);
    parent.registerMethod("dispose", this);
  }

  public FrequencyAnalyzer setBandsPerOctave(int bandsPerOctave) {
    this.bandsPerOctave = bandsPerOctave;
    this.fft.logAverages(22, bandsPerOctave ); // 3 results in 30 bands. 1 results in 10 etc.
    return this;
  }

  public FrequencyAnalyzer setFile(String file) {
    this.audioPlayer = minim.loadFile(file);
    this.audioPlayer.play();
    this.audioPlayer.mute();
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

  public FrequencyAnalyzer setMode(InputMode mode) {
    this.mode = mode;

    // Always close the input when changing inputs. After testing in Windows, I couldn't get multiple input variables running at the same time.
    // Monitoring of inputLineIn is disabled by default when calling setInput again
    this.audioInput.close();
    //always mute the playing file, unmute it only when user chooses FILE input
    if (this.audioPlayer != null) this.audioPlayer.mute();

    switch(mode) {
    case MONO:
      this.audioInput = minim.getLineIn(Minim.MONO);
      this.audioBuffer = this.audioInput.mix;
      break;
    case STEREO:
      this.audioInput = minim.getLineIn(Minim.STEREO);
      this.audioBuffer = this.audioInput.mix;
      break;
    case FILE:
      if (this.audioPlayer != null) {
        this.audioBuffer = this.audioPlayer.mix;
        this.audioPlayer.unmute();
      } else {
        System.out.println("warning: no file input, switching to MONO");
        return this.setMode(InputMode.MONO);
      }
      break;
    }

    //reset the maxVal after each input switch
    this.resetMaxValue();
    return this;
  }










  //////////////////////////////////////////////////////////////
  //                    FFT ANALYSIS
  //////////////////////////////////////////////////////////////
  public int getBands() {
    return this.fft.avgSize();
  }
  public float getAvgRaw(int index) {
    return this.fft.getAvg(index);
  }

  //normalize the average for the given index
  public float getAvg(int index) {
    return this.getAvg(index, this.maxVal);
  }

  //set a new max value for the given index and constrain the result between 0 and 1
  public float getAvg(int index, float max) {
    if (max == 0.000001f) return 0.0f;
    return PApplet.constrain(PApplet.map(fft.getAvg(index), 0, max, 0, 1), 0, 1);
  }

  public void resetMaxValue() {
    this.maxVal = 0.000001f;
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
    if (this.mode==InputMode.FILE) {
      if (this.audioPlayer != null) {
        if (this.audioPlayer.isMuted()) this.audioPlayer.unmute();
        else this.audioPlayer.mute();
      }
    } else {
      if (this.audioInput.isMonitoring()) this.audioInput.disableMonitoring();
      else this.audioInput.enableMonitoring();
    }
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
      if (event.getKey() == '0' ) System.out.println("CTRL+0 is longpressed");

      // handle single press events
      if (event.getKey() == '1' && !this.keyPressedActionTaken) {
        this.setMode(InputMode.FILE);
        this.keyPressedActionTaken = true; // Set the flag to true to avoid repeating the action
      }
      if (event.getKey() == '2'  && !this.keyPressedActionTaken) {
        this.setMode(InputMode.MONO);
        this.keyPressedActionTaken = true; // Set the flag to true to avoid repeating the action
      }
      if (event.getKey() == '3'  && !this.keyPressedActionTaken) {
        this.setMode(InputMode.STEREO);
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
      for (int i = 0; i < this.fft.avgSize(); i++) {
        float xR = (i * overlay.width) / this.fft.avgSize();
        float yR = 100;

        overlay.fill(255);
        overlay.rect(xR, yR, overlay.width / this.fft.avgSize(), PApplet.lerp(0, -100, this.getAvg(i)));
        overlay.fill(255, 0, 0);
        overlay.textAlign(PApplet.CENTER, PApplet.CENTER);
        overlay.textSize(14);
        overlay.text(PApplet.round(PApplet.lerp(0, maxVal, this.getAvg(i))), xR + (overlay.width / this.fft.avgSize() / 2), yR - 20);
        overlay.textSize(8);
        overlay.text(i, xR + (overlay.width / this.fft.avgSize() / 2), yR-6);
      }
      overlay.fill(255);
      overlay.textSize(25);
      overlay.textAlign(PApplet.LEFT);
      overlay.text(PApplet.round(this.parent.frameRate), 20, 30);
      overlay.textAlign(PApplet.CENTER);
      overlay.text("maxVal: " + PApplet.round(maxVal), this.parent.width/2, 30);
      overlay.textAlign(PApplet.LEFT);
      String s = "selected mode: " + mode;
      float posX = overlay.width-overlay.textWidth(s)-10;
      overlay.text(s, posX, 30);
      if (mode == InputMode.FILE && audioPlayer != null) overlay.text("muted: " + audioPlayer.isMuted(), posX, 60);
      else overlay.text("monitoring: " + (audioInput.isMonitoring() ? "on": "off"), posX, 60);
      overlay.endDraw();
      this.parent.image(overlay, this.infoPanel.x, this.infoPanel.y, this.infoPanel.w, this.infoPanel.h); // Draw the overlay onto the main canvas
    }
  }










  public void post() {
    // https://github.com/benfry/processing4/wiki/Library-Basics
    // you cant draw in post() but its perfect for the fft analysis:
    fft.forward(this.audioBuffer);
    //determine max value to normalize all average values
    for (int i = 0; i < fft.avgSize(); i++) if (fft.getAvg(i) > this.maxVal) this.maxVal = fft.getAvg(i);
    if (this.durationResetMaxValue > 0.0f) {
      if (parent.millis() - this.startTime > this.durationResetMaxValue) {
        this.resetMaxValue();
        startTime = parent.millis();
      }
    }
  }

  public void dispose() {
    //might not be necessary, but just in case
    if (this.audioPlayer != null) this.audioPlayer.close();
    this.minim.stop();
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
      System.out.println("buffer size: " + this.audioInput.bufferSize());
      System.out.println( "sample rate: " + this.audioInput.sampleRate());
    }
  }
}
