/* * 
 * Affected Value
 *
 * a kind of value can affect each other.
 * 
 */

static final String C_MSG =
  "press [w] to increment the master value..\n"+
  "press [s] to decrement the master value..\n"+
  "press [q] to exit..\n";

static final int   C_BAR_WIDTH = 20; 
static final int   C_BAR_GAP   =  5; 
static final float C_MASTER_STEP  = 16f;
static final float C_AFFECT_RATIO = 64f;

int pbRoller = 0;
int pbCenterX,pbCenterY;

ZcReal pbMaster = new ZcReal(80f);
ZcReal pbSlave = new ZcReal(40f);

void setup(){
  size(320,240);
  frameRate(16);
  pbCenterX = width/2;
  pbCenterY = height/2;
}//++!

void draw(){
  
  //-- pre
  background(0);
  pbRoller++;pbRoller&=0xF;
  
  //-- scan
  ccTransfer(pbMaster,pbSlave,C_AFFECT_RATIO);
   
  //-- output ** master 
  fill(pbMaster.cmVal>0?0xFF44FF44:0xFFFF7777);
  rect(
    pbCenterX-C_BAR_GAP-C_BAR_WIDTH,pbCenterY,
    C_BAR_WIDTH,-1f * pbMaster.cmVal
  );
  textAlign(RIGHT,BOTTOM);
  text(
    String.format("master:%+4.2f",pbMaster.cmVal),
    pbCenterX-C_BAR_GAP-C_BAR_WIDTH,pbCenterY
  );
  
  //-- output ** slave
  fill(pbSlave.cmVal>0?0xFF22DD22:0xFFDD5555);
  rect(
    pbCenterX+C_BAR_GAP,pbCenterY,
    C_BAR_WIDTH,-1f * pbSlave.cmVal
  );
  textAlign(LEFT,BOTTOM);
  text(
    String.format("slave:%+4.2f",pbSlave.cmVal),
    pbCenterX+C_BAR_GAP*2+C_BAR_WIDTH,pbCenterY
  );
  
  //-- output ** base line
  stroke(0xFFEEEE33);
  line(0,pbCenterY,width,pbCenterY);
  noStroke();
  
  //-- output ** info
  if(pbRoller<7){
    textAlign(LEFT,TOP);
    fill(0xFF);
    text(C_MSG,C_BAR_GAP,pbCenterY+C_BAR_GAP);
  }//..?
  
}//++~

void keyPressed(){
  switch(key){
    case 'w':pbMaster.cmVal+=C_MASTER_STEP;break;
    case 's':pbMaster.cmVal-=C_MASTER_STEP;break;
    case 'q':exit();break;
    default:break;
  }//..?
}//+++

class ZcReal{
  
  float cmVal;
  boolean cmStatical;
  
  ZcReal(){
    cmVal=0.0f;
    cmStatical=false;  
  }//++!
  
  ZcReal(float pxInitValue){
    cmVal=pxInitValue;
    cmStatical=false;  
  }//++!
  
  ZcReal(float pxInitValue, boolean pxStatical){
    cmVal=pxInitValue;
    cmStatical=pxStatical;  
  }//++!
  
}//***

void ccTransfer(ZcReal pxPotentialP, ZcReal pxPotentialN, float pxRatio){
  if(pxRatio<=0f){return;}
  float lpDiff=pxPotentialP.cmVal-pxPotentialN.cmVal;
  lpDiff/=pxRatio;
  if(!pxPotentialP.cmStatical){pxPotentialP.cmVal-=lpDiff;}
  if(!pxPotentialN.cmStatical){pxPotentialN.cmVal+=lpDiff;}
}//+++

//eof
