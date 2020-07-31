# snake

A __snake__ game, with multiple ai algorithms.  

## Table of Contents
1. [About the game](#about-the-game)  
2. [Implemented AI](#implemented-ai)
	2.1 [Hamiltonian cycle](#hamiltonian-cycle) 
3. [Purpose of this project](#purpose-of-this-project) 
4. [Project structure](#project-structure)  
5. [How to get it](#how-to-get-it)  
	5.1 [How to import it to Intellij IDE](#how-to-import-it-to-intellij-ide)   
	5.2 [How to import it to Eclipse IDE](#how-to-import-it-to-eclipse-ide)   
6. [How to run it](#how-to-run-it)  
	6.1 [Within the IDE](#within-the-ide)  
	6.2 [From a Jar file](#from-a-jar-file)  
		6.2.1 [Build Jar in Intellij IDE](#build-jar-in-intellij-ide)    
		6.2.2 [Build Jar in Eclipse IDE](#build-jar-in-eclipse-ide)  
		6.2.3 [Execute Jar](#execute-jar)  
		
## About the game
![screenshot of snake](https://github.com/lpapailiou/snake/blob/master/src/main/resources/snake.png)

The game offers a manual mode, which is controlled by the keyboard.
* __arrow left__, __arrow right__, __arrow up__, __arrow down__: will switch to this direction for the next snake move(s)
  
Every time the level increases, speed will also increase a little. 
The shapes are generated on the fly, so the blocks will 'cluster'. This means, that it is more likely that compact 
shapes appear.  
If you want to get the game more annoying, you can add more blocks in the ``Settings`` class (which is located in the ``util`` 
package). There, also the board size (default 17x10 blocks) may be adjusted.  
  
## Implemented AI
  
### Hamiltonian cycle
One of the most known algorithms to solve snake with an ai is to implement a hamiltonian cycle as base path. A hamiltonian 
cycle is basically a circular path, with runs through every node (here 'cell') of a graph (here 'game panel').  
This algorithm only works though, if the game panel has an even number of cells (e.g. a 25x25 panel will fail as one cell will 
never be included in the path).
  
Example of a such base path: 
![screenshot of hamiltonian cycle](https://github.com/lpapailiou/snake/blob/master/src/main/resources/img/screenshot_hamiltonianPath.png)
  
The snake can strictly follow this path. As a result, it will never run into its own body or a wall.  
![screenshot of hamiltonian cycle](https://github.com/lpapailiou/snake/blob/master/src/main/resources/img/screenshot_hamiltonianPath_running.png)
  
If the snake is small, this may result in unreasonable long paths, thus an inefficient timing. To solve this, path sections can 
be skipped in earlier stages. The snake will remain basically on the base path, which will avoid that a whole new path 
has to be calculated after a goodie is reached.    
![screenshot of hamiltonian cycle](https://github.com/lpapailiou/snake/blob/master/src/main/resources/img/screenshot_hamiltonianPath_shortcut.png)
  
## Purpose of this project
This project ist mainly for playing around with Java. 

## Project structure
  
* ``ai``                    here, all ai related code is in
* ``application``         this package contains the main method (in ``Driver.java``), and gui related code
* ``game``                 here most part of the game logic is in
* ``util``                 helper classes

## How to get it

Clone the repository with:

    git clone https://github.com/lpapailiou/snake your-target-path

Originally, the project was developed with the Intellij IDE. It also runs with Eclipse.

### How to import it to Intellij IDE
1. Go to ``File > New``
2. Pick ``Maven > Project from Existing Sources...``
3. Now, navigate to the directory you cloned it to
4. Select the ``pom.xml`` file and click ``OK``
5. The project will be opened and build

### How to import it to Eclipse IDE
1. Go to ``File > Import``
2. Pick ``Maven > Existing Maven Project``
3. Now, navigate to the directory you cloned it to
4. Pick the root directory ``snake`` and click ``Finish``
5. The project will be opened and build

## How to run it

### Within the IDE
You can directly run it within the IDE.

In case you experience weird UI behavior, it may be a DPI scaling issue known to occur with Windows 10 notebooks.
To fix it, do following steps:
1. Find the ``java.exe`` the game is running with (check Task Manager)
2. Rightclick on the ``java.exe`` and go to ``Properties``
3. Open the ``Compability`` tab
4. Check ``Override high DPI scaling behavior``
5. Choose ``System`` for ``Scaling performed by:``
6. Run the game again

### From a Jar file
You can download the Jar file directly from the [latest release](https://github.com/lpapailiou/snake/releases/latest). Alternatively, you can build it yourself.

#### Build Jar in Intellij IDE 
1. Go to ``File > Project Structure...``
2. Go to the ``Artifacts`` tab and add a new ``Jar > From module with dependencies`` entry
3. Select the main class ``Driver`` (here, the main class is in)
4. Click ``Ok`` twice
5. Go to ``Build > Build Artifacts...``
6. Select ``Build``
7. The Jar file is now added to the ``target`` folder within the project structure

#### Build Jar in Eclipse IDE
1. Right click on the project
2. Choose ``Export``
3. Go to ``Java > Runnable JAR file``
4. Click ``Next``
5. Launch configuration: choose ``Driver`` (here, the main class is in)
6. Export destination: the place you want to save the Jar
7. Choose ``Extract required libraries into generated JAR``
8. Click ``Finish`` to start the Jar generation

#### Execute Jar
Double click on the Jar file directly. 
If nothing happens, you might need to add Java to your PATH variable.

Alternatively, you can start the Jar file from the console with:

    java -jar snake.jar
    
Please make sure you execute it from the correct directory. The naming of the Jar file may vary.