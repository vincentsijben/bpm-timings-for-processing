/**
 * beatcount
 * https://github.com/vincentsijben/bpm-timings-for-processing
 *
 * Show the (rounded) beatcount as text.
 * You can see how you could use the setSurfaceTitle() function as well.
 */

import bpm.library.beatsperminute.*;
BeatsPerMinute bpm;

void setup() {
  size(500, 500);
  bpm = new BeatsPerMinute(this)
    //.showInfoPanel()
    ;
}

void draw() {
  surface.setTitle(bpm.getSurfaceTitle());

  background(100);
  textAlign(CENTER, CENTER);
  textSize(20);
  text(bpm.getSurfaceTitle(), width/2, 50);
  textSize(30);
  text("The beatcount is: " + (int) bpm.getBeatCount(), width/2, height/2);
}
