/* --def %revision%
 * #[flag] "version/filename" : issue $ describe,
 * [ ] "": $,,,
 * [ ] "": $,,,
 * [ ] "": $,,,
 * [ ] "": Hopper Clsss added $name:EcHopperShape,,,
 * --end
 */

package pppif;

import processing.core.PApplet;
import static processing.core.PApplet.ceil;
import static processing.core.PConstants.BOTTOM;
import static processing.core.PConstants.CENTER;
import static processing.core.PConstants.LEFT;
import static processing.core.PConstants.RIGHT;
import static processing.core.PConstants.TOP;

import java.util.ArrayList;
import java.util.HashMap;

public class EcFactory {
  
  static final int
    //--
    C_BUTTON_STROKE       =0xFF555555,
    C_BUTTON_FILL_OVER    =0xFFAACCAA,
    C_BUTTON_FILL_PRESSED =0xFF99BB99,
    C_BUTTON_FILL_NORMAL  =0xFF999999,
    C_DEFAULT_ON          =0xFFEEEE33,
    C_DEFAULT_OFF         =0xFF888888,
    //--
    C_LAMP_TEXT        =0xFF333333,
    C_LABEL_TEXT       =0xFFCCCCCC,
    //--
    C_PANE_BASE_COLOR  =0xFF333333,
    C_PANE_TITLE_COLOR  =0xFFEEEEEE
  ;//---
  
  private static final int
    //--
    C_STROKE_WEIGHT =  2,
    C_BOX_MARG      =  5,
    C_TEXT_HEIGHT   = 16,
    C_TEXT_ADJ_Y    = -2,
    C_LABEL_OFFSET  =  2,
    //--
    C_AUTOSIZE_WIDTH_MARG  =  4,
    C_AUTOSIZE_FULL_HEIGHT = 20
  ;//---
  
  static private PApplet pbOwner=null;
  
  private EcFactory(){}//+++ 
  
  static public void ccSetOwner(PApplet pxOwner){pbOwner=pxOwner;}//+++
  
  //=== === === ===
  //  inner
  //=== === === ===
  
  //=== inner ** gate
  
  static class EcBaseCoordinator{
    private int cmCurrentID;
    private final ArrayList<EcShape> cmShapeList;
    private final ArrayList<EcElement> cmElementList;
    private final EcTipManager cmTipManager;
    
    //--
    public EcBaseCoordinator() {
      cmCurrentID=0;
      cmShapeList=new ArrayList<>();
      cmElementList=new ArrayList<>();
      cmTipManager=new EcTipManager();
    }//+++
    //--
    public final void ccUpdate(){
      if(pbOwner==null){return;}
      cmCurrentID=0;
      for(EcShape it:cmShapeList){it.ccUpdate();}
      for(EcElement it:cmElementList){
        it.ccUpdate();
        if(it.ccIsMouseOver()){cmCurrentID=it.ccGetID();}
      }
      cmTipManager.ccUpdate(cmCurrentID);
    }//+++
    //--
    public final void ccAddElement(EcElement pxElement)
      {cmElementList.add(pxElement);}//+++
    public final void ccAddElement(ArrayList<EcElement> pxList)
      {cmElementList.addAll(pxList);}//+++
    public final void ccAddShape(EcShape pxShate)
      {cmShapeList.add(pxShate);}//+++
    public final void ccAddShape(ArrayList<EcShape> pxList)
      {cmShapeList.addAll(pxList);}//+++
    public final void ccAddTip(int pxID, String pxTip)
      {cmTipManager.ccPut(pxID, pxTip);}//+++ 
    public final int ccTellCurrentID(){return cmCurrentID;}//+++
  }//***
  
  //=== inner ** interface
  
  interface EiGroup{
    ArrayList<EcFactory.EcShape> ccGiveShapeList();
    ArrayList<EcFactory.EcElement> ccGiveElementList();
  }//***
  
  interface EiUpdatable{
    void ccUpdate();
  }//***
  
  //=== inner ** supporter
  
  private static class EcTipManager{
    private final HashMap<Integer, EcTip> cmTipMap=new HashMap<>();
    void ccUpdate(int pxID){
      if(cmTipMap.containsKey(pxID)){
        EcTip lpTip=cmTipMap.get(pxID);
        int lpX=pbOwner.mouseX<(pbOwner.width/2)?
          pbOwner.mouseX:pbOwner.mouseX-lpTip.cmTipW;
        int lpY=pbOwner.mouseY<(pbOwner.height/2)?
          pbOwner.mouseY:pbOwner.mouseY-lpTip.cmTipH;
        pbOwner.fill(0xAA111111);
        pbOwner.rect(lpX,lpY,lpTip.cmTipW,lpTip.cmTipH);
        pbOwner.fill(0xCC);
        pbOwner.text(lpTip.cmTip,lpX,lpY+2);
      }
    }//+++
    final void ccPut(int pxID, String pxTip)
      {cmTipMap.put(pxID, new EcTip(pxTip));}//+++
  }//***
  
  private static class EcTip{
    String cmTip;//---
    int cmTipW,cmTipH;//---
    EcTip(String pxTip){
      cmTip=pxTip;
      cmTipW=ceil(pbOwner.textWidth(pxTip));
      cmTipH=C_AUTOSIZE_FULL_HEIGHT;
      for(char it:pxTip.toCharArray())
        {if(it=='\n'){cmTipH+=C_AUTOSIZE_FULL_HEIGHT;}}
    }//+++
  }//***
  
  //=== inner ** liquid and his childs
  
  static class EcPoint{
    protected int cmX,cmY;
    public EcPoint(){this(8,8);}//+++
    public EcPoint(int pxX, int pxY) {
      ccSetLocation(pxX, pxY);
    }//+++
    public final void ccSetLocation(int pxX,int pxY){
      cmX=pxX;cmY=pxY;
    }//+++
  }//***
  
  static class EcRect extends EcPoint{
    protected int cmW=8,cmH=8;
    public final void ccSetSize(int pxW, int pxH){
      cmW=pxW;cmH=pxH;
    }//+++
    public final void ccSetEndPoint(int pxEndX, int pxEndY){
      cmW=pxEndX-cmX;
      cmH=pxEndY-cmY;
    }//+++
    public final void ccSetBound(int pxX, int pxY, int pxW, int pxH){
      cmX=pxX;cmY=pxY;cmW=pxW;cmH=pxH;
    }//+++  
    public final void ccFollows(EcRect pxTarget, int pxOffsetX, int pxOffsetY){
      cmX=pxOffsetX;
      cmY=pxOffsetY;
      if(pxTarget!=null){
        cmX+=pxTarget.cmX+(pxOffsetY==0?pxTarget.cmW:0);
        cmY+=pxTarget.cmY+(pxOffsetX==0?pxTarget.cmH:0);
      }
    }//+++
    public final void ccMatchs(EcRect pxTarget, boolean pxInW, boolean pxInH){
      if(pxTarget!=null){
        if(pxInW){cmW=pxTarget.ccGetW();}
        if(pxInH){cmH=pxTarget.ccGetH();}
      }
    }
    //--
    public final int ccGetW(){return cmW;}//+++
    public final int ccGetH(){return cmH;}//+++
    public final int ccCenterX(){return cmX+cmW/2;}//+++
    public final int ccCenterY(){return cmY+cmH/2;}//+++
    public final int ccEndX(){return cmX+cmW;}//+++
    public final int ccEndY(){return cmY+cmH;}//+++
    public final boolean ccContains(int pxX, int pxY){
      return (pxX>cmX)&&(pxX<(cmX+cmW))
           &&(pxY>cmY)&&(pxY<(cmY+cmH));
    }//+++
  }//***
  
  static class EcShape extends EcRect{
    int cmBaseColor;
    public EcShape(int pxW, int pxH) {
      super();
      ccSetSize(pxW, pxH);
      cmBaseColor=C_BUTTON_STROKE;
    }//+++
    protected final void ccSetColor(int pxColor){cmBaseColor=pxColor;}//+++
    void ccUpdate(){
      pbOwner.fill(cmBaseColor);
      pbOwner.rect(cmX,cmY,cmW,cmH);
    };
  }//+++
  
  static class EcLineShape extends EcShape{
    float cmLineWeight;
    public EcLineShape(int pxX, int pxY, int pxW, int pxH, float pxWeight){
      super(pxW, pxH);
      ccSetLocation(pxX, pxY);
      cmLineWeight=pxWeight;
    }//+++
    @Override void ccUpdate(){
      pbOwner.stroke(cmBaseColor);
      pbOwner.strokeWeight(cmLineWeight);
      pbOwner.line(cmX, cmY, ccEndX(), ccEndY());
      pbOwner.strokeWeight(1.0f);
      pbOwner.noStroke();
    }//+++
  }//***
  
  static class EcHopperShape extends EcShape{
    int cmRatioPix;
    public EcHopperShape(int pxW, int pxH, float pxRatio){
      super(pxW, pxH);
      cmRatioPix=ceil((float)pxH*pxRatio);
    }//+++
    @Override void ccUpdate(){
      pbOwner.fill(cmBaseColor);
      pbOwner.rect(cmX, cmY, cmW, cmH-cmRatioPix);
      pbOwner.quad(
        cmX, ccEndY()-cmRatioPix,
        ccEndX(), ccEndY()-cmRatioPix,
        ccEndX()-cmRatioPix, ccEndY(),
        cmX+cmRatioPix, ccEndY()
      );
    }//+++
  }//++
  
  //[PLAN]::blower
  //[PLAN]::belcon
  //[PLAN]::screw
  
  static class EcPane extends EcShape{
    String cmTitle;
    public EcPane(String pxTitle, int pxW, int pxH) {
      super(pxW, pxH);
      cmTitle=pxTitle;
    }//+++
    @Override void ccUpdate() {
      pbOwner.fill(C_PANE_TITLE_COLOR);
      pbOwner.rect(cmX,cmY,cmW,cmH);
      pbOwner.fill(C_PANE_BASE_COLOR);
      pbOwner.rect(cmX+2,cmY+18,cmW-4,cmH-22);
      pbOwner.text(cmTitle, cmX+2, cmY+2+C_TEXT_ADJ_Y);
    }//+++
  }//***
  
  //=== inner ** solid and his childs
  
  static class EcElement extends EcRect{
    protected int cmID,cmOnColor,cmOffColor;
    protected String cmKey,cmName,cmText;
    protected boolean cmAct, cmIsEnabled, cmIsVisible;
    protected char cmNameAlign;
    public EcElement() {
      super();
      cmID=0xFFFF;
      cmOnColor  = C_DEFAULT_ON;
      cmOffColor = C_DEFAULT_OFF;
      cmKey="n/k";cmName="n/n";cmText="n/t";
      cmNameAlign='x';
      cmIsEnabled=false;cmIsVisible=true;
    }//+++
    //--
    //-- ** modifier
    public final void ccSetText(String pxText){cmText=pxText;}//+++
    public final void ccSetName(String pxName){cmName=pxName;}//+++
    public final void ccSetNameAlign
      (char pxMode_ablr){cmNameAlign=pxMode_ablr;}//+++
    public final void ccSetID(int pxID){cmID=pxID;}//+++
    public final void ccSetColor(int pxOn, int pxOff)
      {cmOnColor=pxOn;cmOffColor=pxOff;}//+++
    public final void ccSetActivity(boolean pxAct){cmAct=pxAct;}//+++
    public final void ccSetEnability(boolean pxState){cmIsEnabled=pxState;}//+++
    public final void ccSetVisibility(boolean pxState){cmIsVisible=pxState;}//+++
    //--
    public final void ccAutoSize(){
      cmW=ceil(pbOwner.textWidth(cmText))+C_AUTOSIZE_WIDTH_MARG*2;
      cmH=C_AUTOSIZE_FULL_HEIGHT;
      for(char it:cmText.toCharArray())
        {if(it=='\n'){cmH+=C_AUTOSIZE_FULL_HEIGHT;}}
    }//+++
    public final void ccFlip(){cmAct=!cmAct;}//+++
    public final void ccTakeKey(String pxKey){
      cmKey=pxKey;
      cmName=pxKey;
      cmText=pxKey;
    }//+++
    //-- ** updator
    void ccUpdate(){if(!cmIsVisible){return;}
      ccActFill();pbOwner.rect(cmX,cmY,cmW,cmH);
      ccDrawTextAtCenter(C_LAMP_TEXT);
      ccDrawName(C_LABEL_TEXT);
    }//+++
    protected final void ccDrawTextAtCenter(int pxColor){
      pbOwner.fill(pxColor);
      pbOwner.textAlign(CENTER, CENTER);
      pbOwner.text(cmText,ccCenterX(),C_TEXT_ADJ_Y+ccCenterY());
      pbOwner.textAlign(LEFT,TOP);
    }//+++
    protected final void ccDrawName(int pxColor){
      int lpX=ccCenterX();
      int lpY=ccCenterY();
      switch (cmNameAlign) {
        //--
        case 'a':lpY=cmY-C_LABEL_OFFSET;pbOwner.textAlign(CENTER, BOTTOM);break;
        case 'b':lpY=C_LABEL_OFFSET+cmY+cmH;pbOwner.textAlign(CENTER, TOP);break;
        case 'l':lpX=cmX-C_LABEL_OFFSET;pbOwner.textAlign(RIGHT , CENTER);break;
        case 'r':lpX=C_LABEL_OFFSET+cmX+cmW;pbOwner.textAlign(LEFT  , CENTER);break;
        //--
        default:return;
      }
      //--
      pbOwner.fill(pxColor);
      pbOwner.text(cmName,lpX,C_TEXT_ADJ_Y+lpY);
      pbOwner.textAlign(LEFT,TOP);
    }//+++
    //-- ** teller
    public final int ccGetID(){return cmID;}//+++
    public final boolean ccGetActivity(){return cmAct;}//+++
    public final boolean ccIsMouseOver(){
      return ccContains(pbOwner.mouseX, pbOwner.mouseY)&&cmIsEnabled;
    }//+++
    public final boolean ccIsPressed(){
      return ccIsMouseOver()&&pbOwner.mousePressed;
    }//+++
    public final int ccTellMouseID(){
      return ccIsMouseOver()?cmID:0;
    }//+++
    //-- ** supporter
    protected final void ccActFill(){
      pbOwner.fill(cmAct?cmOnColor:cmOffColor);
    }//+++
    void ccCloneStyle(EcElement pxTarget){
      cmOnColor=pxTarget.cmOnColor;
      cmOffColor=pxTarget.cmOffColor;
      cmNameAlign=pxTarget.cmNameAlign;
    }//+++
    //--
  }//***
  
  static class EcButton extends EcElement{
    public EcButton(String pxName,int pxID)
      {this(pxName,8,8,pxID);ccAutoSize();}//+++
    public EcButton(String pxName, int pxW, int pxH, int pxID) {
      super();
      ccSetID(pxID);
      ccTakeKey(pxName);
      ccSetSize(pxW, pxH);
      ccSetColor(0xFFEEEE33, 0xFF111111);
      ccSetEnability(true);
    }//+++
    //--
    @Override void ccUpdate() {
      pbOwner.fill(C_BUTTON_STROKE);pbOwner.rect(cmX,cmY,cmW,cmH);
      pbOwner.fill(ccIsMouseOver()?
        (pbOwner.mousePressed?C_BUTTON_FILL_PRESSED:C_BUTTON_FILL_OVER)
        :C_BUTTON_FILL_NORMAL
      );
      pbOwner.rect(
        cmX+C_STROKE_WEIGHT,cmY+C_STROKE_WEIGHT,
        cmW-C_STROKE_WEIGHT*2,cmH-C_STROKE_WEIGHT*2
      );
      ccDrawTextAtCenter(cmAct?cmOnColor:cmOffColor);
    }//+++
  }//***
  
  static class EcLamp extends EcElement{
    public EcLamp(String pxName){this(pxName,C_TEXT_HEIGHT,C_TEXT_HEIGHT);}
    public EcLamp(String pxName, int pxW, int pxH) {
      super();
      ccTakeKey(pxName);
      ccSetText(pxName.substring(0, 1));
      ccSetSize(pxW, pxH);
      ccSetNameAlign('r');
    }//+++
    @Override void ccUpdate() {
      ccDrawRoundLampAtCenter();
      ccDrawTextAtCenter(C_LAMP_TEXT);
      ccDrawName(C_LABEL_TEXT);
    }//+++
    protected final void ccDrawRoundLampAtCenter(){
      int lpCenterX=ccCenterX();
      int lpCenterY=ccCenterY();
      pbOwner.fill(C_BUTTON_STROKE);
      pbOwner.ellipse(lpCenterX,lpCenterY,cmW,cmH);
      ccActFill();
      pbOwner.ellipse
        (lpCenterX,lpCenterY,cmW-C_STROKE_WEIGHT*2,cmH-C_STROKE_WEIGHT*2);
    }//+++
    @Override void ccCloneStyle(EcElement pxTarget){
      super.ccCloneStyle(pxTarget);
      cmW=pxTarget.cmW;
      cmH=pxTarget.cmH;
      cmText=pxTarget.cmText;
    }//+++
  }//***
  
  static class EcTextBox extends EcElement{
    private int cmTextColor;
    public EcTextBox(String pxName){this(pxName,8,8);ccAutoSize();}
    public EcTextBox(String pxName, int pxW, int pxH) {
      super();
      super.ccTakeKey(pxName);
      super.ccSetSize(pxW, pxH);
      cmTextColor=C_LAMP_TEXT;
    }//+++
    void ccSetTextColor(int pxColor){cmTextColor=pxColor;}
    @Override void ccUpdate() {
      pbOwner.fill(C_BUTTON_STROKE);
      pbOwner.rect(cmX+C_STROKE_WEIGHT,cmY+C_STROKE_WEIGHT,cmW,cmH);
      ccActFill();pbOwner.rect(cmX,cmY,cmW,cmH);
      pbOwner.fill(cmTextColor);
      pbOwner.textAlign(RIGHT, CENTER);
      pbOwner.text(cmText,cmX+cmW-C_BOX_MARG,C_TEXT_ADJ_Y+ccCenterY());
      pbOwner.textAlign(LEFT,TOP);
      ccDrawName(C_LABEL_TEXT);
    }//+++
  }//***
  
  static class EcGauge extends EcElement {
    private boolean cmHasStroke, cmIsVertical;
    private int cmBackColor,cmStrokeColor;
    protected int cmHalfByte;
    public EcGauge(String pxName){this(pxName,8,8);ccAutoSize();}
    public EcGauge(String pxName, int pxW, int pxH) {
      super();
      ccTakeKey(pxName);
      ccSetSize(pxW, pxH);
      ccSetColor(pbOwner.color(0xEE,0x33,0x33),pbOwner.color(0xEE,0xEE,0x33));
      cmHasStroke=true;
      cmIsVertical=true;
      cmBackColor=pbOwner.color(0x11,0x11,0x11);
      cmStrokeColor=pbOwner.color(0xCC,0xCC,0xCC);
      cmHalfByte=32;
    }//+++
    final void ccSetPercentage(float pxZeroToOne){
      cmHalfByte=ceil(127f*pxZeroToOne)&127;
    }//+++
    final void ccSetPercentage(int pxMaxAs_127){cmHalfByte=pxMaxAs_127&127;}//+++
    final void ccSetHasStrokeOrNot(boolean cmVal){cmHasStroke=cmVal;}//+++
    final void ccSetIsVerticalOrNot(boolean cmVal){cmIsVertical=cmVal;}//+++
    final void ccSetGaugeColor(int pxBack, int pxStroke){
      cmBackColor=pxBack;
      cmStrokeColor=pxStroke;
    }//+++
    @Override void ccUpdate() {
      int lpLength=((cmIsVertical?cmH:cmW)*cmHalfByte)/127;
      if(cmHasStroke){pbOwner.stroke(cmStrokeColor);}
      pbOwner.fill(cmBackColor);pbOwner.rect(cmX,cmY,cmW,cmH);
      ccActFill();
      if(cmIsVertical){pbOwner.rect(cmX,cmY+cmH,cmW,-lpLength);}
      else{pbOwner.rect(cmX,cmY,lpLength,cmH);}
      if(cmHasStroke){pbOwner.noStroke();}
      ccDrawName(C_LABEL_TEXT);
    }//+++
    final int ccGauge(int pxVal){return pxVal*cmHalfByte/127;}//+++
  }//***
  
  //=== inner ** solidus
  //[PLAN]::???
  
  //===  < inner
  
}//***eof

