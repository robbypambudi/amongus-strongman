package id.game.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import id.main.GamePanel;

public class KeyboardInputs implements KeyListener{
	private GamePanel gamePanel;
	
	public KeyboardInputs(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_W:
			gamePanel.getGame().getPlayer().setJump(true);
			break;
		case KeyEvent.VK_A:
			gamePanel.getGame().getPlayer().setLeft(true);
			break;
		case KeyEvent.VK_S:
			gamePanel.getGame().getPlayer().setDown(true);
			break;
		case KeyEvent.VK_D:
			gamePanel.getGame().getPlayer().setRight(true);
			break;
		case KeyEvent.VK_UP:
			gamePanel.getGame().getPlayer2().setJump(true);
			break;
		case KeyEvent.VK_LEFT:
			gamePanel.getGame().getPlayer2().setLeft(true);
			break;
		case KeyEvent.VK_DOWN:
			gamePanel.getGame().getPlayer2().setDown(true);
			break;
		case KeyEvent.VK_RIGHT:
			gamePanel.getGame().getPlayer2().setRight(true);
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_W:
			gamePanel.getGame().getPlayer().setJump(false);
			break;
		case KeyEvent.VK_A:
			gamePanel.getGame().getPlayer().setLeft(false);
			break;
		case KeyEvent.VK_S:
			gamePanel.getGame().getPlayer().setDown(false);
			break;
		case KeyEvent.VK_D:
			gamePanel.getGame().getPlayer().setRight(false);
			break;
		case KeyEvent.VK_UP:
			gamePanel.getGame().getPlayer2().setJump(false);
			break;
		case KeyEvent.VK_LEFT:
			gamePanel.getGame().getPlayer2().setLeft(false);
			break;
		case KeyEvent.VK_DOWN:
			gamePanel.getGame().getPlayer2().setDown(false);
			break;
		case KeyEvent.VK_RIGHT:
			gamePanel.getGame().getPlayer2().setRight(false);
			break;
		}
	}

}