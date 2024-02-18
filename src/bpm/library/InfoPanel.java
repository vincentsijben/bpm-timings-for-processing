package bpm.library;

import processing.core.*;
import processing.event.KeyEvent;

public class InfoPanel {
  PApplet parent;
  int x;
  int y;
  int w;
  int h;
  char keyboardKey;
  boolean show;
  PGraphics overlay;
  boolean keyPressedActionTaken;
  boolean enableKeyPress;

  InfoPanel(PApplet parent) {
    this.parent = parent;
    this.x = 0;
    this.y = 0;
    this.w = parent.width;
    this.h = 100;
    this.keyboardKey = 'i';
    this.show = Boolean.FALSE;
    this.keyPressedActionTaken = false;
    this.enableKeyPress = true;
    this.overlay = parent.createGraphics(w, h);

    parent.registerMethod("keyEvent", this);
  }

  public void keyEvent(KeyEvent event) {
    if (this.enableKeyPress) {
      if (event.getAction() == KeyEvent.PRESS) this.onKeyPress(event);
      else if (event.getAction() == KeyEvent.RELEASE) this.onKeyRelease(event);
    }
  }

  private void onKeyPress(KeyEvent event) {
    if (event.getKey()==this.keyboardKey && !this.keyPressedActionTaken) {
      this.show = !this.show;
      this.keyPressedActionTaken = true;
    }
  }

  private void onKeyRelease(KeyEvent event) {
    this.keyPressedActionTaken = false;
  }
}
