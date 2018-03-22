/* *** *** *** ***
 * *
 *   MainFrame
 * *
 * *** *** *** * */
// --- --- --- ---original on my Mac11
// --- --- --- ---with Processing core 2.x

package pppif;

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
    frame.setTitle("blink:i-Field");
    
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
    //--
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
    
    pbElementCoordinator.cmBlinkPL
      .ccSetActivity(pbLinkedMemory.ccReadBit(0, 0x0));
  
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

