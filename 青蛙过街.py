from tkinter import *
import time
#import keyboard


tk = Tk()   #creat an object of the class
w = Canvas(tk, width = 800, height = 600)   #creat a window (Canvas)
w.pack()   #analyze the window
   
y = 50
x = 50

class Car:
    def __init__(self,x,y,w,h,speed):
        self.x = x
        self.y = y
        self.w = w
        self.h = h
        self.speed = speed

# create a function inside Car named move() that implements this code (x = x + speed)
    def move(self):
        self.x = self.x + self.speed
        
    def paint(self,w):
        w.create_rectangle(self.x, self.y, self.x + self.w, self.y + self.h)
        x_vert = self.x + self.w * 0.75
        w.create_line(x_vert, self.y, x_vert, self.y + self.h)
        
class Frog:
    def __init__(self,x, y, speed = 5):
        self.x = x
        self.y = y
        self.w = 30
        self.h = 30     
    def paint(self,w):  
        w.create_rectangle(self.x, self.y, self.x + self.w, self.y + self.h)
        x_vert = self.x + self.w * 0.75
        w.create_line(x_vert, self.y, x_vert, self.y + self.h) 
        

c1 = Car(50, 50, 50, 30, 1)
c2 = Car(50, 150, 50, 30, 2)
c3 = Car(50, 250, 50, 30, 5)
cars = [c1, c2, c3]
frog = Frog(400, 500)
print(111)

while True:
    #move the car to the right
    for c in cars:
        c.move()
    '''if keyboard.is_pressed("up arrow"):
        frog.y = frog.y - frog.speed
    if keyboard.is_pressed("left arrow"):
        frog.x = frog.x - frog.speed
    if keyboard.is_pressed("right arrow"):
        frog.x = frog.x + frog.speed'''   
    #paint the elements of the game
    w.delete("all")
    for c in cars:
        c.paint(w)
    frog.paint(w)
    
    
    w.update() #repaints the windows
    #time.sleep(50/1000)   # 50/1000
    print(222)
update()
tk.mainloop() #Maintains the window on the screen
