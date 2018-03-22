boolean fnIsKeyPressed(char pxKey){
  return keyPressed && (pxKey==key);
}

class EcTrolley{
  int cmPostion,
    cmMax, cmMin;
  EcTrolley(){
    cmPostion=99;
    cmMax=height;
    cmMin=0;
  }
  void ccUpdate(){
    quad(
      cmPostion-20,5+(height/2)-20,
      cmPostion+20,5+(height/2)-20,
      cmPostion+15,5+(height/2)+20,
      cmPostion-15,5+(height/2)+20
    );
  }
  void ccGoRight(){
    cmPostion++;
    cmPostion=constrain(cmPostion,cmMin,cmMax);
  }
  void ccGoLeft(){
    cmPostion--;
    cmPostion=constrain(cmPostion,cmMin,cmMax);
  }
}
