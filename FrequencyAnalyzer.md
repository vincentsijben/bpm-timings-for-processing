# FrequencyAnalyzer class
This FrequencyAnalyzer class is used at the Maastricht Institute of Arts exposition during the "Generative Art" semester.
Students create sketches that react in realtime to audio input (line-in, audio file or microphone).

 This library adds functionality like:
 - easily swtich between input modes (line-in, audio file, microphone)
 - return normalized values of specific frequency bands
 - toggle on/off for audioplayer mute or microphone monitoring
 - reset the max value that is used for mapping normalized values of frequency amplitudes. Can be set to an interval.
 
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
* `.addMinim(minim)` mandatory to add the global minim object to the class.
* `.setBandsPerOctave(6)` to get a total of 6 * 10 bands.
* `.setFile("example.mp3")` to set the file for the audioplayer.
* `.setMode(InputMode.FILE)` to set the input mode to InputMode.FILE. You can also set it to InputMode.STEREO. Defaults to InputMode.MONO. 
* `.resetMaxValueDuration(2000)` to reset the max value every 2000 milliseconds.
* `showInfoPanel()` to show the infopanel.
* `setInfoPanelY(n)` to offset the starting y-position of the infopanel by n pixels. Useful for when you have multiple infopanels to get them all lined up.
* `setInfoPanelKey('u')` to change the hotkey to toggle the infopanel. Useful for when you have multiple infopanels. Defaults to 'i'.
* `disableKeyPress()` to disable listening for keypresses. If you don't disable keypresses, then these keypresses will work:
  * `CTRL + 1` switch to FILE mode
  * `CTRL + 2` switch to MONO mode
  * `CTRL + 3` switch to STEREO mode
  * `CTRL + M` toggle mute or microphone monitoring (depends on InputMode)
  * `CTRL + R` reset the max value

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
