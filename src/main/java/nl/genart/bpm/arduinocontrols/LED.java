package bpm.library.arduinocontrols;

public class LED {
  // dont use private, because this class is not an inner class, so code inside ArduinoControls won't be able to access these properties
  // creating an inner class needs code adjustments for students they don't understand immediately, so I'll avoid this.
  int digitalPort;
  LEDMode mode;
  int value;

  public LED(int digitalPort, LEDMode mode) {
    this.digitalPort = digitalPort;
    this.mode = mode;
    this.value = 0;
  }
}
