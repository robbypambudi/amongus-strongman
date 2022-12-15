package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import objects.ObjectManager;
import ui.GameOverOverlay;
import ui.LevelCompletedOverlay;
import ui.PauseOverlay;
import utilz.LoadSave;
import static utilz.Constants.Environment.*;

public class Playing extends State implements Statemethods {
	private static Player player1;
	private static Player player2;
	private LevelManager levelManager;
	private EnemyManager enemyManager;
	private ObjectManager objectManager;
	private PauseOverlay pauseOverlay;
	private GameOverOverlay gameOverOverlay;
	private LevelCompletedOverlay levelCompletedOverlay;
	private boolean paused = false;

	private int xLvlOffset;
	private int leftBorder = (int) (0.2 * Game.GAME_WIDTH);
	private int rightBorder = (int) (0.8 * Game.GAME_WIDTH);
	private int maxLvlOffsetX;

	private BufferedImage backgroundImg, bigCloud, smallCloud;
	private int[] smallCloudsPos;
	private Random rnd = new Random();

	private boolean gameOver;
	private boolean lvlCompleted;

	public Playing(Game game) {
		super(game);
		initClasses();

		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG);
		bigCloud = LoadSave.GetSpriteAtlas(LoadSave.BIG_CLOUDS);
		smallCloud = LoadSave.GetSpriteAtlas(LoadSave.SMALL_CLOUDS);
		smallCloudsPos = new int[8];
		for (int i = 0; i < smallCloudsPos.length; i++)
			smallCloudsPos[i] = (int) (90 * Game.SCALE) + rnd.nextInt((int) (100 * Game.SCALE));

		calcLvlOffset();
		loadStartLevel();
	}

	public void loadNextLevel() {
		resetAll();
		levelManager.loadNextLevel();
		player1.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
		player2.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
	}

	private void loadStartLevel() {
		enemyManager.loadEnemies(levelManager.getCurrentLevel());
		objectManager.loadObjects(levelManager.getCurrentLevel());
	}

	private void calcLvlOffset() {
		maxLvlOffsetX = levelManager.getCurrentLevel().getLvlOffset();
	}

	private void initClasses() {
		levelManager = new LevelManager(game);
		enemyManager = new EnemyManager(this);
		objectManager = new ObjectManager(this);

		player1 = new Player(200, 200, (int) (64 * Game.SCALE), (int) (40 * Game.SCALE), this, "Amogus 1", 34, 10);
		player2 = new Player(200, 200, (int) (64 * Game.SCALE), (int) (40 * Game.SCALE), this, "Amogus 2",
				Game.GAME_WIDTH / 60,
				Game.GAME_WIDTH / 3);
		player1.loadLvlData(levelManager.getCurrentLevel().getLevelData());
		player1.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
		player2.loadLvlData(levelManager.getCurrentLevel().getLevelData());
		player2.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());

		pauseOverlay = new PauseOverlay(this);
		gameOverOverlay = new GameOverOverlay(this);
		levelCompletedOverlay = new LevelCompletedOverlay(this);
	}

	@Override
	public void update() {
		if (paused) {
			pauseOverlay.update();
		} else if (lvlCompleted) {
			levelCompletedOverlay.update();
		} else if (!gameOver) {
			levelManager.update();
			objectManager.update(levelManager.getCurrentLevel().getLevelData(), player1);
			objectManager.update(levelManager.getCurrentLevel().getLevelData(), player2);
			player1.update();
			player2.update();
			enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player1);
			enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player2);
			checkCloseToBorder();
		}
	}

	private void checkCloseToBorder() {
		int playerX1 = (int) player1.getHitbox().x;
		int playerX2 = (int) player2.getHitbox().x;
		int diff1 = playerX1 - xLvlOffset;
		int diff2 = playerX2 - xLvlOffset;

		if (diff1 > rightBorder)
			xLvlOffset += diff1 - rightBorder;
		else if (diff1 < leftBorder)
			xLvlOffset += diff1 - leftBorder;

		if (diff2 > rightBorder)
			xLvlOffset += diff2 - rightBorder;
		else if (diff2 < leftBorder)
			xLvlOffset += diff2 - leftBorder;

		if (xLvlOffset > maxLvlOffsetX)
			xLvlOffset = maxLvlOffsetX;
		else if (xLvlOffset < 0)
			xLvlOffset = 0;
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

		drawClouds(g);

		levelManager.draw(g, xLvlOffset);
		player1.render(g, xLvlOffset);
		player2.render(g, xLvlOffset);
		enemyManager.draw(g, xLvlOffset);
		objectManager.draw(g, xLvlOffset);

		if (paused) {
			g.setColor(new Color(0, 0, 0, 150));
			g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
			pauseOverlay.draw(g);
		} else if (gameOver)
			gameOverOverlay.draw(g);
		else if (lvlCompleted)
			levelCompletedOverlay.draw(g);
	}

	private void drawClouds(Graphics g) {
		for (int i = 0; i < 3; i++)
			g.drawImage(bigCloud, i * BIG_CLOUD_WIDTH - (int) (xLvlOffset * 0.3), (int) (204 * Game.SCALE), BIG_CLOUD_WIDTH,
					BIG_CLOUD_HEIGHT, null);

		for (int i = 0; i < smallCloudsPos.length; i++)
			g.drawImage(smallCloud, SMALL_CLOUD_WIDTH * 4 * i - (int) (xLvlOffset * 0.7), smallCloudsPos[i],
					SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
	}

	public void resetAll() {
		gameOver = false;
		paused = false;
		lvlCompleted = false;
		player1.resetAll();
		player2.resetAll();
		enemyManager.resetAllEnemies();
		objectManager.resetAllObjects();
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public void checkObjectHit(Rectangle2D.Float attackBox) {
		objectManager.checkObjectHit(attackBox);
	}

	public void checkEnemyHit(Rectangle2D.Float attackBox) {
		enemyManager.checkEnemyHit(attackBox);
	}

	public void checkPotionTouched(Rectangle2D.Float hitbox) {
		objectManager.checkObjectTouched(hitbox);
	}

	public void checkSpikesTouched(Player p) {
		objectManager.checkSpikesTouched(p);
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (gameOver)
			gameOverOverlay.keyPressed(e);
		else
			switch (e.getKeyCode()) {
				case KeyEvent.VK_A:
					player1.setLeft(true);
					break;
				case KeyEvent.VK_D:
					player1.setRight(true);
					break;
				case KeyEvent.VK_W:
					player1.setJump(true);
					break;
				case KeyEvent.VK_F:
					if (!player1.getAttack())
						player1.setAttacking1(true);
					break;
				case KeyEvent.VK_LEFT:
					player2.setLeft(true);
					break;
				case KeyEvent.VK_RIGHT:
					player2.setRight(true);
					break;
				case KeyEvent.VK_UP:
					player2.setJump(true);
					break;
				case KeyEvent.VK_L:
					player2.setAttacking2(true);
					break;
				case KeyEvent.VK_ESCAPE:
					paused = !paused;
					break;
			}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (!gameOver)
			switch (e.getKeyCode()) {
				case KeyEvent.VK_A:
					player1.setLeft(false);
					break;
				case KeyEvent.VK_D:
					player1.setRight(false);
					break;
				case KeyEvent.VK_W:
					player1.setJump(false);
					break;
				case KeyEvent.VK_LEFT:
					player2.setLeft(false);
					break;
				case KeyEvent.VK_RIGHT:
					player2.setRight(false);
					break;
				case KeyEvent.VK_UP:
					player2.setJump(false);
					break;
			}

	}

	public void mouseDragged(MouseEvent e) {
		if (!gameOver)
			if (paused)
				pauseOverlay.mouseDragged(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (!gameOver) {
			if (paused)
				pauseOverlay.mousePressed(e);
			else if (lvlCompleted)
				levelCompletedOverlay.mousePressed(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (!gameOver) {
			if (paused)
				pauseOverlay.mouseReleased(e);
			else if (lvlCompleted)
				levelCompletedOverlay.mouseReleased(e);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (!gameOver) {
			if (paused)
				pauseOverlay.mouseMoved(e);
			else if (lvlCompleted)
				levelCompletedOverlay.mouseMoved(e);
		}
	}

	public void setLevelCompleted(boolean levelCompleted) {
		this.lvlCompleted = levelCompleted;
	}

	public void setMaxLvlOffset(int lvlOffset) {
		this.maxLvlOffsetX = lvlOffset;
	}

	public void unpauseGame() {
		paused = false;
	}

	public void windowFocusLost() {
		player1.resetDirBooleans();
		player2.resetDirBooleans();
	}

	public static Player getPlayer1() {
		return player1;
	}

	public static Player getPlayer2() {
		return player2;
	}

	public EnemyManager getEnemyManager() {
		return enemyManager;
	}

	public ObjectManager getObjectManager() {
		return objectManager;
	}

	public LevelManager getLevelManager() {
		return levelManager;
	}

}