void setup(){
  size(320,240);
}

void draw(){
  background(0);
  stroke(127);
  strokeWeight(5);
  line(
    0,height/2,
    width,height/2
  );
  quad(
    mouseX-20,5+(height/2)-20,
    mouseX+20,5+(height/2)-20,
    mouseX+15,5+(height/2)+20,
    mouseX-15,5+(height/2)+20
  );
}
