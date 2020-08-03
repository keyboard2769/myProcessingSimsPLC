/* *
 * Drum Injection
 *
 *  an injection-pump, inject additive liquid into a rolling dryer, aka drum,
 *   automatically adjust its own speed synced with other aggregates suppliment, 
 *   from a liquid tank.
 *  
 * the pump can get started/stopped manually.
 * the pump speed can NOT be adjusted manually.
 * 
 * other control descriptions can be found inside the dialog window.
 * to exit, press the 'q' key.
 *
 */
 
static private DemoDrumInjection self;

//=== const 

static final float C_TPH_MAX =   90f;
static final float C_RPM_MAX = 1800f;

static final int C_COLOR_METAL = 0xFF787878;
static final int C_COLOR_DUCT  = 0xFFABABAB;

static final int C_PIX_BOX_W   = 64;
static final int C_PIX_BOX_H   = 24;
 
 
//=== overridden

Rectangle cmHopper = new Rectangle( 48,  48);
Rectangle cmBelcon = new Rectangle(140,  16);
Rectangle cmTank   = new Rectangle( 60, 100);
Rectangle cmPipe   = new Rectangle(110, 100);
Rectangle cmDrum   = new Rectangle(140,  60);
EcPumpIcon cmPump  = new EcPumpIcon();
ZcController cmController = new ZcController();

void setup() {

  //-- pre
  size(320, 240);
  noSmooth();
  self=this;
  
  //-- replace
  frameRate(16);
  textAlign(LEFT,TOP);
  ellipseMode(CENTER);
  noStroke();

  //-- swing ui **
  SwingUtilities.invokeLater(T_SWING_INIT);
  
  //-- local ui **
  ccSetLocation(cmHopper, 25, 12);
  ccFollowSouth(cmBelcon, cmHopper, 8);
  ccFollowEast(cmDrum,cmBelcon,8);
  ccSetLocation(cmTank,25,120);
  ccSetLocation(cmPipe,
    ccGetEndX(cmTank)-8,
    ccGetEndY(cmDrum)-14
  );
  ccSetLocation(cmPump.cmBound,
    ccGetEndX(cmPipe) - (cmPump.cmBound.width *2),
    ccGetEndY(cmPipe) - (cmPump.cmBound.height/2)
  );
  
}//+++
  
void draw() { 

  //-- pre
  vmRoller++;vmRoller&=0x0F;
  
  //-- update ** passive
  background(0);
  ssDrawHopper(cmHopper);
  ssDrawTank(cmTank);
  ssDrawDrum(cmDrum);
  ssDrawPipe(cmPipe);
  
  //-- update ** logic
  cmController.ccUpdate();
  
  //-- update ** active
  ssDrawBelcon(cmBelcon,cmController.cmFeederMC);
  ccDrawIntegerAsBox((int)(cmController.cmFeederRPM), "RPM",
    ccGetEndX(cmHopper)+8,
    cmHopper.y+2
  );
  ccDrawFloatAsBox(
    vmPumpTPH*1f, "TPH",
    ccGetEndX(cmPipe)+8,
    ccGetCenterY(cmPipe)
  );
  ccDrawIntegerAsBox(
    (int)(cmController.cmPumpRPM*1f), "RPM",
    ccGetEndX(cmPipe)+8,
    ccGetCenterY(cmPipe)+2+C_PIX_BOX_H
  );
  ccDrawBitAsRedlit(
    (vmRoller%4>2)&&(vmDelayTimeACTIVATOR>2),
    ccGetEndX(cmPipe)+8+8+C_PIX_BOX_W,
    ccGetCenterY(cmPipe)+2+C_PIX_BOX_H
  );
  cmPump.cmActivated=cmController.cmPumpMC;
  cmPump.cmSpeed=(cmController.cmPumpRPM/C_RPM_MAX)*1.0f;
  cmPump.ccUpdate();
  
}//+++
   
void keyPressed() {
  switch(key){
    case 'q':exit();break;
    default:break;
  }//..?
}//+++

void mousePressed() {
  switch (mouseButton) {
    case LEFT:return;
    case RIGHT:SwingUtilities.invokeLater(T_SWING_FLIP);break;
    default:break; //+++
  }//..?
}//+++

//***eof