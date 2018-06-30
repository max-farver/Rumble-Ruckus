package game;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Animates all of our GameObjects using an array of Images converted to frames.
 */
public class Animation
{
	private int frameCount; // Counts ticks for change
	private int frameDelay; // frame delay 1-12
	private int currentFrame; // animations current frame
	private int animationDirection; // animation direction (i.e counting forward
									// or backward)
	private int totalFrames; // total amount of frames of the animation
	private boolean stopped; // has animations stopped
	private List<Frame> frames = new ArrayList<Frame>(); // Arraylist of frames

	/**
	 * The constructor for the Animation class that will set all the variables.
	 * 
	 * @param frames
	 *            The array of images to animate.
	 * @param frameDelay
	 *            The delay between frames.
	 */
	public Animation(BufferedImage[] frames, int frameDelay)
	{
		this.frameDelay = frameDelay;
		this.stopped = true;
		for (int i = 0; i < frames.length; i++)
		{
			addFrame(frames[i], frameDelay);
		}
		this.frameCount = 0;
		this.frameDelay = frameDelay;
		this.currentFrame = 0;
		this.animationDirection = 1;
		this.totalFrames = this.frames.size();
	}

	/**
	 * Starts the animation.
	 */
	public void start()
	{
		if (!stopped)
		{
			return;
		}
		if (frames.size() == 0)
		{
			return;
		}
		stopped = false;
	}

	/**
	 * Stops the animation.
	 */
	public void stop()
	{
		if (frames.size() == 0)
		{
			return;
		}
		stopped = true;
	}

	/**
	 * Restarts the Animation to the first frame and animates.
	 */
	public void restart()
	{
		if (frames.size() == 0)
		{
			return;
		}
		stopped = false;
		currentFrame = 0;
	}

	/**
	 * Resets the animation. Stopping it and setting it back to the beginning
	 * frame.
	 */
	public void reset()
	{
		this.stopped = true;
		this.frameCount = 0;
		this.currentFrame = 0;
	}

	/**
	 * Creates a new Frame object with the given image and duration.
	 * 
	 * @param frame
	 *            Frame to be added.
	 * @param duration
	 *            duration of the frame.
	 */
	private void addFrame(BufferedImage frame, int duration)
	{
		if (duration <= 0)
		{
			System.err.println("Invalid duration: " + duration);
			throw new RuntimeException("Invalid duration: " + duration);
		}
		frames.add(new Frame(frame, duration));
		currentFrame = 0;
	}

	/**
	 * 
	 * @return Returns the image that the animation is currently on.
	 */
	public BufferedImage getSprite()
	{
		return frames.get(currentFrame).getFrame();
	}

	/**
	 * This is what updates the animation to correctly display the image/frame
	 * that we are currently on.
	 */
	public void update()
	{
		if (!stopped)
		{
			frameCount++;
			if (frameCount > frameDelay)
			{
				frameCount = 0;
				currentFrame += animationDirection;
				if (currentFrame > totalFrames - 1)
				{
					currentFrame = 0;
				}
				else if (currentFrame < 0)
				{
					currentFrame = totalFrames - 1;
				}
			}
		}
	}

	public int getEndFrame()
	{
		return totalFrames;
	}

	public int getCurrentFrame()
	{
		return currentFrame;
	}
}