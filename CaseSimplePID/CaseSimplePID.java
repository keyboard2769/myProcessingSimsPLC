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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class CaseSimplePID extends PApplet{
  
  private static PApplet self=null;
  
  //=== swing
  
  //-- swing ** top
  static final JFrame O_FRAME = new JFrame(CaseSimplePID.class.getName());
  
  //-- swing ** operation ** combust
  static final JToggleButton     O_OPRT_BFC_SW = new JToggleButton("FIRE");
  static final JButton           O_OPRT_BUC_SW = new JButton("-");
  static final JButton           O_OPRT_BUO_SW = new JButton("+");
  static final JComboBox<String> O_OPRT_BMD_NT = new JComboBox<>(new String[]{
    "AUTO","DISAB","MANUAL"
  });
  //-- swing ** operation ** flux
  static final JButton    O_OPRT_FDD_SW = new JButton("DEC");
  static final JButton    O_OPRT_FPP_SW = new JButton("INC");
  static final JTextField O_OPRT_FXX_TB = new JTextField("000.0");
  //-- swing ** operation ** cooling
  static final JTextField        O_OPRT_MVL_TB = new JTextField("000.0");
  static final JComboBox<String> O_OPRT_MVR_NT = new JComboBox<>(new String[]{
    "AUTO","DISAB","FORCE"
  });
  
  //-- swing ** temperature controller
  static final JTextField O_TEMP_TARGET_TB = new JTextField("000.0");
  static final JTextField O_TEMP_DEAD_TB   = new JTextField("000.0");
  static final JTextField O_TEMP_PORT_TB   = new JTextField("000.0");
  static final JTextField O_TEMP_SAMP_TB   = new JTextField("000.0");
  static final JTextField O_TEMP_ADJT_TB   = new JTextField("000.0");
  
  //-- swing ** degree controller
  
  //=== action
  
  static final ActionListener O_NOTCH_LISTENER = new ActionListener() {
    @Override public void actionPerformed(ActionEvent ae) {
      Object lpSouce = ae.getSource();
      System.err.println(".actionPerformed():not_yet:"+lpSouce.toString());
    }//+++
  };//***
  
  static final MouseAdapter O_MOMENTARY_LISTENER = new MouseAdapter() {
    @Override public void mousePressed(MouseEvent me) {
      System.err.println(".mousePressed():not_yet");
    }//+++
    @Override public void mouseReleased(MouseEvent me) {
      System.err.println(".mouseReleased()::not_yet");
    }//+++
  };//***
  
  static final MouseAdapter O_INPUT_BOX_LISTENER = new MouseAdapter() {
    @Override public void mouseReleased(MouseEvent me) {
      Object lpSource = me.getSource();
      String lpD = ccGetStringByInputBox("??", "000");
      System.err.println(lpSource.toString()+":not_yet:"+lpD);
    }//+++
  };//***
  
  static final Runnable O_SWING_INIT = new Runnable() {
    @Override public void run() {
      
      //-- restyle
      try{
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      }catch(Exception e){
        System.err.println("O_SWING_INIT::"+e.getMessage());
        System.exit(-1);
      }//..?
      
      //-- operate pane
      //-- operate pane ** combust lane
      O_OPRT_BFC_SW.addActionListener(O_NOTCH_LISTENER);
      O_OPRT_BMD_NT.addActionListener(O_NOTCH_LISTENER);
      ccSetupMomentarySwitch(O_OPRT_BUC_SW);
      ccSetupMomentarySwitch(O_OPRT_BUO_SW);
      JPanel lpCombustLane = ccCreateLane("Combust");
      lpCombustLane.add(O_OPRT_BFC_SW);
      lpCombustLane.add(new JSeparator(JSeparator.VERTICAL));
      lpCombustLane.add(O_OPRT_BMD_NT);
      lpCombustLane.add(O_OPRT_BUC_SW);
      lpCombustLane.add(O_OPRT_BUO_SW);
      //-- operate pane ** flux lane
      ccSetupInputBox(O_OPRT_FXX_TB, 48, 22);
      ccSetupMomentarySwitch(O_OPRT_FDD_SW);
      ccSetupMomentarySwitch(O_OPRT_FPP_SW);
      JPanel lpFluxLane = ccCreateLane("Flux");
      lpFluxLane.add(O_OPRT_FDD_SW);
      lpFluxLane.add(O_OPRT_FPP_SW);
      lpFluxLane.add(new JSeparator(JSeparator.VERTICAL));
      lpFluxLane.add(new JLabel("step[tph]:"));
      lpFluxLane.add(O_OPRT_FXX_TB);
      //-- operate pane ** cooling lane
      O_OPRT_MVR_NT.addActionListener(O_NOTCH_LISTENER);
      ccSetupInputBox(O_OPRT_MVL_TB, 48, 22);
      JPanel lpCoolingLane = ccCreateLane("Cooling");
      lpCoolingLane.add(O_OPRT_MVR_NT);
      lpCoolingLane.add(new JSeparator(JSeparator.VERTICAL));
      lpCoolingLane.add(new JLabel("limit[`C]:"));
      lpCoolingLane.add(O_OPRT_MVL_TB);
      //-- operate pane ** pack
      JPanel lpOperatingPane = new JPanel(new GridLayout(3, 1, 1, 1));
      lpOperatingPane.add(lpCombustLane);
      lpOperatingPane.add(lpFluxLane);
      lpOperatingPane.add(lpCoolingLane);      
      
      //-- setting pane
      //-- setting pane ** target lane
      ccSetupInputBox(O_TEMP_TARGET_TB, 64, 22);
      JPanel lpTargetLane = ccCreateLane("Target");
      lpTargetLane.add(new JLabel("value[`C]:"));
      lpTargetLane.add(O_TEMP_TARGET_TB);
      //-- setting pane ** range lane
      ccSetupInputBox(O_TEMP_PORT_TB, 48, 22);
      ccSetupInputBox(O_TEMP_DEAD_TB, 48, 22);
      JPanel lpRangeLane = ccCreateLane("Range");
      lpRangeLane.add(new JLabel("proportion[?]:"));
      lpRangeLane.add(O_TEMP_PORT_TB);
      lpRangeLane.add(new JSeparator(JSeparator.VERTICAL));
      lpRangeLane.add(new JLabel("dead[?]:"));
      lpRangeLane.add(O_TEMP_DEAD_TB);
      //-- setting pane ** adjust lane
      ccSetupInputBox(O_TEMP_SAMP_TB, 48, 22);
      ccSetupInputBox(O_TEMP_ADJT_TB, 48, 22);
      JPanel lpAdjustLane = ccCreateLane("Adjust");
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
      
      //-- info pane
      JPanel lpInfoPane = new JPanel(new BorderLayout(1, 1));
      JTextArea lpInfoArea = new JTextArea("=D=");
      lpInfoArea.setEditable(false);
      lpInfoArea.setEnabled(false);
      lpInfoArea.append("du you reall yknow?");
      lpInfoArea.append("maybe not?");
      lpInfoPane.add(new JScrollPane(lpInfoArea));
      
      //-- tab
      JTabbedPane lpContentPane = new JTabbedPane();
      lpContentPane.add("Operating", lpOperatingPane);
      lpContentPane.add("Temperature", lpSettingPane);
      lpContentPane.add("Info", lpInfoPane);
      
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
  
  static final void ccSetupMomentarySwitch(JButton pxTarget){
    pxTarget.setFocusPainted(false);
    pxTarget.setBackground(Color.decode("#EEEECC"));
    pxTarget.addMouseListener(O_MOMENTARY_LISTENER);
  }//+++
  
  static final JPanel ccCreateLane(String pxTitle){
    JPanel lpRes = new JPanel(new FlowLayout(FlowLayout.LEADING));
    lpRes.setBorder(BorderFactory
      .createTitledBorder(BorderFactory.createEtchedBorder(), pxTitle));
    return lpRes;
  }//+++
  
  static final String ccGetStringByInputBox(String pxBrief, String pxDefault){
    if(!SwingUtilities.isEventDispatchThread()){return "";}
    return JOptionPane.showInputDialog(
      O_FRAME,
      pxBrief, pxDefault
    );
  }//+++

  //=== overridden
  
  //-- system
  int pbRoller = 0;
  
  //-- local ui
  EcElement mnBurnerICON          = new EcElement();
  EcElement mnBurnerDegreeClosePL = new EcElement("-");
  EcElement mnBurnerDegreeTB      = new EcElement();
  EcElement mnBurnerDegreeOpenPL  = new EcElement("+");
  EcElement mnBurnerFirePL    = new EcElement(" ");
  EcElement mnDryerICON       = new EcElement();
  EcElement mnDryerDegreeTB   = new EcElement();
  EcElement mnDryerFluxTB     = new EcElement();
  EcElement mnCoolingDamperPL = new EcElement(" ");
  
  //-- operated
  static volatile boolean mnBurnerManualCloseFLG = false;
  static volatile boolean mnBurnerManualOpenFLG  = false;
  static volatile boolean mnBurnerAutoFLG        = false;
  static volatile boolean mnBurnerManualFLG      = false;
  static volatile boolean mnBurnerFireFLG        = false;
  
  //-- simulated
  boolean dcBurnerCloseMV     = false;
  boolean dcBurnerOpenMV      = false;
  boolean dcBurnerOnFire      = false;
  boolean dcCoolingDamperMV   = false;
  float   dcDryerFlux         = 160.0f;
  ZcReal dcDryerTemperature = new ZcReal(27.0f);
  ZcReal dcAirTemperature   = new ZcReal(27.0f,true);
  
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
    mnBurnerDegreeClosePL.cmRegion.setBounds(5,25,lpPotentialH,lpPotentialH);
    mnBurnerDegreeTB.cmOffColor=0xFFCCCCCC;
    mnBurnerDegreeTB.cmRegion.setSize(lpPotentialW, lpPotentialH);
    mnBurnerDegreeTB.ccFollowH(mnBurnerDegreeClosePL, 2);
    mnBurnerDegreeOpenPL.cmRegion.setSize(lpPotentialH, lpPotentialH);
    mnBurnerDegreeOpenPL.ccFollowH(mnBurnerDegreeTB, 2);
    lpPotentialW =
      ccGetEndX(mnBurnerDegreeOpenPL.cmRegion)
       - mnBurnerDegreeClosePL.cmRegion.x;
    lpPotentialH = 
      lpPotentialW *2 /3;
    mnBurnerICON.cmRegion.setSize(lpPotentialW, lpPotentialH);
    mnBurnerICON.ccFollowV(mnBurnerDegreeClosePL, 2);
    mnBurnerFirePL.cmRegion.setLocation(ccGetEndX(mnBurnerICON.cmRegion) -mnBurnerFirePL.cmRegion.width -2,
      ccGetCenterY(mnBurnerICON.cmRegion) -mnBurnerFirePL.cmRegion.height -2
    );
    
    //-- local ui ** dryer
    lpPotentialH = lpPotentialW;
    lpPotentialW = lpPotentialH*2;
    mnDryerICON.cmRegion.setSize(lpPotentialW, lpPotentialH);
    mnDryerICON.ccFollowE(mnBurnerICON, 5);
    mnDryerICON.cmRegion.y -= mnBurnerICON.cmRegion.height/4;
    lpPotentialW = 54;
    lpPotentialH = 22;
    mnDryerDegreeTB.cmOffColor=0xFFEECCCC;
    mnDryerDegreeTB.cmRegion.setBounds(ccGetCenterX(mnDryerICON.cmRegion),
      ccGetCenterY(mnDryerICON.cmRegion),
      lpPotentialW, lpPotentialH
    );
    lpPotentialW = 60;
    lpPotentialH = 22;
    mnDryerFluxTB.cmOffColor=0xFFEEEECC;
    mnDryerFluxTB.cmRegion.setSize(lpPotentialW, lpPotentialH);
    mnDryerFluxTB.ccFollowH(mnDryerDegreeTB, 10);
    mnCoolingDamperPL.cmRegion.setLocation(
      mnDryerICON.cmRegion.x+mnDryerICON.cmRegion.width*5/6+8,
      mnDryerICON.cmRegion.y+2
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
    //-- local ui ** burner
    ccDrawAsLabel(mnBurnerDegreeClosePL);
    ccDrawAsValueBox(mnBurnerDegreeTB, mouseX, "%");
    ccDrawAsLabel(mnBurnerDegreeOpenPL);
    ccDrawAsBlower(mnBurnerICON);
    ccDrawAsLabel(mnBurnerFirePL);
    //-- local ui ** burner
    ccDrawAsDryer(mnDryerICON);
    ccDrawAsValueBox(mnDryerDegreeTB, mouseY, "`C");
    ccDrawAsValueBox(mnDryerFluxTB, mouseY, "tph");
    ccDrawAsLabel(mnCoolingDamperPL);
    //-- local ui ** controller
    ccSurroundText("TPCTRL\n..this is a dummy\nnot_yet!!",   5, 125);
    ccSurroundText("BDCTRL\n..this is a dummy\nnot_yet!!", 165, 125);
    
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
      stroke(0xFFAAAAAA);
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
  
  //[todo]::void ccDrawAsLine(ZcController pxTarget){}//+++
  
  //=== real
  
  class ZcReal{
    float cmVal;
    boolean cmStatical;
    public ZcReal(){
      cmVal=0.0f;
      cmStatical=false;  
    }//++!
    public ZcReal(float pxInitValue){
      cmVal=pxInitValue;
      cmStatical=false;  
    }//++!
    public ZcReal(float pxInitValue, boolean pxStatical){
      cmVal=pxInitValue;
      cmStatical=pxStatical;  
    }//++!
  }//***
  
  void ccTransfer(ZcReal pxPotentialP, ZcReal pxPotentialN, int pxRatio){
    float lpDiff=pxPotentialP.cmVal-pxPotentialN.cmVal;
    lpDiff/=pxRatio;
    if(!pxPotentialP.cmStatical){pxPotentialP.cmVal+=lpDiff;}
    if(!pxPotentialN.cmStatical){pxPotentialN.cmVal-=lpDiff;}
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
