package Plane.Dodger.com;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

public class GameScreen extends BaseScreen{

	private PhysicsActor[] background;
	private PhysicsActor[] ground;

	private PhysicsActor baseEnemy;
	private ArrayList<PhysicsActor> enemyList;
	private float enemyTimer;
	private float enemySpeed;

	private PhysicsActor baseStar;
	private ArrayList<PhysicsActor> starList;
	private float starTimer;

	private AnimatedActor baseSparkle;
	private AnimatedActor baseExplosion;

	private ArrayList<BaseActor> removeList;
	private boolean gameOver;

	//game world dimensions
	final int mapWidth = 800;
	final int mapHeight = 600;

	public GameScreen(BaseGame g) {
		super(g);
	}

	@Override
	public void create() {

		background = new PhysicsActor[2];

		PhysicsActor bg0 = new PhysicsActor();
		bg0.storeAnimation("default", new Texture("sky.png"));
		bg0.setPosition(0, 0);
		bg0.setVelocityXY(-50, 0);
		background[0] = bg0;
		mainStage.addActor(bg0);

		PhysicsActor bg1 = bg0.clone();
		bg1.setX(bg0.getWidth());
		background[1] = bg1;
		mainStage.addActor(bg1);

		ground = new PhysicsActor[2];

		PhysicsActor gr0 = new PhysicsActor();
		gr0.storeAnimation("default", new Texture("ground.png"));
		gr0.setPosition(0,0);
		gr0.setVelocityXY(-200,0);
		gr0.setRectangleBoundary();
		ground[0] = gr0;
		mainStage.addActor(gr0);

		PhysicsActor gr1 = gr0.clone();
		gr1.setX(gr0.getWidth());
		ground[1] = gr1;
		mainStage.addActor(gr1);

	}

	@Override
	public void update(float dt) {

		//manage background objects
		for (int i = 0; i < 2; i++) {
			PhysicsActor bg = background[i];
			if (bg.getX() + bg.getWidth() < 0)
				bg.setX(bg.getX() + 2 * bg.getWidth());

			PhysicsActor gr = ground[i];
			if (gr.getX() + gr.getWidth() < 0)
				gr.setX(gr.getX() + 2 * gr.getWidth());

		}

	}
}
