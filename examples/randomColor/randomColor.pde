/**
 * randomColor
 * https://github.com/vincentsijben/bpm-timings-for-processing
 *
 * Show a random background color by setting a new (random) seed every 4 beats.
 * Uses the boolean every_once[4] to turn true every 4 beats for the duration of 1 frame.
 * Also toggles the fill color of the circle every 2 beats.
 * Uses the boolean every[2] to show a text every 2 beats for the duration of 1 beat.
 */
 
import bpm.library.*;

BeatsPerMinute bpm;

int seed = 1;
boolean fillBlack = true;

void setup () {
  size(500, 500);
  bpm = new BeatsPerMinute(this, 60);
  bpm.enableKeyPresses();
  bpm.showInfo = true;

  textAlign(CENTER, CENTER);
  textSize(40);
  fill(100);
}

void draw() {
  background(200);

  // switch to a random background color every 4 beats
  if (bpm.every_once[4]) seed = int(random(100000));
  randomSeed(seed);
  background(random(255), random(255), random(255));

  // switch fill color to black/white every 2 beats
  if (bpm.every_once[2]) fillBlack = !fillBlack;
  if (fillBlack) fill(0);
  else fill(255);

  ellipse(width/2, height/2, 200, 200);

  // show text every 2 beats for the duration of 1 beat
  if (bpm.every[2]) text("yeah!", width/2, height-50);

  bpm.run();
}
