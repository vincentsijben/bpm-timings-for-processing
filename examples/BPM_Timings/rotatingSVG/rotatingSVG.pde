/**
 * rotating SVG
 * https://github.com/vincentsijben/bpm-timings-for-processing
 *
 * Rotate and scale multiple shapes on specific beats.
 * The SVG is a basic shape created in vector software (Adobe Illustrator).
 * In 4 beats, an SVG shape is drawn 15 times and is rotated and scaled
 * In 2 beats, 15 rectangles are rotated and scaled
 * The sketch alternates every 4 beats between the SVG and rectangle shapes.
 * Credits to https://github.com/Mick-Willemsen for creating this example.
 */
import nl.genart.bpm.beatsperminute.*;

BeatsPerMinute bpm;
PShape star;
boolean flipped = false;
float sStar = 0;
float sRect = 0;
float angle = 0;

void setup() {
  size(500, 500);

  bpm = new BeatsPerMinute(this);

  star = loadShape("star.svg");
  star.disableStyle();

  rectMode(CENTER);
  shapeMode(CENTER);
  stroke(50);
  strokeWeight(3);
}

void draw() {
  background(50, 50);

  sStar = lerp(0.2, 1.1, bpm.easeBounce(4));
  sRect = lerp(0.2, 1, bpm.easeBounce(2));
  if (bpm.every_once[4]) flipped = !flipped;

  pushMatrix();
  translate(width/2, height/2);

  int amount = 15;
  for (int i=0; i<amount; i++) {

    rotate(radians(angle));

    if (flipped) {
      scale(sRect);
      float r = map(i, 0, amount, 100, 255);
      float g = 0;
      float b = map(i, 0, amount, 255, 100);
      fill( r, g, b);
      rect(0, 0, width, height, width/5);
    } else {
      scale(sStar);
      float r = 0;
      float g = map(i, 0, amount, 0, 255);
      float b = 200;
      fill(r, g, b);
      shape(star, 0, 0, width, height);
    }
  }
  popMatrix();

  angle += 0.1;
}
