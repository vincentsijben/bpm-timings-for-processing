/**
 * startercode
 * https://github.com/vincentsijben/bpm-timings-for-processing
 *
 * make sure you have the following libraries installed:
 * - Arduino
 * - Minim
 * You can get them from the Processing IDE: Sketch > Import Library > Add Library
 * and search for "Arduino" and "Minim"
 */
 
import bpm.library.beatsperminute.*;
import bpm.library.frequencyanalyzer.*;
import bpm.library.arduinocontrols.*;
import ddf.minim.*;
import ddf.minim.analysis.*;
import processing.serial.*;
import cc.arduino.*;

Arduino arduino;
ArduinoControls ac;
BeatsPerMinute bpm;
Minim minim;
FrequencyAnalyzer fa;

void setup() {

  size(900, 500);

  // change [2] to your usb port
  arduino = new Arduino(this, Arduino.list()[2], 57600);
  minim = new Minim(this);

  ac = new ArduinoControls(this)
    .addArduino(arduino)
    .addLED(9)
    .addPotentiometer(0, 'q')
    .setInfoPanelY(100)
    ;
  bpm = new BeatsPerMinute(this)
    .setBPM(110)
    .setInfoPanelKey('o')
    ;
  fa = new FrequencyAnalyzer(this)
    .addMinim(minim)
    .setFile("https://github.com/vincentsijben/bpm-timings-for-processing/raw/main/assets/infraction_music_-_ritmo.mp3")
    .setAudioInputMode(AudioInputMode.AUDIO_FILE)
    .setAudioOutputMode(AudioOutputMode.MONO)
    .setInfoPanelKey('p')
    ;
}

void draw() {
  background(50);

  // ac: control 2 ellipses with a potentiometer and an LED with a pushbutton
  if (ac.getPotentiometer(0)>0.5) ac.setLED(0, 1);
  else ac.setLED(0, 0);
  fill(200);
  circle(lerp(0, width, ac.getPotentiometer(0)), height/4 - 20, 30);
  circle(lerp(0, width, ac.getPotentiometer(0, 0.1)), height/4 + 20, 30);

  // fa: analyzing 3 frequency band volumes
  stroke(91, 244, 233);
  strokeWeight(3);
  noFill();
  circle(width/4*1, height/2, fa.getAvgRaw(0) * 10);
  circle(width/4*2, height/2, fa.getAvgRaw(10)* 10);
  circle(width/4*3, height/2, fa.getAvgRaw(20)* 10);

  // bpm: grow circle in 1 beat
  noStroke();
  fill(255, 199, 100);
  circle(width/4*1, height/4*3, lerp(0, 100, bpm.linear()));
  circle(width/4*2, height/4*3, lerp(0, 100, bpm.linear(2)));
  circle(width/4*3, height/4*3, lerp(0, 100, bpm.linear(4)));
}
