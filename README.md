# KevinBaconCalculator

Kevin Bacon Calculator is a project I took on starting in the HarvardX course cs50 ai

The Goal of this Project is two link two actors based on association
every association or degree of seperation is two actors who have stared in the same move
This comes from the online notion that every actor in hollyword is connected to kevin bacon by six degrees or less

Originally this was made to the specifications of cs50 ai, but has later been greatly improved to reduce the calculation time.

the two folders display the two different artificial intelligence calculators
  the first labeled "Degree Calc" is my original iteration of this problem
    it impliments a single BreadthFirstSearch algorithm that 
      searches all neighbors of an actor then all neighbors of those actors until it reaches the desired actor
  the second labeled Kev Bacon Calc is the second iteration of this problem
    it implements two BreadFirstSearch algorithms that attempt to find eachother
      this was far more difficult but resulted in a much faster calculation time compared to a single algorithm
      
  the two folders contain similar files
    pycache file created by python compiler
    large folder containing a database of many actors, movies, and links between them
    small folder containing a smaller database used for testing
    util/utility file used for utility functions used in the main file
    a main .py file that runs all calculations
   
   to run the files simply open a terminal at that folder and type:
   
   
    python degrees.py (small/large)
      then fill in the actors name as it prompts you
     
            or
     
   python KevinBacon.py (small/large)
      then fill in the actors name as it prompts you
