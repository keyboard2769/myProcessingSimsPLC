/* *
 * Swing Example
 * 
 * let a bunch of tits blink the dark vacuum.
 * demostrates how to make a hybrid program
 *   of processing sketch and java swing gui application
 *   focusing on concurency issues.
 * to add tit, press the "add" button.
 * to remove tit, press the "remove" button.
 * to exit, press any key.
 * 
 */

import java.util.Queue;
import java.util.LinkedList;

private static PApplet self=null;
private static final String C_MSG
= "press any key to exit...";
public static volatile int roller;

private static final
Queue<Runnable> QUEUE_OF_RUNNER = new LinkedList<Runnable>();

private static final
LinkedList<PVector> LIST_OF_VECTOR = new LinkedList<PVector>();

static private final Runnable ADDING_TIT = new Runnable(){
  @Override public void run(){
    LIST_OF_VECTOR.add(new PVector(
      self.random(5f,320f-5f),
      self.random(5f,240f-5f)
    ));
    String thread = Thread.currentThread().toString();
    System.out.println("::manipulate_container_via:"+thread);
  }
};

static private final Runnable REMOVING_TIT = new Runnable(){
  @Override public void run(){
    if(LIST_OF_VECTOR.isEmpty()){return;}
    LIST_OF_VECTOR.removeFirst();
    String thread = Thread.currentThread().toString();
    System.out.println("::manipulate_container_via:"+thread);
  }
};

void setup(){
  size(320,240);
  ellipseMode(CENTER);
  noStroke();
  fill(0xFF);
  self=this;
  SwingUtilities.invokeLater(SWING_SETUP);
}

void draw(){
  background(0);
  roller++;roller&=63;
  if(!QUEUE_OF_RUNNER.isEmpty()){
    QUEUE_OF_RUNNER.poll().run();
  }
  for(PVector it : LIST_OF_VECTOR){
    ellipse(
      it.x,it.y,
      random(4f,8f),random(4f,8f)
    );
  }
  if(roller>31){
    text(C_MSG,5,220);
  }
}

void keyPressed(){
  exit();
}

public static final void invokeLater(Runnable r){
  if(r==null){return;}
  QUEUE_OF_RUNNER.offer(r);
}