#include <Arduino.h>
#include <Smartcar.h>
#include <Wire.h>
#include <Servo.h>

Odometer encoderLeft, encoderRight;
SR04 frontSensor, backSensor;
Car car;
Servo carServo;

const int TRIGGER1_PIN = 6; //D6
const int ECHO1_PIN = 5; //D5
const int TRIGGER_PIN = 4;
const int ECHO_PIN = 7;
const int encoderLeftPin = 2;
const int encoderRightPin = 3;

const int fwSpeed =  50; //set forward speed
const int bwSpeed = -50; //set backward speed
const int rDegree =  50; //set degrees to turn right
const int lDegree = -50; //set degrees to turn left

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
   int frDistance = frontSensor.getDistance();
   int baDistance = backSensor.getDistance();
   
   car.updateMotors();
   handleInput(); 
    
   if (frDistance != 0 && frDistance < 20) {
    car.stop();
  } 
   if(baDistance !=0 && baDistance <20) {
    car.stop();
   }
   ultsensorConflict();
}

// handle serial input    
void handleInput(){
    if(Serial3.available()){
    char input = Serial3.read(); //read everything that has been received so far and log down the last entry
       Serial.println(input);
      if(input == 'f'){
        car.setSpeed(fwSpeed);
      } 
      if(input == 'b'){
        car.setSpeed(bwSpeed);
      }
      if(input == 's'){
        car.setSpeed(0);
    }
      if(input == 'l'){
        car.setSpeed(fwSpeed);
        car.setAngle(lDegrees);
      }
      if(input == 'r'){
        car.setSpeed(fwSpeed);
        car.setAngle(rDegrees);
      } 
    }	
}
// incase both front ultrasonicSensor and back ultrasonicSensor are values at same time.	
void ultsensorConflict(){
 if((frontSensor.getDistance()&&backSensor.getDistance())<15){
   car.stop();
   Serial.write("1");
   delay(100);
   Serial.flush();
 }else if ((frontSensor.getDistance()&&backSensor.getDistance())==0){
  Serial.write("0");
   car.setSpeed(50);
 }
}

