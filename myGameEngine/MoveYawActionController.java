package myGameEngine;
import a2.MyGame;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rage.game.*;
import ray.rml.*;
import net.java.games.input.Event;



public class MoveYawActionController extends AbstractInputAction
{
private Node avN;
private Camera3Pcontroller c;
public MoveYawActionController(Node n, Camera3Pcontroller rot)
{ 
avN=n;
c=rot;
}
public void performAction(float time, Event e)
{ 


	
	if (e.getValue()<-0.2){
		avN.yaw(Degreef.createFrom(1.0f));
		float rotAmt =1.0f;
		c.cameraAzimuth += rotAmt;
		c.cameraAzimuth = c.cameraAzimuth%360;
		c.updateCameraPosition();
	} else {
		if (e.getValue()>0.2){
		avN.yaw(Degreef.createFrom(-1.0f));
		float rotAmt =-1.0f;
		c.cameraAzimuth += rotAmt;
		c.cameraAzimuth = c.cameraAzimuth%360;
		c.updateCameraPosition();
		}
	}
	
	
}

}

