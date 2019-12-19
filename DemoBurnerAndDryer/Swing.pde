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

//-- swing ** info message
static final String[] C_DES_MESSAGE = new String[]{
  "## basic usage: \n",
  " 1) turn on the fire.\n",
  " 2) tweek on some flux.\n",
  " 3) watch.\n",
  "## for tweeks : \n",
  " - lower proportion value for uglier chattering.\n",
  " - higher dead value for dumber controlling.\n",
  " - slower sampling for rougher calculating.\n",
  " - fast adjusting for jumpier result.\n",
  " - let the cooling damper help you with a wild setting.\n",
  "## the graph : \n",
  " - the yellow one is the inner process average.\n",
  " - the centered green one is the inner shifted target.\n",
  " - the narrower square is suppsed to be the dead range.\n",
  "   (depends on your setting).\n",
  "   it blinks when a sampling is performed.\n",
  " - the wider square is suppsed to be the proportion range.\n",
  "   (depends on your setting).\n",
  "   it blinks when a adjusting is performed.\n",
  " \n",
  "have fun!\n"
};

//-- swing ** top
static final JFrame O_FRAME = new JFrame();

//-- swing ** operation ** combust
static final JToggleButton     O_OPRT_BFC_SW = new JToggleButton("FIRE");
static final JButton           O_OPRT_BUC_SW = new JButton("-");
static final JButton           O_OPRT_BUO_SW = new JButton("+");
static final JComboBox<String> O_OPRT_BMD_NT = new JComboBox<String>(new String[]{
  "AUTO","DISAB","MANUAL"
});

//-- swing ** operation ** flux
static final JButton    O_OPRT_FDD_SW = new JButton("DEC");
static final JButton    O_OPRT_FPP_SW = new JButton("INC");
static final JTextField O_OPRT_FXX_TB = new JTextField("000.0");

//-- swing ** operation ** cooling
static final JTextField        O_OPRT_MVL_TB = new JTextField("000.0");
static final JComboBox<String> O_OPRT_MVR_NT = new JComboBox<String>(new String[]{
  "DISAB","AUTO","FORCE"
});

//-- swing ** temperature controller
static final JTextField O_TEMP_TARGET_TB = new JTextField("000.0");
static final JTextField O_TEMP_DEAD_TB   = new JTextField("000.0");
static final JTextField O_TEMP_PROP_TB   = new JTextField("000.0");
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
    if(lpSource.equals(O_TEMP_PROP_TB)){
      String lpInput = ccGetStringByInputBox(
        "[0.01 ~ 0.99]", O_TEMP_PROP_TB.getText()
      );if(lpInput==null){return;}
      float lpParsed = ccToFloat(lpInput);
      mnCTRLProportionFACT = PApplet.constrain(lpParsed, 0.01f, 0.99f);
      self.fbTemperatureCTRL.ccSetProportion(mnCTRLProportionFACT);
      O_TEMP_PROP_TB.setText(String.format("%.2f", mnCTRLProportionFACT));
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
    O_OPRT_BFC_SW.setToolTipText(
      "heating / controlling is only activated while the fire is on"
    );
    O_OPRT_BMD_NT.addActionListener(O_NOTCH_LISTENER);
    O_OPRT_BMD_NT.setToolTipText(
      "let the system control the damper or manual or not to control at all"
    );
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
    String lpFluxSwitchInfo =
      "adjust current aggregate flux by step";
    O_OPRT_FDD_SW.setToolTipText(lpFluxSwitchInfo);
    O_OPRT_FPP_SW.setToolTipText(lpFluxSwitchInfo);
    JPanel lpFluxLane = ccCreateLane("Flux");
    lpFluxLane.add(O_OPRT_FDD_SW);
    lpFluxLane.add(O_OPRT_FPP_SW);
    lpFluxLane.add(new JSeparator(JSeparator.VERTICAL));
    lpFluxLane.add(new JLabel("step[tph]:"));
    lpFluxLane.add(O_OPRT_FXX_TB);
    
    //-- operate pane ** cooling lane
    O_OPRT_MVR_NT.addActionListener(O_NOTCH_LISTENER);
    O_OPRT_MVR_NT.setToolTipText(
      "auto means automatically open when actual value is higher than limit"
    );
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
    O_TEMP_TARGET_TB.setToolTipText(
      "degree is controlled based on this value(cause an entire reset)."
    );
    JPanel lpTargetLane = ccCreateLane("Target");
    lpTargetLane.add(new JLabel("value[`C]:"));
    lpTargetLane.add(O_TEMP_TARGET_TB);
    
    //-- setting pane ** range lane
    ccSetupInputBox(O_TEMP_PROP_TB, 48, 22);
    O_TEMP_PROP_TB.setToolTipText(
      "target degree is mapped based on this value"
    );
    ccSetupInputBox(O_TEMP_DEAD_TB, 48, 22);
    O_TEMP_DEAD_TB.setToolTipText(
      "deviation is ignored under a range based on this value"
    );
    JPanel lpRangeLane = ccCreateLane("Range");
    lpRangeLane.add(new JLabel("proportion[x]:"));
    lpRangeLane.add(O_TEMP_PROP_TB);
    lpRangeLane.add(new JSeparator(JSeparator.VERTICAL));
    lpRangeLane.add(new JLabel("dead[x]:"));
    lpRangeLane.add(O_TEMP_DEAD_TB);
    
    //-- setting pane ** adjust lane
    ccSetupInputBox(O_TEMP_SAMP_TB, 48, 22);
    O_TEMP_SAMP_TB.setToolTipText(
      "interval time of sampling action for inner reference value"
    );
    ccSetupInputBox(O_TEMP_ADJT_TB, 48, 22);
    O_TEMP_ADJT_TB.setToolTipText(
      "inverval time of adjusting action for inner target shifting"
    );
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
    JTextArea lpInfoArea = new JTextArea("How to use:\n");
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
    O_FRAME.setTitle(self.getClass().getName());
    O_FRAME.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    O_FRAME.getContentPane().add(lpContentPane);
    O_FRAME.setPreferredSize(new Dimension(320, 240));
    O_FRAME.setResizable(false);
    O_FRAME.setLocation(240,240);
    O_FRAME.pack();
    O_FRAME.setVisible(true);
    
    //-- post
    O_OPRT_FXX_TB.setText(String.format("%03.1f", mnFluxAdjustWidth));
    O_OPRT_MVL_TB.setText(String.format("%03.1f", mnCooldownTemperature));
    O_TEMP_TARGET_TB.setText(String.format("%03.1f", mnCTRLTargetCELC));
    O_TEMP_DEAD_TB.setText(String.format("%.2f", mnCTRLDeadFACT));
    O_TEMP_PROP_TB.setText(String.format("%.2f", mnCTRLProportionFACT));
    O_TEMP_SAMP_TB.setText(String.format("%.2f", mnCTRLSamplingSEC));
    O_TEMP_ADJT_TB.setText(String.format("%.2f", mnCTRLAdjustSEC));
    
  }//+++
};//***

static final Runnable O_SWING_FLIP = new Runnable() {
  @Override public void run() {
    boolean lpNow = O_FRAME.isVisible();
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