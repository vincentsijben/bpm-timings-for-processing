/**
 * animatedSVG
 * https://github.com/vincentsijben/bpm-timings-for-processing
 *
 * Animate an SVG shape on specific beats. 
 * I've hidden both glasses and left foot shapes in the original SVG.
 * Created a new glasses.svg and foot.svg in vector software (Adobe Illustrator).
 * Every beat, the size of the glasses grow and shrink.
 * Every 4 beats, the dude's left foot 'taps'.
 *
 * Used a free SVG from https://www.svgrepo.com/svg/191/cool.
 */
 
import bpm.library.*;

BeatsPerMinute bpm;
PShape dude;
PShape glasses, glassesTmp;
PShape foot, footTmp;

void setup () {
  size(500, 500);
  bpm = new BeatsPerMinute(this, 120);
  bpm.enableKeyPresses();
  bpm.showInfo = true;

  shapeMode(CENTER);
  dude = loadShape("dude.svg");
  //hide the original glasses
  glassesTmp = dude.getChild("glasses");
  glassesTmp.setVisible(false);
  //hide the original foot
  footTmp = dude.getChild("foot");
  footTmp.setVisible(false);

  //exported these shapes by rightclicking the shapes in Illustrator
  glasses = loadShape("glasses.svg");
  foot = loadShape("foot.svg");
}

void draw() {
  background(200);

  pushMatrix();
  translate(width/2, height/2);
  shape(dude, 0, 0);

  float s = lerp(1, 2, bpm.easeBounce());

  //only scale the glasses, so we need pushMatrix and popMatrix
  pushMatrix();
  fill(0);
  translate(0, -125);
  scale(s);
  shape(glasses, 0, 0);
  popMatrix();

  pushMatrix();
  if (int(bpm.beatCount)%4==3) { // start one beat before every fourth
    float t = lerp(0, -15, bpm.easeBounce());
    translate(60, 225+t);
  } else {
    translate(60, 225);
  }
  shape(foot, 0, 0);
  popMatrix();
  popMatrix();
  bpm.run();
}
