
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
    lpBodyDefine.position
      .set(pbWorld.coordPixelsToWorld(ccCenterX(), ccCenterY()));

    cmBody = pbWorld.createBody(lpBodyDefine);
    cmBody.createFixture(lpShape, 1);
  }//+++

  @Override public void ccUpdate() {

    fill(CC_METAL_FILL);
    stroke(CC_METAL_STROKE);
    rect(ccCenterX(), ccCenterY(), cmW, cmH);
  }//+++
}//***

class TcSlantingBoundary extends TcWorldObject {

  TcSlantingBoundary(float pxX, float pxY, float pxW, float pxH) {

    //--
    super();
    ccSetLocation(pxX, pxY);
    ccSetSize(pxW, pxH);

    //-- shape
    PolygonShape lpShape = new PolygonShape();
    Vec2[] lpDesVertice = new Vec2[4];

    float lpHalfW=cmW/2;
    float lpHalfH=cmH/2;

    lpDesVertice[0] = pbWorld.vectorPixelsToWorld(new Vec2(
    -lpHalfW, -lpHalfH
      ));
    lpDesVertice[1] = pbWorld.vectorPixelsToWorld(new Vec2(
    lpHalfW, lpHalfH+CP_PLATE_THICK
      ));
    lpDesVertice[2] = pbWorld.vectorPixelsToWorld(new Vec2(
    lpHalfW, lpHalfH
      ));
    lpDesVertice[3] = pbWorld.vectorPixelsToWorld(new Vec2(
    -lpHalfW, -lpHalfH+CP_PLATE_THICK
      ));
    lpShape.set(lpDesVertice, lpDesVertice.length);

    //-- body
    BodyDef lpDefinition = new BodyDef();
    lpDefinition.type = BodyType.STATIC;
    lpDefinition.position.set(pbWorld.coordPixelsToWorld(
    new Vec2(ccCenterX(), ccCenterY())
      ));

    cmBody = pbWorld.createBody(lpDefinition);
    cmBody.createFixture(lpShape, 1.0f);
  }//++!

  //===

  @Override public void ccUpdate() {

    //-- 
    Vec2 lpPosition = pbWorld.getBodyPixelCoord(cmBody);
    float lpAngle = cmBody.getAngle();

    Fixture lpFixture = cmBody.getFixtureList();
    PolygonShape lpShape = (PolygonShape) lpFixture.getShape();

    rectMode(CENTER);
    pushMatrix();
    {
      translate(lpPosition.x, lpPosition.y);
      rotate(-lpAngle);
      fill(CC_METAL_FILL);
      stroke(CC_METAL_STROKE);
      beginShape();
      for (int i = 0; i < lpShape.getVertexCount(); i++) {
        Vec2 lpPoint = pbWorld.vectorWorldToPixels(lpShape.getVertex(i));
        vertex(lpPoint.x, lpPoint.y);
      }//.~
      endShape(CLOSE);
    }
    popMatrix();
  }//+++
}//***

