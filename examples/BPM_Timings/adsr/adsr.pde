/**
 * Attack Decay Sustain Release
 * https://github.com/vincentsijben/bpm-timings-for-processing
 *
 * Use the adsr() function to have more control over the (linear) progression in 1 beat. You can control:
 * - attack duration  (between 0 and 1)
 * - decay duration   (between 0 and 1)
 * - sustain level    (between 0 and 1)
 * - release duration (between 0 and 1)
 * - duration in beats
 * - delay in beats
 *
 * the sum of attack, decay and release values should not exceed 1
 *
 * circle 4 most closely resembles the shown image
 * the image shows the timeline of 1 beat, but you can use any amount for the durationInBeats value
 */

import bpm.library.beatsperminute.*;

BeatsPerMinute bpm;
float attackDuration;
float decayDuration;
float sustainLevel;
float releaseDuration;
float durationInBeats;
float delayInBeats;
float s;
PImage img;

void setup() {
  size(500, 500);
  bpm = new BeatsPerMinute(this);
  img = loadImage("adsr.png");
  imageMode(CENTER);
  textAlign(CENTER,CENTER);
  textSize(40);
  stroke(50);
  fill(0);
}

void draw() {
  background(200);

  // default linear progression in 1 beat
  text("1", width/5, height/2 - 100);
  s = lerp(0, 100, bpm.linear());
  circle(width/5, height/2, s);
  
  // fast attack, no decay, no release
  // so very fast progression (in 20% of the time of 1 beat) from 0 to 1, and keep it at 1 for the remainder of the time
  text("2", width/5 * 2, height/2 - 100);
  attackDuration = 0.2;
  decayDuration = 0;
  sustainLevel = 1;
  releaseDuration = 0;
  durationInBeats = 1;
  s = lerp(0, 100, bpm.adsr(attackDuration, decayDuration, sustainLevel, releaseDuration, durationInBeats));
  // s = lerp(0, 100, bpm.adsr(0.2)); // this has the same effect because all other values are default values
  circle(width/5 * 2, height/2, s);
  
  // slow attack, fast release
  // slowly progress (in 80% of the time of 1 beat) from 0 to 1, and quickly progress from 1 to 0
  text("3", width/5 * 3, height/2 - 100);
  attackDuration = 0.8;
  decayDuration = 0;
  sustainLevel = 1;
  releaseDuration = 0.2;
  durationInBeats = 1;
  s = lerp(0, 100, bpm.adsr(attackDuration, decayDuration, sustainLevel, releaseDuration, durationInBeats));
  circle(width/5 * 3, height/2, s);

  // fast attack, fast decay to 0.5, fast release, with duration of 2 beats and start with delay of 1 beat
  // quickly progress from 0 to 1, then quickly progress to 0.5 and hold this for 40% of the time of 2 beats, then quickly progress from 0.5 to 0 in 20% of the time of 2 beats
  text("4", width/5 * 4, height/2 - 100);
  attackDuration = 0.2;
  decayDuration = 0.2;
  sustainLevel = 0.5;
  releaseDuration = 0.2;
  durationInBeats = 2;
  delayInBeats = 1;
  s = lerp(0, 100, bpm.adsr(attackDuration, decayDuration, sustainLevel, releaseDuration, durationInBeats, delayInBeats));
  circle(width/5 * 4, height/2, s);

  image(img, width/2, 400, 67*8, 30*8);
}
