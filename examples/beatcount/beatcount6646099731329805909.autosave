import bpm.library.*;
BeatsPerMinute bpm;

void setup() {
  size(400, 400);
  bpm = new BeatsPerMinute(this, 60);
}

void draw() {
  surface.setTitle(bpm.setSurfaceTitle());
  background(100);
  textAlign(CENTER, CENTER);
  textSize(30);
  text("The beatcount is: " + (int) bpm.beatCount, width/2, height/2);
  bpm.run();
}
