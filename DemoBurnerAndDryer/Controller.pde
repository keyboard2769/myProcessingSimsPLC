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
    if(pxAdjustCLK && lpHistoryCondition){
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
    if(cmHistoryHead>=7){cmHistoryAllFilled=true;}
    
    //-- average calculation
    if(!cmHistoryAllFilled){
      cmProcessAverage = cmDesProcessHistory[lpLogicalPrev];
      cmGradientAverage = cmDesGradientHistory[lpLogicalPrev];
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