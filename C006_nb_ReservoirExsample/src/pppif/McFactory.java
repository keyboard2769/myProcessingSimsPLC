/* --def %revision%
 * #[flag] "version/filename" : issue $ describe,
 * [ ] "": $,,,
 * [ ] "": $,,,
 * [ ] "": $,,,
 * --end
 */

package pppif;

import processing.data.StringDict;
import static processing.core.PApplet.nf;
import static processing.core.PApplet.nfc;
import static processing.core.PApplet.ceil;
import static processing.core.PApplet.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import processing.data.StringList;

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
  
  static public final int ccToFrameBySecond(float pxSecond){
    int lpRes=ceil(map(pxSecond,0,1,0,16));
    return pxSecond==0?3:lpRes;
  }//+++
  
  static public final String ccToSecondByFrame(int pxFrame){
    return nfc(map(pxFrame,0,16,0,1),2);
  }//+++
  
  
  //=== < Utility
  
  
  //=== === === ===
  //  inner
  //=== === === ===
  
  //== something
  
  static class McPulser{
    private boolean cmBuff;
    final boolean ccPulse(boolean pxAct){
      if(cmBuff!=pxAct){
        cmBuff=pxAct;
        return true;
      }
      return false;
    }//+++
    final boolean ccUpPulse(boolean pxAct){return ccPulse(pxAct)&&(cmBuff);}//+++
    final boolean ccDownPulse(boolean pxAct)
      {return ccPulse(pxAct)&&(!cmBuff);}//+++
  }//***
  
  static class McImpulseTimer{
    private int cmCounter=0;
    final void ccUpdate(){cmCounter-=(cmCounter==0?0:1);}
    final void ccSet(int pxFrame){cmCounter=pxFrame;}
    final boolean ccIsUp(){return cmCounter>0;}
  }//***
  
  
  //== error list
  
  static class McErrorFolder{
    
    private final boolean[] cmStatus=new boolean[16];
    private final McPulser[] cmStatusPulse;
    private final int cmMask=15;
    private final HashMap<Integer,McError> cmErrorList=new HashMap<>();
    private final String[] cmCleanMessage = {"--no error!"};    
    StringList cmActivatedList =new StringList();

    public McErrorFolder(){
      cmStatusPulse=new McPulser[16];
      for(int i=0;i<16;i++){
        cmStatusPulse[i]=new McPulser();
      }
    }//+++
    
    final void ccAdd(int pxID, String pxTitle, String pxDescription){
      ccAdd(new McError(pxID, pxTitle, pxDescription));
    }//+++
    
    final void ccAdd(McError pxError){
      cmErrorList.put(pxError.cmID, pxError);
    }//+++
    
    final void ccSet(int pxID, boolean pxStatus)
      {cmStatus[pxID&cmMask]=pxStatus;}//+++
    
    final void ccClear(){Arrays.fill(cmStatus, false);}
    
    final void ccScanError(){
      cmActivatedList.clear();
      boolean lpTestBit;
      String lpTitle="..";
      for(int i=0,s=cmStatus.length;i<s;i++){
        if(cmErrorList.containsKey(i)){
          lpTitle=cmErrorList.get(i).toString();
          if(cmStatus[i]){if(VcConst.ccIsValidString(lpTitle)){
            cmActivatedList.append(lpTitle);
          }}
        }
        lpTestBit=cmStatus[i];
        if(cmStatusPulse[i].ccPulse(lpTestBit)){
          if(lpTestBit){MainFrame
            .pbSelf.fsStack("[occured]"+lpTitle+VcConst.ccTimeStamp());}
          else{MainFrame
            .pbSelf.fsStack("[clear]"+lpTitle+VcConst.ccTimeStamp());}
        }
      }
    }//+++
    
    synchronized final int ccActivatedCount()
      {return cmActivatedList.size();}//+++
    
    synchronized final String[] ccGetActivatedArray(){
      return (cmActivatedList.size()==0)?
        cmCleanMessage:cmActivatedList.array();
    }//+++
    
  }//***
  
  static class McError{
    final int cmID;
    final String cmTitle;
    final String cmDescription;
    public McError(int pxID, String pxTitle, String pxDescription){
      cmID = pxID;
      cmTitle = pxTitle;
      cmDescription = pxDescription;
    }//+++
    @Override public String toString(){
      StringBuilder lpRes=new StringBuilder("E");
      lpRes.append(nf(cmID,3));
      lpRes.append(":");
      lpRes.append(cmTitle);
      return lpRes.toString();
    }//+++
  }//***
  
  
  //== keyvalue table
  
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

