import ddf.minim.*;
import ddf.minim.analysis.*;
import processing.serial.*;

Minim minim;
AudioPlayer song;
BeatDetect beat;
BeatListener bl;
Serial myPort;
String input;
int count  = 0;
float BPM;
float counter;
//Values to be send to the arduino stored in array
byte [] command1 = {"d"};
byte [] command2 = {"q"};
byte [] command3 = {"f"};
byte [][] ca = {command1 , command2 , command3};
String [][] commands = {command1 , command2 , command3};

class BeatListener implements AudioListener
{
  private BeatDetect beat;
  private AudioPlayer source;
  
  BeatListener(BeatDetect beat, AudioPlayer source)
  {
    this.source = source;
    this.source.addListener(this);
    this.beat = beat;
  }
  
  void samples(float[] samps)
  {
    beat.detect(source.mix);
  }
  
  void samples(float[] sampsL, float[] sampsR)
  {
    beat.detect(source.mix);
  }
}

void setup()
{
  size(512, 200);
  
  String portName = Serial.list()[0];
  myPort = new Serial(this, portName , 9600);
  myPort.bufferUntil('\n');
  
  minim = new Minim(this);
  song = minim.loadFile("safeslow.mp3", 1024);
  beat = new BeatDetect(song.bufferSize(), song.sampleRate());
  //beat.setSensitivity(500);  
  bl = new BeatListener(beat, song); 
  song.play();
}

void serialEvent(Serial myPort){
  input = myPort.readStringUntil('\n');
}

void draw()
{
 if(song.isPlaying()){
 sendCommands();
 }
}

void sendCommands(){
  int duration = millis();
  //Get a random value from the array of commands to be used.
  int command = (int)(Math.random()*(commands.length));
  println(commands[2][0]);
  if ( beat.isKick()){
//   if(count%2 == 0) 
   myPort.write(ca[2][0]);
   count++;
   delay(500); 
}
//if(duration>15000){
//song.pause();
//}
/*
  if ( beat.isSnare() ){
  snareSize = 32;
  myPort.write(commands[command]);
  println("hej");
}*/
}