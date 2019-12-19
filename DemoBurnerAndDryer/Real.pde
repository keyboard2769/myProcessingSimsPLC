class ZcReal{
  
  float cmVal;
  boolean cmStatical;
  
  ZcReal(){
    cmVal=0.0f;
    cmStatical=false;  
  }//++!
  
  ZcReal(float pxInitValue){
    cmVal=pxInitValue;
    cmStatical=false;  
  }//++!
  
  ZcReal(float pxInitValue, boolean pxStatical){
    cmVal=pxInitValue;
    cmStatical=pxStatical;  
  }//++!
  
}//***

void ccTransfer(ZcReal pxPotentialP, ZcReal pxPotentialN, float pxRatio){
  if(pxRatio<=0f){return;}
  float lpDiff=pxPotentialP.cmVal-pxPotentialN.cmVal;
  lpDiff/=pxRatio;
  if(!pxPotentialP.cmStatical){pxPotentialP.cmVal-=lpDiff;}
  if(!pxPotentialN.cmStatical){pxPotentialN.cmVal+=lpDiff;}
}//+++