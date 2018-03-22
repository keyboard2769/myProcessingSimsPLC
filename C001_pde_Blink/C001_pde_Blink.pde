int pbRoller=0;

void setup(){
  size(100,100);
  frameRate(16);
}

void draw(){
  pbRoller++;pbRoller&=0x0F;
  background(pbRoller<7?127:255);
}
