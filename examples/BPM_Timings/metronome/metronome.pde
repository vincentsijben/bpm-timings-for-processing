/**
 * metronome
 * https://github.com/vincentsijben/bpm-timings-for-processing
 *
 * Shows a metronome (simple rectangle) that rotates back and forth every beat.
 * Also sets showInfo to true, to see the BPM info window.
 */

import bpm.library.beatsperminute.*;
BeatsPerMinute bpm;

void setup() {
  size(500, 500);
  bpm = new BeatsPerMinute(this)
    //.setBPM(120)
    //.showInfoPanel()
    //.setInfoPanelY(200)
    //.setInfoPanelKey('o')
    //.disableKeyPress()
    ;
}

void draw() {
  background(100);
  metronome();
}

void metronome() {

  //not really necessary here, but always best practice to use pushMatrix whenever you change the origin with translate()
  pushMatrix();

  //set the rotation point to 250,400
  translate(width/2, height-50);

  //set an angle to change from -45 degrees to 45 and back within 1 beat
  float angle = map(bpm.linearBounce(), 0, 1, -45, 45);
  rotate(radians(angle));

  //draw an ellipse just to clarify the rotation point
  stroke(255);
  fill(200, 0, 0);
  ellipse(0, 0, 8, 8);

  //position the rectangle around the rotation point
  noFill();
  rectMode(CORNER);
  rect(-20, -200, 40, 200);

  popMatrix();
}
