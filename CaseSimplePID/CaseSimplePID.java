/*
 * Simple PID (raw)
 *
 */

package ppptest;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import processing.core.PApplet;

public class CaseSimplePID extends PApplet{
  
  private static PApplet self=null;
  
  //=== action
  
  //=== swing
  
  static final JFrame O_FRAME = new JFrame(CaseSimplePID.class.getName());
  
  static final JTextField O_TEMP_TARGET_TB = new JTextField("0.0");
  
  static final JTextField O_TEMP_DEAD_TB = new JTextField("0.0");
  
  static final JTextField O_TEMP_PORT_TB = new JTextField("0.0");
  
  static final JTextField O_TEMP_SAMP_TB = new JTextField("0.0");
  
  static final JTextField O_TEMP_ADJT_TB = new JTextField("0.0");
  
  static final Runnable O_SWING_INIT = new Runnable() {
    @Override public void run() {
      
      //-- setting pane
      //-- setting pane ** target lane
      JPanel lpTargetLane = new JPanel(new FlowLayout(FlowLayout.LEADING));
      lpTargetLane.setBorder(BorderFactory
        .createTitledBorder(BorderFactory.createEtchedBorder(), "target"));
      lpTargetLane.add(new JLabel("value[%]:"));
      //-- setting pane ** pack
      JPanel lpSettingPane = new JPanel(new GridLayout(3, 1, 1, 1));
      lpSettingPane.add(lpTargetLane);
      
      //-- dummy pane
      JPanel lpDummyPane = new JPanel(new BorderLayout(1, 1));
      lpDummyPane.add(new JButton("=D="));
      
      //-- tab
      JTabbedPane lpContentPane = new JTabbedPane();
      lpContentPane.add("setting", lpSettingPane);
      lpContentPane.add("dummy", lpDummyPane);
      
      //-- frame
      O_FRAME.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
      O_FRAME.getContentPane().add(lpContentPane);
      O_FRAME.setPreferredSize(new Dimension(320, 240));
      O_FRAME.setLocation(
        self.frame.getLocation().x+self.frame.getWidth()+5,
        self.frame.getLocation().y
      );
      O_FRAME.pack();
      O_FRAME.setVisible(true);
      
    }//+++
  };//***
  
  static final Runnable O_SWING_FLIP = new Runnable() {
    @Override public void run() {
      boolean lpNow = O_FRAME.isVisible();
      if(!lpNow){
        O_FRAME.setLocation(
          self.frame.getLocation().x+self.frame.getWidth()+5,
          self.frame.getLocation().y
        );
      }//..?
      O_FRAME.setVisible(!lpNow);
    }//+++
  };//***
  
  static final void ccSetupInputBox(JTextField pxTarget, int pxW, int pxH){
    pxTarget.setEditable(false);
    pxTarget.setEnabled(false);
    pxTarget.setBackground(Color.LIGHT_GRAY);
    pxTarget.setForeground(Color.DARK_GRAY);
    pxTarget.setDisabledTextColor(Color.DARK_GRAY);
    pxTarget.setHorizontalAlignment(JTextField.RIGHT);
    pxTarget.setPreferredSize(new Dimension(pxW, pxH));
  }//+++

  //=== overridden
  
  int pbRoller = 0;
  
  @Override public void setup() {
    
    //-- pre setting
    size(320,240);
    noSmooth();
    frame.setTitle(CaseSimplePID.class.getName());
    self=this;
    
    //-- replace setting
    frameRate(16);noStroke();textAlign(LEFT, TOP);ellipseMode(CENTER);
    
    //-- post setting
    SwingUtilities.invokeLater(O_SWING_INIT);
    println("-- setup over");
    
  }//++!

  @Override public void draw() {
  
    //-- pre drawing
    background(0);
    pbRoller++;pbRoller&=0x0f;
    
    //-- updating
    fill(0xFF);
    text(nf(pbRoller,2),5,5);
    
  }//++~

  @Override public void keyPressed() {
    switch(key){
      
      //-- direction
      case 'w':break;
      case 's':break;
      case 'a':break;
      case 'd':break;
      
      //-- confirm
      case 'r':break;
      case 'f':break;
      case 'j':break;
      case 'k':break;
      
      //-- defult
      case 'q':ssPover();break;
      default:break;
      
    }//..?
  }//+++

  @Override public void mousePressed() {
    switch (mouseButton) {
      case LEFT:return;
      case RIGHT:
        SwingUtilities.invokeLater(O_SWING_FLIP);
      break;
      default:break; //+++
    }//..?
  }//+++
  
  //=== utility
  
  private void ssPover(){
    
    //-- default
    println("--exit <- ");
    exit();
  }//+++
  
  //=== real
  
  class ZcReal{
    float cmVal;
    boolean cmIsStatic;
  }//***
  
  void ccTransfer(ZcReal pxPotentialP, ZcReal pxPotentialN, int pxRatio){
    float lpDiff=pxPotentialP.cmVal-pxPotentialN.cmVal;
    lpDiff/=pxRatio;
    if(!pxPotentialP.cmIsStatic){pxPotentialP.cmVal+=lpDiff;}
    if(!pxPotentialN.cmIsStatic){pxPotentialN.cmVal-=lpDiff;}
  }//+++
  
  //=== controller
  
  class ZcController{
    void ccRun(){}
    //--
    void ccSetDead(){}
    void ccSetProportion(){}
    
    //--
    void ccSetProcessValue(){}
    void ccSetTargetValue(){}
    //--
    float ccGetAnalogoutput(){return 0f;}
    boolean ccGetPositiveOutput(){return false;}
    boolean ccGetNegativeOutput(){return false;}
    
  }//***

  //=== entry
  
  public static void main(String[] args) {
    PApplet.main(CaseSimplePID.class.getCanonicalName());
  }//!!!

}//***eof
