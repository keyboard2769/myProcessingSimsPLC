void ccSurroundText(String pxText, int pxX, int pxY){
  if(pxText==null){return;}
  final int lpMargin = 2;
  int lpWidth = ceil(textWidth(pxText)) + lpMargin*2;
  int lpReturnCount = 1;
  for(char it : pxText.toCharArray()){
    if(it == '\r' || it == '\n'){lpReturnCount++;}
  }//..~
  int lpHeight = ceil(textAscent()+textDescent())*lpReturnCount + lpMargin*2;
  pushStyle();
  {
    fill(0x55555555);
    stroke(0xFFAAAAAA);
    rect(pxX, pxY, lpWidth, lpHeight);
    fill(0xFFEEEEEE);
    text(pxText, pxX+lpMargin, pxY+lpMargin);
  }
  popStyle();
}//+++

void ccDrawLineH(int pxY){
  line(0,pxY,width,pxY);
}//+++

void ccDrawController(ZcController pxTarget){
  
  //-- pushing
  final int lpConstG = 5;
  final int lpConstY = height-ceil(pxTarget.cmShiftedTarget);
  final int lpDeadH  = ceil(
    pxTarget.cmDeadPositive - pxTarget.cmDeadNegative
  );
  final int lpPropH  = ceil(
    pxTarget.cmProportionPositive - pxTarget.cmProportionNegative
  );
  
  //-- proportion
  stroke(0xAA339933);
  fill((32+pxTarget.cmAdjustIndication*4)&0xFF,0x66);
  rect(
    lpConstG *1, lpConstY - lpPropH/2,
    width - lpConstG*2, lpPropH
  );
  
  //-- dead
  stroke(0xAA228822);
  fill((32+pxTarget.cmSamplingIndication*4)&0xFF,0x66);
  rect(
    lpConstG *2, lpConstY - lpDeadH/2,
    width - lpConstG*4, lpDeadH
  );
  
  //-- center / target
  stroke(0xAA33EE33);
  ccDrawLineH(lpConstY);
  stroke(0xAAEEEE33);
  ccDrawLineH(height-ceil(pxTarget.cmProcessAverage));
  
  //-- popping
  noStroke();
  
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

class EcElement{
  Rectangle cmRegion=new Rectangle(8, 8, 8, 8);
  String cmText="<text>";
  boolean cmActivated=false;
  int cmOnColor    = 0xFFEEEE33;
  int cmOffColor   = 0xFF555555;
  int cmFrontColor = 0xFF333333;
  int cmBackColor  = 0xFF777777;
  //===
  public EcElement(){}//++!
  public EcElement(String pxText){
    this();
    cmText=pxText;
  }//++!
  public EcElement(String pxText, boolean pxAct){
    this(pxText);
    cmActivated=pxAct;
  }//++!
  //===
  void ccFollowH(EcElement pxTarget, int pxOffset){
    cmRegion.x=ccGetEndX(pxTarget.cmRegion)+pxOffset;
    cmRegion.y=pxTarget.cmRegion.y;
  }//+++
  void ccFollowV(EcElement pxTarget, int pxOffset){
    cmRegion.x=pxTarget.cmRegion.x;
    cmRegion.y=ccGetEndY(pxTarget.cmRegion)+pxOffset;
  }//+++
  void ccFollowE(EcElement pxTarget, int pxOffset){
    cmRegion.x=ccGetEndX(pxTarget.cmRegion)+pxOffset;
    cmRegion.y=ccGetCenterY(pxTarget.cmRegion) - cmRegion.height/2;
  }//+++
  void ccFollowS(EcElement pxTarget, int pxOffset){
    cmRegion.x=ccGetCenterX(pxTarget.cmRegion) - cmRegion.width/2;
    cmRegion.y=ccGetEndY(pxTarget.cmRegion)+pxOffset;
  }//+++
}//***

void ccDrawAsLabel(EcElement pxTarget){
  pushStyle();
  {
    fill(pxTarget.cmActivated?pxTarget.cmOnColor:pxTarget.cmOffColor);
    rect(
      pxTarget.cmRegion.x, pxTarget.cmRegion.y,
      pxTarget.cmRegion.width, pxTarget.cmRegion.height
    );
    textAlign(CENTER, CENTER);
    fill(pxTarget.cmFrontColor);
    text(pxTarget.cmText,
      ccGetCenterX(pxTarget.cmRegion),
      ccGetCenterY(pxTarget.cmRegion)
    );
  }
  popStyle();
}//+++

void ccDrawAsValueBox(EcElement pxTarget, int pxValue, String pxUnit){
  pxTarget.cmText=String.format("%04d %s", pxValue, pxUnit);
  ccDrawAsLabel(pxTarget);
}//+++

void ccDrawAsBlower(EcElement pxTarget){
  fill(pxTarget.cmBackColor);
  ellipse(
    pxTarget.cmRegion.x + pxTarget.cmRegion.height/2,
    pxTarget.cmRegion.y + pxTarget.cmRegion.height/2,
    pxTarget.cmRegion.height,
    pxTarget.cmRegion.height
  );
  rect(
    pxTarget.cmRegion.x + pxTarget.cmRegion.height/2,
    pxTarget.cmRegion.y,
    pxTarget.cmRegion.width - pxTarget.cmRegion.height/2,
    pxTarget.cmRegion.height/2
  );
}//+++

void ccDrawAsDryer(EcElement pxTarget){
  final int lpDevide      = 6;
  final int lpBarrelWestX = pxTarget.cmRegion.width/lpDevide;
  final int lpBarrelEastX = pxTarget.cmRegion.width*(lpDevide-2) / lpDevide;
  final int lpBarrelW     = pxTarget.cmRegion.width*(lpDevide-1) / lpDevide;
  final int lpSupporterW  = pxTarget.cmRegion.height /16;
  final int lpBarrelH     = pxTarget.cmRegion.height *2 /3;
  final int lpSupporterG  = (pxTarget.cmRegion.height - lpBarrelH)/2;
  final int lpDuctG       = lpSupporterW;
  fill(pxTarget.cmBackColor);
  rect(//..barrel
    pxTarget.cmRegion.x,
    pxTarget.cmRegion.y + lpSupporterG,
    lpBarrelW,
    lpBarrelH
  );
  rect(//..supporter west
    pxTarget.cmRegion.x + lpBarrelWestX,
    pxTarget.cmRegion.y,
    lpSupporterW,
    pxTarget.cmRegion.height
  );
  rect(//..supporter east
    pxTarget.cmRegion.x + lpBarrelEastX,
    pxTarget.cmRegion.y,
    lpSupporterW,
    pxTarget.cmRegion.height
  );
  triangle(//..duct
    pxTarget.cmRegion.x +lpBarrelW +lpDuctG,
    pxTarget.cmRegion.y,
    //--
    ccGetEndX(pxTarget.cmRegion),
    pxTarget.cmRegion.y,
    //--
    pxTarget.cmRegion.x +lpBarrelW +lpDuctG,
    ccGetCenterY(pxTarget.cmRegion) -lpDuctG
  );
}//+++