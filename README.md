Rumble Ruckus
____________________________________________________________________________________________________

-Rumble Ruckus is an online multiplayer fighting game.
____________________________________________________________________________________________________

Getting Started
____________________________________________________________________________________________________

-First, you will need to pull all code, and be sure that all dependencies are present.
-Next, SSH to our server.
-/home/canida2/workspace/RumbleRuckus/src/game
-Once in the game directory run this command 
	-$ javac GameServer.java
		-this will compile the code.
-Now the server is runnable. Run this command to start the server
	-$ java -cp . GameServer
-The server is now running, and waiting for players to connect.
-The server is semi-stable right now, and will not know how many players to accept.
Each player is assigned a number and this will be there player number. If it is 1
they control blue, and 2 is red; Also meaning if you launch another client, it will be assigned 3, which is currently
nobody, so you will need to restart the server. To do this use Ctrl+C and relaunch with the java -cp command.
____________________________________________________________________________________________________

Prerequisites
____________________________________________________________________________________________________

-You will need java 1.0.4 or higher.
-Java JDK 1.6 or higher on server.
-Currently the game is only run on a 1600x900 window. Higher resolution is neccesary.
____________________________________________________________________________________________________

Built With
____________________________________________________________________________________________________

-JXinput - controller API
-Maven - Dependency Management
-Java - Engine/Server Code
-Redhat - Server Host
more to come...
____________________________________________________________________________________________________

Versioning
____________________________________________________________________________________________________

-Version 0.1.4
____________________________________________________________________________________________________

Authors
____________________________________________________________________________________________________

-Clayton Nida
See also the list of contributors who participated in this project.
____________________________________________________________________________________________________

