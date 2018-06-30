package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Player extends GameObject
{
	private volatile BufferedImage[] idle;
	private volatile BufferedImage[] dDeath;
	private volatile BufferedImage[] idleLeft;
	private volatile BufferedImage[] jump;
	private volatile BufferedImage[] jumpLeft;
	private volatile BufferedImage[] run;
	private volatile BufferedImage[] runLeft;
	private volatile BufferedImage[] attack;
	private volatile BufferedImage[] attackLeft;
	private volatile BufferedImage[] jumpAttack;
	private volatile BufferedImage[] jumpAttackLeft;
	private volatile BufferedImage[] shoot;
	private volatile BufferedImage[] shootLeft;
	private volatile Animation dDieing;
	private volatile Animation standing;
	private volatile Animation standingLeft;
	private volatile Animation jumping;
	private volatile Animation jumpingLeft;
	private volatile Animation running;
	private volatile Animation runningLeft;
	private volatile Animation animation;
	private volatile Animation attacking;
	private volatile Animation attackingLeft;
	private volatile Animation jumpAttacking;
	private volatile Animation jumpAttackingLeft;
	private volatile Animation shooting;
	private volatile Animation shootingLeft;
	private Handler handler;
	private Rectangle p1 = new Rectangle();
	private Rectangle p2 = new Rectangle();
	private Rectangle p3 = new Rectangle();
	private Rectangle p4 = new Rectangle();
	public int damageTaken = 0;
	public static int playOnce = 0;
	private Color playerColor;
	private Font nameFont;
	public boolean shotReset;
	private boolean canThrow = true;
	private boolean canAttack = true;
	private long timeOfDeath;
	private long timerForShieldGain;
	private long timerForShieldLoss;
	private String name = "";
	private String character;
	private double acceleration;
	private double store_acceleration;
	static final double gravity_constant = .3;
	private boolean oneSecond = false;
	private boolean halfSecond = false;

	public Player(int x, int y, int id, String character, String name, int stock, Handler handler)
	{
		super(x, y, id);
		this.setStock(stock);
		this.setCharacter(character);
		this.handler = handler;
		this.character = character;
		this.setName(name);
		if (!name.equals(null))
		{
			this.name = name;
		}
		if (id < 0)
		{
			this.setDeath(true);
		}
		else
		{
			this.setDeath(false);
		}
		acceleration = gravity_constant;
		idle = new BufferedImage[10];
		jump = new BufferedImage[10];
		shoot = new BufferedImage[10];
		shootLeft = new BufferedImage[10];
		dDeath = new BufferedImage[11];
		if (character.equals("robot"))
		{
			run = new BufferedImage[8];
			runLeft = new BufferedImage[8];
			attack = new BufferedImage[8];
			attackLeft = new BufferedImage[8];
			shoot = new BufferedImage[5];
			shootLeft = new BufferedImage[5];
		}
		else
		{
			run = new BufferedImage[10];
			runLeft = new BufferedImage[10];
			attack = new BufferedImage[10];
			attackLeft = new BufferedImage[10];
		}
		idleLeft = new BufferedImage[10];
		jumpLeft = new BufferedImage[10];
		jumpAttack = new BufferedImage[10];
		jumpAttackLeft = new BufferedImage[10];
		if (id == 1)
		{
			p1.setBounds(x, y, Game.scale(94), Game.scale(118));
			this.setHitbox(p1);
			playerColor = new Color(255, 0, 0, 50);
		}
		if (id == 2)
		{
			p2.setBounds(x, y, Game.scale(94), Game.scale(118));
			this.setHitbox(p2);
			playerColor = new Color(0, 0, 255, 50);
		}
		if (id == 3)
		{
			p3.setBounds(x, y, Game.scale(94), Game.scale(118));
			this.setHitbox(p3);
			playerColor = new Color(0, 255, 0, 50);
		}
		if (id == 4)
		{
			p4.setBounds(x, y, Game.scale(94), Game.scale(118));
			this.setHitbox(p4);
			playerColor = new Color(0, 255, 255, 50);
		}
		for (int i = 0; i < 10; i++)
		{
			if (character.equals("ninja"))
			{
				idle[i] = Sprite.loadSprite("Idle__00" + i, "ninja");
				idleLeft[i] = Sprite.loadSprite("IdleLeft__00" + i, "ninja");
				jump[i] = Sprite.loadSprite("Jump__00" + i, "ninja");
				jumpLeft[i] = Sprite.loadSprite("JumpLeft__00" + i, "ninja");
				run[i] = Sprite.loadSprite("Run__00" + i, "ninja");
				runLeft[i] = Sprite.loadSprite("RunLeft__00" + i, "ninja");
				attack[i] = Sprite.loadSprite("Attack__00" + i, "ninja");
				attackLeft[i] = Sprite.loadSprite("AttackLeft__00" + i, "ninja");
				jumpAttack[i] = Sprite.loadSprite("Jump_Attack__00" + i, "ninja");
				jumpAttackLeft[i] = Sprite.loadSprite("Jump_AttackLeft__00" + i, "ninja");
				shoot[i] = Sprite.loadSprite("Throw__00" + i, "ninja");
				shootLeft[i] = Sprite.loadSprite("ThrowLeft__00" + i, "ninja");
			}
			if (character.equals("ninjaGirl"))
			{
				idle[i] = Sprite.loadSprite("Idle__00" + i, "ninjaGirl");
				idleLeft[i] = Sprite.loadSprite("IdleLeft__00" + i, "ninjaGirl");
				jump[i] = Sprite.loadSprite("Jump__00" + i, "ninjaGirl");
				jumpLeft[i] = Sprite.loadSprite("JumpLeft__00" + i, "ninjaGirl");
				run[i] = Sprite.loadSprite("Run__00" + i, "ninjaGirl");
				runLeft[i] = Sprite.loadSprite("RunLeft__00" + i, "ninjaGirl");
				attack[i] = Sprite.loadSprite("Attack__00" + i, "ninjaGirl");
				attackLeft[i] = Sprite.loadSprite("AttackLeft__00" + i, "ninjaGirl");
				jumpAttack[i] = Sprite.loadSprite("Jump_Attack__00" + i, "ninjaGirl");
				jumpAttackLeft[i] = Sprite.loadSprite("Jump_AttackLeft__00" + i, "ninjaGirl");
				shoot[i] = Sprite.loadSprite("Throw__00" + i, "ninjaGirl");
				shootLeft[i] = Sprite.loadSprite("ThrowLeft__00" + i, "ninjaGirl");
			}
			if (character.equals("knight"))
			{
				idle[i] = Sprite.loadSprite("Idle__00" + i, "knight");
				idleLeft[i] = Sprite.loadSprite("IdleLeft__00" + i, "knight");
				jump[i] = Sprite.loadSprite("Jump__00" + i, "knight");
				jumpLeft[i] = Sprite.loadSprite("JumpLeft__00" + i, "knight");
				run[i] = Sprite.loadSprite("Run__00" + i, "knight");
				runLeft[i] = Sprite.loadSprite("RunLeft__00" + i, "knight");
				attack[i] = Sprite.loadSprite("Attack__00" + i, "knight");
				attackLeft[i] = Sprite.loadSprite("AttackLeft__00" + i, "knight");
				// jumpAttack[i] = Sprite.loadSprite("Jump_Attack__00" + i,
				// "knight");
				// jumpAttackLeft[i] = Sprite.loadSprite("Jump_AttackLeft__00" +
				// i, "knight");
				shoot[i] = Sprite.loadSprite("Throw__00" + i, "knight");
				shootLeft[i] = Sprite.loadSprite("ThrowLeft__00" + i, "knight");
			}
			if (character.equals("robot"))
			{
				idle[i] = Sprite.loadSprite("Idle__00" + i, "robot");
				idleLeft[i] = Sprite.loadSprite("IdleLeft__00" + i, "robot");
				jump[i] = Sprite.loadSprite("Jump__00" + i, "robot");
				jumpLeft[i] = Sprite.loadSprite("JumpLeft__00" + i, "robot");
				if (i < 8)
				{
					run[i] = Sprite.loadSprite("Run__00" + i, "robot");
					runLeft[i] = Sprite.loadSprite("RunLeft__00" + i, "robot");
					attack[i] = Sprite.loadSprite("Attack__00" + i, "robot");
					attackLeft[i] = Sprite.loadSprite("AttackLeft__00" + i, "robot");
				}
				if (i < 5)
				{
					shoot[i] = Sprite.loadSprite("Shoot__00" + i, "robot");
					shootLeft[i] = Sprite.loadSprite("ShootLeft__00" + i, "robot");
				}
			}
		}
		for (int i = 0; i < 11; i++)
		{
			dDeath[i] = Sprite.loadSprite("img/" + i + ".png", "death");
		}
		dDieing = new Animation(dDeath, 1);
		standing = new Animation(idle, 6);
		standingLeft = new Animation(idleLeft, 6);
		jumping = new Animation(jump, 5);
		jumpingLeft = new Animation(jumpLeft, 5);
		running = new Animation(run, 6);
		runningLeft = new Animation(runLeft, 6);
		attacking = new Animation(attack, 3);
		attackingLeft = new Animation(attackLeft, 3);
		jumpAttacking = new Animation(jumpAttack, 10);
		jumpAttackingLeft = new Animation(jumpAttackLeft, 10);
		if (character.equals("robot"))
		{
			shooting = new Animation(shoot, 6);
			shootingLeft = new Animation(shootLeft, 6);
		}
		else
		{
			shooting = new Animation(shoot, 2);
			shootingLeft = new Animation(shootLeft, 2);
		}
		animation = standing;
		try
		{
			nameFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/Audiowide-Regular.ttf")).deriveFont(22f);
		}
		catch (IOException | FontFormatException e)
		{
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see game.GameObject#tick()
	 */
	@Override
	public void tick()
	{
		animation.update();
		damageTaken = this.getDamage();
		if (this.getShieldCharge() < 0)
		{
			this.setShieldCharge(0);
		}
		if (this.getShieldCharge() < 100 && !oneSecond && !this.getShielding())
		{
			timerForShieldGain = System.currentTimeMillis();
			oneSecond = true;
		}
		if (System.currentTimeMillis() - timerForShieldGain > 1000 && oneSecond && !this.getShielding())
		{
			oneSecond = false;
			this.setShieldCharge(this.getShieldCharge() + 5);
		}
		if (this.getShielding() && this.getShieldCharge() > 0 && !halfSecond)
		{
			timerForShieldLoss = System.currentTimeMillis();
			halfSecond = true;
		}
		if (System.currentTimeMillis() - timerForShieldLoss > 500 && halfSecond && this.getShielding())
		{
			halfSecond = false;
			this.setShieldCharge(this.getShieldCharge() - 1);
		}
		if (this.isDead() && this.getStock() > 0)
		{
			if (System.currentTimeMillis() - timeOfDeath > 5000)
			{
				this.setInAction(false);
				this.setShooting(false);
				this.setJumping(false);
				this.setAttacking(false);
				this.setRunning(false);
				this.setX((Game.width / 2) - (Game.scale(94) / 2));
				this.setY(Game.height - Game.scale(600) - Game.scale(94) - Game.scale(130));
				this.setSpeedX(0);
				this.setSpeedY(0);
				this.setDeath(false);
			}
		}
		x += speedX;
		y += speedY;
		if (speedY < Game.scale(14))
		{
			acceleration = acceleration * 1.02;
			if (acceleration < 1.0)
			{
				store_acceleration = store_acceleration + acceleration;
			}
			else
			{
				speedY = (int) (speedY + acceleration);
			}
			if (store_acceleration > 1.0)
			{
				speedY = (int) (speedY + store_acceleration);
				store_acceleration = 0;
			}
		}
		x = Game.clamp(x, Game.scale(-900), Game.width + Game.scale(900));
		y = Game.clamp(y, Game.scale(-900), Game.height + Game.scale(900));
		// pits
		if (x < ((Game.width / 2) - Game.scale(640) - Game.scale(90))
				&& y > Game.height - Game.scale(100) - Game.scale(118))
		{
			x = Game.clamp(x, Game.scale(-900), ((Game.width / 2) - Game.scale(640) - Game.scale(101)));
		}
		if (x > ((Game.width / 2) + Game.scale(640)) && y > Game.height - Game.scale(100) - Game.scale(118))
		{
			x = Game.clamp(x, ((Game.width / 2) + Game.scale(655)), ((Game.width) + Game.scale(900)));
		}
		// floor
		else if (x > ((Game.width / 2) - Game.scale(640) - Game.scale(90)) && x < ((Game.width / 2) + Game.scale(640)))
		{
			y = Game.clamp(y, Game.scale(-900), Game.height - Game.scale(100) - Game.scale(118));
			if (y == Game.height - Game.scale(100) - Game.scale(118))
				acceleration = gravity_constant;
		}
		// platforms
		// left
		if (y < Game.height - Game.scale(350) - Game.scale(94)
				&& (x > ((Game.width / 2) - Game.scale(640)) + Game.scale(74) - Game.scale(94)
						&& x < ((Game.width / 2) - Game.scale(640)) + Game.scale(74) + Game.scale(384)))
		{
			y = Game.clamp(y, Game.scale(-900), Game.height - Game.scale(350) - Game.scale(118));
			if (y == Game.height - Game.scale(350) - Game.scale(118))
				acceleration = gravity_constant;
		}
		// right
		if (y < Game.height - Game.scale(350) - Game.scale(94)
				&& (x > ((Game.width / 2) + Game.scale(640) - Game.scale(384)) - Game.scale(74) - Game.scale(94)
						&& x < ((Game.width / 2) + Game.scale(640)) - Game.scale(74)))
		{
			y = Game.clamp(y, Game.scale(-900), Game.height - Game.scale(350) - Game.scale(118));
			if (y == Game.height - Game.scale(350) - Game.scale(118))
				acceleration = gravity_constant;
		}
		// mid
		if (y < Game.height - Game.scale(694)
				&& (x > (((Game.width / 2) - Game.scale(384)) + Game.scale(192)) - Game.scale(94)
						&& x < ((Game.width / 2)) + Game.scale(192)))
		{
			y = Game.clamp(y, Game.scale(-900), Game.height - Game.scale(600) - Game.scale(118));
			if (y == Game.height - Game.scale(600) - Game.scale(118))
				acceleration = gravity_constant;
		}
//		 if (this.getRunning() && this.getJumping() == false && speedX > 0)
//		 {
//		 speedX -= Game.scale(1);
//		 }
//		 if (this.getRunning() && this.getJumping() == false && speedX < 0)
//		 {
//		 speedX += Game.scale(1);
//		 }
		detectCollision(this);
		if (this.x < Game.scale(-899) && this.isDead() == false)
		{
			int currentStock = this.getStock();
			this.setStock(currentStock - 1);
			this.setDeath(true);
			this.setDamage(0);
			this.damageTaken = 0;
			timeOfDeath = System.currentTimeMillis();
			for (int i = 0; i < handler.object.size(); i++)
			{
				GameObject tempObject = handler.object.get(i);
				if (tempObject.getName() == this.getKilledBy())
				{
					tempObject.setKills(tempObject.getKills() + 1);
				}
			}
		}
		if (this.y < Game.scale(-899) && this.isDead() == false)
		{
			int currentStock = this.getStock();
			this.setStock(currentStock - 1);
			this.setDeath(true);
			this.setDamage(0);
			this.damageTaken = 0;
			timeOfDeath = System.currentTimeMillis();
			for (int i = 0; i < handler.object.size(); i++)
			{
				GameObject tempObject = handler.object.get(i);
				if (tempObject.getName() == this.getKilledBy())
				{
					tempObject.setKills(tempObject.getKills() + 1);
				}
			}
		}
		if (this.x > Game.width + Game.scale(899) && this.isDead() == false)
		{
			int currentStock = this.getStock();
			this.setStock(currentStock - 1);
			this.setDeath(true);
			this.setDamage(0);
			this.damageTaken = 0;
			timeOfDeath = System.currentTimeMillis();
			for (int i = 0; i < handler.object.size(); i++)
			{
				GameObject tempObject = handler.object.get(i);
				if (tempObject.getName() == this.getKilledBy())
				{
					tempObject.setKills(tempObject.getKills() + 1);
				}
			}
		}
		if (this.y > Game.height + Game.scale(899) && this.isDead() == false)
		{
			int currentStock = this.getStock();
			this.setStock(currentStock - 1);
			this.setDeath(true);
			this.setDamage(0);
			this.damageTaken = 0;
			timeOfDeath = System.currentTimeMillis();
			for (int i = 0; i < handler.object.size(); i++)
			{
				GameObject tempObject = handler.object.get(i);
				if (tempObject.getName() == this.getKilledBy())
				{
					tempObject.setKills(tempObject.getKills() + 1);
				}
			}
		}
	}

	@Override
	public void render(Graphics g)
	{
		if (Game.allPlayersReady)
		{
			hasLanded();
			double centerX = 0.0;
			double centerY = 0.0;
			for (int i = 0; i < handler.object.size(); i++)
			{
				GameObject tempObject = handler.object.get(i);
				if (tempObject.getID() == this.id)
				{
					Rectangle attackSwing = new Rectangle();
					if (this.id == 1)
					{
						p1.setBounds(x, y, Game.scale(94), Game.scale(118));
						centerX = p1.getCenterX();
						centerY = p1.getCenterY();
						String dam = this.getDamage() + "";
						String lives = this.getStock() + "";
						g.setFont(nameFont);
						Graphics2D g2 = (Graphics2D) g;
						g2.setColor(new Color(255, 255, 255));
						g2.drawString("x" + lives, (Game.width - Game.scale(160)) - Game.scale(385), Game.scale(220));
						if (this.getDamage() > 200)
						{
							g2.setColor(new Color(252, 70, 0));
						}
						else if (this.getDamage() > 100)
						{
							g2.setColor(new Color(249, 166, 2));
						}
						g2.drawString(dam + "%", (Game.width - Game.scale(160)) - Game.scale(385), Game.scale(180));
					}
					if (this.id == 2)
					{
						p2.setBounds(x, y, Game.scale(94), Game.scale(118));
						centerX = p2.getCenterX();
						centerY = p2.getCenterY();
						String dam = this.getDamage() + "";
						String lives = this.getStock() + "";
						g.setFont(nameFont);
						Graphics2D g2 = (Graphics2D) g;
						g2.setColor(new Color(255, 255, 255));
						g2.drawString("x" + lives, (Game.width - Game.scale(80)) - Game.scale(260), Game.scale(220));
						if (this.getDamage() > 200)
						{
							g2.setColor(new Color(252, 70, 0));
						}
						else if (this.getDamage() > 100)
						{
							g2.setColor(new Color(249, 166, 2));
						}
						g2.drawString(dam + "%", (Game.width - Game.scale(80)) - Game.scale(260), Game.scale(180));
					}
					if (this.id == 3)
					{
						p3.setBounds(x, y, Game.scale(94), Game.scale(118));
						centerX = p3.getCenterX();
						centerY = p3.getCenterY();
						String dam = this.getDamage() + "";
						String lives = this.getStock() + "";
						g.setFont(nameFont);
						Graphics2D g2 = (Graphics2D) g;
						g2.setColor(new Color(255, 255, 255));
						g2.drawString("x" + lives, Game.scale(65) + Game.scale(260) + Game.scale(200), Game.scale(220));
						if (this.getDamage() / 100 > 200)
						{
							g2.setColor(new Color(252, 70, 0));
						}
						else if (this.getDamage() > 100)
						{
							g2.setColor(new Color(249, 166, 2));
						}
						g2.drawString(dam + "%", Game.scale(65) + Game.scale(260) + Game.scale(200), Game.scale(180));
					}
					if (this.id == 4)
					{
						p4.setBounds(x, y, Game.scale(94), Game.scale(118));
						centerX = p4.getCenterX();
						centerY = p4.getCenterY();
						String dam = this.getDamage() + "";
						String lives = this.getStock() + "";
						g.setFont(nameFont);
						Graphics2D g2 = (Graphics2D) g;
						g2.setColor(new Color(255, 255, 255));
						g2.drawString("x" + lives, Game.scale(65) + Game.scale(260), Game.scale(220));
						if (this.getDamage() > 200)
						{
							g2.setColor(new Color(252, 70, 0));
						}
						else if (this.getDamage() > 100)
						{
							g2.setColor(new Color(249, 166, 2));
						}
						g2.drawString(dam + "%", Game.scale(65) + Game.scale(260), Game.scale(180));
					}
					attackSwing.setLocation((int) centerX, (int) centerY);
					int alpha = 0; // 100% transparent
					Color invis = new Color(255, 255, 255, alpha);
					g.setColor(invis);
					int imageLocationX = x;
					int imageLocationY = y;
					if (character.equals("robot"))
					{
						imageLocationX = x - Game.scale(45);
						imageLocationY = y - Game.scale(15);
					}
					if (character.equals("knight"))
					{
						imageLocationX = x - Game.scale(25);
						imageLocationY = y - Game.scale(23);
					}
					if (this.getFacingLeft())
					{
						if (this.getJumping())
						{
							animation = jumpingLeft;
							animation.start();
							g.drawImage(animation.getSprite(), imageLocationX, imageLocationY, null);
						}
						else if (this.getRunning())
						{
							animation = runningLeft;
							animation.start();
							g.drawImage(animation.getSprite(), imageLocationX, imageLocationY, null);
						}
						else if (this.getAttacking())
						{
							g.fillRect((int) centerX - Game.scale(100), (int) centerY, Game.scale(100), Game.scale(30));
							animation = attackingLeft;
							animation.start();
							if (character.equals("robot") || character.equals("knight"))
							{
								g.drawImage(animation.getSprite(), imageLocationX, imageLocationY, null);
							}
							else if (character.equals("ninjaGirl"))
							{
								g.drawImage(animation.getSprite(), imageLocationX - Game.scale(52),
										imageLocationY - Game.scale(10), null);
							}
							else
							{
								g.drawImage(animation.getSprite(), imageLocationX - Game.scale(52), imageLocationY,
										null);
							}
							if (animation.getCurrentFrame() == animation.getEndFrame() / 2 && canAttack)
							{
								attackSwing.setBounds((int) centerX - Game.scale(100), (int) centerY, Game.scale(100),
										Game.scale(30));
								detectCollision(attackSwing);
								canAttack = false;
							}
							if (animation.getCurrentFrame() == animation.getEndFrame() - 2)
							{
								canAttack = true;
								this.setInAction(false);
								animation.reset();
								this.setAttacking(false);
							}
						}
						else if (this.getShooting())
						{
							animation = shootingLeft;
							animation.start();
							g.drawImage(animation.getSprite(), imageLocationX, imageLocationY, null);
							if (animation.getCurrentFrame() == animation.getEndFrame() / 2 && canThrow)
							{
								handler.addObject(new Projectile((int) centerX - Game.scale(100), (int) centerY,
										10 + id, 1, character, handler));
								canThrow = false;
							}
							if (animation.getCurrentFrame() == animation.getEndFrame() - 2)
							{
								canThrow = true;
								this.setInAction(false);
								animation.reset();
								this.setShooting(false);
							}
						}
						else
						{
							animation = attackingLeft;
							animation.reset();
							animation = standingLeft;
							animation.start();
							g.drawImage(animation.getSprite(), imageLocationX, imageLocationY, null);
						}
					}
					else
					{
						if (this.getJumping())
						{
							animation = jumping;
							animation.start();
							g.drawImage(animation.getSprite(), imageLocationX, imageLocationY, null);
						}
						else if (this.getRunning())
						{
							animation = running;
							animation.start();
							g.drawImage(animation.getSprite(), imageLocationX, imageLocationY, null);
						}
						else if (this.getAttacking())
						{
							animation = attacking;
							animation.start();
							if (character.equals("robot") || character.equals("knight"))
							{
								g.drawImage(animation.getSprite(), imageLocationX, imageLocationY, null);
							}
							else if (character.equals("ninjaGirl"))
							{
								g.drawImage(animation.getSprite(), imageLocationX - Game.scale(25),
										imageLocationY - Game.scale(10), null);
							}
							else
							{
								g.drawImage(animation.getSprite(), imageLocationX - Game.scale(25), imageLocationY,
										null);
							}
							if (animation.getCurrentFrame() == animation.getEndFrame() / 2 && canAttack)
							{
								attackSwing.setBounds((int) centerX, (int) centerY, Game.scale(100), Game.scale(30));
								detectCollision(attackSwing);
								canAttack = false;
							}
							if (animation.getCurrentFrame() == animation.getEndFrame() - 2)
							{
								canAttack = true;
								this.setInAction(false);
								animation.reset();
								this.setAttacking(false);
							}
						}
						else if (this.getShooting())
						{
							animation = shooting;
							animation.start();
							g.drawImage(animation.getSprite(), imageLocationX, imageLocationY, null);
							if (animation.getCurrentFrame() == animation.getEndFrame() / 2 && canThrow)
							{
								handler.addObject(
										new Projectile((int) centerX, (int) centerY, 10 + id, 0, character, handler));
								canThrow = false;
							}
							if (animation.getCurrentFrame() == animation.getEndFrame() - 2)
							{
								canThrow = true;
								this.setInAction(false);
								animation.reset();
								this.setShooting(false);
							}
						}
						else
						{
							animation = attacking;
							animation.reset();
							animation = standing;
							animation.start();
							g.drawImage(animation.getSprite(), imageLocationX, imageLocationY, null);
						}
					}
					if (this.getShielding())
					{
						drawShield(g, centerX, centerY);
					}

					// g.setColor(playerColor);
					// g.fillRect(x, y, Game.scale(94), Game.scale(118));
				}
			}
			// Drawing the username above player
			int nameWidth = g.getFontMetrics().stringWidth(name);
			Color randomColor = new Color(0, 0, 0, 50);
			g.setColor(randomColor);
			g.fillRect((int) centerX - nameWidth / 2, (int) centerY - Game.scale(105), nameWidth, Game.scale(30));
			Graphics2D g2 = (Graphics2D) g;
			randomColor = new Color(255, 255, 255, 90);
			g.setColor(randomColor);
			g2.drawString(name, (int) centerX - nameWidth / 2, (int) centerY - Game.scale(80));
			int[] xPoints = { (int) centerX, (int) centerX - Game.scale(10), (int) centerX + Game.scale(10) };
			int[] yPoints = { (int) centerY - Game.scale(60), (int) centerY - Game.scale(72),
					(int) centerY - Game.scale(72) };
			int nPoints = 3;
			g.setColor(playerColor);
			g2.drawPolygon(xPoints, yPoints, nPoints);
			g2.fillPolygon(xPoints, yPoints, nPoints);
			if (!this.isDead())
			{
				if (x < Game.scale(-94))
				{
					drawOutOfBoundsIndicator(g, 0, y);
				}
				if (x > Game.width + Game.scale(75))
				{
					drawOutOfBoundsIndicator(g, Game.width - Game.scale(105), y);
				}
				if (y < Game.scale(-118))
				{
					drawOutOfBoundsIndicator(g, x, 0);
				}
				if (y > Game.height + Game.scale(20))
				{
					drawOutOfBoundsIndicator(g, x, Game.height - Game.scale(130));
				}
			}
			if (x <= Game.scale(-899))
			{
				if (System.currentTimeMillis() - timeOfDeath < 800)
				{
					if (playOnce == 0)
					{
						AudioInputStream audioInputStream;
						try
						{
							audioInputStream = AudioSystem.getAudioInputStream(new File("sounds/death" + ".wav"));
							Clip clip = AudioSystem.getClip();
							clip.open(audioInputStream);
							FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
							gainControl.setValue(-30.0f); // Reduce volume by 30
															// decibels.
							clip.start();
							playOnce = 1;
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
					animation = dDieing;
					animation.start();
					g.drawImage(animation.getSprite(), 0, Game.scale(Game.height) - Game.scale(40), null);
				}
			}
			if (y <= Game.scale(-899))
			{
				if (System.currentTimeMillis() - timeOfDeath < 800)
				{
					if (playOnce == 0)
					{
						AudioInputStream audioInputStream;
						try
						{
							audioInputStream = AudioSystem.getAudioInputStream(new File("sounds/death" + ".wav"));
							Clip clip = AudioSystem.getClip();
							clip.open(audioInputStream);
							FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
							gainControl.setValue(-30.0f); // Reduce volume by 30
															// decibels.
							clip.start();
							playOnce = 1;
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
					animation = dDieing;
					animation.start();
					g.drawImage(animation.getSprite(), x - Game.scale(118), 0, null);
				}
			}
			if (x >= Game.width + Game.scale(899))
			{
				if (System.currentTimeMillis() - timeOfDeath < 800)
				{
					if (playOnce == 0)
					{
						AudioInputStream audioInputStream;
						try
						{
							audioInputStream = AudioSystem.getAudioInputStream(new File("sounds/death" + ".wav"));
							Clip clip = AudioSystem.getClip();
							clip.open(audioInputStream);
							FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
							gainControl.setValue(-30.0f); // Reduce volume by 30
															// decibels.
							clip.start();
							playOnce = 1;
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
					animation = dDieing;
					animation.start();
					g.drawImage(animation.getSprite(), x - Game.scale(118), 0, null);
				}
			}
			if (y >= Game.height + Game.scale(899))
			{
				if (System.currentTimeMillis() - timeOfDeath < 800)
				{
					if (playOnce == 0)
					{
						AudioInputStream audioInputStream;
						try
						{
							audioInputStream = AudioSystem.getAudioInputStream(new File("sounds/death" + ".wav"));
							Clip clip = AudioSystem.getClip();
							clip.open(audioInputStream);
							FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
							gainControl.setValue(-30.0f); // Reduce volume by 30
															// decibels.
							clip.start();
							playOnce = 1;
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
					animation = dDieing;
					animation.start();
					g.drawImage(animation.getSprite(), x - Game.scale(118), 0, null);
				}
			}
		}
	}

	private void detectCollision(GameObject player)
	{
		for (int i = 0; i < handler.object.size(); i++)
		{
			GameObject tempObject = handler.object.get(i);
			if (player.equals(null) || tempObject.equals(null))
			{
			}
			else
			{
				if (tempObject.getID() != this.id && tempObject.getID() < 10 && tempObject.getID() > 0)
				{
					if (player.getHitbox().intersects(tempObject.getHitbox()))
					{
						if (player.getHitbox().intersects(tempObject.getHitbox()))
						{
							doCollision(tempObject);
						}
					}
				}
			}
		}
	}

	private void detectCollision(Rectangle attack)
	{
		for (int i = 0; i < handler.object.size(); i++)
		{
			GameObject tempObject = handler.object.get(i);
			if (tempObject.getID() != this.id)
			{
				if (attack.intersects(tempObject.getHitbox()) && tempObject.getID() < 10 && tempObject.getID() > 0)
				{
					doAttackCollision(tempObject);
				}
			}
		}
	}

	private void doCollision(GameObject player)
	{
		// Collision is to the left of us
		if (player.getX() < x)
		{
			player.setX(player.getX() - Game.scale(1));
		}
		// Collision is to the right of us
		else
		{
			player.setX(player.getX() + Game.scale(1));
		}
	}

	private void doAttackCollision(GameObject player)
	{
		if (player.getShielding() && player.getShieldCharge() > 0)
		{
			player.setShieldCharge(Game.scale(player.getShieldCharge()) - Game.scale(20));
		}
		else
		{
			player.setKilledBy(name);
			player.setDamage(player.getDamage() + 20);
			damageTaken = player.getDamage();
			// Collision is to the left of us
			if (player.getX() < x)
			{
				player.setSpeedX(Game.scale((-1 * player.getDamage()) / 10));
			}
			// Collision is to the right of us
			else
			{
				player.setSpeedX(Game.scale(+1 * player.getDamage()) / 10);
			}
			if (player.getCharacter().equals("ninjaGirl"))
			{
				if (playOnce == 0)
				{
					playOnce = 1;
					Random rand = new Random();
					try
					{
						AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
								new File("sounds/gruntgirl" + (rand.nextInt((3 - 1) + 1) + 1) + ".wav"));
						Clip clip = AudioSystem.getClip();
						clip.open(audioInputStream);
						FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
						gainControl.setValue(-30.0f); // Reduce volume by 30
														// decibels.
						clip.start();
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void hasLanded()
	{
		Rectangle landbox = new Rectangle(x, y + Game.scale(118), Game.scale(94), Game.scale(2));
		for (int i = 0; i < handler.object.size(); i++)
		{
			GameObject tempObject = handler.object.get(i);
			if (tempObject.getID() == this.id)
			{
				if ((landbox.intersects(Game.ground) || landbox.intersects(Game.platLeft)
						|| landbox.intersects(Game.platMid) || landbox.intersects(Game.platRight))
						&& tempObject.getJumping())
				{
					tempObject.setDoubleJumped(false);
					tempObject.setJumped(0);
					tempObject.setJumping(false);
				}
				if (!((landbox.intersects(Game.ground) || landbox.intersects(Game.platLeft)
						|| landbox.intersects(Game.platMid) || landbox.intersects(Game.platRight))))
				{
					tempObject.setJumping(true);
				}
			}
		}
	}

	private void drawOutOfBoundsIndicator(Graphics graphics, int x, int y)
	{
		graphics.setColor(playerColor);
		graphics.fillOval(x, y, Game.scale(100), Game.scale(100));
	}

	private void drawShield(Graphics graphics, double x, double y)
	{
		int halfShieldSize = Game.scale(this.getShieldCharge()) / 2;
		boolean shieldPop = false;
		long shieldPopTime = 0;// = System.currentTimeMillis();
		graphics.setColor(playerColor);
		if (this.getShieldCharge() <= 0)
		{
			shieldPop = true;
			this.setInAction(true);
			shieldPopTime = System.currentTimeMillis();
		}
		if (shieldPop == true && System.currentTimeMillis() - shieldPopTime > 1500)
		{
			shieldPop = false;
			this.setInAction(false);
		}
		if (!shieldPop)
			graphics.fillOval((int) x - halfShieldSize, (int) y - halfShieldSize, Game.scale(this.getShieldCharge()),
					Game.scale(this.getShieldCharge()));
	}

	public String getName()
	{
		return name;
	}
}
