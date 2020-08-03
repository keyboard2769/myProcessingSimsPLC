
  static final boolean ccIsValidString(String pxLine){
    if(pxLine==null){return false;}
    return !pxLine.isEmpty();
  }//+++

  static final float ccToSecond(int pxFrameCount){
    float lpAmplified = ((float)pxFrameCount)*100f/16f;
    int lpCeiled = ceil(lpAmplified);return ((float)(lpCeiled))/100f;
  }//+++

  static final int ccToFrameCount(float pxSecond){
    return (int)(pxSecond*self.frameRate);
  }//+++
  
  static final int ccParseInteger(String pxSource){
    int lpRes = 0;
    try {
      lpRes = Integer.valueOf(pxSource);
    } catch (Exception e) {
      lpRes = 0;
    }//..?
    return lpRes;
  }//+++
  
  static final float ccParseFloat(String pxSource){
    float lpRes = 0f;
    try {
      lpRes = Float.valueOf(pxSource);
    } catch (Exception e) {
      lpRes = 0f;
    }//..?
    return lpRes;
  }//+++
  
  static final float ccDevideInteger(int pxVal, int pxBase){
    if(pxBase==0){
      return 0f;
    }else{
      return (float)(
        ((float)(pxVal))/((float)(pxBase))
      );
    }//..?
  }//+++
  

//***eof