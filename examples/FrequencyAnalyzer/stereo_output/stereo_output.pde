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
    .setFile("example-stereo.mp3")
    .setAudioInputMode(AudioInputMode.AUDIO_FILE)
    .setAudioOutputMode(AudioOutputMode.STEREO)
    ;
}


void draw() {
  background(50);

  circle(width/3, height/2, fa.getAvgRawLeft(0)*30);
  circle(width/3*2, height/2, fa.getAvgRawRight(0)*30);
}
