package myGameEngine;
import a2.MyGame;
import ray.rage.scene.*;
import ray.rage.scene.controllers.*;
import ray.rml.*;

//Flat controller that makes nodes flattened out
public class FlatController extends AbstractController {
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
			Vector3 curScale = n.getLocalScale();
			if (curScale.y()>.008f){
			curScale = Vector3f.createFrom(curScale.x(), curScale.y()-.002f, curScale.z());
			n.setLocalScale(curScale);
			}
			
			
		}
	}
}
