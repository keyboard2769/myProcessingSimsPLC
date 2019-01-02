
class Gate extends TcWorldObject {

  Gate(float pxX, float pxY, float pxW, float pxH) {

    super();
    ccSetLocation(pxX, pxY);
    ccSetSize(pxW, pxH);

    //-- shape 
    Vec2 lpOffset=new Vec2(0, cmH/2-CP_PLATE_THICK/2);
    PolygonShape lpBarShape= new PolygonShape();
    lpBarShape.setAsBox(
    pbWorld.scalarPixelsToWorld(cmW/2), 
    pbWorld.scalarPixelsToWorld(CP_PLATE_THICK/2), 
    pbWorld.vectorPixelsToWorld(lpOffset), 0f
      );

    PolygonShape lpPillerShape=new PolygonShape();
    lpPillerShape.setAsBox(
    pbWorld.scalarPixelsToWorld(CP_PLATE_THICK/2), 
    pbWorld.scalarPixelsToWorld(cmH/2)
      );

    //-- body
    BodyDef lpDefinition=new BodyDef();
    lpDefinition.type=BodyType.DYNAMIC;
    lpDefinition.position.set(pbWorld.coordPixelsToWorld(
    new Vec2(cmX+cmW/2, cmY+cmH/2)
      ));

    cmBody=pbWorld.createBody(lpDefinition);
    cmBody.createFixture(lpPillerShape, 1f);
    cmBody.createFixture(lpBarShape, 1f);
  }//++!

  @Override public void ccUpdate() {

    Vec2 lpPosition=pbWorld.getBodyPixelCoord(cmBody);
    float lpAngle=cmBody.getAngle();

    pushMatrix();
    {
      translate(lpPosition.x, lpPosition.y);
      rotate(-lpAngle);
      //--
      fill(CC_METAL_FILL);
      stroke(CC_METAL_STROKE);
      rect(0, 0, CP_PLATE_THICK, cmH);
      rect(0, cmH/2-CP_PLATE_THICK/2, cmW, CP_PLATE_THICK);
      //--
      stroke(CC_DEVICE_STROKE);
      fill(CC_GATE_FILL);
      triangle(
      0, -cmH/2-CP_PLATE_THICK, 
      cmW/2, cmH/2-CP_PLATE_THICK, 
      -cmW/2, cmH/2-CP_PLATE_THICK
        );
    }
    popMatrix();
  }//+++
}//***

class TcGateUnit extends EcRect implements EiUpdatable {

  TcStraightBoundary cmBodary;
  Gate cmGate;
  RevoluteJoint cmJoint;
  Vec2 cmJointPosition;

  TcGateUnit(
  float pxBoundaryX, float pxBoundaryY, float pxThick, char pxMode_lr
    ) {
    super();

    int lpScale=9;
    ccSetSize(pxThick*lpScale, pxThick*5);
    ccSetLocation(pxBoundaryX-pxThick*4, pxBoundaryY);

    //-- body
    float lpGap=pxThick;
    cmBodary=new TcStraightBoundary(pxBoundaryX, pxBoundaryY, pxThick, cmH);
    cmGate =new Gate(cmX, cmY+cmH-lpGap/2, cmW, cmH);

    //-- joint
    cmJointPosition=cmBodary.cmBody.getWorldCenter().clone();
    Vec2 lpOffset=pbWorld.vectorPixelsToWorld(0, cmH/2-lpGap/2);
    cmJointPosition.addLocal(lpOffset);

    RevoluteJointDef lpDefinition=new RevoluteJointDef();
    lpDefinition.initialize
      (cmBodary.cmBody, cmGate.cmBody, cmJointPosition);
    lpDefinition.enableLimit=true;
    float lpBoundA=0.1f;
    float lpBoundB=0.0f;
    switch(pxMode_lr) {
    case 'l':
      lpBoundA=-PI/4;
      break;
    case 'r':
      lpBoundA=PI/4;
      break;
    default:
      break;
    }//..?
    lpDefinition.upperAngle=fnBigger(lpBoundA, lpBoundB);
    lpDefinition.lowerAngle=fnSmaller(lpBoundA, lpBoundB);

    cmJoint=(RevoluteJoint) pbWorld.world.createJoint(lpDefinition);
  }//+++

  @Override public void ccUpdate() {

    cmBodary.ccUpdate();
    cmGate.ccUpdate();

    Vec2 lpJpointPixelPosition=
      pbWorld.coordWorldToPixels(cmJointPosition);
    noStroke();
    fill(0xFFEEEE33);
    ellipse(
    lpJpointPixelPosition.x, 
    lpJpointPixelPosition.y, 
    4, 4
      );
  }//+++

  //===

  void ccMove(float pxPower) {
    cmGate.cmBody.applyTorque(pxPower);
  }//+++
}//***

