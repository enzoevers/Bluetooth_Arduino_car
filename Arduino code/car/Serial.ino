bool startReading = false;
bool stopReading = true;
String message = "";

void checkMessage()
{
  if (Serial.available() > 0)
  {
    char incomingChar = (char)Serial.read();

    if (incomingChar == '$' && startReading)
    {
      startReading = false;
      stopReading = true;
      showMessage();
      interpretMessage(message);
    }

    makeMessage(incomingChar);

    if (incomingChar == '#' && stopReading)
    {
      message = "";
      stopReading = false;
      startReading = true;
    }
  }
}

void makeMessage(char Char)
{
  if (startReading) {
    message += Char;
  }
}

void showMessage()
{
  Serial.print("Received message: ");
  Serial.println(message);
}
