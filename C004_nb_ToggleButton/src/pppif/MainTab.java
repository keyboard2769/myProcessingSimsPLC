/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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
    
    EcFactory.EcElement cmToggleSW;
    
    public EcCoordinator() {
      super();
      
      cmToggleSW=new EcFactory.EcButton("Toggle!", 50, 50, 27);
      cmToggleSW.ccSetLocation(160-25, 120-25);
      ccAddElement(cmToggleSW);
      
    }//+++
  }//***
  
  //=== local ui ** group
  
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
    
    //-- global resource
    private boolean mnSwitchFB=false;
    ZcFactory.ZcTimer cmInputPulser = new ZcFactory.ZcOnDelayPulser(1);
    
    //-- custom tasks
    
    //-- default tasks
    ZcFactory.ZiTask cmDefaultLadder = new ZcFactory.ZiTask() {
      @Override public void ccScan() {
        
        //-- takein
        boolean lpInputSW = cmLinkedMemory.ccReadBit(0, 0x1);
        
        //-- operate
        cmInputPulser.ccAct(lpInputSW);
        if(cmInputPulser.ccIsUP()){mnSwitchFB=!mnSwitchFB;}
        
        //-- feedback
        cmLinkedMemory.ccWriteBit(4, 0x1, mnSwitchFB);
        
      }//+++
      @Override public void ccSimulate(){}//+++
    };//---
    
    //-- regist task to contructro

    public ZcCPUZero(ZcFactory.ZiMemory pxMemory) {
      super(pxMemory);
      ccAddTask(cmDefaultLadder);
    }//+++
    
  }//***
  
  //== custom task
  
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

