package game;

import java.awt.image.BufferedImage;

/**
 *  Manipulates separate frames that are being animated.
 */
public class Frame
{
	private BufferedImage frame;
	private int duration;

	/**
	 * Frame constructor. Creates a Frame object with the given image and duration.
	 * @param frame Image this Frame consists of.
	 * @param duration The given duration of this Frame.
	 */ 
	public Frame(BufferedImage frame, int duration)
	{
		this.frame = frame;
		this.duration = duration;
	}

	/**
	 * @return Returns this frame image.
	 */
	public BufferedImage getFrame()
	{
		return frame;
	}

	/**
	 * @param frame Sets this frames image to the given image.
	 */
	public void setFrame(BufferedImage frame)
	{
		this.frame = frame;
	}

	/**
	 * @return Returns the duration of this frame.
	 */
	public int getDuration()
	{
		return duration;
	}

	/**
	 * @param duration Sets the duration of this frame.
	 */
	public void setDuration(int duration)
	{
		this.duration = duration;
	}
}