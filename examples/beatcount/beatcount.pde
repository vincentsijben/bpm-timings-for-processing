/**
 * beatcount
 * https://github.com/vincentsijben/bpm-timings-for-processing
 *
 * Show the (rounded) beatcount as text.
 * You can see how you could use the setSurfaceTitle() function as well.
 */
 
import bpm.library.*;
BeatsPerMinute bpm;

void setup() {
  size(500, 500);
  bpm = new BeatsPerMinute(this, 60);
  bpm.enableKeyPresses();
}

void draw() {
  surface.setTitle(bpm.setSurfaceTitle());
  
  background(100);
  textAlign(CENTER, CENTER);
  textSize(20);
  text(bpm.setSurfaceTitle(), width/2, 50);
  textSize(30);
  text("The beatcount is: " + (int) bpm.beatCount, width/2, height/2);
  bpm.run();
}
