/**
 * basics
 * https://github.com/vincentsijben/bpm-timings-for-processing
 *
 *
 */

// Import the library to your sketch
import bpm.library.frequencyanalyzer.*;

// Import the minim library
import ddf.minim.*;
import ddf.minim.analysis.*;

Minim minim;
FrequencyAnalyzer fa;

void setup() {
  size(500, 500);

  minim = new Minim(this);

  fa = new FrequencyAnalyzer(this)
    .addMinim(minim)
    .setFile("https://github.com/vincentsijben/bpm-timings-for-processing/raw/main/assets/infraction_music_-_ritmo.mp3")
    .setMode(InputMode.FILE)
    //.setMode(InputMode.MONO)
    ;
}

void draw() {
  background(50);

  circle(width/4*1, height/2, lerp(0, height, fa.getAvg(0)));
  circle(width/4*2, height/2, lerp(0, height, fa.getAvg(10)));
  circle(width/4*3, height/2, lerp(0, height, fa.getAvg(20)));
}
