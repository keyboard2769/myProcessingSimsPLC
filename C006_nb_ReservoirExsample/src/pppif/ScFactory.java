/* --def %revision%
 * #[flag] "version/filename" : issue $ describe,
 * [ ] "": $,,,
 * [ ] "": $,,,
 * [ ] "": $,,,
 * --end
 */

package pppif;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

public class ScFactory {
  
  static final Color
    DIM_WHITE     =Color.decode("#DDDDDD"),
    
    LIGHT_RED     =Color.decode("#CC9999"),
    
    WATER         =Color.decode("#6699CC"),
      
    DARK_YELLOW   =Color.decode("#666633"),
    DARK_PURPLE   =Color.decode("#663366"),
    DARK_ORANGE   =Color.decode("#996633"),
    DARK_BLUE     =Color.decode("#335588"),
    DARK_RED      =Color.decode("#991111"),
    DARK_GREEN    =Color.decode("#119911")
  ;//---
  
  private ScFactory(){}//+++ 
  
  
  //=== === === ===
  //  creator
  //=== === === ===
  
  //== container
  
  //== container ** panel
  
  //== container ** border panel
  
  static final JPanel ccMyBorderPanel(int pxGap)
    {return new JPanel(new BorderLayout(pxGap, pxGap));}//+++
  
  static final JPanel ccMyBorderPanel(int pxGap, Color pxColor,int pxWeight){
    JPanel lpRes=ccMyBorderPanel(pxGap);
    lpRes.setBorder(BorderFactory.createLineBorder(pxColor, pxWeight));
    return lpRes;
  }//+++
  
  static final JPanel ccMyBorderPanel(int pxGap, String pxTitle){
    JPanel lpRes=ccMyBorderPanel(pxGap);
    lpRes.setBorder(BorderFactory.createTitledBorder(pxTitle));
    return lpRes;
  }//+++
  
  //== container ** flow panel
  
  //[PLAN]::ccMyFlowPannel(int pxGap, bolean pxAlignRight)
  static final JPanel ccMyFlowPanel(int pxGap, boolean pxAlignRight){
    return new JPanel(new FlowLayout
      (pxAlignRight?FlowLayout.RIGHT:FlowLayout.LEFT,pxGap, pxGap));
  }//+++
  
  static final JPanel ccMyFlowPanel
    (int pxGap, boolean pxAlignRight, Color pxColor, int pxWeight)
  { JPanel lpRes=ccMyFlowPanel(pxGap, pxAlignRight);
    lpRes.setBorder(BorderFactory.createLineBorder(pxColor, pxWeight));
    return lpRes;
  }//+++
  
  //[PLAN]::ccMyFlowPannel(int pxGap, bolean pxAlignRight, int pxColor, int pxWeight)
  //[PLAN]::ccMyFlowPannel(int pxGap, bolean pxAlignRight, String pxTitle)
  
  //== container ** grid panel
  
  static final JPanel ccMyGripPanel(int pxR, int pxC)
    {return new JPanel(new GridLayout(pxR, pxC));}//+++
  //[PLAN]::ccMyGripPanel(int pxR, int pxC, int pxColor, int pxWeight)
  //[PLAN]::ccMyGripPanel(int pxR, int pxC, String pxTitle)
  
  //== container ** toolbar
  
  static final JToolBar ccMyStuckedToolBar(){
    JToolBar lpRes=new JToolBar("nc");
    lpRes.setFloatable(false);
    lpRes.setRollover(false);
    return lpRes;
  }//+++
  
  //== component
  //== component ** button
  
  static final JButton ccMyCommandButton
    (String pxName, String pxCommand, int pxW, int pxH)
  { JButton lpRes=ccMyCommandButton(pxName, pxCommand);
    lpRes.setPreferredSize(new Dimension(pxW, pxH));
    return lpRes;
  }//+++
  
  static final JButton ccMyCommandButton(String pxName){
    return ccMyCommandButton(pxName, pxName);
  }//+++
  
  static final JButton ccMyCommandButton(String pxName, String pxCommand){
    JButton lpRes=new JButton(pxName);
      lpRes.setBackground(Color.LIGHT_GRAY);
      lpRes.setForeground(Color.DARK_GRAY);
      lpRes.setActionCommand(pxCommand);
      lpRes.setMargin(new Insets(2, 2, 2, 2));
    return lpRes;
  }//+++
  
  //== component ** textfield
  
  static final JTextField ccMyTextLamp(String pxInitText, int pxW, int pxH){
    JTextField lpRes=new JTextField(pxInitText, 0);
    lpRes.setEditable(false);
    lpRes.setEnabled(false);
    lpRes.setBackground(Color.DARK_GRAY);
    lpRes.setDisabledTextColor(Color.GREEN);
    lpRes.setHorizontalAlignment(JTextField.CENTER);
    if(pxW>0 && pxH>0){lpRes.setPreferredSize(new Dimension(pxW, pxH));}
    return lpRes;
  }//+++
  
  static final JTextField ccMyTextContainer(String pxInitText,int pxW,int pxH){
    JTextField lpRes=ccMyTextLamp(pxInitText, pxW, pxH);
    lpRes.setBackground(DIM_WHITE);
    lpRes.setDisabledTextColor(DARK_GREEN);
    lpRes.setHorizontalAlignment(JTextField.RIGHT);
    return lpRes;
  }//+++
  
  static final JTextField ccMyTextBox(String pxInitText,int pxW,int pxH){
    JTextField lpRes=ccMyTextContainer(pxInitText, pxW, pxH);
    lpRes.setDisabledTextColor(Color.DARK_GRAY);
    return lpRes;
  }//+++
  
  //== component ** misc
  
  static final JList ccMyDummyList(){
    DefaultListModel<String> lpModel=new DefaultListModel<>();
    lpModel.addElement("--replace this!!");
    lpModel.addElement("uno");
    lpModel.addElement("!dos");
    lpModel.addElement("tre!");
    JList lpRes=new JList(lpModel);
    return lpRes;
  }//+++
  
  static final JScrollPane ccMyDummyScrolList(int pxW, int pxH){
    JScrollPane lpRes=new JScrollPane(ccMyDummyList());
    lpRes.setPreferredSize(new Dimension(pxW, pxH));
    return lpRes;
  }//+++
  
  //=== === < creator
  
  
  //=== === === ===
  //  dialog
  //=== === === ===
  
  static final boolean ccCheckEDT(){
    if(SwingUtilities.isEventDispatchThread()){return true;}
    System.err.println("--err::out_from_edt");
    return false;
  }//+++
  
  static void ccMessageBox(String pxMessage){
    if(ccCheckEDT())
      {JOptionPane.showMessageDialog(null,pxMessage);}
  }//+++

  static String ccGetStingFromInputBox(String pxBrief, String pxDefault){
    if(ccCheckEDT()){
      String lpRes=JOptionPane.showInputDialog
        (null, pxBrief, pxDefault);
      if(VcConst.ccIsValidString(lpRes)){return lpRes;}
      else{return "<nc/>";}
    }
    return "<oedt/>";
  }//+++
      
  //=== === < dialog
  
  
  //=== === === ===
  //  inner
  //=== === === ===
  
  //== window
  
  static class ScToolBarWindow extends JWindow {
    private JToolBar cmBar;
    private int cmHeight;
    JMenuItem cmHelpMI,cmQuitMI;
    JTextField cmRunPL;
    public ScToolBarWindow() {super();}//+++
    //-- initiator
    void ccInit(String pxTitle, ActionListener pxListener){
      ScToolBarWindow lpWindow = this;
      
      //-- public
      cmBar=ccMyStuckedToolBar();
      
      cmHelpMI=new JMenuItem("help");
      cmHelpMI.setActionCommand("--toolbar-help");
      cmHelpMI.addActionListener(pxListener);
      
      cmQuitMI=new JMenuItem("quit");
      cmQuitMI.setActionCommand("--toolbar-quit");
      cmQuitMI.addActionListener(pxListener);
      
      cmRunPL=ccMyTextLamp("Run", 30,30);
      
      //-- local
      JPopupMenu lpPop = new JPopupMenu();
      lpPop.add(cmHelpMI);
      lpPop.add(cmQuitMI);
      
      cmHeight=20;
      
      JLabel lpTitleLabel= new JLabel("[-] "+pxTitle);
      lpTitleLabel.addMouseListener(new ScFactory.ScMenuListener(lpPop));
      lpTitleLabel.addMouseMotionListener(new MouseMotionAdapter() {
        @Override public void mouseDragged(MouseEvent e) {
          lpWindow.setLocation
            (e.getXOnScreen()-10, e.getYOnScreen()-cmHeight/2);
        }
      });
      
      //-- setup
      cmBar.add(lpTitleLabel);
      cmBar.add(cmRunPL);
      cmBar.addSeparator();
      
      lpWindow.add(cmBar);
    
    }//+++
    void ccAdd(JComponent pxComponent){cmBar.add(pxComponent);}//+++
    void ccAddSeparator(){cmBar.addSeparator();}//+++
    void ccFinish(int pxX, int pxY){
      pack();
      setLocation(pxX, pxY);
      setVisible(true);
      setAlwaysOnTop(true);
      cmHeight=cmBar.getPreferredSize().height;
    }//+++
    //-- modifier
    void ccSetRunLampStatus(boolean pxStatus){
      if(SwingUtilities.isEventDispatchThread()){
        cmRunPL.setBackground(pxStatus?DARK_RED:Color.DARK_GRAY);
      }
    }//+++
  }//***
  
  static class ScTitledWindow extends JWindow{
    
    private boolean cmHasCenter;
    private boolean cmHasEnd;
    private JLabel cmTitle;
  
    public ScTitledWindow(){this(null);}
    public ScTitledWindow(Frame pxOwner) {
      super(pxOwner);
      cmHasCenter=false;
      cmHasEnd=false;
    }//+++
    
    //-- initiator
    
    void ccInit(String pxTitle){
      JWindow lpWindow=this;
      
      Object lpContentPanel = lpWindow.getContentPane();
      if(lpContentPanel instanceof JPanel){
        ((JPanel)lpContentPanel).setBackground(Color.LIGHT_GRAY);
        ((JPanel)lpContentPanel).setBorder(BorderFactory.createEtchedBorder());
      }
      
      cmTitle= new JLabel("[-] "+pxTitle);
      cmTitle.setForeground(Color.DARK_GRAY);
      
      cmTitle.addMouseListener(new MouseAdapter() {
        @Override public void mousePressed(MouseEvent e){
          lpWindow.toFront();
          lpWindow.setAlwaysOnTop(true);
          lpWindow.setAlwaysOnTop(false);
        }
      });
      
      cmTitle.addMouseMotionListener(new MouseMotionAdapter() {
        @Override public void mouseDragged(MouseEvent e) {
          lpWindow.setLocation(e.getXOnScreen()-10, e.getYOnScreen()-10);
        }
      });
      
      Container lpContentPane=lpWindow.getContentPane();
        lpContentPane.add(cmTitle,BorderLayout.PAGE_START);
      //--
    }//+++
    
    final void ccAddCenter(JComponent pxContainer){
      if(!cmHasCenter){
        Container lpContentPane=this.getContentPane();
          lpContentPane.add(pxContainer,BorderLayout.CENTER);
      }cmHasCenter=true;
    }//+++
    
    final void ccAddPageEnd(JComponent pxContainer){
      if(!cmHasEnd){
        Container lpContentPane=this.getContentPane();
          lpContentPane.add(pxContainer,BorderLayout.PAGE_END);
      }cmHasEnd=true;
    }//+++
    
    //-- finisher 
    
    final void ccFinish(boolean pxVisible){
      pack();
      setVisible(pxVisible);
      setAlwaysOnTop(false);
      cmHasCenter=true;
      cmHasEnd=true;
    }
    
    final void ccFinish(boolean pxVisible,int pxX, int pxY){
      ccFinish(pxVisible);
      setLocation(pxX, pxY);
    }//+++
    
    final void ccFinish(boolean pxVisible, JWindow pxTarget, int pxX, int pxY){
      ccFinish(pxVisible);
      ccFollows(pxTarget, pxX, pxY);
    }//+++
    
    //-- modifier
    
    void ccFollows(JWindow pxTarget, int pxOffsetX, int pxOffsetY){
      int lpX=pxOffsetX;
      int lpY=pxOffsetY;
      if(pxTarget!=null){
        lpX+=pxTarget.getX()+(pxOffsetY==0?pxTarget.getWidth():0);
        lpY+=pxTarget.getY()+(pxOffsetX==0?pxTarget.getHeight():0);
      } setLocation(lpX, lpY);
    }//+++
    
    void ccFlip(){
      if(ccCheckEDT()){
        boolean lpNow=isVisible();
        setVisible(!lpNow);
      }
    }//+++
    
    
  }//***
  
  //== view
  
  static class ScStringList extends JScrollPane{
    private final ScStringList cmSelf;
    private final DefaultListModel<String> cmModel;
    private final JList<String> cmList;
    ScStringList(){this(0,0);}
    ScStringList(int pxW, int pxH){
      super();cmSelf=this;
      cmModel=new DefaultListModel<>();
      cmList=new JList<>(cmModel);
      cmList.setEnabled(false);
      cmSelf.setViewportView(cmList);
      if (pxW>0 && pxH>0) {
        cmSelf.setPreferredSize(new Dimension(pxW, pxH));
      }
    }//+++
    final void ccAdd(String pxElement){
      cmModel.addElement(pxElement);
    }//+++
    final void ccAddListSelectionListener(ListSelectionListener pxListener){
      cmList.addListSelectionListener(pxListener);
    }//+++
    final void ccAddMouseListener(MouseListener pxListener){
      cmList.addMouseListener(pxListener);
    }//+++
    final void ccRefreshModel(String[] pxList){
      cmModel.clear();
      for(String it:pxList){cmModel.addElement(it);}
    }//+++
    //-- ** teller
    final int ccTellCurrentIndex() {
      return cmList.getSelectedIndex();
    }//+++
    final String ccTellCurrentSelected(){
      return cmModel.get(ccTellCurrentIndex());
    }//+++
    final Object ccGetEventSource(){return cmList;}//+++
  }//***
  
  static class ScTable extends JScrollPane{
    protected final ScTable cmSelf;
    protected final JTable cmTable;
    ScTable(TableModel pxModel, int pxW, int pxH) {
      super();cmSelf=this;
      cmTable=new JTable(pxModel);
      cmTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      cmSelf.setViewportView(cmTable);
      if (pxW>0 && pxH>0)
        {cmSelf.setPreferredSize(new Dimension(pxW, pxH));}
    }//+++
    
    final void ccSetColor(Color pxFore, Color pxBack, Color pxGrid){
      cmTable.setForeground(pxFore);
      cmTable.setBackground(pxBack);
      cmTable.setGridColor(pxGrid);
    }//+++
    
    final void ccSetEnabled(boolean pxState){cmTable.setEnabled(pxState);}//+++
    
    protected final int ccFixIndex(int pxIndex){
      int lpMasked=pxIndex&0xFFFF;
      int lpSize=cmTable.getColumnModel().getColumnCount();
      return lpMasked>lpSize?lpSize-1:lpMasked;
    }//+++
    
    void ccSetColumnWidth(int pxIndex, int pxWidth){
      TableColumn lpColumn= cmTable.getColumnModel()
        .getColumn(ccFixIndex(pxIndex));
      lpColumn.setMinWidth(0x04);
      lpColumn.setMaxWidth(0xFF);
      lpColumn.setPreferredWidth(pxWidth);
    }//+++
    
    void ccSetModel(TableModel pxModel){cmTable.setModel(pxModel);}//+++
    
  }//***
  
  static class ScOutputConsole extends JScrollPane{
    private final ScOutputConsole cmSelf;
    private final JTextArea cmArea;
    public ScOutputConsole(int pxR, int pxC){
      super();cmSelf=this;
      cmArea=new JTextArea("--standby::\n", pxR, pxC);
      cmArea.setEditable(false);
      cmArea.setEnabled(false);
      cmArea.setBackground(Color.BLACK);
      cmArea.setDisabledTextColor(Color.GREEN);
      cmSelf.setViewportView(cmArea);
    }//+++
    final void ccStack(String pxLine){
      if(ccCheckEDT()){
        cmArea.append(pxLine+"\n");
        int lpLength=cmArea.getText().length();
        cmArea.setSelectionStart(lpLength-2);
        cmArea.setSelectionEnd(lpLength-1);
      }
    }//+++
  }//***
  
  //== group
  
  static interface SiAdditionListener{
    void ccFire();
  }//***
  
  static class ScThreeNotch extends JPanel implements ActionListener{
    private final ScThreeNotch cmSelf;
    private SiAdditionListener cmClickedAsWhole;
    private int cmState;
    protected final JButton
      cmUpSW,cmMidSW,cmLowSW;
    public ScThreeNotch(String pxTitle, String pxUpName, String pxMidName,String pxLowName) {
      super(new GridLayout(0, 1));
      cmSelf = this;
      //--
      cmState=0;
      //--
      cmUpSW=ScFactory.ccMyCommandButton(pxUpName, "upper",75,18);
      cmUpSW.addActionListener(cmSelf);
      cmSelf.add(cmUpSW);
      //--
      cmMidSW=ScFactory.ccMyCommandButton(pxMidName, "middie",75,18);
      cmMidSW.addActionListener(cmSelf);
      cmSelf.add(cmMidSW);
      //--
      cmLowSW=ScFactory.ccMyCommandButton(pxLowName, "lower",75,18);
      cmLowSW.addActionListener(cmSelf);
      cmSelf.add(cmLowSW);
      //--
      ccRefresh();
      if(pxTitle!=null){
        cmSelf.setBorder(BorderFactory.createTitledBorder(pxTitle));
      }
      //--
      cmClickedAsWhole=null;
    }//+++
    void ccSetAdditionalListenter(SiAdditionListener pxListener) {
      cmClickedAsWhole = pxListener;
    }//+++
    final void ccSetState(int pxState){cmState=pxState;}//---
    final int ccGetState(){return cmState;}//+++
    protected final void ccRefresh(){
      cmUpSW.setBackground(cmState==1?Color.YELLOW:Color.LIGHT_GRAY);
      cmMidSW.setBackground(cmState==0?Color.YELLOW:Color.LIGHT_GRAY);
      cmLowSW.setBackground(cmState==2?Color.YELLOW:Color.LIGHT_GRAY);
    }//+++
    @Override public void actionPerformed(ActionEvent e) {
      String lpCommand=e.getActionCommand();
      switch(lpCommand){
        case "upper":cmState=1;break;
        case "middie":cmState=0;break;
        case "lower":cmState=2;break;
        default:
          System.err.println(
            ".ScThreeNotch.actionPerformed()::unhandled_command"
            +lpCommand
          );
        break;
      }
      ccRefresh();
      if(cmClickedAsWhole!=null){
        cmClickedAsWhole.ccFire();
      }
    }//+++
  }//***
  
  static class ScTwoNotch extends ScThreeNotch{
    public ScTwoNotch(String pxTitle, String pxUpName, String pxLowName) {
      super(pxTitle, pxUpName, "<nc/>", pxLowName);
      super.removeAll();
      Dimension lpSize=new Dimension(75, 27);
      cmUpSW.setPreferredSize(lpSize);
      cmLowSW.setPreferredSize(lpSize);
      super.add(cmUpSW);
      super.add(cmLowSW);
      ccSetState(1);
      ccRefresh();
    }
  }//***
  
  static class ScLampedThreeNotch extends ScThreeNotch{
    private Color cmLampColor;
    protected final JTextField cmLamp;
    public ScLampedThreeNotch(String pxTitle,String pxLampText) {
      super(pxTitle, "x", "#", "o");
      cmLampColor=DARK_GREEN;
      cmLamp=ccMyTextLamp(pxLampText, 75, 27);
      Dimension lpSize=new Dimension(25, 27);
      cmUpSW.setPreferredSize(lpSize);
      cmMidSW.setPreferredSize(lpSize);
      cmLowSW.setPreferredSize(lpSize);
      super.removeAll();
      super.setLayout(new BorderLayout());
      super.add(cmLamp,BorderLayout.PAGE_START);
      super.add(cmUpSW,BorderLayout.LINE_START);
      super.add(cmMidSW,BorderLayout.CENTER);
      super.add(cmLowSW,BorderLayout.LINE_END);
      ccRefresh();
    }
    final void ccSetLampColor(Color pxColor){cmLampColor=pxColor;}//+++
    final void ccSetLampStatus(boolean pxStatus){
      cmLamp.setBackground(pxStatus?cmLampColor:Color.DARK_GRAY);
    }//+++
  }//***
  
  static class ScLampedTwoNotch extends ScLampedThreeNotch{
    public ScLampedTwoNotch(String pxTitle, String pxLampText) {
      super(pxTitle, pxLampText);
      super.removeAll();
      Dimension lpSize=new Dimension(37, 27);
      cmUpSW.setPreferredSize(lpSize);
      cmLowSW.setPreferredSize(lpSize);
      super.add(cmLamp,BorderLayout.PAGE_START);
      super.add(cmUpSW,BorderLayout.LINE_START);
      super.add(cmLowSW,BorderLayout.LINE_END);
      ccSetState(1);
      ccRefresh();
    }//+++
  }//***
  
  static class ScFeederGroup extends JPanel implements ActionListener{
    private static ScFeederGroup cmSelf;
    private SiAdditionListener cmClickedAsWhole;
    private int cmRPM,cmState,cmShiftSpeed;
    private final JButton cmChangeSW,cmDecSW,cmIncSW,cmAutoSW,cmDisabSW,cmRunSW;
    private final JTextField cmSpeedTC,cmTonperTC;
    private boolean cmIsStuck, cmIsRunning;
    public ScFeederGroup(String pxTitle) {
      super(new FlowLayout());
      cmSelf=this;
      super.setBorder(BorderFactory.createEtchedBorder());
      cmClickedAsWhole=null;
      
      cmChangeSW=ScFactory.ccMyCommandButton("!", "change", 20, 20);
      cmChangeSW.addActionListener(cmSelf);
      cmDecSW=ScFactory.ccMyCommandButton("<", "dec", 20, 20);
      cmDecSW.addActionListener(cmSelf);
      cmIncSW=ScFactory.ccMyCommandButton(">", "inc", 20, 20);
      cmIncSW.addActionListener(cmSelf);
      cmAutoSW=ScFactory.ccMyCommandButton("#", "auto", 20, 20);
      cmAutoSW.addActionListener(cmSelf);
      cmDisabSW=ScFactory.ccMyCommandButton("x", "disab", 20, 20);
      cmDisabSW.addActionListener(cmSelf);
      cmRunSW=ScFactory.ccMyCommandButton("o", "run", 20, 20);
      cmRunSW.addActionListener(cmSelf);
      cmSpeedTC=ScFactory.ccMyTextBox("#### r/m", 64, 20);
      cmTonperTC=ScFactory.ccMyTextContainer("###.# t/h", 64, 20);
      
      super.add(new JLabel(pxTitle));
      super.add(cmChangeSW);
      super.add(cmDecSW);
      super.add(cmSpeedTC);
      super.add(cmIncSW);
      super.add(new JSeparator());
      super.add(cmAutoSW);
      super.add(cmDisabSW);
      super.add(cmRunSW);
      super.add(new JSeparator());
      super.add(cmTonperTC);
      
      cmRPM=0;
      cmState=1;
      cmShiftSpeed=100;
      
      cmIsStuck=false;
      cmIsRunning=false;
      
      ccRefresh();
      
    }//+++
    void ccSetAdditionalListenter(SiAdditionListener pxListener)
      {cmClickedAsWhole = pxListener;}//+++
    final void ccSetStucked(boolean pxState){cmIsStuck=pxState;}//+++
    final void ccSetRunning(boolean pxState){cmIsRunning=pxState;}//+++
    final void ccSetShiftSpeed(int pxSpeed){cmShiftSpeed=pxSpeed;}//+++
    protected final void ccShiftSpeed(int pxOffset){
      cmRPM+=pxOffset;
      cmRPM=cmRPM<=0?0:cmRPM;
      cmRPM=cmRPM>=1800?1800:cmRPM;
    }//+++
    protected final void ccRefresh(){
      cmChangeSW.setBackground(
        cmIsStuck?Color.RED:(
          cmIsRunning?Color.GREEN:Color.LIGHT_GRAY
        )
      );
      cmSpeedTC.setText(Integer.toString(cmRPM)+"r/m");
      cmAutoSW.setBackground(cmState==1?Color.YELLOW:Color.LIGHT_GRAY);
      cmDisabSW.setBackground(cmState==0?Color.YELLOW:Color.LIGHT_GRAY);
      cmRunSW.setBackground(cmState==2?Color.YELLOW:Color.LIGHT_GRAY);
    }//+++
    //-- interface
    @Override public void actionPerformed(ActionEvent e) {
      String lpCommand=e.getActionCommand();
      switch(lpCommand){
        
        case "dec":ccShiftSpeed(-1*cmShiftSpeed);break;
        case "inc":ccShiftSpeed(   cmShiftSpeed);break;
        
        case "auto":cmState=1;break;
        case "disab":cmState=0;break;
        case "run":cmState=2;break;
        
        default:
          System.err.println(".ScFeederGroup.actionPerformed()::unhandled:"
          +lpCommand);
        break;
      }
      ccRefresh();
      if(cmClickedAsWhole!=null){cmClickedAsWhole.ccFire();}
    }//+++
  }//***
  
  //== misc
  
  static class ScMenuListener extends MouseAdapter {
    JPopupMenu cmPop;
    ScMenuListener(JPopupMenu pxPop) {cmPop = pxPop;}
    @Override public void mousePressed(MouseEvent e) {ccCheckIfShowsPop(e);}
    @Override public void mouseReleased(MouseEvent e) {ccCheckIfShowsPop(e);}
    private void ccCheckIfShowsPop(MouseEvent e) {
      if (e.isPopupTrigger()) {
        cmPop.show(e.getComponent(),
                   e.getX(), e.getY());
      }
    }
  }//*** *
  
  //=== === < inner
  
}//***EOF

