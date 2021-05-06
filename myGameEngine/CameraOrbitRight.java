package myGameEngine;
import a2.MyGame;


import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rage.game.*;
import ray.rml.*;
import net.java.games.input.Event;

public class CameraOrbitRight extends AbstractInputAction {
	
	private Camera3Pcontroller c;
	
	public CameraOrbitRight(Camera3Pcontroller rot) {
		c = rot;
	}

	@Override
	public void performAction(float arg0, Event arg1) {
		float rotAmt =1.0f;
		c.cameraAzimuth += rotAmt;
		c.cameraAzimuth = c.cameraAzimuth%360;
		c.updateCameraPosition();
	}
}