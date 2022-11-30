package id.game.levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import id.game.utils.LoadSave;
import id.main.Game;

public class LevelManager {
	private Game game;
	private BufferedImage[] levelSprite;
	private Level levelOne;
	
	public LevelManager(Game game) {
		this.game = game;
		importOutsideSprites();
		levelOne = new Level(LoadSave.GetLevelData());
	}
	
	private void importOutsideSprites() {
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
		levelSprite = new BufferedImage[364];
		for(int j = 0; j < 14; j++)
			for(int i = 0; i < 26; i++) {
				int index = j*26 + i;
				levelSprite[index] = img.getSubimage(i * 32, j * 32, 32, 32);
			}
	}

	public void draw(Graphics g) {
		for(int j = 0; j < 14; j++)
			for(int i = 0; i < 26; i++) {
//				int index = levelOne.getSpriteIndex(i, j);
				int index = j*26 + i;
				g.drawImage(levelSprite[index], Game.TILES_SIZE * i, Game.TILES_SIZE * j, Game.TILES_SIZE, Game.TILES_SIZE, null);
			}
		
	}
	
	public void update() {
		
	}
	
	public Level getCurrentLevel() {
		return levelOne;
	}
}
