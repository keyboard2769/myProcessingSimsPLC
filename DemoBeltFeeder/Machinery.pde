
class EcMotor implements EiUpdatable {

  TcAggregate cmRotor;
  TcStraightBoundary cmAxel;
  RevoluteJoint cmJoint;

  EcMotor(float pxCenterX, float pxCenterY, char pxMode_lr) {

    //--
    float lpDirectOffsetX;
    if (pxMode_lr=='r') {
      lpDirectOffsetX=20;
    }
    else {
      lpDirectOffsetX=-20;
    }//..?
    cmRotor = new TcAggregate(pxCenterX+lpDirectOffsetX, pxCenterY, 15);
    cmRotor.cmColor = CC_ROTOR_COLOR;

    //--
    float lpW=30;
    float lpH=10;
    float lpX=pxCenterX-lpW/2;
    float lpY=pxCenterY-lpH/2;
    cmAxel = new TcStraightBoundary(lpX, lpY, lpW, lpH);

    //--
    RevoluteJointDef lpDefinition = new RevoluteJointDef();
    lpDefinition.initialize(
      cmRotor.cmBody, 
      cmAxel.cmBody, 
      cmRotor.cmBody.getWorldCenter()
    );
    lpDefinition.motorSpeed = PI * 2;
    lpDefinition.maxMotorTorque = 1000.0f;
    lpDefinition.enableMotor = true;

    cmJoint = (RevoluteJoint) pbWorld.world.createJoint(lpDefinition);
  }//++!

  //===

  void ccAdjustPower(float pxRank) {
    float lpSpeed=cmJoint.getMotorSpeed();
    float lpTorque=cmJoint.getMaxMotorTorque();

    lpSpeed+=pxRank*0.3f;
    lpTorque+=pxRank*10;

    cmJoint.setMotorSpeed(lpSpeed);
    cmJoint.setMaxMotorTorque(lpTorque);
  }//+++

  float ccGetMotorSpeed() {
    return cmJoint.getMotorSpeed();
  }//+++

  float ccGetMotorTorque() {
    return cmJoint.getMaxMotorTorque();
  }//+++

  void ccFlipMotorStatus() {
    cmJoint.enableMotor(!cmJoint.isMotorEnabled());
  }//+++

  boolean ccIsMotorEnabled() {
    return cmJoint.isMotorEnabled();
  }//+++

  //===

  @Override public void ccUpdate() {

    //-- updating
    cmAxel.ccUpdate();
    cmRotor.ccUpdate();

    //-- draw joint
    Vec2 lpAnchor = pbWorld.coordWorldToPixels(
      cmRotor.cmBody.getWorldCenter()
    );
    fill(ccIsMotorEnabled()?
      CC_MOTOR_ON:CC_METAL_FILL  
    );
    ellipse(lpAnchor.x, lpAnchor.y, 4, 4);
  }//+++
}//***

class EcBelt implements EiUpdatable {

  float cmCutRadius;  
  float cmBeltLengthRadius;
  float cmCutAmount; 

  ArrayList<Body> cmCutList;
  ConstantVolumeJointDef cmJointDefinition;

  //===

  EcBelt() {

    cmBeltLengthRadius = 40;
    cmCutAmount = 40;
    cmCutRadius = 4;

    cmCutList = new ArrayList<Body>();
    cmJointDefinition = new ConstantVolumeJointDef();

    //-- 
    int lpStartX=150;
    int lpStartY=125;
    int lpBeltW=90;
    int lpBeltH=40;
    int lpCutDiff=10;
    for (int i=0;i<lpBeltW;i+=lpCutDiff)
    {
      ccAddCut(lpStartX+i, lpStartY);
    }
    for (int i=0;i<lpBeltH;i+=lpCutDiff) {
      ccAddCut(lpStartX+lpBeltW, lpStartY+i);
    }
    for (int i=0;i<lpBeltW;i+=lpCutDiff)
    {
      ccAddCut(lpStartX+lpBeltW-i, lpStartY+lpBeltH);
    }
    for (int i=0;i<lpBeltH;i+=lpCutDiff)
    {
      ccAddCut(lpStartX, lpStartY+lpBeltH-i);
    }

    //-- 
    cmJointDefinition.frequencyHz = 24f;
    cmJointDefinition.dampingRatio = 1f;
    cmJointDefinition.collideConnected=true;
    pbWorld.world.createJoint(cmJointDefinition);
  }//++!

  void ccAddCut(float pxX, float pxY) {
    Body lpBody=ccCreateBeltCut(pxX, pxY);
    cmJointDefinition.addBody(lpBody);
    cmCutList.add(lpBody);
  }//+++

  Body ccCreateBeltCut(float pxX, float pxY) {
    CircleShape lpShape = new CircleShape();
    lpShape.m_radius = pbWorld.scalarPixelsToWorld(4f);
    FixtureDef lpFixture = new FixtureDef();
    lpFixture.shape = lpShape;
    lpFixture.density = 1f;
    lpFixture.friction= 3f;
    BodyDef lpBodyDefinition = new BodyDef();
    lpBodyDefinition.type = BodyType.DYNAMIC;
    lpBodyDefinition.fixedRotation = true; // no rotation!
    lpBodyDefinition.position.set(pbWorld.coordPixelsToWorld(pxX, pxY));
    Body lpBody = pbWorld.createBody(lpBodyDefinition);
    lpBody.createFixture(lpFixture);
    return lpBody;
  }//+++

  @Override public void ccUpdate() {

    //-- draw joint
    beginShape();
    noFill();
    stroke(CC_JOINT_COLOR);
    strokeWeight(4);
    for (Body it : cmCutList) {
      Vec2 lpPosition = pbWorld.getBodyPixelCoord(it);
      vertex(lpPosition.x, lpPosition.y);
    }//..~
    endShape(CLOSE);
    strokeWeight(1);

    //-- draw cut
    for (Body it : cmCutList) {

      Vec2 lpPosition = pbWorld.getBodyPixelCoord(it);
      float lpAngle = it.getAngle();

      pushMatrix();
      {
        translate(lpPosition.x, lpPosition.y);
        rotate(lpAngle);
        fill(CC_BELT_FILL);
        ellipse(0, 0, cmCutRadius * 2, cmCutRadius * 2);
        stroke(CC_BELT_STROKE);
        line(0, 0, cmCutRadius, 0);
      }
      popMatrix();
    }//..~
  }//+++
}//***

