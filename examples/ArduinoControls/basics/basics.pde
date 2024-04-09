/**
 * basics
 * a demo sketch for using the ArduinoControls class. It uses the onboard LED (digital port 13).
 * https://github.com/vincentsijben/bpm-timings-for-processing
 *
 * This example requires a connected Arduino board with:
 * 1 LED at digital ports 13
 *
 */
 
import bpm.library.arduinocontrols.*;
import processing.serial.*;
import cc.arduino.*;

Arduino arduino;
ArduinoControls ac;

void setup() {
  size(500, 500);

  println(Arduino.list());
  arduino = new Arduino(this, Arduino.list()[2], 57600);
  
  ac = new ArduinoControls(this)
    .addArduino(arduino)
    .addLED(13)
    ;
}

void draw() {
  background(50);

  // if mouse position is to the left, turn LED on, else turn it off
  if (mouseX < width/2) ac.setLED(0, 1);
  else ac.setLED(0, 0);

  text("on", width/4, height/2);
  text("off", width/4*3, height/2);
  stroke(255, 0, 0);
  line(width/2, 0, width/2, height);
}
