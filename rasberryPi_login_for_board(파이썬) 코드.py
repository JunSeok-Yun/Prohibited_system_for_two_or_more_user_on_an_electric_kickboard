import time

import sys

import RPi.GPIO as GPIO

import I2C_LCD_driver

import drivers

from time import sleep

import socket

 

EMULATE_HX711=False

 

referenceUnit = 1

display = drivers.Lcd()

GPIO.setwarnings(False)

GPIO.setmode(GPIO.BCM)

 

 

buzzer = 23

scale1 = [130, 130, 130]

scale2 = [261, 329, 391]

scale3 = [220, 220, 220]

 

 

GPIO.setup(buzzer,GPIO.OUT)

p = GPIO.PWM(buzzer,600)

 

if not EMULATE_HX711:

    import RPi.GPIO as GPIO

    from hx711 import HX711

else:

    from emulated_hx711 import HX711

 

def cleanAndExit():

    print("Cleaning...")

 

    if not EMULATE_HX711:

        GPIO.cleanup()

 

    print("Bye!")

    sys.exit()

 

 

def compare(weight): # send fun

    global flag

    if val < weight-300:

        display.lcd_display_string("Under Weight!!", 1)  

        display.lcd_display_string("You can't use it!", 2)

        msg = "Under Weight!! You can't use it!"

        client_sock.sendall(msg.encode())

        

        for i in range(3):

            p.start(50)

            p.ChangeFrequency(scale3[i])

            time.sleep(0.2)

            p.stop()

        exit()

        display.lcd_clear()

        

        return 1

    

    elif val > weight+300:

        display.lcd_display_string("Over Weight!!", 1)  

        display.lcd_display_string("You can't use it!", 2)

        msg = "Over Weight!! You can't use it!"

        client_sock.sendall(msg.encode())

        for i in range(3):

            p.start(50)

            p.ChangeFrequency(scale1[i])

            time.sleep(0.2)

            p.stop()

        exit()

        display.lcd_clear() 

        return 2

    

    else:

        display.lcd_display_string("Average Weight!!", 1)

        display.lcd_display_string("You can use it!!", 2)

        msg = "Average Weight!! You can use it!"

        client_sock.sendall(msg.encode())

        if flag:

            for i in range(3):

                p.start(50)

                p.ChangeFrequency(scale2[i])

                time.sleep(0.5)

                p.stop()

            display.lcd_clear()

            flag = False

            

                

        return 3

 

            

        

 

def compare2(weight):

    

    if val < weight-300:

        return 1

    

    elif val > weight+300:

        return 2

    

    else:

        return 3

 

hx = HX711(20, 16)

hx.set_reading_format("MSB", "MSB")

hx.set_reference_unit(448)

hx.reset()

hx.tare()

print("Tare done! Add weight now...")

 

Host = "192.168.137.202" # ip address

port = 50000# port number

flag = True

server_sock = socket.socket(socket.AF_INET,socket.SOCK_STREAM)

server_sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)

print('Socekt created')

server_sock.bind((Host, port))

print('Socekt bind complete')

server_sock.listen()

print("\n wait...")

client_sock, addr = server_sock.accept()

print('Connected by?!',addr)

running=False

while True:

    #client_sock, addr = server_sock.accept()

    #print('Connected by?!',addr)

    data = client_sock.recv(1024) # recv func => thread

    data = data.decode("utf8").strip()

    

    if not data:break

    print("Received: " +data)

    

    weight = int(data)

    weight = weight * 10

    while True:

        try:

            val = hx.get_weight(5)

            print(val)

            hx.power_down()

            hx.power_up()

            time.sleep(0.1)

            if val > 50:

                compare(weight)

                time.sleep(0.1)

                

                   

        except (KeyboardInterrupt, SystemExit):    

            cleanAndExit()

            p.stop()

            display.lcd_clear()

            print("sys down")

            GPIO.cleanup()

        

    client_sock.close()

    server_sock.close()