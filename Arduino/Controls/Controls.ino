#include <Smartcar.h>
#include <Wire.h>
#include <Servo.h>

Odometer encoderLeft, encoderRight;
SR04 frontSensor, backSensor;
Car car;

const int TRIGGER1_PIN = 6; //D6
const int ECHO1_PIN = 5; //D5
const int TRIGGER_PIN = 4;
const int ECHO_PIN = 7;
const int encoderLeftPin = 2;
const int encoderRightPin = 3;


void setup() {
  Serial3.begin(9600);
  Serial.begin(19200);
  encoderLeft.attach(encoderLeftPin);
  encoderRight.attach(encoderRightPin);
  frontSensor.attach(TRIGGER_PIN, ECHO_PIN);
  backSensor.attach(TRIGGER1_PIN, ECHO1_PIN);
  encoderLeft.begin();
  encoderRight.begin();
  car.begin(encoderLeft, encoderRight); //initialize the car using the encoders
}


void loop() {  
   if (frontSensor.getMedianDistance() != 0 && frontSensor.getMedianDistance() < 15) {
    car.setMotorSpeed(0,0);
  } 

   if(backSensor.getMedianDistance() !=0 && backSensor.getMedianDistance() <15) {
    car.setMotorSpeed(0,0);
   }
   
  if(Serial3.available()){
    char input = Serial3.read(); //read everything that has been received so far and log down the last entry
       Serial.println(input);
      if(input == 'f'){
        car.setMotorSpeed(50,50);
      } 
      if(input == 'b'){
        car.setMotorSpeed(-50,-50);
      }
      if(input == 's'){
        car.setMotorSpeed(0,0);
    }
      if(input == 'l'){
        car.setMotorSpeed(0,50);
      }
      if(input == 'r'){
        car.setMotorSpeed(50,0);
      } 
    }
    }


