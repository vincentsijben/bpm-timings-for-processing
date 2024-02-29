# ArduinoControls class
This ArduinoControls class is used at the Maastricht Institute of Arts exposition during the "Generative Art" semester.
Students build their own Arduino remote controller with 3 potentiometers, 3 pushbuttons and 3 LEDs.

 This library simplifies the use for these controls. It adds functionality like:
 - executing single commands when longpressing pushbuttons;
 - multiple pushbuttons being pressed
 - smooth analog potmeter values, reducing 'jumping' values
 - fallback to keyboard and mouse when not using arduino
 - adjustable infopanel
 - Only write LED once instead of continuously, preventing flickering
 

## Usage

```
// Import the library to your sketch
import bpm.library.arduinocontrols.*;
// Import the arduino and serial libraries
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
```


The ArduinoControls class provides the following main functions:
* `setLEDToOn(0)` function that turns on the LED with index 0. If it's an LED that was set up with PWM, it uses `255`, else `Arduino.High`.
* `setLEDToOff(2)` function that turns off the LED with index 2.
* `setLED(0,200)` function that sets the value of LED with index 0 to 200. All values set to LEDs are only set once in draw() to prevent flickering.
* `getPushButtonOnce(0)` function that returns true if the pushbutton with index 0 was pushed. Only returns true for the duration of 1 frame.
* `getPushButton(1)` function that returns true while the pushbutton with index 1 is being pushed.
* `getPotentiometer(0)` functon that returns the raw normalized value from potentiometer with index 0, without any smoothing
* `getPotentiometer(0, 0.5)` functon that returns the smoothed normalized value from potentiometer with index 0. Smoothness is a value between 0 and 1 which adds a little delay.

You can tweak the behaviour of this library with the following functions (you can also chain them when initializing for clarity):
* `.addArduino(arduino)` mandatory to add the arduino object to the class.
* `.addLED(9)` to add an LED to the class at digital port 9.
* `.addLED(10, LEDMode.PWM)` to add an LED to the class at digital port 10 as a PWM connected LED. The LEDMode argument is optional (default is `LEDMode.DIGITAL`).
* `.addPushButton(7, '1', Arduino.LOW)` to add a pushbutton to the class at digital port 7, that is controllable with the keyboard key '1' when not connected and has a value of Arduino.LOW when pressed. All three arguments are mandatory.
* `.addPotentiometer(0, 'q')` to add a potentiometer to the class at analog port 0, that is controllable with the mouseX position while pressing the keyboard key 'q'. 
* `showInfoPanel()` to show the infopanel.
* `setInfoPanelY(n)` to offset the starting y-position of the infopanel by n pixels. Useful for when you have multiple infopanels to get them all lined up.
* `setInfoPanelKey('u')` to change the hotkey to toggle the infopanel. Useful for when you have multiple infopanels. Defaults to 'i'.
* `disableKeyPress()` to disable listening for keypresses. If you don't disable keypresses, then the keys you provided as arguments for pushbuttons and potentiometers will work


## Examples
You can find all these examples in `Processing -> File - Examples - Contributed Libraries - BPM timings - ArduinoControls`.

<table width="100%">

  <tr>
    <td valign="top" align="center" width="33%"><a href="https://github.com/vincentsijben/bpm-timings-for-processing/blob/master/examples/BPM_Timings/animatedSVG/animatedSVG.pde">animatedSVG</a><br><img src="assets/example-animatedsvg.gif" width="100%" alt="screenshot for example animatedSVG" /></td>
    <td valign="top" align="center" width="33%"><a href="https://github.com/vincentsijben/bpm-timings-for-processing/blob/master/examples/BPM_Timings/beatcount/beatcount.pde">beatCount</a><br><img src="assets/example-beatcount.gif" width="100%" alt="screenshot for example beatCount" /></td>
    <td valign="top" align="center" width="33%"><a href="https://github.com/vincentsijben/bpm-timings-for-processing/blob/master/examples/BPM_Timings/colorPalettes/colorPalettes.pde">animatedSVG</a><br><img src="assets/example-colorpalettes.gif" width="100%" alt="screenshot for example colorPalettes" /></td>
  </tr>
   <tr>
    <td valign="top" align="center" width="33%"><a href="https://github.com/vincentsijben/bpm-timings-for-processing/blob/master/examples/BPM_Timings/delay/delay.pde">delay</a><br><img src="assets/example-delay.gif" width="100%" alt="screenshot for example delay" /></td>
    <td valign="top" align="center" width="33%"><a href="https://github.com/vincentsijben/bpm-timings-for-processing/blob/master/examples/BPM_Timings/metronome/metronome.pde">metronome</a><br><img src="assets/example-metronome.gif" width="100%" alt="screenshot for example metronome" /></td>
    <td valign="top" align="center" width="33%"><a href="https://github.com/vincentsijben/bpm-timings-for-processing/blob/master/examples/BPM_Timings/randomColor/randomColor.pde">randomcolor</a><br><img src="assets/example-randomcolor.gif" width="100%" alt="screenshot for example randomcolor" /></td>
  </tr>
  <tr>
    <td valign="top" align="center" width="33%"><a href="https://github.com/vincentsijben/bpm-timings-for-processing/blob/master/examples/BPM_Timings/randomGridSpots/randomGridSpots.pde">randomGridSpots</a><br><img src="assets/example-randomgridspots.gif" width="100%" alt="screenshot for example randomGridSpots" /></td>
    <td valign="top" align="center" width="33%"></td>
    <td valign="top" align="center" width="33%"></td>
  </tr>
 
 </table>
