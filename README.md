# BPM timings for Processing

## Introduction
BPM timings for Processing is a small library for Processing that provides easy to use timing functions based on beats per minute. For example, you could easily grow or shrink shapes in any amount of beats, or delay shrinking a shape to start after 6 beats.

It provides the following functions:
* `run()` needs to be called in your `draw()` as the last statement
* `enableKeyPresses()` to enable the library to listen for keys
  * `i` show info window
  * `0` press once to reset timer, press multiple times to set bpm to your 'press'-timing
  * `-` lower bpm
  * `=` raise bpm
* `setSurfaceTitle()` to return information for BPM, beatCount and frameRate to be shown in your surface title
* `linear` main function that gives a normalized linear progress value from 0 to 1 for any given amount of beats, or with a given delay
  * linear() gives progress in 1 beat
  * linear(4) gives progress in 4 beats
  * linear(4,3) gives progress in 4 beats, with a delay of 3 beats
* `ease` same function as `linear()` but does not use a linear progression, but an 'eased' one, using the first quarter part of a sine function. Again, can be called without arguments, with 1 (duration in beats) or another one (delay in beats)
* `linearBounce`same function as `linear()` but goes from 0 to 1 to 0 in the same amount of time. Useful for shrinking or growing of visuals
* `easeBounce`same function as `ease()` but goes from 0 to 1 to 0 in the same amount of time. Useful for shrinking or growing of visuals
* `every[n]` boolean that returns true every n beats. Gives true for the duration of 1 beat. Limited from 1 to 16.
* `every_once[n]` same boolean as `every[n]` but is only true for 1 frame. Useful for changing a variable onces every n beats

## How to
Install the library by downloading the latest release of through the Processing contribution manager: go to `Processing > Sketch > Import Library... > Manage Libraries...` and search for "BPM timings" and click install.

## Usage

```
// Import the library to your sketch
import bpm.library.*;

// Create the bpm object
BeatsPerMinute bpm;

void setup(){
  // Initialize the bpm
  bpm = new BeatsPerMinute(this, 60);

  // Enable the info window when pressing i
  // or raise/lower bpm with = and -
  // or reset timing with 0
  bpm.enableKeyPresses();

  // always show the info window
  bpm.showInfo = true;
}

void draw(){
  background(100);
  
  // Use a timing function
  float radius = 20 + bpm.linear() * 50;
  // or: float radius = map(bpm.linear(), 0, 1, 20, 70);
  // or: float radius = lerp(20, 70, bpm.linear());
  ellipse(width/2,height/2,radius,radius);

  // Run the timing every frame
  bpm.run();
}
```

## Examples
You can find all these examples in `Processing -> File - Examples - Contributed Libraries - BPM timings for Processing`.

### [animatedSVG](https://github.com/vincentsijben/bpm-timings-for-processing/blob/master/examples/animatedSVG/animatedSVG.pde)
```
/**
 * animatedSVG
 * by Vincent Sijben
 *
 * Animate an SVG shape on specific beats. 
 * I've hidden both glasses and left foot shapes in the original SVG.
 * Created a new glasses.svg and foot.svg in vector software (Adobe Illustrator).
 * Every beat, the size of the glasses grow and shrink.
 * Every 4 beats, the dude's left foot 'taps'.
 *
 * Used a free SVG from https://www.svgrepo.com/svg/191/cool.
 */
```
![animatedSVG](assets/example-animatedsvg.gif)
---

### [beatcount](examples/beatcount/)
```
/**
 * beatcount
 * by Vincent Sijben
 *
 * Show the (rounded) beatcount as text.
 * You can see how you could use the setSurfaceTitle() function as well.
 */
```
![beatcount](assets/example-beatcount.gif)
---

### colorPalettes
![beatcount](assets/example-colorpalettes.gif)

### delay
![beatcount](assets/example-delay.gif)

### randomColor
![beatcount](assets/example-randomcolor.gif)

### randomGridSpots
![beatcount](assets/example-randomgridspots.gif)

### wiggle
![beatcount](assets/example-wiggle.gif)

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
* Name the release `BPM Library Release [version] ([pretty version])` and tag it wit tag **latest**
* Upload the `distribution\BPM-[version]\download\BPM.txt` and `distribution\BPM-[version]\download\BPM.zip` into the release.
* Remove the **latest** tag from older versions

## Create your own Processing Library
Thanks [Elie Zananiri](https://github.com/prisonerjohn) for pointing out these things...

There are a few steps:
* Package your library according to the guidelines here: https://github.com/processing/processing/wiki/Library-Guidelines
* Add a properties file according to the guidelines here: https://github.com/processing/processing/wiki/Library-Basics#describing-your-library--libraryproperties
* Check the revision numbers for Processing 4 [here](https://github.com/processing/processing4/blob/main/build/shared/revisions.md).
* Publish your library and properties to a static URL according to the guidelines here: https://github.com/processing/processing/wiki/Library-Basics#advertising-your-library

* If you're hosting your library on GitHub, use the GitHub Releases feature. Create a release tagged "latest" and move that tag up to the new commit whenever you make an update. Note that you can also tag each release with its version number, in case you want to make older releases still available on GitHub. Check out [processing-video]() for a good example of that.

So my URLs are always available through:
* https://github.com/vincentsijben/bpm-timings-for-processing/releases/download/latest/BPM.txt
* https://github.com/vincentsijben/bpm-timings-for-processing/releases/download/latest/BPM.zip



I've based my Library on the [Processing Library Template](https://github.com/processing/processing-library-template). You could also check out the [Coding Train tutorial](https://www.youtube.com/watch?v=pI2gvl9sdtE).
