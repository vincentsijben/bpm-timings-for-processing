## Note to self
I've copied `library.properties` to the root and called it `library.properties.example` so I could see the original comments for the file. In `resources\library.properties` I've removed all comments, so the generated `distribution\...\.txt` file is clean and simple.

### Update, test and release
* Open Eclipse
* Update `src\bpm.library\BeatsPerMinute.java`
* Open Ant window `Window -> Show View - Ant`
* drag `resources\build.xml` to the Ant window on the right
* Run the Ant build
* Test every embedded example locally through the `Processing -> File -> Examples`
* Commit changes to GitHub and create a new release
* Name the release `BPM Library Release [version] ([prettyVersion])` and tag it with tag [prettyVersion].
* Upload the `distribution\BPM-[version]\download\BPM_timings.txt` and `distribution\BPM-[version]\download\BPM_timings.zip` into the release.
* Check "Set as the latest release" (it should be checked by default)
* Edit the previous release. Remove the latest tag and set the appropriate [prettyVersion] tag.
* Edit the new created release, remove the [prettyVersion] tag and add the **latest** tag.
* You can check [https://download.processing.org/contribs](https://download.processing.org/contribs) to check if the newest version is picked up by the automated contribution system.

### Debugging issues
* Always check the build.properties files. I've been down a rabithole for 4 hours finding out I had changed my Documents folder location and the build.properties still had `sketchbook.location=${user.home}/Documents/Processing` instead of the new `sketchbook.location=${user.home}/Docs/Processing`

## Create your own Processing Library
Thanks [Elie Zananiri](https://github.com/prisonerjohn) for pointing out these things...

There are a few steps:
* Package your library according to the guidelines here: https://github.com/processing/processing/wiki/Library-Guidelines
* Add a properties file according to the guidelines here: https://github.com/processing/processing/wiki/Library-Basics#describing-your-library--libraryproperties
* Check the revision numbers for Processing 4 [here](https://github.com/processing/processing4/blob/main/build/shared/revisions.md).
* Publish your library and properties to a static URL according to the guidelines here: https://github.com/processing/processing/wiki/Library-Basics#advertising-your-library

* If you're hosting your library on GitHub, use the GitHub Releases feature. Create a release tagged "latest" and move that tag up to the new commit whenever you make an update. Note that you can also tag each release with its version number, in case you want to make older releases still available on GitHub. Check out [processing-video]() for a good example of that.

So my URLs are always available through:
* https://github.com/vincentsijben/bpm-timings-for-processing/releases/download/latest/BPM_timings.txt
* https://github.com/vincentsijben/bpm-timings-for-processing/releases/download/latest/BPM_timings.zip



I've based my Library on the [Processing Library Template](https://github.com/processing/processing-library-template). You could also check out the [Coding Train tutorial](https://www.youtube.com/watch?v=pI2gvl9sdtE). 

Note: 
* use `classpath.local.location=/Applications/Processing.app/Contents/Java/core/library` instead of Daniel's example. When releasing a Library you can't have the `core.jar` in your lib folder.
* Find and comment this line in your build.xml `<taglet name="ExampleTaglet" path="resources/code" />`if you get [errors with generating Javadoc](https://github.com/processing/processing-library-template/issues/19). Find and remove this line in your build.xml as well `stylesheetfile="resources/stylesheet.css"`
* I added a symlink to each and every example folder, so I can directly open up an example in Processing IDE and add new features in the BeatsPerMinute.java file. Processing IDE needs to "see" the .java file in the same directory as the .pde file to work. Unfortunately I haven't found a way for these symlinks to work both on MacOS and Windows at the same time. So according to your dev environment, (re)create the symlinks when necessary. I excluded the .java file in build.xml so they won't show up in production with:
```
<copy todir="${project.tmp}/${project.name}/examples">
	<fileset dir="${project.examples}">
		<exclude name="**/*README*"/>
		<exclude name="**/*.java"/>
	</fileset>
</copy>
```
To create symlinks:
```
# For MacOS: while in the root folder of this project:
ln -s ../../src/bpm/library/BeatsPerMinute.java ./examples/animatedSVG/BeatsPerMinute.java
ln -s ../../src/bpm/library/BeatsPerMinute.java ./examples/beatcount/BeatsPerMinute.java
ln -s ../../src/bpm/library/BeatsPerMinute.java ./examples/colorPalettes/BeatsPerMinute.java
ln -s ../../src/bpm/library/BeatsPerMinute.java ./examples/delay/BeatsPerMinute.java
ln -s ../../src/bpm/library/BeatsPerMinute.java ./examples/metronome/BeatsPerMinute.java
ln -s ../../src/bpm/library/BeatsPerMinute.java ./examples/randomColor/BeatsPerMinute.java
ln -s ../../src/bpm/library/BeatsPerMinute.java ./examples/randomGridSpots/BeatsPerMinute.java

# For Windows: while in the root folder of this project:
mklink .\examples\animatedSVG\BeatsPerMinute.java "..\..\src\bpm\library\BeatsPerMinute.java"
mklink .\examples\beatcount\BeatsPerMinute.java "..\..\src\bpm\library\BeatsPerMinute.java"
mklink .\examples\colorPalettes\BeatsPerMinute.java "..\..\src\bpm\library\BeatsPerMinute.java"
mklink .\examples\delay\BeatsPerMinute.java "..\..\src\bpm\library\BeatsPerMinute.java"
mklink .\examples\metronome\BeatsPerMinute.java "..\..\src\bpm\library\BeatsPerMinute.java"
mklink .\examples\randomColor\BeatsPerMinute.java "..\..\src\bpm\library\BeatsPerMinute.java"
mklink .\examples\randomGridSpots\BeatsPerMinute.java "..\..\src\bpm\library\BeatsPerMinute.java"
```
ArduinoControls examples:
```
ln -s ../../../src/bpm/library/arduinocontrols/ArduinoControls.java ./examples/ArduinoControls/basics/ArduinoControls.java
ln -s ../../../src/bpm/library/arduinocontrols/LED.java ./examples/ArduinoControls/basics/LED.java
ln -s ../../../src/bpm/library/arduinocontrols/LEDMode.java ./examples/ArduinoControls/basics/LEDMode.java
ln -s ../../../src/bpm/library/arduinocontrols/Potentiometer.java ./examples/ArduinoControls/basics/Potentiometer.java
ln -s ../../../src/bpm/library/arduinocontrols/PushButton.java ./examples/ArduinoControls/basics/PushButton.java
ln -s ../../../src/bpm/library/InfoPanel.java ./examples/ArduinoControls/basics/InfoPanel.java
```
FrequencyAnalyzer examples:
```
ln -s ../../../src/bpm/library/frequencyanalyzer/FrequencyAnalyzer.java ./examples/FrequencyAnalyzer/basics/FrequencyAnalyzer.java
ln -s ../../../src/bpm/library/frequencyanalyzer/AudioFileInputSource.java ./examples/FrequencyAnalyzer/basics/AudioFileInputSource.java
ln -s ../../../src/bpm/library/frequencyanalyzer/AudioInputMode.java ./examples/FrequencyAnalyzer/basics/AudioInputMode.java
ln -s ../../../src/bpm/library/frequencyanalyzer/AudioOutputMode.java ./examples/FrequencyAnalyzer/basics/AudioOutputMode.java
ln -s ../../../src/bpm/library/frequencyanalyzer/AudioInputSource.java ./examples/FrequencyAnalyzer/basics/AudioInputSource.java
ln -s ../../../src/bpm/library/frequencyanalyzer/MicrophoneInputSource.java ./examples/FrequencyAnalyzer/basics/MicrophoneInputSource.java
ln -s ../../../src/bpm/library/frequencyanalyzer/LineInInputSource.java ./examples/FrequencyAnalyzer/basics/LineInInputSource.java
ln -s ../../../src/bpm/library/InfoPanel.java ./examples/FrequencyAnalyzer/basics/InfoPanel.java

ln -s ../../../src/bpm/library/frequencyanalyzer/FrequencyAnalyzer.java ./examples/FrequencyAnalyzer/mono/FrequencyAnalyzer.java
ln -s ../../../src/bpm/library/frequencyanalyzer/AudioFileInputSource.java ./examples/FrequencyAnalyzer/mono/AudioFileInputSource.java
ln -s ../../../src/bpm/library/frequencyanalyzer/AudioInputMode.java ./examples/FrequencyAnalyzer/mono/AudioInputMode.java
ln -s ../../../src/bpm/library/frequencyanalyzer/AudioOutputMode.java ./examples/FrequencyAnalyzer/mono/AudioOutputMode.java
ln -s ../../../src/bpm/library/frequencyanalyzer/AudioInputSource.java ./examples/FrequencyAnalyzer/mono/AudioInputSource.java
ln -s ../../../src/bpm/library/frequencyanalyzer/MicrophoneInputSource.java ./examples/FrequencyAnalyzer/mono/MicrophoneInputSource.java
ln -s ../../../src/bpm/library/frequencyanalyzer/LineInInputSource.java ./examples/FrequencyAnalyzer/mono/LineInInputSource.java
ln -s ../../../src/bpm/library/InfoPanel.java ./examples/FrequencyAnalyzer/mono/InfoPanel.java

ln -s ../../../src/bpm/library/frequencyanalyzer/FrequencyAnalyzer.java ./examples/FrequencyAnalyzer/stereo/FrequencyAnalyzer.java
ln -s ../../../src/bpm/library/frequencyanalyzer/AudioFileInputSource.java ./examples/FrequencyAnalyzer/stereo/AudioFileInputSource.java
ln -s ../../../src/bpm/library/frequencyanalyzer/AudioInputMode.java ./examples/FrequencyAnalyzer/stereo/AudioInputMode.java
ln -s ../../../src/bpm/library/frequencyanalyzer/AudioOutputMode.java ./examples/FrequencyAnalyzer/stereo/AudioOutputMode.java
ln -s ../../../src/bpm/library/frequencyanalyzer/AudioInputSource.java ./examples/FrequencyAnalyzer/stereo/AudioInputSource.java
ln -s ../../../src/bpm/library/frequencyanalyzer/MicrophoneInputSource.java ./examples/FrequencyAnalyzer/stereo/MicrophoneInputSource.java
ln -s ../../../src/bpm/library/frequencyanalyzer/LineInInputSource.java ./examples/FrequencyAnalyzer/stereo/LineInInputSource.java
ln -s ../../../src/bpm/library/InfoPanel.java ./examples/FrequencyAnalyzer/stereo/InfoPanel.java
```
BPM_Timing examples:
```
ln -s ../../../src/bpm/library/beatsperminute/BeatsPerMinute.java ./examples/BPM_Timings/animatedSVG/BeatsPerMinute.java
ln -s ../../../src/bpm/library/InfoPanel.java ./examples/BPM_Timings/animatedSVG/InfoPanel.java
ln -s ../../../src/bpm/library/beatsperminute/BeatsPerMinute.java ./examples/BPM_Timings/beatcount/BeatsPerMinute.java
ln -s ../../../src/bpm/library/InfoPanel.java ./examples/BPM_Timings/beatcount/InfoPanel.java
ln -s ../../../src/bpm/library/beatsperminute/BeatsPerMinute.java ./examples/BPM_Timings/colorPalettes/BeatsPerMinute.java
ln -s ../../../src/bpm/library/InfoPanel.java ./examples/BPM_Timings/colorPalettes/InfoPanel.java
ln -s ../../../src/bpm/library/beatsperminute/BeatsPerMinute.java ./examples/BPM_Timings/delay/BeatsPerMinute.java
ln -s ../../../src/bpm/library/InfoPanel.java ./examples/BPM_Timings/delay/InfoPanel.java
ln -s ../../../src/bpm/library/beatsperminute/BeatsPerMinute.java ./examples/BPM_Timings/metronome/BeatsPerMinute.java
ln -s ../../../src/bpm/library/InfoPanel.java ./examples/BPM_Timings/metronome/InfoPanel.java
ln -s ../../../src/bpm/library/beatsperminute/BeatsPerMinute.java ./examples/BPM_Timings/randomColor/BeatsPerMinute.java
ln -s ../../../src/bpm/library/InfoPanel.java ./examples/BPM_Timings/randomColor/InfoPanel.java
ln -s ../../../src/bpm/library/beatsperminute/BeatsPerMinute.java ./examples/BPM_Timings/randomGridSpots/BeatsPerMinute.java
ln -s ../../../src/bpm/library/InfoPanel.java ./examples/BPM_Timings/randomGridSpots/InfoPanel.java
ln -s ../../../src/bpm/library/beatsperminute/BeatsPerMinute.java ./examples/BPM_Timings/adsr/BeatsPerMinute.java
ln -s ../../../src/bpm/library/InfoPanel.java ./examples/BPM_Timings/adsr/InfoPanel.java
ln -s ../../../src/bpm/library/beatsperminute/BeatsPerMinute.java ./examples/BPM_Timings/test/BeatsPerMinute.java
ln -s ../../../src/bpm/library/InfoPanel.java ./examples/BPM_Timings/test/InfoPanel.java

```
All_combined:
```
ln -s ../../../src/bpm/library/arduinocontrols/ArduinoControls.java ./examples/All_combined/startercode/ArduinoControls.java
ln -s ../../../src/bpm/library/arduinocontrols/LED.java ./examples/All_combined/startercode/LED.java
ln -s ../../../src/bpm/library/arduinocontrols/LEDMode.java ./examples/All_combined/startercode/LEDMode.java
ln -s ../../../src/bpm/library/arduinocontrols/Potentiometer.java ./examples/All_combined/startercode/Potentiometer.java
ln -s ../../../src/bpm/library/arduinocontrols/PushButton.java ./examples/All_combined/startercode/PushButton.java
ln -s ../../../src/bpm/library/InfoPanel.java ./examples/All_combined/startercode/InfoPanel.java
ln -s ../../../src/bpm/library/frequencyanalyzer/FrequencyAnalyzer.java ./examples/All_combined/startercode/FrequencyAnalyzer.java
ln -s ../../../src/bpm/library/frequencyanalyzer/AudioFileInputSource.java ./examples/All_combined/startercode/AudioFileInputSource.java
ln -s ../../../src/bpm/library/frequencyanalyzer/AudioInputMode.java ./examples/All_combined/startercode/AudioInputMode.java
ln -s ../../../src/bpm/library/frequencyanalyzer/AudioOutputMode.java ./examples/All_combined/startercode/AudioOutputMode.java
ln -s ../../../src/bpm/library/frequencyanalyzer/AudioInputSource.java ./examples/All_combined/startercode/AudioInputSource.java
ln -s ../../../src/bpm/library/frequencyanalyzer/MicrophoneInputSource.java ./examples/All_combined/startercode/MicrophoneInputSource.java
ln -s ../../../src/bpm/library/frequencyanalyzer/LineInInputSource.java ./examples/All_combined/startercode/LineInInputSource.java
ln -s ../../../src/bpm/library/beatsperminute/BeatsPerMinute.java ./examples/All_combined/startercode/BeatsPerMinute.java
```
