void fnDrawGrid(color pxColor){
  stroke(pxColor);
  int i=0;
  while(i<=width){
    line(
      i,0,
      i,height
    );
    i+=10;
  }
  i=0;
  while(i<=width){
    line(
      0,i,
      width,i
    );
    i+=10;
  }
}

void fnDrawSize(){
  return;
}
