/* *
 * Aggregate Silo
 *
 * a silo stockes aggregates.
 * the silo has :
 *  - an airator to blast its contents when they get stuck.
 *  - a levelor to let maintainer know if it is fully loaded.
 *  - a dicsharge gate to discharge its contents.
 *  - last of all, a warper to transfer aggregates from the fourth dimension.
 *
 * (this is an excise project of learning box2d )
 * (built with Processing 2.x core)
 *
 * to download box2d library for processing :
 *   https://github.com/shiffman/Box2D-for-Processing
 *  
 */

import shiffman.box2d.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.common.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.*;

static int pbRoller;
static Vec2 pbAirPower;

//-- box 2d world object
static Box2DProcessing pbWorld;
static ArrayList<EiUpdatable> pbBoundaryList;
static ArrayList<TcAggregate> pbAggregateList;
static TcGateUnit pbGateLeft, pbGateRight;

//-- direct objects
static EcIndicator pbAirator, pbWarper, pbLevelor;

//--

void setup() {

  //-- presetting
  size(640, 480);
  noSmooth();
  textAlign(LEFT, TOP);
  rectMode(CENTER);

  //-- construction

  float lpStartX=250;
  float lpStartY=16;
  float lpSiloGateW=100;
  float lpSiloH=200;
  float lpSiloWaistW=100;
  float lpSiloWaistH=100;

  //-- construction ** direct 
  pbAirator=new EcIndicator();
  pbAirator.ccSetLocation(
  lpStartX+lpSiloWaistW+lpSiloGateW+lpSiloWaistW/3, 
  lpStartY+lpSiloH+lpSiloWaistH/6
    );
  pbAirator.ccSetSize(CP_PLATE_THICK*3, CP_PLATE_THICK*6);
  pbAirator.ccSetActivatedColor(CC_AIR_ON_FILL);

  pbWarper=new EcWarper();
  pbWarper.ccSetLocation(
  lpStartX+lpSiloWaistW+lpSiloGateW/2, 
  lpStartY+lpSiloWaistH/3
    );
  pbWarper.ccSetActivatedColor(CC_WARPER_FILL);

  pbLevelor=new EcIndicator();
  pbLevelor.ccSetLocation(
  lpStartX+lpSiloWaistH*2+lpSiloGateW-CP_PLATE_THICK*3, 
  lpStartY+lpSiloH*2/4
    );
  pbLevelor.ccSetSize(CP_PLATE_THICK*6, CP_PLATE_THICK);
  pbLevelor.ccSetActivatedColor(CC_LEVEL_ON_FILL);

  //-- construction ** box2d ** world
  pbWorld = new Box2DProcessing(this);
  pbWorld.createWorld();
  pbWorld.setGravity(0f, -10f);//..DO NOT CHANGE

  //-- construction ** box 2d ** container
  pbAggregateList = new ArrayList<TcAggregate>();
  pbBoundaryList = new ArrayList<EiUpdatable>();

  //-- construction ** box 2d ** gate
  pbGateLeft=new TcGateUnit(
  lpStartX+lpSiloWaistW, lpStartY+lpSiloH+lpSiloWaistH-CP_PLATE_THICK, 
  CP_PLATE_THICK, 'l'
    );
  pbGateRight=new TcGateUnit(
  lpStartX+lpSiloWaistW+lpSiloGateW-CP_PLATE_THICK, 
  lpStartY+lpSiloH+lpSiloWaistH-CP_PLATE_THICK, 
  CP_PLATE_THICK, 'r'
    );

  //-- construction ** misc
  pbAirPower=new Vec2(0, CR_AIR_POW);

  //-- configuration ** silo 
  pbBoundaryList.add(new TcStraightBoundary(
  lpStartX-CP_PLATE_THICK, lpStartY, 
  CP_PLATE_THICK, lpSiloH
    ));
  pbBoundaryList.add(new TcStraightBoundary(
  lpStartX+lpSiloWaistW*2+lpSiloGateW, lpStartY, 
  CP_PLATE_THICK, lpSiloH
    ));

  pbBoundaryList.add(new TcSlantingBoundary(
  lpStartX, lpStartY+lpSiloH-CP_PLATE_THICK, 
  lpSiloWaistW, lpSiloWaistH
    ));
  pbBoundaryList.add(new TcSlantingBoundary(
  lpStartX+lpSiloWaistW*2+lpSiloGateW, lpStartY+lpSiloH-CP_PLATE_THICK, 
  -lpSiloWaistW, lpSiloWaistH
    ));

  pbBoundaryList.add(new TcStraightBoundary(
  lpStartX+lpSiloWaistW, lpStartY+lpSiloH+lpSiloWaistH-CP_PLATE_THICK, 
  CP_PLATE_THICK, CP_PLATE_THICK*2
    ));
  pbBoundaryList.add(new TcStraightBoundary(
  lpStartX+lpSiloWaistW+lpSiloGateW-CP_PLATE_THICK, 
  lpStartY+lpSiloH+lpSiloWaistH-CP_PLATE_THICK, 
  CP_PLATE_THICK, CP_PLATE_THICK*2
    ));
}//+++

void draw() {

  background(CC_BACKGROUND);
  pbRoller++;
  pbRoller&=0x3F;

  //-- controlling
  boolean lpChargeSW=fnKeyPressed('r');
  boolean lpDischargeSW=fnKeyPressed('d');
  boolean lpAiratorSW=fnKeyPressed('f');

  pbAirator.ccSetIsActivated(lpAiratorSW);
  pbWarper.ccSetIsActivated(lpChargeSW);

  if ( lpChargeSW && (random(1) < CR_GENERATE_SPEED)) {
    if (!pbLevelor.cmIsActivated) {
      pbAggregateList.add(new TcAggregate(
      pbWarper.cmX+random(0, CP_GENERATE_RANDOM), 
      pbWarper.cmY+random(0, CP_GENERATE_RANDOM)
        ));
    }
  }//..?

  if (lpDischargeSW) {
    pbGateLeft.ccMove(-CR_GATE_POW);
    pbGateRight.ccMove(CR_GATE_POW);
  }
  else {
    pbGateLeft.ccMove(4*CR_GATE_POW);
    pbGateRight.ccMove(-4*CR_GATE_POW);
  }//..?

  //-- updating
  pbWorld.step();
  for (EiUpdatable it : pbBoundaryList) {
    it.ccUpdate();
  }
  for (TcAggregate it : pbAggregateList) {
    it.ccUpdate();
  }
  pbLevelor.ccSetIsActivated(false);
  for (int i = pbAggregateList.size() - 1; i >= 0; i--) {
    TcAggregate it = pbAggregateList.get(i);
    it.ccSetIsInAirRange
      (pbAirator.ccContains(it.cmX, it.cmY));
    if (pbLevelor.ccContains(it.cmX, it.cmY))
    {
      pbLevelor.ccSetIsActivated(true);
    }
    if (lpAiratorSW) {
      it.ccBlowed(pbAirPower);
    }
    if (it.ccIsDone()) {
      pbAggregateList.remove(i);
    }
  }//..~
  pbWarper.ccUpdate();
  pbGateLeft.ccUpdate();
  pbGateRight.ccUpdate();
  pbAirator.ccUpdate();
  pbLevelor.ccUpdate();

  //-- info
  fill(CC_INFO_TEXT);
  text("fps:"+nfc(frameRate, 2), 
  CP_INFO_X, CP_INFO_Y+CP_INFO_GRID*1);
  text("amount:"+nf(pbAggregateList.size(), 2), 
  CP_INFO_X, CP_INFO_Y+CP_INFO_GRID*2);
  if (pbRoller<31) {
    text(C_INFO, CP_INFO_X, CP_INFO_Y+CP_INFO_GRID*3);
  }//..?
}//+++

void keyPressed() {
  switch(key) {
  case 'q':
    println("-- exit from mod AggregateSilo simulator..");
    exit();
    break;
  default:
    break;
  }//..?
}//+++

