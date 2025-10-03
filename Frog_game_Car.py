class Car:
    def __init__(self,x,y,w,h,speed,color):
        self.x=x
        self.y=y
        self.w=w
        self.h=h
        self.speed=speed
        self.color=color
    def move(self):
        self.x=self.x+self.speed
        if self.speed>0:
            if self.x>800: self.x=0-self.w
        else:
            if self.x<0-self.w: self.x=800
    def paint(self,w):
        w.create_rectangle(self.x,self.y,
                            self.x+self.w,self.y+self.h,fill=self.color)
        x_vert=self.x+self.w*0.75 if self.speed>0 else self.x+self.w*0.25
        w.create_line(x_vert,self.y,
                      x_vert,self.y+self.h)