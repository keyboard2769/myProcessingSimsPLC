/*
 * Simple PID (raw)
 *
 */

package ppptest;

import processing.core.PApplet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

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
  
  EcElement pbBurnerICON          = new EcElement();
  EcElement pbBurnerDegreeClosePL = new EcElement("-");
  EcElement pbBurnerDegreeTB      = new EcElement();
  EcElement pbBurnerDegreeOpenPL  = new EcElement("+");
  
  EcElement pbDryerICON     = new EcElement();
  EcElement pbDryerDegreeTB = new EcElement();
  
  @Override public void setup() {
    
    //-- pre setting
    size(320,240);
    noSmooth();
    frame.setTitle(CaseSimplePID.class.getName());
    self=this;
    
    //-- replace setting
    frameRate(16);noStroke();textAlign(LEFT, TOP);ellipseMode(CENTER);
    
    //-- local ui
    int lpPotentialW, lpPotentialH;
    
    //-- local ui ** burner
    lpPotentialW = 48;
    lpPotentialH = 18;
    pbBurnerDegreeClosePL.cmRegion.setBounds(25,25,lpPotentialH,lpPotentialH);
    pbBurnerDegreeTB.cmOffColor=0xFFCCCCCC;
    pbBurnerDegreeTB.cmRegion.setSize(lpPotentialW, lpPotentialH);
    pbBurnerDegreeTB.ccFollowH(pbBurnerDegreeClosePL, 2);
    pbBurnerDegreeOpenPL.cmRegion.setSize(lpPotentialH, lpPotentialH);
    pbBurnerDegreeOpenPL.ccFollowH(pbBurnerDegreeTB, 2);
    lpPotentialW =
      ccGetEndX(pbBurnerDegreeOpenPL.cmRegion)
       - pbBurnerDegreeClosePL.cmRegion.x;
    lpPotentialH = 
      lpPotentialW *2 /3;
    pbBurnerICON.cmRegion.setSize(lpPotentialW, lpPotentialH);
    pbBurnerICON.ccFollowV(pbBurnerDegreeClosePL, 2);
    
    //-- local ui ** dryer
    lpPotentialH = lpPotentialW;
    lpPotentialW = lpPotentialH*2;
    pbDryerICON.cmRegion.setSize(lpPotentialW, lpPotentialH);
    pbDryerICON.ccFollowE(pbBurnerICON, 5);
    pbDryerICON.cmRegion.y -= pbBurnerICON.cmRegion.height/4;
    lpPotentialW = 54;
    lpPotentialH = 22;
    pbDryerDegreeTB.cmOffColor=0xFFCCCCCC;
    pbDryerDegreeTB.cmRegion.setBounds(
      ccGetCenterX(pbDryerICON.cmRegion),
      ccGetCenterY(pbDryerICON.cmRegion),
      lpPotentialW, lpPotentialH
    );
    
    //-- post setting
    SwingUtilities.invokeLater(O_SWING_INIT);
    println("-- setup over");
    
  }//++!

  @Override public void draw() {
  
    //-- pre drawing
    background(0);
    pbRoller++;pbRoller&=0x0f;
    
    //-- local ui
    ccDrawAsLabel(pbBurnerDegreeClosePL);
    ccDrawAsValueBox(pbBurnerDegreeTB, mouseX, "%");
    ccDrawAsLabel(pbBurnerDegreeOpenPL);
    ccDrawAsBlower(pbBurnerICON);
    ccDrawAsDryer(pbDryerICON);
    ccDrawAsValueBox(pbDryerDegreeTB, mouseY, "`C");
    
    //-- controller ** linear
    
    //-- controller ** indication
    ccSurroundText("we will we will \n rock you!", mouseX, mouseY);
    
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
  
  void ssPover(){
    
    //-- default
    println("--exit <- ");
    exit();
  }//+++
  
  boolean ssIsValidString(String pxLine){
    if(pxLine==null){return false;}
    return !pxLine.isEmpty();
  }//+++
  
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
      stroke(0xFFEEEEEE);
      rect(pxX, pxY, lpWidth, lpHeight);
      fill(0xFFEEEEEE);
      text(pxText, pxX+lpMargin, pxY+lpMargin);
    }
    popStyle();
  }//+++
  
  //=== local ui
  
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
    boolean cmActivity=false;
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
      cmActivity=pxAct;
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
      fill(pxTarget.cmActivity?pxTarget.cmOnColor:pxTarget.cmOffColor);
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
    final int lpBerrelH     = pxTarget.cmRegion.height *2 /3;
    final int lpSupporterG  = (pxTarget.cmRegion.height - lpBerrelH)/2;
    final int lpDuctG       = lpSupporterW;
    fill(pxTarget.cmBackColor);
    rect(//..barrel
      pxTarget.cmRegion.x,
      pxTarget.cmRegion.y + lpSupporterG,
      lpBarrelW,
      lpBerrelH
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
      ccGetCenterY(pxTarget.cmRegion)
    );
  }//+++
  
  //[todo]::void ccDrawAsLine(ZcController pxTarget){}//+++
  
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
