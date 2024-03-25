/**
 * controls
 * https://github.com/vincentsijben/bpm-timings-for-processing
 *
 * This example requires a connected Arduino board with:
 * a pushbutton at digital pin 7 
 * a potentiometer at A0
 * a LED at digital port 9
 */

import bpm.library.arduinocontrols.*;
import processing.serial.*;
import cc.arduino.*;

Arduino arduino;
ArduinoControls ac;

void setup() {
  size(900, 500);

  //  println(Arduino.list());
  arduino = new Arduino(this, Arduino.list()[2], 57600);
  // for "built-in" potentiometer like https://www.benselectronics.nl/potentiometer-10k-inbouw.html, a press returns Arduino.LOW
  // it also needs Arduino.INPUT_PULLUP instead of Arduino.INPUT
  arduino.pinMode(7, Arduino.INPUT_PULLUP);
  arduino.pinMode(9, Arduino.OUTPUT);

  ac = new ArduinoControls(this)
    .addArduino(arduino)
    .addLED(9)
    .addPushButton(7, '1', Arduino.LOW)
    .addPotentiometer(0, 'q')
    .showInfoPanel()
    ;

  // delay the start of the draw loop so the Arduino is in the ready state
  // (the first few frames, digitalRead returned incorrect values)
  delay(2000);
}

void draw() {
  background(100);

  // if potentiometer is turned halfways, turn LED on, else turn it off
  if (ac.getPotentiometer(0)>0.5) ac.setLED(0, 1);
  else ac.setLED(0, 0);

  //show circle at relative potentiometer value
  circle(lerp(0, width, ac.getPotentiometer(0)), height/4, 200);

  // when pushbutton is pressed turn the circle red
  if (ac.getPushButton(0)) fill(255, 0, 0);
  else noFill();
  
  //show ellipse at relative smoothed potentiometer value (prevents jumping around)
  circle(lerp(0, width, ac.getPotentiometer(0, 0.1)), height/4*3, 100);
}
