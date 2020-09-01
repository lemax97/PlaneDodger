package Plane.Dodger.com;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.ArrayList;

public class GameScreen extends BaseScreen{

	private PhysicsActor[] background;
	private PhysicsActor[] ground;

	private PhysicsActor baseEnemy;
	private PhysicsActor player;
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
		bg0.setVelocityXY(-25, 0);
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

		player = new PhysicsActor();
		Animation animation = GameUtils.parseImageFiles("planeGreen",
				".png",
				3,
				0.1f,
				Animation.PlayMode.LOOP_PINGPONG);
		player.storeAnimation("default", animation);
		player.setPosition(200, 300);
		player.setAccelerationXY(0, -600); // gravity
		player.setOriginCenter();
		player.setEllipseBoundary();
		mainStage.addActor(player);

		baseStar = new PhysicsActor();
		Texture starTexture = new Texture("star.png");
		starTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		baseStar.storeAnimation("default", starTexture);
		baseStar.setVelocityXY(-200, 0);
		baseStar.setOriginCenter();
		baseStar.setEllipseBoundary();

		starList = new ArrayList<PhysicsActor>();
		starTimer = 0;

		baseSparkle = new AnimatedActor();
		Animation sparkleAnimation = GameUtils.parseSpriteSheet("sparkle.png",
				8,
				8,
				0.01f,
				Animation.PlayMode.NORMAL);
		baseSparkle.storeAnimation("default", sparkleAnimation);
		baseSparkle.setWidth(64);
		baseSparkle.setHeight(64);
		baseSparkle.setOriginCenter();

		removeList = new ArrayList<BaseActor>();

		baseEnemy = new PhysicsActor();
		Animation redAnimation = GameUtils.parseImageFiles("planeRed",
				".png",
				3,
				0.1f,
				Animation.PlayMode.LOOP_PINGPONG);
		baseEnemy.storeAnimation("default", redAnimation);
		baseEnemy.setWidth(baseEnemy.getWidth() * 1.25f);
		baseEnemy.setHeight(baseEnemy.getHeight() * 1.25f);
		baseEnemy.setOriginCenter();
		baseEnemy.setEllipseBoundary();

		enemyTimer = 0;
		enemySpeed = -250;
		enemyList = new ArrayList<PhysicsActor>();

		baseExplosion = new AnimatedActor();
		Animation explosionAnimation = GameUtils.parseSpriteSheet("explosion.png",
				6,
				6,
				0.03f,
				Animation.PlayMode.NORMAL);
		baseExplosion.storeAnimation("default", explosionAnimation);
		baseExplosion.setWidth(96);
		baseExplosion.setHeight(96);
		baseExplosion.setOriginCenter();

		gameOver = false;

	}

//	@Override
//	public boolean keyDown(int keycode) {
//		if (keycode == Input.Keys.SPACE)
//			player.setVelocityXY(0,300);
//		return false;
//	}

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

		if (gameOver)
			return;

		if (Gdx.input.isKeyPressed(Input.Keys.SPACE))
			player.addVelocityXY(0, 25);

		if (player.getY() > mapHeight - player.getHeight()){
			player.setVelocityXY(0,0);
			player.setY(mapHeight - player.getHeight());
		}

		for (int i = 0; i < 2; i++) {
			PhysicsActor gr = ground[i];
			if (player.overlaps(gr, true)){
				player.setVelocityXY(0,0);
			}
		}

		starTimer += dt;
		if (starTimer > 1){
			starTimer = 0;
			PhysicsActor star = baseStar.clone();
			star.setPosition(900, MathUtils.random( 100, 500));

			starList.add(star);
			star.setParentList(starList);
			mainStage.addActor(star);
		}

		removeList.clear();

		for (PhysicsActor star: starList){
			if (star.getX() + star.getWidth() < 0)
				removeList.add(star);

			if (player.overlaps(star, false)){
				removeList.add(star);
				AnimatedActor sparkle = baseSparkle.clone();
				sparkle.moveToOrigin(star);
				sparkle.addAction(
						Actions.sequence(
								Actions.delay(0.64f),
								Actions.removeActor() )	);
				mainStage.addActor(sparkle);
			}
		}

		enemyTimer += dt;
		if (enemyTimer > 3){
			enemyTimer = 0;
			if (enemySpeed > - 800)
				enemySpeed -= 15;
			PhysicsActor enemy = baseEnemy.clone();
			enemy.setPosition(900, MathUtils.random(100,500));
			enemy.setVelocityXY(enemySpeed, 0);

			enemy.setRotation(10);
			enemy.addAction(Actions.forever(
					Actions.sequence(Actions.rotateBy(-20,1),
							Actions.rotateBy(20, 1))));

			enemyList.add(enemy);
			enemy.setParentList(enemyList);
			mainStage.addActor(enemy);
		}

		for (PhysicsActor enemy : enemyList){
			if (enemy.getX() + enemy.getWidth() < 0)
				removeList.add(enemy);

			if (player.overlaps(enemy, false)){
				AnimatedActor explosion = baseExplosion.clone();
				explosion.moveToOrigin(player);
				explosion.addAction(Actions.sequence(
						Actions.delay(1.08f), Actions.removeActor()));
				mainStage.addActor(explosion);
				removeList.add(player);
				gameOver = true;
			}
		}

		for (BaseActor ba: removeList){
			ba.destroy();
		}



	}
}
