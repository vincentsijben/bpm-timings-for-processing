/**
 * basics
 * https://github.com/vincentsijben/bpm-timings-for-processing
 *
 *
 */
 
import ddf.minim.*;
import ddf.minim.analysis.*;
import bpm.library.frequencyanalyzer.*;

FrequencyAnalyzer fa;
PGraphics pg;
ArrayList<Circle> circles = new ArrayList<Circle>();

void setup() {
  size(500, 500);

  fa = new FrequencyAnalyzer(this)
    .setMode(InputMode.MONO)
    .showInfoPanel()
    ;

  pg = createGraphics(width, height);
  for (int i = 0; i < fa.getBands(); i++) {
    circles.add(new Circle(i));
  }
}

void draw() {
  drawCircles();

  stroke(200);
  strokeWeight(5);
  noFill();
  
  // get the raw value for band 11:
  if (fa.getAvgRaw(10)>40) {
    circle(width/4, height-100, 100);
  }

  // get the normalized value for band 11:
  if (fa.getAvg(10)>0.5) {
    circle(width/2, height-100, 100);
  }

  // get the normalized value for band 5 using 20 as the max mapped value:
  if (fa.getAvg(4, 20)>0.5) {
    circle(width/4*3, height-100, 100);
  }
}
