Jason Nichols-Allen

4.  I chose breadth-first search for my uninformed search method because I 
figured it would be the easiest to convert directly into A*.  My breadth-
first implementation is capable of solving all the yards from 1-5, though
yard 1 takes between 10-20 minutes to complete.

5.  (t^c)*c!

6.  The heuristic in my AStar function counts the number of misplaced
cars on a particular track, and adds that amount to the path cost as 
the h value.  The heuristic speeds up the breadth-first search version
of the function from 10 minutes in the best case, to 84 seconds.
