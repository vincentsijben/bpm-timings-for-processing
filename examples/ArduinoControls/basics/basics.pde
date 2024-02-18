/**
 * basics
 * https://github.com/vincentsijben/bpm-timings-for-processing
 *
 *
 */

import bpm.library.*;
import processing.serial.*;
import cc.arduino.*;

Arduino arduino;
ArduinoControls ac;

void setup() {
  size(900, 500);

  //  println(Arduino.list());
  arduino = new Arduino(this, Arduino.list()[2], 57600);

  // for "built-in" potentiometer, a press returns Arduino.LOW
  // it also needs Arduino.INPUT_PULLUP instead of Arduino.INPUT
  // arduino.pinMode(7, Arduino.INPUT_PULLUP);
  
  // delay the start of the draw loop so the Arduino is in the ready state
  // (the first few frames, digitalRead returned incorrect values)
  // delay(2000);
  
  ac = new ArduinoControls(this)
    .addArduino(arduino)
    .addLED(9)
    //.addPushButton(7, '1', Arduino.LOW)
    .addPotentiometer(0, 'q')
    .showInfoPanel()
    ;
}

void draw() {
  background(100);

  //if potentiometer is turned halfways, turn LED on, else turn it off
  if (ac.getPotentiometer(0)>0.5) {
    ac.setLED(0, 1);
  } else {
    ac.setLED(0, 0);
  }
  
  //show ellipse at relative potentiometer value
  ellipse(lerp(0, width, ac.getPotentiometer(0)), 150, 130, 130);
  
  //when pushbutton is pressed fill the ellipse with a red color
  //if (ac.getPushButton(0)) {
  //  fill(255, 0, 0);
  //} else {
  //  noFill();
  //}
  
  //show ellipse at relative smoothed potentiometer value (prevents jumping around)
  ellipse(lerp(0, width, ac.getPotentiometer(0, 0.1)), 350, 50, 50);
}
