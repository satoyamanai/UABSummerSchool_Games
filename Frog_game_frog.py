from tkinter import *
import time
import keyboard
from Car import *  #Imports everything from Car.py
from Frog import *


#Point p=new Point()    #create an object in Java
#p=Point()              #create an object in Python
tk=Tk() #Creates an object of Tk class
w=Canvas(tk,width=800,height=600)   #Creates a window (Canvas object)
w.pack()    #Displays the window
#w.mainloop()    #Maintains the window on the screen

class Lane:
    def  __init__(self,x,y,w,h,num_of_cars,color_of_cars,speed_of_cars,car_width=50,car_height=30):
        self.x=x
        self.y=y
        self.w=w
        self.h=h
        self.cars=[]
        for i in range(num_of_cars):
            if speed_of_cars>0:
                startx=0-car_width*(i+1)*2
            else:
                startx=800+car_width*(i+1)*2
            c=Car(startx,
                  self.y+self.h/2-car_height/2,
                  car_width,car_height,speed_of_cars,color_of_cars)
            self.cars.append(c)
    def moveCars(self):
        for c in self.cars:
            c.move()
    def paint(self,w):
        w.create_rectangle(self.x,self.y,
                           self.x+self.w,
                           self.y+self.h,fill="grey")
        for c in self.cars:
            c.paint(w)



#y=50
x=50
#Now, using create_rectangle(x1,y1,x2,y2) draw a rectangle
#that moves to the right
frog=Frog(400,500)
lane=Lane(0,50,800,100,3,"yellow",5)
lane2=Lane(0,150,800,100,3,"blue",-10)
lanes=[lane,lane2]
attempts=1
t0=time.time()
while True:
    #MOVE THE ELEMENTS OF THE GAME
    for lane in lanes:
        lane.moveCars()
    
    if keyboard.is_pressed("up arrow"):
        frog.y=frog.y-frog.speed
    if keyboard.is_pressed("left arrow"):
        frog.x=frog.x-frog.speed
    if keyboard.is_pressed("right arrow"):
        frog.x=frog.x+frog.speed
    
    #DETECT CRASHES
    for lane in lanes:
        for car in lane.cars:
            if frog.crashed(car):
                print("CRASHED!!!!")
                attempts+=1 #equivalent to attempts=attempts+1
                frog.setAtStartingPosition()
    t=time.time()-t0
    
    #PAINT THE ELEMENTS OF THE GAME
    w.delete("all")
    for lane in lanes:
        lane.paint(w)
    frog.paint(w)
    if frog.y<30:
        print("YOU WON!!!")
        w.create_text(300,300,text=f"YOU WON!!! You needed {attempts} attemps.",font="family='Helvetica', 36",fill="RED")
    w.create_text(700,30,text=str(round(t,1)))
    w.update()  #repaints the window
    time.sleep(0.05)  #50/1000 = 50ms = 0.05sc

