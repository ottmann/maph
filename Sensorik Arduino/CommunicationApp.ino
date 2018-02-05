#include <SoftwareSerial.h>

#define RxD 2
#define TxD 3

SoftwareSerial BLE(RxD, TxD);

int x = 0;

void InitBleConnection()
{
    Serial.begin(9600);
    pinMode(RxD, INPUT);
    pinMode(TxD, OUTPUT);
    BLE.begin(9600);
    delay(1000);
    BLE.print("AT+CLEAR");//Alle vorherigen AT Commands Clearen
    delay(20);
    BLE.print("AT+NAMEDeppBox"); //Anzeigenamen des BLE Moduls ändern 
    delay(20);
}

void SendMessageToPairedDevice(int TemperaturValue, int AirQuality, int LightValue)
{
    String Message = String(TemperaturValue)+";"+String(AirQuality)+";"+String(LightValue);
    BLE.print(Message);
    delay(500);
}


