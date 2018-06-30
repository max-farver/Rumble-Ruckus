package game;

import java.awt.Graphics;
import java.util.LinkedList;

/**
 * This is the Class that is used to keep track of all GameObjects within the Game. It will also call each 
 * GameObjects tick and render methods.
 * @author Clayton Nida
 *
 */
public class Handler
{
	LinkedList<GameObject> object = new LinkedList<GameObject>();

	/**
	 * Will loop through our list of GameObjects and call tick() on each one.
	 */
	public void tick()
	{
		for (int i = 0; i < object.size(); i++)
		{
			GameObject tempObject = object.get(i);
			tempObject.tick();
		}
	}

	/**
	 * Will loop through our list of GameObjects and call tick() on each one.
	 * @param g The given Graphics object which will be used to draw our images.
	 */
	public void render(Graphics g)
	{
		for (int i = 0; i < object.size(); i++)
		{
			GameObject tempObject = object.get(i);
			if(tempObject.getStock() > 0 || !tempObject.isDead())
			tempObject.render(g);

		}
	}

	/**
	 * This will add a GameObject to our list of GameObjects.
	 * @param object GameObject to be added.
	 */
	public void addObject(GameObject object)
	{
		this.object.add(object);
	}

	/**
	 * his will remove a GameObject to our list of GameObjects.
	 * @param object GameObject to be removed.
	 */
	public void removeObject(GameObject object)
	{
		this.object.remove(object);
	}
	
	/**
	 * @return Returns the number of GameObjects in our list.
	 */
	public int size()
	{
		return object.size();
	}
}
