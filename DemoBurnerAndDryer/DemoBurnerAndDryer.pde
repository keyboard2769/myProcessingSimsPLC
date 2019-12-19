/* *
 * Burner and Dryer
 * 
 * a fire-blasting blower, aka the burner, 
 *   heats and dries some aggregates sent to a rolling barrel, aka the dryer.
 *
 * the burner has :
 *  - a ignitor turns its fire on or off. 
 *  - a controllable air damper restrict the power of fire. 
 *  - a varistor measured in percentage indicated as degree of the damper. 
 * 
 * the dryer has :
 *  - a temperature sensor indicates how hot the dryer barrel currently is. 
 *  - a flux scaler in scaler indicates how may aggregate is sent. 
 *  - a cooling damper drains atomsphere air into the dryer barrel on occasion.
 *
 * other control descriptions can be found inside the dialog window.
 * to exit, press the 'q' key.
 *
 */

private static DemoBurnerAndDryer self=null;

//-- constant
static final float C_TEMP_BURN_MAX = 800.0f;
static final float C_TEMP_ATOM_CON =  27.5f;
static final float C_TEMP_INWD_CON =  23.5f;
static final float C_TR_SLOW = 256f;
static final float C_TR_FAST =  64f;

//-- system
int pbRoller = 0;

//-- local ui
EcElement pbBurnerICON      = new EcElement();
EcElement pbBurnerClosePL   = new EcElement("-");
EcElement pbBurnerDegreeTB  = new EcElement();
EcElement pbBurnerOpenPL    = new EcElement("+");
EcElement pbBurnerIgnitorPL = new EcElement(" ");
EcElement pbDryerICON       = new EcElement();
EcElement pbDryerDegreeTB   = new EcElement();
EcElement pbDryerFluxTB     = new EcElement();
EcElement pbCoolingDamperPL = new EcElement(" ");

//-- emulated
ZcFlicker fbSamplingClockTimer = new ZcFlicker(8);
ZcFlicker fbAdjustClockTimer   = new ZcFlicker(80);
ZcController fbTemperatureCTRL = new ZcController();
ZcController fbDegreeCTRL      = new ZcController();

//-- simulated
boolean dcBurnerCloseMV     = false;
boolean dcBurnerOpenMV      = false;
boolean dcBurnerIgnitorMV   = false;
boolean dcCoolingDamperMV   = false;
boolean dcColdAggregateLS   = false;
float   dcDryerFlux         = 160.0f;
ZcDamper simBurnerDamper        = new ZcDamper();
ZcReal simBurnerTemperature     = new ZcReal(C_TEMP_INWD_CON,true);
ZcReal simDryerTemperature      = new ZcReal(C_TEMP_INWD_CON);
ZcReal simAtomsphereTemperature = new ZcReal(C_TEMP_ATOM_CON,true);
ZcReal simAggregateTemperature  = new ZcReal(C_TEMP_INWD_CON/2f);

void setup() {
  
  size(320,240,JAVA2D);
  noSmooth();
  self=this;
  
  //-- replace setting
  frameRate(16);noStroke();textAlign(LEFT, TOP);ellipseMode(CENTER);
  
  //-- local ui
  int lpPotentialW, lpPotentialH;
  
  //-- local ui ** burner
  lpPotentialW = 48;
  lpPotentialH = 18;
  pbBurnerClosePL.cmRegion.setBounds(5,25,lpPotentialH,lpPotentialH);
  pbBurnerDegreeTB.cmOffColor=0xFFCCCCCC;
  pbBurnerDegreeTB.cmRegion.setSize(lpPotentialW, lpPotentialH);
  pbBurnerDegreeTB.ccFollowH(pbBurnerClosePL, 2);
  pbBurnerOpenPL.cmRegion.setSize(lpPotentialH, lpPotentialH);
  pbBurnerOpenPL.ccFollowH(pbBurnerDegreeTB, 2);
  lpPotentialW =
    ccGetEndX(pbBurnerOpenPL.cmRegion)
     - pbBurnerClosePL.cmRegion.x;
  lpPotentialH = 
    lpPotentialW *2 /3;
  pbBurnerICON.cmRegion.setSize(lpPotentialW, lpPotentialH);
  pbBurnerICON.ccFollowV(pbBurnerClosePL, 2);
  pbBurnerIgnitorPL.cmOnColor = 0xFFEECC99;
  pbBurnerIgnitorPL.cmRegion.setLocation(
    ccGetEndX(pbBurnerICON.cmRegion) -pbBurnerIgnitorPL.cmRegion.width -2,
    ccGetCenterY(pbBurnerICON.cmRegion) -pbBurnerIgnitorPL.cmRegion.height -2
  );
  
  //-- local ui ** dryer
  lpPotentialH = lpPotentialW;
  lpPotentialW = lpPotentialH*2;
  pbDryerICON.cmRegion.setSize(lpPotentialW, lpPotentialH);
  pbDryerICON.ccFollowE(pbBurnerICON, 5);
  pbDryerICON.cmRegion.y -= pbBurnerICON.cmRegion.height/4;
  lpPotentialW = 54;
  lpPotentialH = 22;
  pbDryerDegreeTB.cmOffColor=0xFFEECCCC;
  pbDryerDegreeTB.cmRegion.setBounds(ccGetCenterX(pbDryerICON.cmRegion),
    ccGetCenterY(pbDryerICON.cmRegion),
    lpPotentialW, lpPotentialH
  );
  lpPotentialW = 60;
  lpPotentialH = 22;
  pbDryerFluxTB.cmOffColor =0xFF999999;
  pbDryerFluxTB.cmOnColor  =0xFFEEEECC;
  pbDryerFluxTB.cmRegion.setSize(lpPotentialW, lpPotentialH);
  pbDryerFluxTB.ccFollowH(pbDryerDegreeTB, 10);
  pbCoolingDamperPL.cmOnColor = 0xFF99CCEE;
  pbCoolingDamperPL.cmRegion.setLocation(
    pbDryerICON.cmRegion.x+pbDryerICON.cmRegion.width*5/6+8,
    pbDryerICON.cmRegion.y+2
  );
  
  //-- swing ui
  SwingUtilities.invokeLater(O_SWING_INIT);
  
  //-- emulated
  fbDegreeCTRL.cmRangeMinimum=  0f;
  fbDegreeCTRL.cmRangeMaximum=100f;
  fbDegreeCTRL.ccSetDead(0.02f);
  fbDegreeCTRL.ccSetProportion(0.10f);
  fbTemperatureCTRL.ccSetTargetValue(mnCTRLTargetCELC);
  fbTemperatureCTRL.ccSetDead(mnCTRLDeadFACT);
  fbTemperatureCTRL.ccSetProportion(mnCTRLProportionFACT);
  fbSamplingClockTimer.ccSetTimer(ccToFrameCount(mnCTRLSamplingSEC));
  fbAdjustClockTimer.ccSetTimer(ccToFrameCount(mnCTRLAdjustSEC));
  
}//++!

void draw() {

  //-- pre drawing
  background(0);
  pbRoller++;pbRoller&=0x0f;
  
  //-- emulated ** scan ** controller ** clock
  fbSamplingClockTimer.ccRun();
  fbAdjustClockTimer.ccRun();
  
  //-- emulated ** scan ** controller ** temperature
  fbTemperatureCTRL.ccRun(simDryerTemperature.cmVal,
    dcBurnerIgnitorMV & fbSamplingClockTimer.ccGetPulse(),
    dcBurnerIgnitorMV & fbAdjustClockTimer.ccGetPulse()
  );
  
  //-- emulated ** scan ** controller ** degree
  fbDegreeCTRL.ccSetTargetValue(constrain(
    fbTemperatureCTRL.ccGetAnalogOutput()*100f,0f,100f
  ));
  fbDegreeCTRL.ccRun(
    (float)simBurnerDamper.ccToPercentage()
  );
  
  //-- emulated ** scan ** flag
  boolean lpBurnerAutoCloseFLG = sel(
    dcBurnerIgnitorMV,fbDegreeCTRL.ccGetNegativeOutput(),
    sel(simBurnerDamper.ccToPercentage()<=3,false,true)
  );
  boolean lpBurnerAutoOpenFLG = sel(
    dcBurnerIgnitorMV,fbDegreeCTRL.ccGetPositiveOutput(),false
  );
  
  //-- emulated ** scan ** output
  dcBurnerCloseMV = gate(
    mnBurnerAutoFLG, lpBurnerAutoCloseFLG,
    mnBurnerManualFLG, mnBurnerManualCloseFLG
  );
  dcBurnerOpenMV = gate(
    mnBurnerAutoFLG, lpBurnerAutoOpenFLG,
    mnBurnerManualFLG, mnBurnerManualOpenFLG
  );
  dcBurnerIgnitorMV = mnBurnerFireFLG;
  dcCoolingDamperMV = sel(
    mnCoolingDamperAutoFLG,
    (simDryerTemperature.cmVal>mnCooldownTemperature),
    mnCoolingDamperForceFLG
  );
  
  //-- emulated ** simulate ** device
  dcColdAggregateLS = (mnFluxTPH>20.0f);
  simBurnerDamper.ccClose(dcBurnerCloseMV);
  simBurnerDamper.ccOpen(dcBurnerOpenMV);
  
  //-- emulated ** simulate ** real
  simBurnerTemperature.cmVal=sel(dcBurnerIgnitorMV,
    (simBurnerDamper.ccToProportion()+0.02f)*C_TEMP_BURN_MAX,
    C_TEMP_INWD_CON
  );
  if((pbRoller==7) && (random(1f)<0.33f)){
    simAtomsphereTemperature.cmVal=C_TEMP_ATOM_CON*random(0.75f,1.25f);
  }//..?
  ccTransfer(simBurnerTemperature, simDryerTemperature, C_TR_SLOW);
  ccTransfer(simDryerTemperature,simAtomsphereTemperature,
    sel(dcCoolingDamperMV, C_TR_FAST, C_TR_SLOW)
  );
  ccTransfer(simAggregateTemperature, simAtomsphereTemperature, C_TR_SLOW);
  ccTransfer(
    simDryerTemperature, simAggregateTemperature,
    sel(dcColdAggregateLS, map(mnFluxTPH,360f,1f,C_TR_FAST,C_TR_SLOW)/2f, 0f)
  );
  
  //-- bind
  pbBurnerClosePL.cmActivated=dcBurnerCloseMV;
  pbBurnerOpenPL.cmActivated=dcBurnerOpenMV;
  pbBurnerIgnitorPL.cmActivated=dcBurnerIgnitorMV&&(pbRoller%6>4);
  pbCoolingDamperPL.cmActivated=dcCoolingDamperMV;
  pbDryerFluxTB.cmActivated=dcColdAggregateLS;
  
  //-- local ui ** passive
  ccDrawAsBlower(pbBurnerICON);
  ccDrawAsDryer(pbDryerICON);
  
  //-- local ui ** line-inicator
  ccDrawController(fbTemperatureCTRL);
  ccDrawAsLabel(pbBurnerIgnitorPL);
  ccDrawAsLabel(pbCoolingDamperPL);
  
  //-- local ui ** active ** burner
  ccDrawAsLabel(pbBurnerClosePL);
  ccDrawAsValueBox(pbBurnerDegreeTB, simBurnerDamper.ccToPercentage(), "%");
  ccDrawAsLabel(pbBurnerOpenPL);
  
  //-- local ui ** active ** burner
  ccDrawAsValueBox(pbDryerDegreeTB, ceil(simDryerTemperature.cmVal), "`C");
  ccDrawAsValueBox(pbDryerFluxTB, ceil(mnFluxTPH), "tph");
  
  //-- local ui ** active ** controller
  ccSurroundText(
    fbTemperatureCTRL.ccToInicator("Temperature Controller"),
    10, 125
  );
  ccSurroundText(
    fbDegreeCTRL.ccToInicator("Degree Controller"),
    170, 125
  );
  
  //-- watch
  fill(0xFF66CC66);
  text(String.format(
    "[r:%02d]|[m:(%03d,%03d)]|[f:%.2f]",
    pbRoller,mouseX,mouseY,frameRate
  ),5,5);
  
}//++~

void keyPressed() {
  switch(key){
    case 'q':exit();break;
    default:break;
  }//..?
}//+++

void mousePressed() {
  switch (mouseButton) {
    case LEFT:return;
    case RIGHT:SwingUtilities.invokeLater(O_SWING_FLIP);break;
    default:break; //+++
  }//..?
}//+++