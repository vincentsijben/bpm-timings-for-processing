/**
 * genart heart
 * https://github.com/vincentsijben/bpm-timings-for-processing
 *
 * This example requires a connected Arduino board with:
 * a pushbutton at digital pin 8
 * a LED at digital port 11
 *
 * This is a replica of the created instagram video for the
 * Communication & Multimedia Design Generative Art Expo 2024
 * by https://www.instagram.com/raoulboers/
 */

import ddf.minim.*;
import bpm.library.*;
import bpm.library.arduinocontrols.*;
import bpm.library.beatsperminute.*;
import bpm.library.frequencyanalyzer.*;
import processing.serial.*;
import cc.arduino.*;

BeatsPerMinute bpm;
Minim minim;
FrequencyAnalyzer fa;
Arduino arduino;
ArduinoControls ac;

PShape heart_textured;
PShape heart_gold;
float ry;
float s;
float rotationSpeed = -0.01;

public void setup() {
  size(500, 500, P3D);

  bpm = new BeatsPerMinute(this)
    .setBPM(120)
    .setInfoPanelKey('i')
    ;

  minim = new Minim(this);
  fa = new FrequencyAnalyzer(this)
    .addMinim(minim)
    .setFile("https://github.com/vincentsijben/bpm-timings-for-processing/raw/main/assets/Ananya_-_Mornings_in_Love.mp3")
    .setAudioInputMode(AudioInputMode.AUDIO_FILE)
    .setAudioOutputMode(AudioOutputMode.MONO)
    .setInfoPanelKey('o')
    ;

  arduino = new Arduino(this, Arduino.list()[2], 57600);
  arduino.pinMode(8, Arduino.INPUT_PULLUP);
  ac = new ArduinoControls(this)
    .addArduino(arduino)
    .addLED(11, LEDMode.PWM)
    .addPushButton(8, '1', Arduino.LOW)
    ;

  heart_textured = loadShape("heart_textured.obj");
  heart_gold = loadShape("heart_gold.obj");
}

public void draw() {
  background(#4A423C);
  lights();

  //light up LED to scale of the heart
  ac.setLED(0, int(lerp(0, 255, bpm.adsr(0.2, 0.2, 0.6, 0.2))));

  pushMatrix();
  translate(width/2, height/2, 0);
  s = lerp(100, 300, bpm.adsr(0.2, 0.2, 0.6, 0.2));
  scale(s);
  rotateZ(PI);
  rotateY(ry);
  shape(heart_textured);
  popMatrix();

  pushMatrix();
  translate(width/2, height/2+100, 0);
  scale(50);
  rotateZ(PI);
  rotateY(ry);
  shape(heart_gold);
  popMatrix();

  //push and hold the pushbutton to rotate back and forth every beat
  if (ac.getPushButton(0)) {
    rotationSpeed = lerp(0.01, 0.1, bpm.easeBounce(1));
    if (bpm.every[2]) ry -= rotationSpeed;
    else ry += rotationSpeed;
  } else {
    rotationSpeed = -0.01;
    ry -= rotationSpeed;
  }

  //if frequency band 15 exceeds a raw amplitude value of 25, change the color of the wired heart
  float freqBand15 = fa.getAvgRaw(15);
  if (freqBand15 > 25) {
    heart_gold.setFill(color(random(200), random(200), 0));
  }
}
