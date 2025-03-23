/**
 * randomGridSpots
 * https://github.com/vincentsijben/bpm-timings-for-processing
 *
 * Shows a 9 by 9 grid of squares that rotates every 8 beats.
 * Every beat, 1 random square pulsates on the beat, starting each first beat with a red color.
 * Uses the boolean every_once[8] to turn true every 8 beats for the duration of 1 frame.
 * Also toggles the fill color of the circle every 2 beats.
 * Uses the boolean every[4] to use a red fill color every 4 beats for the duration of 1 beat.
 */
 
import nl.genart.bpm.beatsperminute.*;
BeatsPerMinute bpm;

color[] colors = {#2c69f3, #fefdfa, #231e10};
int[] randomRect = {0, 0}; //store an x and y location for a random grid spot
int lastBeatCount = 0;
float rotate = 0;

void setup () {

  size(500, 500);
  rectMode(CENTER);
  noStroke();
  bpm = new BeatsPerMinute(this)
    //.setBPM(120)
    //.showInfoPanel()
    //.setInfoPanelY(200)
    //.setInfoPanelKey('o')
    //.disableKeyPress()
    ;

}

void draw() {
  background(#f7c12b);
  for (int i=0; i<9; i++) {
    for (int j=0; j<9; j++) {

      pushMatrix();
      float itemIndex = i*1000 + j*100;
      float x = i * 50 + 50;
      float y = j * 50 + 50;
      translate(x, y);
      rotate(radians(rotate));
      float sizeRect = 35;
      int colorChoice = int(lerp(0,3,noise(itemIndex)));

      fill(colors[colorChoice]);

      if (randomRect[0]==i && randomRect[1]==j) {
        if (bpm.every[4]) { 
          fill(#FF0000);
        }
        sizeRect = lerp(1, 40, bpm.linear());
      }
      rect(0, 0, sizeRect, sizeRect);
      popMatrix();
    }
  }
  
  if (bpm.every_once[4]) {
    randomRect[0] = int(random(9));
    randomRect[1] = int(random(9));
  }

  if (bpm.every_once[8]) {
    rotate = (rotate + 45) % 90;
  }

}
