package game;

import java.io.IOException;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinDef.HWND;

/**
 * This class will launch a chrome window in application mode that will be directed to the login page. 
 * @author Clayton Nida
 * @author Ryan Hilby
 *
 */
public class Login
{
	public volatile boolean openOrClosed = false;
	public volatile boolean closed = false;
	public volatile boolean isUserLoggedIn = false;

	/**
	 * Launches a chrome window in application mode that will be directed to the login page.
	 * @param key The key of the user instance.
	 */
	public Login(int key)
	{
		try
		{
			// String startCommand = "cmd.exe /c start chrome
			// --app=http://127.0.0.1:8000/store/" + key;
			String startCommand = "cmd.exe /c start chrome --app=http://proj-309-tg-3.cs.iastate.edu/store/" + key;
			Runtime.getRuntime().exec(startCommand);
			// Runtime.getRuntime().exec("cmd.exe /c start chrome
			// --app=http://proj-309-tg-3.cs.iastate.edu" );
			// Runtime.getRuntime().exec("cmd.exe /c start chrome
			// --app=http://127.0.0.1:8000/" ); when making local changes only
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @return Returns whether or not the login browser is still open.
	 */
	public boolean isBrowserOpen()
	{
		HWND hwnd = User32.INSTANCE.FindWindow(null, "Rumble Ruckus");
		if (hwnd == null)
		{
			System.out.println("Login is not running");
			openOrClosed = false;
		}
		else
		{
			openOrClosed = true;
			System.out.println("LOGIN is running");
		}
		return openOrClosed;
	}

	/**
	 * @return Returns whether or not the login browser has been closed.
	 */
	public boolean isBrowserClosed()
	{
		HWND hwnd = User32.INSTANCE.FindWindow(null, "Rumble Ruckus");
		if (hwnd == null)
		{
			try
			{
				Thread.sleep(1500);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			hwnd = User32.INSTANCE.FindWindow(null, "Rumble Ruckus");
			if (hwnd == null)
			{
				// System.out.println("Login is not running");
				closed = true;
			}
			else
			{
				closed = false;
			}
		}
		else
		{
			closed = false;
			// System.out.println("LOGIN is running");
		}
		return closed;
	}


	/**
	 * @return Returns whether or not a User is logged in.
	 */


	public boolean isGameLaunched()

	{
		HWND hwnd = User32.INSTANCE.FindWindow(null, "Launch");
		if (hwnd == null)
		{
			// System.out.println("User is not logged in");
			isUserLoggedIn = false;
		}
		else
		{
			isUserLoggedIn = true;
			//User32.INSTANCE.PostMessage(hwnd, WinUser.WM_CLOSE, null, null);
			System.out.println("User is logged in.  Launch game.");
		}
		return isUserLoggedIn;
	}
	
	public void closeLogin() {
		HWND hwnd = User32.INSTANCE.FindWindow(null, "Launch");
		if (hwnd == null)
		{
			
			
			//System.exit(0);
		}
		else{
			User32.INSTANCE.PostMessage(hwnd, WinUser.WM_CLOSE, null, null);
			System.out.println("Login window successfully closed.");
		}
	}

}
