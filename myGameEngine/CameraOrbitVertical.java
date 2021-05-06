package myGameEngine;
import a2.MyGame;

import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rage.game.*;
import ray.rml.*;
import net.java.games.input.Event;


//Camera orbit Up and Down with controller
public class CameraOrbitVertical extends AbstractInputAction {
	
	private Camera3Pcontroller c;
	
	public CameraOrbitVertical(Camera3Pcontroller rot) {
		c = rot;
	}

	@Override
	public void performAction(float arg0, Event e) {
		
		if (e.getValue()<-0.20){	
			if (c.cameraElevation <=88.0f){	
				float rotAmt =1.0f;
				c.cameraElevation += rotAmt;
				c.cameraElevation = c.cameraElevation%360;
				c.updateCameraPosition();
		}
	} else {
		if (e.getValue()>0.2){
		if (c.cameraElevation >=0.0f){
			float rotAmt =-1.0f;
			c.cameraElevation += rotAmt;
			c.cameraElevation = c.cameraElevation%360;
			c.updateCameraPosition();
		}
		}
	}
}
}