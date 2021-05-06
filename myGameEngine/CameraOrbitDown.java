package myGameEngine;
import a2.MyGame;


import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rage.game.*;
import ray.rml.*;
import net.java.games.input.Event;

public class CameraOrbitDown extends AbstractInputAction {
	
	private Camera3Pcontroller c;
	
	public CameraOrbitDown(Camera3Pcontroller rot) {
		c = rot;
	}

	@Override
	public void performAction(float arg0, Event arg1) {
		if (c.cameraElevation >=0.0f){
			float rotAmt =-1.0f;
			c.cameraElevation += rotAmt;
			c.cameraElevation = c.cameraElevation%360;
			c.updateCameraPosition();
		}
	}
}