
class EcIndicator extends EcRect implements EiUpdatable {

  boolean cmIsActivated=false;
  int cmActColor=0xFFCCCCCC;

  @Override public void ccUpdate() {

    fill(cmIsActivated?cmActColor:CC_DEVICE_OFF_FILL);
    stroke(CC_DEVICE_STROKE);
    rect(ccCenterX(), ccCenterY(), cmW, cmH);
    noStroke();
  }//+++

  void ccSetIsActivated(boolean pxStatus) {
    cmIsActivated=pxStatus;
  }//+++

  void ccSetActivatedColor(int pxColor) {
    cmActColor=pxColor;
  }//+++
}//***

class EcWarper extends EcIndicator {

  int cmRadius=0;

  @Override public void ccUpdate() {
    if (cmIsActivated) {
      if (cmRadius<64) {
        cmRadius++;
      }
    }
    else {
      if (cmRadius>0) {
        cmRadius--;
      }
    }//..?
    if (cmRadius>0) {
      fill(cmActColor);
      ellipse(cmX, cmY, cmRadius, cmRadius);
    }//..?
  }//+++
}//***

