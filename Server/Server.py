#! /usr/bin/env python
#@ Author: Filip Walldén, Simon Löfving, Karanveer Singh & Anton Karlsson
import http.server
import socketserver
from socket import *
from _thread import *
import serial
import os
import subprocess
import webbrowser
from pykeyboard import PyKeyboard


ser= serial.Serial('/dev/ttyACM0', 9600)
byteArray = ()
BUFF = 1024
PORT = 9998
HOST = '192.168.42.1'
keyboard = PyKeyboard()
subprocess.Popen(['http-server'],cwd='/home/pi/Desktop/p5.serialport-master')
subprocess.Popen(['node', 'startserver.js'],cwd='/home/pi/Desktop/p5.serialport-master')
webbrowser.open_new('http://127.0.0.1:8080')


def response(key):
        return ('this_is_the_return_from_the_server')



def handler(clientsock,addr):
        while 1:
                        data = clientsock.recv(BUFF)
                        print ('data:' + repr(data))
                        b = data.decode('UTF-8')
                        print(b)
                        if b == 'dnc':
                                keyboard.tap_key('p')
                        if b == 'stp':
                                keyboard.tap_key('s')
                        if b == 'sn1':
                                keyboard.tap_key('a')
                        if b == 'sn2':
                                keyboard.tap_key('b')
                        ser.write(b.encode())
                        #serversocket.shutdown(1)
                        #serversocket.close()
                        break
if __name__=='__main__':
        Handler = http.server.SimpleHTTPRequestHandler
        serversocket = socket(AF_INET, SOCK_STREAM)
        serversocket.setsockopt(SOL_SOCKET, SO_REUSEADDR, 1)
        serversocket.bind((HOST, PORT))
        serversocket.listen(5)
        #serversock.serve_forever()

        while True:
                print('waiting for connection...')
                (clientsock, addr) = serversocket.accept()
                print ('... connected from:', addr)
                start_new_thread(handler, (clientsock, addr))




class MyServer(socketserver.socket):
        allow_reuse_address = True
