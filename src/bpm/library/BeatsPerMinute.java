package bpm.library;


import processing.core.*;
import java.util.Map;

/**
 * This is a template class and can be used to start a new processing Library.
 * Make sure you rename this class as well as the name of the example package 'template' 
 * to your own Library naming convention.
 * 
 * (the tag example followed by the name of an example included in folder 'examples' will
 * automatically include the example in the javadoc.)
 *
 * @example Hello 
 */

public class BeatsPerMinute {
	
	// myParent is a reference to the parent sketch
	PApplet myParent;

	int myVariable = 0;
	
	int bpm;
	  float beatDuration;
	  public float beatCount;
	  public boolean showInfo;

	  //helper variables to only run functions once until they reset
	  boolean doOnce;
	  float lastBeatCount;
	  int lastFrameCount;

	  //we use these variables to be able to 'reset' the time whenever we want.
	  float millis_runtime;
	  float millis_start;

	  //helper booleans that turn true every n beats. Added one extra upfront that isn't used, so the user could do every[3] which means 3rd beat.
	  boolean[] every = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
	  //helper booleans that turn true every n beats for 1 frame.
	  boolean[] every_once = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
	
	public final static String VERSION = "##library.prettyVersion##";
	

	/**
	 * a Constructor, usually called in the setup() method in your sketch to
	 * initialize and start the Library.
	 * 
	 * @example Hello
	 * @param theParent the parent PApplet
	 */
	public BeatsPerMinute(PApplet theParent) {
		myParent = theParent;
		welcome();
	}
	
	public BeatsPerMinute(PApplet theParent, int bpmTemp) {
		welcome();
		myParent = theParent;
	    bpm = bpmTemp;
	    beatDuration = 0.001f; // to prevent dividing by 0
	    beatCount = 0f;
	    showInfo = false;
	    doOnce = false;
	    lastBeatCount = 0f;
	    lastFrameCount = -1;
	    millis_start = myParent.millis();
	}
	
	private void welcome() {
		System.out.println("##library.name## ##library.prettyVersion## by ##author##");
	}
	
	public void run() {
	    millis_runtime = myParent.millis()-millis_start;
	    beatDuration = 60/(float)(bpm)*1000;
	    beatCount = millis_runtime/beatDuration;

	    checkBeatPeriods();
	    checkKeyPress();
	    showInfo();
//	    PApplet.getSurface().setTitle();
	    //myParent.surface.setTitle("BPM: " + int(bpm) + " // beatCount: " + nf((int)(beatCount), 3) + " // frameRate: " + nf(int(frameRate), 2));
	  }
	
	public String setSurfaceTitle() {
		return "BPM: " + (int)bpm + " // beatCount: " + PApplet.nf((int)beatCount, 3) + " // frameRate: " + PApplet.nf((int) myParent.frameRate, 2);
	
	}

	  public float beatDurationNormalizedLinear(float durationInBeats) {
	    // returns a normalized 'linear' progress value for 1 beat duration (ranges from 0 to 1)
	    return millis_runtime%(beatDuration*durationInBeats)/(beatDuration*durationInBeats);
	  }
	  public float beatDurationNormalizedSmooth(float durationInBeats) {
	    // returns a normalized 'eased' progress value for 1 beat duration (ranges from 0 to 1)
	    return PApplet.sin(PApplet.lerp(0, (float)Math.PI/2, beatDurationNormalizedLinear(durationInBeats)));
	  }
	  public float beatDurationNormalizedStartEndStartLinear(float durationInBeats) {
	    // returns a normalized 'linear' progress value for 1 beat duration (ranges from 0 to 1 to 0)
	    if (beatDurationNormalizedLinear(durationInBeats) < 0.5) return PApplet.map(beatDurationNormalizedLinear(durationInBeats), 0, 0.5f, 1, 0);
	    else return PApplet.map(beatDurationNormalizedLinear(durationInBeats), 0.5f, 1f, 0f, 1f);
	  }
	  public float beatDurationNormalizedStartEndStartSmooth(float durationInBeats) {
	    // returns a normalized 'eased' progress value for 1 beat duration (ranges from 0 to 1 to 0)
	    return PApplet.sin(PApplet.lerp(0, (float) Math.PI, beatDurationNormalizedLinear(durationInBeats)));
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

	  public void showInfo() {
	    if (showInfo) {
	      //processing doesn't like transparancy in 3D
	      //see https://www.reddit.com/r/processing/comments/59r0le/problems_with_transparency_in_3d/
	      myParent.pushStyle();
	      myParent.textAlign(PConstants.LEFT, PConstants.CENTER);
	      myParent.fill(255, 100);
	      myParent.rect(0, 0, 180, 440);
	      myParent.fill(0);
	      myParent.textSize(20);
	      myParent.text("BPM: " + bpm, 10, 20);
	      myParent.text("beatDuration: " + Math.floor(beatDuration), 10, 40);
	      myParent.text("beatCount: " + Math.floor(beatCount), 10, 60);
	      myParent.text("normalized: " + PApplet.nf(beatDurationNormalizedLinear(1), 0, 3), 10, 80);
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
	    if (myParent.keyPressed) {
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
	
	/**
	 * return the version of the Library.
	 * 
	 * @return String
	 */
	public static String version() {
		return VERSION;
	}
}

