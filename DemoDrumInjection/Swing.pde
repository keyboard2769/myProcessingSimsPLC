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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
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
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

//-- swing ** info message
static final String[] C_DES_MESSAGE = new String[]{
  "## basic usage: \n",
  " \n",
  "#### operating pannel: \n",
  "- [momentary]aggregate.++ : increment aggregate ton-per-hour value. \n",
  "- [momentary]aggregate.-- : decrement aggregate ton-per-hour value. \n",
  "- [box]addtive.bias: work with aggregate ton-per-hour value \n",
  "- [box]additve.high-bound: filter to aggregate ton-per-hour ratio \n",
  "- [box]additve.low-bound: filter to aggregate ton-per-hour ratio \n",
  "- [alternate]mannual.feeder: the mannual start-stop button. \n",
  "- [alternate]mannual.pump: the mannual start-stop button. \n",
  "#### setting pannel: \n",
  "- [scalor]feeder: between ton-per-hour and roll-per-minute value. \n",
  "- [scalor]pump: between ton-per-hour and roll-per-minute value. \n",
  "- [box]adjust.step: to the aggregate increment and decrement button. \n",
  "- [box]adjust.delay: after critical parameter change. \n",
  " \n",
  "have fun!\n"
};

static volatile String vmInputDialogBrief = "(?)";
static volatile String vmInputDialogDefault = "...";
static volatile String vmLastInputted = "";

//-- swing ** components

static final JFrame O_FRAME = new JFrame();

static final JTextField O_AGG_SUP_TB = new JTextField(" 000TPH ");

static final JButton O_AGG_INC_SW = new JButton("++");

static final JButton O_AGG_DEC_SW = new JButton("--");

static final JTextField O_APG_BIAS_TB  = new JTextField(" 000% ");
  
static final JTextField O_APG_BIASL_TB = new JTextField(" 000% ");

static final JTextField O_APG_BIASH_TB = new JTextField(" 000% ");

static final JToggleButton O_MNG_FED_SW = new JToggleButton("Feeder");

static final JToggleButton O_MNG_ADP_SW = new JToggleButton("Pump");

static final List<JTextField> O_LES_AGG_MAPPER = 
  Collections.unmodifiableList(Arrays.asList(
    new JTextField(" 0000RPM "),new JTextField(" 0000TPH "),
    new JTextField(" 0000RPM "),new JTextField(" 0000TPH ")
  ));

static final List<JTextField> O_LES_APG_MAPPER = 
  Collections.unmodifiableList(Arrays.asList(
    new JTextField(" 0000RPM "),new JTextField(" 0000TPH "),
    new JTextField(" 0000RPM "),new JTextField(" 0000TPH ")
  ));

static final JTextField O_ADJ_STEP_TB = new JTextField(" 000T ");

static final JTextField O_DELAY_TIME_TB = new JTextField(" 000S ");

//-- swing ** action

static final Runnable T_INPUT_GETTING = new Runnable() {
  @Override public void run() {
    if(!SwingUtilities.isEventDispatchThread())
      {System.err.println(".$ OEDT!!");return;}
    vmLastInputted = JOptionPane.showInputDialog(
      O_FRAME,
      vmInputDialogBrief, vmInputDialogDefault
    );
  }//+++
};//***eof

static final ActionListener M_NOTCH_LISTENER = new ActionListener() {
  @Override public void actionPerformed(ActionEvent ae) {
    Object lpSouce = ae.getSource();
    
    if(lpSouce.equals(O_MNG_FED_SW)){
      vmFeederSW=O_MNG_FED_SW.isSelected();
      vmDelayTimeACTIVATOR=vmDelayTimeFRAME;
      return;
    }//..?
    
    if(lpSouce.equals(O_MNG_ADP_SW)){
      vmPumpSW=O_MNG_ADP_SW.isSelected();
      return;
    }//..?
    
    //-- unhandled
    {System.err.println(
      "O_NOTCH_LISTENER::unhandled:"+lpSouce.toString()
    );}//..?

  }//+++
};//***

static final MouseAdapter M_MOMENTARY_LISTENER = new MouseAdapter() {
  @Override public void mousePressed(MouseEvent me) {
    Object lpSource = me.getSource();
    
    if(lpSource.equals(O_AGG_INC_SW)){
      vmFeederSettingTPH+=vmFeederAdjustStep;
      vmFeederSettingTPH=PApplet.constrain(vmFeederSettingTPH, 0f, C_TPH_MAX);
      ssRefreshAggregateTPH();
      vmDelayTimeACTIVATOR=vmDelayTimeFRAME;
      return;
    }//..?
    
    if(lpSource.equals(O_AGG_DEC_SW)){
      vmFeederSettingTPH-=vmFeederAdjustStep;
      vmFeederSettingTPH=PApplet.constrain(vmFeederSettingTPH, 0f, C_TPH_MAX);
      ssRefreshAggregateTPH();
      vmDelayTimeACTIVATOR=vmDelayTimeFRAME;
      return;
    }//..?
    
    //-- unhandled
    System.err.println(
      "O_MOMENTARY_LISTENER::unhandled:"+lpSource.toString()
    );

  }//+++
  @Override public void mouseReleased(MouseEvent me) {

    //-- reset bit

  }//+++
};//***

static final MouseAdapter M_INPUT_BOX_LISTENER = new MouseAdapter() {
  @Override public void mouseReleased(MouseEvent me) {
    Object lpSource = me.getSource();

    //if(lpSource.equals(...)){vm...=...;T_INPUT_GETTING.Run();
    
    //-- unfilling
    
    if(lpSource.equals(O_AGG_SUP_TB)){
      return;
    }//..?
    
    //-- bias
    
    if(lpSource.equals(O_APG_BIAS_TB)){
      vmInputDialogBrief = "to current aggregate suppliment";
      vmInputDialogDefault = PApplet.nf(ceil(vmAdditveBias*100f),2);
      T_INPUT_GETTING.run();
      float lpRaw = (float)(ccParseInteger(vmLastInputted));
      lpRaw=PApplet.constrain(lpRaw, 10f, 190f);
      vmAdditveBias = lpRaw/100f;
      ssRefreshAdditiveBias();
      vmDelayTimeACTIVATOR=vmDelayTimeFRAME;
      return;
    }//..?
    
    if(lpSource.equals(O_APG_BIASH_TB)){
      vmInputDialogBrief = "filter high limit";
      vmInputDialogDefault = PApplet.nf(ceil(vmAdditveBiasH*100f),2);
      T_INPUT_GETTING.run();
      float lpRaw = (float)(ccParseInteger(vmLastInputted));
      lpRaw=PApplet.constrain(lpRaw, 0f, 100f);
      lpRaw = lpRaw/100f;
      if(lpRaw<=vmAdditveBiasL){
        JOptionPane.showMessageDialog(O_FRAME,
          "high bound value must be bigger than low bound",
          "<!>", JOptionPane.ERROR_MESSAGE
        );
        return;
      }//..?
      vmAdditveBiasH=lpRaw;
      ssRefreshAdditiveBias();
      return;
    }//..?
    
    if(lpSource.equals(O_APG_BIASL_TB)){
      vmInputDialogBrief = "filter low limit";
      vmInputDialogDefault = PApplet.nf(ceil(vmAdditveBiasL*100f),2);
      T_INPUT_GETTING.run();
      float lpRaw = (float)(ccParseInteger(vmLastInputted));
      lpRaw=PApplet.constrain(lpRaw, 0f, 100f);
      lpRaw = lpRaw/100f;
      if(lpRaw>=vmAdditveBiasH){
        JOptionPane.showMessageDialog(O_FRAME,
          "low bound value must be smaller than high bound",
          "<!>", JOptionPane.ERROR_MESSAGE
        );
        return;
      }//..?
      vmAdditveBiasL=lpRaw;
      ssRefreshAdditiveBias();
      return;
    }//..?
    
    //-- feeder scale
    
    if(lpSource.equals(O_LES_AGG_MAPPER.get(0))){
      vmInputDialogBrief = "motor speed zero value";
      vmInputDialogDefault = PApplet.nfc(vmFeederRPMZero,0);
      T_INPUT_GETTING.run();
      float lpRaw = ccParseFloat(vmLastInputted);
      lpRaw=PApplet.constrain(lpRaw, 0f, C_RPM_MAX);
      if(lpRaw>=vmFeederRPMSpan){
        JOptionPane.showMessageDialog(O_FRAME,
          "zero value must be lesser than span value",
          "<!>", JOptionPane.ERROR_MESSAGE
        );
        return;
      }//..?
      vmFeederRPMZero = lpRaw;
      ssRefreshFeederScaler();
      return;
    }//..?
    
    if(lpSource.equals(O_LES_AGG_MAPPER.get(2))){
      vmInputDialogBrief = "motor speed span value";
      vmInputDialogDefault = PApplet.nfc(vmFeederRPMSpan,0);
      T_INPUT_GETTING.run();
      float lpRaw = ccParseFloat(vmLastInputted);
      lpRaw=PApplet.constrain(lpRaw, 0f, C_RPM_MAX);
      if(lpRaw<=vmFeederRPMZero){
        JOptionPane.showMessageDialog(O_FRAME,
          "span value must be greater than zero value",
          "<!>", JOptionPane.ERROR_MESSAGE
        );
        return;
      }//..?
      vmFeederRPMSpan = lpRaw;
      ssRefreshFeederScaler();
      return;
    }//..?
    
    if(lpSource.equals(O_LES_AGG_MAPPER.get(1))){
      vmInputDialogBrief = "aggregate suppliment zero value";
      vmInputDialogDefault = PApplet.nfc(vmFeederTPHZero,1);
      T_INPUT_GETTING.run();
      float lpRaw = ccParseFloat(vmLastInputted);
      lpRaw=PApplet.constrain(lpRaw, 0f, C_TPH_MAX);
      if(lpRaw>=vmFeederTPHSpan){
        JOptionPane.showMessageDialog(O_FRAME,
          "zero value must be lesser than span value",
          "<!>", JOptionPane.ERROR_MESSAGE
        );
        return;
      }//..?
      vmFeederTPHZero = lpRaw;
      ssRefreshFeederScaler();
      return;
    }//..?
    
    if(lpSource.equals(O_LES_AGG_MAPPER.get(3))){
      vmInputDialogBrief = "aggregate suppliment span value";
      vmInputDialogDefault = PApplet.nfc(vmFeederTPHSpan,1);
      T_INPUT_GETTING.run();
      float lpRaw = ccParseFloat(vmLastInputted);
      lpRaw=PApplet.constrain(lpRaw, 0f, C_TPH_MAX);
      if(lpRaw<=vmFeederTPHZero){
        JOptionPane.showMessageDialog(O_FRAME,
          "span value must be greater than zero value",
          "<!>", JOptionPane.ERROR_MESSAGE
        );
        return;
      }//..?
      vmFeederTPHSpan = lpRaw;
      ssRefreshFeederScaler();
      return;
    }//..?
    
    //-- pump scaler
    
    if(lpSource.equals(O_LES_APG_MAPPER.get(0))){
      vmInputDialogBrief = "motor speed zero value";
      vmInputDialogDefault = PApplet.nfc(vmPumpRPMZero,0);
      T_INPUT_GETTING.run();
      float lpRaw = ccParseFloat(vmLastInputted);
      lpRaw=PApplet.constrain(lpRaw, 0f, C_RPM_MAX);
      if(lpRaw>=vmPumpRPMSpan){
        JOptionPane.showMessageDialog(O_FRAME,
          "zero value must be lesser than span value",
          "<!>", JOptionPane.ERROR_MESSAGE
        );
        return;
      }//..?
      vmPumpRPMZero = lpRaw;
      ssRefreshPumpScaler();
      return;
    }//..?
    
    if(lpSource.equals(O_LES_APG_MAPPER.get(2))){
      vmInputDialogBrief = "motor speed span value";
      vmInputDialogDefault = PApplet.nfc(vmPumpRPMSpan,0);
      T_INPUT_GETTING.run();
      float lpRaw = ccParseFloat(vmLastInputted);
      lpRaw=PApplet.constrain(lpRaw, 0f, C_RPM_MAX);
      if(lpRaw<=vmPumpRPMZero){
        JOptionPane.showMessageDialog(O_FRAME,
          "span value must be greater than zero value",
          "<!>", JOptionPane.ERROR_MESSAGE
        );
        return;
      }//..?
      vmPumpRPMSpan = lpRaw;
      ssRefreshPumpScaler();
      return;
    }//..?
    
    if(lpSource.equals(O_LES_APG_MAPPER.get(1))){
      vmInputDialogBrief = "additive flux zero value";
      vmInputDialogDefault = PApplet.nfc(vmPumpTPHZero,1);
      T_INPUT_GETTING.run();
      float lpRaw = ccParseFloat(vmLastInputted);
      lpRaw=PApplet.constrain(lpRaw, 0f, C_TPH_MAX);
      if(lpRaw>=vmPumpTPHSpan){
        JOptionPane.showMessageDialog(O_FRAME,
          "zero value must be lesser than span value",
          "<!>", JOptionPane.ERROR_MESSAGE
        );
        return;
      }//..?
      vmPumpTPHZero = lpRaw;
      ssRefreshPumpScaler();
      return;
    }//..?
    
    if(lpSource.equals(O_LES_APG_MAPPER.get(3))){
      vmInputDialogBrief = "additive flux span value";
      vmInputDialogDefault = PApplet.nfc(vmPumpTPHSpan,1);
      T_INPUT_GETTING.run();
      float lpRaw = ccParseFloat(vmLastInputted);
      lpRaw=PApplet.constrain(lpRaw, 0f, C_TPH_MAX);
      if(lpRaw<=vmPumpTPHZero){
        JOptionPane.showMessageDialog(O_FRAME,
          "span value must be greater than zero value",
          "<!>", JOptionPane.ERROR_MESSAGE
        );
        return;
      }//..?
      vmPumpTPHSpan = lpRaw;
      ssRefreshPumpScaler();
      return;
    }//..?
    
    //-- adjusting 
    
    if(lpSource.equals(O_ADJ_STEP_TB)){
      vmInputDialogBrief = "step of incre/decrement";
      vmInputDialogDefault = PApplet.nfc(vmFeederAdjustStep,0);
      T_INPUT_GETTING.run();
      float lpRaw = (float)(ccParseInteger(vmLastInputted));
      vmFeederAdjustStep = PApplet.constrain(lpRaw, 1f, 10f);
      ssRefreshAdjusting();
      return;
    }//..?
    
    if(lpSource.equals(O_DELAY_TIME_TB)){
      vmInputDialogBrief = "from send-out to arrival";
      vmInputDialogDefault = Integer.toString(vmDelayTimeSEC);
      T_INPUT_GETTING.run();
      int lpRaw = ccParseInteger(vmLastInputted);
      vmDelayTimeSEC = PApplet.constrain(lpRaw, 1, 99);
      vmDelayTimeFRAME = ccToFrameCount((float)vmDelayTimeSEC);
      ssRefreshAdjusting();
      return;
    }//..?
    
    //-- unhandled
    System.err.println(
      "O_INPUT_BOX_LISTENER::unhandled:"+lpSource.toString()
    );//..?

  }//+++
};//***

//-- swing ** steup

static final Runnable T_SWING_INIT = new Runnable() {
  @Override public void run() {
    
    //-- restyle
    try{
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }catch(Exception e){
      System.err.println("O_SWING_INIT::"+e.getMessage());
      System.exit(-1);
    }//..?
    
    //-- ui setup
    ccSetupInputBox(O_AGG_SUP_TB);
    O_AGG_SUP_TB.setDisabledTextColor(Color.DARK_GRAY);
    ccSetupInputBox(O_APG_BIAS_TB);
    ccSetupInputBox(O_APG_BIASH_TB);
    ccSetupInputBox(O_APG_BIASL_TB);
    ccSetupInputBox(O_DELAY_TIME_TB);
    ccSetupInputBox(O_ADJ_STEP_TB);
    ccSetupMomentarySwitch(O_AGG_INC_SW);
    ccSetupMomentarySwitch(O_AGG_DEC_SW);
    O_MNG_FED_SW.addActionListener(M_NOTCH_LISTENER);
    O_MNG_ADP_SW.addActionListener(M_NOTCH_LISTENER);
    
    //-- aggregate group
    JPanel lpAggregatePane = new JPanel(new GridLayout(4, 1));
    lpAggregatePane.setBorder(BorderFactory.createTitledBorder("Aggregate"));
    lpAggregatePane.add(new JLabel("Suppliment:"));
    lpAggregatePane.add(O_AGG_SUP_TB);
    lpAggregatePane.add(O_AGG_INC_SW);
    lpAggregatePane.add(O_AGG_DEC_SW);
    
    //-- additive pump group
    JPanel lpPumpPane = new JPanel(new GridLayout(3, 2));
    lpPumpPane.setBorder(BorderFactory.createTitledBorder("Additive"));
    lpPumpPane.add(new JLabel("Bias:"));
    lpPumpPane.add(O_APG_BIAS_TB);
    lpPumpPane.add(new JLabel("High Bound:"));
    lpPumpPane.add(O_APG_BIASH_TB);
    lpPumpPane.add(new JLabel("Low Bound:"));
    lpPumpPane.add(O_APG_BIASL_TB);
    
    //-- mannual group
    JPanel lpMannualPane = new JPanel(new GridLayout(1, 2));
    lpMannualPane.setBorder(BorderFactory.createTitledBorder("Mannual"));
    lpMannualPane.add(O_MNG_FED_SW);
    lpMannualPane.add(O_MNG_ADP_SW);
    
    //-- operate pane
    JPanel lpOperatingPane = new JPanel(new BorderLayout(1,1));
    lpOperatingPane.add(lpAggregatePane,BorderLayout.LINE_START);
    lpOperatingPane.add(lpPumpPane,BorderLayout.CENTER);
    lpOperatingPane.add(lpMannualPane,BorderLayout.PAGE_END);
    
    //-- scaler lane
    for(int i=0;i<4;i++){
      ccSetupInputBox(O_LES_AGG_MAPPER.get(i));
      ccSetupInputBox(O_LES_APG_MAPPER.get(i));
    }//..~
    JPanel lpFeederScalerLane = new JPanel();
    JPanel lpPumpScalerLane = new JPanel();
    ccSetupSclaerLane(lpFeederScalerLane, O_LES_AGG_MAPPER, "Feeder");
    ccSetupSclaerLane(lpPumpScalerLane, O_LES_APG_MAPPER, "Pump");
    
    //-- adjust lane
    JPanel lpAdjustLane 
      = new JPanel(new FlowLayout(FlowLayout.LEADING, 1, 1));
    lpAdjustLane.setBorder(BorderFactory.createTitledBorder("Adjust"));
    lpAdjustLane.add(new JLabel("Step:"));
    lpAdjustLane.add(O_ADJ_STEP_TB);
    lpAdjustLane.add(new JSeparator(SwingConstants.VERTICAL));
    lpAdjustLane.add(new JLabel("Delay:"));
    lpAdjustLane.add(O_DELAY_TIME_TB);
    
    //-- setting pane
    JPanel lpSettingPane = new JPanel(new GridLayout(4,1));
    lpSettingPane.add(lpFeederScalerLane);
    lpSettingPane.add(lpPumpScalerLane);
    lpSettingPane.add(lpAdjustLane);
    
    //-- info pane
    JPanel lpInfoPane = new JPanel(new BorderLayout(1, 1));
    JTextArea lpInfoArea = new JTextArea("How to use:\n");
    lpInfoArea.setEditable(false);
    lpInfoArea.setEnabled(false);
    for(String it : C_DES_MESSAGE){lpInfoArea.append(it);}
    lpInfoPane.add(new JScrollPane(lpInfoArea));
    
    //-- content pane
    JTabbedPane lpContentPane = new JTabbedPane();
    lpContentPane.add("Operating",lpOperatingPane);
    lpContentPane.add("Setting",lpSettingPane);
    lpContentPane.add("Info",lpInfoPane);
    
    //-- pack
    O_FRAME.setTitle(self.getClass().getName());
    O_FRAME.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    O_FRAME.getContentPane().add(lpContentPane);
    O_FRAME.setPreferredSize(new Dimension(320, 240));
    O_FRAME.setResizable(false);
    O_FRAME.setLocation(240,240);
    O_FRAME.pack();
    O_FRAME.setVisible(true);
    
    //-- post
    ssRefreshAggregateTPH();
    ssRefreshAdditiveBias();
    ssRefreshFeederScaler();
    ssRefreshPumpScaler();
    ssRefreshAdjusting();
    O_MNG_FED_SW.requestFocus();
    self.frame.requestFocus();
    vmDelayTimeFRAME = ccToFrameCount((float)vmDelayTimeSEC);
    
  }//+++
};//***

static final Runnable T_SWING_FLIP = new Runnable() {
  @Override public void run() {
    boolean lpNow = O_FRAME.isVisible();
    O_FRAME.setVisible(!lpNow);
  }//+++
};//***

//=== refresh

static final void ssRefreshAggregateTPH(){
  O_AGG_SUP_TB.setText(PApplet.nfc(vmFeederSettingTPH, 0)+"t/h");
}//+++

static final void ssRefreshAdditiveBias(){
  O_APG_BIAS_TB.setText(PApplet.nf(ceil(vmAdditveBias*100f),2)+"%");
  O_APG_BIASH_TB.setText(PApplet.nf(ceil(vmAdditveBiasH*100f),2)+"%");
  O_APG_BIASL_TB.setText(PApplet.nf(ceil(vmAdditveBiasL*100f),2)+"%");
}//+++

static final void ssRefreshFeederScaler(){
  O_LES_AGG_MAPPER.get(0).setText(PApplet.nfc(vmFeederRPMZero,0)+"r/m");
  O_LES_AGG_MAPPER.get(1).setText(PApplet.nfc(vmFeederTPHZero,1)+"t/h");
  O_LES_AGG_MAPPER.get(2).setText(PApplet.nfc(vmFeederRPMSpan,0)+"r/m");
  O_LES_AGG_MAPPER.get(3).setText(PApplet.nfc(vmFeederTPHSpan,1)+"t/h");
}//+++

static final void ssRefreshPumpScaler(){
  O_LES_APG_MAPPER.get(0).setText(PApplet.nfc(vmPumpRPMZero,0)+"r/m");
  O_LES_APG_MAPPER.get(1).setText(PApplet.nfc(vmPumpTPHZero,2)+"t/h");
  O_LES_APG_MAPPER.get(2).setText(PApplet.nfc(vmPumpRPMSpan,0)+"r/m");
  O_LES_APG_MAPPER.get(3).setText(PApplet.nfc(vmPumpTPHSpan,2)+"t/h");
}//+++

static final void ssRefreshAdjusting(){
  O_ADJ_STEP_TB.setText(PApplet.nfc(vmFeederAdjustStep,0)+"T");
  O_DELAY_TIME_TB.setText(Integer.toString(vmDelayTimeSEC)+"S");
}//+++

//=== factory

static final void ccSetupInputBox(JTextField pxTarget){
  pxTarget.setEditable(false);
  pxTarget.setEnabled(false);
  pxTarget.setColumns(5);
  pxTarget.setBackground(Color.decode("#EEEEEE"));
  pxTarget.setForeground(Color.DARK_GRAY);
  pxTarget.setDisabledTextColor(Color.decode("#336699"));
  pxTarget.setHorizontalAlignment(JTextField.RIGHT);
  pxTarget.addMouseListener(M_INPUT_BOX_LISTENER);
}//+++

static final void ccSetupMomentarySwitch(JButton pxTarget){
  pxTarget.setFocusPainted(false);
  pxTarget.setBackground(Color.decode("#E1E1E1"));
  pxTarget.setForeground(Color.decode("#339933"));
  pxTarget.addMouseListener(M_MOMENTARY_LISTENER);
}//+++

static final
void ccSetupSclaerLane(JPanel pxLane, List<JTextField> pxMap, String pxTitle){
  pxLane.setLayout(new FlowLayout(FlowLayout.LEADING, 1, 1));
  pxLane.setBorder(BorderFactory.createTitledBorder(pxTitle));
  pxLane.add(pxMap.get(0));
  pxLane.add(pxMap.get(1));
  pxLane.add(new JSeparator(SwingConstants.VERTICAL));
  pxLane.add(new JLabel("->"));
  pxLane.add(new JSeparator(SwingConstants.VERTICAL));
  pxLane.add(pxMap.get(2));
  pxLane.add(pxMap.get(3));
}//+++

//***eof