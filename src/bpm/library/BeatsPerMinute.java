package bpm.library;


import processing.core.*;

/**
 * BPM timings for Processing..
 * 
 */
public class BeatsPerMinute {

	  // myParent is a reference to the parent sketch
	  PApplet myParent;

	  int bpm;
	  float beatDuration = 0.001f; // to prevent dividing by 0
	  /**
	   * Read the current beatcount
	   * 
	   */
	  public float beatCount = 0f;
	  /**
	   * Set this boolean to true to show the info window
	   * 
	   */
	  public boolean showInfo = false;
	  private boolean enableKeyPresses = false; //enable reading key presses

	  //helper variables to only run functions once until they reset
	  boolean doOnce = true;
	  float lastBeatCount = 0f;
	  int lastFrameCount = -1;

	  //we use these variables to be able to 'reset' the time whenever we want.
	  float millis_runtime;
	  float millis_start;

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
		 * Call this (overload) constructor in the setup() method of your 
		 * sketch to initialize and start the library.
		 * If you don't use a second argument the constructor sets the bpm to 60
		 * 
		 * @param theParent Your sketch's PApplet object
		 */
	  public BeatsPerMinute(PApplet theParent) {
	    this(theParent, 60);
	  }

	  /**
		 * Call this constructor in the setup() method of your 
		 * sketch to initialize and start the library.
		 * Set the second argument to be your starting bpm amount.
		 * 
		 * @param theParent Your sketch's PApplet object
		 * @param bpmTemp the initial beat per minute amount (integer)
		 */
	  public BeatsPerMinute(PApplet theParent, int bpmTemp) {
	    welcome();
	    myParent = theParent;
	    bpm = bpmTemp;
	    millis_start = myParent.millis();
	  }

	  private void welcome() {
	    System.out.println("##library.name## ##library.prettyVersion## by ##author.url##");
	  }

	  /**
	   * Enable or disable the library to read keyPresses and act on =,-,0 and i.
	   *
	   */
	  public void enableKeyPresses() {
	    enableKeyPresses = true;
	  }

	  /**
	   * call this method as the last method in your draw() 
	   * 
	   */
	  public void run() {
	    millis_runtime = myParent.millis()-millis_start;
	    beatDuration = 60/(float)(bpm)*1000;
	    beatCount = millis_runtime/beatDuration;

	    checkBeatPeriods();
	    checkKeyPress();
	    showInfo();
	  }

	  /**
	   * returns a string with information about bpm amount, beatcount and framerate, to be used in your surface title.
	   * @return String 
	   * 
	   */
	  public String setSurfaceTitle() {
	    return "BPM: " + bpm + " // beatCount: " + PApplet.nf((int)beatCount, 3) + " // frameRate: " + PApplet.nf((int) myParent.frameRate, 2);
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
	    float duration = beatDuration*durationInBeats;
	    float delay = beatDuration * delayInBeats;

	    if (millis_runtime < delay) return 0;
	    return (millis_runtime-delay)%duration/duration;
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


	  private void checkBeatPeriods() {
	    //skip i=0
	    for (int i=1; i<every.length; i++) every[i] = (int)(beatCount)%i==0;

	    //set the corresponding 'once' boolean to true for a 1 frame period
	    if (doOnce == false) {
	      doOnce = true;
	      lastBeatCount = (int)(beatCount);
	      lastFrameCount = myParent.frameCount;
	      for (int i=1; i<every_once.length; i++) every_once[i] = every[i];
	    }

	    //flip the 'once' booleans immediately after one frame
	    if (myParent.frameCount != lastFrameCount) for (int i=1; i<every_once.length; i++) every_once[i] = false;

	    //reset the mechanism after one beat
	    if ((int)(lastBeatCount) != (int)(beatCount)) doOnce = false;
	  }

	  /**
	   * shows an info window on top of the sketch
	   * 
	   */
	  public void showInfo() {
	    if (showInfo) {
	      //processing doesn't like transparancy in 3D
	      //see https://www.reddit.com/r/processing/comments/59r0le/problems_with_transparency_in_3d/
	      myParent.pushStyle();
	      myParent.rectMode(PConstants.CORNER);
	      myParent.textAlign(PConstants.LEFT, PConstants.CENTER);
	      myParent.fill(255, 100);
	      myParent.rect(0, 0, 190, 440);
	      myParent.fill(0);
	      myParent.textSize(20);
	      myParent.text("BPM: " + bpm, 10, 20);
	      myParent.text("beatDuration: " + Math.floor(beatDuration), 10, 40);
	      myParent.text("beatCount: " + Math.floor(beatCount), 10, 60);
	      myParent.text("normalized: " + PApplet.nf(linear(), 0, 3), 10, 80);
	      myParent.text("frameRate: " + (int)(myParent.frameRate), 10, 100);

	      myParent.textSize(12);
	      for (int i=2; i<every.length; i++) {
	        myParent.fill(0);
	        myParent.text("every " + i + " beat", 12, 88 + i*20);
	        if (every[i]) myParent.fill(0);
	        else myParent.fill(255);
	        myParent.ellipse(90, 90 + i*20, 10, 10);
	      }
	      myParent.popStyle();
	    }
	  }

	  void checkKeyPress() {
	    if (enableKeyPresses && myParent.keyPressed) {
	      myParent.keyPressed = false; //don't allow the key to be 'longpressed' immediately
	      if (myParent.key == 'i') showInfo = !showInfo;
	      if (myParent.key == '=') bpm++;
	      if (myParent.key == '-') bpm--;
	      if (myParent.key == '0') {
	        millis_start = myParent.millis();
	        // change bpm only if next keystroke is registered under 2 seconds
	        if (millis_runtime > 0 && millis_runtime < 2000) bpm = (int)((60/millis_runtime)*1000);
	      }
	    }
	  }
	}
