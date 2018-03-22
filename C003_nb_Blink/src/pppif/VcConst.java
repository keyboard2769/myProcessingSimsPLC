/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pppif;

/**
 *
 * @author keypad on my mac11
 */
public class VcConst {
  
  private VcConst(){
    
    
  }//+++ 
  
  
  
  
  
  //--
  static void ccPrintln(String pxLine){
    System.out.println("[const]"+pxLine);}
  static void ccPrintkv(String pxTag, Object pxVal){
    System.out.println("[const]"+pxTag+":"+pxVal.toString());}
  //--
  static boolean ccIsIntegerOrNot(String pxNum)
    {return pxNum.matches("^[0-9]{1,16}$");}//+++
  static boolean ccIsFloatOrNot(String pxNum)
    {return pxNum.matches("^[0-9]{1,16}[.]{0,1}[0-9]{0,4}$");}//+++
  static boolean ccIsValidOrNot(String pxLine)
    {if(pxLine==null){return false;}else{return !pxLine.isEmpty();}}//+++
  static void ccPrintv(Object pxVal)
    {System.out.println("[const]"+pxVal.toString());}//+++
  static void ccPrintv(Object[] pxVal)
    {for(Object it:pxVal){System.out.println("[const]:"+it.toString());}}//+++
  //--
  
  
  
  
  
  synchronized static int[] ccSplitStringToIntArray(String pxCommand, String pxSplit){
    String[] lpTokens=pxCommand.split(pxSplit);
    int[] lpRes=new int[lpTokens.length];
    for(int i=0,s=lpRes.length;i<s;i++){
      if(ccIsIntegerOrNot(lpTokens[i])){
        lpRes[i]=Integer.decode(lpTokens[i]);
      }else{
        lpRes[i]=0;
      }
    }
    return lpRes;
  }//+++
  
  
  
  //--- --- --- ---
  //  title
  //--- --- --- ---
  //--- < title
}//***EOF

