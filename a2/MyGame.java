package a2;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import myGameEngine.*;
import ray.rage.*;
import ray.rage.game.*;
import ray.rage.rendersystem.*;
import ray.rage.rendersystem.Renderable.*;
import ray.rage.scene.*;
import ray.rage.scene.Camera.Frustum.*;
import ray.rage.scene.controllers.*;
import ray.rml.*;
import ray.rage.rendersystem.gl4.GL4RenderSystem;
import ray.rage.rendersystem.states.*;
import ray.rage.asset.texture.*;
import ray.input.*;
import ray.input.action.*;
import ray.rage.util.BufferUtil;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import ray.rage.asset.texture.Texture;
import ray.rage.rendersystem.shader.*;
import java.util.Random;
import java.util.ArrayList;
import net.java.games.input.Controller;
import ray.rage.asset.material.Material;
import ray.rage.scene.Camera.Frustum.Projection;


public class MyGame extends VariableFrameRateGame {


	GL4RenderSystem rs;
	float elapsTime = 0.0f;
	String elapsTimeStr, counterStr, dispStr1,dispStr2;
	int elapsTimeSec, counter = 0,score1=0, score2=0;
	private InputManager im;
	private Action quitGameAction,moveYawController,moveYawRight,moveYawLeft, moveHorizontal, movePitchUp, moveForwardAction,moveBackwardAction; 
	public Camera camera;
	private Random r;
	public SceneNode dolphinN,dolphin2N,dolphinNG,cubeN;
	public boolean toggleDolphin = false;
	public SceneManager sceneManager;
	public RotationController rc;
	public BounceController bc;
	public FlatController fc;
	public OrbitController oc;
	private ArrayList<Integer> planets = new ArrayList<Integer>();
	private Camera3Pcontroller orbitController1,orbitController2;
	

	

    public MyGame() {
        super();
		System.out.println("---------------------------------------------------------------------------------------------------------------------");
		System.out.println("Welcome to Dolphin Contest! ");
		System.out.println("Player 1, your objective is to deflate 10 beach balls before player 2. You do so by touching the beach ball first");
		System.out.println("Player 2, your objective is to bounce 10 beach balls before player 1. You do so by touching the beach ball first");
		System.out.println("Once all the available beach balls have been reached, any player may visit the treasure chest guarded by Master Turtle and his son The Fish");
		System.out.println("By visiting the treasure chest, Master Turtle and The Fish will summon 3 more beach balls");
		System.out.println("---------------------------------------------------------------------------------------------------------------------");
		System.out.println("KEYBOARD CONTROLS");
		System.out.println("-------------");
		System.out.println("Player 1");
		System.out.println("W/S: move dolphin forward and backward");
		System.out.println("A/D: turn dolphin left and right");
		System.out.println("UP/LEFT/RIGHT/Down Arrow: orbit camera in respective direction");
		System.out.println("R/F: zoom in and out");
		System.out.println("KEYBOARD CONTROLS");
		System.out.println("-------------");
		System.out.println("Player 2");
		System.out.println("X-axis: turn dolphin left and right");
		System.out.println("Y-axis: move dolphin forward and backward");
		System.out.println("Rx-axis: orbit camera left and right");
		System.out.println("Ry-axis: orbit camera up and down");
		System.out.println("A Button: zoom out");
		System.out.println("Y Button: zoom out");
    }

    public static void main(String[] args) {
        Game game = new MyGame();
        try {
            game.startup();
            game.run();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            game.shutdown();
            game.exit();
        }
    }
	
	@Override
	protected void setupWindow(RenderSystem rs, GraphicsEnvironment ge) {
		rs.createRenderWindow(new DisplayMode(1000, 700, 24, 60), false);
	}
	
	protected void setupWindowViewports(RenderWindow rw){
		rw.addKeyListener(this);
		Viewport topViewport = rw.getViewport(0);
		topViewport.setDimensions(.51f, .01f, .99f, .49f); // B,L,W,H
		topViewport.setClearColor(new Color(.7f, 1f, 1f));
		Viewport botViewport = rw.createViewport(.01f, .01f, .99f, .49f);
		botViewport.setClearColor(new Color(.7f, 1f, 1f));
		}

    @Override
    protected void setupCameras(SceneManager sm, RenderWindow rw) {
		sceneManager=sm;
        SceneNode rootNode = sm.getRootSceneNode();
        camera = sm.createCamera("MainCamera", Projection.PERSPECTIVE);
        rw.getViewport(0).setCamera(camera);
		
		camera.setRt((Vector3f)Vector3f.createFrom(1.0f, 0.0f, 0.0f));
		camera.setUp((Vector3f)Vector3f.createFrom(0.0f, 1.0f, 0.0f));
		camera.setFd((Vector3f)Vector3f.createFrom(0.0f, 0.0f, -1.0f));
		
		camera.setPo((Vector3f)Vector3f.createFrom(0.0f, 0.0f, 0.0f));
		camera.setMode('n');
		camera.getFrustum().setFarClipDistance(1000.0f);
        SceneNode cameraNode = rootNode.createChildSceneNode(camera.getName() + "Node");
        cameraNode.attachObject(camera);
		
		Camera camera2 = sm.createCamera("MainCamera2",Projection.PERSPECTIVE);
		rw.getViewport(1).setCamera(camera2);
		SceneNode cameraN2 =
		rootNode.createChildSceneNode("MainCamera2Node");
		cameraN2.attachObject(camera2);
		camera2.setMode('n');
		camera2.getFrustum().setFarClipDistance(1000.0f);
		
		
    }
	
  @Override
    protected void setupScene(Engine eng, SceneManager sm) throws IOException {
        
		rc = new RotationController(Vector3f.createUnitVectorY(), .02f);
		bc = new BounceController();
		fc = new FlatController();
		makeCube(eng,sm);
		oc = new OrbitController(cubeN,1.0f, 2.0f,0.5f,true);
		Random r = new Random();
		drawFloor(eng,sm);
		
		//set up dolphin
		Entity dolphinE = sm.createEntity("myDolphin", "dolphinHighPoly.obj");
        dolphinE.setPrimitive(Primitive.TRIANGLES);
        dolphinN = sm.getRootSceneNode().createChildSceneNode(dolphinE.getName() + "Node");
        dolphinN.attachObject(dolphinE);
		dolphinN.setLocalPosition(-0.5f, 0.5f, 0.0f);
		
		//make dolphin red
		TextureManager tm = eng.getTextureManager();
		Texture redTexture = tm.getAssetByPath("red.jpeg");
		RenderSystem rs = sm.getRenderSystem();
		TextureState state = (TextureState)rs.createRenderState(RenderState.Type.TEXTURE);
		state.setTexture(redTexture);
		dolphinE.setRenderState(state);
		
		
		//set up turtles
		SceneNode turtleNG = sm.getRootSceneNode().createChildSceneNode("turtleNodeG");
		
		
		Entity turtleE = sm.createEntity("turtle","turtle2.obj");
		turtleE.setPrimitive(Primitive.TRIANGLES);
		SceneNode turtleN=turtleNG.createChildSceneNode(turtleE.getName() + "Node");
		turtleN.attachObject(turtleE);

		//set up fish
		Entity turtle2E = sm.createEntity("turtle2","fish.obj");
		turtleE.setPrimitive(Primitive.TRIANGLES);
		SceneNode turtle2N=turtleNG.createChildSceneNode(turtle2E.getName() + "Node");
		turtle2N.attachObject(turtle2E);
		turtleN.setLocalPosition(0.0f, 1.0f, 0.0f);
		
		camera.setMode('n');
	
		Vector3 dolphinLocation = dolphinN.getLocalPosition();
		Vector3f p1 = (Vector3f)Vector3f.createFrom(0.5f, -0.50f, 0.5f); 
		Vector3f p2 = (Vector3f)dolphinLocation.add((Vector3)p1);
		camera.setPo((Vector3f)p2);
		
		//set up dolphin 2
		Entity dolphin2E =sm.createEntity("myDolphin2","dolphinHighPoly.obj");
		dolphin2E.setPrimitive(Primitive.TRIANGLES);
		dolphin2N=sm.getRootSceneNode().createChildSceneNode("myDolphin2Node");
		dolphin2N.attachObject(dolphin2E);
		dolphin2N.setLocalPosition(0.5f, 0.5f, 0.0f);
		
		
		//create 3 planets
		planets.add(0);
		SceneNode dolphinNG=sm.getRootSceneNode().createChildSceneNode("myDolphinNodeG");		
		for (int i=1;i<4;i++){
			makePlanets(eng,sm,i);
			planets.add(0);
		}

		
		

		//add controllers
		oc.addNode(turtleNG);
		sm.addController(oc);
		sm.addController(bc);
		sm.addController(rc);
		sm.addController(fc);
        
		
		
		
		// set up lights
        sm.getAmbientLight().setIntensity(new Color(.3f, .3f, .3f));
        Light plight = sm.createLight("testLamp1", Light.Type.POINT);
        plight.setAmbient(new Color(.1f, .1f, .1f));
        plight.setDiffuse(new Color(0.8f, 0.8f, 0.8f));
        plight.setSpecular(new Color(1.0f, 1.0f, 1.0f));
        plight.setRange(50f);
        SceneNode plightNode =
        sm.getRootSceneNode().createChildSceneNode("plightNode");
        plightNode.attachObject(plight);
        plightNode.setLocalPosition(1.0f, 1.0f, 5.0f);
		
		setupOrbitCamera(eng,sm);
		dolphinN.yaw(Degreef.createFrom(45.0f));
		dolphin2N.yaw(Degreef.createFrom(45.0f));
		setupInputs(sm);
    }
	
	//Create a new planet and returns a node that is has the planet attached
	public SceneNode makePlanets(Engine eng, SceneManager sm, int i) throws IOException{
		
		Random r = new Random();
		Entity planetE = sm.createEntity("myPlanet"+i,"planet1.obj");
		planetE.setPrimitive(Primitive.TRIANGLES);
		
		//SceneNode planetN=dolphinNG.createChildSceneNode(planetE.getName()+"Node");
		SceneNode planetN=sm.getRootSceneNode().createChildSceneNode(planetE.getName()+"Node");
		
		planetN.attachObject(planetE);
		planetN.setLocalPosition(((float)(r.nextInt(40)-20)), 0.75f, ((float)(r.nextInt(40)-20)));
		planetN.setLocalScale(0.45f, 0.45f, 0.45f);
		
		return planetN;
		
	}
	
	protected void setupOrbitCamera(Engine eng, SceneManager sm){ 
	SceneNode dolphinN = sm.getSceneNode("myDolphinNode");
	SceneNode cameraN = sm.getSceneNode("MainCameraNode");
	im = new GenericInputManager();
	Camera camera = sm.getCamera("MainCamera");
	String gpName = im.getFirstGamepadName();
	orbitController1 = new Camera3Pcontroller(camera, cameraN, dolphinN, gpName, im);
	
	SceneNode dolphin2N = sm.getSceneNode("myDolphin2Node");
	SceneNode camera2N = sm.getSceneNode("MainCamera2Node");
	String msName = im.getMouseName();
	Camera camera2 = sm.getCamera("MainCamera");
	orbitController2 = new Camera3Pcontroller(camera2, camera2N, dolphin2N, msName, im);
	}


	protected void setupInputs(SceneManager sm){ 
		SceneNode dolphinN =getEngine().getSceneManager().getSceneNode("myDolphinNode");
		SceneNode dolphin2N=getEngine().getSceneManager().getSceneNode("myDolphin2Node");
		im = new GenericInputManager();
		String kbName = im.getKeyboardName();
		String gpName = im.getFirstGamepadName();

		// build some action objects for doing things in response to user input
		quitGameAction = new QuitGameAction(this);
		MoveVerticalController moveVertical = new MoveVerticalController(dolphinN);
		MoveYawActionController moveYawController = new MoveYawActionController(dolphinN,orbitController1);
		CameraOrbitHorizontal orbitHorizontal = new CameraOrbitHorizontal(orbitController1);
		CameraOrbitVertical orbitVertical = new CameraOrbitVertical(orbitController1);
		CameraZoomIn zoomIn = new CameraZoomIn(orbitController1);
		CameraZoomOut zoomOut = new CameraZoomOut(orbitController1);
		
		//dolphin 2 moves
		MoveForwardAction moveForwardAction2 = new MoveForwardAction(dolphin2N);
		MoveBackwardAction moveBackwardAction2 = new MoveBackwardAction(dolphin2N);
		MoveYawLeftAction moveYawLeft2 = new MoveYawLeftAction(dolphin2N,orbitController2);
		MoveYawRightAction moveYawRight2 = new MoveYawRightAction(dolphin2N,orbitController2);
		CameraOrbitLeft orbitLeft2 = new CameraOrbitLeft(orbitController2);
		CameraOrbitRight orbitRight2 = new CameraOrbitRight(orbitController2);
		CameraOrbitDown orbitDown2 = new CameraOrbitDown(orbitController2);
		CameraOrbitUp orbitUp2 = new CameraOrbitUp(orbitController2);
		CameraZoomIn zoomIn2 = new CameraZoomIn(orbitController2);
		CameraZoomOut zoomOut2 = new CameraZoomOut(orbitController2);
		
		
		

	 
	//Assigning keys to gamepad
	 if (im.getFirstGamepadName()!=null){
		im.associateAction(gpName,net.java.games.input.Component.Identifier.Button._3,zoomIn,InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction(gpName, net.java.games.input.Component.Identifier.Axis.Y,moveVertical, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction(gpName, net.java.games.input.Component.Identifier.Button._0,zoomOut, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction(gpName, net.java.games.input.Component.Identifier.Axis.X,moveYawController, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction(gpName, net.java.games.input.Component.Identifier.Axis.RY,orbitVertical, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction(gpName, net.java.games.input.Component.Identifier.Axis.RX,orbitHorizontal, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
	}
	
	//Assigning keys to keyboard
	ArrayList controllers = im.getControllers();
	for (int i =0;i<controllers.size();i++){
		Controller c = (Controller)controllers.get(i);
		if (c.getType()==Controller.Type.KEYBOARD){
			
			im.associateAction(c,net.java.games.input.Component.Identifier.Key.W,moveForwardAction2, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			im.associateAction(c,net.java.games.input.Component.Identifier.Key.A,moveYawLeft2, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			im.associateAction(c,net.java.games.input.Component.Identifier.Key.S,moveBackwardAction2, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			im.associateAction(c,net.java.games.input.Component.Identifier.Key.D,moveYawRight2, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			im.associateAction(c,net.java.games.input.Component.Identifier.Key.UP,orbitUp2,InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			im.associateAction(c,net.java.games.input.Component.Identifier.Key.DOWN,orbitDown2,InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			im.associateAction(c,net.java.games.input.Component.Identifier.Key.LEFT,orbitLeft2,InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			im.associateAction(c,net.java.games.input.Component.Identifier.Key.RIGHT,orbitRight2,InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			im.associateAction(c,net.java.games.input.Component.Identifier.Key.R,zoomIn2,InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			im.associateAction(c,net.java.games.input.Component.Identifier.Key.F,zoomOut2,InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		}
	}
	
}
	
    @Override
    protected void update(Engine engine) {
		// build and set HUD
		rs = (GL4RenderSystem) engine.getRenderSystem();
		elapsTime += engine.getElapsedTimeMillis();
		elapsTimeSec = Math.round(elapsTime/1000.0f);
		elapsTimeStr = Integer.toString(elapsTimeSec);
		counterStr = Integer.toString(counter);
		dispStr1 = " Player 2: Beach Balls Bounced= "+score1;
		dispStr2 = " Player 1: Beach Balls Deflated= "+score2;
		
		
		rs.setHUD(dispStr1, 15, 15);
		rs.setHUD2(dispStr2,15,rs.getCanvas().getHeight()/2 +17);
		im.update(elapsTime);
		orbitController1.updateCameraPosition();
		orbitController2.updateCameraPosition();
		
		
		//check if game is over
		if (score1>=10){
			System.out.println("Player 2 has Won!");
			exit();
		} else if (score2>=10){
			System.out.println("Player 1 has Won!");
			exit();
		}

								
		//Checks if player is visiting a planet and increment score if so
		for (int i=1;i<planets.size();i++){
			//check if dolphin 1 visited a planet
			if (getDistance(sceneManager.getSceneNode("myPlanet"+i+"Node"),dolphinN) <1&&planets.get(i)==0){
				planets.set(i,1);
				score2++;
				fc.addNode(sceneManager.getSceneNode("myPlanet"+i+"Node"));
			}
			
			//check if dolphin 2 visisted a planet
			if (getDistance(sceneManager.getSceneNode("myPlanet"+i+"Node"),dolphin2N) <1&&planets.get(i)==0){
				planets.set(i,1);
				score1++;
				bc.addNode(sceneManager.getSceneNode("myPlanet"+i+"Node"));
			}
			
	}
	
	//checks if player is near the add planet cube, if player is at cube and there are no more unvisited planets, create a new planet
	if ((getDistance(sceneManager.getSceneNode("CubeNode"),dolphinN)<2)||(getDistance(sceneManager.getSceneNode("CubeNode"),dolphin2N)<2)){
		if (checkForUnvistedPlanets()==true){
			try {
			addPlanet(engine);
			addPlanet(engine);
			addPlanet(engine);
			System.out.println("added 3 planets!");
			//rc.addNode(dolphinNG);
			}catch(IOException io){
			}
		} else {
		}
		
	}
	
	
	}
	
	//checks the planets arraylist to see if there are any unvisited planets available
	private boolean checkForUnvistedPlanets(){
		for (int i=1;i<planets.size();i++){
			if (planets.get(i)==0){
				return false;
			}
		}
		return true;
	}
	
	//add a planet to the game
	public void addPlanet(Engine eng)throws IOException
	{			
		rc.addNode(makePlanets(eng,sceneManager,planets.size()));
		//rc.addNode(makePlanets(eng,sceneManager,planets.size()));
		//rc.addNode(makePlanets(eng,sceneManager,planets.size()));
		planets.add(0);
		//planets.add(0);
	//	planets.add(0);
		//System.out.println("Added 3 planets!");
	}
	
	//draw the floor plane of the game
	public void drawFloor(Engine eng, SceneManager sm)throws IOException{
		ManualObject floor = sm.createManualObject("Floor");
		ManualObjectSection floorSec = floor.createManualSection("floorSection");
		floor.setGpuShaderProgram(sm.getRenderSystem().getGpuShaderProgram(GpuShaderProgram.Type.RENDERING));
		
		float[] verticies = new float[]{
			-30.0f,0.0f,30.0f,
			30.0f,0.0f,30.0f,
			-30.0f,0.0f,-30.0f,
			30.0f,0.0f,-30.0f,
			-30.0f,0.0f,-30.0f,
			30.0f, 0.0f, 30.0f,			
		};
		
		float[] texcoords = new float[]{
			0.0f,0.0f,
			1.0f,1.0f,
			0.0f,1.0f,
			1.0f,1.0f,
			0.0f,0.0f,
			1.0f,0.0f,
		};
		
		float[] normals = new float[]{
			0.0f,-1.0f,0.0f,
			0.0f,-1.0f,0.0f,
			0.0f,-1.0f,0.0f,
			0.0f,-1.0f,0.0f,
			0.0f,-1.0f,0.0f,
			0.0f,-1.0f,0.0f,
		};
		
		int[] indicies = new int[] {0,1,2,3,4,5};
		
		FloatBuffer vertBuf = BufferUtil.directFloatBuffer(verticies);
		FloatBuffer texBuf = BufferUtil.directFloatBuffer(texcoords);
		FloatBuffer normBuf = BufferUtil.directFloatBuffer(normals);
		IntBuffer indexBuf= BufferUtil.directIntBuffer(indicies);
		
		floorSec.setVertexBuffer(vertBuf);
		floorSec.setTextureCoordsBuffer(texBuf);
		floorSec.setNormalsBuffer(normBuf);
		floorSec.setIndexBuffer(indexBuf);
		
		Texture floorTex = eng.getTextureManager().getAssetByPath("sand.jpg");
		TextureState texState = (TextureState)sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
		texState.setTexture(floorTex);
		
		floor.setDataSource(DataSource.INDEX_BUFFER);
		floor.setRenderState(texState);
		
		SceneNode floorNode = sm.getRootSceneNode().createChildSceneNode("floorNode");
		floorNode.attachObject(floor);
		floor.setPrimitive(Primitive.TRIANGLES);
	}
	
	
	//make a cube manually
	protected SceneNode makeCube(Engine eng, SceneManager sm) throws IOException{ 
		Random r = new Random();
		ManualObject cube = sm.createManualObject("Cube");
		ManualObjectSection cubeSec =cube.createManualSection("CubeSec");
		cube.setGpuShaderProgram(sm.getRenderSystem().
		getGpuShaderProgram(GpuShaderProgram.Type.RENDERING));

	float[] vertices = new float[]{

	
		1.0f, 1.0f, 1.0f,  1.0f, 1.0f,-1.0f, -1.0f, 1.0f,-1.0f,  
		1.0f, 1.0f, 1.0f, -1.0f, 1.0f,-1.0f, -1.0f, 1.0f, 1.0f,  
		1.0f,-1.0f, 1.0f, -1.0f,-1.0f, 1.0f, -1.0f,-1.0f,-1.0f,  
		 1.0f,-1.0f, 1.0f, -1.0f,-1.0f,-1.0f,  1.0f,-1.0f,-1.0f,  
		-1.0f,-1.0f,-1.0f, -1.0f,-1.0f, 1.0f, -1.0f, 1.0f, 1.0f,  
		-1.0f,-1.0f,-1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f,-1.0f,  
		 1.0f, 1.0f,-1.0f, -1.0f,-1.0f,-1.0f, -1.0f, 1.0f,-1.0f,  
		 1.0f, 1.0f,-1.0f,  1.0f,-1.0f,-1.0f, -1.0f,-1.0f,-1.0f,  
		-1.0f, 1.0f, 1.0f, -1.0f,-1.0f, 1.0f,  1.0f,-1.0f, 1.0f,  
		1.0f, 1.0f, 1.0f, -1.0f, 1.0f, 1.0f,  1.0f,-1.0f, 1.0f,   
		 1.0f, 1.0f, 1.0f,  1.0f,-1.0f,-1.0f,  1.0f, 1.0f,-1.0f,  
		 1.0f,-1.0f,-1.0f,  1.0f, 1.0f, 1.0f,  1.0f,-1.0f, 1.0f,  
		 
		
	
};

float[] texcoords = new float[]{
	
	0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f,
	0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f,
	0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f,
	0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f,
	0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f,
	0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f,
	0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f,
	0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f,
	0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f,
	0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f,
	0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f,
	0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f,
	

	
};


float[] normals = new float[]{
	
		-1.0f, 0.0f, 0.0f, 
		 0.0f, 0.0f, -1.0f, 
		 0.0f,-1.0f, 0.0f, 
		 0.0f, 0.0f, -1.0f, 
		-1.0f, 0.0f, 0.0f, 
		 0.0f, -1.0f, 0.0f, 
		 0.0f, 0.0f, 1.0f, 
		 1.0f, 0.0f, 0.0f, 
		 1.0f, 0.0f, 0.0f, 
		 0.0f, 1.0f, 0.0f, 
		 0.0f, 1.0f, 0.0f, 
		 0.0f, 0.0f, 1.0f, 
		 
		
	
	
};

	int[] indices = new int[] { 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35 };
	FloatBuffer vertBuf = BufferUtil.directFloatBuffer(vertices);
	FloatBuffer texBuf = BufferUtil.directFloatBuffer(texcoords);
	FloatBuffer normBuf = BufferUtil.directFloatBuffer(normals);
	IntBuffer indexBuf = BufferUtil.directIntBuffer(indices);
	
	cubeSec.setVertexBuffer(vertBuf);
	cubeSec.setTextureCoordsBuffer(texBuf);
	cubeSec.setNormalsBuffer(normBuf);
	cubeSec.setIndexBuffer(indexBuf);
	
	Texture tex = eng.getTextureManager().getAssetByPath("brown.jpg");
	TextureState texState = (TextureState)sm.getRenderSystem().
	createRenderState(RenderState.Type.TEXTURE);
	texState.setTexture(tex);
	FrontFaceState faceState = (FrontFaceState) sm.getRenderSystem().
	
	createRenderState(RenderState.Type.FRONT_FACE);
	cube.setDataSource(DataSource.INDEX_BUFFER);
	cube.setRenderState(texState);
	cube.setRenderState(faceState);
	
	
	cubeN =sm.getRootSceneNode().createChildSceneNode(cube.getName()+"Node");
	cubeN.attachObject(cube);
	cubeN.setLocalPosition(((float)(r.nextInt(40)-20)), 1.5f, ((float)(r.nextInt(40)-20)));
	
	return cubeN;
}
	
	
	
	
	//calculate the distance between two nodes
	public float getDistance(SceneNode node1, SceneNode node2){
		float node1X = node1.getLocalPosition().x();
		float node1Y = node1.getLocalPosition().y();
		float node1Z = node1.getLocalPosition().z();
		float node2X = node2.getLocalPosition().x();
		float node2Y = node2.getLocalPosition().y();
		float node2Z = node2.getLocalPosition().z();
		
		return (float) (Math.sqrt((double)+(node1X-node2X)*(node1X-node2X)+(node1Y-node2Y)*(node1Y-node2Y)+(node1Z-node2Z)*(node1Z-node2Z)));
		
	}
	
   
}
