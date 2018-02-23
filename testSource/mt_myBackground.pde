

void setup(){
  size(800,300);
  
  
}

void draw(){
  background(0);
  fnDrawGrid(0x55);
  
  rectMode(CENTER);
  stroke(0xFF);
  fill(0x77);
  rect(width/2,height/2,240,240);
}


void keyPressed(){
  switch(key){
    case 'f':saveFrame("myBackground-######.png");break;
    case 'q':exit();break;
    default:break;
  }
}
