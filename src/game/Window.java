package game;

import java.awt.Canvas;
import java.awt.Dimension;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.JFrame;

/**
 * This Class will construct the window used to display the game.
 * 
 * @author Clayton Nida
 *
 */
public class Window extends Canvas
{
	private static final long serialVersionUID = 3937514150523938004L;
	JFrame frame;

	/**
	 * Creates a new window of the given width/height and displays the given Game.
	 * 
	 * @param width
	 *            Width of the window.
	 * @param height
	 *            Height of the window.
	 * @param title
	 *            Name of the window.
	 * @param game
	 *            The game that is being displayed.
	 */
	Window(int width, int height, String title, Game game)
	{
		frame = new JFrame(title);
		// drawingComponent LP = new drawingComponent();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setMaximumSize(new Dimension(width, height));
		frame.setMinimumSize(new Dimension(width, height));
		// frame.add(LP);
		frame.add(game);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		// frame.setSize(1600, 900);
		frame.setVisible(true);
		frame.setAlwaysOnTop(true);
		try
		{
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("sounds/The-Jewels-Era.wav"));
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(-30.0f); // Reduce volume by 30 decibels.
			clip.start();
			clip.loop(4);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		game.start();
	}
}
