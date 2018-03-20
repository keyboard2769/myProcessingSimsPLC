/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ppptrolleyexample;

public class VcTagger {
  private static MainFrame cmParent=null;
  private static int cmCounter=0;
  private static int cmRow=8;
  private static int cmGapX=100;
  private static int cmGapY=20;
  private static int cmOffsetX=5;
  private static int cmOffsetY=5;
  private static int cmBackground=0x99666666;
  private static int cmForeround =0xFF33EE33;
  
  private static float cmLayWidthCoefficient=1.02f;
  private static int cmLayHeight=16;
  private static boolean cmAct=true;
  
  private VcTagger(){}
  
  //--
  
  //=== === === ===
  //  modifier
  //=== === === ===
  
  static public void ccInit(MainFrame pxParent, int pxRow)
    {cmParent=pxParent;cmRow=pxRow;}//+++
  
  static public void ccSetGap(int pxGapX, int pxGapY)
    {cmGapX=pxGapX;cmGapY=pxGapY;}//+++
  
  static public void ccSetLayStyle(int pxHeight, float pxWCoeff)
    {cmLayHeight=pxHeight;cmLayWidthCoefficient=pxWCoeff; }//+++
  
  static public void ccSetLocationOffset(int pxOffsetX, int pxOffsetY)
    {cmOffsetX=pxOffsetX;cmOffsetY=pxOffsetY;}//+++
  
  static public void ccSetColor(int pxFore, int pxBack)
    {cmForeround=pxFore;cmBackground=pxBack;}//+++
  
  static public void ccFlip(){cmAct=!cmAct;}//+++
  
  //=== === <<< modifier
  
  //=== === === ===
  //  updator
  //=== === === ===
  
  //==  public
  
  static public void ccTag(String pxLine)
    {ccUpdate(pxLine);}//+++
  
  static public void ccTag(String pxTag, Object pxValue)
    {ccUpdate(pxTag+":"+pxValue.toString());}//+++
  
  static public void ccStabilize(){cmCounter=0;}//+++
  
  //==  private
  static private void ccUpdate(String pxTag){
    if(cmParent==null || !cmAct){return;}
    int lpX=(cmCounter/cmRow)*cmGapX+cmOffsetX;
    int lpY=(cmCounter%cmRow)*cmGapY+cmOffsetY;
    int lpW=MainFrame.ceil(cmParent.textWidth(pxTag)*cmLayWidthCoefficient);
    //--
    cmParent.fill(cmBackground);
    cmParent.rect(lpX, lpY, lpW, cmLayHeight);
    //--
    cmParent.fill(cmForeround);
    cmParent.text(pxTag, lpX, lpY);
    //--
    cmCounter++;
  }//+++
  
  //=== === < updator
  
}//***EOF

