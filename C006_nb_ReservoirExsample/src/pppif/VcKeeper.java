/* --def %revision%
 * #[flag] "version/filename" : issue $ describe,
 * [ ] "": $,,,
 * [ ] "": $,,,
 * [ ] "": $,,,
 * --end
 */

package pppif;

public class VcKeeper {
  
  static enum COMMANDS{
    ERROR_CLEAR,ROLL,EXIT,
    NONE
  };
  
  volatile COMMANDS cmCommand;
  
  volatile String cmParam;
  
  public VcKeeper(){
    cmCommand=COMMANDS.NONE;
    cmParam="";
  }//+++ 
  
  //== public
  
  synchronized void ccSet(COMMANDS pxCommands)
    {cmCommand=pxCommands;}//+++
  
  synchronized void ccSet(COMMANDS pxCommand, String pxParam)
    {cmCommand=pxCommand;cmParam=pxParam;}//+++
  
  String[] ccGetTokens()
    {return cmParam.split(",");}//+++
  
  int[] ccGetValues()
    {return VcConst.ccSplitStringToIntArray(cmParam, ",");}//+++
  
  //== <
  
}//***eof
