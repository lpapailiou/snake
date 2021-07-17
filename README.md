# WIP - snake

A __snake__ game, with multiple ai algorithms.  
Currently, this project is used to test the [neuralnetwork library](https://github.com/lpapailiou/neuralnetwork). 

## Table of Contents
1. [About the game](#about-the-game)  
2. [Implemented AI](#implemented-ai)  
	2.1 [Hamiltonian cycle](#hamiltonian-cycle)   
	2.2 [A* algorithm](#a-algorithm) 
3. [Purpose of this project](#purpose-of-this-project) 
4. [Project structure](#project-structure)  
5. [How to get it](#how-to-get-it)  
		
## About the game
![screenshot of snake](https://github.com/lpapailiou/snake/blob/master/src/main/resources/snake.png)

The game offers a manual mode, which is controlled by the keyboard.
* __arrow left__, __arrow right__, __arrow up__, __arrow down__: will switch to this direction for the next snake move(s).  
Alternatively, a bot can overtake the moves. Currently, the gui does not allow choosing between different modes.
    
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
  
But - how to find the best shortcut efficiently?  
  
### A* algoritm  
A good option to find the best shortcut fast is offered by the A*-algorithm (which is used also in map software to calculate the shortest routes).  
This algorithm will first build up a graph, then - based on weight and distance - calculate the shortest possible shortcut.  
In the current implementation, the graph is build for the current hamilton path section to the next goodie. Meaning: the snake will 
still stay on the hamiltonian cycle.    
![screenshot of hamiltonian cycle](https://github.com/lpapailiou/snake/blob/master/src/main/resources/img/screenshot_aStartShortcut.png)
  
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

For further help, click [here](https://gist.github.com/lpapailiou/d4d63338ccb1413363970ac571aa71c9).
