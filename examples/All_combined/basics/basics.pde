/**
 * basics
 * https://github.com/vincentsijben/bpm-timings-for-processing
 *
 *
 */
import ddf.minim.*;
import ddf.minim.analysis.*;
import bpm.library.*;
import processing.serial.*;
import cc.arduino.*;

Arduino arduino;
ArduinoControls ac;
BeatsPerMinute bpm;
FrequencyAnalyzer fa;
float radius;
PGraphics pg;
ArrayList<Circle> circles = new ArrayList<Circle>();
color col = 50;

void setup() {
  size(900, 500);
  frameRate(200);

  arduino = new Arduino(this, Arduino.list()[2], 57600);
  ac = new ArduinoControls(this)
    .addArduino(arduino)
    .addLED(9)
    .addPotentiometer(0, 'q')
    .showInfoPanel()
    ;
  bpm = new BeatsPerMinute(this)
    //.setBPM(120)
    //.showInfoPanel()
    //.setInfoPanelY(200)
    //.setInfoPanelKey('o')
    //.disableKeyPress()
    ;
  fa = new FrequencyAnalyzer(this)
    //.setBandsPerOctave(6)
    .setFile("https://github.com/vincentsijben/processing-fft-students/raw/main/cmd_fft_library/assets/hot-coffee.mp3")
    .setMode(InputMode.MONO)
    .showInfoPanel()
    .debug()
    ;
    
  pg = createGraphics(width, height);
  for (int i = 0; i < fa.getBands(); i++) {
    circles.add(new Circle(i));
  }
}

void draw() {
  background(100);

drawCircles();

  if (ac.getPotentiometer(0)>0.5) {
    ac.setLED(0, 1);
  } else {
    ac.setLED(0, 0);
  }
  ellipse(lerp(0, width, ac.getPotentiometer(0)), 150, 130, 130);
  ellipse(lerp(0, width, ac.getPotentiometer(0, 0.1)), 350, 50, 50);
  
  
  radius = lerp(0, 100, bpm.linear(3));
  ellipse(width/4*1, height/5*1, radius, radius);

  radius = lerp(0, 100, bpm.linear(3, 1));
  ellipse(width/4*2, height/5*1, radius, radius);

  radius = lerp(0, 100, bpm.linear(3, 2));
  ellipse(width/4*3, height/5*1, radius, radius);

  radius = lerp(0, 100, bpm.linearBounce(3));
  ellipse(width/4*1, height/5*2, radius, radius);

  radius = lerp(0, 100, bpm.linearBounce(3, 1));
  ellipse(width/4*2, height/5*2, radius, radius);

  radius = lerp(0, 100, bpm.linearBounce(3, 2));
  ellipse(width/4*3, height/5*2, radius, radius);

  radius = lerp(0, 100, bpm.ease(3));
  ellipse(width/4*1, height/5*3, radius, radius);

  radius = lerp(0, 100, bpm.ease(3, 1));
  ellipse(width/4*2, height/5*3, radius, radius);

  radius = lerp(0, 100, bpm.ease(3, 2));
  ellipse(width/4*3, height/5*3, radius, radius);

  radius = lerp(0, 100, bpm.easeBounce(3));
  ellipse(width/4*1, height/5*4, radius, radius);

  radius = lerp(0, 100, bpm.easeBounce(3, 1));
  ellipse(width/4*2, height/5*4, radius, radius);

  radius = lerp(0, 100, bpm.easeBounce(3, 2));
  ellipse(width/4*3, height/5*4, radius, radius);
}
