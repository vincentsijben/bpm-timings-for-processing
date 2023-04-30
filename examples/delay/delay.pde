/**
 * delay
 * by Vincent Sijben
 *
 * Show 4 rows of 3 circles which animate every 3 beats, some with a delay. Each row uses one of the core timing functions.
 * 1st row: linear(), a linear progression value from 0 to 1 in 3 beats
 * 2nd row: linearBounce(), a linear progression value from 0 to 1 and back to 0 in 3 beats
 * 3rd row: ease(), an 'eased' progression value from 0 to 1 in 3 beats
 * 4th row: easeBounce(), an 'eased' progression value from 0 to 1 and back to 0 in 3 beats
 * Each row shows 3 circles. The first has no delay, the second has a delay of 1 beat, the last one a delay of 2 beats
 */
 
import bpm.library.*;

BeatsPerMinute bpm;

float radius;

void setup () {
  size(500, 500);
  bpm = new BeatsPerMinute(this, 60);
  //bpm.enableKeyPresses();
  bpm.showInfo = true;
}

void draw() {
  background(200);

  radius = lerp(0, 100, bpm.linear(3));
  ellipse(width/4*1, height/5*1, radius, radius);

  radius = lerp(0, 100, bpm.linear(3, 1));
  ellipse(width/4*2, height/5*1, radius, radius);

  radius = lerp(0, 100, bpm.linear(3, 2));
  ellipse(width/4*3, height/5*1, radius, radius);

  radius = lerp(0, 100, bpm.linearBounce(3));
  ellipse(width/4*1, height/5*2, radius, radius);

  radius = lerp(0, 100, bpm.linearBounce(3, 1));
  ellipse(width/4*2, height/5*2, radius, radius);

  radius = lerp(0, 100, bpm.linearBounce(3, 2));
  ellipse(width/4*3, height/5*2, radius, radius);

  radius = lerp(0, 100, bpm.ease(3));
  ellipse(width/4*1, height/5*3, radius, radius);

  radius = lerp(0, 100, bpm.ease(3, 1));
  ellipse(width/4*2, height/5*3, radius, radius);

  radius = lerp(0, 100, bpm.ease(3, 2));
  ellipse(width/4*3, height/5*3, radius, radius);

  radius = lerp(0, 100, bpm.easeBounce(3));
  ellipse(width/4*1, height/5*4, radius, radius);

  radius = lerp(0, 100, bpm.easeBounce(3, 1));
  ellipse(width/4*2, height/5*4, radius, radius);

  radius = lerp(0, 100, bpm.easeBounce(3, 2));
  ellipse(width/4*3, height/5*4, radius, radius);

  bpm.run();
}
