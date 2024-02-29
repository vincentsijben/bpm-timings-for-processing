# FrequencyAnalyzer class
This FrequencyAnalyzer class is used at the Maastricht Institute of Arts exposition during the "Generative Art" semester.
Students create sketches that react in realtime to audio input (line-in or microphone).

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
import bpm.library.frequencyanalyzer.*;

// Import the minim library
import ddf.minim.*;
import ddf.minim.analysis.*;

FrequencyAnalyzer fa;

void setup() {
  size(500, 500);

  fa = new FrequencyAnalyzer(this)
    .setMode(InputMode.MONO)
    ;
}

void draw() {
  background(50);

  circle(width/4*1, height/2, lerp(0, height, fa.getAvg(0)));
  circle(width/4*2, height/2, lerp(0, height, fa.getAvg(10)));
  circle(width/4*3, height/2, lerp(0, height, fa.getAvg(20)));
}
```


The FrequencyAnalyzer class provides the following main functions:
* `getBands()` function that returns total amount of bands used. Useful for creating spectograms. 
* `getAvgRaw(1)` function that returns raw value of the frequency band with index 1.
* `getAvg(2)` function that returns normalized value of the frequency band with index 2. The normalization mapping is done by continuously checking what the highest amplitude overall is.
* `getAvg(2, 150)` function that returns normalized value of the frequency band with index 2, mapped with a max value of 150.
* `resetMaxValue()` function that resets the overall max value (to 0.000001f).

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
You can find all these examples in `Processing -> File - Examples - Contributed Libraries - BPM timings - FrequencyAnalyzer`.

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
