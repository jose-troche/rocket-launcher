/*

The igniter: 
Turns on a relay when an IGNITE string is sent via bluetooth connection

Author: Jose Troche

Portions of this code were extracted from BluetoothShield Demo Code Slave_Recv
(https://github.com/Seeed-Studio/Bluetooth_Shield_Demo_Code/)

SeeedStudio Bluetooth and Relay modules (shields) were used
*/

#include <SoftwareSerial.h>   //Software Serial Port

// Make sure that jumpers in bluetooth module are set RX: 3, TX: 2, opposite to:
#define RxD         2
#define TxD         3

// The input string that will turn on the relay
#define IGNITE     "1"

// The pin used to turn the relay on and off
#define RELAY       4

#define RELAYON()     digitalWrite(RELAY, HIGH)
#define RELAYOFF()    digitalWrite(RELAY, LOW)

SoftwareSerial blueToothSerial(RxD,TxD);

String inputString = "";         // A string to hold incoming data
boolean stringComplete = false;  // Whether the string is complete
long startTime = millis();

void setup()
{
  Serial.begin(9600);
  pinMode(RxD, INPUT);
  pinMode(TxD, OUTPUT);
  pinMode(RELAY, OUTPUT);
  RELAYOFF();
  
  setupBlueToothConnection();
  
  // Reserve 200 bytes for the inputString:
  inputString.reserve(200);
}

void loop()
{   
  // Turn on the relay for a few seconds when the IGNITE string is received
  if (stringComplete) {
    if (inputString == IGNITE && ( (millis()-startTime) > 100) ) {
      // The elapsed time is calculated to ignore IGNITEs sent while
      // the relay is ON
      Serial.println("Turning ON relay");
      RELAYON();
      delay(10000);
      Serial.println("Turning OFF relay");
      RELAYOFF();
      startTime = millis();
    }
    
    // Clear the inputString:
    inputString = "";
    stringComplete = false;
  }

  // Read a line string from the bluetooth connection
  while (blueToothSerial.available()) {
    char inputCharacter = (char) blueToothSerial.read();
    inputString += inputCharacter;
    if (inputCharacter == '\n') {
      inputString.trim();
      Serial.print("Input string: ");
      Serial.println(inputString);
      stringComplete = true;
    }
  }
}

// Initialize Bluetooth
void setupBlueToothConnection()
{
  blueToothSerial.begin(38400);                           // Set BluetoothBee BaudRate to default baud rate 38400
  blueToothSerial.print("\r\n+STWMOD=0\r\n");             // set the bluetooth work in slave mode
  blueToothSerial.print("\r\n+STNA=SeeedBTSlave\r\n");    // set the bluetooth name as "SeeedBTSlave"
  blueToothSerial.print("\r\n+STOAUT=1\r\n");             // Permit Paired device to connect me
  blueToothSerial.print("\r\n+STAUTO=0\r\n");             // Auto-connection should be forbidden here
  delay(2000);                                            // This delay is required.
  blueToothSerial.print("\r\n+INQ=1\r\n");                // make the slave bluetooth inquirable
  Serial.println("The slave bluetooth is inquirable!");
  delay(2000);                                            // This delay is required.
  blueToothSerial.flush();
}
