import ddf.minim.*;
import bpm.library.*;
import bpm.library.arduinocontrols.*;
import bpm.library.beatsperminute.*;
import bpm.library.frequencyanalyzer.*;

BeatsPerMinute bpm;
Minim minim;
FrequencyAnalyzer fa;

PShape heart_textured;
PShape heart_gold;
float ry;
float s;
float rotationSpeed =0;

public void setup() {
  size(500, 500, P3D);

  bpm = new BeatsPerMinute(this)
    .setBPM(120)
    .setInfoPanelKey('i')
    ;

  minim = new Minim(this);
  fa = new FrequencyAnalyzer(this)
    .addMinim(minim)
    .setFile("https://github.com/vincentsijben/bpm-timings-for-processing/raw/main/assets/Ananya - Mornings in Love.mp3")
    .setAudioInputMode(AudioInputMode.AUDIO_FILE)
    .setAudioOutputMode(AudioOutputMode.MONO)
    .setInfoPanelKey('o')
    ;

  heart_textured = loadShape("hearttextured.obj");
  heart_gold = loadShape("heartgold.obj");
}

public void draw() {
  background(#4A423C);
  lights();

  if (bpm.every_once[2]) {
    heart_gold.setFill(color(random(200), random(200), 0));
  }

  translate(width/2, height/2, 0);

  s = lerp(300, 600, bpm.adsr(0.2, 0.2, 0.6, 0.2));
  scale(s);
  rotateZ(PI);
  rotateY(ry);
  shape(heart_textured);

  translate(-30, +150, 0);
  scale(140);
  rotateZ(PI);
  rotateY(ry);
  shape(heart_gold);

  rotationSpeed = lerp(0.01, 0.1, bpm.easeBounce(1));
  if (bpm.every[2]) ry -= rotationSpeed;
  else ry += rotationSpeed;

}
