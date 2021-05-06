package myGameEngine;
import a2.MyGame;


import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rage.game.*;
import ray.rml.*;
import net.java.games.input.Event;


public class MoveBackwardAction extends AbstractInputAction
{
private Node avN;

public MoveBackwardAction(Node n)
{ 
avN=n;
}

public void performAction(float time, Event e)
{ 
	avN.moveBackward(0.03f);
}

}