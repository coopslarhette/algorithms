# algorithms
This is a repo I keep for showcasing different algorithms I have implemented for my algorithms class at school.

## pathfinder
uninformed
* this is a search algorithm that searches for a goal state in a text-based maze composed of X's and .'s, where X's represent 
an impassible wall and .'s are places where the algorithm is free to move
  * the algorithm is implemented using a search tree where cost of each move is uniform so cost is proportional to the depth
  of the tree
* this alogrithm is, as the name implies, completely uninformed about its environment. because of this it is rather dumb and
inefficient, it is simple and easy to understand however.
informed

## game of nim
* this module implements an algorithm that plays and tries to win the game of nim ([wiki article here](https://en.wikipedia.org/wiki/Nim))
  * it is implemented through a minimax search tree combined with alpha-beta pruning for more efficient search
  * it works by searching through the tree to find which moves will lead to it winning and then taking the appropriate amount of stones from the pile
  * unfortunately I never completed this module in time for the due date :(
  
## longest common subsequence (LCS) 
* this module implents a recursive and iterative solution to the classic longerst common subsequence problem
  * the recurive solution to this problem is implemented by filling a 2D array in a top-down fashion, ie by starting at the solution cell and then recursively tracing back to a base case and then filling the cells with the length of each lcs of the cross between a substring of each intial string
    * it does this until it is able to fill the solution cell which is the length of the lcs of the cross between each full-length intial strings
    * because it does this in a top-down recursive fashion, it is usually faster (depending on input of course) than the iteriative solution as it does not have to fill out every cell in the array
    * once it finds the length of the problems's lcs, it traces back through the array recursively and collects the solution by seeing if the letters of the strings match at each index of the array (note it is possible to have multiple LCS's which this solution accounts for)
  * the iterative solution to this solution is somewhat similar as it functions by filling a 2D array of LCS lengths
    * it differs in that it has to fill all of the cells in the array, essentially functioning to find the length of every possible combination of substrings of the two intial strings (ie finding solutions to all subproblems)
    * it finds the final solution by building on the solutions to the smaller subproblems where it looks for longest lcs length in the cell to the left and the cell above and takes the max of those two cells
    * each cell either inherits the solution from the cell to the left or above if the characters of the strings at the current index don't matches or increments the last found solution (located diagonally to the upper left in the array) if the characters match
    * it collects the solution in the same manner as the recursive solution
