//Source: http://wiki.seeed.cc/Grove-Digital_Light_Sensor/
#include <Digital_Light_TSL2561.h>
int getLightValue()
{
  int LightValue=TSL2561.readVisibleLux();
  if(LightValue>254)
  {
    LightValue=254;
  }
  
  //Serial.println("LV");
  //Serial.println(LightValue);
  return LightValue;
}

