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
FrequencyAnalyzer fa;

void setup() {

  size(900, 500);

  // change [2] to your usb port
  arduino = new Arduino(this, Arduino.list()[2], 57600);

  ac = new ArduinoControls(this)
    .addArduino(arduino)
    .addLED(9)
    .addPotentiometer(0, 'q')
    //.showInfoPanel()
    .setInfoPanelY(100)
    ;
  bpm = new BeatsPerMinute(this)
    .setBPM(110)
    //.showInfoPanel()
    .setInfoPanelKey('o')
    ;
  fa = new FrequencyAnalyzer(this)
    .setFile("https://github.com/vincentsijben/bpm-timings-for-processing/raw/main/assets/infraction-music-ritmo.mp3")
    .setMode(InputMode.FILE)
    //.showInfoPanel()
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
  circle(width/4*1, height/2, lerp(0, height, fa.getAvg(0)));
  circle(width/4*2, height/2, lerp(0, height, fa.getAvg(10)));
  circle(width/4*3, height/2, lerp(0, height, fa.getAvg(20)));

  // bpm: grow circle in 1 beat
  noStroke();
  fill(255, 199, 100);
  circle(width/4*1, height/4*3, lerp(0, 100, bpm.linear()));
  circle(width/4*2, height/4*3, lerp(0, 100, bpm.linear(2)));
  circle(width/4*3, height/4*3, lerp(0, 100, bpm.linear(4)));
}
