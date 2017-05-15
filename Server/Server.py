### Auther - Karan and Filip 
#! /usr/bin/env python
import http.server
import socketserver 
from socket import *
from _thread import *
import serial 



ser= serial.Serial('/dev/ttyACM0', 9600)
byteArray = ()
BUFF = 1024
HOST = '192.168.42.1' #must be input parameter @TODO
PORT = 9999 # must be input parameter @TODO
def response(key):
        return ('this_is_the_return_from_the_server')

def handler(clientsock,addr):
                while 1:
                        data = clientsock.recv(BUFF) 
                        print ('data:' + repr(data))
                        b = data.decode('UTF-8')
                        print(b)
                        ser.write(b.encode())        
						#serversocket.shutdown(1)
						#serversocket.close()
                        break
                # clientsock.close() # - reports [Errno 9] Bad file descriptor as it looks like that socket is trying to send data when it is already closed.

if __name__=='__main__':
		Handler = http.server.SimpleHTTPRequestHandler
        serversocket = socket(AF_INET, SOCK_STREAM)
        serversocket.setsockopt(SOL_SOCKET, SO_REUSEADDR, 1)
        serversocket.bind(ADDR)
        serversocket.listen(5)
        while True:
                print('waiting for connection...')
                (clientsock, addr) = serversock.accept()
                print ('... connected from:', addr)
                start_new_thread(handler, (clientsock, addr))

class MyServer(socketserver.socket):
					allow_reuse_address = True