#include <SoftwareSerial.h>
#include <Wire.h>
#include "Adafruit_Trellis.h"

Adafruit_Trellis matrix0 = Adafruit_Trellis();
Adafruit_TrellisSet trellis =  Adafruit_TrellisSet(&matrix0);
#define NUMTRELLIS 1
#define numKeys (NUMTRELLIS * 16)

const int c1 = 13;
const int c1s = 12;
const int d1 = 11;
const int d1s = 10;
const int e1 = 9;
const int f1 = 8;
const int f1s = 7;
const int g1 = 6;
const int g1s = 5;
const int a1 = 4;
const int a1s = 1;
const int b1 = 0;

int btnC1;
int btnC1S;
int btnD1;
int btnD1S;
int btnE1;
int btnF1;
int btnF1S;
int btnG1;
int btnG1S;
int btnA1;
int btnA1S;
int btnB1;
int bluetoothTx = 2;  // TX-O pin of bluetooth mate, Arduino D2
int bluetoothRx = 3;  // RX-I pin of bluetooth mate, Arduino D3

int lastC1State = 0;
int lastC1SState = 0;
int lastD1State = 0;
int lastD1SState = 0;
int lastE1State = 0;
int lastF1State = 0;
int lastF1SState = 0;
int lastG1State = 0;
int lastG1SState = 0;
int lastA1State = 0;
int lastA1SState = 0;
int lastB1State = 0;

SoftwareSerial bluetooth(bluetoothTx, bluetoothRx);

void setup() {
  Serial.begin(9600);
  trellis.begin(0x70);
  bluetooth.begin(115200);
  pinMode(c1, INPUT);
  pinMode(c1s, INPUT);
  pinMode(d1, INPUT);
  pinMode(d1s, INPUT);
  pinMode(e1, INPUT);
  pinMode(f1, INPUT);
  pinMode(f1s, INPUT);
  pinMode(g1, INPUT);
  pinMode(g1s, INPUT);
  pinMode(a1, INPUT);
  pinMode(a1s, INPUT);
  pinMode(b1, INPUT);
}

void loop() {
  delay(30);
  btnC1 = digitalRead(c1);
  btnC1S = digitalRead(c1s);
  btnD1 = digitalRead(d1);
  btnD1S = digitalRead(d1s);
  btnE1 = digitalRead(e1);
  btnF1 = digitalRead(f1);
  btnF1S = digitalRead(f1s);
  btnG1 = digitalRead(g1);
  btnG1S = digitalRead(g1s);
  btnA1 = digitalRead(a1);
  btnA1S = digitalRead(a1s);
  btnB1 = digitalRead(b1);

  if (Serial.available()) {
    bluetooth.print((char)Serial.read());
  }

  if (trellis.readSwitches()) {
    for (uint8_t i = 0; i < numKeys; i++) {
      if (trellis.justPressed(i)) {
        if(i == 15){
          bluetooth.print(String(i));
          bluetooth.print("/");
        }
        Serial.println(i); 
        if (i == 8 || i == 12) {
          trellis.setLED(i);
          bluetooth.print(String(i));
          bluetooth.print("/");
        } else {
          if (trellis.isLED(i)) {
            trellis.clrLED(i);
            bluetooth.print("off_");
            bluetooth.print(String(i));
            bluetooth.print("/");
          } else {
            trellis.setLED(i);
            bluetooth.print(String(i));
            bluetooth.print("/");
          }
        }
      }
      if(trellis.justReleased(i) && i == 8 || i == 12){
           trellis.clrLED(i);
      }
    }
    trellis.writeDisplay();
  }

  /*if (btnC1 != lastC1State) {
    if (btnC1 == HIGH) {
      bluetooth.print("c1/");
      Serial.println("c1");
    }
    delay(50);
    }

    if (btnC1S != lastC1SState) {
    if (btnC1S == HIGH) {
      bluetooth.print("c1s/");
      Serial.println("c1s");
    }
    delay(50);
    }

    if (btnD1 != lastD1State) {
    if (btnD1 == HIGH) {
      bluetooth.print("d1/");
      Serial.println("d1");
    }
    delay(50);
    }

    if (btnD1S != lastD1SState) {
    if (btnD1S == HIGH) {
      bluetooth.print("d1s/");
      Serial.println("d1s");
    }
    delay(50);
    }

    if (btnE1 != lastE1State) {
    if (btnE1 == HIGH) {
      bluetooth.print("e1/");
      Serial.println("e1");
    }
    delay(50);
    }

    if (btnF1 != lastF1State) {
    if (btnF1 == HIGH) {
      bluetooth.print("f1/");
      Serial.println("f1");
    }
    delay(50);
    }

    if (btnF1S != lastF1SState) {
    if (btnF1S == HIGH) {
      bluetooth.print("drum/");
      Serial.println("f1s");
    }
    delay(50);
    }

    if (btnG1 != lastG1State) {
    if (btnG1 == HIGH) {
      bluetooth.print("snare/");
      Serial.println("g1");
    }
    delay(50);
    }

    if (btnG1S != lastG1SState) {
    if (btnG1S == HIGH) {
      bluetooth.print("g1s/");
      Serial.println("g1s");
    }
    delay(50);
    }

    if (btnA1 != lastA1State) {
    if (btnA1 == HIGH) {
      bluetooth.print("a1/");
      Serial.println("a1");
    }
    delay(50);
    }
    if (btnA1S != lastA1SState) {
    if (btnA1S == HIGH) {
      bluetooth.print("a1s/");
      Serial.println("a1s");
    }
    delay(50);
    }

    if (btnB1 != lastB1State) {
    if (btnB1 == HIGH) {
      bluetooth.print("b1/");
      Serial.println("b1");
    }
    delay(50);
    }

    lastC1State = btnC1;
    lastC1SState = btnC1S;
    lastD1State = btnD1;
    lastD1SState = btnD1S;
    lastE1State = btnE1;
    lastF1State = btnF1;
    lastF1SState = btnF1S;
    lastG1State = btnG1;
    lastG1SState = btnG1S;
    lastA1State = btnA1;
    lastA1SState = btnA1S;
    lastB1State = btnB1;*/
}
