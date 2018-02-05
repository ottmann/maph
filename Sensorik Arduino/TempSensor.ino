//Source: https://funduino.de/nr-9-temperatur-messen

int getTemperatur()
{
  int sensorwert=analogRead(TMP36); //Auslesen des Sensorwertes.
  int temperatur= map(sensorwert, 0, 410, -50, 150); //Umwandeln des Sensorwertes mit Hilfe des "map" Befehls.
  return temperatur;
}

