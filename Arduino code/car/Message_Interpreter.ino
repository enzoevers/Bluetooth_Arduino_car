void interpretMessage(String receivedMessage) {

  /*
   * For- and backward
   */
  if (receivedMessage == "f") {
    Serial.println("About to go forward");
    motorLeft.run(FORWARD);
    motorRight.run(FORWARD);
    motorLeft.setSpeed(250);
    motorRight.setSpeed(250);
  }
  else if (receivedMessage == "b") {
    Serial.println("About to go backward");
    motorLeft.run(BACKWARD);
    motorRight.run(BACKWARD);
    motorLeft.setSpeed(250);
    motorRight.setSpeed(250);
  }

  /*
   * Stop
   */
  else if (receivedMessage == "s") {
    Serial.println("STOP!!!");
    motorLeft.setSpeed(0);
    motorRight.setSpeed(0);
  }

  /*
   * Turning
   */
  //Forward
  else if(receivedMessage == "lf"){
    Serial.println("Turning left forward");
    motorLeft.run(FORWARD);
    motorRight.run(FORWARD);
    motorLeft.setSpeed(0);
    motorRight.setSpeed(150);
  }
  else if(receivedMessage == "rf"){
    Serial.println("Turning right forward");
    motorLeft.run(FORWARD);
    motorRight.run(FORWARD);
    motorLeft.setSpeed(150);
    motorRight.setSpeed(0);
  }

 // Backward
  else if(receivedMessage == "lb"){
    Serial.println("Turning left backward");
    motorLeft.run(BACKWARD);
    motorRight.run(BACKWARD);
    motorLeft.setSpeed(0);
    motorRight.setSpeed(150);
  }
  else if(receivedMessage == "rb"){
    Serial.println("Turning right backward");
    motorLeft.run(BACKWARD);
    motorRight.run(BACKWARD);
    motorLeft.setSpeed(150);
    motorRight.setSpeed(0);
  }
}



