package myGameEngine;
import a2.MyGame;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rage.game.*;
import ray.rml.*;
import net.java.games.input.Event;
public class MoveForwardAction extends AbstractInputAction
{
private Node avN;

//move dolphin forward
public MoveForwardAction(Node n)
{ 
avN=n;
}

public void performAction(float time, Event e)
{ 
	avN.moveForward(0.06f);
}

}