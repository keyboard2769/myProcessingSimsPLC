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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import processing.core.PApplet;

public class CaseSimplePID extends PApplet{
  
  private static PApplet self=null;
  
  //=== action
  
  static final MouseAdapter O_INPUT_BOX_LISTENER = new MouseAdapter() {
    @Override public void mouseReleased(MouseEvent me) {
      String lpD = ccGetStringByInputBox("??", "000");
      System.err.println("not_yet:"+lpD);
    }//+++
  };//***
  
  //=== swing
  
  static final JFrame O_FRAME = new JFrame(CaseSimplePID.class.getName());
  
  static final JTextField O_TEMP_TARGET_TB = new JTextField("0.0");
  
  static final JTextField O_TEMP_DEAD_TB = new JTextField("0.0");
  
  static final JTextField O_TEMP_PORT_TB = new JTextField("0.0");
  
  static final JTextField O_TEMP_SAMP_TB = new JTextField("0.0");
  
  static final JTextField O_TEMP_ADJT_TB = new JTextField("0.0");
  
  static final Runnable O_SWING_INIT = new Runnable() {
    @Override public void run() {
      
      //-- restyle
      try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (Exception e) {
        System.err.println("O_SWING_INIT::"+e.getMessage());
        System.exit(-1);
      }//..?
      
      //-- setting pane
      //-- setting pane ** target lane
      ccSetupInputBox(O_TEMP_TARGET_TB, 64, 22);
      JPanel lpTargetLane = new JPanel(new FlowLayout(FlowLayout.LEADING));
      lpTargetLane.setBorder(BorderFactory
        .createTitledBorder(BorderFactory.createEtchedBorder(), "Target"));
      lpTargetLane.add(new JLabel("value[`C]:"));
      lpTargetLane.add(O_TEMP_TARGET_TB);
      //-- setting pane ** range lane
      ccSetupInputBox(O_TEMP_PORT_TB, 48, 22);
      ccSetupInputBox(O_TEMP_DEAD_TB, 48, 22);
      JPanel lpRangeLane = new JPanel(new FlowLayout(FlowLayout.LEADING));
      lpRangeLane.setBorder(BorderFactory
        .createTitledBorder(BorderFactory.createEtchedBorder(), "Range"));
      lpRangeLane.add(new JLabel("proportion[?]:"));
      lpRangeLane.add(O_TEMP_PORT_TB);
      lpRangeLane.add(new JSeparator(JSeparator.VERTICAL));
      lpRangeLane.add(new JLabel("dead[?]:"));
      lpRangeLane.add(O_TEMP_DEAD_TB);
      //-- setting pane ** adjust lane
      ccSetupInputBox(O_TEMP_SAMP_TB, 48, 22);
      ccSetupInputBox(O_TEMP_ADJT_TB, 48, 22);
      JPanel lpAdjustLane = new JPanel(new FlowLayout(FlowLayout.LEADING));
      lpAdjustLane.setBorder(BorderFactory
        .createTitledBorder(BorderFactory.createEtchedBorder(), "Adjust"));
      lpAdjustLane.add(new JLabel("sampling[s]:"));
      lpAdjustLane.add(O_TEMP_SAMP_TB);
      lpAdjustLane.add(new JSeparator(JSeparator.VERTICAL));
      lpAdjustLane.add(new JLabel("adjusting[s]:"));
      lpAdjustLane.add(O_TEMP_ADJT_TB);
      
      //-- setting pane ** pack
      JPanel lpSettingPane = new JPanel(new GridLayout(3, 1, 1, 1));
      lpSettingPane.add(lpTargetLane);
      lpSettingPane.add(lpRangeLane);
      lpSettingPane.add(lpAdjustLane);
      
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
      O_FRAME.setResizable(false);
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
    pxTarget.addMouseListener(O_INPUT_BOX_LISTENER);
  }//+++
  
  static final
  String ccGetStringByInputBox(String pxBrief, String pxDefault){
    if(!SwingUtilities.isEventDispatchThread()){return "";}
    return JOptionPane.showInputDialog(
      O_FRAME,
      pxBrief, pxDefault
    );
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
  
  private boolean ssIsValidString(String pxLine){
    if(pxLine==null){return false;}
    return !pxLine.isEmpty();
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
    float[] cmDesProcessHistory;
    float cmProcessValue,cmProcessMinimum,cmProcessMaximum;
    float cmTarget,cmShiftedTarget;
    float cmDeadPositive, cmDeadNegative;
    float cmProportionPositive, cmProportionNegative;
    float cmSamplingDead,cmAdjustWidth;
    float cmAnalogOutput;
    //--
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
  
  public static void main(String[] args){
    PApplet.main(CaseSimplePID.class.getCanonicalName());
  }//!!!

}//***eof
