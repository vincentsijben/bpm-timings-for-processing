# ArduinoControls class
This ArduinoControls class is used at the Maastricht Institute of Arts exposition during the "Generative Art" semester.
Students build their own Arduino remote controller with 3 potentiometers, 3 pushbuttons and 3 LEDs.

 This library simplifies the use for these controls. It adds functionality like:
 - executing single commands when longpressing pushbuttons;
 - multiple pushbuttons being pressed
 - smooth analog potmeter values, reducing 'jumping' values
 - fallback to keyboard and mouse when not using arduino
 - only write LED once instead of continuously, preventing flickering
 
## Usage
Be sure to check the console to find the correct serial port. In this example port 3 (index 2) is used.
```
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
```


The ArduinoControls class provides the following main functions:
* `setLEDToOn(0)` function that turns on the LED with index 0. If it's an LED that was set up with PWM, it uses `255`, else `Arduino.High`.
* `setLEDToOff(2)` function that turns off the LED with index 2.
* `setLED(0,200)` function that sets the value of LED with index 0 to 200. All values set to LEDs are only set once in draw() to prevent flickering.
* `getPushButton(0)` function that returns true while the pushbutton with index 0 is being pushed.
* `getPushButtonOnce(0)` function that returns true if the pushbutton with index 0 was pushed. Only returns true for the duration of 1 frame.
* `getPotentiometer(0)` functon that returns the raw normalized value from potentiometer with index 0, without any smoothing
* `getPotentiometer(0, 0.5)` functon that returns the smoothed normalized value from potentiometer with index 0. Smoothness is a value between 0 and 1 which adds a little delay.

You can tweak the behaviour of this library with the following functions (you can also chain them when initializing your arduinocontrols object for clarity):
* `.addArduino(arduino)` mandatory to add the global arduino object to the class. If you omit it, the keypresses associated with all controls will be enabled.
* `.addLED(9)` to add an LED to the class at digital port 9.
* `.addLED(10, LEDMode.PWM)` to add an LED to the class at digital port 10 as a PWM connected LED. The LEDMode argument is optional (default is `LEDMode.DIGITAL`).
* `.addPushButton(7, '1', Arduino.LOW)` to add a pushbutton to the class at digital port 7, that is controllable with the keyboard key '1' when not connected and has a value of Arduino.LOW when pressed. All three arguments are mandatory.
* `.addPotentiometer(0, 'q')` to add a potentiometer to the class at analog port 0, that is controllable with the mouseX position while pressing the keyboard key 'q'. Both arguments are mandatory.
* `.showInfoPanel()` to show the infopanel.
* `.setInfoPanelY(n)` to offset the starting y-position of the infopanel by n pixels. Useful for when you have multiple infopanels to get them all lined up.
* `.setInfoPanelKey('u')` to change the hotkey to toggle the infopanel. Useful for when you have multiple infopanels. Defaults to 'i'.
* `.disableKeyPress()` to disable listening for keypresses. If you don't disable keypresses, then the keys you provided as arguments for pushbuttons and potentiometers will work


## Examples
You can find all these examples in `Processing -> File - Examples - Contributed Libraries - BPM timings - ArduinoControls`.

<table width="100%">
  <tr>
    <td valign="top" align="center" width="33%">example title<br>image example</td>
    <td valign="top" align="center" width="33%">example title<br>image example</td>
    <td valign="top" align="center" width="33%">example title<br>image example</td>
  </tr>
   <tr>
   <td valign="top" align="center" width="33%">example title<br>image example</td>
   <td valign="top" align="center" width="33%">example title<br>image example</td>
   <td valign="top" align="center" width="33%">example title<br>image example</td>
  </tr>
 </table>
