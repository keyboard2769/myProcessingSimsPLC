EcTrolley pbTrolley;

void setup(){
  size(320,240);
  stroke(127);
  fill(255);
  strokeWeight(5);
  pbTrolley=new EcTrolley();
}

void draw(){
  background(0);
  line(
    0,height/2,
    width,height/2
  );
  if(fnIsKeyPressed('d')){
    pbTrolley.ccGoRight();
  }
  if(fnIsKeyPressed('a')){
    pbTrolley.ccGoLeft();
  }
  pbTrolley.ccUpdate();  
}
