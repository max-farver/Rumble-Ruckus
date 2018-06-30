package game;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * It is used as an abstract class, and contains all the methods that all
 * objects within the game will use.
 * 
 * @author Clayton Nida
 *
 */
public abstract class GameObject
{
	protected int x, y;
	protected int id;
	protected int speedX, speedY;
	protected boolean facingLeft;
	protected boolean running;
	protected boolean jumping;
	protected boolean attacking;
	protected boolean shooting;
	protected Rectangle player;
	protected int damage;
	protected int stock;
	protected boolean doubleJumped = false;
	protected int jumped = 0;
	protected int oldX = 0;
	protected int oldY = 0;
	protected boolean death = false;
	protected volatile boolean inAction = false;
	protected boolean invulnerable;
	protected String character = "";
	protected int shieldCharge = 100;
	protected boolean shielding = false;
	protected String name = "";
	protected String killedBy = "";
	protected int kills = 0;

	/**
	 * Constructs a GameObject with the given x and y locations, and with the
	 * given ID number.
	 * 
	 * @param x
	 *            Starting x location.
	 * @param y
	 *            Starting y location.
	 * @param id
	 *            Given ID number.
	 */
	public GameObject(int x, int y, int id)
	{
		this.x = x;
		this.y = y;
		oldX = x;
		oldY = y;
		this.id = id;
		if (id % 2 == 0)
		{
			facingLeft = true;
		}
	}

	/**
	 * This is the method that will be called to "tick" the object. Each object
	 * will have different logic that will be used after tick is called.
	 */
	public abstract void tick();

	/**
	 * This will be used to draw the images needed. Depending on each
	 * GameObject, different things will be rendered. Example: players will have
	 * their models rendered, and level will have maps and platforms rendered.
	 * 
	 * @param g
	 *            The given Graphics object that will be used to draw/render all
	 *            images.
	 */
	public abstract void render(Graphics g);

	/**
	 * This will move this GameObject to the given X location.
	 * 
	 * @param x
	 *            X value to set this GameObject to.
	 */
	public void setX(int x)
	{
		this.x = x;
	}

	/**
	 * This will move this GameObject to the given Y location.
	 * 
	 * @param y
	 *            Y value to set this GameObject to.
	 */
	public void setY(int y)
	{
		this.y = y;
	}

	/**
	 * This will set the number of times a GameObject has jumped.
	 * 
	 * @param jumped
	 *            The number of times this GameObject has jumped.
	 */
	public void setJumped(int jumped)
	{
		this.jumped = jumped;
	}

	/**
	 * @return Returns the number of times a GameObject has jumped.
	 */
	public int getJumped()
	{
		return jumped;
	}

	/**
	 * This will set the status of a GameObject's doubleJumped to true or false.
	 * 
	 * @param doubleJumped
	 *            Boolean value saying whether or not a GameObject has double
	 *            jumped.
	 */
	public void setDoubleJumped(boolean doubleJumped)
	{
		this.doubleJumped = doubleJumped;
	}

	/**
	 * @return Whether or not a GameObject has double jumped.
	 */
	public boolean getDoubleJumped()
	{
		return doubleJumped;
	}

	/**
	 * Changes a GameObject's ID.
	 * 
	 * @param id
	 *            The number that this GameObject's ID will be set to.
	 */
	public void setID(int id)
	{
		this.id = id;
	}

	/**
	 * @return Returns the X location of this GameObject.
	 */
	public int getX()
	{
		return x;
	}

	/**
	 * @return Returns the Y location of this GameObject
	 */
	public int getY()
	{
		return y;
	}

	/**
	 * @return Returns the ID of this GameObject.
	 */
	public int getID()
	{
		return id;
	}

	/**
	 * This will set the speed that this GameObject is moving horizontally.
	 * 
	 * @param speedX
	 *            The speed that this GameObject will move in pixels.
	 */
	public void setSpeedX(int speedX)
	{
		this.speedX = speedX;
	}

	/**
	 * This will set the speed that this GameObject is moving vertically.
	 * 
	 * @param speedY
	 *            The speed that this GameObject will move in pixels.
	 */
	public void setSpeedY(int speedY)
	{
		this.speedY = speedY;
	}

	/**
	 * @return Returns the speed this GameObject is moving horizontally.
	 */
	public int getSpeedX()
	{
		return speedX;
	}

	/**
	 * @return Returns the speed this GameObject is moving vertically.
	 */
	public int getSpeedY()
	{
		return speedY;
	}

	/**
	 * @return Returns whether or not this GameObject is facing left or not.
	 */
	public boolean getFacingLeft()
	{
		return facingLeft;
	}

	/**
	 * @param facingLeft
	 *            Sets facingLeft to true or false.
	 */
	public void setFacingLeft(boolean facingLeft)
	{
		this.facingLeft = facingLeft;
	}

	/**
	 * @return Returns whether or not this GameObject is running or not.
	 */
	public boolean getRunning()
	{
		return running;
	}

	/**
	 * @param running
	 *            Sets this GameObject's running to true or false.
	 */
	public void setRunning(boolean running)
	{
		this.running = running;
	}

	/**
	 * @return Returns whether or not this GameObject is attacking or not.
	 */
	public boolean getAttacking()
	{
		return attacking;
	}

	/**
	 * @param attacking
	 *            Sets this GameObject's attacking to true or false.
	 */
	public void setAttacking(boolean attacking)
	{
		this.attacking = attacking;
	}

	/**
	 * @return Returns whether or not this GameObject is shooting or not.
	 */
	public boolean getShooting()
	{
		return shooting;
	}

	/**
	 * @param shooting
	 *            Sets this GameObject's shooting to true or false.
	 */
	public void setShooting(boolean shooting)
	{
		this.shooting = shooting;
	}

	/**
	 * @return Returns whether or not this GameObject is jumping or not.
	 */
	public boolean getJumping()
	{
		return jumping;
	}

	/**
	 * @param jumping
	 *            Sets this GameObject's jumping to true or false.
	 */
	public void setJumping(boolean jumping)
	{
		this.jumping = jumping;
	}

	/**
	 * @return Returns the Rectangle that is used as this GameObject's hit box.
	 */
	public Rectangle getHitbox()
	{
		return player;
	}

	/**
	 * @param player
	 *            Sets this GameObject's hit box to the given Rectangle.
	 */
	public void setHitbox(Rectangle player)
	{
		this.player = player;
	}

	/**
	 * @param damage
	 *            Sets this GameObject's damage to the given argument.
	 */
	public void setDamage(int damage)
	{
		this.damage = damage;
	}

	/**
	 * @return Returns this GameObject's damage.
	 */
	public int getDamage()
	{
		return damage;
	}

	/**
	 * @param stock
	 *            Sets this GameObject's number of lives.
	 */
	public void setStock(int stock)
	{
		this.stock = stock;
	}

	/**
	 * @return Returns this GameObject's number of lives.
	 */
	public int getStock()
	{
		return stock;
	}

	public boolean getInAction()
	{
		return inAction;
	}

	public void setInAction(boolean inAction)
	{
		this.inAction = inAction;
	}

	public void storeXandY(int x, int y)
	{
		this.oldX = x;
		this.oldY = y;
	}

	public boolean checkXandY(int requestX, int requestY)
	{
		return ((this.oldX == requestX) && (this.oldY == requestY));
	}

	public int getOldX()
	{
		return oldX;
	}

	public int getOldY()
	{
		return oldY;
	}

	public boolean isDead()
	{
		return this.death;
	}

	public void setDeath(boolean deathStatus)
	{
		this.death = deathStatus;
	}

	public void setCharacter(String character)
	{
		this.character = character;
	}

	/**
	 * @return Returns this GameObject's number of lives.
	 */
	public String getCharacter()
	{
		return character;
	}

	public int getShieldCharge()
	{
		return shieldCharge;
	}

	public void setShieldCharge(int shieldCharge)
	{
		this.shieldCharge = shieldCharge;
	}
	
	public boolean getShielding()
	{
		return shielding;
	}

	public void setShielding(boolean shielding)
	{
		this.shielding = shielding;
	}


	public int getKills()
	{
		return kills;
	}

	public void setKills(int kills)
	{
		this.kills = kills;
	}

	public String getKilledBy()
	{
		return killedBy;
	}

	public void setKilledBy(String killedBy)
	{
		this.killedBy = killedBy;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
