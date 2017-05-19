Author Simon Löfving

Setup
  Serialserver
    To be able to send commands via serial the P5 Serialserver needs to be running.
    In order to to that type 'node startserver.js' in the command line.

  HTTP-Server
  To be able to run the script in a browser it needs to be on a local server.
  To that type 'http-server' in the command line.

  Run the Script
    To run the script: Type 'localhost:8080' or '127.0.0.1:8080' in the webbrowser.

  To run the servers from python:
    import subprocess
    subprocess.Popen(['http-server' ], cwd='/Users/simon/Desktop/test')
    subprocess.Popen(['node', 'startserver.js' ], cwd='/Users/simon/Desktop/test')

  And the browser:
    import webbrowser
    webbrowser.open('http://127.0.0.1:8080')

  Control the Script
    The script has a default song and to run that hit the UP arrow key.
    Choose a song with the Left or Right arrow key.
    Stop the song with the Down arrow key.
    Pause the song with the ALT key.
