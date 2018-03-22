/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pppif;

import processing.data.StringDict;

import java.util.ArrayList;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import processing.data.StringList;

/**
 *
 * @author keypad on my mac11
 */
public class McFactory {
  
  private McFactory(){}//+++ 
  
  //=== === === ===
  //  Utility
  //=== === === ===
  
  static final String[] ccToStringArray(Object[] pxArray){
    String[] lpRes=new String[pxArray.length];
    for(int i=0,s=lpRes.length;i<s;i++){
      lpRes[i]=pxArray[i].toString();
    }
    return lpRes;
  }//+++
  
  //=== < Utility
  
  
  
  
  
  
  
  //=== === === ===
  //  inner
  //=== === === ===
  
  //== folded model
  
  static class McKeyValueFolder{
    private final ArrayList<McKeyValueModel> cmList;

    public McKeyValueFolder() {cmList=new ArrayList<>();}//+++
    
    final void ccAdd(McKeyValueModel pxModel){cmList.add(pxModel);}//+++
    
    private int ccFixIndex(int pxIndex){
      int lpFix=pxIndex&0xFFFF;
      int lpSize=cmList.size();
      return lpFix>lpSize?lpSize:lpFix;
    }//+++
    
    final McKeyValueModel ccGet(int pxIndex)
      {return cmList.get(ccFixIndex(pxIndex));}//+++
    
    final String[] ccTellTitles(){
      StringList lpRes=new StringList();
      for(McKeyValueModel it:cmList){lpRes.append(it.ccGetTitle());}
      return lpRes.array();
    }//+++
    
  }//***
  
  //== basic model
  
  static class McKeyValueModel implements TableModel{
    private final String cmTitle;
    private final StringDict cmData;
    private int cmListenerCounter;
    McKeyValueModel(String pxTitle) {
      cmListenerCounter=0;
      cmData=new StringDict();
      cmTitle=pxTitle;
    }//+++
    
    //-- ** modifier
    synchronized final void ccSet(String pxKey, String pxValue)
      {cmData.set(pxKey, pxValue);}//+++
    
    //-- ** teller
    synchronized final String ccGetTitle(){return cmTitle;}
    synchronized final String ccGet(String pxKey){return cmData.get(pxKey);}//+++
    synchronized final String ccGetKeyAt(int pxIndex)
      {return cmData.key(pxIndex);}//+++
    synchronized final String ccGetValueAt(int pxIndex)
      {return cmData.value(pxIndex);}//+++
    synchronized final int ccGetListenerCounter(){return cmListenerCounter;}//+++
    
    //-- ** support
    private int ccFixIndex(int pxIndex){
      int lpMask=pxIndex&0xFFFF;
      int lpSize=cmData.size();
      return lpMask>lpSize?lpSize:lpMask;
    }//+++
    
    //-- ** interface
    @Override public int getRowCount() {return cmData.size();}//+++

    @Override public int getColumnCount() {return 2;}//+++

    @Override public String getColumnName(int columnIndex) {
      if(columnIndex==0){return "key";}
      if(columnIndex==1){return "value";}
      return "<illeagle/>";
    }//+++

    @Override public Class<?> getColumnClass(int columnIndex)
      {return "</>".getClass();}//+++

    @Override public boolean isCellEditable(int rowIndex, int columnIndex)
      {return columnIndex==1;}//+++

    @Override public Object getValueAt(int rowIndex, int columnIndex) {
      int lpFix=ccFixIndex(rowIndex);
      if(columnIndex==0){return ccGetKeyAt(lpFix);}
      if(columnIndex==1){return ccGetValueAt(lpFix);}
      return "<nc/>";
    }//+++

    @Override public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
      if(columnIndex==1){
        int lpFix=ccFixIndex(rowIndex);
        String lpKey=cmData.key(lpFix);
        ccSet(lpKey, aValue.toString());
      }
    }//+++

    @Override public void addTableModelListener(TableModelListener l)
      {cmListenerCounter++;}//+++

    @Override public void removeTableModelListener(TableModelListener l) 
      {cmListenerCounter--;}//+++
    //--
  }//***
  
  //=== < inner

}//***EOF

