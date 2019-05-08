/* *
 * Belt Feeder
 *
 * a belt conveyor "feeds" aggregates from a stock hopper.
 * the feeder can :
 *  - get started or stopped.
 *  - get its speed adjusted.
 * and some time it sure got stuck with its self!!
 *
 * (this is an excise project of learning box2d )
 * (built with Processing 2.x core)
 *
 * to download box2d library for processing :
 *   https://github.com/shiffman/Box2D-for-Processing/tree/master/Box2D-for-Processing/dist
 *
 */

import processing.core.*;

import shiffman.box2d.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.common.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.*;

import java.util.ArrayList;

int pbRoller;

//-- box 2d world object
Box2DProcessing pbWorld;
EcBelt pbBelt;
EcMotor pbMotorL, pbMotorR;
ArrayList<TcStraightBoundary> pbBoundaryList;
ArrayList<TcAggregate> pbAggregateList;
ArrayList<EiUpdatable> pbStaticObjectList;

void setup() {

  //-- presetting
  size(400, 300);
  rectMode(CENTER);
  noSmooth();
  pbRoller=0;

  //-- construction 
  //-- construction ** box2d ** world
  pbWorld = new Box2DProcessing(this);
  pbWorld.createWorld();

  //-- construction ** box2d ** single
  pbBelt = new EcBelt();
  pbMotorL = new EcMotor(185, 150, 'l');
  pbMotorR = new EcMotor(215, 150, 'r');

  //-- construction ** box2d ** list
  pbBoundaryList = new ArrayList<TcStraightBoundary>();
  pbBoundaryList.add(new TcStraightBoundary(100, 10, 10, 80));
  pbBoundaryList.add(new TcStraightBoundary(100, 90, 50, 10));
  pbBoundaryList.add(new TcStraightBoundary(150, 90, 10, 20));

  pbBoundaryList.add(new TcStraightBoundary(250, 10, 10, 80));
  pbBoundaryList.add(new TcStraightBoundary(210, 90, 50, 10));
  pbBoundaryList.add(new TcStraightBoundary(200, 90, 10, 20));

  pbStaticObjectList = new ArrayList<EiUpdatable>();
  pbStaticObjectList.addAll(pbBoundaryList);
  pbStaticObjectList.add(pbBelt);
  pbStaticObjectList.add(pbMotorL);
  pbStaticObjectList.add(pbMotorR);

  pbAggregateList = new ArrayList<TcAggregate>();
}//+++

void draw() {

  background(CC_BACKGROUND);
  pbRoller++;
  pbRoller&=0x3F;

  //-- controlling
  boolean lpIsBlowSW=fsIsPressed('w');
  if (fsIsPressed('r')&&(random(1)<CR_GENERATE_SPEED)) {
    pbAggregateList.add(new TcAggregate(
    CP_AGG_GENP_X+random(-1*CP_GENERATE_RANDOM, CP_GENERATE_RANDOM), 
    CP_AGG_GENP_Y, 
    random(CP_AGG_SIZE_MIN, CP_AGG_SIZE_MAX)));
  }//..?

  //-- updating
  pbWorld.step();
  for (EiUpdatable it : pbStaticObjectList) {
    it.ccUpdate();
  }
  for (TcAggregate it : pbAggregateList) {
    it.ccUpdate();
  }
  for (int i = pbAggregateList.size()-1;i>=0;i--) {
    TcAggregate it=pbAggregateList.get(i);
    if (lpIsBlowSW) {
      it.ccApplyForce(CR_DEGRA_POW);
    }
    if (it.ccIsDone()) {
      pbAggregateList.remove(i);
    }
  }//..~

  //--
  pushStyle();
  {
    fill(CC_INFO_TEXT);
    text(
      "fps:"+nfc(frameRate, 2), 
      CP_INFO_X, CP_INFO_Y+CP_INFO_GRID*1
    );
    text(
      "amount:"+nf(pbAggregateList.size(), 2), 
      CP_INFO_X, CP_INFO_Y+CP_INFO_GRID*2
    );
    if (pbRoller<31) {
      text(C_INFO, CP_INFO_X, CP_INFO_Y+CP_INFO_GRID*3);
    }//..?
  }
  popStyle();
}//+++

void keyPressed() {
  switch(key) {

  case 's':
    pbMotorL.ccFlipMotorStatus();
    pbMotorR.ccFlipMotorStatus();
  break;

  case 'd':
    pbMotorL.ccAdjustPower(1f);
    pbMotorR.ccAdjustPower(1f);
  break;

  case 'a':
    pbMotorL.ccAdjustPower(-1f);
    pbMotorR.ccAdjustPower(-1f);
  break;

  case 'q':
    fsPover();
  break;

  default:break;
  }//..?
}//+++