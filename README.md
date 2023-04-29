# BPM timings for Processing

## Introduction
BPM timings for Processing is a small library for Processing that provides easy to use timing functions based on beats per minute. For example, you could easily grow or shrink shapes in any amount of beats, or delay shrinking a shape to start after 6 beats.

It provides the following functions:
* `run()` needs to be called in your `draw()` as the last statement
* `enableKeyPresses()` to enable the library to listen for keys `i`, `0`, `-` and `=` to show info window, reset timers and lower/raise bpm amount
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

### animatedSVG
![beatcount](data/example-animatedsvg.png)

### beatcount
![beatcount](data/example-beatcount.png)

### colorPalettes
![beatcount](data/example-colorpalettes.png)

### delay
![beatcount](data/example-delay.png)

### randomColor
![beatcount](data/example-randomcolor.png)

### randomGridSpots
![beatcount](data/example-randomgridspots.png)

### wiggle
![beatcount](data/example-wiggle.png)


Library template based on the [Processing Library Template](https://github.com/processing/processing-library-template). If you want to create your own library check out the [Coding Train tutorial](https://www.youtube.com/watch?v=pI2gvl9sdtE).
