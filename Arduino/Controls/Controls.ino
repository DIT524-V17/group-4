#include <Smartcar.h>
#include <Wire.h>


Odometer encoderLeft, encoderRight;
SR04 frontSensor;
SR04 backSensor;
Gyroscope gyro;
Car car;


const int TRIGGER1_PIN = 6; //D6
const int ECHO1_PIN = 5; //D5
const int TRIGGER_PIN = 4;
const int ECHO_PIN = 7;
const int encoderLeftPin = 2;
const int encoderRightPin = 3;

const int fwSpeed =  70; //set forward speed
const int bwSpeed = -70; //set backward speed
const int rDegree =  75; //set degrees to turn right
const int lDegree = -75; //set degrees to turn left
char Direction = 'n';

int frDistance;
int baDistance;

void setup() {
  Serial3.begin(9600);
  Serial.begin(9600);

  gyro.attach();
  encoderLeft.attach(encoderLeftPin);
  encoderRight.attach(encoderRightPin);
  frontSensor.attach(TRIGGER_PIN, ECHO_PIN);
  backSensor.attach(TRIGGER1_PIN, ECHO1_PIN);

  // start components
  gyro.begin();
  encoderLeft.begin();
  encoderRight.begin();
  car.begin(encoderLeft, encoderRight, gyro); //initialize the car using the encoders

  //ini Ultrasonic distance meansurement as 0
  frDistance = 0;
  baDistance = 0;

}

void loop(){

  frDistance = frontSensor.getDistance();
  baDistance = backSensor.getDistance();
  
  if (NoObstacle(baDistance) == false && NoObstacle(frDistance) == false) {
    car.stop();
  }
  else if(NoObstacle(baDistance) == false){
    if(NoObstacle(frDistance) == true){
      handleInput();
    }
    else{
      car.stop();
    }
  }
  
  if(NoObstacle(frDistance) == false){
    if(NoObstacle(baDistance) == true){
      handleInput();
    }
    else{
      car.stop();
    }
  }
  else{
    dancing();
    handleInput();
  }
   
}

  void dancing(){
   if (Serial.available()) {
    char input;
    frDistance = frontSensor.getDistance();
    baDistance = backSensor.getDistance();
    
    while (Serial.available()) input = Serial.read(); //read everything that has been received so far and log down the last entry
    switch (input) {
      case 'k': //forward
        Direction = 'k';
//        car.setMotorSpeed(10,10);
          car.go(10);
          car.rotate(-180);
          car.go(-15);
          car.rotate(80);
        break;
      case 's': //backward
        Direction = 's';
//        car.setMotorSpeed(20,20);
        car.go(-10);
        car.rotate(60);
        car.go(20);
        car.rotate(-60);
        break;
      case 'h': //turn left
        Direction = 'h';
//        car.setMotorSpeed(0,70);
        car.go(20);
       car.rotate(90);
       car.go(5);
       car.rotate(-90);
        break;
      default: //if there isn't any command
        car.setSpeed(0);
        car.setAngle(0);
    }
  }
  }


void handleInput() {
  if (Serial3.available()) {
    char input;
     frDistance = frontSensor.getDistance();
     baDistance = backSensor.getDistance();
     
    while (Serial3.available()) input = Serial3.read(); //read everything that has been received so far and log down the last entry
    switch (input) {
      case 'f': //forward
       // Direction = 'f';
        if(NoObstacle(frDistance) == false) {
          car.setSpeed(0);
        }
        else{
          car.setSpeed(fwSpeed);
          car.setAngle(0);
        }
        break;
      case 'b': //backward
       // Direction = 'b';
        if(NoObstacle(baDistance) == false) {
          car.setSpeed(0);
        }
        else{
          car.setSpeed(bwSpeed);
          car.setAngle(0);
        }
        break;
      case 'l': //turn left
       // Direction = 'l';
        if(NoObstacle(frDistance) == false) {
          car.setSpeed(0);
        }
        else{
          car.setSpeed(fwSpeed);
          car.setAngle(lDegree);
        }
        break;
      case 'r': //turn right
       // Direction = 'r';
        if(NoObstacle(frDistance) == false) {
          car.setSpeed(0);
        }
        else{
          car.setSpeed(fwSpeed);
          car.setAngle(rDegree);
        }
        break;
      case 'q': //turn left front
       // Direction = 'q';
       if(NoObstacle(frDistance) == false) {
          car.setSpeed(0);
        }
       else{
        car.setMotorSpeed(35,fwSpeed);
       }
        break;
      case 'e': //turn right front
       // Direction = 'e';
       if(NoObstacle(frDistance) == false) {
          car.setSpeed(0);
        }
       else{
        car.setMotorSpeed(fwSpeed, 35);
       }
        break;
      case 'z': //turn left bottom
       // Direction = 'z';
        if(NoObstacle(frDistance) == false) {
          car.setSpeed(0);
        }
       else{
          car.setMotorSpeed(-35, bwSpeed);
       }
        break;
      case 'c': //turn right bottom
       // Direction = 'c';
        if(NoObstacle(frDistance) == false) {
          car.setSpeed(0);
        }
       else{
          car.setMotorSpeed(bwSpeed, -35);
       }
        break;
      case 's': //stop car
        car.setSpeed(0);
        car.setAngle(0);
        break;
      default: //if there isn't any command
        car.setSpeed(0);
        car.setAngle(0);
    }
  }
}


boolean NoObstacle(int distance) {
  if (distance > 20) {
    return true;
  }
  if (distance == 0) {
    return true;
  }
  else {
    return false;
  }
}

