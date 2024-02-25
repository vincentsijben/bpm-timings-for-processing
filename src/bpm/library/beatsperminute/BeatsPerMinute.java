/*
 - BPM: bug: make bpm.bpm public
 - BPM: bug: showinfo bpm class should have nostroke in pushstyle
 todo: //processing doesn't like transparancy in 3D
 //see https://www.reddit.com/r/processing/comments/59r0le/problems_with_transparency_in_3d/
 */

package bpm.library.beatsperminute;

import bpm.library.InfoPanel;
import processing.core.*;
import processing.event.KeyEvent;

/**
 * BPM timings for Processing..
 *
 */
public class BeatsPerMinute {

  // myParent is a reference to the parent sketch
  private PApplet parent;
  private InfoPanel infoPanel;

  private int bpm;
  private float beatDuration = 0.001f; // to prevent dividing by 0
  /**
   	   * Read the current beatcount
   	   *
   	   */
  public float beatCount = 0f;
  private boolean enableKeyPress;
  private boolean keyPressedActionTaken;

  //helper variables to only run functions once until they reset
  private boolean doOnce = true;
  private float lastBeatCount = 0f;
  private int lastFrameCount = -1;

  //we use these variables to be able to 'reset' the time whenever we want.
  private float millis_runtime;
  private float millis_start;

  /**
   	   * helper booleans that turn true every n beats. Added one extra upfront that isn't used, so the user could do every[3] which means 3rd beat.
   	   *
   	   */
  public boolean[] every = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
  /**
   	   * helper booleans that turn true every n beats for 1 frame.
   	   *
   	   */
  public boolean[] every_once = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};

  /**
   	   * Read this libraries version
   	   *
   	   */
  public final static String VERSION = "##library.prettyVersion##";

  /**
   		 * Call this constructor in the setup() method of your
   		 * sketch to initialize and start the library.
   		 * Set the second argument to be your starting bpm amount.
   		 *
   		 * @param theParent Your sketch's PApplet object
   		 * @param bpmTemp the initial beat per minute amount (integer)
   		 */
  public BeatsPerMinute(PApplet parent) {
    this.welcome();
    this.parent = parent;
    this.setBPM(60);
    this.millis_start = parent.millis();
    this.infoPanel = new InfoPanel(parent);
    this.infoPanel.h = parent.height;
    this.infoPanel.overlay = parent.createGraphics(parent.width, parent.height);
    this.enableKeyPress = true;
    this.keyPressedActionTaken = false;

    parent.registerMethod("draw", this);
    parent.registerMethod("post", this);
    parent.registerMethod("keyEvent", this);
  }

  private void welcome() {
    System.out.println("##library.name## ##library.prettyVersion## by ##author.url##");
  }

  public BeatsPerMinute setBPM(int bpm) {
    if (bpm < 1) bpm = 1;
    this.bpm = bpm;
    this.beatDuration = 60000 / bpm;
    return this;
  }




  /**
   	   * returns a string with information about bpm amount, beatcount and framerate, to be used in your surface title.
   	   * @return String
   	   *
   	   */
  public String setSurfaceTitle() {
    return "BPM: " + this.bpm + " // beatCount: " + PApplet.nf((int) this.beatCount, 3) + " // frameRate: " + PApplet.nf((int) this.parent.frameRate, 2);
  }
  /**
   	   * Overload function for linear(float durationInBeats, float delayInBeats). Sets the durationInBeats to 1 and delayInBeats to 0.
   	   *
   	   * @return float
   	   *
   	   */
  public float linear() {
    return linear(1, 0);
  }
  /**
   	   * Overload function for linear(float durationInBeats, float delayInBeats). Sets the delayInBeats to 0.
   	   *
   	   * @param durationInBeats the duration in amount of beats
   	   * @return float
   	   *
   	   */
  public float linear(float durationInBeats) {
    return linear(durationInBeats, 0);
  }
  /**
   	   * returns a normalized 'linear' progress value for the durationInBeats amount of beats
   	   * with a delay of the delayInBeats amount of beats
   	   * range from 0 to 1
   	   *
   	   * @param durationInBeats the duration in amount of beats
   	   * @param delayInBeats the delay in amount of beats
   	   * @return float
   	   *
   	   */
  public float linear(float durationInBeats, float delayInBeats) {
    float duration = this.beatDuration*durationInBeats;
    float delay = this.beatDuration * delayInBeats;

    if (this.millis_runtime < delay) return 0;
    return (this.millis_runtime-delay)%duration/duration;
  }

  /**
   	   * Overload function for linearBounce(float durationInBeats, float delayInBeats). Sets the durationInBeats to 1 and delayInBeats to 0.
   	   *
   	   * @return float
   	   *
   	   */
  public float linearBounce() {
    return linearBounce(1, 0);
  }
  /**
   	   * Overload function for linearBounce(float durationInBeats, float delayInBeats). Sets the delayInBeats to 0.
   	   *
   	   * @param durationInBeats the duration in amount of beats
   	   * @return float
   	   *
   	   */
  public float linearBounce(float durationInBeats) {
    return linearBounce(durationInBeats, 0);
  }
  /**
   	   * returns a normalized 'linear' progress value for the durationInBeats amount of beats
   	   * with a delay of the delayInBeats amount of beats
   	   * in a 'bounced' way: range from 0 to 1 to 0
   	   *
   	   * @param durationInBeats the duration in amount of beats
   	   * @param delayInBeats the delay in amount of beats
   	   * @return float
   	   *
   	   */
  public float linearBounce(float durationInBeats, float delayInBeats) {
    float progress = linear(durationInBeats, delayInBeats);
    if (progress < 0.5) return PApplet.map(progress, 0, 0.5f, 0, 1);
    return PApplet.map(progress, 0.5f, 1, 1, 0);
  }

  /**
   	   * Overload function for ease(float durationInBeats, float delayInBeats). Sets the durationInBeats to 1 and delayInBeats to 0.
   	   *
   	   * @return float
   	   *
   	   */
  public float ease() {
    return ease(1, 0);
  }
  /**
   	   * Overload function for ease(float durationInBeats, float delayInBeats). Sets the delayInBeats to 0.
   	   *
   	   * @param durationInBeats the duration in amount of beats
   	   * @return float
   	   *
   	   */
  public float ease(float durationInBeats) {
    return ease(durationInBeats, 0);
  }
  /**
   	   * returns a normalized 'eased' progress value for the durationInBeats amount of beats
   	   * with a delay of the delayInBeats amount of beats
   	   * range from 0 to 1
   	   *
   	   * @param durationInBeats the duration in amount of beats
   	   * @param delayInBeats the delay in amount of beats
   	   * @return float
   	   *
   	   */
  public float ease(float durationInBeats, float delayInBeats) {
    return PApplet.sin(PApplet.lerp(0, (float) Math.PI/2, linear(durationInBeats, delayInBeats)));
  }

  /**
   	   * Overload function for easeBounce(float durationInBeats, float delayInBeats). Sets the durationInBeats to 1 and delayInBeats to 0.
   	   *
   	   * @return float
   	   *
   	   */
  public float easeBounce() {
    return easeBounce(1, 0);
  }
  /**
   	   * Overload function for easeBounce(float durationInBeats, float delayInBeats). Sets the delayInBeats to 0.
   	   *
   	   * @param durationInBeats the duration in amount of beats
   	   * @return float
   	   *
   	   */
  public float easeBounce(float durationInBeats) {
    return easeBounce(durationInBeats, 0);
  }
  /**
   	   * returns a normalized 'eased' progress value for the durationInBeats amount of beats
   	   * with a delay of the delayInBeats amount of beats
   	   * in a 'bounced' way: range from 0 to 1 to 0
   	   *
   	   * @param durationInBeats the duration in amount of beats
   	   * @param delayInBeats the delay in amount of beats
   	   * @return float
   	   *
   	   */
  public float easeBounce(float durationInBeats, float delayInBeats) {
    return PApplet.sin(PApplet.lerp(0, (float) Math.PI, linear(durationInBeats, delayInBeats)));
  }













  //////////////////////////////////////////////////////////////
  //                    KEYBOARD + MOUSE
  //////////////////////////////////////////////////////////////
  public BeatsPerMinute disableKeyPress() {
    this.enableKeyPress = false;
    this.infoPanel.enableKeyPress = false;
    return this;
  }

  public void keyEvent(KeyEvent event) {
    if (this.enableKeyPress) {
      // Removed KeyEvent.TYPE because p2d or p3d don't register TYPE
      if (event.getAction() == KeyEvent.PRESS) this.onKeyPress(event);
      else if (event.getAction() == KeyEvent.RELEASE) this.onKeyRelease(event);
    }
  }

  private void onKeyPress(KeyEvent event) {
    if (event.getKeyCode() == '=') this.setBPM(++this.bpm);
    if (event.getKeyCode() == '-') this.setBPM(--this.bpm);
    if (event.getKey() == '0'  && !this.keyPressedActionTaken) {
      this.keyPressedActionTaken = true;
      this.millis_start = this.parent.millis();
      // change bpm only if next keystroke is registered under 2 seconds
      if (this.millis_runtime > 0 && this.millis_runtime < 2000) this.setBPM((int) (60000/this.millis_runtime));
    }
  }

  private void onKeyRelease(KeyEvent event) {
    this.keyPressedActionTaken = false;
  }










  //////////////////////////////////////////////////////////////
  //                    INFOPANEL
  //////////////////////////////////////////////////////////////
  public BeatsPerMinute showInfoPanel() {
    this.infoPanel.show = true;
    return this;
  }

  public BeatsPerMinute setInfoPanelY(int y) {
    this.infoPanel.y = y;
    return this;
  }

  public BeatsPerMinute setInfoPanelKey(char keyboardKey) {
    this.infoPanel.keyboardKey = keyboardKey;
    return this;
  }

  public void draw() {

    if (this.infoPanel.show) {
      PGraphics overlay = this.infoPanel.overlay;
      overlay.beginDraw();
      overlay.pushStyle();
      overlay.rectMode(PConstants.CORNER);
      overlay.textAlign(PConstants.LEFT, PConstants.CENTER);
      overlay.fill(255, 100);
      overlay.rect(0, 0, 190, 440);
      overlay.fill(0);
      overlay.textSize(20);
      overlay.text("BPM: " + this.bpm, 10, 20);
      overlay.text("beatDuration: " + Math.floor(this.beatDuration), 10, 40);
      overlay.text("beatCount: " + Math.floor(this.beatCount), 10, 60);
      overlay.text("normalized: " + PApplet.nf(linear(), 0, 3), 10, 80);
      overlay.text("frameRate: " + (int)(this.parent.frameRate), 10, 100);

      overlay.textSize(12);
      for (int i=2; i<this.every.length; i++) {
        overlay.fill(0);
        overlay.text("every " + i + " beat", 12, 88 + i*20);
        if (this.every[i]) overlay.fill(0);
        else overlay.fill(255);
        overlay.ellipse(90, 90 + i*20, 10, 10);
      }
      overlay.popStyle();
      overlay.endDraw();
      this.parent.image(overlay, this.infoPanel.x, this.infoPanel.y, this.infoPanel.w, this.infoPanel.h); // Draw the overlay onto the main canvas
    }
  }










  public void post() {
    // https://github.com/benfry/processing4/wiki/Library-Basics
    // you cant draw in post()
    this.millis_runtime = this.parent.millis()-this.millis_start;
    // Assuming beatDuration is updated elsewhere when bpm changes
    this.beatCount = this.millis_runtime/this.beatDuration;

    checkBeatPeriods();
  }

  private void checkBeatPeriods() {
    //skip i=0
    for (int i=1; i<this.every.length; i++) this.every[i] = (int) this.beatCount % i==0;

    //set the corresponding 'once' boolean to true for a 1 frame period
    if (this.doOnce == false) {
      this.doOnce = true;
      this.lastBeatCount = (int) this.beatCount;
      this.lastFrameCount = this.parent.frameCount;
      for (int i=1; i<this.every_once.length; i++) this.every_once[i] = this.every[i];
    }

    //flip the 'once' booleans immediately after one frame
    if (this.parent.frameCount != this.lastFrameCount) for (int i=1; i<this.every_once.length; i++) this.every_once[i] = false;

    //reset the mechanism after one beat
    if ((int) this.lastBeatCount != (int) this.beatCount) this.doOnce = false;
  }
}
