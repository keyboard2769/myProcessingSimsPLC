class ZcController{
  
  boolean cmFeederMC;
  boolean cmPumpMC;
  float cmFeederRPM;
  float cmPumpRPM;
  String tstLine="</>";
  
  void ccUpdate(){
    
    //-- feeder calc
    vmFeederSettingRPM=map(vmFeederSettingTPH, 
      vmFeederTPHZero, vmFeederTPHSpan, 
      vmFeederRPMZero, vmFeederRPMSpan
    );
    
    //-- feeder apply
    cmFeederMC = vmFeederSW;
    if(cmFeederMC){
      cmFeederRPM=constrain(vmFeederSettingRPM, 0f, C_RPM_MAX);
    }else{
      cmFeederRPM=0f;
    }//..?
    
    //-- pump calc
    float lpActualFACT = (vmFeederTPHSpan<=0.0f)
      ?(0f)
      :( map(cmFeederRPM,
          vmFeederRPMZero,vmFeederRPMSpan,
          vmFeederTPHZero,vmFeederTPHSpan
        ) / vmFeederTPHSpan);
    float lpBiasedFACT = lpActualFACT * vmAdditveBias;
    float lpLimitedFACT = constrain(lpBiasedFACT,
      vmAdditveBiasL, vmAdditveBiasH
    );
    vmPumpTPH = lpLimitedFACT * vmPumpTPHSpan;
    vmPumpRPM = map(vmPumpTPH,
      vmPumpTPHZero,vmPumpTPHSpan,
      vmPumpRPMZero,vmPumpRPMSpan
    );
    
    //-- pump apply
    cmPumpMC=vmPumpSW && (cmFeederRPM>0);
    if(vmDelayTimeACTIVATOR>0){vmDelayTimeACTIVATOR--;}
    if(vmDelayTimeACTIVATOR==1){
      if(cmPumpMC){
        cmPumpRPM=constrain(vmPumpRPM, 0f, C_RPM_MAX);
      }else{
        cmPumpRPM=0;
      }//..?
    }
    
    //[test]::tstLine = String.format(...);
    
  }//+++
  
  //=== interface

  @Override public String toString(){return tstLine;}
  
}//***eof
  