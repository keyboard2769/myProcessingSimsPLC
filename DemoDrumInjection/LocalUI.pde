void ccSetLocation(Rectangle pxTarget, int pxX, int pxY){
  pxTarget.x=pxX;
  pxTarget.y=pxY;
}//+++

void ccSetSize(Rectangle pxTarget, int pxW, int pxH){
  pxTarget.width=pxW;
  pxTarget.height=pxH;
}//+++

void ccSetBound(Rectangle pxTarget, int pxX, int pxY, int pxW, int pxH){
  ccSetLocation(pxTarget, pxX, pxY);
  ccSetSize(pxTarget, pxW, pxH);
}//+++

int ccGetCenterX(Rectangle pxTarget){
  return pxTarget.x+pxTarget.width/2;
}//+++

int ccGetCenterY(Rectangle pxTarget){
  return pxTarget.y+pxTarget.height/2;
}//+++

int ccGetEndX(Rectangle pxTarget){
  return pxTarget.x + pxTarget.width;
}//+++

int ccGetEndY(Rectangle pxTarget){
  return pxTarget.y + pxTarget.height;
}//+++

void ccFollowEast(Rectangle pxSelf, Rectangle pxTarget, int pxOffset){
  pxSelf.x=ccGetEndX(pxTarget)+pxOffset;
  pxSelf.y=pxTarget.y;
}//+++

void ccFollowSouth(Rectangle pxSelf, Rectangle pxTarget, int pxOffset){
  pxSelf.x=pxTarget.x;
  pxSelf.y=ccGetEndY(pxTarget)+pxOffset;
}//+++

void ccDrawLineH(int pxY){
  line(0,pxY,width,pxY);
}//+++

void ccDrawLineV(int pxX){
  line(pxX,0,pxX,height);
}//+++

void ccDrawBitAsRedlit(boolean pxAct, int pxX, int pxY){
  pushStyle();
  {
    noStroke();
    fill(pxAct?0xFF333333:0xFFCC6666);
    rect(pxX, pxY, 4, 4);
  }
  popStyle();
}//+++

void ccDrawIntegerAsBox(int pxVal, String pxUnit, int pxX, int pxY){
  pushStyle();
  {
    strokeWeight(1);
    textAlign(RIGHT, CENTER);
    stroke(0xFF33CC33);
    fill(0xFF333333);
    rect(pxX, pxY, C_PIX_BOX_W, C_PIX_BOX_H);
    fill(0xFF33CC33);
    text(
      String.format("%d %s", pxVal, pxUnit),
      pxX+C_PIX_BOX_W-2,
      pxY+C_PIX_BOX_H/2
    );
  }
  popStyle();
}//+++

void ccDrawFloatAsBox(float pxVal, String pxUnit, int pxX, int pxY){
  pushStyle();
  {
    strokeWeight(1);
    textAlign(RIGHT, CENTER);
    stroke(0xFF33CC33);
    fill(0xFF333333);
    rect(pxX, pxY, C_PIX_BOX_W, C_PIX_BOX_H);
    fill(0xFF33CC33);
    text(
      String.format("%.2f %s", pxVal, pxUnit),
      pxX+C_PIX_BOX_W-2,
      pxY+C_PIX_BOX_H/2
    );
  }
  popStyle();
}//+++

void ssDrawDrum(Rectangle pxBound){
  final int lpSupporterWidthPIX = pxBound.width /16;
  final int lpSupporterPartPIX  = pxBound.width / 4;
  final int lpDrumCutPIX        = pxBound.height/ 8;
  pushStyle();
  {
    fill(C_COLOR_METAL);
    rect(
      pxBound.x, pxBound.y+(lpDrumCutPIX),
      pxBound.width, pxBound.height-(lpDrumCutPIX*2)
    );
    rect(
      pxBound.x+lpSupporterPartPIX,pxBound.y,
      lpSupporterWidthPIX,pxBound.height
    );
    rect(
      pxBound.x+lpSupporterPartPIX*3,pxBound.y,
      lpSupporterWidthPIX,pxBound.height
    );
  }
  popStyle();
}//+++

void ssDrawHopper(Rectangle pxBound){
  final int lpHopperCutPIX  = pxBound.width/4;
  pushStyle();
  {
    fill(C_COLOR_METAL);
    quad(
      pxBound.x, pxBound.y,
      ccGetEndX(pxBound), pxBound.y,
      ccGetEndX(pxBound)-lpHopperCutPIX, ccGetEndY(pxBound),
      pxBound.x+lpHopperCutPIX, ccGetEndY(pxBound)
    );
  }
  popStyle();
}//+++

void ssDrawTank(Rectangle pxBound){
  pushStyle();
  {
    fill(C_COLOR_METAL);
    rect(pxBound.x, pxBound.y, pxBound.width, pxBound.height);
  }
  popStyle();
}//+++

void ssDrawPipe(Rectangle pxBound){
  final int lpPipeWidthPIX  = 3;
  pushStyle();
  {
    fill(C_COLOR_DUCT);
    rect(
      pxBound.x, pxBound.y+pxBound.height-lpPipeWidthPIX,
      pxBound.width, lpPipeWidthPIX
    );
    rect(
      ccGetEndX(pxBound)-lpPipeWidthPIX,pxBound.y,
      lpPipeWidthPIX,pxBound.height
    );
  }
  popStyle();

}//+++

void ssDrawBelcon(Rectangle pxBound, boolean pxActivated){
  pushStyle();
  {
    fill(pxActivated?0xFFCCCC33:C_COLOR_DUCT);
    rect(
      pxBound.x+pxBound.height/2, pxBound.y,
      pxBound.width-pxBound.height, pxBound.height
    );
    ellipse(
      pxBound.x+pxBound.height/2, ccGetCenterY(pxBound),
      pxBound.height, pxBound.height
    );
    ellipse(
      ccGetEndX(pxBound)-pxBound.height/2, ccGetCenterY(pxBound),
      pxBound.height, pxBound.height
    );
  }
  popStyle();
}//+++
  
//***eof