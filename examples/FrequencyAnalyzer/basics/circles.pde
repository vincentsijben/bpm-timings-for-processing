class Circle {
  float x;
  float y;
  float r;
  float rSpeed;
  int counter;   
  color col;
  int index;

  Circle(int i) {
    this.x = random(-pg.width / 4, pg.width / 4);
    this.y = random(-pg.height / 4, pg.height / 4);
    this.r = 0;
    this.rSpeed = random(0.1, 0.6);
    this.counter = circles.size()+1;
    this.col = color(255, 199, 100);
    if (this.counter % 2 == 1) this.col = color(91, 244, 233);
    this.index = i;
  }
}

void drawCircles() {
  pg.beginDraw();
  pg.fill(50, 30);
  pg.rect(0, 0, width, height);
  pg.translate(pg.width / 2, pg.height / 2);
  pg.strokeWeight(2);
  pg.noFill();

  for (int i = 0; i < circles.size(); i++) {
    Circle c = circles.get(i);

    pg.pushMatrix();
    pg.stroke(c.col);
    pg.rotate(radians(c.r));
    pg.circle(c.x, c.y, lerp(0, 100, fa.getAvg(c.index)));
    pg.popMatrix();

    c.r+=c.rSpeed;
  }

  pg.endDraw();
  image(pg, 0, 0);
}
