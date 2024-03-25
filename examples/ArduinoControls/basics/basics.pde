// Import the library to your sketch
import bpm.library.arduinocontrols.*;

// Import the arduino and serial libraries
import processing.serial.*;
import cc.arduino.*;

Arduino arduino;
ArduinoControls ac;

void setup() {
  size(500, 500);

  println(Arduino.list());
  arduino = new Arduino(this, Arduino.list()[2], 57600);
  arduino.pinMode(13, Arduino.OUTPUT);

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
