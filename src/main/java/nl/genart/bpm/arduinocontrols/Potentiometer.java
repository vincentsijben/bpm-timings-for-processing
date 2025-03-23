package nl.genart.bpm.arduinocontrols;

public class Potentiometer {
  float value;
  float smoothValue;
  int analogPort;
  int minValue;
  int maxValue;
  char keyboardKey;

  public Potentiometer(int analogPort, char keyboardKey, int minValue, int maxValue) {
    this.analogPort = analogPort;
    this.keyboardKey = keyboardKey;
    this.value = 0;
    this.smoothValue = 0;
    this.minValue = minValue;
    this.maxValue = maxValue;
  }
}
