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

  
  //  print("Serialports: ");
  //  println(Arduino.list());
    arduino = new Arduino(this, Arduino.list()[2], 57600);

  //  //arduino.pinMode(7, Arduino.INPUT_PULLUP);
  //  //arduino.pinMode(6, Arduino.INPUT_PULLUP);

    //arduinoo.pinMode(2, Arduino.INPUT);
  //  //arduinoo.pinMode(10, Arduino.INPUT_PULLUP);
  //  //arduinoo.pinMode(9, Arduino.OUTPUT);
  //  //arduinoo.pinMode(8, Arduino.INPUT_PULLUP);
  //  //arduinoo.pinMode(7, Arduino.OUTPUT);
  //  //arduinoo.pinMode(6, Arduino.OUTPUT);
  //  // delay the start of the draw loop so the Arduino is in the ready state
  //  // (the first few frames, digitalRead returned incorrect values)
  //  delay(2000);
  //}
  ac = new ArduinoControls(this)
    .addArduino(arduino)
    //.addLED(7, false)
    .addLED(9)
    //.addLED(10, true)
    .addPushButton(2, '1', Arduino.HIGH)
    //.addPushButton(8, '2', Arduino.HIGH)
    //.addPushButton(9, '3', Arduino.LOW)
    //.addPushButton(10, '4', Arduino.HIGH)
    .addPotentiometer(0, 'q')
    //.addPotentiometer(1, 'w')
    //.addPotentiometer(2, 'e', 2, 945)
    .showInfoPanel()
    .setInfoPanelY(200)
    //.setInfoPanelKey('o')
    //.disableKeyPress()
    ;

}

void draw() {

  background(100);


  
  //ac.setLED(1, (int) map(mouseX,0,width,0,255));
  //ac.setLED(2, (int) map(mouseX,0,width,0,255));
  if (ac.getPotentiometer(0)>0.5){
    ac.setLED(0, 1);
  } else {
  ac.setLED(0, 0);
}
  ellipse(lerp(0, width, ac.getPotentiometer(0)), 150, 130, 130);
  if (ac.getPushButton(0)) {
    fill(255,0,0);
  } else {
    noFill();
  }
  ellipse(lerp(0, width, ac.getPotentiometer(0, 0.1)), 350, 50, 50);
  
}
