
import java.io.File;
import processing.core.PApplet;
import static processing.core.PApplet.binary;
import static processing.core.PApplet.constrain;
import static processing.core.PApplet.day;
import static processing.core.PApplet.hex;
import static processing.core.PApplet.hour;
import static processing.core.PApplet.map;
import static processing.core.PApplet.match;
import static processing.core.PApplet.minute;
import static processing.core.PApplet.month;
import static processing.core.PApplet.nf;
import static processing.core.PApplet.nfc;
import static processing.core.PApplet.println;
import static processing.core.PApplet.second;
import static processing.core.PApplet.split;
import static processing.core.PApplet.str;
import static processing.core.PApplet.year;
import static processing.core.PConstants.CENTER;
import static processing.core.PConstants.HALF_PI;
import static processing.core.PConstants.LEFT;
import static processing.core.PConstants.PI;
import static processing.core.PConstants.PIE;
import static processing.core.PConstants.QUARTER_PI;
import static processing.core.PConstants.RIGHT;
import static processing.core.PConstants.TOP;
import processing.data.IntDict;
import processing.data.StringList;
import processing.data.Table;
import processing.data.TableRow;


/**
 *
 * @author 2053
 */
public class Extab extends PApplet {

  
  
/* ** ** ** ** ** ** ** */
/* **                ** */
/* **    FUNCTION    ** */
/* **                ** */
/* ** ** ** ** ** ** ** */
  
  
  
//-- --- --- --- --- ---
//  Utility
//-- --- --- --- --- ---
//--
String fnByteToLines(String pxTitle,byte[] pxBytes, int pxReturnSets, boolean pxShowsBinary,String pxHeader,int pxOffset){
  String pyResult=">>> "+pxTitle+"\n";
  if(pxBytes==null){return pyResult+"\nnull!!";}
  int lpLength=pxBytes.length;
  for(int i=0;i<lpLength;i++){
    if(i%2==0 && i!=0){pyResult+=pxHeader+nf(i/2-1+pxOffset,3)+"]    ";}
    if(i%(2*pxReturnSets)==0 && i!=0){pyResult+="::\n";}
    pyResult+=pxShowsBinary? binary(pxBytes[i]):hex(pxBytes[i]);
    pyResult+="_";
  }pyResult+="[..End]<<<";
  return pyResult;
}//++
//--
String fnTimeStamp(char pxMode_lsc){
  String pyStamp="_";
  switch(pxMode_lsc){
    case 'l':pyStamp+=nf(year(),4)+nf(month(),2)+nf(day(),2)+nf(hour(),2)+nf(minute(),2)+nf(second(),2);break;
    case 's':pyStamp+=nf(year(),4)+nf(month(),2)+nf(day(),2);break;
    case 'c':pyStamp=nf(hour(),2)+"   "+nf(minute(),2);break;
    default:break;
  } 
  return pyStamp;  
}//++
//--
String[] fnListFileNames(String pxPath) {
  File lpFile = new File(pxPath);
  if (lpFile.isDirectory()) {
    String lpList[] = lpFile.list();
    return lpList;
  } else {
    return null;
  }
}//++
//--
boolean fnLockerTest(String pxPath){
  File lpLocker=new File(pxPath);
  if(lpLocker.exists()){
    return true;
  }else{
    String[] lpDummy={"Dont","Touch","Me"};
    saveStrings(lpLocker.getAbsolutePath(),lpDummy);
    if(lpLocker.exists()){lpLocker.deleteOnExit();}
    return false;
  }
}//++
//--
  
//-- --- --- --- --- ---
//  Shapes
//-- --- --- --- --- ---
//--
void shapeFan(int pxX, int pxY, int pxScale, char pxStyle_udlr){
  fill(0xAA);
  ellipse(pxX, pxY, pxScale, pxScale);
  switch(pxStyle_udlr){
    case 'u':rect(pxX-pxScale/2,pxY-pxScale,pxScale/2,pxScale);break;
    case 'd':rect(pxX,pxY,pxScale/2,pxScale);break;
    case 'l':rect(pxX,pxY-pxScale/2,(-1)*pxScale,pxScale/2);break;
    case 'r':rect(pxX,pxY-pxScale/2,pxScale,pxScale/2);break;
    default:break;
  }
}//++
void shapeWeigher(int pxX, int pxY, int pxWidth, int pxHeight){
  fill(0xAA);
  int lpXOff=pxWidth/4;
  int lpYOff=pxHeight/12;
  rect(pxX,pxY,pxWidth,pxHeight,lpXOff,lpXOff,0,0);
  rect(pxX-lpXOff,pxY-lpYOff*2,pxWidth+2*lpXOff,lpYOff,0,0,lpYOff,lpYOff);
  rect(pxX+pxWidth/2-lpXOff/2,pxY-lpYOff*2,lpXOff,pxHeight);  
}//++
void shapeHopper(int pxX, int pxY, int pxWidth, int pxHeight){
  fill(0xAA);
  int lpXOff=pxWidth/4;
  int lpYOff=pxWidth/4;
  rect(pxX,pxY,pxWidth, pxHeight-lpYOff);
  quad(pxX,pxY+pxHeight-lpYOff, pxX+pxWidth,pxY+pxHeight-lpYOff, pxX+pxWidth-lpXOff,pxY+pxHeight, pxX+lpXOff,pxY+pxHeight);
}//++
void shapeMixer(int pxX, int pxY, int pxScale){
  fill(0xAA);
  rect(pxX,pxY,pxScale,pxScale/3);
  ellipse(pxX+pxScale/4,           pxY+pxScale/4, pxScale/2, pxScale/2);
  ellipse(pxX+pxScale/4+pxScale/2, pxY+pxScale/4, pxScale/2, pxScale/2); 
}//++
//-- --

  
  
  
  
  
  
  
  
  
  
  
/* ** ** ** ** ** ** ** */
/* **                ** */
/* **      CLASS     ** */
/* **                ** */
/* ** ** ** ** ** ** ** */
//-- --- --- --- --- ---
//  cBasicElement
//-- --- --- --- --- ---
//--

  
class EcLabledFrame{
  //---
  int cmX, cmY, cmWidth, cmHeight;
  char cmStyle;
  String cmLable;
  boolean cmAct;
  int cmFocusID;
  //---
  EcLabledFrame(int pxX, int pxY, int pxWidth, int pxHeight, String pxLable, char pxStyle_mpg, int pxID){
    cmX=pxX;cmY=pxY;cmWidth=pxWidth;cmHeight=pxHeight;cmStyle=pxStyle_mpg;
    cmLable=pxLable;
    cmAct=false;
    cmFocusID=pxID;
  }
  //---
  void ccUpdate(int pxFocus){
    cmAct=(cmFocusID==pxFocus);
    switch(cmStyle){
      case 'm':spModelFrame();break;
      case 'p':spPannelFrame();break;
      case 'g':spGroupFrame();break;
      default:break;
    }
  }
  //---
  boolean ccIsMouseOver(){return (mouseX>cmX && mouseY>cmY&&mouseX<cmX+cmWidth && mouseY<cmY+cmHeight);}
  void ccFlip(){cmAct=!cmAct;}
  //--
  int[] ccGetPos(){
    int[] lpPos=new int[2];
    lpPos[0]=cmX;lpPos[1]=cmY;
    return lpPos;
  }
  //--
  boolean ccGotDragged(int pxFocus){
    if(pxFocus==cmFocusID){cmX=mouseX-10;cmY=mouseY-10;return true;}
    return false;
  }
  //---
  void spModelFrame(){
    stroke(cmAct? color(0xEE,0xEE,0x33): color(0x55,0x55,0x55,0x55));noFill();rect(cmX,cmY,cmWidth,cmHeight);noStroke();
  }
  void spPannelFrame(){
    fill(cmAct? color(0xEE,0xEE,0x33): 0xEE);rect(cmX,cmY,cmWidth,cmHeight);
    fill(0x33);rect(cmX+2,cmY+2,cmWidth-4,cmHeight-4);
    fill(cmAct? color(0xEE,0xEE,0x33): 0xEE);rect(cmX,cmY,4+cmLable.length()*8,16);
    fill(0x33);text(cmLable,cmX+2,cmY); 
  }
  void spGroupFrame(){
    stroke(0x77);fill(0x33);
    rect(cmX,cmY,cmWidth,cmHeight);
    rect(cmX+6,cmY-6,4+cmLable.length()*8,16);
    fill(0xEE);text(cmLable,cmX+8,cmY-6); 
    noStroke();
  }
  //---
}//----EcLabledFrame

class EcMultiButton{
  //---
  int cmX, cmY, cmWidth, cmHeight;
  char cmStyle;
  String cmLable;
  //---
  int cmBackColorNM,cmBackColorOV,cmBackColorDN;
  int cmForeColorOFF,cmForeColorON;
  //---
  boolean cmAct;
  boolean cmDisable;
  //--
  //---
  EcMultiButton(int pxWidth, int pxHeight, String pxLable, char pxStyle_mpl){
    cmWidth=pxWidth;cmHeight=pxHeight;cmStyle=pxStyle_mpl;
    cmLable=pxLable;
    cmX=0;cmY=0;
    cmBackColorNM=color(0x88,0x88,0x88);
    cmBackColorOV=color(0xBB,0xBB,0xBB);
    cmBackColorDN=color(0xAA,0xAA,0xAA);
    cmForeColorOFF=color(0x55,0x55,0x55);
    cmForeColorON=color( 0xEE,0xEE,0x33);
    cmAct=false;cmDisable=false;
  }
  //---
  void ccUpdate(int[] pxPos, int pxOffsetX, int pxOffsetY){ccRepos(pxPos,pxOffsetX,pxOffsetY);
    switch(cmStyle){
      case 'm':spMenuButton();break;
      case 'p':spPushButton();break;
      case 'l':spLatchButton();break;
      default:break;
    }
    if(cmDisable){fill(cmForeColorOFF);rect(cmX+cmWidth/4,cmY+cmHeight/2,cmWidth/2,2);}
  }
  //--
  void ccDisable(){cmDisable=true;cmAct=false;cmLable="nc";cmForeColorOFF=color(0xFF,0xFF,0xFF);}
  void ccFlip(){cmAct=!cmAct;}
  //---
  boolean ccIsMouseOver(){return (!cmDisable)&&(mouseX>cmX && mouseY>cmY&&mouseX<cmX+cmWidth && mouseY<cmY+cmHeight);}
  //--
  int[] ccGetPos(){
    int[] lpPos=new int[2];
    lpPos[0]=cmX;lpPos[1]=cmY;
    return lpPos;
  }
  //-- 
  void ccRepos(int[] pxPos, int pxOffsetX, int pxOffsetY){
    if(pxPos==null){return;}
    if(pxPos.length!=2){return;}
    cmX=pxPos[0]+pxOffsetX;cmY=pxPos[1]+pxOffsetY;
  }
  //---
  void spMenuButton(){
    fill(ccIsMouseOver()?(mousePressed?cmBackColorDN:cmBackColorOV):cmBackColorNM);rect(cmX,cmY,cmWidth,cmHeight);
    fill(cmAct?cmForeColorON:cmForeColorOFF);textAlign(CENTER);text(cmLable,cmX+cmWidth/2,cmY+cmHeight/2+4);textAlign(LEFT,TOP);
  }
  void spPushButton(){
    fill(0x55);rect(cmX,cmY,cmWidth,cmHeight);
    fill(ccIsMouseOver()?(mousePressed?cmBackColorDN:cmBackColorOV):cmBackColorNM);stroke(0xCC);rect(cmX+2,cmY+2,cmWidth-5,cmHeight-5);noStroke();
    fill(cmAct?cmForeColorON:cmForeColorOFF);textAlign(CENTER);text(cmLable,cmX+cmWidth/2,cmY+cmHeight/2+4);textAlign(LEFT,TOP);
  }
  void spLatchButton(){
    fill(0x55);rect(cmX,cmY,cmWidth,cmHeight);
    fill(ccIsMouseOver()?(mousePressed?cmBackColorDN:cmBackColorOV):cmBackColorNM);stroke(0xCC);rect(cmX+2,cmY+2,cmWidth-5,cmHeight-5);noStroke();
    fill(0x22);textAlign(CENTER);text(cmLable,cmX+cmWidth/2+8,cmY+cmHeight/2+4);textAlign(LEFT,TOP);
    fill(cmAct?cmForeColorON:cmForeColorOFF);rect(cmX+6,cmY+6,8,cmHeight-13);
  }
  //--
  void ccSetColor(int pxBCM,int pxBCO,int pxBCD,int pxFCF,int pxFCO){  
    cmBackColorNM=pxBCM;
    cmBackColorOV=pxBCO;
    cmBackColorDN=pxBCD;
    cmForeColorOFF=pxFCF;
    cmForeColorON=pxFCO;
  }
  //--
}class EcInputContainer extends EcMultiButton{
  //--
  int cmFocusID;
  int cmVal;
  boolean cmShowsAsFloat;
  //--
  int cmMin,cmMax;
  //--
  public EcInputContainer(int pxWidth, int pxHeight, String pxLable, int pxID){
    super(pxWidth, pxHeight, pxLable, 'x');
    cmVal=0;cmFocusID=pxID;cmShowsAsFloat=false;
    cmMin=0;cmMax=9999;
  }
  //--
  @Override
  void ccUpdate(int[] pxPos, int pxOffsetX, int pxOffsetY){ccRepos(pxPos,pxOffsetX,pxOffsetY);
    fill(0x44);rect(cmX,cmY,cmWidth,cmHeight);
    fill(cmAct?cmForeColorON:cmForeColorOFF);rect(cmX,cmY,cmWidth-2,cmHeight-2);
    fill(cmBackColorDN);textAlign(RIGHT,TOP);text((cmShowsAsFloat? nfc(((float)cmVal)/10,1):str(cmVal))+cmLable,cmX+cmWidth-2,cmY+2);textAlign(LEFT,TOP);
  }
  //--
  void ccShiftValue(int pxOffset){cmVal+=pxOffset;cmVal=constrain(cmVal, cmMin, cmMax);}
  void ccChangeValue(int pxVal){cmVal=constrain(pxVal, cmMin, cmMax);}
  void ccRefreshAct(int pxFocus){cmAct=(pxFocus==cmFocusID);}
  //--
}//++EcMultiButton and his offsprings

class EcMultiLamp{
  //---
  int cmX,cmY;
  char cmStyle;
  int cmScale;
  int cmOffColor;
  int cmOnColor;
  boolean cmAct;
  int cmHeight;
  //---
  String cmLable="nc";
  //---
  EcMultiLamp(int pxScale, char pxStyle_tbselrd){
    cmStyle=pxStyle_tbselrd;
    cmScale=pxScale;
    cmX=0;cmY=0;
    cmOffColor=color(0x77,0x77,0x77);
    cmOnColor=color(0xEE,0xEE,0x33);
    cmAct=false;
    cmHeight=pxStyle_tbselrd=='t'? 16:3;
  }
  //---
  void ccUpdate(int[] pxPos, int pxOffsetX, int pxOffsetY){ccRepos(pxPos,pxOffsetX,pxOffsetY);
    fill(cmAct? cmOnColor:cmOffColor);
    switch(cmStyle){
      case 't':rect(cmX,cmY,cmScale,cmHeight);fill(0x33);text(cmLable,cmX+1,cmY);break;
      case 'b':rect(cmX,cmY,cmScale,cmHeight);break;
      case 's':rect(cmX,cmY,cmScale,cmScale);break;
      case 'e':ellipse(cmX,cmY,cmScale,cmScale);;break;
      case 'r':triangle(cmX,cmY,cmX+cmScale,cmY+cmScale/2,cmX,cmY+cmScale);break;
      case 'l':triangle(cmX+cmScale,cmY,cmX+cmScale,cmY+cmScale,cmX,cmY+cmScale/2);break;
      case 'd':triangle(cmX,cmY,cmX+cmScale,cmY,cmX+cmScale/2,cmY+cmScale);break;
      default:break;
    }
  }
  void ccFlip(){cmAct=!cmAct;}
  //--
  //--
  int[] ccGetPos(){
    int[] lpPos=new int[2];
    lpPos[0]=cmX;lpPos[1]=cmY;
    return lpPos;
  }
  //--
  void ccRepos(int[] pxPos, int pxOffsetX, int pxOffsetY){
    if(pxPos==null){return;}
    if(pxPos.length!=2){return;}
    cmX=pxPos[0]+pxOffsetX;cmY=pxPos[1]+pxOffsetY;
  }
  //--
  //---
}//++EcMultiLamp

class EcMultiGauge{
  int cmX,cmY,cmWidth,cmHeight;
  char cmStyle;
  float cmFill;
  int cmOnColor,cmOffColor;
  boolean cmAct;
  int cmVal;
  float cmSpan;
  //---
  EcMultiGauge(int pxWidth, int pxHeight, char pxStyle_hvda){
    cmWidth=pxWidth;cmHeight=pxHeight;cmStyle=pxStyle_hvda;
    cmX=0;cmY=0;cmFill=1;
    cmOnColor=color(0xEE,0x33,0x33);cmOffColor=color(0xEE,0xEE,0x33);
    cmVal=0;cmAct=false;cmSpan=5000;
  }
  //---
  void ccUpdate(int[] pxPos, int pxOffsetX, int pxOffsetY){ccRepos(pxPos, pxOffsetX, pxOffsetY);
    Float lpTag =cmSpan;
    Integer lpVal=cmVal>lpTag.intValue()?lpTag.intValue():cmVal;
    cmFill=map(lpVal.floatValue(),0,cmSpan,1,99);
    switch(cmStyle){  
      case 'h':spHorizonBar();break;//H-bar
      case 'v':spVerticalBar();break;//V-bar
      case 'd':spDamper();break;//Damper
      case 'a':spArc();break;//Arc
      default:break;
    }
  }
  //---
  void ccFlip(){cmAct=!cmAct;}
  //--
  int[] ccGetPos(){
    int[] lpPos=new int[2];
    lpPos[0]=cmX;lpPos[1]=cmY;
    return lpPos;
  }
  //--
  void ccRepos(int[] pxPos, int pxOffsetX, int pxOffsetY){
    if(pxPos==null){return;}
    if(pxPos.length!=2){return;}
    cmX=pxPos[0]+pxOffsetX;cmY=pxPos[1]+pxOffsetY;
  }
  //---
  void spHorizonBar(){
    fill(0x66);rect(cmX,cmY,cmWidth,cmHeight);
    fill(cmAct?cmOnColor:cmOffColor);
    rect(cmX,cmY,cmWidth*(cmFill/100),cmHeight);
  }
  void spVerticalBar(){
    fill(0x66);rect(cmX,cmY,cmWidth,(-1)*cmHeight);
    fill(cmAct?cmOnColor:cmOffColor);
    rect(cmX,cmY,cmWidth,-(cmHeight*(cmFill/100)));
  }
  void spDamper(){
    pushMatrix();
    translate(cmX,cmY);
    rotate((PI/2)*(cmFill/100));
    fill(cmAct?cmOnColor:cmOffColor);rect(-(cmWidth/2),-(cmHeight/2),cmWidth,cmHeight);
    popMatrix();
  }
  void spArc(){
    fill(cmAct?cmOnColor:cmOffColor);
    arc(cmX,cmY,cmWidth,cmHeight,PI-QUARTER_PI,PI-QUARTER_PI+(PI+HALF_PI)*(cmFill/100),PIE);
  }
}//++EcMultiGauge

class EcPushButtonDeo{
  int cmX,cmY,cmWidth,cmHeight;
  //---
  int cmBackColorNM,cmBackColorOV,cmBackColorDN;
  int cmForeColorOFF,cmForeColorON;
  //--
  String cmNegtiveText;
  String cmPositiveText;
  //--
  int cmStatus;
  //--
  EcPushButtonDeo(int pxYScale,String pxNegtiveText,String pxPositiveText){
    cmHeight=pxYScale;
    cmWidth=pxYScale*2;
    cmX=0;cmY=0;
    //--
    cmNegtiveText=pxNegtiveText;
    cmPositiveText=pxPositiveText;
    //--
    cmBackColorNM=color(0x88,0x88,0x88);
    cmBackColorOV=color(0xBB,0xBB,0xBB);
    cmBackColorDN=color(0xAA,0xAA,0xAA);
    cmForeColorOFF=color(0x55,0x55,0x55);
    cmForeColorON=color( 0xEE,0xEE,0x33);
    //--
    cmStatus=0;
  }
  //--
  void ccUpdate(int[] pxPos, int pxOffsetX, int pxOffsetY){ccRepos(pxPos,pxOffsetX,pxOffsetY);
    fill(0x55);rect(cmX,cmY,cmWidth,cmHeight);
    fill(ccIsMouseOver()==1? (mousePressed? cmBackColorDN:cmBackColorOV):cmBackColorNM);rect(cmX+2,cmY+2,cmWidth/2-4,cmHeight-4);
    fill(ccIsMouseOver()==2? (mousePressed? cmBackColorDN:cmBackColorOV):cmBackColorNM);rect(cmX+cmWidth/2+2,cmY+2,cmWidth/2-4,cmHeight-4);
    textAlign(CENTER,CENTER);
    fill(cmStatus==1? cmForeColorON:cmForeColorOFF);text(cmNegtiveText,cmX+cmWidth/4,cmY+cmHeight/2-2);
    fill(cmStatus==2? cmForeColorON:cmForeColorOFF);text(cmPositiveText,cmX+cmWidth/2+cmWidth/4,cmY+cmHeight/2-2);
    textAlign(LEFT,TOP);
  }
  //--
  int ccIsMouseOver(){
    if(mouseX>cmX && mouseY>cmY&&
       mouseX<cmX+cmWidth && mouseY<cmY+cmHeight){
       if(mouseX>cmX+cmWidth/2){
         return 2;
       }else{return 1;}
     }else{return 0;}
  }
  //--
  //--
  int[] ccGetPos(){
    int[] lpPos=new int[2];
    lpPos[0]=cmX;lpPos[1]=cmY;
    return lpPos;
  }
  //--
  void ccRepos(int[] pxPos, int pxOffsetX, int pxOffsetY){
    if(pxPos==null){return;}
    if(pxPos.length!=2){return;}
    cmX=pxPos[0]+pxOffsetX;cmY=pxPos[1]+pxOffsetY;
  }
  //--
  //--
  }//+++EcPushButtonDeo

class EcPushButtonTre{
  //--
  boolean cmIsVertical;
  int cmX,cmY,cmWidth,cmHeight;
  //--
  int cmBackColorNM,cmBackColorOV,cmBackColorDN;
  int cmForeColorOFF,cmForeColorON;
  //--
  String cmNegtiveText;
  String cmEnterText;
  String cmPositiveText;
  //--
  int cmStatus;
  //--
  public EcPushButtonTre(int pxYScale,String pxNegtiveText,String pxEnterText,String pxPositiveText, boolean pxIsVertical) {
    cmIsVertical=pxIsVertical;
    //--
    cmHeight=pxYScale;cmWidth=cmIsVertical?pxYScale:pxYScale*3;
    cmNegtiveText=pxNegtiveText;cmEnterText=pxEnterText;cmPositiveText=pxPositiveText;
    //--
    cmX=0;cmY=0;
    //--
    cmBackColorNM=color(0x88,0x88,0x88);
    cmBackColorOV=color(0xBB,0xBB,0xBB);
    cmBackColorDN=color(0xAA,0xAA,0xAA);
    cmForeColorOFF=color(0x55,0x55,0x55);
    cmForeColorON=color( 0xEE,0xEE,0x33);
    //--
    cmStatus=0;
    //--
  }
  //--
  //--
  //--
  void ccUpdate(int[] pxPos, int pxOffsetX, int pxOffsetY){ccRepos(pxPos,pxOffsetX,pxOffsetY);
    int lpBlockWW=cmIsVertical? cmWidth-4:cmWidth/3-4;
    int lpBlockHH=cmIsVertical? cmHeight/3-4:cmHeight-4;
    int lpBlockXO=cmIsVertical? 0:cmWidth/3;
    int lpBlockYO=cmIsVertical? cmHeight/3:0;
    int lpLableXO=cmIsVertical? cmWidth/2-2:cmWidth/6;
    int lpLableYO=cmIsVertical? cmHeight/6:cmHeight/2-2;
  
    fill(0x55);rect(cmX,cmY,cmWidth,cmHeight);
    fill(ccIsMouseOver()==1? (mousePressed? cmBackColorDN:cmBackColorOV):cmBackColorNM);rect(cmX+2+lpBlockXO*0,cmY+2+lpBlockYO*0,lpBlockWW,lpBlockHH);
    fill(ccIsMouseOver()==3? (mousePressed? cmBackColorDN:cmBackColorOV):cmBackColorNM);rect(cmX+2+lpBlockXO*1,cmY+2+lpBlockYO*1,lpBlockWW,lpBlockHH);
    fill(ccIsMouseOver()==2? (mousePressed? cmBackColorDN:cmBackColorOV):cmBackColorNM);rect(cmX+2+lpBlockXO*2,cmY+2+lpBlockYO*2,lpBlockWW,lpBlockHH);
    textAlign(CENTER,CENTER);
    fill(cmStatus==1? cmForeColorON:cmForeColorOFF);text(cmNegtiveText,cmX+lpLableXO,cmY+lpLableYO);
    fill(cmStatus==3? cmForeColorON:cmForeColorOFF);text(cmEnterText,cmX+lpLableXO*(cmIsVertical?1:3),cmY+lpLableYO*(!cmIsVertical?1:3));
    fill(cmStatus==2? cmForeColorON:cmForeColorOFF);text(cmPositiveText,cmX+lpLableXO*(cmIsVertical?1:5),cmY+lpLableYO*(!cmIsVertical?1:5));
    textAlign(LEFT,TOP);
  }
  //--
  int ccIsMouseOver(){
    if(mouseX>cmX && mouseY>cmY&&
       mouseX<cmX+cmWidth && mouseY<cmY+cmHeight){
       if(cmIsVertical){
        if(mouseY>cmY+cmHeight/3){if(mouseY>cmY+cmHeight*2/3){return 2;}else{return 3;}}else{return 1;}
       }else{
        if(mouseX>cmX+cmWidth/3){if(mouseX>cmX+cmWidth*2/3){return 2;}else{return 3;}}else{return 1;}
       }
     }else{return 0;}
    //--
  }
  //--
  //--
  int[] ccGetPos(){
    int[] lpPos=new int[2];
    lpPos[0]=cmX;lpPos[1]=cmY;
    return lpPos;
  }
  //--
  void ccRepos(int[] pxPos, int pxOffsetX, int pxOffsetY){
    if(pxPos==null){return;}
    if(pxPos.length!=2){return;}
    cmX=pxPos[0]+pxOffsetX;cmY=pxPos[1]+pxOffsetY;
  }
  //--
  }class EcFeedButton extends EcPushButtonTre{
  int cmLampWidth;
  boolean cmAct;
  String cmLable;
  //--
  public EcFeedButton(String pxLable,int pxLampWidth,int pxYScale,String pxNegtiveText,String pxEnterText,String pxPositiveText, boolean pxIsVertical) {
    super(pxYScale, pxNegtiveText, pxEnterText, pxPositiveText, pxIsVertical);
    cmLampWidth=pxLampWidth;
    cmLable=pxLable;
    cmAct=false;
  }
  //--
  @Override void ccUpdate(int[] pxPos, int pxOffsetX, int pxOffsetY){
    super.ccUpdate(pxPos, pxOffsetX, pxOffsetY);
    fill(cmAct?color(0x33,0xEE,0x33):color(0x55,0x55,0x55));rect(super.cmX,super.cmY,-1*cmLampWidth,super.cmHeight);
    fill(0x99);text(cmLable,super.cmX+2-cmLampWidth,super.cmY);
  }
  //--
}//++

class EcValueContainer{
  //---
  int cmX, cmY, cmWidth, cmHeight;
  char cmStyle;
  boolean cmHasBack;
  String cmLable;
  //---
  int cmTextColor,cmBoxColor;
  //---
  int cmVal;
  String cmTextString;
  //---
  int cmMax,cmMin;
  //--
  EcValueContainer(int pxWidth, int pxHeight, String pxLable, char pxStyle_dhbf, boolean pxHasBack){
    cmWidth=pxWidth;cmHeight=pxHeight;cmStyle=pxStyle_dhbf;
    cmLable=pxLable;cmHasBack=pxHasBack;
    cmX=0;cmY=0;
    cmTextColor =color(0x33,0x33,0x22);
    cmBoxColor  =color(0xCC,0xCC,0xCC);
    cmVal=0;cmTextString="..";
    cmMax=5000;cmMin=0;
  }
  //---
  void ccUpdate(int[] pxPos, int pxOffsetX, int pxOffsetY){ccRepos(pxPos, pxOffsetX, pxOffsetY);
    switch(cmStyle){
      case 'd':cmTextString=str(cmVal);break;
      case 'b':cmTextString=binary(cmVal,16);break;
      case 'h':cmTextString=hex(cmVal,4);break;
      case 'f':cmTextString=nfc(((float)cmVal)/10,1);break;//...decimal point set missing 
      default:break;
    }
    if(cmHasBack){
      fill(0x44);rect(cmX,cmY,cmWidth,cmHeight);
      fill(cmBoxColor);rect(cmX+2,cmY+2,cmWidth-2,cmHeight-2);
    }
    fill(cmTextColor);textAlign(RIGHT,TOP);text(cmTextString+cmLable,cmX+cmWidth-1,cmY+2);textAlign(LEFT,TOP);
  }
  //---
  void ccShiftValue(int pxOffset){cmVal+=pxOffset;cmVal=constrain(cmVal, cmMin, cmMax);}
  void ccChangeValue(int pxVal){cmVal=constrain(pxVal, cmMin, cmMax);}
  //--
  int[] ccGetPos(){
    int[] lpPos=new int[2];
    lpPos[0]=cmX;lpPos[1]=cmY;
    return lpPos;
  }
  //--
  void ccRepos(int[] pxPos, int pxOffsetX, int pxOffsetY){
    if(pxPos==null){return;}
    if(pxPos.length!=2){return;}
    cmX=pxPos[0]+pxOffsetX;cmY=pxPos[1]+pxOffsetY;
  }
  //---
}//--HcTextContainer

class EcTitleContainer{
  //--
  int cmX, cmY, cmWidth, cmHeight;
  //
  String[] cmTitleList;
  //--
  int cmIndex;
  int cmTextColor,cmBoxColor;
  //--
  EcTitleContainer(int pxWidth, int pxHeight, String[] pxList){
    cmWidth=pxWidth;cmHeight=pxHeight;
    cmX=0;cmY=0;cmIndex=0;
    //--
    cmTextColor =color(0x66,0x66,0x66);
    cmBoxColor  =color(0xCC,0xCC,0xCC);
    //--
    cmTitleList=pxList;
  }
  //--
  void ccUpdate(int[] pxPos, int pxOffsetX, int pxOffsetY){ccRepos(pxPos, pxOffsetX, pxOffsetY);
    fill(cmTextColor);rect(cmX,cmY,cmWidth,cmHeight);
    fill(cmBoxColor);rect(cmX+1,cmY+1,cmWidth-1,cmHeight-1);
    if(cmTitleList!=null){fill(cmTextColor);text(cmTitleList[cmIndex],cmX+3,cmY+3);}
  }
  //--
  void ccShiftIndex(int pxOffset){if(cmTitleList!=null){cmIndex+=pxOffset;cmIndex=constrain(cmIndex,0, cmTitleList.length-1);}}
  void ccChangeIndex(int pxIndex){cmIndex=constrain(pxIndex, 0, ccTellSize()-1);}
  //--
  String ccTellCurrentTitle(){return cmTitleList[cmIndex];}
  int ccTellSize(){return cmTitleList.length;}
  //--
  //--
  int[] ccGetPos(){
    int[] lpPos=new int[2];
    lpPos[0]=cmX;lpPos[1]=cmY;
    return lpPos;
  }
  //--
  void ccRepos(int[] pxPos, int pxOffsetX, int pxOffsetY){
    if(pxPos==null){return;}
    if(pxPos.length!=2){return;}
    cmX=pxPos[0]+pxOffsetX;cmY=pxPos[1]+pxOffsetY;
  }
  //--
}//+++
//-- --



//-- --- --- --- --- ---
//  cPlantElement
//-- --- --- --- --- ---
//--
class PcMotorBlock{
  int cmX,cmY,cmWidth,cmHeight;
  String cmLable;
  //--
  boolean cmIsAX,cmIsAN,cmIsAL;
  float cmInputSpan,cmOutputSpan;
  int cmCurrent;
  //--
  PcMotorBlock(int pxWidth, int pxHeight, String pxLable){
    cmWidth=pxWidth;cmHeight=pxHeight;cmLable=pxLable;
    cmX=0;cmY=0;
    cmIsAX=false;cmIsAN=false;cmIsAL=false;
    cmInputSpan =5000;
    cmOutputSpan= 100;cmCurrent=0;
  }
  //--
  void ccUpdate(int[] pxPos, int pxOffsetX, int pxOffsetY){
    ccRepos(pxPos,  pxOffsetX,  pxOffsetY);
    stroke(cmIsAX? color(0xEE,0xEE,0x33):color(0x55,0x55,0x55));
    fill(cmIsAL?   color(0x77,0x33,0x33):color(0x55,0x55,0x55));
    rect(cmX,cmY,cmWidth,cmHeight);noStroke();
    //--
    if(cmIsAN){fill(0x22,0x77,0x22);rect(cmX+3,cmY+cmHeight-7,cmWidth-6,6);}
    //--
    if(cmCurrent>0){
      Float lpTag=cmInputSpan;
      Integer lpV= (cmCurrent>lpTag.intValue()? lpTag.intValue():cmCurrent);
      textAlign(RIGHT,TOP);fill(0xEE);
      text(nfc(map(lpV.floatValue(), 0, cmInputSpan, 0, cmOutputSpan),1)+" A",cmX+cmWidth-2,cmY);
      textAlign(LEFT,TOP);
      fill(0xEE,0xEE,0x33);
      rect(cmX+5,cmY+cmHeight-5,map(lpV.floatValue(), 0, cmInputSpan, 0, cmWidth-10),2);
    }
    //--
    fill(cmIsAX? color(0xEE,0xEE,0x33):color(0x99,0x99,0x99));
    text(cmLable,cmX+1,cmY+1);
  }
  //--
  int[] ccGetPos(){
    int[] lpPos=new int[2];
    lpPos[0]=cmX;lpPos[1]=cmY;
    return lpPos;
  }
  void ccRepos(int[] pxPos, int pxOffsetX, int pxOffsetY){
    if(pxPos==null){return;}
    if(pxPos.length!=2){return;}
    cmX=pxPos[0]+pxOffsetX;cmY=pxPos[1]+pxOffsetY;
  }
  //--
}//--PcMotorBlock
//-- --


//-- --- --- --- --- ---
//  cSystemElement
//-- --- --- --- --- ---
//--
class VcMessageBar{
  int cmX, cmY, cmWidth,cmMax;
  int cmHeight,cmReturn,cmIndex;
  String[] cmLineShower;
  String[] cmLineBuffer;
  boolean cmIsMid,cmIsLarge;
  StringList cmLogger;
  //---
  VcMessageBar(int pxX, int pxY, int pxWidth, int pxMax){
    cmX=pxX;cmY=pxY;cmWidth=pxWidth;cmMax=pxMax;
    cmHeight=16;cmReturn=16;cmIndex=0;
    cmLineShower=new String[8];for(int i=0;i<8;i++){cmLineShower[i]=">> "+str(i);}
    cmLineBuffer=new String[64];for(int i=0;i<64;i++){cmLineBuffer[i]=".. "+str(i);}
    cmLogger=new StringList();
  }
  //---
  void ccUpdate(){
    cmHeight=cmIsLarge? 128 : (cmIsMid? 48: 16);
    fill(0x77,0x77,0x77,0xCC);rect(cmX, cmY, cmWidth,cmHeight);
    for(int i=0;i<8;i++){cmLineShower[i]=cmLineBuffer[i+cmIndex];}
    fill(cmIndex==0? 0xFF:0xAA);text(cmLineShower[0],cmX+2,cmY);
    fill(0xAA);if(cmIsMid){
      text(cmLineShower[1],cmX+2,cmY+cmReturn*1);
      text(cmLineShower[2],cmX+2,cmY+cmReturn*2);
    }
    if(cmIsLarge){for(int i=3;i<8;i++){
      text(cmLineShower[i],cmX+2,cmY+cmReturn*i);
    }}
    if(cmIndex!=0){fill(0x88);text(str(cmIndex),cmX+cmWidth-25,cmY);}
  }
  //---
  void ccFlip(int pxV){switch (pxV){
    case 1:if(cmIsLarge){return;}cmIsMid=!cmIsMid;break;
    case 2:cmIsLarge=!cmIsLarge;cmIsMid=cmIsLarge;break;
    default:break;
  }}
  void ccShiftIndex(int pxOffset){cmIndex+=pxOffset;cmIndex=constrain(cmIndex,0,54);}
  //--
  void ccStack(String pxLine){
    String lpTimeStamp="--"+nf(hour(),2)+":"+nf(minute(),2);
    for(int i=63;i>=1;i--){cmLineBuffer[i]=cmLineBuffer[i-1];}
    if(pxLine.length()>cmMax){cmLineBuffer[0]=pxLine.substring(0,cmMax-1);}
    else{cmLineBuffer[0]=pxLine;}
    cmLineBuffer[0]+=lpTimeStamp;
    //cmLogger.append(cmLineBuffer[0]);
    //if(cmLogger.size()>255){ccLogToFile("data");}
  }
  //--
  /*
  void ccLogToFile(String pxName){
    String lpTimeStamp="_"+nf(year(),4)+nf(month(),2)+nf(day(),2)+nf(hour(),2)+nf(minute(),2)+nf(second(),2);
    String lpFileName=pxName+lpTimeStamp+".txt";
    if(cmLogger.size()>0){saveStrings(lpFileName,cmLogger.array());cmLogger.clear();}  
  }
  */
}//--VcMessageBar

class VcMouseAxis{
  boolean cmIsAxisOn;
  int cmMouseRelateX,cmMouseRelateY;
  //---
  VcMouseAxis(boolean pxAct){
    cmIsAxisOn=pxAct;
    cmMouseRelateX=0;
    cmMouseRelateY=0;
  }
  //---
  void ccUpdate(){
    if(!cmIsAxisOn){return;}
    stroke(255);fill(255);
    line(0,mouseY,width,mouseY);
    line(mouseX,0,mouseX,height);
    text(str(mouseX)+":"+str(mouseY),mouseX-64,mouseY);
    text(str(cmMouseRelateX)+":"+str(cmMouseRelateY),mouseX-64,mouseY-16);
    text(str(mouseX-cmMouseRelateX)+":"+str(mouseY-cmMouseRelateY),mouseX+2,mouseY-16);
    noFill();rect(cmMouseRelateX,cmMouseRelateY,mouseX-cmMouseRelateX,mouseY-cmMouseRelateY);
    noStroke();
  }
  //--
  void ccFlip(){cmIsAxisOn=!cmIsAxisOn;cmMouseRelateX=cmMouseRelateY=0;}
  void ccSetOrigin(){cmMouseRelateX=mouseX;cmMouseRelateY=mouseY;return;}
  void ccClicked(){
    if(mouseButton==RIGHT){ccFlip();}
    if(mouseButton==LEFT&&cmIsAxisOn){ccSetOrigin();}
  }
}//--VcMouseAxis

class VcKeyConsole{
  //---
  boolean cmIsOn;
  String cmInputLine;
  String cmLastLine;
  //---
  int cmMax;
  boolean cmDoEcho;
  boolean cmDoTell;
  //---
  IntDict cmCommandSets;
  //--
  VcKeyConsole(){
    cmIsOn=false;
    cmInputLine=">>..";
    cmLastLine="_";
    //--
    cmCommandSets = new IntDict();
    cmCommandSets.set("echo",35);
    cmCommandSets.set("tell",36);
    cmCommandSets.set("quit",99);
    cmCommandSets.set("help",98);
    //--
    cmDoEcho=false;
    cmDoTell=false;
    //--
  }
  //---
  void ccUpdate(){
    if(!cmIsOn){return;}
    fill(0xEE);rect(0,height-20,width,16);
    fill(0x33);text(cmInputLine,2,height-20);
    cmMax=32;
  }
  //--
  void ccFlipEcho(){cmDoEcho=!cmDoEcho;}
  void ccFlipTell(){cmDoTell=!cmDoTell;}
  //--
  int ccHandCommandID(String pxCommand){return cmCommandSets.get(pxCommand);}
  //---
  boolean ccTyped(){
    if(key==0x0A&&!cmIsOn){cmIsOn=true;return false;}
    if(cmIsOn){
      if((key>=',' && key<='z') && cmInputLine.length()<(4+cmMax)){cmInputLine+=str(key);return false;}
      if(cmInputLine.length()>4){if(key==0x08){cmInputLine=cmInputLine.substring(0,cmInputLine.length()-1);}}
    }
    if(key==0x0A&&cmIsOn){
      cmIsOn=false;
      if(cmInputLine.length()==4){return false;}
      cmLastLine=cmInputLine.substring(4,cmInputLine.length());
      cmInputLine=">>..";
      return true;
    }
  return false;}
  //---
  //---
}//--VcKeyConsole

class VcFunctionBox{
  //--
  boolean cmIsOn;
  int cmCommandIndex;
  String[] cmTitle;
  //--
  //--
  VcFunctionBox(){
    cmIsOn=false;
    cmCommandIndex=0;
    cmTitle=new String[4];
    cmTitle[0]="..QUIT";
    cmTitle[1]="..UNUSED 1";
    cmTitle[2]="..UNUSED 2";
    cmTitle[3]="..UNUSED 3";
  }
  //--
  void ccUpdate(){
    if(!cmIsOn){return;}
    fill(0xEE);stroke(0x77);rect(width/4,height/4,width/2,height/2);noStroke();
    fill(0x33);textAlign(CENTER,CENTER);text("CMD"+nf(cmCommandIndex,2)+cmTitle[cmCommandIndex],width/2,height/2);textAlign(LEFT, TOP);
  }
  //--
  void ccFlip(){cmIsOn=!cmIsOn;}
  //--
  int ccTyped(){
    if(keyCode==0x73){ccFlip();cmCommandIndex=0;return 0xFF;}
    if(keyCode==0x72){cmCommandIndex++;cmCommandIndex&=0x03;return 0xFF;}
    if(keyCode==0x71){cmCommandIndex--;cmCommandIndex&=0x03;return 0xFF;}
    if((keyCode==0x70 || keyCode==0x0A) && cmIsOn){cmIsOn=false;return cmCommandIndex;}
    return 0xFF;
  }
  }//--VcFunctionConsole
//-- --







//-- --- --- --- --- ---
//  cData
//-- --- --- --- --- ---
//--
class McByteMemory{
  byte[] cmMain;
  byte cmMask;
  int  cmSize;
  McByteMemory(int pxByteSize){
    cmSize=0;
    if(                       pxByteSize<  2){cmMask=(byte)0x01;cmSize=2;}
    else if(pxByteSize>= 2 && pxByteSize<  4){cmMask=(byte)0x03;cmSize=pxByteSize;}
    else if(pxByteSize>= 4 && pxByteSize<  8){cmMask=(byte)0x07;cmSize=pxByteSize;}
    else if(pxByteSize>= 8 && pxByteSize< 16){cmMask=(byte)0x0F;cmSize=pxByteSize;}
    else if(pxByteSize>=16 && pxByteSize< 32){cmMask=(byte)0x1F;cmSize=pxByteSize;}
    else if(pxByteSize>=32 && pxByteSize< 64){cmMask=(byte)0x3F;cmSize=pxByteSize;}
    else if(pxByteSize>=64 && pxByteSize<128){cmMask=(byte)0x7F;cmSize=pxByteSize;}
    else if(pxByteSize>=128                 ){cmMask=(byte)0xFF;cmSize=256;}
    cmMain=new byte[cmSize];
    for(int i=0;i<cmSize;i++){cmMain[i]=(byte)0;}
  }
  //---
  void ccWriteWord(int pxWordAddr, int pxVal){
    Integer lpUpper=(pxVal&0xFF00)>>8;
    Integer lpLower=(pxVal&0x00FF)   ;
    cmMain[(pxWordAddr&cmMask)*2+0]=lpUpper.byteValue();
    cmMain[(pxWordAddr&cmMask)*2+1]=lpLower.byteValue();    
  }
  void ccWriteBit(int pxWordAddr, int pxBit, boolean pxStatus){
    int lpArc=pxStatus? 0x01:0xFE;
    int lpAddr= (pxBit&0x0F)>7? (pxWordAddr&cmMask)*2:(pxWordAddr&cmMask)*2+1;
    int lpUpperAM=1;lpUpperAM<<=((pxBit&0xFF)-8);
    int lpLowerAM=1;lpLowerAM<<=((pxBit&0xFF)-0);
    //
    int lpAim=  (pxBit&0xFF)>7? (lpArc<<((pxBit&0xFF)-8))|(pxStatus? 0:(lpUpperAM-1)) : lpArc<<(pxBit&0xFF)|(pxStatus? 0:(lpLowerAM-1));
    if(pxStatus){cmMain[lpAddr]|=(byte)lpAim;}
    else{        cmMain[lpAddr]&=(byte)lpAim;}
    /* just in case::
       dont use this:int lpAim=  (pxBit&0xFF)>7? (lpArc<<(pxBit&0xFF-8)) | (pxStatus? 0:int((pow(2,pxBit&0xFF-8))-1)) : lpArc<<(pxBit&0xFF) | (pxStatus? 0:int((pow(2,pxBit&0xFF))-1));   
       dont use this;Float lpUpperAM=pow(2,(pxBit&0xFF)-8);
       dont use this;Float lpLowerAM=pow(2,(pxBit&0xFF)-0);
       dont use this;int lpAim=  (pxBit&0xFF)>7? (lpArc<<((pxBit&0xFF)-8))|(pxStatus? 0:(lpUpperAM.intValue()-1)) : lpArc<<(pxBit&0xFF)|(pxStatus? 0:(lpLowerAM.intValue()-1));
    */
    return;
  }
  //---
  int ccReadWord(int pxWordAddr){
    if(pxWordAddr>=ccTellWordSize()){return 0;}
    byte lpUpper=cmMain[((pxWordAddr&cmMask)<<1)+0];
    byte lpLower=cmMain[((pxWordAddr&cmMask)<<1)+1];
    int lpA=(int)lpUpper<<8&0x00FF00;
    int lpB=(int)lpLower   &0x0000FF;
    return lpA+lpB;
  }
  boolean ccReadBit(int pxWordAddr, int pxBit){
    int lpAddr= (pxBit&0xFF)>7? (pxWordAddr&0x1F)*2:(pxWordAddr&0x1F)*2+1;
    int lpAim= (pxBit&0xFF)>7? (0x01<<(pxBit&0xFF-8)) : (0x01<<(pxBit&0xFF));
    if((cmMain[lpAddr]&lpAim)==0){return false;}else{return true;}
  }
  //---
  int ccTellByteSize(){return cmMain.length;}
  int ccTellWordSize(){return cmMain.length/2;}
  int[] ccHandWordArray(int pxStartWordAddr, int pxWordLength){
    int lpMax=ccTellWordSize();
    if(pxWordLength>lpMax || pxWordLength<1 || pxStartWordAddr<0 || pxWordLength>lpMax){return null;}
    int lpLength=(pxStartWordAddr+pxWordLength)>(lpMax)? (lpMax-pxStartWordAddr):pxWordLength;
    int[] pyRes=new int[lpLength];
    for(int i=0;i<lpLength;i++){
      pyRes[i]=ccReadWord(pxStartWordAddr+i);
    }
    return pyRes;
  }
  //---
  void ccShiftWord(int pxWordAddr, int pxOffset){ccWriteWord(pxWordAddr&cmMask,(ccReadWord(pxWordAddr&cmMask)+pxOffset)&0x0000FFFF);}
  void ccFlipBit(int pxWordAddr, int pxBit){
    boolean lpCX=ccReadBit(pxWordAddr,pxBit);
    lpCX=!lpCX;ccWriteBit(pxWordAddr, pxBit, lpCX);
  }
  void ccAllClear(){for(int i=0;i<cmSize;i++){cmMain[i]=0;}}
  //---
  byte[] ccGetBytes(int pxWordIndex ,int pxByteLength){
    int lpLength= pxByteLength<=0?2:pxByteLength;
    int lpIndex = pxWordIndex*2;
    byte[] lpMemory=new byte[lpLength];
    for(int i=0;i<lpLength;i++){lpMemory[i]=0;}
    if(lpLength+lpIndex>cmSize){return lpMemory;}
    for(int i=0;i<lpLength;i++){lpMemory[i]=cmMain[i+lpIndex];}
    return lpMemory;
  }
  //---
  void ccSetBytes(int pxWordIndex,byte[] pxData){
    if(pxData==null){return;}
    int lpLength=pxData.length;
    int lpIndex =pxWordIndex*2;
    if(lpLength+lpIndex>cmSize){lpLength=cmSize-pxWordIndex;}
    for(int i=0;i<lpLength;i++){cmMain[i+lpIndex]=pxData[i];}
  }
}//--McByteMemory

class McCsvData{
  //--
  Table cmData;
  //--
  McCsvData(){
    cmData=new Table();
    cmData.addColumn("id");
    cmData.addColumn("name");
    cmData.addColumn("BatchKG");
    cmData.addColumn("DryS");
    cmData.addColumn("WetS");
    cmData.addColumn("AG6");
    cmData.addColumn("AG4");
    cmData.addColumn("AG3");
    cmData.addColumn("AG2");
    cmData.addColumn("AG1");
    cmData.addColumn("FR2");
    cmData.addColumn("FR1");
    cmData.addColumn("AS1");
    //--
    for(int j=0;j<100;j++){
      TableRow lpRow=cmData.addRow();
      lpRow.setString(0,str(j));
      lpRow.setString(1,"Test"+nf(j,3));
      lpRow.setString(2,"1600");
      for(int i=3;i<cmData.getColumnCount();i++){
        lpRow.setString(i,str((int)random(0,99)));
      }
    }
    //--
  }
  //--
  String ccHandCell(int pxRow, int pxColumn){return cmData.getString(constrain(pxRow,0,cmData.getRowCount()-1),constrain(pxColumn,0,cmData.getColumnCount()-1));}
  String[] ccHandColumn(int pxIndex){return cmData.getStringColumn(constrain(pxIndex,0,cmData.getColumnCount()));}
  String[] ccHandRow(int pxIndex){
    StringList lpList=new StringList();
    TableRow lpRow=cmData.getRow(constrain(pxIndex,0,cmData.getRowCount()));
    for(int i=0;i<cmData.getColumnCount();i++){lpList.append(lpRow.getString(i));}
    return lpList.array();
  }
  //--
  int ccHandColumnCount(){return cmData.getColumnCount();}
  int ccHandRowCount(){return cmData.getRowCount();}
  //--
  boolean ccLoadFromFile(String pxPath, String pxHeaderStand){
    String[] lpBuffer=loadStrings(pxPath);
    if(lpBuffer==null){return false;}
    if(lpBuffer.length<=1){return false;}
    if(!lpBuffer[0].equals(pxHeaderStand)){return false;}
    cmData=loadTable(pxPath,"header");
    return true;
  }
  //--
  void ccSaveToFile(String pxPath){
    saveTable(cmData,pxPath);
  }
  //--
  //--
}//+++

class McValueData{
  //--
  IntDict cmData;
  //--
  McValueData(){
    cmData=new IntDict();
    for(int i=0;i<4;i++){
      cmData.set("dummy"+nf(i,3),10+i*2);
    }
  }
  //--
  void ccShiftValue(int pxIndex, int pxOffset){
    String[] lpList=cmData.keyArray();
    cmData.add(lpList[constrain(pxIndex,0,lpList.length-1)],pxOffset);
  }
  void ccTakeValue(int pxIndex, int pxValue){
    String[] lpList=cmData.keyArray();
    cmData.set(lpList[constrain(pxIndex,0,lpList.length-1)],pxValue);
  }
  //--
  String[] ccHandKeyList(){
    return cmData.keyArray();
  }
  int ccHandValue(String pxKey){
    return cmData.get(pxKey);
  }
  //--
  boolean ccReadFromFile(String pxPath){
    boolean pyStatus=false;
    String[] lpList=loadStrings(pxPath);
    if(lpList==null){return pyStatus;}
    StringList lpCheckList=new StringList();
    for(String s:cmData.keyArray()){lpCheckList.append(s);}
    pyStatus=true;
    for(int i=0;i<lpList.length;i++){
      if(match(lpList[i],":")!=null){
        String lpKey=split(lpList[i],':')[0];
        String lpVal=split(lpList[i],':')[1];
        boolean lpIsKeyOK=true;//lpCheckList.hasValue(lpKey);
        boolean lpIsValueOK=(match(lpVal,"^[0-9]{4}$")!=null);
        if(lpIsKeyOK && lpIsValueOK){
          Integer lpValINT=new Integer(lpVal);
          cmData.set(lpKey,lpValINT);
        }else{pyStatus=false;}
      }else{pyStatus=false;}
    }
    //--
    return pyStatus;
  }
  //--
  void ccSaveToFile(String pxPath){
    String[] lpList=cmData.keyArray();
    for(int i=0;i<lpList.length;i++){
      lpList[i]+=":"+nf(cmData.get(lpList[i]),4);
    }
    saveStrings(pxPath,lpList);
  }
  //--
  //--
}//+++

class McKeycodeData{
  //--
  int[] cmData;
  //--
  McKeycodeData(){
    cmData=new int[100];
    for(int i=0;i<100;i++){cmData[i]=i;}
  }
  //--
  int ccTellKeyIndexFromKey(char pxKey){
    if(pxKey==' '){return 99;}
    if(pxKey>='0' && pxKey<='9'){return (int)pxKey-48;}
    if(pxKey>='a' && pxKey<='z'){return (int)pxKey-97+10;}
    return 0;
  }
  //--
  int ccTellPannelIDFromKey(char pxKey){return cmData[ccTellKeyIndexFromKey(pxKey)];}
  //--
  void ccChangeKeyPair(char pxKey, int pxPannelID){cmData[ccTellKeyIndexFromKey(pxKey)]=pxPannelID&0xFF;}
  //--
  void ccSaveToFile(String pxPath){println("not done yet");}
  void ccLoadFromFile(String pxPath){println("not done yet");}
  //--
  //--
}//++
//-- --
//++++++++++++++++++++++++++++++++



// <editor-fold defaultstate="collapsed" desc="ExtraBin">  

/* - -- we need clean these code when necessary

class XcSkipPosition{
  //--
  int cmX,cmY,cmWidth,cmHeight;
  //--
  int cmTurningPointLL,cmTurningPointHH;
  int cmPosX,cmPosY;
  //--
  XcSkipPosition(int pxWidth, int pxHeight,int pxPercentage){
    cmWidth=pxWidth;cmHeight=pxHeight;
    //--
    cmX=5;cmY=5;
    //--
    cmTurningPointHH=(pxPercentage*cmWidth)/100;
    cmTurningPointLL=cmTurningPointHH+cmHeight;
    cmPosX=cmX;cmPosY=cmY+cmHeight;
  }
  //--
  void ccShiftPosition(int pxOffset){
    cmPosX+=pxOffset;cmPosX=constrain(cmPosX,cmX,cmX+cmWidth);
    int lpRunHeight=0;
    if(cmPosX<cmTurningPointHH){lpRunHeight=0;}
    if(cmPosX>=cmTurningPointHH && cmPosX<cmTurningPointLL){lpRunHeight=cmPosX-cmTurningPointHH;}
    if(cmPosX>=cmTurningPointLL){lpRunHeight=cmHeight;}
    cmPosY=cmY+lpRunHeight;
  }
  //--
}//+++



class McMessageLib{
  String cmPop,cmTitle;
  boolean[] cmNumberBit;
  boolean[] cmNumberBitBuf;
  String[] cmList;
  //---
  McMessageLib(){    
    cmPop="...";cmTitle="[]";
    cmNumberBit=new boolean[8];cmNumberBitBuf=new boolean[8];
    for(int i=0;i<8;i++){cmNumberBit[i]=cmNumberBitBuf[i]=false;}
    cmList=new String[8];
    cmList[0]="ERR_0:";
    cmList[1]="ERR_01:wake up";
    cmList[2]="ERR_02:your house is on fire";
    cmList[3]="ERR_03:your cat is cought in your dryer";
    cmList[4]="ERR_04:your philosophy is a lier";
    cmList[5]="ERR_05:your bed is your headstone";
    cmList[6]="ERR_06:chances are lies of hope";
    cmList[7]="ERR_07:you could never be a hero";
  }
  //---
  boolean ccUpdate(boolean pxAct){if(!pxAct){return false;}
    int lpIndex=0xFF;
    for(int i=0;i<8;i++){
      if(cmNumberBit[i]!=cmNumberBitBuf[i]){lpIndex=i;break;}
    }
    if(lpIndex==0xFF){return false;}
    else{
      cmPop=cmList[lpIndex];
      cmTitle=cmNumberBit[lpIndex]?"[triggered]":"[elast]";
      for(int i=0;i<8;i++){cmNumberBitBuf[i]=cmNumberBit[i]?true:false;}
      return true;
    }
  }
  //---
  void ccApplyStatus(int pxIndex,boolean pxVal){
    for(int i=0;i<8;i++){cmNumberBitBuf[i]=cmNumberBit[i]?true:false;}
    cmNumberBit[pxIndex&0x07]=pxVal;
  }
  //
  boolean ccCheckSum(){
    boolean lpSum=false;
    for(int i=0;i<8;i++){lpSum|=cmNumberBit[i];}
    return lpSum;
  }
}
*/

// </editor-fold>  


  
}
