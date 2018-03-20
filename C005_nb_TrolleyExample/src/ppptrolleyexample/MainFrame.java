/* *** *** *** ***
 * *
 *   MainFrame
 * *
 * *** *** *** * */
// --- --- --- ---original on my Mac11
// --- --- --- ---with Processing core 2.x

package ppptrolleyexample;

import processing.core.*;
import processing.data.*; 
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class MainFrame extends MainTab {
  
  
  //=== === === ===
  //  overriden
  //=== === === ===
  //<editor-fold desc="packed_code">
  
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
  
  //--
  
  @Override public void setup(){size(320, 240);
    frameRate(16);noStroke();textAlign(LEFT,TOP);ellipseMode(CENTER);
    frame.setTitle("trolley:i-Field");
    
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
    VcTagger.ccTag("mouseX",mouseX);
    VcTagger.ccTag("mouseY",mouseY);
    
    pbMouseMeter.ccSetEndPoint(mouseX, mouseY);
    VcTagger.ccTag("meterW",pbMouseMeter.cmW);
    VcTagger.ccTag("meterH",pbMouseMeter.cmH);
    
    VcTagger.ccTag("rolling",pbLeRoller);
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
      
      case 11:
        pbElementCoordinator.cmAutoSW.ccFlip();
      break;
      
      default:break;
    }
  }//+++
  
  //</editor-fold>
  //=== === < overriden
  
  
  //=== === === ===
  //  operte
  //=== === === ===
  //<editor-fold desc="packed_code">
  
  private void fsLineKeep(){
    if(pbLineKeeper.cmCommand!=VcKeeper.COMMANDS.NONE){
      switch(pbLineKeeper.cmCommand){
      
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
    
    //-- wiring 
    
    //-- wiring ** send
    
    pbLinkedMemory.ccWriteBit
      (0, 0x1,pbElementCoordinator.cmForewardSW.ccIsPressed());
    pbLinkedMemory.ccWriteBit
      (0, 0x2,pbElementCoordinator.cmBackwardSW.ccIsPressed());
    pbLinkedMemory.ccWriteBit
      (0, 0x9,pbElementCoordinator.cmAutoSW.ccIsAct());
    pbLinkedMemory.ccWriteBit
      (1, 0x1,pbElementCoordinator.cmChargeSW.ccIsPressed());
    pbLinkedMemory.ccWriteBit
      (1, 0x2,pbElementCoordinator.cmDischargeSW.ccIsPressed());
    
    //-- wiring ** recv
    
    boolean lpTrolleyGoesUp  =pbLinkedMemory.ccReadBit(4,0x1);
    boolean lpTrolleyGoesDown=pbLinkedMemory.ccReadBit(4,0x2);
    boolean lpTrolleyHitsUp  =pbLinkedMemory.ccReadBit(6,0x1);
    boolean lpTrolleyHitsDown=pbLinkedMemory.ccReadBit(6,0x2);
    
    pbElementCoordinator.cmTrolley.ccSetAct(lpTrolleyHitsUp||lpTrolleyHitsDown);
    
    pbElementCoordinator.cmChargePL
      .ccSetAct(pbLinkedMemory.ccReadBit(5, 0x1));
    pbElementCoordinator.cmDischargePL
      .ccSetAct(pbLinkedMemory.ccReadBit(5, 0x2));
    pbElementCoordinator.cmForewardSW
      .ccSetAct(lpTrolleyGoesUp);
    pbElementCoordinator.cmBackwardSW
      .ccSetAct(lpTrolleyGoesDown);
    
    //-- zelda
    
    if(lpTrolleyGoesUp)
      {pbDataCoordinator.cmTrolleyIconPercentage+=0.01f;}
    if(lpTrolleyGoesDown)
      {pbDataCoordinator.cmTrolleyIconPercentage-=0.01f;}
    pbDataCoordinator.cmTrolleyIconPercentage
      =constrain(pbDataCoordinator.cmTrolleyIconPercentage, 0f, 1f);
    
    if(lpTrolleyHitsUp)
      {pbDataCoordinator.cmTrolleyIconPercentage=0.9f;}
    if(lpTrolleyHitsDown)
      {pbDataCoordinator.cmTrolleyIconPercentage=0.1f;}
    pbElementCoordinator.cmTrolley.ccSetPercentage
      (pbDataCoordinator.cmTrolleyIconPercentage);
    
  }//+++
  
  //</editor-fold>
  //=== === < operate
  
  
  //=== === === ===
  //  inner
  //=== === === ===
  //<editor-fold desc="packed_code">
  
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
        
      }//+++
    };//---
    
    VcRunner cmUpdateRunner=new VcRunner(){
      @Override public void run(){
        //--
        
        pbSwingCoordinator.cmToolBarWindow.ccSetRunLampStatus(pbLeRoller<7);
        
        //--
      }//+++
    };//---
    
    //-- ** base class
    class VcRunner implements Runnable{
      volatile protected String cmInputed=null;
      synchronized void ccSetParam(String pxParam){cmInputed=pxParam;}
      @Override public void run(){
        System.err.println
          (".VcRunner.run()::this_should_never_been_reached");
      }//+++
    }//***
  }//***

  //</editor-fold>
  //=== === < swing ui
  
  
  /* *** *** *** *** *
   * DONT TOUCH THIS *
   * *** *** *** *** */
  //<editor-fold defaultstate="collapsed" desc="staitc_public_void_main">
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "ppptrolleyexample.MainFrame" };
    if (passedArgs != null) {PApplet.main(concat(appletArgs, passedArgs));}
    else {PApplet.main(appletArgs);}
  }//+++
  //--
  //</editor-fold>
  /* *** *** *** *** *
   * DONT TOUCH THIS *
   * *** *** *** *** */
  
}//***eof

