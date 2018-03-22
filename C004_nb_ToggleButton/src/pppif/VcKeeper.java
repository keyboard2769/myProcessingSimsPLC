/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pppif;

public class VcKeeper {
  
  static enum COMMANDS{
    ROLL,EXIT,
    NONE
  };
  
  volatile COMMANDS cmCommand;
  volatile String cmParam;
  
  public VcKeeper(){
    cmCommand=COMMANDS.NONE;
    cmParam="";
  }//+++ 
  
  //-- public
  
  synchronized void ccSet(COMMANDS pxCommands)
    {cmCommand=pxCommands;}//+++
  synchronized void ccSet(COMMANDS pxCommand, String pxParam)
    {cmCommand=pxCommand;cmParam=pxParam;}//+++
  
  String[] ccGetTokens()
    {return cmParam.split(",");}//+++
  int[] ccGetValues()
    {return VcConst.ccSplitStringToIntArray(cmParam, ",");}//+++
  
}//***EOF

