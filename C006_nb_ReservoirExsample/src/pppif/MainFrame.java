/* --def %rivision%
 * #[flag] "version/filename" : issue $ describe,, 
 * [ ] "": $,,,
 * [ ] "": $,,,
 * [ ] "": $,,,
 * --end
 *
 * --base:"tp_IField_vAA03.zip"
 * --data:1803222204
 * --number:C006
 * --for:Processing 2.x core
 */

package pppif;

import java.awt.Color;
import processing.core.*;
import processing.data.*; 
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class MainFrame extends MainTab {
  
  static volatile MainFrame pbSelf;
  static volatile int pbLeRoller=0;
  
  McCoordinator pbDataCoordinator;
  
  EcCoordinator pbElementCoordinator;
  
  ZcSimplifiedMemory pbLinkedMemory;
  ZcCPUZero pbTheSX;
  
  VcKeeper pbLineKeeper;
  VcRunnerManager pbRunnerManager;
  ScCoordinator pbSwingCoordinator;
  
  EcFactory.EcRect pbMouseMeter;
  
  //=== === === ===
  //  overriden
  //=== === === ===
  
  @Override public void setup(){size(320, 240);
    frameRate(16);noStroke();textAlign(LEFT,TOP);ellipseMode(CENTER);
    frame.setTitle("reservoir:i-Field");
    
    //-- refrence initiate
    
    pbSelf=this;
    VcTagger.ccInit(this, 8);
    VcTagger.ccFlip();
    EcFactory.ccSetOwner(this);
    
    //-- construction
    
    pbElementCoordinator=new EcCoordinator();
    
    pbDataCoordinator=new McCoordinator();
    
    pbLinkedMemory=new ZcSimplifiedMemory();
    pbTheSX=new ZcCPUZero(pbLinkedMemory);
    
    pbLineKeeper=new VcKeeper();
    pbRunnerManager=new VcRunnerManager();
    SwingUtilities.invokeLater(pbRunnerManager.cmSetupRunner);
    
    pbMouseMeter=new EcFactory.EcRect();
    pbMouseMeter.ccSetBound(0, 0, width, height);
    
    //-- post setting
    
    //-- end of setting 
    println("--done setup");
    
  }//++
  
  @Override public void draw() { 
    background(0);
    pbLeRoller++;pbLeRoller&=0x0F;
    int lpMillis=millis();
    
    //-- background
    fill(0xCC);text("Res",50,50);
    
    //-- linking
    fsZelda();
    
    //-- clockwise operationg
    fsLineKeep();
    
    //-- updating
    try{
      SwingUtilities.invokeAndWait(pbRunnerManager.cmUpdateRunner);
    }catch(Exception e){System.err.println(".cmUpdateRunner::"+e.toString());}
    
    pbElementCoordinator.ccUpdate();
    
    pbTheSX.ccUpdate();
    
    //-- taggin 
    VcTagger.ccTag("mouseID",pbElementCoordinator.ccTellCurrentID());
    
    VcTagger.ccTag("rolling",pbLeRoller);
    VcTagger.ccTag("mouseX",mouseX);
    VcTagger.ccTag("mouseY",mouseY);
    
    pbMouseMeter.ccSetEndPoint(mouseX, mouseY);
    VcTagger.ccTag("meterW",pbMouseMeter.cmW);
    VcTagger.ccTag("meterH",pbMouseMeter.cmH);
    
    lpMillis=millis()-lpMillis;
    VcTagger.ccTag("ms/f", lpMillis);
    VcTagger.ccStabilize();
    
  }//+++

  @Override public void keyPressed() {switch(key){
    //--

    //-- system
    case 'n':VcTagger.ccFlip();break;
    case 'm':pbMouseMeter.ccSetLocation(mouseX, mouseY);break;
    case 'q':fsPover();break;
    default:break;
  }}//+++
  
  @Override public void mousePressed() {
    switch(pbElementCoordinator.ccTellCurrentID()){
      //[WRITE]::case :
      default:break;
    }
  }//+++
  
  //=== === < overriden
  
  
  //=== === === ===
  //  operate
  //=== === === ===
  
  //== operate ** invokelater
  
  final void fsStack(String pxLine){
    if(!SwingUtilities.isEventDispatchThread()){
      pbRunnerManager.cmStackRunner.ccSetParam(pxLine);
      SwingUtilities.invokeLater(pbRunnerManager.cmStackRunner);
    }else{System.err.
      println(".MainFrame.fsStack()::dont call this from EDT::"+pxLine);}
  }//***
  
  //== operate ** normal
  
  private void fsLineKeep(){
    if(pbLineKeeper.cmCommand!=VcKeeper.COMMANDS.NONE){
      switch(pbLineKeeper.cmCommand){
        
        case ERROR_CLEAR:
          pbDataCoordinator.cmErrorClearSwitchTimer.ccSet(8);
        break;
        
        case EXIT:fsPover();break;
        
        default:break;
      } 
      pbLineKeeper.cmCommand=VcKeeper.COMMANDS.NONE;
    }
  }//+++ 
  
  void fsPover(){
    //--


    //--
    println("--exit from main frame");
    exit();
  }//+++
  
  void fsZelda(){
    
    //-- send
    
    //-- send ** bit
    pbLinkedMemory.ccWriteBit
      (0, 0x0, fnIsKeyPressed(' '));//...TEST-BIT!!
    
    pbLinkedMemory.ccWriteBit(0, 0xA, pbDataCoordinator.mnRunSW);
    pbLinkedMemory.ccWriteBit
      (0, 0xF, pbDataCoordinator.cmErrorClearSwitchTimer.ccIsUp());
    pbLinkedMemory.ccWriteBit(2, 0x1, pbDataCoordinator.mnLowCut);
    pbLinkedMemory.ccWriteBit(2, 0x2, pbDataCoordinator.mnUpCut);
    
    //-- send ** word
    pbLinkedMemory.ccWriteWord(0x1, pbDataCoordinator.cmOnDelayTime);
    pbLinkedMemory.ccWriteWord(0x2, pbDataCoordinator.cmOffDelayTime);
    
    //-- recv
    
    int lpTankAD=pbLinkedMemory.ccReadWord(0x44);
    float lpTankPT=map((float)lpTankAD, 0, 990, 0, 1);
    
    //-- ui binding 
    
    pbDataCoordinator.mnRunPL=pbLinkedMemory.ccReadBit(4, 0xA);
    
    pbElementCoordinator.cmLowBoundPL
      .ccSetActivity(pbLinkedMemory.ccReadBit(6, 0x0));
    pbElementCoordinator.cmLowerPL
      .ccSetActivity(pbLinkedMemory.ccReadBit(6, 0x1));
    pbElementCoordinator.cmUpperPL
      .ccSetActivity(pbLinkedMemory.ccReadBit(6, 0x2));
    pbElementCoordinator.cmUpBoundPL
      .ccSetActivity(pbLinkedMemory.ccReadBit(6, 0x3));
    
    pbElementCoordinator.cmPumpPL
      .ccSetActivity(pbLinkedMemory.ccReadBit(6, 0xA));
    
    pbElementCoordinator.cmTankLevel.ccSetPercentage(lpTankPT);
    
    //-- error
    
    pbDataCoordinator.cmErrorClearSwitchTimer.ccUpdate();
    pbDataCoordinator.cmErrorCount
      =pbDataCoordinator.cmErrorFolder.ccActivatedCount();
    
    pbDataCoordinator.cmErrorFolder.ccSet
      (5,pbLinkedMemory.ccReadBit(5, 0x5));
    pbDataCoordinator.cmErrorFolder.ccSet
      (6,pbLinkedMemory.ccReadBit(5, 0x6));
    pbDataCoordinator.cmErrorFolder.ccSet
      (7,pbLinkedMemory.ccReadBit(5, 0x7));
    pbDataCoordinator.cmErrorFolder.ccScanError();
    
  }//+++
  
  //=== === < operate
  
  
  //=== === === ===
  //  inner
  //=== === === ===
  
  class VcRunnerManager{
    
    VcRunner cmSetupRunner=new VcRunner(){
      @Override public void run(){
        if(!SwingUtilities.isEventDispatchThread())
          {System.err.println("cmSetupRunner.run()::"
          +"blocking_outside_from_edt");}
        if(pbSelf==null){return;}
        //--
        try{
          UIManager.setLookAndFeel
            (UIManager.getCrossPlatformLookAndFeelClassName());
        }catch(Exception e)
          {System.err.println("cmSetupRunner.run::"+e.toString());}
        //--
        
        pbSwingCoordinator= new ScCoordinator();
        
        //-- post setting 
        pbSwingCoordinator.cmLimitSwitchOnDelayTB.setText
          (McFactory.ccToSecondByFrame(pbDataCoordinator.cmOnDelayTime));
        pbSwingCoordinator.cmLimitSwitchOffDelayTB.setText
          (McFactory.ccToSecondByFrame(pbDataCoordinator.cmOffDelayTime));
        
        pbSwingCoordinator.cmToolbarOperateSW.setForeground(
          pbSwingCoordinator.cmOperateWindow.isVisible()?
          Color.YELLOW:Color.DARK_GRAY
        );
        pbSwingCoordinator.cmToolbarErrorSW.setForeground(
          pbSwingCoordinator.cmErrorWindow.isVisible()?
          Color.YELLOW:Color.DARK_GRAY
        );
        
        //-- end of setting 
        System.out.println(".run()::done setup from EDT");
        
      }//+++
    };//---
    
    VcRunner cmUpdateRunner=new VcRunner(){
      @Override public void run(){
        
        //-- take
        
        pbSwingCoordinator.cmToolBarWindow.ccSetRunLampStatus(pbLeRoller<7);
        pbSwingCoordinator.cmRunSW.ccSetLampStatus(pbDataCoordinator.mnRunPL);
        pbSwingCoordinator.cmErrorList.ccRefreshModel(
          pbDataCoordinator.cmErrorFolder.ccGetActivatedArray()
        );
        pbSwingCoordinator.cmErrorSumTB.setText(Integer.toString(
          pbDataCoordinator.cmErrorCount
        ));
        
        //-- give
        
        pbDataCoordinator.mnRunSW=(pbSwingCoordinator.cmRunSW.ccGetState()==2);
        
        pbDataCoordinator.mnLowCut
          =pbSwingCoordinator.cmLowerMalfuncSW.isSelected();
        pbDataCoordinator.mnUpCut
          =pbSwingCoordinator.cmUpperMalfuncSW.isSelected();
        
      }//+++
    };//---
    
    VcRunner cmStackRunner = new VcRunner(){
      @Override public void run(){
        pbSwingCoordinator.cmErrorConsole.ccStack(cmInputed);
      }
    };//---
    
    //-- ** base class
    class VcRunner implements Runnable{
      volatile protected String cmInputed="<ni/>";
      synchronized void ccSetParam(String pxParam){cmInputed=pxParam;}
      @Override public void run(){
        System.err.println
          (".VcRunner.run()::this_should_never_been_reached");
      }//+++
    }//***
  }//***

  //=== === < swing ui
  
  /* --def %check-list%
   * #[flag] "file/version": issue $ description
   * [ ] "": $,,,
   * [c] "": build hide switch function to toolbar $,,,
   * [ ] "": $,,,
   * [c] "": build time input logic with focus losting $,,,
   * [c] "": let time span can be changed while running $,,,
   * [ ] "": $,,,
   * [c] "": build error count logic $,,,
   * [c] "": build error logic $,,,
   * [ ] "": $,,,
   * [c] "": build zelda logic $,,,
   * [c] "": build PLC logic $,,,
   * [ ] "": $,,,
   * [c] "": add error window with list and text area and clear button $,,,
   * [c] "": build a console group$,,,
   * [c] "": add operate window with pump lamped sw and timer setting box $,,,
   * [c] "": add operate and error switch to swing toolbar $,,,
   * [ ] "": $,,,
   * [c] "": build a pump icon $,,,
   * [c] "": build a duct as background $,,,
   * [c] "": build a reservoir model with gauge and limit $,,,
   * [c] "": build a hopper class $,,,
   * --end
  */
  
  /* *** *** *** *** *
   * DONT TOUCH THIS *
   * *** *** *** *** */
  //<editor-fold defaultstate="collapsed" desc="staitc_public_void_main">
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "pppif.MainFrame" };
    if (passedArgs != null) {PApplet.main(concat(appletArgs, passedArgs));}
    else {PApplet.main(appletArgs);}
  }//+++
  //--
  //</editor-fold>
  /* *** *** *** *** *
   * DONT TOUCH THIS *
   * *** *** *** *** */
  
}//***eof

