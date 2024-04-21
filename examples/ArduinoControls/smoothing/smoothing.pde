/**
 * smoothing
 * a demo sketch showing how to use smoothed values for potentiometers. Also includes a pushbutton and an LED.
 * https://github.com/vincentsijben/bpm-timings-for-processing
 *
 * This example requires a connected Arduino board with:
 * a pushbutton at digital port 7
 * a potentiometer at A0
 * a LED at digital port 9
 *
 * for some pushbuttons like https://www.benselectronics.nl/drukknop-aluminium-zwart-met-schroefterminal.html, a press returns Arduino.LOW
 * they also need Arduino.INPUT_PULLUP instead of Arduino.INPUT
 *
 * for most default small pushbuttons like https://www.benselectronics.nl/push-button-set-met-kap-5-stuks.html, a press returns Arduino.HIGH
 *
 */

import bpm.library.arduinocontrols.*;
import processing.serial.*;
import cc.arduino.*;

Arduino arduino;
ArduinoControls ac;

void setup() {
  size(900, 500);

  println(Arduino.list());
  arduino = new Arduino(this, Arduino.list()[2], 57600);
  arduino.pinMode(7, Arduino.INPUT_PULLUP);
  arduino.pinMode(9, Arduino.OUTPUT);

  ac = new ArduinoControls(this)
    .addArduino(arduino)
    .addLED(9, LEDMode.PWM)
    .addPushButton(7, '1', Arduino.LOW)
    .addPotentiometer(0, 'q')
    .showInfoPanel()
    ;

  // delay the start of the draw loop so the Arduino is in the ready state
  // because the first few frames, digitalRead returned incorrect values
  delay(2000);
}

void draw() {
  background(50);

  // use the potentiometers normalized value to control the LED brightness
  ac.setLED(0, int(lerp(0, 255, ac.getPotentiometer(0))));

  //show circle at relative potentiometer value
  circle(lerp(0, width, ac.getPotentiometer(0)), height/4, 200);

  // when pushbutton is pressed turn the circle red. Change it to ac.getPushButtonOnce(0) and the circle will be filled red for 1 frame.
  if (ac.getPushButton(0)) fill(255, 0, 0);
  else noFill();

  //show ellipse at relative smoothed potentiometer value (prevents jumping around)
  circle(lerp(0, width, ac.getPotentiometer(0, 0.1)), height/4*3, 100);
}
