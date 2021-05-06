package myGameEngine;
import a2.MyGame;


import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rage.game.*;
import ray.rml.*;
import net.java.games.input.Event;

public class CameraZoomIn extends AbstractInputAction {
	
	private Camera3Pcontroller c;
	
	public CameraZoomIn(Camera3Pcontroller rot) {
		c = rot;
	}

	@Override
	public void performAction(float arg0, Event arg1) {
		if (c.radius >=1.0f){
		
		float rotAmt =-.05f;
		c.radius += rotAmt;
		c.updateCameraPosition();
		}
	}
}