@author Simon Löfving

Setup
  Serialserver
    To be able to send commands via serial the P5 Serialserver needs to be running.
    In order to to that go to folder where the code is located and type 'node startserver.js' in the command line.

  HTTP-Server
  To be able to run the script in a browser it needs to be on a local server.
  In order to to that go to folder where the code is located and type 'http-server' in the command line.

  Run the Script
    To run the script: Type 'localhost:8080' or '127.0.0.1:8080' in the webbrowser.

  To run the servers from python:
    import subprocess
    subprocess.Popen(['http-server' ], cwd='Path to the code's folder')
    subprocess.Popen(['node', 'startserver.js' ], cwd='Path to the code's folder')

  And the browser:
    import webbrowser
    webbrowser.open('http://127.0.0.1:8080')

  Control the Script
    The script has a default song and to play that hit 'p'.
    Choose a song with 'a' and 'b'
    Stop the song with 's'.
