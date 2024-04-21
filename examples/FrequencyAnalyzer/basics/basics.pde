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
    //.setAudioInputMode(AudioInputMode.AUDIO_FILE, 128) // also set bufferSize, needs to be power of 2. Lower amount means less audio resolution and decreasing delay
    //.setAudioInputMode(AudioInputMode.LINE_IN)
    .showInfoPanel()
    ;
}


void draw() {
  background(0); // Clear the screen with a black background
  noFill();

  // Example visualization: Draw the frequency spectrum using FFT analysis
  // Only visible when MONO output (mixed fft)
  //println(fa.specSize());
  for (int i = 0; i < fa.specSize(); i++) {
    // Calculate the amplitude and position for each frequency band
    float amplitude = fa.getBand(i);
    float x = map(i, 0, fa.specSize(), 0, width);
    float y = map(amplitude, 0, 100, height, 0); // Scale amplitude to window height
    stroke(255);
    line(x, height, x, y);
  }

  // Only visible when STEREO output (fftRight and fftLeft)
  // Visualize the left channel
  //drawChannelFFT(fa.getFFTLeft(), color(255, 255, 0), 0, height / 2);

  //// Visualize the right channel
  //drawChannelFFT(fa.getFFTRight(), color(0, 0, 255), height / 2, height);

  fill(0, 200, 0);
  circle(width/4*1, height/2, fa.getAvgRawRight(0)*30);
  circle(width/4*2, height/2, fa.getAvgRaw(10)*30);
  circle(width/4*3, height/2, fa.getAvgRaw(20)*30);
}

void drawWaveform(float[] buffer, float x, float y, float w, float h, int strokeColor) {
  stroke(strokeColor);
  beginShape();
  //println(buffer.length);
  for (int i = 0; i < buffer.length; i++) {
    float xVal = map(i, 0, buffer.length, x, x + w);
    float yVal = map(buffer[i], -1, 1, y + h, y);
    vertex(xVal, yVal);
  }
  endShape();
}

//void drawChannelFFT(FFT fft2, int color2, float startY, float endY) {
//  stroke(color2);
//  //println(fft2.specSize());
//  for (int i = 0; i < fft2.specSize(); i++) {
//    // Map the frequency band index to the x-coordinate
//    float x = map(i, 0, fft2.specSize(), 0, width);
//    // Map the amplitude to the y-coordinate, scaling by the height of the channel visualization area

//    float amplitude = fft2.getBand(i) * 5; // Scale factor for visual effect
//    float y = map(amplitude, 0, 100, endY, startY); // Invert direction for visual effect
//    line(x, endY, x, y);
//  }
//}
