/* %history%
 * #flag,title,content,addition,addition,addition,addition,
 * ,,,,,,,,,,,,
 * ,,,,,,,,,,,,
 * _180319,newFile,,,,,,,,,,,
 * ,,,,,,,,,,,,
 * ,,,,,,,,,,,,
 */

package ppptrolleyexample;

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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.security.spec.ECField;

import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;
import javax.swing.JTabbedPane;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

public class MainTab extends PApplet{
  
  
  //=== === === ===
  //  data bank
  //=== === === ===
  //<editor-fold desc="packed_code">
  
  class McCoordinator{
    
    public float cmTrolleyIconPercentage=0.2f;
    
    public McCoordinator() {
    }//+++
    
  }//***
  
  //</editor-fold>
  //=== === < data bank
  
  
  //=== === === ===
  //  local ui
  //=== === === ===
  //<editor-fold desc="packed_code">
  
  class EcCoordinator extends EcFactory.EcBaseCoordinator{
    
    EcFactory.EcButton
      cmAutoSW,cmChargeSW,cmDischargeSW,
      cmForewardSW,cmBackwardSW
    ;//---
    
    EcFactory.EcLamp cmChargePL,cmDischargePL;//---
    
    EcTrolleyIcon cmTrolley;//---
    
    public EcCoordinator() {
      super();
      
      //-- control pane
      
      EcFactory.EcPane lpControlPane = new EcFactory.EcPane("control", 300, 80);
      lpControlPane.ccSetLocation(10, 10);
      ccAddShape(lpControlPane);
      
      cmAutoSW = new EcFactory.EcButton("auto", 50, 50, 11);
      cmAutoSW.ccFollows(lpControlPane, 5, 22);
      ccAddElement(cmAutoSW);
      
      cmChargeSW = new EcFactory.EcButton("charge", 80, 23, 21);
      cmChargeSW.ccFollows(cmAutoSW, 10, 0);
      ccAddElement(cmChargeSW);
      
      cmDischargeSW = new EcFactory.EcButton("discharge", 80, 23, 22);
      cmDischargeSW.ccFollows(cmChargeSW, 0, 3);
      ccAddElement(cmDischargeSW);
      
      cmBackwardSW = new EcFactory.EcButton("back", 50, 50, 23);
      cmBackwardSW.ccFollows(cmChargeSW, 10, 0);
      ccAddElement(cmBackwardSW);
      
      cmForewardSW = new EcFactory.EcButton("fore", 50, 50, 24);
      cmForewardSW.ccFollows(cmBackwardSW, 3, 0);
      ccAddElement(cmForewardSW);
      
      //-- model pane
      
      EcFactory.EcPane lpModelPane = new EcFactory.EcPane("model", 300, 130);
      lpModelPane.ccFollows(lpControlPane, 0, 5);
      ccAddShape(lpModelPane);
      
      cmTrolley=new EcTrolleyIcon(220, 100);
      cmTrolley.ccSetPercentage(0.5f);
      cmTrolley.ccFollows(lpModelPane, 20, 25);
      ccAddElement(cmTrolley);
      
      cmChargePL=new EcFactory.EcLamp("charge", 20, 20, 0xFFFF);
      cmChargePL.ccSetLocation(36, 122);
      cmChargePL.ccSetColor(0xFFCC9933, 0xFF999999);
      ccAddElement(cmChargePL);
      
      cmDischargePL=new EcFactory.EcLamp("discharge", 20, 20, 0xFFFF);
      cmDischargePL.ccSetLocation(223, 195);
      cmDischargePL.ccSetColor(0xFFCC9933, 0xFF999999);
      ccAddElement(cmDischargePL);
      
    }//+++
  }//***
  
  //=== local ui ** group
  
  //=== local ui ** icon
  
  class EcTrolleyIcon extends EcFactory.EcGauge{
    public EcTrolleyIcon(int pxW, int pxH){
      super("<nn/>", pxW, pxH, 0xFFFF);
      ccSetColor(0xFFCC3333, 0xFF333333);
    }//+++
    @Override
    void ccUpdate() {
      int lpX=ccScale(cmW)+cmX;
      int lpY=ccCenterY();
      fill(0x55);rect(cmX,lpY-10,cmW,4);
      fill(0x99);quad(
        lpX-20,lpY-20,
        lpX+20,lpY-20,
        lpX+15,lpY+20,
        lpX-15,lpY+20
      );
      ccActFill();
      rect(lpX-18,lpY-18,36,4);
    }//+++
    private int ccScale(int pxInput){return pxInput*cmPerHalfByte/127;}//+++
  }//***
  
  //</editor-fold>
  //=== === < local ui
  
  
  //=== === === ===
  //  swing ui
  //=== === === ===
  //<editor-fold desc="packed_code">
  
  class ScCoordinator{
    
    final JButton
      cmToolbarQuitSW
    ;//---
    
    final ScFactory.ScToolBarWindow cmToolBarWindow;
        
    final ScActionManager cmActionManager;
    final ScMouseClickManager cmClickManager;
    
    public ScCoordinator() {
      
      //-- builed events
      
      cmActionManager=new ScActionManager(MainFrame.pbSelf);
      cmClickManager=new ScMouseClickManager(MainFrame.pbSelf);
      
      //-- winodw
      
      //[MAYUSE]::JPanel lpDummyPane=null;
      
      //-- winodw ** toolbar window
      cmToolbarQuitSW=ScFactory.ccMyCommandButton("quit", "--toolbar-quit");
      cmToolbarQuitSW.addActionListener(cmActionManager);
      cmToolBarWindow=new ScFactory.ScToolBarWindow();
      cmToolBarWindow.ccInit("#:if ",cmActionManager);
      cmToolBarWindow.ccAdd(cmToolbarQuitSW);
      cmToolBarWindow.ccFinish(100, 100);
      
      //-- register event
      
    }//+++
    
    //-- inner class
    
    class ScActionManager implements ActionListener{
      private final MainFrame cmOwner;
      public ScActionManager(MainFrame pxOwner) {cmOwner = pxOwner;}//+++
      @Override public void actionPerformed(ActionEvent e) {
        String lpCommand=e.getActionCommand();
        
        switch(lpCommand){
          
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
  
  //</editor-fold>
  //=== === < swing ui
  
  
  //=== === === ===
  //  PLC
  //=== === === ===
  //<editor-fold desc="packed_code">
  
  class ZcCPUZero extends ZcFactory.ZcPLC{
    
    /* %IOLayout%
     *
     *   bit
     * ->PLC 0123 | 4567 |  89AB | CDEF
     * +00: ,TRL_UP_SW,TRL_DN_SW, | ,,, | ,AUTO_SW,, | ,,, 
     * +10: ,CHAR_SW,DISCHR_SW, | ,,, | ,,, | ,,, 
     * +20: ,,, | ,,, | ,,, | ,,, 
     * +30: ,,, | ,,, | ,,, | ,,, 
     * 
     *   bit
     * <-PLC 0123 | 4567 | 89AB | CDEF
     * +40: ,TRL_UP_PL,TRL_DN_PL, | ,,, | ,FLKR,, | ,,, 
     * +50: ,CHG_PL,DSC_PL, | ,,, | ,,, | ,,, 
     * +60: ,TRL_UP_LSPL,TRL_DN_LSPL, | ,,, | ,,, | ,,, 
     * +70: ,,, | ,,, | ,,, | ,,, 
     * 
     */
    
    //-- global resource
    
    boolean mnAutoSW;//---
    
    ZcFactory.ZcFlicker cmFullSecondFlicker=new ZcFactory.ZcFlicker(32, 0.5F);
    
    //-- custom tasks
    ZcTrolleyTask cmTrolleyTask = new ZcTrolleyTask();
    
    //-- default tasks
    ZcFactory.ZiTask cmDefaultLadder = new ZcFactory.ZiTask() {
      @Override public void ccScan() {
        
        mnAutoSW=cmLinkedMemory.ccReadBit(0, 0x9);
        
        cmFullSecondFlicker.ccAct(true);
        cmLinkedMemory.ccWriteBit(4, 0x9, cmFullSecondFlicker.ccIsUP());
        
      }//+++
      @Override public void ccSimulate(){}//+++
    };//---
    
    //-- regist task to contructro

    public ZcCPUZero(ZcFactory.ZiMemory pxMemory) {
      super(pxMemory);
      ccAddTask(cmDefaultLadder);
      ccAddTask(cmTrolleyTask);
    }//+++
    
    //-- custom task
    
    class ZcTrolleyTask implements ZcFactory.ZiTask{
      
      private boolean
        mnUpSW,mnDownSW,
        mnChargeSW,mnDischargeSW,
              
        cmHasMixture,
              
        dcUpMC,dcDownMC,
        dcUpperLS,dcLowerLS,
        dcDischargeMV,dcChargeMV
      ;//---
      
      private final  ZcFactory.ZcCheckedValueModel simTrolley;
      
      private final  ZcFactory.ZcTimer
        cmStartWait,cmReachedWait,cmDischargeDurate,
        cmClearanceWait,cmStandbyWait
      ;//---
      
      private final  ZcFactory.ZcStepper cmAutoStepper;

      public ZcTrolleyTask() {
        
        cmAutoStepper=new ZcFactory.ZcStepper();
        
        cmStartWait=new ZcFactory.ZcOffDelayPulser(20);
        cmReachedWait=new ZcFactory.ZcOnDelayTimer(20);
        cmDischargeDurate=new ZcFactory.ZcOnDelayTimer(40);
        cmClearanceWait=new ZcFactory.ZcOnDelayTimer(20);
        cmStandbyWait=new ZcFactory.ZcOnDelayTimer(20);
        
        simTrolley=new ZcFactory.ZcCheckedValueModel(0, 2500);
        simTrolley.ccSetCurrent(1000);
        simTrolley.ccAddChecker(250, 500);
        simTrolley.ccAddChecker(2000, 2250);
        
      }//+++
      
      @Override public void ccScan() {
        
        //-- takewith
        mnUpSW        = cmLinkedMemory.ccReadBit(0, 0x1);
        mnDownSW      = cmLinkedMemory.ccReadBit(0, 0x2);
        mnChargeSW    = cmLinkedMemory.ccReadBit(1, 0x1);
        mnDischargeSW = cmLinkedMemory.ccReadBit(1, 0x2);
        
        //-- stepper
        cmAutoStepper.ccSetTo(0, !mnAutoSW);//..[0]stop
        
        boolean lpToStandby = mnAutoSW && dcLowerLS;
        cmAutoStepper.ccStep(0, 1, lpToStandby);//..
        cmAutoStepper.ccStep(9, 1, lpToStandby);//..[1]standby
        
        cmAutoStepper.ccStep(1, 2, cmStartWait.ccIsUP());//..[2]go_foreward
        cmAutoStepper.ccStep(2, 3, dcUpperLS);//..[3]wait_to_discharge
        cmAutoStepper.ccStep(3, 4, cmReachedWait.ccIsUP());//..[4]discharge
        cmAutoStepper
          .ccStep(4, 5, cmDischargeDurate.ccIsUP());//..[5]wait_to_goback
        cmAutoStepper
          .ccStep(5, 6, cmClearanceWait.ccIsUP());//..[6]goes_back
        cmAutoStepper
          .ccStep(6, 7, dcLowerLS);//..[7]wait_to_restandby
        
        cmAutoStepper.ccStep(7, 9, cmStandbyWait.ccIsUP());//..[9]return
        
        //-- stepper ** relay
        boolean lpIsStandby       =cmAutoStepper.ccIsAt(1);
        boolean lpAutoGoForeward  =cmAutoStepper.ccIsAt(2);
        boolean lpAutoDischarge   =cmAutoStepper.ccIsAt(4);
        boolean lpAutoGoBackward  =cmAutoStepper.ccIsAt(6);
        
        //-- stepper ** timer
        cmStartWait.ccAct(dcChargeMV&&lpIsStandby);
        cmReachedWait.ccAct(cmAutoStepper.ccIsAt(3));
        cmDischargeDurate.ccAct(cmAutoStepper.ccIsAt(4));
        cmClearanceWait.ccAct(cmAutoStepper.ccIsAt(5));
        cmStandbyWait.ccAct(cmAutoStepper.ccIsAt(7));
        
        //-- output
        
        dcUpMC=(!dcDownMC)&&(!dcUpperLS)&&(
            (mnUpSW           && !mnAutoSW)||
            (lpAutoGoForeward &&  mnAutoSW)
        );
        
        dcDownMC=(!dcUpMC)&&(!dcLowerLS)&&(
            (mnDownSW         && !mnAutoSW)||
            (lpAutoGoBackward &&  mnAutoSW)
        );
        
        dcChargeMV=dcLowerLS&&(
          (mnChargeSW && !mnAutoSW )||
          (mnChargeSW &&  mnAutoSW && lpIsStandby)
        );
        
        dcDischargeMV=dcUpperLS&&(
          (mnDischargeSW   && !mnAutoSW)||
          (lpAutoDischarge && mnAutoSW)
        );
        
        //-- feedback
        
        cmLinkedMemory.ccWriteBit(4, 0x1, dcUpMC);
        cmLinkedMemory.ccWriteBit(4, 0x2, dcDownMC);
        cmLinkedMemory.ccWriteBit(5, 0x1, dcChargeMV);
        cmLinkedMemory.ccWriteBit(5, 0x2, dcDischargeMV);
        cmLinkedMemory.ccWriteBit(6, 0x1, dcUpperLS);
        cmLinkedMemory.ccWriteBit(6, 0x2, dcLowerLS);
        
      }//+++
      
      @Override public void ccSimulate() {
        
        if(dcUpMC){simTrolley.ccShift( 24);}
        if(dcDownMC){simTrolley.ccShift(-24);}
        dcLowerLS=simTrolley.ccCheckFor(0);
        dcUpperLS=simTrolley.ccCheckFor(1);
        
        if(dcChargeMV&&dcLowerLS){cmHasMixture=true;}
        if(dcDischargeMV&&dcUpperLS){cmHasMixture=false;}
        
      }//+++
      
      final boolean ccHasMixture(){return cmHasMixture;}
      
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
  
  //</editor-fold>
  //=== === < PLC
  
  
}//***eof

