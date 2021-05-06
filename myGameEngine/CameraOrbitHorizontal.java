package myGameEngine;
import a2.MyGame;


import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rage.game.*;
import ray.rml.*;
import net.java.games.input.Event;

//Camera Orbit Left and Right with controller
public class CameraOrbitHorizontal extends AbstractInputAction {
	
	private Camera3Pcontroller c;
	
	public CameraOrbitHorizontal(Camera3Pcontroller rot) {
		c = rot;
	}

	@Override
	public void performAction(float arg0, Event e) {
		
		if (e.getValue()<-0.20){	
			float rotAmt =-1.0f;
			c.cameraAzimuth += rotAmt;
			c.cameraAzimuth = c.cameraAzimuth%360;
			c.updateCameraPosition();
	} else {
		if (e.getValue()>0.20){
		float rotAmt =1.0f;
		c.cameraAzimuth += rotAmt;
		c.cameraAzimuth = c.cameraAzimuth%360;
		c.updateCameraPosition();
		}
	}
}
}