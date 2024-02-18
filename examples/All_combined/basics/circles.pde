class Circle {
  float x = random(-pg.width / 4, pg.width / 4);
  float y = random(-pg.height / 4, pg.height / 4);
  float r = 0;
  float rSpeed = random(0.1, 0.6);
  int counter = circles.size()+1;   
  color col = color(255, 199, 100);
  int index;

  Circle(int i) {
    if (counter % 2 == 1) col = color(91, 244, 233);
    index = i;
  }
}

void drawCircles() {
  pg.beginDraw();
  pg.fill(col, 30);
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
