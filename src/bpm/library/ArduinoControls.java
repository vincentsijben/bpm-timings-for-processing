/*
  todo: build example where a pushbutton acts as a switch. one press turns LED on, second press turns it off
 todo: create function that returns potentiometer value, but only when stopped turning, so with a threshold
 
 */

/*
  This ArduinoControls class is used at the Institute of Arts Maastricht exposition, semester Generative Art
 Students build their own Arduino remote controller with 3 potentiometers and 3 pushbuttons.
 This library simplifies the use for these controls. It adds functionality like:
 - executing single commands when longpressing getPushButtonOnce(0);
 - multiple pushbuttons being pressed if (getPushButton(0) && getPushButton(1))
 - smooth analog potmeter values getPotmeter(0,0.02); reducing jumping values
 - fallback to keyboard and mouse when not using arduino. e.g. 1 to 9 for pushbuttons. q,w,e,r,t,y together with mouseX for potmeters
 - adjustable infopanel (set hotkey, y position)
 - Only write LED once, not continuously, preventing flickering
 */

package bpm.library;

import java.util.ArrayList;
import processing.core.*;
import processing.event.KeyEvent;
import cc.arduino.*;

public class ArduinoControls {

  private PApplet parent;
  private Arduino arduino;
  private InfoPanel infoPanel;

  private boolean enableKeyPress;
  private ArrayList <PushButton> pushbuttons;
  private ArrayList <Potentiometer> potentiometers;
  private ArrayList <LED> leds;
  private int lastFrameCount;

  public ArduinoControls(PApplet parent) {
    this.parent = parent;
    this.pushbuttons = new ArrayList<PushButton>();
    this.potentiometers = new ArrayList<Potentiometer>();
    this.leds = new ArrayList<LED>();
    this.infoPanel = new InfoPanel(parent);
    this.enableKeyPress = true;
    this.lastFrameCount = -1;

    //https://github.com/benfry/processing4/wiki/Library-Basics
    parent.registerMethod("draw", this);
    parent.registerMethod("post", this);
    parent.registerMethod("keyEvent", this);
  }

  public ArduinoControls addArduino(Arduino arduino) {
    this.arduino = arduino;
    return this;
  }










  //////////////////////////////////////////////////////////////
  //                    LED
  //////////////////////////////////////////////////////////////
  public ArduinoControls addLED(int pinNumber) {
    return this.addLED(pinNumber, LEDMode.DIGITAL);
  }

  public ArduinoControls addLED(int pinNumber, LEDMode mode) {
    LED newLED = new LED(pinNumber, mode);
    this.leds.add(newLED);
    return this;
  }

  public void setLEDToOn(int index) {
    if (index >= 0 && index < this.leds.size()) {
      LED led = this.leds.get(index);

      if (led.mode == LEDMode.PWM) this.setLED(index, 255);
      else this.setLED(index, Arduino.HIGH);
    } else {
      System.out.println("warning: index " + index + " was used which is out of bounds for the ArrayList leds with size " + leds.size());
    }
  }

  public void setLEDToOff(int index) {
    this.setLED(index, Arduino.LOW);
  }

  public void setLED(int index, int value) {
    if (index >= 0 && index < this.leds.size()) {
      LED led = this.leds.get(index);
      if (value != led.value) { // no need to continuously write the same value, causes flickering on pwm
        led.value = value;
        if (this.arduino != null) {

          if (led.mode == LEDMode.PWM) this.arduino.analogWrite(led.digitalPort, value);
          else {
            this.arduino.digitalWrite(led.digitalPort, value);
          }
        }
      }
    } else {
      System.out.println("warning: index " + index + " was used which is out of bounds for the ArrayList leds with size " + leds.size());
    }
  }










  //////////////////////////////////////////////////////////////
  //                    PUSHBUTTON
  //////////////////////////////////////////////////////////////
  public ArduinoControls addPushButton(int pinNumber, char keyboardKey, int signalPressed) {
    PushButton newPushButton = new PushButton(pinNumber, keyboardKey, signalPressed);
    this.pushbuttons.add(newPushButton);
    return this;
  }

  public boolean getPushButtonOnce(int index) {
    return this.getPushButton(index, true);
  }

  public boolean getPushButton(int index) {
    return this.getPushButton(index, false);
  }

  private boolean getPushButton(int index, boolean once) {
    if (index >= 0 && index < this.pushbuttons.size()) {
      PushButton pushbutton = this.pushbuttons.get(index);
      if (this.arduino != null) {
        if (this.arduino.digitalRead(pushbutton.digitalPort) == pushbutton.signalPressed && pushbutton.actionTaken == false) {
          pushbutton.actionTaken = true;
          pushbutton.pressed = true;
          pushbutton.pressedOnce = true;
          this.lastFrameCount = this.parent.frameCount;
        }
        if (this.arduino.digitalRead(pushbutton.digitalPort) != pushbutton.signalPressed) {
          pushbutton.actionTaken = false;
          pushbutton.pressed = false;
        }
      }

      return once ? pushbutton.pressedOnce : pushbutton.pressed;
    } else {
      System.out.println("warning: index " + index + " was used which is out of bounds for the ArrayList pushbuttons with size " + pushbuttons.size() + ", returning false");
      return false;
    }
  }










  //////////////////////////////////////////////////////////////
  //                    POTENTIOMETER
  //////////////////////////////////////////////////////////////
  public ArduinoControls addPotentiometer(int pinNumber, char keyboardKey) {
    return this.addPotentiometer(pinNumber, keyboardKey, 0, 1023);
  }

  public ArduinoControls addPotentiometer(int pinNumber, char keyboardKey, int minValue, int maxValue) {
    Potentiometer newPotentiometer = new Potentiometer(pinNumber, keyboardKey, minValue, maxValue);
    this.potentiometers.add(newPotentiometer);
    return this;
  }



  public float getPotentiometer(int index) {
    return this.getPotentiometer(index, 1.0f);
  }

  public float getPotentiometer(int index, float smoothness) {
    if (index >= 0 && index < this.potentiometers.size()) {
      float returnValue = this.potentiometers.get(index).value;
      if (this.arduino != null) {
        this.potentiometers.get(index).value = this.arduino.analogRead(this.potentiometers.get(index).analogPort);
        returnValue = this.potentiometers.get(index).value;

        //if we don't handle the raw input seperately (when calling getPotmeter(index, 1.0)), every additional call to getPotmeter removes the previous smoothness
        if (smoothness < 1.0) {
          this.potentiometers.get(index).smoothValue = PApplet.lerp(this.potentiometers.get(index).smoothValue, this.potentiometers.get(index).value, smoothness);
          returnValue = this.potentiometers.get(index).smoothValue;
        }
      }
      return PApplet.constrain(PApplet.map(returnValue, this.potentiometers.get(index).minValue, this.potentiometers.get(index).maxValue, 0, 1), 0, 1);
    } else {
      System.out.println("warning: index " + index + " was used which is out of bounds for the ArrayList potmeters with size " + potentiometers.size() + ", returning 0.0");
      return 0.0f;
    }
  }










  //////////////////////////////////////////////////////////////
  //                    KEYBOARD + MOUSE
  //////////////////////////////////////////////////////////////
  public ArduinoControls disableKeyPress() {
    this.enableKeyPress = false;
    this.infoPanel.enableKeyPress = false;
    return this;
  }

  public void keyEvent(KeyEvent event) {
    if (this.enableKeyPress) {
      // Removed KeyEvent.TYPE because p2d or p3d don't register TYPE
      if (event.getAction() == KeyEvent.PRESS) this.onKeyPress(event);
      else if (event.getAction() == KeyEvent.RELEASE) this.onKeyRelease(event);
    }
  }

  private void onKeyPress(KeyEvent event) {
    //handle long press events, only works in default renderer, not in P2D or P3D
    //if in P2D or P3D mode, quick-tap the q,w or e button to get the correct mouseX value
    for (int i=0; i<this.potentiometers.size(); i++) {
      Potentiometer potmeter = this.potentiometers.get(i);
      if (event.getKey() == potmeter.keyboardKey ) potmeter.value = PApplet.constrain((int) PApplet.map(this.parent.mouseX, 0, this.parent.width, potmeter.minValue, potmeter.maxValue), potmeter.minValue, potmeter.maxValue);
    }

    for (int i=0; i<this.pushbuttons.size(); i++) {
      PushButton pushbutton = this.pushbuttons.get(i);

      if (event.getKey()== pushbutton.keyboardKey && pushbutton.actionTaken == false) {
        pushbutton.actionTaken = true;
        pushbutton.pressed = true;
        pushbutton.pressedOnce = true;
        this.lastFrameCount = this.parent.frameCount;
      }
    }
  }

  private void onKeyRelease(KeyEvent event) {
    // Reset the flag when the key is released, allowing for the action to be taken on the next key press
    for (PushButton button : pushbuttons) {
      if (button.keyboardKey == event.getKey()) {
        button.actionTaken = false;
        button.pressed = false;
      }
    }
  }










  //////////////////////////////////////////////////////////////
  //                    INFOPANEL
  //////////////////////////////////////////////////////////////
  public ArduinoControls showInfoPanel() {
    this.infoPanel.show = true;
    return this;
  }

  public ArduinoControls setInfoPanelY(int y) {
    this.infoPanel.y = y;
    return this;
  }

  public ArduinoControls setInfoPanelKey(char keyboardKey) {
    this.infoPanel.keyboardKey = keyboardKey;
    return this;
  }

  public void draw() {
    if (this.infoPanel.show) {
      //System.out.println(""+this.infoPanel.x + this.infoPanel.y + this.infoPanel.w + this.infoPanel.h);
      boolean portrait = false; //this.infoPanelLocation[2] < this.infoPanelLocation[3];

      PGraphics overlay = this.infoPanel.overlay;
      overlay.beginDraw();
      overlay.background(0, 200);
      overlay.noStroke();
      overlay.fill(255);
      if (portrait) {
        for (int i=0; i<this.pushbuttons.size(); i++) overlay.text("getPushButton("+i+ "): " + this.getPushButton(i), 5, 15+i*20);
        for (int i=0; i<this.potentiometers.size(); i++) overlay.text("getPotentiometer("+i+ "): " + PApplet.nf(this.getPotentiometer(i), 0, 2) + " raw: " + this.potentiometers.get(i).value, 5, 115+i*20);
      } else {
        for (int i=0; i<this.pushbuttons.size(); i++) overlay.text("getPushButton("+i+ "): " + this.getPushButton(i), 5, 15+i*20);
        for (int i=0; i<this.potentiometers.size(); i++) overlay.text("getPotentiometer("+i+ "): " + PApplet.nf(this.getPotentiometer(i), 0, 2) + " raw: " + this.potentiometers.get(i).value, 185, 15+i*20);
      }
      overlay.endDraw();

      this.parent.image(overlay, this.infoPanel.x, this.infoPanel.y, this.infoPanel.w, this.infoPanel.h); // Draw the overlay onto the main canvas
    }
  }










  public void post() {
    // https://github.com/benfry/processing4/wiki/Library-Basics
    // you cant draw in post() but its perfect for resetting the inputButtonsOnce array:
    if (this.parent.frameCount != this.lastFrameCount) for (PushButton button : pushbuttons) button.pressedOnce = false;
  }
}
