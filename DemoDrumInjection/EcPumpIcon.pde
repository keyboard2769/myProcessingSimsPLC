class EcPumpIcon{
  
  float cmHeading = 1.0f;
  float cmSpeed = 0.1f;
  boolean cmActivated = true;
  Rectangle cmBound = new Rectangle(8, 8, 24, 24);
  
  void ccUpdate(){
    
    if(cmActivated){
      cmHeading+=cmSpeed;
      if(cmHeading>3.14f){cmHeading=(cmHeading%3.14f);}
    }//..?
    
    pushStyle();pushMatrix();
    {
      rectMode(CENTER);
      stroke(C_COLOR_DUCT);
      translate(ccGetCenterX(cmBound), ccGetCenterY(cmBound));
      rotate(cmHeading);
      
      //-- cover
      strokeWeight(1.25f);
      fill(cmActivated?0xFF117777:0xFF333333);
      ellipse(
        0,0,
        cmBound.width, cmBound.height
        );
      
      //-- shafter
      noStroke();
      fill(C_COLOR_DUCT);
      rect(
        0,0,
        cmBound.width/4, cmBound.height*3/4,
        2,2,2,2
      );
      
    }
    popStyle();popMatrix();
    
  }//+++
  
  //=== interface

  @Override public String toString() {
    return String.format(
      "pump$h:%.2f;s:%.2f;a:%c;",
      cmHeading,cmSpeed,cmActivated?'o':'x'
    );
  }//+++
  
}//***eof