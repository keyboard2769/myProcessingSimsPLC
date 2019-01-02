
interface EiUpdatable {
  void ccUpdate();
}//***

abstract class EcRect {

  float 
    cmX=8f, 
  cmY=8f, 
  cmW=8f, 
  cmH=8f
    ;//...

  //===

  final void ccSetLocation(float pxX, float pxY) {
    cmX=pxX;
    cmY=pxY;
  }//+++

  final void ccSetLocation(EcRect pxTarget, float pxOffsetX, float pxOffsetY) {
    cmX=pxOffsetX;
    cmY=pxOffsetY;
    if (pxTarget!=null) {
      cmX+=pxTarget.cmX+(pxOffsetY==0?pxTarget.cmW:0);
      cmY+=pxTarget.cmY+(pxOffsetX==0?pxTarget.cmH:0);
    }
  }//+++

  final void ccSetSize(float pxW, float pxH) {
    cmW=pxW;
    cmH=pxH;
  }//+++

  final void ccSetSize(EcRect pxTarget, boolean pxInW, boolean pxInH) {
    if (pxInW) {
      cmW=pxTarget.cmW;
    }
    if (pxInH) {
      cmH=pxTarget.cmH;
    }
  }//+++

  //===

  final float ccCenterX() {
    return cmX+cmW/2;
  }//+++
  final float ccCenterY() {
    return cmY+cmH/2;
  }//+++
  final float ccEndX() {
    return cmX+cmW;
  }//+++
  final float ccEndY() {
    return cmY+cmH;
  }//+++
  final boolean ccContains(float pxX, float pxY) {
    return (pxX>cmX)&&(pxX<(cmX+cmW))
      &&(pxY>cmY)&&(pxY<(cmY+cmH));
  }//+++
}//***

abstract class TcWorldObject extends EcRect implements EiUpdatable {
  Body cmBody;
}//***

