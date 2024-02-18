package bpm.library;

public class PushButton {
  int signalPressed;
  int digitalPort;
  boolean pressed;
  boolean pressedOnce;
  boolean actionTaken;
  char keyboardKey;

  public PushButton(int digitalPort, char keyboardKey, int signalPressed) {
    this.digitalPort = digitalPort;
    this.keyboardKey = keyboardKey;
    this.signalPressed = signalPressed; // By default, pressing a button returns Arduino.HIGH
    this.pressed = Boolean.FALSE;
    this.pressedOnce = Boolean.FALSE;
    this.actionTaken = Boolean.FALSE;
  }
}
