#include <Servo.h>

const int NUMBER_OF_BOTTLES = 4;
const char PING_FLAG = 'P';
const char ROTATE_FLAG = 'R';
const char BEGIN_STATIONARY_OPERATION_COMMAND_FLAG = 'B';
const char BOTTLE_DELIMITER = ','; 
const char STIR_FLAG = 'S';
const char ICE_FLAG = 'I';
const char END_STATIONARY_OPERATION_COMMAND_FLAG = 'E';
const int RELAY_PIN_NUMBER = 2;

int pulse_count = 0;

const int innerEncoderPin = 9; 

int pulse_after_inner_count = 0;

boolean inner_old_state = LOW; 
// current state of the encoder 
boolean inner_current_state = LOW; 


const char OPERATION_COMPLETE_FLAG = 'C';
const char PING_RESPONSE_FLAG = 'A';

const int encoderPin = A0; 
const int motorPin = 4; 
// old state of the encoder 
boolean old_state = LOW; 
// current state of the encoder 
boolean current_state = LOW; 

int ticksToGo = 0;
const int full_turn_count = 60;
const int next_cup_turn_count = 10;

// For stirrer
const int frotherMotor = 10;
const int hBridgeIn2 = 11;
const int hBridgeIn1 = 12;
const int enabler = 13;

const int icePin = 7;

// For pinch valves
const int pinch1Pin = 3;
const int pinch2Pin = 5;
const int pinch3Pin = 9;
//const int pinch4Pin = 6;

Servo pinch1;   // create servo object to control a servo
Servo pinch2;
Servo pinch3;
//Servo pinch4;

void dispenseIce()
{
 for(int i=0;i<50;i++)
 {
   digitalWrite(icePin,HIGH);
   delay(10);
   digitalWrite(icePin,LOW);
   delay(10);
 }
}

void pourOzNumFromPinchNum(int oz1, int oz2, int oz3, int oz4)
{
     int d1 = ouncesToMilliseconds(oz1);
     int d2 = ouncesToMilliseconds(oz2);
     int d3 = ouncesToMilliseconds(oz3);
     //int d4 = ouncesToMilliseconds(oz4);
     int initialTime = millis();

     //turn on the servos that have to dispense liquid
     //assume all servos initially off

     
     if(oz1 != 0)
       pinch1.write(180);
     if(oz2 != 0)
       pinch2.write(0);
     if(oz3 != 0)
       pinch3.write(0);
     //if(oz4 != 0)
       //pinch4.write(180);
       
     int timeDif = millis()-initialTime;
     boolean isClosed1 = false;
     boolean isClosed2 = false;
     boolean isClosed3 = false;
     //boolean isClosed4 = false;

     //loop until time has surpassed for all motors to turn off
     while(timeDif<d1 || timeDif<d2 || timeDif<d3)// || timeDif<d4)
     {
       if(timeDif>=d1 && !isClosed1)
       {
         pinch1.write(10);
         isClosed1 = true;
       }
       if(timeDif>=d2 && !isClosed2)
       {
         pinch2.write(180);
         isClosed2 = true;
       }
       if(timeDif>=d3 && !isClosed3)
       {
         pinch3.write(180);
         isClosed3 = true;
       }
       //if(timeDif>=d4 && !isClosed4)
       //{
       //  pinch4.write(0);
       //  isClosed4 = true;
       //}
       

       timeDif = millis()-initialTime;
     }
     
   
   pinch1.write(10);
   pinch2.write(180);
   pinch3.write(180);
   //pinch4.write(0);
   delay(500);
 } 

int ouncesToMilliseconds(int ounces)
{
  /*
 switch (ounces) {
      case 0:
        Serial.write('0');     
        break;
      case 1:
        Serial.write('1');      
        break;
      case 2:
        Serial.write('2');    
        break;
      case 3:
        Serial.write('3');     
        break;
      case 4:
        Serial.write('4');     
        break;
 }
 */
 return ((ounces-0.7499)*1000)/(0.35);
}

void setupPinchValves()
{
 pinch1.attach(pinch1Pin);
 pinch2.attach(pinch2Pin);
 pinch3.attach(pinch3Pin);
 //pinch4.attach(pinch4Pin);

 // Closing all pinch valves
 pinch1.write(10);
 pinch2.write(180);
 pinch3.write(180);
 /* Test code
 delay(2000);
 
 pinch1.write(180);
 pinch2.write(0);
 pinch3.write(0);
 */
 
 //pinch4.write(0);
}

void stirrerUp()
{
    digitalWrite(enabler, LOW);
    digitalWrite(hBridgeIn1, HIGH);
    digitalWrite(hBridgeIn2, LOW);
    digitalWrite(enabler, HIGH);
}

void stirrerDown()
{
    digitalWrite(enabler, LOW);
    digitalWrite(hBridgeIn1, LOW);
    digitalWrite(hBridgeIn2, HIGH);
    digitalWrite(enabler, HIGH);
}

void stopStirrerMotor()
{
    digitalWrite(enabler, LOW);
    digitalWrite(hBridgeIn1, LOW);
    digitalWrite(hBridgeIn2, LOW);
    digitalWrite(enabler, HIGH);
}

void resetToTop()
{
 stirrerUp();
 delay(700);
 stopStirrerMotor();
}


void executeFrother()
{
 for(int i=0;i<70;i++)
 {
   digitalWrite(frotherMotor,HIGH);
   delay(20);
   digitalWrite(frotherMotor,LOW);
   delay(20);
 }
}

void setupStirrer() {
   // initialize the digital pin as an output.
  pinMode(hBridgeIn1, OUTPUT);
  pinMode(hBridgeIn2, OUTPUT);
  pinMode(enabler, OUTPUT);
  pinMode(frotherMotor,OUTPUT);
  //reset to top
  resetToTop();
}

void stir() {
  stirrerDown();
  delay(675); //575
  stopStirrerMotor();
  delay(1000);

  //turn on frother
  executeFrother();
  delay(1000);
   
  resetToTop();
  delay(500);
}

void rotateOneCup()
{ 
  ticksToGo += next_cup_turn_count;
  int ticksGone = 0;
  
  while(ticksToGo > 0)
  {   
    current_state = digitalRead(encoderPin); 
    if (current_state != old_state) 
    {
      ticksToGo--;
      ticksGone++;
    }
    old_state = current_state;
    if (ticksGone < 2)
        digitalWrite(motorPin,HIGH); 
    else if (ticksToGo > 2)
    {
        digitalWrite(motorPin,HIGH); 
        delay(ticksToGo);
        digitalWrite(motorPin,LOW); 
        delay(10 - ticksToGo);
    }
    else
    {
        digitalWrite(motorPin,HIGH); 
        delay(1);
        digitalWrite(motorPin,LOW); 
        delay(8);
    }
  }
  
  for(int i = 0; i < 200; i++)
  {
    current_state = digitalRead(encoderPin); 
    if (current_state != old_state) 
    { 
      ticksToGo--;
    } 
    old_state = current_state;
    delay(1); 
  }
}

void autoAllign()
{
  pinMode(innerEncoderPin,INPUT); 
  digitalWrite(motorPin,HIGH); 
  inner_current_state = digitalRead(innerEncoderPin); 
  while(inner_current_state != 1) 
  { 
    inner_current_state = digitalRead(innerEncoderPin);
  } 
  while(pulse_count < 4)
  { 
    current_state = digitalRead(encoderPin); 
    if (current_state != old_state) 
    { 
      pulse_count++;
    } 
    old_state = current_state; 
  }
  digitalWrite(motorPin,LOW);
}

void setup()
{
  Serial.begin(9600);
  pinMode(motorPin,OUTPUT); 
  pinMode(encoderPin,INPUT);
  pinMode(icePin, OUTPUT);
  Serial.flush();
  setupStirrer(); 
  setupPinchValves();
  pinMode(RELAY_PIN_NUMBER, OUTPUT);
  //digitalWrite(RELAY_PIN_NUMBER, HIGH);
  //autoAllign();
}

void loop()
{
  
  if (Serial.available() > 0) 
  {
    char messageType = Serial.read();
    if (messageType == PING_FLAG)
    {
      Serial.write(PING_RESPONSE_FLAG);
    }
    if (messageType == ROTATE_FLAG)
    {
      rotateOneCup(); 
      Serial.write(OPERATION_COMPLETE_FLAG);
    }
    if (messageType == BEGIN_STATIONARY_OPERATION_COMMAND_FLAG)
    {
      int ouncesToPour[NUMBER_OF_BOTTLES];
      boolean stirBool = false;
      boolean iceBool = false;
      
      for (int i = 0; i < NUMBER_OF_BOTTLES; i++)
      {
        ouncesToPour[i] = 0;
        for (char let = Serial.read(); let != BOTTLE_DELIMITER; let = Serial.read())
        {
           switch (let) {
            case '0':
              ouncesToPour[i] = 0; 
              //Serial.write(let);     
              break;
            case '1':
              ouncesToPour[i] = 1;
              //Serial.write(let);      
              break;
            case '2':
              ouncesToPour[i] = 2; 
              //Serial.write(let);    
              break;
            case '3':
              ouncesToPour[i] = 3; 
              //Serial.write(let);     
              break;
            case '4':
              ouncesToPour[i] = 4; 
              //Serial.write(let);     
              break;
           }
          
        }
        //switch (ouncesToPour[i]) {
         // case 0:
         //  Serial.write('0');     
         //   break;
         // case 1:
         //   Serial.write('1');      
         //   break;
         // case 2:
         //   Serial.write('2');    
         //   break;
         // case 3:
         //   Serial.write('3');     
         //   break;
         // case 4:
         //   Serial.write('4');     
         //   break;
         //}
         
      }  
      
      for (char let = Serial.read(); let != END_STATIONARY_OPERATION_COMMAND_FLAG; let = Serial.read())
      {
        if (let == STIR_FLAG)
          stirBool = true;
        if (let == ICE_FLAG)
          iceBool = true;
      }
      //TODO: pour the ounces in the array (ie, if ounces[2] is 4
      //then pour 4 ounces from bottle 2). if stir is true,
      //activate the cup. If ice is true, activate the ice tray
      //NO NEED TO ROTATE: THOSE CALCULATIONS HAVE ALREADY BEEN
      //DONE FOR YOU
      pourOzNumFromPinchNum(ouncesToPour[0], ouncesToPour[1], ouncesToPour[2], ouncesToPour[3]);
      
      if (stirBool)
        stir();
      if (iceBool)
        dispenseIce();
     
      
      
      Serial.write(OPERATION_COMPLETE_FLAG);
    }
  }
  
  /*
  stirrerDown();
  delay(675); //575
  stopStirrerMotor();
  delay(1000);
   
  resetToTop();
  delay(500);
  */
}
