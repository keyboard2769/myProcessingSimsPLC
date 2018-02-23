/* *** *** *** *** *** *** */
/* **                   ** */
/*      Next ZZ Proto      */
/* **                   ** */
/* *** *** *** *** *** *** */
//- --- --- --- --- --- ---
// <editor-fold defaultstate="collapsed" desc="extraInfo">  
//---- originally by Processing v2.0 core on NetBeans IDE v8.2
//---- originally from my Fujitsu '13
// </editor-fold>  
//- --- --- --- --- --- ---


import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.net.*;
import java.io.*;
import java.net.*;

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.sound.midi.Patch;
import static processing.core.PApplet.constrain;
import static processing.core.PApplet.hex;
import static processing.core.PApplet.nfc;
import static processing.core.PApplet.pow;
import static processing.core.PApplet.println;
import static processing.core.PApplet.split;
import static processing.core.PApplet.str;
import static processing.core.PConstants.CENTER;
import static processing.core.PConstants.LEFT;
import static processing.core.PConstants.RIGHT;
import static processing.core.PConstants.TOP;

       
// <<Name Structure>>
// <editor-fold defaultstate="collapsed" desc="nameStructure">  
/*
 * --
 * pb:Public Variable or Object
 * lp:Local Variable or Object
 * cm:Class Field
 * cc:Class Instance Method
 * sp:Class Method just Draw shapes should no be used outside
 * --
 * st:Sub Routine of setup Function
 * dr:Sub Routine of draw Function
 * ms:Sub Routine of mousePressed or other Mouse Function
 * kb:Sub Routine of keyPressed or other Keyboard Function
 * fs:Support Function Involved in Other Routines
 * fn:Utility Function Without External Variable Involved
 * --
 * Le:System Primitive Variable
 * Les:System Data Object
 * --
 * The:System Element
 * This;Pannel Frame
 * That:Pannel Element
 * Those:Pannel Element Array
 * --
 * My:
 * Your:
 * Her:
 * His:
 * Our:
 * Their:
 * --
 * Thu:
 * Tha:
 * --
 * Der:
 * Das:
 * --
 * SW:PushButton or LatchButton
 * PL:Lamp
 * LV:Gauge or Container
 * AD:InputButton or ValueContainer
 * MB:Motor Block
 * :
 * :
 * :
*/
// </editor-fold>  


public class Sketch extends Extab {

       
/* ** ** ** ** ** ** ** */
/* ** DONT TOUCH THIS** */
/* ** ** ** ** ** ** ** */
// <editor-fold defaultstate="collapsed" desc="dontTouchThis">  
@Override
public void setup() {size(1024, 768);noStroke();frameRate(16);textAlign(LEFT, TOP);ellipseMode(CENTER);gateSetup();
  frame.setTitle("Next ZZ-demoAA");}
@Override
public void draw() {background(0);gateDraw();}
@Override
public void mousePressed() {gateMousePressed();}
@Override
public void mouseDragged(){gateMouseDragged();}
@Override
public void keyPressed() {gateKeyPressed();}
// </editor-fold>  
/* ** ** ** ** ** ** ** */
/* ** DONT TOUCH THIS** */
/* ** ** ** ** ** ** ** */





/* ** ** ** ** ** ** ** */
/* **                ** */
/* **      GATE      ** */
/* **                ** */
/* ** ** ** ** ** ** ** */
//--
McByteMemory pbLesLinkMemory    = new McByteMemory(300);
McByteMemory pbLesSettingMemory = new McByteMemory(300);
McByteMemory pbLesMonitorMemory = new McByteMemory(300);
//--
McKeycodeData pbLesHotkeyData   = new McKeycodeData();
//--
McValueData pbLesPannelPosData = new McValueData();
//
int pbLeRoller=0;
boolean pbLeFlicker=false;
int pbLeDoubleClicker=0;
int[] pbLeOringinPoint;
//--
String pbLeDatPath=true? "C:\\Next_ZZ\\dat\\" : "/Users/keypad/Documents/Next_ZZ/";
//--
EcMultiButton pbTheQuitButton  = new EcMultiButton(49,  16, "quit", 'p');
VcMessageBar pbTheBar          = new VcMessageBar(150, 0, 564, 64);
VcMouseAxis pbTheAxis          = new VcMouseAxis(false);
VcKeyConsole pbTheConsole      = new VcKeyConsole();
VcFunctionBox pbTheCommandBox  = new VcFunctionBox();
//--
void gateSetup(){
  //if(fnLockerTest("/*pathhere*/")){exit();}
  //--
  
  if(!pbLesPannelPosData.ccReadFromFile(pbLeDatPath+"nzzPannelPos.txt")){pbTheBar.ccStack("failed to read position data");}
  //--
  stPannelGate();
  stThisMenuSet();
  //--
  //--
  pbLeOringinPoint=new int[2];pbLeOringinPoint[0]=0;pbLeOringinPoint[1]=0;
  pbTheBar.cmWidth=width-pbTheBar.cmX-10;
  //--
  //*** Link ***
  plcLinkSetup(false);
  pbTheBar.ccStack(plcLinkAvailableAtStart? "[Sys] PLC Link connected.":"[Sys] Can not connect to PLC.");
  //--
}//++
//--
void gateDraw(){pbLeRoller++;pbLeRoller&=0x0F;if(pbLeRoller==0){pbLeFlicker=!pbLeFlicker;}
  //--
  int lpMilliChekerA=millis();
  //--
  if(pbLeDoubleClicker>0){pbLeDoubleClicker--;}
  //--
  fill(0xEE);text(fnTimeStamp('c'),5,0);if(pbLeFlicker){text(":",22,0);}


  //*** box ***
  boxDrawMemoryBox(false);
  
  //*** Pannels ***
  drPannelGate(true);
    
  //*** Link ***
  if(plcLinkDraw(false,pbLeRoller)){
    //pbLinkRecievePB.ccFlip();
    int lpAddr=(plcLinkRecievedADDR<0? 65536:0)+plcLinkRecievedADDR;
    if(lpAddr==plcLinkReadADDR){pbLesLinkMemory.ccSetBytes(4,plcLinkRecievedWM);}
    switch(lpAddr){
      case 30064:break;
      default:break;
    }
    plcLinkRecievedADDR=0;
  }
  //*** Zelda ***
  pbTheSimulator.ccRefresh(pbLeRoller);
  zeldaDraw(true);
  
  //*** SYS ***
  drThisMenuSet();
  pbTheQuitButton.ccUpdate(pbLeOringinPoint,100,0);
  //--
  pbTheBar.ccUpdate();
  pbTheAxis.ccUpdate();
  pbTheConsole.ccUpdate();
  pbTheCommandBox.ccUpdate();
  //--
  //--
  //--
  int lpMilliChekerB=millis();
  if(pbTheCommandBox.cmIsOn){
    String lpSystemInfo="<INFO>\n";
    lpSystemInfo+="pbLeRoller: "+nf(pbLeRoller,4);lpSystemInfo+="_\n";
    lpSystemInfo+="pbLeMenuFocus: "+nf(pbLeMenuFocus,4);lpSystemInfo+="_\n";
    lpSystemInfo+="pbLePannelFocus: "+nf(pbLePannelFocus,4);lpSystemInfo+="_\n";
    lpSystemInfo+="pbLeInputFocus: "+nf(pbLeInputFocus,4);lpSystemInfo+="_\n";
    //--
    lpSystemInfo+="__ __ __\n"; lpMilliChekerB=millis();
    lpSystemInfo+="Millils: "+nf(lpMilliChekerB-lpMilliChekerA,4);lpSystemInfo+="_\n"; 
    fill(0xFF,0x77);text(lpSystemInfo,6,32);
  }
  //--
  fill(0xFF);text(nf(lpMilliChekerB-lpMilliChekerA,2)+"ms",0,17);
  //--
}//++
//--
void gateMousePressed(){
  //--
  if(mouseButton==RIGHT){
    if(!msRightPannelGate()){pbLePannelFocus=0;}
  }
  //--
  if(mouseButton==LEFT){if(pbLeDoubleClicker>0){msDoublePannelGate();}pbLeDoubleClicker=3;
      //--
      if(pbTheQuitButton.ccIsMouseOver()){fsPover();}
      if(msThisMenuSet()){return;}
      //--
      pbLeInputFocus=0;
      //*** Pannels ***
      if(msLeftPannelGate()){return;}
      pbLeMenuFocus=0;
  }
  //--
}//+++
//--
void gateKeyPressed(){//println("0x"+hex(keyCode,4));
  boolean lpDoReturn=true;
  //--
  if(pbTheCommandBox.ccTyped()==0){fsPover();}if(pbTheCommandBox.cmIsOn){return;}
  if(pbTheConsole.ccTyped()){commandInputOperate(pbTheConsole.cmLastLine);}if(pbTheConsole.cmIsOn){return;}
  //--
  switch(keyCode){
    case 0x74:pbTheAxis.ccFlip();break;
    case 0x75:pbTheAxis.ccSetOrigin();break;
    //--
    case 0x7A:boxDoShowUp=!boxDoShowUp;break;
    case 0x7B:boxIndex++;boxIndex&=0x03;break;
    //--
    case 0x25:case 0x41:pbTheBar.cmIndex=0;break;//LT
    case 0x26:case 0x57:pbTheBar.ccShiftIndex(-1);break;//UP
    case 0x27:case 0x44:pbTheBar.ccFlip(2);break;//RT
    case 0x28:case 0x53:pbTheBar.ccShiftIndex( 1);break;//DN
    //--
    default:lpDoReturn=false;break;
  }
  //--
  if(lpDoReturn){return;}lpDoReturn=true;
  switch(key){
    case 'f':println("testMove"+nf(pbLesLinkMemory.ccTellWordSize(),4));break;
    case 'q':fsPover();break;
    //--
    default:lpDoReturn=false;break;
  }
  if(lpDoReturn){return;}
  //--
  pbLePannelFlag[pbLesHotkeyData.ccTellPannelIDFromKey(key)]=!pbLePannelFlag[pbLesHotkeyData.ccTellPannelIDFromKey(key)];
  //--  
}//+++
//++++ +++ +++ +++









/* ** ** ** ** ** ** ** */
/* **                ** */
/* **    Operate     ** */
/* **                ** */
/* ** ** ** ** ** ** ** */
//-- --- --- --- --- ---
//  Console Command
//-- --- --- --- --- ---
//--
void commandInputOperate(String pxInput){
  //--
  int lpMode=0;
  //--
  int lpVal=-1;
  String lpCommand="_";
  String lpParameter="_";
  //--
  if(match(pxInput,"^[0-9]{1,8}[\\.]{0,1}[0-9]{0,1}$")==null){
    //--
    String[] lpInputedCommandString=split(pxInput,':');
    if(lpInputedCommandString.length>1){
      lpCommand=lpInputedCommandString[0];
      lpParameter=lpInputedCommandString[1];
      if(match(lpParameter,"^[0-9]{1,4}$")==null){lpMode=23;}else{lpMode=22;}
    }else{lpCommand=lpInputedCommandString[0];lpMode=21;}
    //--
  }
  //--
  else{
    String[] lpInputedNumberString=split(pxInput, '.');
    if(lpInputedNumberString.length>1){
      Integer lpAX=new Integer(lpInputedNumberString[0]);
      Integer lpBX=new Integer(lpInputedNumberString[1]);
      lpVal=lpAX*10+lpBX;lpMode=12;
    }else{
      Integer lpCX=new Integer(lpInputedNumberString[0]);
      lpVal=lpCX;lpMode=11;
    }
    //--
  }
  //--
  //--
  if(pbTheConsole.cmDoEcho){pbTheBar.ccStack("[echo]"+pxInput);}
  if(pbTheConsole.cmDoTell){pbTheBar.ccStack("[tell]"+nf(lpMode,2)+">> "+nf(lpVal,4)+" | "+lpCommand+" | "+lpParameter);}
  //--
  //--
  switch(pbLeInputFocus){
    //--
    case 0:
      //CommandOperatesHere
      switch(lpMode){
        case 21:commandRunner(lpCommand);break;
        case 22:break;//we need to deal with this later
        case 23:break;//we need to deal with this later
        default:break;
      }
      break;
    //--
    //--
    case 1:
      //Input ID operates here
      break;
    //--
    //--
    case 2:
      //Input ID operates here
      break;
    //--
    //--
    default:break;
    //--
  }
  //--
  pbLeInputFocus=0;
  //--
}//++
//--
void commandRunner(String pxCommand){switch(pbTheConsole.ccHandCommandID(pxCommand)){
  case 35:pbTheConsole.ccFlipEcho();break;
  case 36:pbTheConsole.ccFlipTell();break;
  case 98:pbTheBar.ccStack("[Sys] there is no help available.");break;
  case 99:fsPover();break;
  default:pbTheBar.ccStack("[Sys] command illeagle.");break;
}}//++
//-- --



void fsPover(){
  pannelSavePosition();
  plcCloseClient();
  exit();
}//++
//-- --


//++ +++ +++ +++ ++++ +++



/* ** ** ** ** ** ** ** */
/* **                ** */
/* **       Box      ** */
/* **                ** */
/* ** ** ** ** ** ** ** */
int boxIndex=1;
boolean boxDoShowUp=false;
void boxDrawMemoryBox(boolean pxAct){if(pxAct){return;}
  if(boxDoShowUp){
    fill(0xEE);rect(5,30,width-5,400);
    fill(0x33);text(nf(boxIndex,2),5,30);
    switch(boxIndex){
      case 0:text(fnByteToLines("Command",plcLinkCommand,6, false,"[M+",0),5,50);break;
      case 1:text(fnByteToLines("LinkedMemory",pbLesLinkMemory.cmMain,6, false,"[M+",0),5,50);break;
      case 2:text(fnByteToLines("SetMemory",pbLesSettingMemory.cmMain,6, false,"[L+",500),5,50);break;
      case 3:text(fnByteToLines("MonitorMemory",pbLesMonitorMemory.cmMain,6, false,"[?<",0),5,50);break;
      default:break;
    }
    if(true){text("Start",20,400);}
    if(true){text("Active",80,400);}
    if(true){text("RECV",140,400);}
  }
}
//++ +++ +++ +++ ++++ +++







/* ** ** ** ** ** ** ** */
/* **                ** */
/* **      Menu      ** */
/* **                ** */
/* ** ** ** ** ** ** ** */
int pbLeMenuFocus=0;
boolean[] pbLePannelFlag;
//-- --- --- --- --- ---
//  Pannel Menu Set
//-- --- --- --- --- ---
EcMultiButton pbThisPannelMenu = new EcMultiButton(49, 16, "Pannel", 'm');
//--
EcMultiButton pbThatControllMenuItem = new EcMultiButton(79, 16, "Control", 'm');
EcMultiButton pbThatModelMenuItem    = new EcMultiButton(79, 16, "Model", 'm');
EcMultiButton pbThatViewMenuItem         = new EcMultiButton(79, 16, "View", 'm');
EcMultiButton pbThatSettingMenuItem         = new EcMultiButton(79, 16, "Setting", 'm');
//--
EcMultiButton[] pbThoseMenuItem;
//--
void stThisMenuSet(){
  pbLePannelFlag = new boolean[256];for(int i=0;i<256;i++){pbLePannelFlag[i]=false;}
  pbThoseMenuItem = new EcMultiButton[255];for(int i=0;i<255;i++){pbThoseMenuItem[i]=new EcMultiButton(79,16,"nc"+nf(i,2),'m');}
  //--
  
  pbLePannelFlag[ 1]=true;pbThoseMenuItem[ 1].cmLable="MixerCP";
  pbLePannelFlag[ 2]=true;pbThoseMenuItem[ 2].cmLable="WeightCP";
  pbLePannelFlag[ 3]=true;pbThoseMenuItem[ 3].cmLable="Ag-W-CP";
  pbLePannelFlag[41]=true;pbThoseMenuItem[41].cmLable="MixerVP";
  
}//++
//--
void drThisMenuSet(){
  int lpX=50;
  int lpY=16;
  if(pbThisPannelMenu.ccIsMouseOver()){pbLeMenuFocus=10;}
  pbThisPannelMenu.ccUpdate(pbLeOringinPoint,lpX,0);pbThisPannelMenu.cmAct = (pbLeMenuFocus>01);
  if(pbThisPannelMenu.cmAct){
    //--
    if(pbThatControllMenuItem.ccIsMouseOver()){pbLeMenuFocus=11;}
    if(pbThatModelMenuItem.ccIsMouseOver()){pbLeMenuFocus=12;}
    if(pbThatViewMenuItem.ccIsMouseOver()){pbLeMenuFocus=13;}
    if(pbThatSettingMenuItem.ccIsMouseOver()){pbLeMenuFocus=14;}
    pbThatControllMenuItem.ccUpdate(pbLeOringinPoint,lpX,lpY*1);pbThatControllMenuItem.cmAct=(pbLeMenuFocus==11);
    pbThatModelMenuItem.ccUpdate(   pbLeOringinPoint,lpX,lpY*2);pbThatModelMenuItem.cmAct   =(pbLeMenuFocus==12);
    pbThatViewMenuItem.ccUpdate(    pbLeOringinPoint,lpX,lpY*3);pbThatViewMenuItem.cmAct    =(pbLeMenuFocus==13);
    pbThatSettingMenuItem.ccUpdate( pbLeOringinPoint,lpX,lpY*4);pbThatSettingMenuItem.cmAct    =(pbLeMenuFocus==14);
    //--
    if(pbLeMenuFocus==11){for(int i=0;i<39;i++){
      pbThoseMenuItem[i+  1].ccUpdate(        pbLeOringinPoint,129+i/7*79,16+i%7*16);pbThoseMenuItem[i+ 1].cmAct=pbLePannelFlag[i+  1];
    }}
    if(pbLeMenuFocus==12){for(int i=0;i<39;i++){
      pbThoseMenuItem[i+ 41].ccUpdate(        pbLeOringinPoint,129+i/7*79,32+i%7*16);pbThoseMenuItem[i+ 41].cmAct=pbLePannelFlag[i+ 41];
    }}
    if(pbLeMenuFocus==13){for(int i=0;i<39;i++){
      pbThoseMenuItem[i+ 81].ccUpdate(        pbLeOringinPoint,129+i/7*79,48+i%7*16);pbThoseMenuItem[i+ 81].cmAct=pbLePannelFlag[i+ 81];
    }}
    if(pbLeMenuFocus==14){for(int i=0;i<39;i++){
      pbThoseMenuItem[i+121].ccUpdate(        pbLeOringinPoint,129+i/7*79,64+i%7*16);pbThoseMenuItem[i+121].cmAct=pbLePannelFlag[i+121];
    }}
    //--
  }
  //--
}//++
//--
boolean msThisMenuSet(){
  if(pbLeMenuFocus==0){return false;}
  //--
  if(pbLeMenuFocus==11){for(int i=0;i<39;i++){
    if(pbThoseMenuItem[i+ 1].ccIsMouseOver()){fsFlipFlag(i+ 1);return true;}
  }}
  //--
  if(pbLeMenuFocus==12){for(int i=0;i<39;i++){
    if(pbThoseMenuItem[i+41].ccIsMouseOver()){fsFlipFlag(i+41);return true;}
  }}
  //--
  if(pbLeMenuFocus==13){for(int i=0;i<39;i++){
    if(pbThoseMenuItem[i+81].ccIsMouseOver()){fsFlipFlag(i+81);return true;}
  }}
  if(pbLeMenuFocus==14){for(int i=0;i<39;i++){
    if(pbThoseMenuItem[i+121].ccIsMouseOver()){fsFlipFlag(i+121);return true;}
  }}
  //--
  return false;
}void fsFlipFlag(int pxIndex){pbLePannelFlag[pxIndex]=!pbLePannelFlag[pxIndex];}//++
//-- --
//++ +++ +++ +++ +++








/* ** ** ** ** ** ** ** */
/* **                ** */
/* **     Pannel     ** */
/* **                ** */
/* ** ** ** ** ** ** ** */
int pbLePannelFocus=0;
int pbLeInputFocus=0;
//** *** *** *** *** ***
//  GATE ROUTE
//** *** *** *** *** ***
void stPannelGate(){
  //--
  stThisMixerControlPannel();
  stThisWeighOperateCP();
  stThisAgWeighCP();

  //--
  //--
  stThisControlPannel();
  stThisModelPannel();
  stThisViewPannel();
}//++
//--
void drPannelGate(boolean pxAct){if(!pxAct){return;}
  //--
  //-- 
  drThisMixerControlPannel(pbLePannelFlag[pbThisMixerControlPannel.cmFocusID]);
  drThisWeighOperateCP(pbLePannelFlag[pbThisWeighOperateCP.cmFocusID]);
  drThisAgWeighCP(pbLePannelFlag[pbThisAgWeighControlPannel.cmFocusID]);

  //--
  drThisViewPannel(pbLePannelFlag[pbThisViewPannel.cmFocusID]);
  drThisModelPannel(pbLePannelFlag[pbThisModelPannel.cmFocusID]);
  drThisControlPannel(pbLePannelFlag[pbThisControlPannel.cmFocusID]);
}//++
//--
boolean msRightPannelGate(){
  //--
  //--
  if(pbLePannelFlag[pbThisMixerControlPannel.cmFocusID]&&pbThisMixerControlPannel.ccIsMouseOver()){fsChangePannelFocus(pbThisMixerControlPannel.cmFocusID);return true;}
  if(pbLePannelFlag[pbThisWeighOperateCP.cmFocusID]&&pbThisWeighOperateCP.ccIsMouseOver()){fsChangePannelFocus(pbThisWeighOperateCP.cmFocusID);return true;}
  if(pbLePannelFlag[pbThisAgWeighControlPannel.cmFocusID]&&pbThisAgWeighControlPannel.ccIsMouseOver()){fsChangePannelFocus(pbThisAgWeighControlPannel.cmFocusID);return true;}

  //--
  //--
  //--
  //--
  if(pbLePannelFlag[pbThisControlPannel.cmFocusID]&&pbThisControlPannel.ccIsMouseOver()){fsChangePannelFocus(pbThisControlPannel.cmFocusID);return true;}
  if(pbLePannelFlag[pbThisModelPannel.cmFocusID]&&pbThisModelPannel.ccIsMouseOver()){fsChangePannelFocus(pbThisModelPannel.cmFocusID);return true;}
  if(pbLePannelFlag[pbThisViewPannel.cmFocusID]&&pbThisViewPannel.ccIsMouseOver()){fsChangePannelFocus(pbThisViewPannel.cmFocusID);return true;}
  return false;
}void fsChangePannelFocus(int pxIndex){pbLePannelFocus= (pbLePannelFocus==pxIndex)? 0:pxIndex;}//++
//--
void gateMouseDragged(){
  //--
  //--
  //--
  if(pbThisMixerControlPannel.ccGotDragged(pbLePannelFocus)){return;}
  if(pbThisWeighOperateCP.ccGotDragged(pbLePannelFocus)){return;}
  if(pbThisAgWeighControlPannel.ccGotDragged(pbLePannelFocus)){return;}

  //--
  //--
  //--
  //--
  //--
  if(pbThisControlPannel.ccGotDragged(pbLePannelFocus)){return;}
  if(pbThisModelPannel.ccGotDragged(pbLePannelFocus)){return;}
  if(pbThisViewPannel.ccGotDragged(pbLePannelFocus)){return;}
}//++
//--
boolean msLeftPannelGate(){
  //--
  //--
  //--
  if(msThisMixerControlPannel(pbLePannelFlag[pbThisMixerControlPannel.cmFocusID])){return true;}
  if(msThisWeighOperateCP(pbLePannelFlag[pbThisWeighOperateCP.cmFocusID])){return true;}
  if(msThisAgWeighCP(pbLePannelFlag[pbThisAgWeighControlPannel.cmFocusID])){return true;}

  //-- 
  //--
  //--
  if(msThisControlPannel(pbLePannelFlag[pbThisControlPannel.cmFocusID])){return true;}
  return false;
}//++
//--
void msDoublePannelGate(){
  pbTheBar.ccStack("[Sys] still no function for double click.");
  if(pbThatAddrSelectStepper.ccIsMouseOver()==1){pbThatMemoryAddrContainer.ccShiftValue(-8);return;}
  if(pbThatAddrSelectStepper.ccIsMouseOver()==2){pbThatMemoryAddrContainer.ccShiftValue( 8);return;}
}//++
void pannelSavePosition(){
  //--
  //--
  //--
  //--
  //--
  //--
  //--
  //--
          
  pbLesPannelPosData.cmData.set("pbThisAgWeighControlPannel.cmX", pbThisAgWeighControlPannel.cmX);
  pbLesPannelPosData.cmData.set("pbThisAgWeighControlPannel.cmY", pbThisAgWeighControlPannel.cmY);
  //--
  //--
  pbLesPannelPosData.cmData.set("pbThisWeighOperateCP.cmX", pbThisWeighOperateCP.cmX);
  pbLesPannelPosData.cmData.set("pbThisWeighOperateCP.cmY", pbThisWeighOperateCP.cmY);
  //--
  pbLesPannelPosData.cmData.set("pbThisMixerControlPannel.cmX", pbThisMixerControlPannel.cmX);
  pbLesPannelPosData.cmData.set("pbThisMixerControlPannel.cmY", pbThisMixerControlPannel.cmY);
  //--
  pbLesPannelPosData.ccSaveToFile(pbLeDatPath+"nzzPannelPos.txt");
}
//** **





//-- New Pannel Template
// <editor-fold defaultstate="collapsed" desc="P000:NewPannel">
/* [setup]   stThisNewPannel();
 * [draw]   drThisNewPannel(pbLePannelFlag[pbThisNewPannel.cmFocusID]);
 * [mouseRightClick]   if(pbLePannelFlag[pbThisNewPannel.cmFocusID]&&pbThisNewPannel.ccIsMouseOver()){fsChangePannelFocus(pbThisNewPannel.cmFocusID);return true;}
 * [mouseDoubleClick]  //nothing here still
 * [mouseDrag]    if(pbThisNewPannel.ccGotDragged(pbLePannelFocus)){return;}
 * [mouseLeft]   if(msThisNewPannel(pbLePannelFlag[pbThisNewPannel.cmFocusID])){return true;}
 * []
 * []
//-- --- --- --- --- ---
//  <New Pannel>--P000
//-- --- --- --- --- ---
EcLabledFrame pbThisNewPannel = new EcLabledFrame(99, 99, 99, 99, "nu",'p',0 );
//--
EcMultiLamp pbThatNewElement = new EcMultiLamp(16, 's');//<--delete this
//--
void stThisNewPannel(){;}
void drThisNewPannel(boolean pxAct){if(!pxAct){return;}
  pbThisNewPannel.ccUpdate(pbLePannelFocus);
  //--
  pbThatNewElement.ccUpdate(pbThisNewPannel.ccGetPos(), 5, 20);
}
boolean msThisNewPannel(boolean pxAct){if(!pxAct){return false;}
  //--
  return false;
}//++
*/
// </editor-fold>  






//-- --- --- --- --- ---
//  <Mixer Control Pannel>--P001
//-- --- --- --- --- ---
EcLabledFrame pbThisMixerControlPannel = new EcLabledFrame(120, 120, 70, 90, "Mixer",'p',1 );
// <editor-fold defaultstate="collapsed" desc="P001:pbThisMixerControlPannel">
//--
/*
 * [setup]   stThisMixerControlPannel();
 * [draw]   drThisMixerControlPannel(pbLePannelFlag[pbThisMixerControlPannel.cmFocusID]);
 * [mouseRightClick]   if(pbLePannelFlag[pbThisMixerControlPannel.cmFocusID]&&pbThisMixerControlPannel.ccIsMouseOver()){fsChangePannelFocus(pbThisMixerControlPannel.cmFocusID);return true;}
 * [mouseDoubleClick]  //nothing here still
 * [mouseDrag]    if(pbThisMixerControlPannel.ccGotDragged(pbLePannelFocus)){return;}
 * [mouseLeft]   if(msThisMixerControlPannel(pbLePannelFlag[pbThisMixerControlPannel.cmFocusID])){return true;}
*/
//--
EcPushButtonTre pbThatMixerOperateSW = new EcPushButtonTre(60, "DISAB", "AUTO", "ENAB", true);
//--
void stThisMixerControlPannel(){
  pbThisMixerControlPannel.cmX=pbLesPannelPosData.cmData.get("pbThisMixerControlPannel.cmX");
  pbThisMixerControlPannel.cmY=pbLesPannelPosData.cmData.get("pbThisMixerControlPannel.cmY");
}
void drThisMixerControlPannel(boolean pxAct){if(!pxAct){return;}
  pbThisMixerControlPannel.ccUpdate(pbLePannelFocus);
  //--
  pbThatMixerOperateSW.ccUpdate(pbThisMixerControlPannel.ccGetPos(), 5, 20);
}
boolean msThisMixerControlPannel(boolean pxAct){if(!pxAct){return false;}
  //--
  if(pbThatMixerOperateSW.ccIsMouseOver()==1){pbThatMixerOperateSW.cmStatus=1;return true;}
  if(pbThatMixerOperateSW.ccIsMouseOver()==2){pbThatMixerOperateSW.cmStatus=2;return true;}
  if(pbThatMixerOperateSW.ccIsMouseOver()==3){pbThatMixerOperateSW.cmStatus=3;return true;}
  return false;
}//++
// </editor-fold>  


//-- --- --- --- --- ---
//  <Weigh Operate Control Pannel>--P002
//-- --- --- --- --- ---
EcLabledFrame pbThisWeighOperateCP = new EcLabledFrame(400, 99, 90, 150, "WeighOPT",'p',2 );
// <editor-fold defaultstate="collapsed" desc="P002:pbThisWeighOperateCP">
/* : newPannelTemplate
 * [setup]   stThisWeighOperateCP();
 * [draw]   drThisWeighOperateCP(pbLePannelFlag[pbThisWeighOperateCP.cmFocusID]);
 * [mouseRightClick]   if(pbLePannelFlag[pbThisWeighOperateCP.cmFocusID]&&pbThisWeighOperateCP.ccIsMouseOver()){fsChangePannelFocus(pbThisWeighOperateCP.cmFocusID);return true;}
 * [mouseDoubleClick]  //nothing here still
 * [mouseDrag]    if(pbThisWeighOperateCP.ccGotDragged(pbLePannelFocus)){return;}
 * mouseLeft]   if(msThisWeighOperateCP(pbLePannelFlag[pbThisWeighOperateCP.cmFocusID])){return true;}
 * []
 * []
*/
//--
EcMultiLamp pbThatWeighIsReadyPL = new EcMultiLamp(40, 't');
EcMultiLamp pbThatWeighIsOpratingPL = new EcMultiLamp(40, 't');
EcMultiButton pbThatWeighStartSW = new EcMultiButton(36, 36, "start", 'p');
//--
EcPushButtonTre pbThatWeighModeSW = new EcPushButtonTre(80, "DISAB", "AUTO", "MANNUAL", true);
//--
void stThisWeighOperateCP(){
  //--
  pbThisWeighOperateCP.cmX=pbLesPannelPosData.cmData.get("pbThisWeighOperateCP.cmX");
  pbThisWeighOperateCP.cmY=pbLesPannelPosData.cmData.get("pbThisWeighOperateCP.cmY");
  //--
  pbThatWeighIsReadyPL.cmLable="ready";
  pbThatWeighIsOpratingPL.cmLable="oprate";
}
void drThisWeighOperateCP(boolean pxAct){if(!pxAct){return;}
  pbThisWeighOperateCP.ccUpdate(pbLePannelFocus);
  //--
  pbThatWeighIsReadyPL.ccUpdate(pbThisWeighOperateCP.ccGetPos(), 5, 20);
  pbThatWeighIsOpratingPL.ccUpdate(pbThisWeighOperateCP.ccGetPos(), 5, 40);
  pbThatWeighStartSW.ccUpdate(pbThisWeighOperateCP.ccGetPos(), 50, 20);
  pbThatWeighModeSW.ccUpdate(pbThisWeighOperateCP.ccGetPos(), 5, 65);
}
boolean msThisWeighOperateCP(boolean pxAct){if(!pxAct){return false;}
  //--
  return false;
}//++
// </editor-fold>  


//-- --- --- --- --- ---
//  <AG Weigh Control Pannel>--P003
//-- --- --- --- --- ---
EcLabledFrame pbThisAgWeighControlPannel = new EcLabledFrame(99, 99, 225, 125, "AG-Weigh",'p',3 );
// <editor-fold defaultstate="collapsed" desc="P003:pbThisAgWeighCP">
/* [setup]   stThisAgWeighCP();
 * [draw]   drThisAgWeighCP(pbLePannelFlag[pbThisAgWeighCP.cmFocusID]);
 * [mouseRightClick]   if(pbLePannelFlag[pbThisAgWeighCP.cmFocusID]&&pbThisAgWeighCP.ccIsMouseOver()){fsChangePannelFocus(pbThisAgWeighCP.cmFocusID);return true;}
 * [mouseDoubleClick]  //nothing here still
 * [mouseDrag]    if(pbThisAgWeighCP.ccGotDragged(pbLePannelFocus)){return;}
 * mouseLeft]   if(msThisAgWeighCP(pbLePannelFlag[pbThisAgWeighCP.cmFocusID])){return true;}
 * []
 * []
*/
//--
EcPushButtonDeo pbThatAgSkipAndResetSW = new EcPushButtonDeo(17, "$", "z");
//--
EcMultiButton[] pbThoseAgWeighSW=new EcMultiButton[8];
EcMultiButton[] pbThoseAgWeighLockSW=new EcMultiButton[8];
//--
void stThisAgWeighCP(){
  //--
  pbThisAgWeighControlPannel.cmX=pbLesPannelPosData.cmData.get("pbThisAgWeighControlPannel.cmX");
  pbThisAgWeighControlPannel.cmY=pbLesPannelPosData.cmData.get("pbThisAgWeighControlPannel.cmY");  
  //--
  pbThoseAgWeighSW[0]=new EcMultiButton(140, 20, "discharge", 'p');
  pbThoseAgWeighLockSW[0]=new EcMultiButton(34, 18, "X", 'l');
  for(int i=1;i<8;i++){
    pbThoseAgWeighSW[i]=new EcMultiButton(34, 34, "AG"+nf(i,1), 'p');
    pbThoseAgWeighLockSW[i]=new EcMultiButton(34, 18, "x", 'l');
  }
}
void drThisAgWeighCP(boolean pxAct){if(!pxAct){return;}
  pbThisAgWeighControlPannel.ccUpdate(pbLePannelFocus);
  //--
  pbThoseAgWeighLockSW[0].ccUpdate(pbThisAgWeighControlPannel.ccGetPos(), 5,  20);
  pbThatAgSkipAndResetSW.ccUpdate(pbThisAgWeighControlPannel.ccGetPos(), 180,  20);
  //--
  for(int i=1;i<7;i++){
    pbThoseAgWeighLockSW[i].ccUpdate(pbThisAgWeighControlPannel.ccGetPos(), 215-i*35, 42);
    pbThoseAgWeighSW[i].ccUpdate(pbThisAgWeighControlPannel.ccGetPos(), 215-i*35, 60);
  }
  pbThoseAgWeighSW[0].ccUpdate(pbThisAgWeighControlPannel.ccGetPos(), 40, 95);
}
boolean msThisAgWeighCP(boolean pxAct){if(!pxAct){return false;}
  //--
  for(int i=0;i<7;i++){
    if(pbThoseAgWeighLockSW[i].ccIsMouseOver()){pbThoseAgWeighLockSW[i].ccFlip();return true;}
  }
  if(pbThoseAgWeighSW[0].ccIsMouseOver()){pbThoseAgWeighSW[0].ccFlip();return true;}
  return false;
}//++
// </editor-fold>  







//-- --- --- --- --- ---
//  Model Pannel: as mixer model pannel <--P:041
//-- --- --- --- --- ---
EcLabledFrame pbThisModelPannel= new EcLabledFrame(245, 355, 100, 100, "Mixer",'m',41 );
// <editor-fold defaultstate="collapsed" desc="P041:pbThisModelPannel">
//--
EcMultiLamp pbThatMixerMixturePL = new EcMultiLamp(80, 'b');
EcMultiLamp pbThatMixerGatePL = new EcMultiLamp(16,'d');
//--
EcTitleContainer pbThatMixerWetDryPL;
EcValueContainer pbThatMixerTimerTB = new EcValueContainer(40, 18, "S", 'd', true);
//--
EcValueContainer pbThatMixerTempDC = new EcValueContainer(40, 18, "'C", 'd', true);
EcTitleContainer pbThatMixerLimitPL;
//--
void stThisModelPannel(){
  String[] lpTitlesA={" W"," S"," N","  F-Oil"};pbThatMixerWetDryPL = new EcTitleContainer(20, 18,lpTitlesA);
  String[] lpTitlesB={" OP"," CL"," .."};pbThatMixerLimitPL = new EcTitleContainer(25, 18,lpTitlesB);
  pbThatMixerTempDC.cmBoxColor=color(0xCC,0x77,0x77);
  
}//++
void drThisModelPannel(boolean pxAct){if(!pxAct){return;}
  pbThisModelPannel.ccUpdate(pbLePannelFocus);
  shapeMixer(pbThisModelPannel.cmX+10,pbThisModelPannel.cmY+10,80);
  pbThatMixerMixturePL.ccUpdate(pbThisModelPannel.ccGetPos(), 10, 10);
  pbThatMixerGatePL.ccUpdate(pbThisModelPannel.ccGetPos(), 43, 60);
  //--  
  pbThatMixerWetDryPL.ccUpdate(pbThisModelPannel.ccGetPos(), 20, 16);
  pbThatMixerTimerTB.ccUpdate(pbThatMixerWetDryPL.ccGetPos(), pbThatMixerWetDryPL.cmWidth+2, 0);
  //--  
  pbThatMixerLimitPL.ccUpdate(pbThatMixerGatePL.ccGetPos(), pbThatMixerGatePL.cmScale+8, 0);
  
  pbThatMixerTempDC.ccUpdate(pbThatMixerGatePL.ccGetPos(), -50, 0);
  if(true){
    //--feedback--
    pbThatMixerMixturePL.cmAct=pbLesLinkMemory.ccReadBit(18, 0x0);
    pbThatMixerGatePL.cmAct=pbLesLinkMemory.ccReadBit(18, 0x1);
  }
}//++
//-- --
// </editor-fold>  


//-- --- --- --- --- ---
//  Control Pannel: as memory test setting <--P:151
//-- --- --- --- --- ---
//--
EcLabledFrame pbThisControlPannel=new EcLabledFrame(360, 370, 260, 85, "MemoryTST", 'p',151);
// <editor-fold defaultstate="collapsed" desc="P151:pbThisControlPannel">
//--
EcTitleContainer pbThatMemoryTitleContainer;
EcValueContainer pbThatMemoryAddrContainer=new EcValueContainer(50, 18, ":", 'd', true);
//--
EcPushButtonDeo pbThatMemoryViewPageStepper = new EcPushButtonDeo(18, "<", ">");
EcValueContainer pbThatMemoryViewPageContainer = new EcValueContainer(55, 18, " -p", 'd', true);
//--
EcPushButtonDeo pbThatMemorySelectStepper = new EcPushButtonDeo(18, "<", ">");
EcPushButtonDeo pbThatAddrSelectStepper = new EcPushButtonDeo(18, "<", ">");
EcPushButtonDeo pbThatValueChangeStepper = new EcPushButtonDeo(18, "-", "+");
//--
EcMultiButton pbThoseMemoryFlipSW[]= new EcMultiButton[16];
//--
void stThisControlPannel(){
  String[] lpTitles={"Linked","Setting","Monitor"};
  pbThatMemoryTitleContainer=new EcTitleContainer(70, 18, lpTitles);
  for(int i=0;i<16;i++){pbThoseMemoryFlipSW[i]=new EcMultiButton(18, 18, hex(i,1), 'p');}
}//++
void drThisControlPannel(boolean pxAct){if(!pxAct){return;}
  pbThisControlPannel.ccUpdate(pbLePannelFocus);
  //--
  pbThatMemoryTitleContainer.ccUpdate(pbThisControlPannel.ccGetPos(), 5, 20);
  pbThatMemorySelectStepper.ccUpdate(pbThisControlPannel.ccGetPos(), 80, 20);
  pbThatMemoryViewPageContainer.ccUpdate(pbThisControlPannel.ccGetPos(), 155, 20);
  pbThatMemoryViewPageStepper.ccUpdate(pbThisControlPannel.ccGetPos(), 215, 20);
  //--
  pbThatAddrSelectStepper.ccUpdate(pbThisControlPannel.ccGetPos(), 5, 40);
  pbThatMemoryAddrContainer.ccUpdate(pbThisControlPannel.ccGetPos(), 45, 40);
  //--
  for(int i=0;i<8;i++){pbThoseMemoryFlipSW[i].ccUpdate(pbThisControlPannel.ccGetPos(), 233-19*i, 40);}
  for(int i=8;i<16;i++){pbThoseMemoryFlipSW[i].ccUpdate(pbThisControlPannel.ccGetPos(), 290-19*i, 60);}
  pbThatValueChangeStepper.ccUpdate(pbThisControlPannel.ccGetPos(), 215, 60);
}//++
boolean msThisControlPannel(boolean pxAct){if(!pxAct){return false;}
  if(pbThatMemorySelectStepper.ccIsMouseOver()==1){pbThatMemoryTitleContainer.ccShiftIndex(-1);fsRefreshViewerPage();return true;}
  if(pbThatMemorySelectStepper.ccIsMouseOver()==2){pbThatMemoryTitleContainer.ccShiftIndex( 1);fsRefreshViewerPage();return true;}
  //--
  if(pbThatMemoryViewPageStepper.ccIsMouseOver()==1){fsMemoryShifPage(-1);return true;}
  if(pbThatMemoryViewPageStepper.ccIsMouseOver()==2){fsMemoryShifPage( 1);return true;}
  //--
  if(pbThatAddrSelectStepper.ccIsMouseOver()==1){pbThatMemoryAddrContainer.ccShiftValue(-1);return true;}
  if(pbThatAddrSelectStepper.ccIsMouseOver()==2){pbThatMemoryAddrContainer.ccShiftValue( 1);return true;}
  //--
  if(pbThatValueChangeStepper.ccIsMouseOver()==1){fsMemoryTestShift(-1);return true;}
  if(pbThatValueChangeStepper.ccIsMouseOver()==2){fsMemoryTestShift( 1);return true;}
  //--
  if(true){
    for(int i=0;i<16;i++){
      if(pbThoseMemoryFlipSW[i].ccIsMouseOver()){fsMemoryTestFlip(i);return true;}
    }
  }
  return false;
}//++
//--
void fsMemoryShifPage(int pxOffset){switch(pbThatMemoryTitleContainer.cmIndex){
  case 0:pbThatMemoryViewPageContainer.cmMax=(pbLesLinkMemory.ccTellWordSize()-1)/16;break;
  case 1:pbThatMemoryViewPageContainer.cmMax=(pbLesSettingMemory.ccTellWordSize()-1)/16;break;
  case 2:pbThatMemoryViewPageContainer.cmMax=(pbLesMonitorMemory.ccTellWordSize()-1)/16;break;
  default:break;}pbThatMemoryViewPageContainer.ccShiftValue(pxOffset);fsRefreshViewerPage();}
//--
void fsMemoryTestFlip(int pxBit){switch(pbThatMemoryTitleContainer.cmIndex){
    case 0:pbLesLinkMemory.ccFlipBit(pbThatMemoryAddrContainer.cmVal, pxBit);break;
    case 1:pbLesSettingMemory.ccFlipBit(pbThatMemoryAddrContainer.cmVal, pxBit);break;
    case 2:pbLesMonitorMemory.ccFlipBit(pbThatMemoryAddrContainer.cmVal, pxBit);break;
    default:break;}fsRefreshViewerPage();}
//--
void fsMemoryTestShift(int pxOffset){switch(pbThatMemoryTitleContainer.cmIndex){
    case 0:pbLesLinkMemory.ccShiftWord(pbThatMemoryAddrContainer.cmVal, pxOffset);break;
    case 1:pbLesSettingMemory.ccShiftWord(pbThatMemoryAddrContainer.cmVal, pxOffset);break;
    case 2:pbLesMonitorMemory.ccShiftWord(pbThatMemoryAddrContainer.cmVal, pxOffset);break;
    default:break;}fsRefreshViewerPage();}
//++
//-- --

// </editor-fold>  


//-- --- --- --- --- ---
//  View Pannel: as memory test viewer <--P:152
//-- --- --- --- --- ---
EcLabledFrame pbThisViewPannel= new EcLabledFrame(400, 30, 195, 205, "MemoryTST", 'p',152);
// <editor-fold defaultstate="collapsed" desc="P152:pbThisViewPannel">
//--
//--
EcValueContainer pbThoseMemoryViewer[] = new EcValueContainer[16];
void stThisViewPannel(){
  for(int i=0;i<16;i++){
    pbThoseMemoryViewer[i]=new EcValueContainer(55, 18, " ", 'h', true);
    pbThoseMemoryViewer[i].cmBoxColor=color(0x77,0xEE,0x77);
  }
}//++
void drThisViewPannel(boolean pxAct){if(!pxAct){return;}
  pbThisViewPannel.ccUpdate(pbLePannelFocus);
  int lpX=pbThisViewPannel.cmX;
  int lpY=pbThisViewPannel.cmY;
  //--
  fill(0xEE);
  text(pbThatMemoryTitleContainer.ccTellCurrentTitle(),lpX+5,lpY+20);
  //--
  for(int i=0;i<8;i++){
    fill(0xEE);text(nf(i+pbThatMemoryViewPageContainer.cmVal*16,3),lpX+5,lpY+40+i*20);
    pbThoseMemoryViewer[i].ccUpdate(pbThisViewPannel.ccGetPos(), 30, 40+i*20);
  }
  for(int i=8;i<16;i++){
    fill(0xEE);text(nf(i+pbThatMemoryViewPageContainer.cmVal*16,3),lpX+105,lpY-120+i*20);
    pbThoseMemoryViewer[i].ccUpdate(pbThisViewPannel.ccGetPos(), 130, i*20-120);
  }
  //--
  if((pbLeRoller%4)==1){fsRefreshViewerPage();}
  //--
}//++
void fsRefreshViewerPage(){
  int[] lpArr=null;
  switch(pbThatMemoryTitleContainer.cmIndex){
    case 00:lpArr=pbLesLinkMemory.ccHandWordArray(pbThatMemoryViewPageContainer.cmVal*16, 16);break;
    case 01:lpArr=pbLesSettingMemory.ccHandWordArray(pbThatMemoryViewPageContainer.cmVal*16, 16);break;
    case 02:lpArr=pbLesMonitorMemory.ccHandWordArray(pbThatMemoryViewPageContainer.cmVal*16, 16);break;
    default:break;
  }
  if(lpArr==null){return;}if(lpArr.length>16){println(nf(lpArr.length,2)+"reject!!");return;}
  for(int i=0;i<16;i++){pbThoseMemoryViewer[i].cmVal=lpArr[i];}
}//++
//--

// </editor-fold>  
//++
//-- --

//++ +++ +++ +++ +++






/* ** ** ** ** ** ** ** */
/* **                ** */
/* **     Zelda      ** */
/* **                ** */
/* ** ** ** ** ** ** ** */
SimPlant pbTheSimulator = new SimPlant();
class SimPlant{
  //--
  boolean cmCompressorAX, cmCompressorAN;
  int cmCompressorCT;
  //--
  SimPlant(){
    cmCompressorAX=false;cmCompressorAN=false;
    cmCompressorCT=0;
  }
  void ccRefresh(int pxRoller){if(pxRoller%3!=1){return;}
    cmCompressorAN=cmCompressorAX;
    cmCompressorCT=cmCompressorAN? 1999+pxRoller*16:0;
  }
  //--
}//++
//-- 
void zeldaDraw(boolean pxAct){if(!pxAct){return;}
  //--
  //--
  //--
  //-- ---mouse pressed---
  if(mousePressed){
    //-- Buttons in Control Pannel
    if(pbLePannelFlag[pbThisControlPannel.cmFocusID]){
      //--
    }
    //--
  }
  //--
}//++
void zeldaFlipCompressor(){
  pbTheSimulator.cmCompressorAX=!pbTheSimulator.cmCompressorAX;
}//++
//++ +++ +++ +++ +++










/* ** ** ** ** ** ** ** */
/* **                ** */
/* **    PlcLink     ** */
/* **                ** */
/* ** ** ** ** ** ** ** */
//--
//import java.io.*;
//import java.net.*;
//import processing.net.*;
//--
Client plcLinkClient;
boolean plcLinkAvailableAtStart=false;
boolean plcLinkHasUnknownHostException=false;
boolean plcLinkIsIOException=false;
boolean plcLinkClientIsActive=false;
byte[] plcLinkCommand;
byte[] plcLinkData=new byte[128];
//--
int plcLinkRecievedADDR=0;
byte[] plcLinkRecievedWM=new byte[128];
//--
int plcLinkWriteADDR=8000;
int plcLinkReadADDR=8016;
//--
void plcLinkSetup(boolean pxAct){if(!pxAct){return;}
  try{
    println("::try connect");
    InetAddress lpAdd=InetAddress.getByName("10.57.7.3");
    //println(lpAdd.getHostName());
    //println(lpAdd.getHostAddress());
    plcLinkAvailableAtStart=lpAdd.isReachable(128);
    //println(str(pbLinkAvailableAtStart));
  }catch(UnknownHostException e){plcLinkHasUnknownHostException=true;}
   catch(IOException e){plcLinkIsIOException=true;}
  plcLinkCommand =new byte[420];for(int i=0;i<420;i++){plcLinkCommand[i]=0;}//<--OFFICIAL MAX LENGTH IS 492 
  if(plcLinkAvailableAtStart){
    plcLinkClient=new Client(this,"10.57.7.3",507);
    
    Integer lpCX=new Integer(0);
    
    //prototol header
    lpCX=0xFB;plcLinkCommand[ 0]=lpCX.byteValue();//
    lpCX=0x80;plcLinkCommand[ 1]=lpCX.byteValue();//
    lpCX=0x80;plcLinkCommand[ 2]=lpCX.byteValue();//
    lpCX=0x00;plcLinkCommand[ 3]=lpCX.byteValue();//
    
    //command body
    lpCX=0xFF;plcLinkCommand[ 4]=lpCX.byteValue();//C-status
    lpCX=0x7B;plcLinkCommand[ 5]=lpCX.byteValue();//connection option
    lpCX=0xFE;plcLinkCommand[ 6]=lpCX.byteValue();//ID_L
    lpCX=0x00;plcLinkCommand[ 7]=lpCX.byteValue();//ID_H
    
    //constant
    lpCX=0x11;plcLinkCommand[ 8]=lpCX.byteValue();//DONT TOUCH
    lpCX=0x00;plcLinkCommand[ 9]=lpCX.byteValue();//DONT TOUCH
    lpCX=0x00;plcLinkCommand[10]=lpCX.byteValue();//DONT TOUCH
    lpCX=0x00;plcLinkCommand[11]=lpCX.byteValue();//DONT TOUCH
    lpCX=0x00;plcLinkCommand[12]=lpCX.byteValue();//DONT TOUCH
    lpCX=0x00;plcLinkCommand[13]=lpCX.byteValue();//DONT TOUCH
    
    //command set
    lpCX=0x00;plcLinkCommand[14]=lpCX.byteValue();//command
    lpCX=0x00;plcLinkCommand[15]=lpCX.byteValue();//mode
    lpCX=0x00;plcLinkCommand[16]=lpCX.byteValue();//DONT TOUCH
    lpCX=0x01;plcLinkCommand[17]=lpCX.byteValue();//DONT TOUCH
    
    //data amount
    lpCX=0x06;plcLinkCommand[18]=lpCX.byteValue();//reg L
    lpCX=0x00;plcLinkCommand[19]=lpCX.byteValue();//reg H
    
    //request
    lpCX=0x02;plcLinkCommand[20]=lpCX.byteValue();//memory type
    
    lpCX=0xE8;plcLinkCommand[21]=lpCX.byteValue();//memory adr L
    lpCX=0x03;plcLinkCommand[22]=lpCX.byteValue();//memory adr M
    lpCX=0x00;plcLinkCommand[23]=lpCX.byteValue();//memory adr H
    
    lpCX=0x01;plcLinkCommand[24]=lpCX.byteValue();//memory(word) amount L
    lpCX=0x00;plcLinkCommand[25]=lpCX.byteValue();//memory(word) amount H
    
    //command data
    lpCX=0x00;plcLinkCommand[26]=lpCX.byteValue();//data write L
    lpCX=0x00;plcLinkCommand[27]=lpCX.byteValue();//data write H
    
    lpCX=0x00;plcLinkCommand[28]=lpCX.byteValue();//--01
    lpCX=0x00;plcLinkCommand[29]=lpCX.byteValue();//
    
    lpCX=0x00;plcLinkCommand[30]=lpCX.byteValue();//--02
    lpCX=0x00;plcLinkCommand[31]=lpCX.byteValue();//
    
    lpCX=0x00;plcLinkCommand[32]=lpCX.byteValue();//--03
    lpCX=0x00;plcLinkCommand[33]=lpCX.byteValue();//
    
    lpCX=0x00;plcLinkCommand[38]=lpCX.byteValue();//--04
    lpCX=0x00;plcLinkCommand[39]=lpCX.byteValue();//
    
    lpCX=0x00;plcLinkCommand[40]=lpCX.byteValue();//--05
    lpCX=0x00;plcLinkCommand[41]=lpCX.byteValue();//
    
    lpCX=0x00;plcLinkCommand[42]=lpCX.byteValue();//--06
    lpCX=0x00;plcLinkCommand[43]=lpCX.byteValue();//
    
    lpCX=0x00;plcLinkCommand[44]=lpCX.byteValue();//--07
    lpCX=0x00;plcLinkCommand[45]=lpCX.byteValue();//
    
    lpCX=0x00;plcLinkCommand[46]=lpCX.byteValue();//--08
    lpCX=0x00;plcLinkCommand[47]=lpCX.byteValue();//
    
    lpCX=0x00;plcLinkCommand[48]=lpCX.byteValue();//--09
    lpCX=0x00;plcLinkCommand[49]=lpCX.byteValue();//
    
    lpCX=0x00;plcLinkCommand[50]=lpCX.byteValue();//--10
    lpCX=0x00;plcLinkCommand[51]=lpCX.byteValue();//
    
    lpCX=0x00;plcLinkCommand[52]=lpCX.byteValue();//--11
    lpCX=0x00;plcLinkCommand[53]=lpCX.byteValue();//
    
    lpCX=0x00;plcLinkCommand[54]=lpCX.byteValue();//--12
    lpCX=0x00;plcLinkCommand[55]=lpCX.byteValue();//
    
    lpCX=0x00;plcLinkCommand[56]=lpCX.byteValue();//--13
    lpCX=0x00;plcLinkCommand[57]=lpCX.byteValue();//
    
    lpCX=0x00;plcLinkCommand[58]=lpCX.byteValue();//--14
    lpCX=0x00;plcLinkCommand[59]=lpCX.byteValue();//
    
    lpCX=0x00;plcLinkCommand[60]=lpCX.byteValue();//--15
    lpCX=0x00;plcLinkCommand[61]=lpCX.byteValue();//
        
  }else{plcLinkClient=null;}
  //--
}//+++
//--
boolean plcLinkDraw(boolean pxAct, int pxRoller){if(!pxAct){return false;}
  //--
  boolean lpIntervalLV0=(pxRoller%4==0);
  boolean lpIntervalLV1=(pxRoller%4==1);
  boolean lpIntervalLV2=(pxRoller%4==2);
  boolean lpIntervalLV3=(pxRoller%4==3);
  //--
  if(!plcLinkAvailableAtStart){return false;}
  if(plcLinkClient==null){return false;}else{plcLinkClientIsActive=plcLinkClient.active();if(!plcLinkClient.active()){return false;}}
  //--
  if(lpIntervalLV1){
    plcLinkUpdateCommand('w','m',plcLinkWriteADDR,pbLesLinkMemory.ccGetBytes(0, 32));
    plcLinkSendRequest();  
  }
  if(lpIntervalLV3){
    plcLinkUpdateCommand('r','m',plcLinkReadADDR,new byte[128]);
    plcLinkSendRequest();  
  }
  //--
  //--
  if(pxAct){
    if(plcLinkReadAnswer()){
      //fnReadUp(pbLinkData,"r__[");
      if(plcLinkData[14]==0){
        Integer lpCX=new Integer(plcLinkData[24]);
        int lpLength=lpCX*2;
        byte[] lpBuf=new byte[lpLength];
        for(int i=0;i<lpLength;i+=2){
          lpBuf[i+1]=plcLinkData[26+i  ];
          lpBuf[i  ]=plcLinkData[26+1+i];
        }
        int lpAddrHii=(int)plcLinkData[23]<0?plcLinkData[23]+256:plcLinkData[23];
        int lpAddrMid=(int)plcLinkData[22]<0?plcLinkData[22]+256:plcLinkData[22];
        int lpAddrLow=(int)plcLinkData[21]<0?plcLinkData[21]+256:plcLinkData[21];
        plcLinkRecievedADDR=(lpAddrHii<<16)+(lpAddrMid<<8)+lpAddrLow;
        plcLinkRecievedWM=lpBuf;
      }
    }return true;
  }else{return false;}
  //--
}//+++
//--
boolean plcLinkSendRequest( ){
  if(plcLinkClient!=null && plcLinkClient.active()){plcLinkClient.write(plcLinkCommand);return true;}
  else{return false;}
}//++
//--
boolean plcLinkReadAnswer(  ){
  if(plcLinkClient.available()>0){
    plcLinkClient.readBytes(plcLinkData);
    return true;
  }else{return false;}  
}//++
//--
void plcLinkUpdateCommand(char pxMode, char pxType, int pxAddr, byte[] pxData){
  //---
  if(pxData==null){return;}
  //---
  String lpAddAll;
  String lpAddLow;
  String lpAddMid;
  String lpAddHi;
  Integer lpCX;
  int cmLength=pxData.length;
  //--
  if(cmLength>32){cmLength=32;}
  lpCX=cmLength/2;plcLinkCommand[24]=lpCX.byteValue();
  //---
  switch(pxMode){
    case 'r':
      lpCX=0x00;plcLinkCommand[14]=lpCX.byteValue();
      lpCX=6;plcLinkCommand[18]=lpCX.byteValue();
      break;//command + data amount
    case 'w':
      lpCX=0x01;plcLinkCommand[14]=lpCX.byteValue();
      lpCX=6+cmLength;plcLinkCommand[18]=lpCX.byteValue();
      break;//command + data amount
    default:break;
  }
  //---
  switch(pxType){
    case 'm': lpCX=0x02;plcLinkCommand[20]=lpCX.byteValue();break;//memory type
    case 'l': lpCX=0x04;plcLinkCommand[20]=lpCX.byteValue();break;//memory type
    default:break;
  }
  //---
  //---
  lpAddAll=hex(pxAddr&0x00FFFFFF,6);
  lpAddHi=lpAddAll.substring(0,2);
  lpAddMid=lpAddAll.substring(2,4);
  lpAddLow=lpAddAll.substring(4,6);
  lpCX=unhex(lpAddLow);plcLinkCommand[21]=lpCX.byteValue();//memory adr L
  lpCX=unhex(lpAddMid);plcLinkCommand[22]=lpCX.byteValue();//memory adr M
  lpCX=unhex(lpAddHi);plcLinkCommand[23]=lpCX.byteValue();//memory adr H
  //---
  for(int i=0;i<cmLength;i+=2){
    plcLinkCommand[i+26]=pxData[i+1];
    plcLinkCommand[i+1+26]=pxData[i];
  }
}//++
//--
void plcCloseClient(){
  if(plcLinkClient==null){return;}
  plcLinkClient.clear();
  plcLinkClient.stop();
  plcLinkClient.dispose();
}//++
//--
void plcReadUpBytes(byte[] pxBt, String pxHead){
  int cmDataLen=pxBt.length;
  if(cmDataLen<=0){println(">>> cant read up");return;}
  for(int i=0;i<cmDataLen;i++){
   String lpMark="..";
   switch(i){
     case 14:lpMark="..COMMAND!!";break;
     case 18:lpMark="..COMMAND AMOUNT!!";break;
     case 20:lpMark="..MEMORY TYPE!!";break;
     case 21:lpMark="..MEMORY ADDR LOW";break;
     case 22:lpMark="..MEMORY ADDR MID";break;
     case 23:lpMark="..MEMORY ADDR HI";break;
     case 24:lpMark="..DATA AMOUNT";break;
     case 26:lpMark="..DATA WRITE LOW";break;
     case 27:lpMark="..DATA WRITE HI";break;
     default:break;
   }
   println(pxHead+hex(pxBt[i],2)+"] $$_"+str(i)+"of"+str(cmDataLen)+lpMark);
  }
  println(">>> over for"+pxHead);
}//++
//++ +++ +++ +++ +++ 









/* ** ** ** ** ** ** ** */
/* ** DONT TOUCH THIS** */
/* ** ** ** ** ** ** ** */
// <editor-fold defaultstate="collapsed" desc="dontTouchThis">  
static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Sketch" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
// </editor-fold>  
/* ** ** ** ** ** ** ** */
/* ** DONT TOUCH THIS** */
/* ** ** ** ** ** ** ** */
//EOF