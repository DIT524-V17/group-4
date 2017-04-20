/*
"Music: www.bensound.com"
*/
import ddf.minim.*;

Minim minim;
AudioPlayer song;
AudioMetaData meta;

void setup()
{
  size(256, 200);
  //Setting up the minim, song and meta. 
  minim = new Minim(this);
  song = minim.loadFile("bensound-jazzyfrenchy.mp3", 1024);
  meta = song.getMetaData();
  song.play();
  textFont(createFont("Serif", 20));
}

int ys = 25;
int yi = 20;

void draw()
{
  //Convert milliseconds to minutes and seconds. 
  int minutes = meta.length()/60000;
  int seconds = (meta.length()/1000) % 60;
  String duration = minutes + "," + seconds;
  background(0);
  int y = ys;
  //Display the meta data
  text("Title: " + meta.title(), 5, y);
  text("Author: " + meta.author(), 5, y+=yi); 
  text("Album: " + meta.album(), 5, y+=yi);
  text("Date: " + meta.date(), 5, y+=yi);
  text("Genre: " + meta.genre(), 5, y+=yi);
  text("Length : " + duration, 5, y+=yi);
  text("Lyrics: " + meta.lyrics(), 5, y+=yi ); 

}