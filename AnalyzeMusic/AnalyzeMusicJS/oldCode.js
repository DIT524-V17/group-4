/**
*@author Simon Löfving
*/
var song;
var count;
var serial;
const portName = '/dev/cu.usbmodem1421';
var sendK = 'k';
var sendS = 's';
var sendH = 'h';


function preload() {
  song = loadSound('data/grey.mp3'); // Here goes the song
}

function setup() {
   serial = new p5.SerialPort(); // make a new instance of the serialport library
   serial.open(portName); // Open the port to the arduino
  count = 0;
  // Get the BPM of the song and then run the playSong function.
  song.getBPM(playSong,0.98,0.5);
}

function draw() {
  //As long as the song is playing the program will send commands.
  if(song.isPlaying()){
    //serial.write(outByte2);
    serial.write(sendS);
    console.log(sendS);
    // if(count == 0) serial.write(sendH);
    // if(count == 2) serial.write(sendS);
    // if(count == 4) serial.write(sendK);
      //console.log(count); //Send random commands
    count = (count+1)%6;
}
}
function playSong(bpm){
  //Get the BPM of the song from processPeaks and divide it by 60
  //in order to get it in beats per second which will set the loop speed
  //of the draw function.
  var fps = bpm.tempo/60;
  console.log(bpm.tempo);
  frameRate(fps);
  song.play();
}

/**
 * getBPM is a remake of the P5.Sound function processPeaks. It has been manipulated in
 * such a way that it returns the BPM of the chosen song.
 */

p5.SoundFile.prototype.getBPM = function (callback, _initThreshold, _minThreshold, _minPeaks) {
  var bufLen = this.buffer.length;
  var sampleRate = this.buffer.sampleRate;
  var buffer = this.buffer;
  var initialThreshold = _initThreshold || 0.9, threshold = initialThreshold, minThreshold = _minThreshold || 0.22, minPeaks = _minPeaks || 200;
  var offlineContext = new OfflineAudioContext(1, bufLen, sampleRate);
  var source = offlineContext.createBufferSource();
  source.buffer = buffer;
  var filter = offlineContext.createBiquadFilter();
  filter.type = 'lowpass';
  filter.frequency.value = 200;
  source.connect(filter);
  filter.connect(offlineContext.destination);
  source.start(0);
  offlineContext.startRendering();

  offlineContext.oncomplete = function (e) {
    var data = {};
    var filteredBuffer = e.renderedBuffer;
    var bufferData = filteredBuffer.getChannelData(0);

    do {
      allPeaks = getPeaksAtThreshold(bufferData, threshold);
      threshold -= 0.005;
    } while (Object.keys(allPeaks).length < minPeaks && threshold >= minThreshold);

    var intervalCounts = countIntervalsBetweenNearbyPeaks(allPeaks);

    var groups = groupNeighborsByTempo(intervalCounts, filteredBuffer.sampleRate);

    var topTempos = groups.sort(function (intA, intB) {
      return intB.count - intA.count;
    }).splice(0, 5);

    this.tempo = topTempos[0].tempo;
    callback(topTempos[0]);
  };
};

var Peak = function (amp, i) {
  this.sampleIndex = i;
  this.amplitude = amp;
  this.tempos = [];
  this.intervals = [];
};
var allPeaks = [];

function getPeaksAtThreshold(data, threshold) {
  var peaksObj = {};
  var length = data.length;
  for (var i = 0; i < length; i++) {
    if (data[i] > threshold) {
      var amp = data[i];
      var peak = new Peak(amp, i);
      peaksObj[i] = peak;
      i += 6000;
    }
    i++;
  }
  return peaksObj;
}
function countIntervalsBetweenNearbyPeaks(peaksObj) {
  var intervalCounts = [];
  var peaksArray = Object.keys(peaksObj).sort();
  for (var index = 0; index < peaksArray.length; index++) {
    for (var i = 0; i < 10; i++) {
      var startPeak = peaksObj[peaksArray[index]];
      var endPeak = peaksObj[peaksArray[index + i]];
      if (startPeak && endPeak) {
        var startPos = startPeak.sampleIndex;
        var endPos = endPeak.sampleIndex;
        var interval = endPos - startPos;
        if (interval > 0) {
          startPeak.intervals.push(interval);
        }
        var foundInterval = intervalCounts.some(function (intervalCount, p) {
          if (intervalCount.interval === interval) {
            intervalCount.count++;
            return intervalCount;
          }
        });
        if (!foundInterval) {
          intervalCounts.push({
            interval: interval,
            count: 1
          });
        }
      }
    }
  }
  return intervalCounts;
}
function groupNeighborsByTempo(intervalCounts, sampleRate) {
  var tempoCounts = [];
  intervalCounts.forEach(function (intervalCount, i) {
    try {
      var theoreticalTempo = Math.abs(60 / (intervalCount.interval / sampleRate));
      theoreticalTempo = mapTempo(theoreticalTempo);
      var foundTempo = tempoCounts.some(function (tempoCount) {
        if (tempoCount.tempo === theoreticalTempo)
          return tempoCount.count += intervalCount.count;
      });
      if (!foundTempo) {
        if (isNaN(theoreticalTempo)) {
          return;
        }
        tempoCounts.push({
          tempo: Math.round(theoreticalTempo),
          count: intervalCount.count
        });
      }
    } catch (e) {
      throw e;
    }
  });
  return tempoCounts;
}

function mapTempo(theoreticalTempo) {
  if (!isFinite(theoreticalTempo) || theoreticalTempo == 0) {
    return;
  }
  while (theoreticalTempo < 90)
    theoreticalTempo *= 2;
  while (theoreticalTempo > 180 && theoreticalTempo > 90)
    theoreticalTempo /= 2;
  return theoreticalTempo;
}


//
// if (key === 'a') {
//   if(!song.isPlaying()) song = loadSound('data/grey.mp3');
// }
// if (key === 'b') {
// if(!song.isPlaying()) song = loadSound('data/safe.mp3');
// }
// if (key === 'p') {
//   if(!song.isPlaying()){
//     getBPM(song, playSong); //Calculate the BPM of the song and play it.
//   }
// }
// if (key === 's'){
//   if(song.isPlaying()){
//     serial.write(stop);
//     console.log("Stop");
//     song.stop(); //Stop the song
//     count = 0;
//     noLoop();
//   }
// }
