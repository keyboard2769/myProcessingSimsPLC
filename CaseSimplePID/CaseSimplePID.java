/* *
 * Burner and Dryer
 * 
 * a fire-blasting blower, aka the burner, 
 *   heats and dries some aggregates sent to a rolling barrel, aka the dryer.
 *
 * the burner has :
 *  - a ignitor turns its fire on or off. 
 *  - a controllable air damper restrict the power of fire. 
 *  - a varistor measured in percentage indicated as degree of the damper. 
 * 
 * the dryer has :
 *  - a temperature sensor indicates how hot the dryer barrel currently is. 
 *  - a flux scaler in scaler indicates how may aggregate is sent. 
 *  - a cooling damper drains atomsphere air into the dryer barrel on occasion.
 *
 * other control descriptions can be found inside the dialog window.
 * to exit, press the 'q' key.
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
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
  
  private static CaseSimplePID self=null;
  
  //=== swing
  
  //-- swing ** info message
  static final String[] C_DES_MESSAGE = new String[]{
    //[head]:: fill this!
    "uno\n",
    "dos\n",
    "tre!\n"
  };
  
  //-- swing ** top
  static final JFrame O_FRAME = new JFrame(CaseSimplePID.class.getName());
  
  //-- swing ** operation ** combust
  static final JToggleButton     O_OPRT_BFC_SW = new JToggleButton("FIRE");
  static final JButton           O_OPRT_BUC_SW = new JButton("-");
  static final JButton           O_OPRT_BUO_SW = new JButton("+");
  static final JComboBox<String> O_OPRT_BMD_NT = new JComboBox<>(new String[]{
    //[head]:: rotate this!
    "AUTO","DISAB","MANUAL"
  });
  
  //-- swing ** operation ** flux
  static final JButton    O_OPRT_FDD_SW = new JButton("DEC");
  static final JButton    O_OPRT_FPP_SW = new JButton("INC");
  static final JTextField O_OPRT_FXX_TB = new JTextField("000.0");
  
  //-- swing ** operation ** cooling
  static final JTextField        O_OPRT_MVL_TB = new JTextField("000.0");
  static final JComboBox<String> O_OPRT_MVR_NT = new JComboBox<>(new String[]{
    //[head]:: rotate this!
    "DISAB","AUTO","FORCE"
  });
  
  //-- swing ** temperature controller
  static final JTextField O_TEMP_TARGET_TB = new JTextField("000.0");
  static final JTextField O_TEMP_DEAD_TB   = new JTextField("000.0");
  static final JTextField O_TEMP_PORT_TB   = new JTextField("000.0");
  static final JTextField O_TEMP_SAMP_TB   = new JTextField("000.0");
  static final JTextField O_TEMP_ADJT_TB   = new JTextField("000.0");
  
  //=== pipe
  
  //-- pipe ** flags
  static volatile boolean mnBurnerManualCloseFLG  = false;
  static volatile boolean mnBurnerManualOpenFLG   = false;
  static volatile boolean mnBurnerAutoFLG         = true;
  static volatile boolean mnBurnerManualFLG       = false;
  static volatile boolean mnBurnerFireFLG         = false;
  static volatile boolean mnCoolingDamperAutoFLG  = false;
  static volatile boolean mnCoolingDamperForceFLG = false;
  
  //-- pipe ** values ** operating
  static volatile float mnFluxAdjustWidth     =  20.0f;
  static volatile float mnFluxTPH             =   0.0f;
  static volatile float mnCooldownTemperature = 200.0f;
  
  //-- pipe ** values ** controller
  static volatile float mnCTRLTargetCELC     = 120.00f;
  static volatile float mnCTRLDeadFACT       =   0.02f;
  static volatile float mnCTRLProportionFACT =   0.75f;
  static volatile float mnCTRLSamplingSEC    =   0.50f;
  static volatile float mnCTRLAdjustSEC      =  30.00f;
  
  //=== action
  
  static final ActionListener O_NOTCH_LISTENER = new ActionListener() {
    @Override public void actionPerformed(ActionEvent ae) {
      Object lpSouce = ae.getSource();
      
      //-- burner mode
      if(lpSouce.equals(O_OPRT_BMD_NT)){
        int lpIndex = O_OPRT_BMD_NT.getSelectedIndex();
        mnBurnerAutoFLG   = (lpIndex == 0);
        mnBurnerManualFLG = (lpIndex == 2);
      }else//..?
        
      //-- fire set
      if(lpSouce.equals(O_OPRT_BFC_SW)){
        mnBurnerFireFLG=O_OPRT_BFC_SW.isSelected();
      }else//..?
      
      //-- cooling mode
      if(lpSouce.equals(O_OPRT_MVR_NT)){
        int lpIndex = O_OPRT_MVR_NT.getSelectedIndex();
        mnCoolingDamperAutoFLG  = (lpIndex == 1);
        mnCoolingDamperForceFLG = (lpIndex == 2);
      }else//..?
        
      //-- unhandled
      {System.err.println(
        "O_NOTCH_LISTENER::unhandled:"+lpSouce.toString()
      );}//..?
      
    }//+++
  };//***
  
  static final MouseAdapter O_MOMENTARY_LISTENER = new MouseAdapter() {
    @Override public void mousePressed(MouseEvent me) {
      Object lpSource = me.getSource();
      
      //-- degree close 
      if(lpSource.equals(O_OPRT_BUC_SW)){
        mnBurnerManualCloseFLG=true;
      }else//..?
      
      //-- degree open
      if(lpSource.equals(O_OPRT_BUO_SW)){
        mnBurnerManualOpenFLG=true;
      }else//..?
      
      //-- flux decrement
      if(lpSource.equals(O_OPRT_FDD_SW)){
        mnFluxTPH -= mnFluxAdjustWidth;
        mnFluxTPH = PApplet.constrain(mnFluxTPH, 0f, 320f);
      }else//..?
      
      //-- flux increment
      if(lpSource.equals(O_OPRT_FPP_SW)){
        mnFluxTPH += mnFluxAdjustWidth;
        mnFluxTPH = PApplet.constrain(mnFluxTPH, 0f, 320f);
      }else//..?
      
      //-- unhandled
      {System.err.println(
        "O_MOMENTARY_LISTENER::unhandled:"+lpSource.toString()
      );}//..?
      
    }//+++
    @Override public void mouseReleased(MouseEvent me) {
      //-- anyway
      mnBurnerManualCloseFLG=false;
      mnBurnerManualOpenFLG=false;
    }//+++
  };//***
  
  static final MouseAdapter O_INPUT_BOX_LISTENER = new MouseAdapter() {
    @Override public void mouseReleased(MouseEvent me) {
      Object lpSource = me.getSource();
      
      //-- operate ** flux width
      if(lpSource.equals(O_OPRT_FXX_TB)){
        String lpInput = ccGetStringByInputBox(
          "[1 ~ 50]", O_OPRT_FXX_TB.getText()
        );if(lpInput==null){return;}
        float lpParsed = ccToFloat(lpInput);
        mnFluxAdjustWidth = PApplet.constrain(lpParsed, 1f, 50f);
        O_OPRT_FXX_TB.setText(String.format("%03.1f", mnFluxAdjustWidth));
      }else//..?
        
      //-- operate ** cooldown celcius
      if(lpSource.equals(O_OPRT_MVL_TB)){
        String lpInput = ccGetStringByInputBox(
          "[1 ~ 999]", O_OPRT_MVL_TB.getText()
        );if(lpInput==null){return;}
        float lpParsed = ccToFloat(lpInput);
        mnCooldownTemperature = PApplet.constrain(lpParsed, 1f, 999f);
        O_OPRT_MVL_TB.setText(String.format("%03.1f", mnCooldownTemperature));
      }else//..?
        
      //-- temperature ** target
      if(lpSource.equals(O_TEMP_TARGET_TB)){
        String lpInput = ccGetStringByInputBox(
          "[1 ~ 999]", O_TEMP_TARGET_TB.getText()
        );if(lpInput==null){return;}
        float lpParsed = ccToFloat(lpInput);
        mnCTRLTargetCELC = PApplet.constrain(lpParsed, 1f, 999f);
        self.fbTemperatureCTRL.ccSetTargetValue(mnCTRLTargetCELC);
        O_TEMP_TARGET_TB.setText(String.format("%03.1f", mnCTRLTargetCELC));
      }else//..?
      
      //-- temperature ** dead
      if(lpSource.equals(O_TEMP_DEAD_TB)){
        String lpInput = ccGetStringByInputBox(
          "[0.01 ~ 0.99]", O_TEMP_DEAD_TB.getText()
        );if(lpInput==null){return;}
        float lpParsed = ccToFloat(lpInput);
        mnCTRLDeadFACT = PApplet.constrain(lpParsed, 0.01f, 0.99f);
        self.fbTemperatureCTRL.ccSetDead(mnCTRLDeadFACT);
        O_TEMP_DEAD_TB.setText(String.format("%.2f", mnCTRLDeadFACT));
      }else//..?
        
      //-- temperature ** proportion
      if(lpSource.equals(O_TEMP_PORT_TB)){
        String lpInput = ccGetStringByInputBox(
          "[0.01 ~ 0.99]", O_TEMP_PORT_TB.getText()
        );if(lpInput==null){return;}
        float lpParsed = ccToFloat(lpInput);
        mnCTRLProportionFACT = PApplet.constrain(lpParsed, 0.01f, 0.99f);
        self.fbTemperatureCTRL.ccSetProportion(mnCTRLProportionFACT);
        O_TEMP_PORT_TB.setText(String.format("%.2f", mnCTRLProportionFACT));
      }else//..?
        
      //-- temperature ** sampling
      if(lpSource.equals(O_TEMP_SAMP_TB)){
        String lpInput = ccGetStringByInputBox(
          "[00.50 ~ 59.99]", O_TEMP_SAMP_TB.getText()
        );if(lpInput==null){return;}
        float lpParsed = ccToFloat(lpInput);
        mnCTRLSamplingSEC = PApplet.constrain(lpParsed, 0.5f, 59.99f);
        self.fbSamplingClockTimer.ccSetTimer(ccToFrameCount(mnCTRLSamplingSEC));
        O_TEMP_SAMP_TB.setText(String.format("%.2f", mnCTRLSamplingSEC));
      }else//..?
      
      //-- temperature ** adjust
      if(lpSource.equals(O_TEMP_ADJT_TB)){
        String lpInput = ccGetStringByInputBox(
          "[00.50 ~ 59.99]", O_TEMP_ADJT_TB.getText()
        );if(lpInput==null){return;}
        float lpParsed = ccToFloat(lpInput);
        mnCTRLAdjustSEC = PApplet.constrain(lpParsed, 0.5f, 59.99f);
        self.fbAdjustClockTimer.ccSetTimer(ccToFrameCount(mnCTRLAdjustSEC));
        O_TEMP_ADJT_TB.setText(String.format("%.2f", mnCTRLAdjustSEC));
      }else//..?
      
      //-- unhandled
      {System.err.println(
        "O_INPUT_BOX_LISTENER::unhandled:"+lpSource.toString()
      );}//..?
      
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
      JTextArea lpInfoArea = new JTextArea("How ot use:\n");
      lpInfoArea.setEditable(false);
      lpInfoArea.setEnabled(false);
      for(String it : C_DES_MESSAGE){lpInfoArea.append(it);}
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
      
      //-- post
      O_OPRT_FXX_TB.setText(String.format("%03.1f", mnFluxAdjustWidth));
      O_OPRT_MVL_TB.setText(String.format("%03.1f", mnCooldownTemperature));
      O_TEMP_TARGET_TB.setText(String.format("%03.1f", mnCTRLTargetCELC));
      O_TEMP_DEAD_TB.setText(String.format("%.2f", mnCTRLDeadFACT));
      O_TEMP_PORT_TB.setText(String.format("%.2f", mnCTRLProportionFACT));
      O_TEMP_SAMP_TB.setText(String.format("%.2f", mnCTRLSamplingSEC));
      O_TEMP_ADJT_TB.setText(String.format("%.2f", mnCTRLAdjustSEC));
      
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
  
  static final float ccToFloat(String pxText){
    float lpRes;
    try{
      lpRes = Float.parseFloat(pxText);
    }catch(Exception e) {
      lpRes = 0.0f;
    }//..?
    return lpRes;
  }//+++

  //=== overridden
  
  //-- constant
  static final float C_TEMP_BURN_MAX = 800.0f;
  static final float C_TEMP_ATOM_CON =  27.5f;
  static final float C_TEMP_INWD_CON =  23.5f;
  static final float C_TR_SLOW = 256f;
  static final float C_TR_FAST =  64f;
  
  //-- system
  int pbRoller = 0;
  private WindowAdapter cmSkechClosing = new WindowAdapter() {
    @Override public void windowClosing(WindowEvent we) {
      System.out.println(self.getClass().getName()+"::call_exit");
      self.exit();
    }//+++
  };//***
  
  //-- local ui
  EcElement pbBurnerICON      = new EcElement();
  EcElement pbBurnerClosePL   = new EcElement("-");
  EcElement pbBurnerDegreeTB  = new EcElement();
  EcElement pbBurnerOpenPL    = new EcElement("+");
  EcElement pbBurnerIgnitorPL = new EcElement(" ");
  EcElement pbDryerICON       = new EcElement();
  EcElement pbDryerDegreeTB   = new EcElement();
  EcElement pbDryerFluxTB     = new EcElement();
  EcElement pbCoolingDamperPL = new EcElement(" ");
  
  //-- emulated
  ZcFlicker fbSamplingClockTimer = new ZcFlicker(8);
  ZcFlicker fbAdjustClockTimer   = new ZcFlicker(80);
  ZcController fbTemperatureCTRL = new ZcController();
  ZcController fbDegreeCTRL      = new ZcController();
  
  //-- simulated
  boolean dcBurnerCloseMV     = false;
  boolean dcBurnerOpenMV      = false;
  boolean dcBurnerIgnitorMV   = false;
  boolean dcCoolingDamperMV   = false;
  boolean dcColdAggregateLS   = false;
  float   dcDryerFlux         = 160.0f;
  ZcDamper simBurnerDamper        = new ZcDamper();
  ZcReal simBurnerTemperature     = new ZcReal(C_TEMP_INWD_CON,true);
  ZcReal simDryerTemperature      = new ZcReal(C_TEMP_INWD_CON);
  ZcReal simAtomsphereTemperature = new ZcReal(C_TEMP_ATOM_CON,true);
  ZcReal simAggregateTemperature  = new ZcReal(C_TEMP_INWD_CON/2f);
  
  @Override public void setup() {
    
    //-- pre setting
    size(320,240);
    noSmooth();
    self=this;
    frame.setTitle(CaseSimplePID.class.getName());
    frame.addWindowListener(cmSkechClosing);
    
    //-- replace setting
    frameRate(16);noStroke();textAlign(LEFT, TOP);ellipseMode(CENTER);
    
    //-- local ui
    int lpPotentialW, lpPotentialH;
    
    //-- local ui ** burner
    lpPotentialW = 48;
    lpPotentialH = 18;
    pbBurnerClosePL.cmRegion.setBounds(5,25,lpPotentialH,lpPotentialH);
    pbBurnerDegreeTB.cmOffColor=0xFFCCCCCC;
    pbBurnerDegreeTB.cmRegion.setSize(lpPotentialW, lpPotentialH);
    pbBurnerDegreeTB.ccFollowH(pbBurnerClosePL, 2);
    pbBurnerOpenPL.cmRegion.setSize(lpPotentialH, lpPotentialH);
    pbBurnerOpenPL.ccFollowH(pbBurnerDegreeTB, 2);
    lpPotentialW =
      ccGetEndX(pbBurnerOpenPL.cmRegion)
       - pbBurnerClosePL.cmRegion.x;
    lpPotentialH = 
      lpPotentialW *2 /3;
    pbBurnerICON.cmRegion.setSize(lpPotentialW, lpPotentialH);
    pbBurnerICON.ccFollowV(pbBurnerClosePL, 2);
    pbBurnerIgnitorPL.cmOnColor = 0xFFEECC99;
    pbBurnerIgnitorPL.cmRegion.setLocation(
      ccGetEndX(pbBurnerICON.cmRegion) -pbBurnerIgnitorPL.cmRegion.width -2,
      ccGetCenterY(pbBurnerICON.cmRegion) -pbBurnerIgnitorPL.cmRegion.height -2
    );
    
    //-- local ui ** dryer
    lpPotentialH = lpPotentialW;
    lpPotentialW = lpPotentialH*2;
    pbDryerICON.cmRegion.setSize(lpPotentialW, lpPotentialH);
    pbDryerICON.ccFollowE(pbBurnerICON, 5);
    pbDryerICON.cmRegion.y -= pbBurnerICON.cmRegion.height/4;
    lpPotentialW = 54;
    lpPotentialH = 22;
    pbDryerDegreeTB.cmOffColor=0xFFEECCCC;
    pbDryerDegreeTB.cmRegion.setBounds(ccGetCenterX(pbDryerICON.cmRegion),
      ccGetCenterY(pbDryerICON.cmRegion),
      lpPotentialW, lpPotentialH
    );
    lpPotentialW = 60;
    lpPotentialH = 22;
    pbDryerFluxTB.cmOffColor =0xFF999999;
    pbDryerFluxTB.cmOnColor  =0xFFEEEECC;
    pbDryerFluxTB.cmRegion.setSize(lpPotentialW, lpPotentialH);
    pbDryerFluxTB.ccFollowH(pbDryerDegreeTB, 10);
    pbCoolingDamperPL.cmOnColor = 0xFF99CCEE;
    pbCoolingDamperPL.cmRegion.setLocation(
      pbDryerICON.cmRegion.x+pbDryerICON.cmRegion.width*5/6+8,
      pbDryerICON.cmRegion.y+2
    );
    
    //-- swing ui
    SwingUtilities.invokeLater(O_SWING_INIT);
    
    //-- emulated
    fbDegreeCTRL.cmRangeMinimum=  0f;
    fbDegreeCTRL.cmRangeMaximum=100f;
    fbDegreeCTRL.ccSetDead(0.02f);
    fbDegreeCTRL.ccSetProportion(0.10f);
    fbTemperatureCTRL.ccSetTargetValue(mnCTRLTargetCELC);
    fbTemperatureCTRL.ccSetDead(mnCTRLDeadFACT);
    fbTemperatureCTRL.ccSetProportion(mnCTRLProportionFACT);
    fbSamplingClockTimer.ccSetTimer(ccToFrameCount(mnCTRLSamplingSEC));
    fbAdjustClockTimer.ccSetTimer(ccToFrameCount(mnCTRLAdjustSEC));
    
    //-- post
    println(this.getClass().getName()+"::setup_end");
    
  }//++!

  @Override public void draw() {
  
    //-- pre drawing
    background(0);
    pbRoller++;pbRoller&=0x0f;
    
    //-- emulated ** scan ** controller ** clock
    fbSamplingClockTimer.ccRun();
    fbAdjustClockTimer.ccRun();
    
    //-- emulated ** scan ** controller ** temperature
    fbTemperatureCTRL.ccRun(simDryerTemperature.cmVal,
      dcBurnerIgnitorMV & fbSamplingClockTimer.ccGetPulse(),
      dcBurnerIgnitorMV & fbAdjustClockTimer.ccGetPulse()
    );
    
    //-- emulated ** scan ** controller ** degree
    fbDegreeCTRL.ccSetTargetValue(constrain(
      fbTemperatureCTRL.ccGetAnalogOutput()*100f,0f,100f
    ));
    fbDegreeCTRL.ccRun(
      (float)simBurnerDamper.ccToPercentage()
    );
    
    //-- emulated ** scan ** flag
    boolean lpBurnerAutoCloseFLG = sel(
      dcBurnerIgnitorMV,fbDegreeCTRL.ccGetNegativeOutput(),
      sel(simBurnerDamper.ccToPercentage()<=3,false,true)
    );
    boolean lpBurnerAutoOpenFLG = sel(
      dcBurnerIgnitorMV,fbDegreeCTRL.ccGetPositiveOutput(),false
    );
    
    //-- emulated ** scan ** output
    dcBurnerCloseMV = gate(
      mnBurnerAutoFLG, lpBurnerAutoCloseFLG,
      mnBurnerManualFLG, mnBurnerManualCloseFLG
    );
    dcBurnerOpenMV = gate(
      mnBurnerAutoFLG, lpBurnerAutoOpenFLG,
      mnBurnerManualFLG, mnBurnerManualOpenFLG
    );
    dcBurnerIgnitorMV = mnBurnerFireFLG;
    dcCoolingDamperMV = sel(
      mnCoolingDamperAutoFLG,
      (simDryerTemperature.cmVal>mnCooldownTemperature),
      mnCoolingDamperForceFLG
    );
    
    //-- emulated ** simulate ** device
    dcColdAggregateLS = (mnFluxTPH>20.0f);
    simBurnerDamper.ccClose(dcBurnerCloseMV);
    simBurnerDamper.ccOpen(dcBurnerOpenMV);
    
    //-- emulated ** simulate ** real
    simBurnerTemperature.cmVal=sel(dcBurnerIgnitorMV,
      (simBurnerDamper.ccToProportion()+0.02f)*C_TEMP_BURN_MAX,
      C_TEMP_INWD_CON
    );
    if((pbRoller==7) && (random(1f)<0.33f)){
      simAtomsphereTemperature.cmVal=C_TEMP_ATOM_CON*random(0.75f,1.25f);
    }//..?
    ccTransfer(simBurnerTemperature, simDryerTemperature, C_TR_SLOW);
    ccTransfer(simDryerTemperature,simAtomsphereTemperature,
      sel(dcCoolingDamperMV, C_TR_FAST, C_TR_SLOW)
    );
    ccTransfer(simAggregateTemperature, simAtomsphereTemperature, C_TR_SLOW);
    ccTransfer(
      simDryerTemperature, simAggregateTemperature,
      sel(dcColdAggregateLS, map(mnFluxTPH,360f,1f,C_TR_FAST,C_TR_SLOW)/2f, 0f)
    );
    
    //-- bind
    pbBurnerClosePL.cmActivated=dcBurnerCloseMV;
    pbBurnerOpenPL.cmActivated=dcBurnerOpenMV;
    pbBurnerIgnitorPL.cmActivated=dcBurnerIgnitorMV&&(pbRoller%6>4);
    pbCoolingDamperPL.cmActivated=dcCoolingDamperMV;
    pbDryerFluxTB.cmActivated=dcColdAggregateLS;
    
    //-- local ui ** passive
    ccDrawAsBlower(pbBurnerICON);
    ccDrawAsDryer(pbDryerICON);
    
    //-- local ui ** line-inicator
    ccDrawController(fbTemperatureCTRL);
    ccDrawAsLabel(pbBurnerIgnitorPL);
    ccDrawAsLabel(pbCoolingDamperPL);
    
    //-- local ui ** active ** burner
    ccDrawAsLabel(pbBurnerClosePL);
    ccDrawAsValueBox(pbBurnerDegreeTB, simBurnerDamper.ccToPercentage(), "%");
    ccDrawAsLabel(pbBurnerOpenPL);
    
    //-- local ui ** active ** burner
    ccDrawAsValueBox(pbDryerDegreeTB, ceil(simDryerTemperature.cmVal), "`C");
    ccDrawAsValueBox(pbDryerFluxTB, ceil(mnFluxTPH), "tph");
    
    //-- local ui ** active ** controller
    ccSurroundText(
      fbTemperatureCTRL.ccToInicator("Temperature Controller"),
      10, 125
    );
    ccSurroundText(
      fbDegreeCTRL.ccToInicator("Degree Controller"),
      170, 125
    );
    
    //-- watch
    fill(0xFF66CC66);
    text(String.format(
      "[r:%02d]|[m:(%03d,%03d)]|[f:%.2f]",
      pbRoller,mouseX,mouseY,frameRate
    ),5,5);
    
  }//++~

  @Override public void keyPressed() {
    switch(key){
      case java.awt.event.KeyEvent.VK_ESCAPE:
      case 'q':cmSkechClosing.windowClosing(null);break;
      default:break;
    }//..?
  }//+++

  @Override public void mousePressed() {
    switch (mouseButton) {
      case LEFT:return;
      case RIGHT:SwingUtilities.invokeLater(O_SWING_FLIP);break;
      default:break; //+++
    }//..?
  }//+++
  
  //=== utility
  
  static final boolean not(boolean a){
    return !a;
  }//+++
  
  static final boolean and(boolean a, boolean b){
    return a&&b;
  }//+++
  
  static final boolean or(boolean a, boolean b){
    return a||b;
  }//+++
  
  static final boolean sel(boolean c, boolean a, boolean b){
    return c?a:b;
  }//+++
  
  static final float sel(boolean c, float a, float b){
    return c?a:b;
  }//+++
  
  static final boolean gate(boolean ca, boolean a, boolean cb, boolean b){
    return ca?a:(cb?b:false);
  }//+++
  
  static final boolean ccIsValidString(String pxLine){
    if(pxLine==null){return false;}
    return !pxLine.isEmpty();
  }//+++
  
  static final float ccToSecond(int pxFrameCount){
    float lpAmplified = ((float)pxFrameCount)*100f/16f;
    int lpCeiled = ceil(lpAmplified);return ((float)(lpCeiled))/100f;
  }//+++
  
  static final int ccToFrameCount(float pxSecond){
    return (int)(pxSecond*16f);
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
  
  void ccDrawLineH(int pxY){
    line(0,pxY,width,pxY);
  }//+++
  
  void ccDrawController(ZcController pxTarget){
    
    //-- pushing
    final int lpConstG = 5;
    final int lpConstY = height-ceil(pxTarget.cmShiftedTarget);
    final int lpDeadH  = ceil(
      pxTarget.cmDeadPositive - pxTarget.cmDeadNegative
    );
    final int lpPropH  = ceil(
      pxTarget.cmProportionPositive - pxTarget.cmProportionNegative
    );
    
    //-- proportion
    stroke(0xAA339933);
    fill((32+pxTarget.cmAdjustIndication*4)&0xFF,0x66);
    rect(
      lpConstG *1, lpConstY - lpPropH/2,
      width - lpConstG*2, lpPropH
    );
    
    //-- dead
    stroke(0xAA228822);
    fill((32+pxTarget.cmSamplingIndication*4)&0xFF,0x66);
    rect(
      lpConstG *2, lpConstY - lpDeadH/2,
      width - lpConstG*4, lpDeadH
    );
    
    //-- center / target
    stroke(0xAA33EE33);
    ccDrawLineH(lpConstY);
    stroke(0xAAEEEE33);
    ccDrawLineH(height-ceil(pxTarget.cmProcessAverage));
    
    //-- popping
    noStroke();
    
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
    boolean cmActivated=false;
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
      cmActivated=pxAct;
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
      fill(pxTarget.cmActivated?pxTarget.cmOnColor:pxTarget.cmOffColor);
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
  
  //=== real
  
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
  
  //=== controller
  
  class ZcFlicker{
    int cmVal;
    int cmJudge, cmMax;
    ZcFlicker(int pxJudge, int pxMax){ccSetTimer(pxJudge, pxMax);}//++!
    ZcFlicker(int pxMax){ccSetTimer(pxMax, pxMax);}//++!
    void ccRun(){cmVal++;if(cmVal>cmMax){cmVal=0;}}//++~
    synchronized final void ccSetTimer(int pxJudge, int pxMax){
      cmVal=0;
      if(pxMax<3){
        cmJudge=cmMax=3;
      }else{
        cmMax=pxMax;
        cmJudge=constrain(pxJudge, 0, pxMax);
      }//..?
    }//++<
    synchronized final void ccSetTimer(int pxMax){
      ccSetTimer(pxMax, pxMax);
    }//++<
    boolean ccIsAt(int pxCount){return cmVal==pxCount;}//++>
    boolean ccIsAbove(int pxCount){return cmVal>=pxCount;}//++>
    boolean ccGetSquare(){return ccIsAbove(cmJudge);}//++>
    boolean ccGetPulse(){return ccIsAt(cmJudge);}//++>
  }//***
  
  class ZcDamper{
    int cmAD=500;
    int cmSpeed=16;
    void ccOpen(boolean pxCond){
      if(!pxCond){return;}
      cmAD+=cmSpeed;ccLimit();
    }//+++
    void ccClose(boolean pxCond){
      if(!pxCond){return;}
      cmAD-=cmSpeed;ccLimit();
    }//+++
    void ccLimit(){cmAD=constrain(cmAD, 400, 3600);}//+++
    float ccToProportion(){return map((float)cmAD,400f,3600f,0f,1f);}//+++
    int ccToPercentage(){return ceil(map(cmAD,400,3600,0,100));}//+++
    int ccGetAD(){return cmAD;}//+++
  }//***
  
  class ZcController{
    
    boolean cmHistoryAllFilled=false;
    int cmHistoryHead=0;
    float[] cmDesProcessHistory=new float[]{0,0,0,0, 0,0,0,0};
    float cmProcessAverage = 0f;
    float[] cmDesGradientHistory=new float[]{0,0,0,0, 0,0,0,0};
    float cmGradientAverage = 0f;
    float cmProcessValue=0f,cmRangeMinimum=1f,cmRangeMaximum=239f;
    float cmSamplingDead=5f,cmAdjustWidth=1f;
    float cmTarget=0f,cmShiftedTarget=0f;
    float cmDead=0.20f,cmDeadPositive=0f, cmDeadNegative=0f;
    float cmProportion=0.70f,cmProportionPositive=0f, cmProportionNegative=0f;
    float cmAnalogOutput=0.0f;
    
    int cmSamplingIndication = 16;
    int cmAdjustIndication = 16;
    
    //-- ** **
    
    void ccRun(float pxProcessVAl){
      cmAnalogOutput = ccCalculateOutput(pxProcessVAl);
    }//++~
    
    void ccRun(float pxProcessVAL, boolean pxSamplingCLK, boolean pxAdjustCLK){
      
      //-- output
      cmAnalogOutput = ccCalculateOutput(cmProcessAverage);
      
      //-- sampling
      if(pxSamplingCLK){
        ccOfferProcessValue(pxProcessVAL);
        cmSamplingIndication=16;
      }//..?
      if(cmSamplingIndication>0){cmSamplingIndication--;}
      
      //--  adjusting
      boolean lpHistoryCondition = cmHistoryAllFilled
        && (abs(cmGradientAverage)<=cmSamplingDead);
      boolean lpProcessCondition = (cmAnalogOutput != 0f);
      if(pxAdjustCLK && lpHistoryCondition && lpProcessCondition){
          ccAdjustTarget();
          ccCalculateDeadRange();
          ccCalculateProportionRange();
          cmAdjustIndication=16;
      }//..?
      if(cmAdjustIndication>0){cmAdjustIndication--;}
      
    }//++~
    
    //-- ** **
    
    float ccCalculateOutput(float pxReal){
      if(pxReal < cmProportionNegative){
        return 1f;
      }else
      if(pxReal > cmProportionPositive){
        return -1f;
      }else
      if(pxReal > cmDeadPositive){
        return -1f * map(
          pxReal-cmDeadPositive,
          0f,cmProportionPositive-cmDeadPositive,
          0.001f,0.999f
        );
      }else
      if(pxReal < cmDeadNegative){
        return map(
          pxReal-cmProportionNegative,
          cmDeadNegative-cmProportionNegative,0f,
          0.001f,0.999f
        );
      }else{
        return 0f;
      }//..?
    }//+++
    
    void ccOfferProcessValue(float pxValue){
      
      //-- offering
      int lpLogicalPrev = (cmHistoryHead-1)&0x07;
      cmDesProcessHistory[cmHistoryHead]=pxValue;
      cmDesGradientHistory[cmHistoryHead]=
        cmDesProcessHistory[cmHistoryHead]
          - cmDesProcessHistory[lpLogicalPrev];
      cmHistoryHead++;cmHistoryHead&=0x07;
      
      //-- fulfilling
      if(cmHistoryHead==7){cmHistoryAllFilled=true;}
      
      //-- average calculation
      if(cmHistoryAllFilled){
        cmProcessAverage  = cmDesProcessHistory[ cmHistoryHead];
        cmGradientAverage = cmDesGradientHistory[cmHistoryHead];
      }else{
        
        //-- ** process
        cmProcessAverage = 0f;
        for(int i=0;i<8;i++){cmProcessAverage += cmDesProcessHistory[i];}
        cmProcessAverage/=8f;
        
        //-- ** gradient
        cmGradientAverage = 0f;
        for(int i=0;i<8;i++){cmGradientAverage += cmDesGradientHistory[i];}
        cmGradientAverage/=8f;
        
      }//..?
      
    }//+++
    
    void ccAdjustTarget(){
      if(cmProcessAverage<cmTarget){cmShiftedTarget+=cmAdjustWidth;}
      if(cmProcessAverage>cmTarget){cmShiftedTarget-=cmAdjustWidth;}
      cmShiftedTarget=constrain
        (cmShiftedTarget, cmRangeMinimum, cmRangeMaximum);
    }//+++
    
    void ccCalculateDeadRange(){
      cmDeadNegative = cmShiftedTarget
        - (cmRangeMaximum-cmRangeMinimum)*cmDead;
      cmDeadPositive = cmShiftedTarget
        + (cmRangeMaximum-cmRangeMinimum)*cmDead;
    }//+++
    
    void ccCalculateProportionRange(){
      cmProportionNegative = cmShiftedTarget
        - (cmRangeMaximum-cmRangeMinimum)*cmProportion;
      cmProportionPositive = cmShiftedTarget
        + (cmRangeMaximum-cmRangeMinimum)*cmProportion;
    }//+++
    
    //-- ** **
    
    synchronized void ccSetDead(float pxFactor){
      cmDead = constrain(pxFactor, 0.01f, 0.99f);
      ccCalculateDeadRange();
      cmSamplingDead=cmAdjustWidth
       = (cmDeadPositive-cmDeadNegative);//..work around
    }//++<
    
    synchronized void ccSetProportion(float pxFactor){
      cmProportion = constrain(pxFactor, 0.01f, 0.99f);
      ccCalculateProportionRange();
    }//++<
    
    synchronized void ccSetTargetValue(float pxReal){
      cmTarget = constrain(pxReal, cmRangeMinimum, cmRangeMaximum);
      cmShiftedTarget=cmTarget;
      ccResetHistory();
      ccCalculateDeadRange();
      ccCalculateProportionRange();
    }//++<
    
    synchronized void ccResetHistory(){
      cmHistoryAllFilled=false;
      cmHistoryHead=0;
      cmProcessAverage=0f;
      Arrays.fill(cmDesProcessHistory, 0f);
      cmGradientAverage=0f;
      Arrays.fill(cmDesGradientHistory, 0f);
    }//++<
    
    //-- ** **
    
    float ccGetAnalogOutput(){
      return cmAnalogOutput;
    }//++>
    
    boolean ccGetPositiveOutput(){
      return ccGetAnalogOutput() > 0f;
    }//++>
    
    boolean ccGetNegativeOutput(){
      return ccGetAnalogOutput() < 0f;
    }//++>
    
    //-- ** **
    
    String ccToInicator(String pxTitle){
      StringBuilder lpRes = new StringBuilder(pxTitle);
      lpRes.append(':');lpRes.append('\n');
      lpRes.append(String.format("[at:%3.1f][st%3.1f]\n",
        cmTarget, cmShiftedTarget
      ));
      lpRes.append(String.format("[d:(%3.1f,%3.1f)]\n",
        cmDeadNegative, cmDeadPositive
      ));
      lpRes.append(String.format("[p:(%3.1f,%3.1f)]\n",
        cmProportionNegative, cmProportionPositive
      ));
      lpRes.append(String.format("[avp:%3.1f][avg:%3.3f]\n",
        cmProcessAverage, cmGradientAverage
      ));
      lpRes.append(String.format("[opt:%.2f]",
        cmAnalogOutput
      ));
      return lpRes.toString();
    }//++>
    
  }//***

  //=== entry
  
  public static void main(String[] args){
    PApplet.main(CaseSimplePID.class.getCanonicalName());
  }//!!!

}//***eof
