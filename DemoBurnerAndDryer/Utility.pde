static final float ccToFloat(String pxText){
  float lpRes;
  try{
    lpRes = Float.parseFloat(pxText);
  }catch(Exception e) {
    lpRes = 0.0f;
  }//..?
  return lpRes;
}//+++

static final boolean not(boolean a){
  return !a;
}//+++

static final boolean and(boolean a, boolean b){
  return a&&b;
}//+++

static final boolean or(boolean a, boolean b){
  return a||b;
}//+++

static final boolean sel(boolean c, boolean a, boolean b){
  return c?a:b;
}//+++

static final float sel(boolean c, float a, float b){
  return c?a:b;
}//+++

static final boolean gate(boolean ca, boolean a, boolean cb, boolean b){
  return ca?a:(cb?b:false);
}//+++

static final boolean ccIsValidString(String pxLine){
  if(pxLine==null){return false;}
  return !pxLine.isEmpty();
}//+++

static final float ccToSecond(int pxFrameCount){
  float lpAmplified = ((float)pxFrameCount)*100f/16f;
  int lpCeiled = ceil(lpAmplified);return ((float)(lpCeiled))/100f;
}//+++

static final int ccToFrameCount(float pxSecond){
  return (int)(pxSecond*16f);
}//+++