/* tried to recreate https://www.instagram.com/reel/C2dEMGFsg8E/ */
import nl.genart.bpm.beatsperminute.*;

ArrayList<Rectangles> rectangles = new ArrayList<Rectangles>();
BeatsPerMinute bpm;
int gridSize = 50;

void setup() {
  size(500, 200);
  bpm = new BeatsPerMinute(this)
    .setBPM(120)
    ;

  for (int x=0; x<width; x+=gridSize) {
    for (int y=0; y<height; y+=gridSize) {
      rectangles.add(new Rectangles(gridSize/2 + x, gridSize/2 + y));
    }
  }
}

void draw() {
  background(50);

  for (int i=0; i<rectangles.size(); i++) {
    Rectangles r = rectangles.get(i);
    r.draw();
  }
}

class Rectangles {
  float pW, pH, w, h, x, y, r;
  
  Rectangles(float xTemp, float yTemp) {
    r = 90;
    pW = 0;
    pH = 0;
    w = random(gridSize * 0.1, gridSize * 0.9);
    h = random(gridSize * 0.1, gridSize * 0.9);
    x = xTemp;
    y = yTemp;
  }

  void draw() {
    pushStyle();
    rectMode(CENTER);
    if (bpm.every_once[1]) {
      pW = w;
      pH = h;
      w = random(gridSize * 0.1, gridSize * 0.9);
      h = random(gridSize * 0.1, gridSize * 0.9);
    }
    noFill();
    stroke(200);
    strokeWeight(2);
    rect(x, y, lerp(pW, w, bpm.adsr(0.2)), lerp(pH, h, bpm.adsr(0.2)));
    popStyle();
  }
}