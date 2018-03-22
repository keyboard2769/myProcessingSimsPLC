/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pppif;

import java.util.ArrayList;

public class ZcFactory {
  
  private ZcFactory (){}//+++
  
  //=== === === ===
  //  inner
  //=== === === ===
  
  //== gate
  
  static class ZcPLC{
    
    protected ZiMemory cmLinkedMemory;
    private final ArrayList<ZiTask> cmTaskList;
    
    public ZcPLC(ZiMemory pxLinkedMemory) {
      cmLinkedMemory=pxLinkedMemory;
      cmTaskList=new ArrayList<>();
    }//+++
    
    public final void ccUpdate(){
      if(cmLinkedMemory==null){return;}
      for(ZiTask it:cmTaskList){
        it.ccScan();
        it.ccSimulate();
      }
    }//+++
    
    public final void ccAddTask(ZiTask pxTask)
      {cmTaskList.add(pxTask);}//+++
    
  }//***
  
  //== interface
  
  interface ZiTask{
    void ccScan();
    void ccSimulate();
  }//***
  
  interface ZiMemory{
    int ccReadWord(int pxAddr);
    boolean ccReadBit(int pxAddr, int pxBit);
    void ccWriteWord(int pxAddr, int pxValue);
    void ccWriteBit(int pxAddr, int pxBit, boolean pxValue);
  }//***
  
  //== model
  
  static class ZcRangedModel {
    protected int cmMax,cmMin;
    public ZcRangedModel(int pxStart, int pxSpan) {
      ccSetRange(pxStart, pxSpan);
    }//+++
    //--
    final void ccSetSpan(int pxSpan){cmMax=cmMin+pxSpan;}//+++
    final void ccSetRange(int pxStart, int pxSpan){
      cmMin=pxStart;
      cmMax=pxStart+pxSpan;
    }//+++
    //--
    final int ccLimit(int pxSource){
      int lpRes=pxSource>cmMax?cmMax:pxSource;
      lpRes=lpRes<cmMin?cmMin:lpRes;
      return lpRes;
    }//+++
    final int ccWarp(int pxSource) {
      int lpRes=pxSource>cmMax?cmMin:pxSource;
      lpRes=lpRes<cmMin?cmMax:lpRes;
      return lpRes;
    }//+++
    final boolean ccContains(int pxSource)
      {return (pxSource>=cmMin)&&(pxSource<cmMax);}//+++
    //--
    final int ccGetMin(){return cmMin;}
    final int ccGetMax(){return cmMax;}
    final int ccTellSpan(){return cmMax-cmMin;}//+++
    //--
  }//***
  
  static class ZcRangedValueModel extends ZcRangedModel{
    protected int cmCurrent;
    public ZcRangedValueModel(int pxStart, int pxSpan)
      {super(pxStart, pxSpan);cmCurrent=pxStart;}//+++
    //--
    final void ccSetCurrent(int pxCurrent){cmCurrent=ccLimit(pxCurrent);}//+++
    final void ccShift(int pxOffset)
      {cmCurrent=ccLimit(cmCurrent+pxOffset);}//+++
    final void ccRoll(int pxOffset)
      {cmCurrent=ccWarp(cmCurrent+pxOffset);}//+++
    //--
    final int ccGetCurrent(){return cmCurrent;}
    //--
  }//***
  
  static class ZcCheckedValueModel extends ZcRangedValueModel{
    ArrayList<ZcRangedModel> cmCheckList;
    public ZcCheckedValueModel(int pxStart, int pxSpan) {
      super(pxStart, pxSpan);
      cmCheckList=new ArrayList<>();
    }//+++
    //--
    void ccAddChecker(int pxStart, int pxLength){
      cmCheckList.add(new ZcRangedModel(pxStart, pxLength));
    }//+++
    ZcRangedModel ccGetChecker(int pxIndex){
      if(cmCheckList.isEmpty()){return null;}
      if(pxIndex>cmCheckList.size()){return null;}
      return cmCheckList.get(pxIndex&0xFF);
    }//+++
    boolean ccCheckFor(int pxIndex) {
      ZcRangedModel lpChecker=ccGetChecker(pxIndex);
      if(lpChecker==null){return false;}
      return lpChecker.ccContains(cmCurrent);
    }//+++
    //--
  }//***
  
  //== device
  
  //== device ** timer
  
  static abstract class ZcTimer {
    ZcCheckedValueModel cmModel;
    public ZcTimer(int pxSpan) {
      cmModel=new ZcCheckedValueModel(0, pxSpan&0xFFFF);
    }//+++
    int ccGetCurrent(){return cmModel.ccGetCurrent();}
    //--
    abstract void ccAct(boolean pxAct);
    abstract boolean ccIsUP();
    //--
  }//***
  
  static class ZcOnDelayTimer extends ZcTimer{
    public ZcOnDelayTimer(int pxSpan) {
      super(pxSpan);
      cmModel.ccAddChecker(pxSpan, 1);
    }//+++
    //--
    @Override void ccAct(boolean pxAct) {
      cmModel.ccShift(pxAct?1:-1*cmModel.ccTellSpan());
    }//+++
    @Override boolean ccIsUP() {return cmModel.ccCheckFor(0);}//+++
  }//***
  
  static class ZcOffDelayTimer extends ZcTimer{
    public ZcOffDelayTimer(int pxSpan) {
      super(pxSpan);
      cmModel.ccAddChecker(cmModel.ccGetMin(), 1);
    }//+++
    //--
    @Override void ccAct(boolean pxAct) {
      cmModel.ccShift(pxAct?cmModel.ccTellSpan():-1);
    }//+++
    @Override boolean ccIsUP() {return !cmModel.ccCheckFor(0);}//+++
  }//***
  
  static class ZcOnDelayPulser extends ZcOnDelayTimer{
    public ZcOnDelayPulser(int pxSpan) {
      super( (pxSpan|0x03)&0xFFFF );
      ZcRangedModel lpChecker=cmModel.ccGetChecker(0);
      if(lpChecker!=null)
        {lpChecker.ccSetRange(cmModel.ccGetMax()-1, 1);}
    }//+++
  }//***
  
  static class ZcOffDelayPulser extends ZcOffDelayTimer{
    public ZcOffDelayPulser(int pxSpan) {
      super( (pxSpan|0x03)&0xFFFF );
      ZcRangedModel lpChecker=cmModel.ccGetChecker(0);
      if(lpChecker!=null)
        {lpChecker.ccSetRange(cmModel.ccGetMin()+1, 1);}
    }//+++
    @Override boolean ccIsUP() {return cmModel.ccCheckFor(0);}//+++
  }//+++
  
  static class ZcFlicker extends ZcTimer{
    private final int cmJudge;
    public ZcFlicker(int pxSpan, float pxDuty)
      {super(pxSpan);cmJudge=(int)(pxDuty*(float)cmModel.ccTellSpan());}//+++
    //--
    @Override void ccAct(boolean pxAct)
      {if(pxAct){cmModel.ccRoll(1);}}//+++
    @Override boolean ccIsUP()
      {return cmModel.ccGetCurrent()<cmJudge;}//+++
    //--
  }//***
  
  //== device ** misc
  
  static class ZcStepper {
    int cmStage;
    public ZcStepper() {cmStage=0;}//+++
    //--
    void ccStep(int pxFrom, int pxTo, boolean pxCondition)
      {if(cmStage==pxFrom && pxCondition){cmStage=pxTo;}}//+++
    void ccSetTo(int pxStage, boolean pxCondition)
      {if (pxCondition) {cmStage = pxStage;}}//+++
    boolean ccIsAt(int pxStage){return cmStage==pxStage;}//+++
    //--
  }//***
  
  //=== === < inner
  
}//***eof

