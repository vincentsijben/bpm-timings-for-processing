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
  private float beatDuration;
  private int beatCount = 0;
  private boolean enableKeyPress;
  private boolean keyPressedActionTaken;

  //helper variables to only run functions once until they reset
  private boolean doOnce;
  private int lastBeatCount;
  private int lastFrameCount;

  //we use these variables to be able to 'reset' the time whenever we want.
  private long millis_runtime;
  private long millis_start;

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
   		 * @param parent Your sketch's PApplet object
   		 * //@param bpmTemp the initial beat per minute amount (integer)
   		 */
  public BeatsPerMinute(PApplet parent) {
    this.welcome();
    this.parent = parent;
    this.beatDuration = 0.001f; // to prevent dividing by 0
    this.doOnce = true;
    this.beatCount = 0;
    this.lastBeatCount = 0;
    this.lastFrameCount = -1;
    this.setBPM(60); 
    this.millis_start = parent.millis();
    this.infoPanel = new InfoPanel(parent);
    this.infoPanel.overlay = parent.createGraphics(190, 440);
    this.enableKeyPress = true;
    this.keyPressedActionTaken = false;

    parent.registerMethod("draw", this); 
    parent.registerMethod("pre", this);
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

  public int getBPM() {
    return this.bpm;
  }

  public int getBeatCount() {
    return this.beatCount;
  }

  public String getSurfaceTitle() {
    return "BPM: " + this.bpm + " // beatCount: " + PApplet.nf(this.beatCount, 3) + " // frameRate: " + PApplet.nf((int) this.parent.frameRate, 2);
  }

  public float adsr(float attackDuration) {
    return this.adsr(attackDuration, 0, 1, 0, 1, 0);
  }

  public float adsr(float attackDuration, float decayDuration, float sustainLevel, float releaseDuration) {
    return this.adsr(attackDuration, decayDuration, sustainLevel, releaseDuration, 1, 0);
  }

  public float adsr(float attackDuration, float decayDuration, float sustainLevel, float releaseDuration, float durationInBeats) {
    return this.adsr(attackDuration, decayDuration, sustainLevel, releaseDuration, durationInBeats, 0);
  }

  public float adsr(float attackDuration, float decayDuration, float sustainLevel, float releaseDuration, float durationInBeats, float delayInBeats) {
    float duration = this.beatDuration * durationInBeats;
    float delay = this.beatDuration * delayInBeats;
    if (this.millis_runtime < delay) return 0;
    float currentTime = (this.millis_runtime-delay) % duration;
    float attackEndTime = duration * attackDuration;
    float decayEndTime = attackEndTime + this.beatDuration * decayDuration;
    float sustainEndTime = duration - duration * releaseDuration;

    // Compute ADSR value based on the current phase
    if (currentTime <= attackEndTime) return currentTime / attackEndTime; // Attack phase
    if (currentTime <= decayEndTime) return 1 + (sustainLevel - 1) * ((currentTime - attackEndTime) / (this.beatDuration * decayDuration)); // Decay phase
    if (currentTime <= sustainEndTime) return sustainLevel; // Sustain phase
    return sustainLevel * (1 - (currentTime - sustainEndTime) / (duration * releaseDuration)); // Release phase
  }

  public float linear() {
    return linear(1, 0);
  }

  public float linear(float durationInBeats) {
    return linear(durationInBeats, 0);
  }

  public float linearBounce() {
    return linearBounce(1, 0);
  }

  public float linearBounce(float durationInBeats) {
    return linearBounce(durationInBeats, 0);
  }

  public float linear(float durationInBeats, float delayInBeats) {
    return this.adsr(1, 0, 1, 0, durationInBeats, delayInBeats);
  }

  public float linearBounce(float durationInBeats, float delayInBeats) {
    return this.adsr(0.5f, 0, 1, 0.5f, durationInBeats, delayInBeats);
  }

  public float ease() {
    return ease(1, 0);
  }

  public float ease(float durationInBeats) {
    return ease(durationInBeats, 0);
  }

  public float ease(float durationInBeats, float delayInBeats) {
    return (float) easeInOutSine(linear(durationInBeats, delayInBeats));
  }

  public float easeBounce() {
    return easeBounce(1, 0);
  }

  public float easeBounce(float durationInBeats) {
    return easeBounce(durationInBeats, 0);
  }

  public float easeBounce(float durationInBeats, float delayInBeats) {
    return (float) easeInOutSineMirror(linear(durationInBeats, delayInBeats));
  }

  private double easeInOutSine(float x) {
    return -(Math.cos(Math.PI * x) - 1) / 2;
  }

  private double easeInOutSineMirror(float x) {
    if (x <= 0.5) return easeInOutSine(x * 2);
    return easeInOutSine((1 - x) * 2);
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
    // make sure everything in the main sketch is wrapped inside pushMatrix and popMatrix, so the infopanel is always shown top left, even in 3D mode
    // pushMatrix in registermethod pre()
    // popMatrix in registermethod draw()
    this.parent.popMatrix();
    this.parent.popStyle();

    if (this.infoPanel.show) {
      this.parent.hint(PConstants.DISABLE_DEPTH_TEST);

      // put the parents imageMode temporarily to CORNER
      this.parent.pushStyle();
      this.parent.imageMode(PConstants.CORNER);
      //

      PGraphics overlay = this.infoPanel.overlay;
      overlay.beginDraw();
      overlay.background(0, 170);
      overlay.noStroke();
      overlay.fill(255);
      overlay.textSize(18);
      overlay.text("BPM: " + this.bpm, 10, 20);
      overlay.text("beatDuration: " + Math.floor(this.beatDuration), 10, 40);
      overlay.text("beatCount: " + this.beatCount, 10, 60);
      overlay.text("frameRate: " + (int)(this.parent.frameRate), 10, 80);

      overlay.textSize(12);
      overlay.text("every 1 beat", 12, 108);
      overlay.stroke(255);
      if (this.every_once[1]) overlay.fill(255);
      else overlay.noFill();
      overlay.ellipse(90, 105, 10, 10);
      for (int i=2; i<this.every.length; i++) {
        overlay.fill(255);
        overlay.text("every " + i + " beat", 12, 88 + i*20);
        if (this.every[i]) overlay.fill(255);
        else overlay.noFill();
        overlay.ellipse(90, 84 + i*20, 10, 10);
      }
      overlay.endDraw();
      this.parent.image(overlay, this.infoPanel.x, this.infoPanel.y); // Draw the overlay onto the main canvas
      this.parent.popStyle();
      this.parent.hint(PConstants.ENABLE_DEPTH_TEST);
    }
  }






  public void post() {
    // https://github.com/benfry/processing4/wiki/Library-Basics
    // you cant draw in post()
  }



  public void pre() {
    this.millis_runtime = this.parent.millis()-this.millis_start;
    // Assuming beatDuration is updated elsewhere when bpm changes
    this.beatCount = (int) Math.floor(this.millis_runtime/this.beatDuration);
    checkBeatPeriods();
    // make sure everything in the main sketch is wrapped inside pushMatrix and popMatrix, so the infopanel is always shown top left, even in 3D mode
    // pushMatrix in registermethod pre()
    // popMatrix in registermethod draw()
    this.parent.pushMatrix();
    this.parent.pushStyle();
  }

  private void checkBeatPeriods() {
    //skip i=0
    for (int i=1; i<this.every.length; i++) this.every[i] = this.beatCount % i==0;

    //reset the mechanism after one beat
    if (this.lastBeatCount != this.beatCount) this.doOnce = false;

    //set the corresponding 'once' boolean to true for a 1 frame period
    if (this.doOnce == false) {
      this.doOnce = true;
      this.lastBeatCount = this.beatCount;
      this.lastFrameCount = this.parent.frameCount;
      for (int i=1; i<this.every_once.length; i++) this.every_once[i] = this.every[i];
    }

    //flip the 'once' booleans immediately after one frame
    if (this.parent.frameCount != this.lastFrameCount) for (int i=1; i<this.every_once.length; i++) this.every_once[i] = false;
  }
}
