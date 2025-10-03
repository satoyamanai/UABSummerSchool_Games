class Frog:
    def __init__(self,x,y,speed=5):
        self.x=x
        self.y=y
        self.w=30
        self.h=30
        self.speed=speed
        self.startx=x
        self.starty=y
    def setAtStartingPosition(self):
         self.x=self.startx
         self.y=self.starty
    def paint(self,w):
        w.create_rectangle(self.x,self.y,
                            self.x+self.w,self.y+self.h,fill="green")
    def pointInsideCar(self,x,y,car):
        if car.x <= x <= car.x+car.w and \
           car.y <= y <= car.y+car.h:
                return True
        else: return False
    def crashed(self,car):
        if self.pointInsideCar(self.x,self.y,car): return True
        if self.pointInsideCar(self.x+self.w,self.y,car): return True
        if self.pointInsideCar(self.x,self.y+self.h,car): return True
        if self.pointInsideCar(self.x+self.w,self.y+self.h,car): return True
        return False