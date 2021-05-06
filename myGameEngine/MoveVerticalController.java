package myGameEngine;
import a2.MyGame;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rage.game.*;
import ray.rml.*;
import net.java.games.input.Event;


//Move forward and backward for dolphin with controller
public class MoveVerticalController extends AbstractInputAction
{
private Node avN;
public MoveVerticalController(Node n)
{ 
avN=n;
}
public void performAction(float time, Event e)
{ 


	
	if (e.getValue()<-0.20){
		avN.moveForward(0.06f);
	} else {
		if (e.getValue()>0.02f){
		avN.moveBackward(0.03f);
		}
	}
	
	
}

}

