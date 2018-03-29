/* --def %revision%
 * #[flag] "version/filename" : issue $ describe,
 * [ ] "": $,,,
 * [ ] "": $,,,
 * [ ] "": $,,,
 * --end
 */

package pppif;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.HashMap;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;
import javax.swing.JTabbedPane;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.table.DefaultTableModel;

public class MainTab extends PApplet{
  
  
  //=== === === ===
  //  utility
  //=== === === ===
  
  boolean fnIsKeyPressed(char pxKey){return keyPressed && (key==pxKey);}//+++
  
  //=== === < utility
  
  
  //=== === === ===
  //  data bank
  //=== === === ===
  
  class McCoordinator{
    
    volatile int cmErrorCount=0;
    
    volatile int cmOnDelayTime=8;
    volatile int cmOffDelayTime=8;
    
    volatile boolean
      mnRunSW,mnRunPL,
      mnLowCut,mnUpCut
    ;
    
    McFactory.McErrorFolder cmErrorFolder;
    McFactory.McImpulseTimer cmErrorClearSwitchTimer
      = new McFactory.McImpulseTimer();
    
    public McCoordinator() {
      
      cmErrorFolder=new McFactory.McErrorFolder();
      cmErrorFolder.ccAdd(5, "high_bound_error", "<nc/>");
      cmErrorFolder.ccAdd(6, "low_bound_error", "<nc/>");
      cmErrorFolder.ccAdd(7, "limit_malfunc_error", "<nc/>");
      
    }//+++
    
  }//***
  
  //=== === < data bank
  
  
  //=== === === ===
  //  local ui
  //=== === === ===
  
  class EcCoordinator extends EcFactory.EcBaseCoordinator{
    
    EcFactory.EcGauge cmTankLevel;
    
    EcFactory.EcElement
      cmUpBoundPL,cmUpperPL,
      cmLowBoundPL,cmLowerPL
    ;//---
    
    EcPumpIcon cmPumpPL;
    
    public EcCoordinator() {
      super();
      
      //== shape layer
      
      EcFactory.EcHopperShape lpTank 
        = new EcFactory.EcHopperShape(100, 125, 0.2f);
      lpTank.ccSetLocation(50, 50);
      ccAddShape(lpTank);
      
      EcFactory.EcShape lpTankFunnel
        =new EcFactory.EcShape(50, 60);
      lpTankFunnel.ccSetLocation(75, 175);
      ccAddShape(lpTankFunnel);
      
      EcFactory.EcLineShape lpDuctA
        =new EcFactory.EcLineShape
          (lpTank.ccEndX()-20, lpTank.cmY, 0, -10, 8f);
      ccAddShape(lpDuctA);
      
      EcFactory.EcLineShape lpDuctB
        =new EcFactory.EcLineShape
          (lpDuctA.ccEndX(), lpDuctA.ccEndY(), 40, 0, 8f);
      ccAddShape(lpDuctB);
      
      EcFactory.EcLineShape lpDuctC
        =new EcFactory.EcLineShape
          (lpDuctB.ccEndX(), lpDuctB.ccEndY(), 0, 160, 8f);
      ccAddShape(lpDuctC);
      
      EcFactory.EcLineShape lpDuctD
        =new EcFactory.EcLineShape
          (lpDuctC.ccEndX(), lpDuctC.ccEndY(), 150, 0, 8f);
      ccAddShape(lpDuctD);
      
      //== element layer
      
      cmPumpPL = new  EcPumpIcon("Pump", 24, 24);
      cmPumpPL.ccFollows(lpDuctD, 35, -11);
      cmPumpPL.ccSetNameAlign('a');
      cmPumpPL.ccSetColor(0xFF99EEEE, 0xFF777777);
      ccAddElement(cmPumpPL);
      
      cmTankLevel = new EcFactory.EcGauge("Tank", 8, 99);
      cmTankLevel.ccFollows(lpTank, 18, 5);
      cmTankLevel.ccSetColor(0xFFCC9999, 0xFF99EEEE);
      cmTankLevel.ccSetNameAlign('a');
      ccAddElement(cmTankLevel);
      
      cmUpBoundPL = new EcFactory.EcLamp("SQ4");
      cmUpBoundPL.ccSetText("L");
      cmUpBoundPL.ccSetNameAlign('l');
      cmUpBoundPL.ccSetColor(0xFF99EE99, 0xFF336633);
      cmUpBoundPL.ccFollows(lpTank, 1, 2);
      ccAddElement(cmUpBoundPL);
      
      int lpLevlerGapY = 12;
      
      cmUpperPL = new EcFactory.EcLamp("SQ3");
      cmUpperPL.ccCloneStyle(cmUpBoundPL);
      cmUpperPL.ccFollows(cmUpBoundPL, 0, lpLevlerGapY);
      ccAddElement(cmUpperPL);
      
      cmLowerPL = new EcFactory.EcLamp("SQ2");
      cmLowerPL.ccCloneStyle(cmUpBoundPL);
      cmLowerPL.ccFollows(cmUpperPL, 0, lpLevlerGapY);
      ccAddElement(cmLowerPL);
      
      cmLowBoundPL = new EcFactory.EcLamp("SQ1");
      cmLowBoundPL.ccCloneStyle(cmUpBoundPL);
      cmLowBoundPL.ccFollows(cmLowerPL, 0, lpLevlerGapY);
      ccAddElement(cmLowBoundPL);
      
    }//+++
  }//***
  
  //== local ui ** icon
  
  class EcPumpIcon extends EcFactory.EcLamp{
    float cmHeading;
    public EcPumpIcon(String pxName, int pxW, int pxH){
      super(pxName, pxW, pxH);
      cmHeading=0f;
    }//+++
    @Override void ccUpdate(){
      ccDrawRoundLampAtCenter();
      ccDrawName(EcFactory.C_LABEL_TEXT);
      ccDrawRollingBar(0xFF333333);
    }//+++
    protected void ccDrawRollingBar(int pxColor){
      if(ccGetActivity())
        {cmHeading-=0.3f;if(cmHeading>=PI){cmHeading=0f;}}
      pushMatrix();{
        translate(ccCenterX(),ccCenterY());
        rotate(cmHeading);
        fill(pxColor);
        int lpH=cmH-8;
        rect(-2,-lpH/2,4,lpH);
      }popMatrix();
    }//+++
  }//***
  
  //</editor-fold>
  //=== === < local ui
  
  
  //=== === === ===
  //  swing ui
  //=== === === ===
  
  class ScCoordinator{
    
    final JButton
      cmToolbarOperateSW,cmToolbarErrorSW,cmErrorClearSW
    ;//---
    
    final JToggleButton cmLowerMalfuncSW,cmUpperMalfuncSW;
    
    final JTextField
      cmLimitSwitchOnDelayTB,cmLimitSwitchOffDelayTB,
      cmErrorSumTB
    ;//---
    
    final ScFactory.ScToolBarWindow cmToolBarWindow;
    
    final ScFactory.ScTitledWindow
      cmOperateWindow,cmErrorWindow;
    ;//---
    
    final ScFactory.ScStringList cmErrorList;
    
    final ScFactory.ScOutputConsole cmErrorConsole;
    
    final ScFactory.ScLampedTwoNotch cmRunSW;
        
    final ScActionManager cmActionManager;
    final ScMouseClickManager cmClickManager;
    
    public ScCoordinator() {
      
      //-- builed events
      
      cmActionManager=new ScActionManager(MainFrame.pbSelf);
      cmClickManager=new ScMouseClickManager(MainFrame.pbSelf);
      
      //-- winodw
      
      JPanel lpDummyPane=null;
      
      //-- winodw ** toolbar window
      
      cmToolbarOperateSW=ScFactory
        .ccMyCommandButton("operate", "--toolbar-operate");
      cmToolbarOperateSW.addActionListener(cmActionManager);
      
      cmToolbarErrorSW=ScFactory
        .ccMyCommandButton("error", "--toolbar-error");
      cmToolbarErrorSW.addActionListener(cmActionManager);
      
      cmLowerMalfuncSW=new JToggleButton("!LMF");
      cmLowerMalfuncSW.setForeground(ScFactory.DARK_YELLOW);
      cmLowerMalfuncSW.setBackground(ScFactory.WATER);
      
      cmUpperMalfuncSW=new JToggleButton("!UMF");
      cmUpperMalfuncSW.setForeground(ScFactory.DARK_YELLOW);
      cmUpperMalfuncSW.setBackground(ScFactory.WATER);
      
      cmToolBarWindow=new ScFactory.ScToolBarWindow();
      cmToolBarWindow.ccInit("reservoir:if v1.x ",cmActionManager);
      cmToolBarWindow.ccAddSeparator();
      cmToolBarWindow.ccAdd(cmToolbarOperateSW);
      cmToolBarWindow.ccAdd(cmToolbarErrorSW);
      cmToolBarWindow.ccAddSeparator();
      cmToolBarWindow.ccAdd(cmLowerMalfuncSW);
      cmToolBarWindow.ccAdd(cmUpperMalfuncSW);
      cmToolBarWindow.ccFinish(100, 100);
      
      //-- winodw ** operate window
      
      cmRunSW= new ScFactory.ScLampedTwoNotch("pump", "run");
      
      cmLimitSwitchOnDelayTB =new JTextField(" ## S");
      cmLimitSwitchOnDelayTB.setHorizontalAlignment(JTextField.RIGHT);
      cmLimitSwitchOnDelayTB.addFocusListener(new FocusAdapter() {
        @Override public void focusLost(FocusEvent pxE){
          String lpText=cmLimitSwitchOnDelayTB.getText();
          if(VcConst.ccIsFloatString(lpText)){
            float lpValue=Float.valueOf(lpText);
            MainFrame.pbSelf.pbDataCoordinator.cmOnDelayTime=
              McFactory.ccToFrameBySecond(lpValue);
            cmLimitSwitchOnDelayTB.setText(lpText);
          }else{cmErrorConsole.ccStack("--invalid input");}
          cmLimitSwitchOnDelayTB.setText(
            McFactory.ccToSecondByFrame(
              MainFrame.pbSelf.pbDataCoordinator.cmOnDelayTime
            )
          );
        }//+++
      });
      
      cmLimitSwitchOffDelayTB=new JTextField(" ## S");
      cmLimitSwitchOffDelayTB.setHorizontalAlignment(JTextField.RIGHT);
      cmLimitSwitchOffDelayTB.addFocusListener(new FocusAdapter() {
        @Override public void focusLost(FocusEvent pxE){
          String lpText=cmLimitSwitchOffDelayTB.getText();
          if(VcConst.ccIsFloatString(lpText)){
            float lpValue=Float.valueOf(lpText);
            MainFrame.pbSelf.pbDataCoordinator.cmOffDelayTime=
              McFactory.ccToFrameBySecond(lpValue);
            cmLimitSwitchOffDelayTB.setText(lpText);
          }else{cmErrorConsole.ccStack("--invalid input");}
          cmLimitSwitchOffDelayTB.setText(
            McFactory.ccToSecondByFrame(
              MainFrame.pbSelf.pbDataCoordinator.cmOffDelayTime
            )
          );
        }//+++
      });
      
      JPanel lpInputPane = ScFactory.ccMyGripPanel(0, 1);
      lpInputPane.add(new JLabel("on_delay_time[s]:"));
      lpInputPane.add(cmLimitSwitchOnDelayTB);
      lpInputPane.add(new JLabel("off_delay_time[s]:"));
      lpInputPane.add(cmLimitSwitchOffDelayTB);
      
      lpDummyPane=ScFactory.ccMyFlowPanel(1, false,Color.LIGHT_GRAY,2);
      lpDummyPane.add(cmRunSW);
      lpDummyPane.add(lpInputPane);
      
      cmOperateWindow=new ScFactory.ScTitledWindow(frame);
      cmOperateWindow.ccInit("Operate");
      cmOperateWindow.ccAddCenter(lpDummyPane);
      cmOperateWindow.ccFinish(true, cmToolBarWindow ,0, 10);
      
      //-- error window
      
      cmErrorConsole = new ScFactory.ScOutputConsole(8, 32);
      cmErrorList = new ScFactory.ScStringList(cmErrorConsole.getWidth(), 70);
      cmErrorList.ccAdd("dummy error one");
      cmErrorList.ccAdd("dummy error two");
      cmErrorList.ccAdd("dummy error three");
      
      lpDummyPane = ScFactory.ccMyBorderPanel(1, Color.LIGHT_GRAY, 2);
      lpDummyPane.add(cmErrorList,BorderLayout.PAGE_START);
      lpDummyPane.add(cmErrorConsole,BorderLayout.CENTER);
      
      cmErrorClearSW=ScFactory.ccMyCommandButton("clear", "--error-clear");
      cmErrorClearSW.addActionListener(cmActionManager);
      cmErrorSumTB=ScFactory.ccMyTextContainer(" ###", 70, 20);
      JPanel lpErrorStatusPane = ScFactory.ccMyFlowPanel(1, false);
      lpErrorStatusPane.add(cmErrorClearSW);
      lpErrorStatusPane.add(new JLabel("  ErrorSum:"));
      lpErrorStatusPane.add(cmErrorSumTB);
      
      cmErrorWindow=new ScFactory.ScTitledWindow(frame);
      cmErrorWindow.ccInit("Error");
      cmErrorWindow.ccAddCenter(lpDummyPane);
      cmErrorWindow.ccAddPageEnd(lpErrorStatusPane);
      cmErrorWindow.ccFinish(true, cmOperateWindow, 0, 10);
      
      //-- register event
      
    }//+++
    
    //-- inner class
    
    class ScActionManager implements ActionListener{
      private final MainFrame cmOwner;
      public ScActionManager(MainFrame pxOwner) {cmOwner = pxOwner;}//+++
      @Override public void actionPerformed(ActionEvent e) {
        String lpCommand=e.getActionCommand();
        
        switch(lpCommand){
          
          case "--toolbar-operate":
            cmOperateWindow.ccFlip();
            cmToolbarOperateSW.setForeground(
              cmOperateWindow.isVisible()?
              Color.YELLOW:Color.DARK_GRAY
            );
          break;
          
          case "--toolbar-error":
            cmErrorWindow.ccFlip();
            cmToolbarErrorSW.setForeground(
              cmErrorWindow.isVisible()?
              Color.YELLOW:Color.DARK_GRAY
            );
          break;
          
          case "--error-clear":
            MainFrame.pbSelf.pbLineKeeper.ccSet(VcKeeper.COMMANDS.ERROR_CLEAR);
          break;
          
          case "--toolbar-help":
            ScFactory.ccMessageBox("i-Field FrameWork v1.x");
          break;
          
          case "--toolbar-quit":
            MainFrame.pbSelf.pbLineKeeper.ccSet(VcKeeper.COMMANDS.EXIT);
          break;
          
          default:
            System.err.println
              (".ScActionManager.actionPerformed()::unhandled_command:"
              +lpCommand);
          break;
        }
      }//+++
    }//***
    
    class ScMouseClickManager extends MouseAdapter{
      private final MainFrame cmOwner;
      private final HashMap<Object, String> cmComponentMap;
      public ScMouseClickManager(MainFrame pxOwner) {
        cmOwner = pxOwner;
        cmComponentMap=new HashMap<>();
      }//+++
      final void ccPut(Object pxComponent, String pxDistinction)
        {cmComponentMap.put(pxComponent,pxDistinction);}//+++
      //-- interface
      @Override public void mouseClicked(MouseEvent e) {
        String lpDistinction=cmComponentMap.get(e.getSource());
        if(lpDistinction!=null){switch(lpDistinction){
          
          case "--neverReadched":
            cmOwner.fsPover();
          break;
          
          default:
            System.err.println(".ScMouseClickManager.mouseClicked()::"
            +"unhandled:"+lpDistinction);
          break;
        }}
      }//+++
    }//***
    
  }//***
  
  //=== === < swing ui
  
  
  //=== === === ===
  //  PLC
  //=== === === ===
  
  class ZcCPUZero extends ZcFactory.ZcPLC{
    
    /* --def %IOLayout%
     *  
     * [B] 0123| 4567| 89AB| CDEF
     * ->PLC
     * +0:  TST-B,,, | ,,, | ,,RUN-SW, | ,,,ECLR-SW
     * +1:  ,,, | ,,, | ,,, | ,,,
     * +2:  ,CUT-LL,CUT-UU, | ,,, | ,,, | ,,,
     * +3:  ,,, | ,,, | ,,, | ,,,
     *  
     *     0123| 4567| 89AB| CDEF
     * PLC->
     * +4:  D-FLK,,, | ,,, | ,,RUN-PL, | ,,,
     * +5:  ,,, | ,H-ERR,L-ERR,LS-ERR | ,,, | ,,,
     * +6:  LB-LS,LL-LS,UU-LS,UB-LS | ,,, | ,,P-MC, | ,,,
     * +7:  ,,, | ,,, | ,,, | ,,,
     *  
     * --- --- --- ---
     *  
     * [W]  0123| 4567| 89AB| CDEF
     * ->PLC
     * +0:  ,ON,OFF, | ,,, | ,,, | ,,,
     * +1:  ,,, | ,,, | ,,, | ,,,
     * +2:  ,,, | ,,, | ,,, | ,,,
     * +3:  ,,, | ,,, | ,,, | ,,,
     *  
     *     0123| 4567| 89AB| CDEF
     * PLC->
     * +4:  ,,, | TK,,, | ,,, | ,,,
     * +5:  ,,, | ,,, | ,,, | ,,,
     * +6:  ,,, | ,,, | ,,, | ,,,
     * +7:  ,,, | ,,, | ,,, | ,,,
     * 
     * --end
     */
    
    //-- global resource
    
    ZcFactory.ZcFlicker cmOnesecFlicker=new ZcFactory.ZcFlicker(32, 0.5F);
    
    //-- custom tasks
    
    ZcPumpTask cmPumpTask = new ZcPumpTask();
    
    //-- default tasks
    ZcFactory.ZiTask cmDefaultLadder = new ZcFactory.ZiTask() {
      @Override public void ccScan() {
        
        boolean lpTestBit=cmLinkedMemory.ccReadBit(0, 0x0);
        
        cmOnesecFlicker.ccAct(true);
        cmLinkedMemory.ccWriteBit(4, 0x0, cmOnesecFlicker.ccIsUP());
        
      }//+++
      @Override public void ccSimulate(){}//+++
    };//---
    
    //-- regist task to contructro

    public ZcCPUZero(ZcFactory.ZiMemory pxMemory) {
      super(pxMemory);
      ccAddTask(cmDefaultLadder);
      ccAddTask(cmPumpTask);
    }//+++
    
    //-- custom tasks
    class ZcPumpTask implements ZcFactory.ZiTask{
      
      boolean 
        mnErrorClearSW,
        mnLowBoundERR,mnHiBoundERR,mnLevelorERR,
        mnRunSW,mnRunPL,mnLowCut, mnUpCut
      ;//---
      
      boolean
        dcPumpMC,
        dcUpperBoundLS,dcUpperLS,dcLowerLS,dcLowBoundLS
      ;//---
      
      ZcFactory.ZcDelayor
        cmUpperLimitDelayor,
        cmLowerLimitDelayor
      ;
      
      ZcFactory.ZcCheckedValueModel dcTank;

      public ZcPumpTask(){
        dcTank = new ZcFactory.ZcCheckedValueModel(0, 990);
        dcTank.ccAddChecker(30, 999);
        dcTank.ccAddChecker(330, 999);
        dcTank.ccAddChecker(660, 999);
        dcTank.ccAddChecker(940, 999);
        dcTank.ccSetCurrent(777);
        
        cmUpperLimitDelayor=new ZcFactory.ZcDelayor(3, 3);
        cmLowerLimitDelayor=new ZcFactory.ZcDelayor(3, 3);
      }//+++
      
      //-- interface
      
      @Override public void ccScan(){
        
        //-- takewith
        mnRunSW=cmLinkedMemory.ccReadBit(0, 0xA);
        mnErrorClearSW=cmLinkedMemory.ccReadBit(0, 0xF);
        
        mnLowCut=cmLinkedMemory.ccReadBit(2, 0x1);
        mnUpCut=cmLinkedMemory.ccReadBit(2, 0x2);
        
        int lpDelayOnTime=cmLinkedMemory.ccReadWord(0x01);
        int lpDelayOffTime=cmLinkedMemory.ccReadWord(0x02);
        
        //-- holding
        
        cmUpperLimitDelayor.ccSetDelay(lpDelayOnTime, lpDelayOffTime);
        cmUpperLimitDelayor.ccAct(dcUpperLS);
        
        cmLowerLimitDelayor.ccSetDelay(lpDelayOnTime, lpDelayOffTime);
        cmLowerLimitDelayor.ccAct(dcLowerLS);
        
        //-- output
        dcPumpMC=mnRunSW&&(!cmUpperLimitDelayor.ccIsUP())&&
          (!mnLevelorERR)&&(!mnHiBoundERR)&&
          (dcPumpMC||!cmLowerLimitDelayor.ccIsUP())
        ;
        
        mnRunPL=mnRunSW&&(dcPumpMC||cmOnesecFlicker.ccIsUP());
        
        //-- error
        
        if(dcUpperBoundLS && ((!dcUpperLS)||(!dcLowerLS)||(!dcLowBoundLS)))
          {mnLevelorERR=true;}
        if(dcUpperLS && ((!dcLowerLS)||(!dcLowBoundLS)))
          {mnLevelorERR=true;}
        if(dcLowerLS && (!dcLowBoundLS))
          {mnLevelorERR=true;}
        if(mnErrorClearSW){mnLevelorERR=false;}
        
        if(dcUpperBoundLS){mnHiBoundERR=true;}
        if(mnErrorClearSW){mnHiBoundERR=false;}
        
        mnLowBoundERR=!dcLowBoundLS;
        
        //-- feed back
        cmLinkedMemory.ccWriteBit(4, 0xA, mnRunPL);
        
        cmLinkedMemory.ccWriteBit(5, 0x5, mnHiBoundERR);
        cmLinkedMemory.ccWriteBit(5, 0x6, mnLowBoundERR);
        cmLinkedMemory.ccWriteBit(5, 0x7, mnLevelorERR);
        
        cmLinkedMemory.ccWriteBit(6, 0x0, dcLowBoundLS);
        cmLinkedMemory.ccWriteBit(6, 0x1, dcLowerLS);
        cmLinkedMemory.ccWriteBit(6, 0x2, dcUpperLS);
        cmLinkedMemory.ccWriteBit(6, 0x3, dcUpperBoundLS);
        
        cmLinkedMemory.ccWriteBit(6, 0xA, dcPumpMC);
        
        cmLinkedMemory.ccWriteWord(0x44, dcTank.ccGetCurrent());
        
      }//+++

      @Override public void ccSimulate(){
        if( dcPumpMC){dcTank.ccShift( 8);}
        if(cmRoller%4==1){dcTank.ccShift(-1*ceil(random(8,16)));}
        dcLowBoundLS=dcTank.ccCheckFor(0);
        dcLowerLS=dcTank.ccCheckFor(1)&&(!mnLowCut);
        dcUpperLS=dcTank.ccCheckFor(2)&&(!mnUpCut);
        dcUpperBoundLS=dcTank.ccCheckFor(3);
      }//+++
      
    }//***
    
  }//***
  
  //== dummy memory
  
  class ZcSimplifiedMemory implements ZcFactory.ZiMemory{
    int[] cmWordArray;
    boolean[][] cmBitArray;
    public ZcSimplifiedMemory() {
      cmWordArray=new int[256];
      cmBitArray=new boolean[256][16];
    }//+++
    @Override public int ccReadWord(int pxAddr)
      {return cmWordArray[pxAddr&0xFF];}//+++
    @Override public boolean ccReadBit(int pxAddr, int pxBit)
      {return cmBitArray[pxAddr&0xFF][pxBit&0xF];}//+++
    @Override public void ccWriteWord(int pxAddr, int pxValue)
      {cmWordArray[pxAddr&0xFF]=pxValue;}//+++
    @Override public void ccWriteBit(int pxAddr, int pxBit, boolean pxValue)
      {cmBitArray[pxAddr&0xFF][pxBit&0xF]=pxValue;}//+++
  }//+++
  
  //=== === < PLC
  
  
}//***eof

