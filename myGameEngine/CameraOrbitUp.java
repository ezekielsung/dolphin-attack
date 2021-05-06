package myGameEngine;
import a2.MyGame;

import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rage.game.*;
import ray.rml.*;
import net.java.games.input.Event;

//Camera Orbit upwards
public class CameraOrbitUp extends AbstractInputAction {
	
	private Camera3Pcontroller c;
	
	public CameraOrbitUp(Camera3Pcontroller rot) {
		c = rot;
	}

	@Override
	public void performAction(float arg0, Event arg1) {
		if (c.cameraElevation <=88.0f){
			float rotAmt =1.0f;
			c.cameraElevation += rotAmt;
			c.cameraElevation = c.cameraElevation%360;
			c.updateCameraPosition();
		}
	}
}