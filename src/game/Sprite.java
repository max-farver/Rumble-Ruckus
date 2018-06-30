package game;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * This class is where we load and scale all of our images.
 * 
 * @author Clayton Nida
 *
 */
public class Sprite
{
	/**
	 * This will take the given String's file and character and use these to load
	 * the correct images. It will then pass these images along to Sprite's
	 * scale(...) method to scale the images to the correct sizes, while keeping
	 * them smooth.
	 * 
	 * @param file
	 *            The file String to load from.
	 * @param character
	 *            The character used to identify what images to load.
	 * @return Returns the loaded image.
	 */
	public static BufferedImage loadSprite(String file, String character)
	{
		BufferedImage sprite = null;
		try
		{
			if (character.equals("ninja"))
			{
				sprite = ImageIO.read(new File("ninja/" + file + ".png"));
				if (file.substring(0, 6).equals("Attack"))
				{
					sprite = scale(sprite, Game.scale(160), Game.scale(140), 5);
				}
				else
				{
					sprite = scale(sprite, Game.scale(160), Game.scale(118), 5);
				}
			}
			if (character.equals("ninjaGirl"))
			{
				sprite = ImageIO.read(new File("ninjagirl/" + file + ".png"));
				if (file.substring(0, 6).equals("Attack"))
				{
					sprite = scale(sprite, Game.scale(160), Game.scale(140), 5);
				}
				else
				{
					sprite = scale(sprite, Game.scale(160), Game.scale(118), 5);
				}
			}
			if (character.equals("robot"))
			{
				sprite = ImageIO.read(new File("robot/" + file + ".png"));
				sprite = scale(sprite, Game.scale(185), Game.scale(145), 5);
			}
			if (character.equals("knight"))
			{
				sprite = ImageIO.read(new File("knight/" + file + ".png"));
				sprite = scale(sprite, Game.scale(155), Game.scale(150), 5);
			}
			if (character.equals("BG"))
			{
				sprite = ImageIO.read(new File(file));
				if (file.equals("winter/png/BG/BG.png") || file.equals("graveyard/BG.png"))
				{
					sprite = scale(sprite, Game.width, Game.height * 2, 5);
				}
				else
				{
					double resized = Game.height * 1.5;
					int cast = (int) resized;
					sprite = scale(sprite, Game.width, cast, 10);
				}
			}
			if (character.equals("screen"))
			{
				sprite = ImageIO.read(new File(file));
				if (file.equals("img/loading.png"))
				{
					sprite = scale(sprite, Game.width, Game.height * 2, 5);
				}
				if (file.equals("img/Victory_Image.jpg"))
				{
					sprite = scale(sprite, Game.width, Game.height * 2, 5);
				}
				else
				{
					sprite = scale(sprite, Game.width, Game.height * 2, 5);
				}
			}
			if (character.equals("nshot"))
			{
				sprite = ImageIO.read(new File(file));
				sprite = scale(sprite, Game.scale(110), Game.scale(120), 5);
			}
			if (character.equals("kshot"))
			{
				sprite = ImageIO.read(new File(file));
				sprite = scale(sprite, Game.scale(110), Game.scale(100), 5);
			}
			if (character.equals("shot"))
			{
				sprite = ImageIO.read(new File(file));
				sprite = scale(sprite, Game.scale(50), Game.scale(50), 5);
			}
			if (character.equals("platform") || character.equals("floor"))
			{
				sprite = ImageIO.read(new File(file));
				sprite = scale(sprite, Game.scale(sprite.getWidth()), Game.scale(sprite.getHeight()), 5);
			}
			if (character.equals("playerIcon"))
			{
				sprite = ImageIO.read(new File(file));
				sprite = scale(sprite, Game.scale(200), Game.scale(200), 5);
			}
			if (character.equals("death"))
			{
				sprite = ImageIO.read(new File(file));
				sprite = scale(sprite, Game.scale(600), Game.height, 5);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return sprite;
	}

	/**
	 * This will scale a given image to a given width and height. It will do this a
	 * given number of times to make the image appear smoother.
	 * 
	 * @param image
	 *            The image being scaled.
	 * @param width
	 *            The width to scale to.
	 * @param height
	 *            The height to scale to.
	 * @param smoothingSteps
	 *            How many times to draw the image, so that it doesn't appear
	 *            ragged.
	 * @return Returns a method call to draw(...) with the newly scaled/smoothed
	 *         image, and it's width and height.
	 */
	private static BufferedImage scale(BufferedImage image, int width, int height, int smoothingSteps)
			throws IOException
	{
		int w = image.getWidth();
		int h = image.getHeight();
		int targetH, targetW;
		if (w > h)
		{
			targetW = width;
			targetH = (int) ((float) h / w * height);
		}
		else
		{
			targetH = height;
			targetW = (int) ((float) w / h * width);
		}
		BufferedImage i = image;
		// for (int n = 0; (w / 2) >= targetW && (h / 2) >= targetH && n <
		// smoothingSteps; i = draw(i, w, h))
		for (int n = 0; (w / 2) >= targetW && (h / 2) >= targetH && n < smoothingSteps; n++)
		{
			// System.out.println("a" + n + ":" +System.currentTimeMillis());
			w /= 2;
			h /= 2;
			i = scaleDown(i, w, h);
			// System.out.println("e"+ System.currentTimeMillis());
		}
		return draw(i, targetW, targetH);
	}

	private static BufferedImage draw(BufferedImage image, int w, int h) throws IOException
	{
		GraphicsEnvironment thisSlowComputer = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsConfiguration computerConfig = thisSlowComputer.getDefaultScreenDevice().getDefaultConfiguration();
		BufferedImage buffer = computerConfig.createCompatibleImage(w, h,
				(image.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB
						: BufferedImage.TYPE_INT_ARGB);
		// Graphics2D graphics = bImage.createGraphics();
		// BufferedImage buffer = new BufferedImage(w, h, (image.getTransparency() ==
		// Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB :
		// BufferedImage.TYPE_INT_ARGB);

		Graphics2D graphics2d = buffer.createGraphics();

		graphics2d.drawImage(scaleDown(image, w, h), 0, 0, null);

		graphics2d.dispose();

		return buffer;
	}

	private static BufferedImage scaleDown(BufferedImage image, int width, int height) throws IOException
	{
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();

		double scaleX = (double) width / imageWidth;
		double scaleY = (double) height / imageHeight;

		AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
		AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);

		return bilinearScaleOp.filter(image, new BufferedImage(width, height, image.getType()));
	}
}
