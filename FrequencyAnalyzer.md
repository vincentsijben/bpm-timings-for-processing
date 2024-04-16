# FrequencyAnalyzer class
This FrequencyAnalyzer class is used at the Maastricht Institute of Arts exposition during the "Generative Art" semester.
Students create sketches that react in realtime to audio input (line-in, audio file or microphone).

 This library adds functionality like:
 - easily switch between input modes (audio file, microphone, line-in)
 - toggle mute (audio file playing) or monitoring (microphone or line-in)
 
 todo:
 - return raw or normalized values of specific frequency bands
 - reset the max value that is used for mapping normalized values of frequency amplitudes. Can be set to an interval.
 
## Usage

```
// Import the library to your sketch
import bpm.library.frequencyanalyzer.*;

// Import the minim library
import ddf.minim.*;
import ddf.minim.analysis.*;

Minim minim;
FrequencyAnalyzer fa;

void setup() {
  size(500, 500);

  minim = new Minim(this);
  fa = new FrequencyAnalyzer(this)
    .addMinim(minim)
    .setFile("https://github.com/vincentsijben/bpm-timings-for-processing/raw/main/assets/infraction_music_-_ritmo.mp3")
    .setAudioInputMode(AudioInputMode.AUDIO_FILE)
    ;
}

void draw() {
  background(50);

  circle(width/4*1, height/2, fa.getAvgRaw(0));
  circle(width/4*2, height/2, fa.getAvgRaw(10));
  circle(width/4*3, height/2, fa.getAvgRaw(20));
}
```

The FrequencyAnalyzer class provides the following main functions:

* `getAvgRaw(1)` returns non-normalized "raw" averaged amplitude for frequency band 1. The index ranges from 0 to logAverages(22, 3) which is 30 by default.
  * `getAvgRawLeft(1)` same as getAvgRaw(1) but specific for the left channel.
  * `getAvgRawRight(1)` same as getAvgRaw(1) but specific for the right channel.

todo:

* ~~`getBand(1)` returns the amplitude for frequency band 1. Used for a very specific and narrow frequency range. The index ranges from 0 to specSize().~~
  * ~~`getBandLeft(1)` same as getBand(1) but specific for the left channel.~~
  * ~~`getBandRight(1)` same as getBand(1) but specific for the right channel.~~
* ~~`specSize()` returns the total amount of bands used. Typically 1025 ~~
* ~~`avgSize()` returns the total amount of bands used in the logAverages function. Typically 30~~
* ~~`getAudioBuffer()` returns the mixed (mono) audio buffer.~~
  * ~~`getLeftChannelBuffer()` returns the left audio buffer.~~
  * ~~`getRightChannelBuffer()` returns the right audio buffer.~~


You can tweak the behaviour of this library with the following functions (you can also chain them when initializing your frequencyanalyzer object for clarity):
* `.addMinim(minim)` mandatory to add the global minim object to the class.
* `.setFile("example.mp3")` to set the file for the audioplayer.
* `.setAudioInputMode(AudioInputMode.AUDIO_FILE)` to set the input mode to AudioInputMode.AUDIO_FILE. You can also set it to AudioInputMode.LINE_IN or AudioInputMode.MICROPHONE. Defaults to AudioInputMode.MICROPHONE. 
* `.setAudioInputMode(AudioInputMode.AUDIO_FILE, n)` same as previous one, but you can also set the bufferSize. Needs to be a power of 2. A lower amount results in less audio resolution and decreases delay . Defaults to 1024.
* `.setAudioOutputMode(AudioOutputMode.STEREO)` to set the output mode to AudioOutputMode.STEREO. Defaults to AudioOutputMode.MONO. Use it to get access to both left and right channel analysis.
* `.showInfoPanel()` to show the infopanel.
* `.setInfoPanelY(n)` to offset the starting y-position of the infopanel by n pixels. Useful for when you have multiple infopanels to get them all lined up.
* `.setInfoPanelKey('u')` to change the hotkey to toggle the infopanel. Useful for when you have multiple infopanels. Defaults to 'i'.
* `.disableKeyPress()` to disable listening for keypresses. If you don't disable keypresses, then these keypresses will work:
  * `CTRL + 1` switch to FILE mode
  * `CTRL + 2` switch to MONO mode
  * `CTRL + 3` switch to STEREO mode
  * `CTRL + M` toggle monitoring on LINE_IN or MICROPHONE input

<!-- 
  * `CTRL + R` reset the max value
* `getAvg(2)` function that returns normalized value of the frequency band with index 2. The normalization mapping is done by continuously checking the highest overall amplitude.
* `getAvg(2, 150)` function that returns normalized value of the frequency band with index 2, mapped with a max value of 150.
* `resetMaxValue()` function that resets the overall max value (to 0.1f).
* `.resetMaxValueDuration(2000)` to reset the max value every 2000 milliseconds.
* `.setBandsPerOctave(6)` to get a total of 6 * 10 bands. 
-->  

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
