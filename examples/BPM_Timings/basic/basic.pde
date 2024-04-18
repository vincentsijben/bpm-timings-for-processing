/**
 * basic
 * https://github.com/vincentsijben/bpm-timings-for-processing
 *
 * Animate a circle on each beat.
 */

import bpm.library.beatsperminute.*;
BeatsPerMinute bpm;

void setup() {
  size(500, 500);
  bpm = new BeatsPerMinute(this);
  bpm.setBPM(30);

}

void draw() {
  background(50);
  circle(width/2, height/2, bpm.easeBounce()*500);
}
