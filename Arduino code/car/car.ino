// Author: Enzo Evers
// Date: Summer 2016

// The Bluetooth HC-05 module is used

#include <AFMotor.h>

AF_DCMotor motorLeft(3);
AF_DCMotor motorRight(4);

void setup()
{
  Serial.begin(9600); 

  // turn on motor
  motorLeft.setSpeed(200);
  motorRight.setSpeed(200);

  motorLeft.run(RELEASE);
  motorRight.run(RELEASE);
  delay(500);
}

void loop()
{
  checkMessage();
}
