#include <Arduino.h>
#include <Smartcar.h>
#include <Servo.h>
#include <NewPing.h>

#define PINSERVO A7 //Pin attached to the servo
#define PINTRIGGER 6 //Ultrasoic trigger pin
#define PINECO A5 //Ultrasoic eco/output pin
#define PINTRIGGER2 8 //Ultrasoic trigger pin

Servo servo; // car servo

frontSensor.attach(PINTRIGGER, PINECO);
backSensor.attach(PINTRIGGER2, PINECO2);
//setting front and back ultrasonic sensors
int angle = 0; // the angle of servo
int distance = 0;  // initial distance read by the front sensor
Odometer encoderLeft, encoderRight;
Car car;

boolean obstruction = false;
long time; //counting how long time the car has drived, not sure if it is necessary;


void setup()
{
	encoderLeft.attach(2);
	encoderRight.attach(3);
	encoderLeft.begin();
	encoderRight.begin();
	servo.attach(PINSERVO);
	Serial.begin(9600);
	Serial3.begin(9600);
	Serial3.setTimeout(10);
	Serial.setTimeout(10);
	car.begin();
	car.enableCruiseControl();

}

void loop() {
	car.updateMotors();
	if(Serial3.available(){
	String serialString = Serial3.readString();
	switch(serialString.charAt(0){
		case 'c': // for manual drive mode
			control(serialString);
			break;
		case 's': // stop the car when obstructer detected
			if obstruction =!obstruction;
			break;
		case 'd':
			// this is for the dancing mode, and basically, dancing mode is a sort of self-driving; 
			break;
		default:
			car.setSpeed(0);
	}
   }
	if(obstruction)	detectObstacle();
		
}

/*##################################
Manual Drive Control
###################################*/
void control(String input){
  int carSpeed;
  int carAngle;
  parseSpeed(input, &carSpeed, &carAngle);//set the speed and angle of car 
  if(carSpeed==0){//stop the car if the input format incorrect or speed = 0 
    car.stop();
  }
  int tmp=0; 
 
  carSpeed=(tmp=0.35*carSpeed)<50?carSpeed!=0?50:0:tmp;
  Serial.println("the speed is "+String(carSpeed));
  carSpeed=carAngle>90||-90<carAngle?carSpeed>75?75:carSpeed:carSpeed;
  car.setSpeed(carSpeed);//set speed
  
  car.setAngle(carAngle);//set angle

}

void parseSpeed(String setSpeed, int * newSpeed, int * angle){
  String string ="";
    for(int i = 1; i<setSpeed.length(); i++){
      if(setSpeed.charAt(i)=='|'){//if character is =| push the parse value the newSpeed than reset the temporary string and skip to the next character
        *newSpeed = string.toInt();
        string = "";
      }else{
        string += setSpeed.charAt(i);//push characters the the temporary string
      }
    }
    *angle = string.toInt();//when the entire string has been read push the value to angle
    string = "";//reset the string
}

void detectObstacle(){
  servo.write(90);//set servo to make sure the car is straight(for dancing function)
  if(frontSensor.getDistance()<=20&&10<=frontSensor.getDistance()){//stop the car if the distance is within a certain range
      car.stop();  
  }
if(backSensor.getDistance() <= 10 && backsensor.getDistance() > 0){
Serial.println(String(backSensor.getDistance()+ "Warning! collision risk checked!"));
}




