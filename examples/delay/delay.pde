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
