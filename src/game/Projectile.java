package game;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * This class extends the GameObject class; It is used to create a GameObject
 * that players can create at will that will fly through the air, in an attempt
 * to damage their opponents.
 * 
 * @author Clayton Nida
 *
 */
public class Projectile extends GameObject
{
	private volatile BufferedImage projectile;
	private volatile BufferedImage projectileLeft;
	private Rectangle rangedAttack = new Rectangle();
	private int facing;
	private Handler handler;

	/**
	 * This will construct the projectile at the given location and in the given
	 * direction. It will load the correct image based on the ID passed to it.
	 * It will begin flying 20 pixels a frame.
	 * 
	 * @param x
	 *            Starting x value of the projectile.
	 * @param y
	 *            Starting y value of the projectile.
	 * @param id
	 *            The ID number used to distinguish this GameObject from others.
	 * @param facing
	 *            The direction the projectile is facing.
	 * @param handler
	 *            The handler used to loop through all GameObjects.
	 */
	public Projectile(int x, int y, int id, int facing, String character, Handler handler)
	{
		super(x, y, id);
		this.facing = facing;
		this.handler = handler;
		if (facing == 0)
		{
			this.setSpeedX(20);
		}
		else
		{
			this.setSpeedX(-20);
		}
		rangedAttack.setBounds(x, y, Game.scale(50), Game.scale(50));
		if (character.equals("ninja") || character.equals("ninjaGirl"))
		{
			projectile = Sprite.loadSprite("ninja/Kunai.png", "nshot");
			projectileLeft = Sprite.loadSprite("ninja/KunaiLeft.png", "nshot");
		}
		else if (character.equals("robot"))
		{
			projectile = Sprite.loadSprite("robot/objects/Bullet_000.png", "shot");
			projectileLeft = Sprite.loadSprite("robot/objects/BulletLeft_000.png", "shot");
		}
		else if (character.equals("knight"))
		{
			projectile = Sprite.loadSprite("knight/spear.png", "kshot");
			projectileLeft = Sprite.loadSprite("knight/spearLeft.png", "kshot");
		}
		this.setHitbox(rangedAttack);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see game.GameObject#tick()
	 */
	@Override
	public void tick()
	{
		x += speedX;
		rangedAttack.setBounds(x, y, Game.scale(50), Game.scale(50));
		if (x < -900 || x > Game.width + 900)
		{
			handler.removeObject(this);
		}
		doThrownCollisionDetection(rangedAttack);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see game.GameObject#render(java.awt.Graphics)
	 */
	@Override
	public void render(Graphics g)
	{
		if (facing == 0)
		{
			g.drawImage(projectile, x, y, null);
		}
		else
		{
			g.drawImage(projectileLeft, x, y, null);
		}
	}

	/**
	 * This will loop through all GameObjects and check to see if the projectile
	 * collides with any players. If it does it removes itself and damages the
	 * player.
	 * 
	 * @param shot
	 *            The rectangle being checked for collision.
	 */
	public void doThrownCollisionDetection(Rectangle shot)
	{
		for (int i = 0; i < handler.object.size(); i++)
		{
			GameObject tempObject = handler.object.get(i);
			if (tempObject.getID() != (this.id - 10) && tempObject.getID() < 10 && tempObject.getID() > 0)
			{
				if (shot.intersects(tempObject.getHitbox()) && !tempObject.getShielding())
				{
					tempObject.setDamage(tempObject.getDamage() + 1);
					handler.removeObject(this);
				}
				else if (shot.intersects(tempObject.getHitbox()) && tempObject.getShielding())
				{
					tempObject.setShieldCharge(tempObject.getShieldCharge() - Game.scale(1));
					handler.removeObject(this);
				}
			}
		}
	}
}
