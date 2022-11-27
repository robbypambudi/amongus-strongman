package id.main;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import id.game.input.KeyboardInputs;
import id.game.input.MouseInputs;
import static id.main.Game.*;

public class GamePanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private MouseInputs mouseInputs;
	private Game game;
	public GamePanel(Game game) {
		mouseInputs = new MouseInputs(this);
		this.game = game;
		
		setPanelSize();
		addKeyListener(new KeyboardInputs(this));
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
	}
	

	private void setPanelSize() {
		Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
		setPreferredSize(size);
	}
	
	public void updateGame() {
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		game.render(g);
	}

	public Game getGame() {
		return game;
	}

	

	
}
