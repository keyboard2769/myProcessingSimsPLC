
class TcAggregate extends TcWorldObject {

  boolean cmIsInAirRange;

  TcAggregate(float pxX, float pxY) {

    super();
    ccSetSize(
    random(CP_AGG_SIZE_MIN, CP_AGG_SIZE_MAX), 
    random(CP_AGG_SIZE_MIN, CP_AGG_SIZE_MAX)
      );

    cmIsInAirRange=false;

    //-- shape
    PolygonShape lpShape = new PolygonShape();
    lpShape.setAsBox(
    pbWorld.scalarPixelsToWorld(cmW / 2), 
    pbWorld.scalarPixelsToWorld(cmH / 2)
      );

    //-- body
    FixtureDef lpFixture = new FixtureDef();
    lpFixture.shape = lpShape;
    lpFixture.density     = 1.0f;
    lpFixture.friction    = 0.3f;
    lpFixture.restitution = 0.5f;

    BodyDef lpBody = new BodyDef();
    lpBody.type = BodyType.DYNAMIC;
    lpBody.position.set(pbWorld.coordPixelsToWorld(new Vec2(pxX, pxY)));

    cmBody = pbWorld.createBody(lpBody);
    cmBody.createFixture(lpFixture);
    cmBody.setLinearVelocity(new Vec2(
    random(-5f, 5f), 
    random(0f, -10f)
      ));
    cmBody.setAngularVelocity(random(-1f, 1f));
  }//++!

  //===

  @Override public void ccUpdate() {

    Vec2 lpPos = pbWorld.getBodyPixelCoord(cmBody);
    ccSetLocation(lpPos.x, lpPos.y);
    pushMatrix();
    {
      translate(cmX, cmY);
      rotate(-1 * cmBody.getAngle());
      fill(cmIsInAirRange?CC_AGG_AIR:CC_AGG_NORM);
      noStroke();
      rect(0, 0, cmW, cmH);
    }
    popMatrix();
  }//+++

  //===

  void ccDestroyBody() {
    pbWorld.destroyBody(cmBody);
  }//+++

  boolean ccIsDone() {
    Vec2 lpPos = pbWorld.getBodyPixelCoord(cmBody);
    if (lpPos.y > height + cmW * cmH) {
      ccDestroyBody();
      return true;
    }
    return false;
  }//+++

  void ccBlowed(Vec2 pxPower) {
    if (!cmIsInAirRange) {
      return;
    }
    cmBody.applyForce(
    pxPower, 
    cmBody.getWorldCenter()
      );
  }//+++

  //===

  void ccSetIsInAirRange(boolean pxStatus) {
    cmIsInAirRange=pxStatus;
  }//+++
}//***

