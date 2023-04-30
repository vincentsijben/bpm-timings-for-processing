/**
 * metronome
 * by Vincent Sijben
 *
 * Shows a metronome (simple rectangle) that rotates back and forth every beat.
 * Also sets showInfo to true, to see the BPM info window.
 */
 
import bpm.library.*;
BeatsPerMinute bpm;

void setup() {
  size(500, 500);
  bpm = new BeatsPerMinute(this, 60);
  bpm.showInfo = true;
}

void draw() {
  background(100);
  metronome();
  bpm.run();
}

void metronome(){

  //not really necessary here, but always best practice to use pushMatrix whenever you change the origin with translate()
  pushMatrix();
  
  //set the rotation point to 250,400
  translate(width/2, height-50);
  
  //set an angle to change from -45 degrees to 45 and back within 1 beat
  float angle = map(bpm.linearBounce(), 0, 1, -45, 45);
  rotate(radians(angle));

  //draw an ellipse just to clarify the rotation point
  stroke(255);
  fill(200,0,0);
  ellipse(0,0,8,8);

  //position the rectangle around the rotation point
  noFill();
  rectMode(CORNER);
  rect(-20, -200, 40, 200);
    
  popMatrix();
 
}
