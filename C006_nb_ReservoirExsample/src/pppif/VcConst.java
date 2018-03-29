/* --def %revision%
 * #[flag] "version/filename" : issue $ describe,
 * [ ] "": $,,,
 * [ ] "": $,,,
 * [ ] "": $,,,
 * --end
 */

package pppif;


import static processing.core.PApplet.nf;
import static processing.core.PApplet.year;
import static processing.core.PApplet.month;
import static processing.core.PApplet.day;
import static processing.core.PApplet.hour;
import static processing.core.PApplet.minute;

public final class VcConst {
  
  private VcConst(){}//+++ 
  
  //== printing
  
  static public final void ccPrintln(String pxLine)
    {System.out.println("[const]"+pxLine);}//+++
  
  static public final void ccPrintln(String pxTag, Object pxVal)
    {System.out.println("[const]"+pxTag+":"+pxVal.toString());}//+++
  
  static public final void ccPrintln(Object[] pxVal)
    {for(Object it:pxVal){System.out.println("[const]:"+it.toString());}}//+++
  
  static public final void ccPrintln(Object pxVal)
    {System.out.println("[const]"+pxVal.toString());}//+++
  
  //== judgement
  
  static boolean ccIsIntegerString(String pxNum)
    {return pxNum.matches("^[0-9]{1,16}$");}//+++
  
  static boolean ccIsFloatString(String pxNum)
    {return pxNum.matches("^[0-9]{1,16}[.]{0,1}[0-9]{0,4}$");}//+++
  
  static boolean ccIsValidString(String pxLine)
    {if(pxLine==null){return false;}else{return !pxLine.isEmpty();}}//+++
  
  //== util
  
  //[TOIMP]::char pxMode_ydas
  static public final String ccTimeStamp(){
    StringBuilder lpRes=new StringBuilder("  --");
    lpRes.append(nf(hour(),2));
    lpRes.append(":");
    lpRes.append(nf(minute(),2));
    return lpRes.toString();
  }//+++
  
  static int ccToPowerOfTwo(int pxVal){
    int lpMasked=pxVal&0xFFFF;
    int lpTester=0x00008000;
    while(lpTester!=1){
      if( (lpTester&lpMasked)!=0 ){break;}
      lpTester>>=1;
    }
    return lpTester==lpMasked?lpTester:lpTester*2;
  }//+++
  
  synchronized static int[] ccSplitStringToIntArray(String pxCommand, String pxSplit){
    String[] lpTokens=pxCommand.split(pxSplit);
    int[] lpRes=new int[lpTokens.length];
    for(int i=0,s=lpRes.length;i<s;i++){
      if(ccIsIntegerString(lpTokens[i])){
        lpRes[i]=Integer.decode(lpTokens[i]);
      }else{
        lpRes[i]=0;
      }
    }
    return lpRes;
  }//+++
  
  //== <
  
}//***eof

