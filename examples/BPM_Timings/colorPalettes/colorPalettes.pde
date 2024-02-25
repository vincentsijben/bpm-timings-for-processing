/**
 * colorPalettes
 * https://github.com/vincentsijben/bpm-timings-for-processing
 *
 * Given a number of https://coolors.co color palette URLs, generate a unique random color for
 * the current color palette each beat.
 * Pick the next palette every 8 beats.
 */

import bpm.library.beatsperminute.*;

BeatsPerMinute bpm;
String[] urls = {
  "https://coolors.co/palette/f6bd60-f7ede2-f5cac3-84a59d-f28482",
  "https://coolors.co/palette/03045e-023e8a-0077b6-0096c7-00b4d8-48cae4-90e0ef-ade8f4-caf0f8",
  "https://coolors.co/palette/ff7b00-ff8800-ff9500-ffa200-ffaa00-ffb700-ffc300-ffd000-ffdd00-ffea00",
  "https://coolors.co/palette/001219-005f73-0a9396-94d2bd-e9d8a6-ee9b00-ca6702-bb3e03-ae2012-9b2226"
};
ArrayList<ArrayList<Integer>> palettes = new ArrayList<ArrayList<Integer>>();
int paletteIndex = 0;
int colorIndex = 0;
int currentRandomNumber = 0;

void setup () {
  size(500, 500);
  bpm = new BeatsPerMinute(this)
    //.setBPM(120)
    //.showInfoPanel()
    //.setInfoPanelY(200)
    //.setInfoPanelKey('o')
    //.disableKeyPress()
    ;

  for (String url : urls) {
    palettes.add(createPalette(url));
  }
}

void draw() {
  background(200);

  for (int i = 0; i < palettes.size(); i++) {

    ArrayList<Integer> palette = palettes.get(i);
    for (int j = 0; j < palette.size(); j++) {
      int hexcolor = palette.get(j);
      fill(hexcolor);
      rect(j*10, i*40, 10, 20);
    }
  }

  fill(palettes.get(paletteIndex).get(colorIndex));
  rect(width/2, 0, width/2, height);

  if (bpm.every_once[8]) {
    // every 8th beat, pick the next palette
    paletteIndex++;
    paletteIndex = paletteIndex%palettes.size();
  }
  if (bpm.every_once[1]) {
    colorIndex = generateDifferentRandom(palettes.get(paletteIndex).size());
  }
  fill(0);
  textSize(30);
  text("Beat: " + int(bpm.beatCount), 20, 260);
  text("Palette: " + (paletteIndex + 1), 20, 300);
  text("Color: " + (colorIndex + 1), 20, 340);

}

ArrayList<Integer> createPalette(String coolorsURL) {
  String[] clrs = splitTokens(coolorsURL, "/");
  //sometimes /palette/ is in URL, so always get the last part from the URL
  String[] c = splitTokens(clrs[clrs.length-1], "-");
  ArrayList<Integer> col = new ArrayList<Integer>();
  for (int i = 0; i < c.length; i++) {
    col.add(unhex("FF" + c[i]));
  }
  return col;
}

int generateDifferentRandom(int max){
  return generateDifferentRandom(0, max);
}

int generateDifferentRandom(int min, int max){
  int result = int(random(min, max));
  if (result == currentRandomNumber) return generateDifferentRandom(min,max);
  else currentRandomNumber = result;
  return result;
}
