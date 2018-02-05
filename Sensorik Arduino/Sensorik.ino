#include "AirQuality.h"
#include "Arduino.h"
#include <Digital_Light_TSL2561.h>
#include <Wire.h>


AirQuality airqualitysensor;
int current_quality =-1;
int TMP36 =A0; //temp

void setup() {
  // put your setup code here, to run once:
    //Serial.begin(9600);  
    //airqualitysensor.init(14); //Air Qualität Sensor an A0 anschließen
    Wire.begin();
    TSL2561.init(); //Lichtsensor an IIC anschließen
    InitBleConnection(); //Initalisiere BLE Broadcast
}

void loop() {
  // put your main code here, to run repeatedly:
  int DelayBreak=20;

  //Frage alle Sensordaten ab
  int AirQuality= getAirQuality();
  delay(DelayBreak);
  int LightValue=getLightValue();
  delay(DelayBreak);
  int Temperatur=getTemperatur();

  //Sende Sensordaten via Ble Broadcast
  SendMessageToPairedDevice(Temperatur,AirQuality,LightValue);
}




