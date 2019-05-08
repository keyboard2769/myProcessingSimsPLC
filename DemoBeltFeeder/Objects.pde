
class TcStraightBoundary extends TcWorldObject {

  TcStraightBoundary(float pxX, float pxY, float pxW, float pxH) {

    super();
    ccSetLocation(pxX, pxY);
    ccSetSize(pxW, pxH);

    //-- shape
    PolygonShape lpShape = new PolygonShape();
    lpShape.setAsBox(
      pbWorld.scalarPixelsToWorld(cmW / 2), 
      pbWorld.scalarPixelsToWorld(cmH / 2)
    );

    //-- body
    BodyDef lpBodyDefine = new BodyDef();
    lpBodyDefine.type = BodyType.STATIC;
    lpBodyDefine.position.set(
      pbWorld.coordPixelsToWorld(ccCenterX(), ccCenterY())
    );

    cmBody = pbWorld.createBody(lpBodyDefine);
    cmBody.createFixture(lpShape, 1);
    
  }//+++

  @Override public void ccUpdate() {
    fill(CC_METAL_FILL);
    stroke(CC_METAL_STROKE);
    rect(ccCenterX(), ccCenterY(), cmW, cmH);
  }//+++
  
}//***

class TcAggregate extends TcWorldObject {

  float cmRadius;
  int cmColor;

  TcAggregate(float pxX, float pxY, float pxRadius) {

    cmRadius = pxRadius;
    cmColor = CC_AGG_NORM;

    //--
    CircleShape lpShape = new CircleShape();
    lpShape.m_radius = pbWorld.scalarPixelsToWorld(pxRadius);

    //--
    FixtureDef lpFixture = new FixtureDef();
    lpFixture.shape = lpShape;
    lpFixture.density = 2.0f;
    lpFixture.friction = 9.9f;
    lpFixture.restitution = 0.3f;

    //--
    BodyDef lpDefinition = new BodyDef();
    lpDefinition.position = pbWorld.coordPixelsToWorld(pxX, pxY);
    lpDefinition.type = BodyType.DYNAMIC;
    cmBody = pbWorld.world.createBody(lpDefinition);
    cmBody.createFixture(lpFixture);
    cmBody.setAngularVelocity(random(-10, 10));
    
  }//++!

  //===

  @Override public void ccUpdate() {
    Vec2 lpPosition = pbWorld.getBodyPixelCoord(cmBody);
    float lpAngle = cmBody.getAngle();
    pushMatrix();
    {
      translate(lpPosition.x, lpPosition.y);
      rotate(-lpAngle);
      fill(cmColor);
      stroke(0);
      strokeWeight(1);
      ellipse(0, 0, cmRadius * 2, cmRadius * 2);
    }
    popMatrix();
  }//+++

  //===

  void ccDestroyBody() {
    pbWorld.destroyBody(cmBody);
  }//+++

  void ccApplyForce(float pxForce) {
    cmBody.applyForceToCenter(new Vec2(0, pxForce));
  }//+++

  boolean ccIsDone() {
    Vec2 pos = pbWorld.getBodyPixelCoord(cmBody);
    if (pos.y > height + cmRadius * 2) {
      ccDestroyBody();
      return true;
    }
    return false;
  }//+++
}//***

