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
    .setAudioInputMode(AudioInputMode.AUDIO_FILE)
    ;
}


void draw() {
  background(0);
 
  fill(0, 200, 0);
  circle(width/4*1, height/2, fa.getAvgRaw(0)*10);
  circle(width/4*2, height/2, fa.getAvgRaw(15)*10);
  circle(width/4*3, height/2, fa.getAvgRaw(29)*10);
  
  fill(255);
  textSize(20);
  textAlign(CENTER);
  text(round(fa.getAvgRaw(0)), width/4*1, height/4*3);
  text(round(fa.getAvgRaw(15)), width/4*2, height/4*3);
  text(round(fa.getAvgRaw(29)), width/4*3, height/4*3);
}
