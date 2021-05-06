package myGameEngine;
import a2.MyGame;
import ray.rage.scene.*;
import ray.rage.scene.controllers.*;
import ray.rml.*;


//Bounce controller that makes nodes bounce up and down
public class BounceController extends AbstractController {
	private float cycleTime = 500.0f;	
	private float totalTime = 0.0f;
	private float direction = 1.0f;



	@Override
	protected void updateImpl(float elapsedTimeMillis){ 
		totalTime += elapsedTimeMillis;
		

		if (totalTime > cycleTime){	
			direction = -direction;
			totalTime = 0.0f;
		}
		
		
	for (Node n : super.controlledNodesList){	
			Vector3 curScale = n.getLocalPosition();
			curScale = Vector3f.createFrom(curScale.x(), curScale.y()+.02f*direction, curScale.z());
			n.setLocalPosition(curScale);
			
		}
	}
}
