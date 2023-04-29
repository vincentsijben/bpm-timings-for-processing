# BPM timings for Processing

## Introduction
BPM timings for Processingi is a small library for Processing that provides easy to use timing functions based on beats per minute. 

## How to
Install the lirbrary by downloading the latest release of through the Processing contribution manager: go to Processing > Sketch > Import Library... > Manage Libraries... and search for "BPM timings" and click install.

## Usage

```
// Import the library to your sketch
import nl.genart.timings.bpm.*

// Create the bpm object
BeatsPerMinute bpm;

void setup(){
  // Initialize the bpm
  bpm = new BeatsPerMinute(this, 60);
}

void draw(){
  background(100);
  
  // Use a timing function
  float radius = 50 + bpm.beatDurationNormalizedLinear(1) * 100;
  // or: float radius = map(bpm.beatDurationNormalized(1), 0, 1, 50, 150);
  // or: float radius = lerp(50, 150, bpm.beatDurationNormalized(1));
  ellipse(width/2,height/2,radius,radius);

  // Run the timing every frame
  bpm.run();
}
```
