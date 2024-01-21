Four Knights Puzzle Using A* Algorithm:
A* Algorithm is implemented to reach the goal state while guaranteeing minimum cost. A* algorithm uses a heuristic function to provide an estimated cost to the goal state while also calculating the cost incurred so far. It is defined as 
f(n) = g(n) + h(n)
where g(n) = cost to reach node n
            h(n) = cost to reach goal state from node n
In order to calculate h(n), I have used Manhattan distance method since each knight moves horizontally and vertically to reach node n. It is given as, 
distance = abs(x2-x1) + abs(y2-y1)
Algorithm used: 
1.	Define an openlist containing only the starting node
2.	Define a closedlist 
3.	While the openlist is not empty, fetch the node from the queue which has least cost. This is implemented using a PriorityQueue which sorts nodes automatically based on least cost. 
4.	If the current (fetched) node equals goal state, return the path and the total number of moves
5.	Else, find neighbor nodes that aren't in closed list and add them to open list  
Number of moves to reach goal state: 16. 

Four Knights Puzzle using Branch and Bound Algorithm (BFS):
Breadth First Search (BFS) is a search algorithm to find the shortest path between two nodes in a graph. It expands all the nodes in a level before moving on to the next level. It is implemented using Queue Data Structure. 
BFS doesn't keep track of the heuristic values.
Algorithm used: 
1.	Define a queue containing only the starting node. 
2.	Define an empty set of visited nodes
3.	While the queue is not empty, fetch a node from the queue. 
4.	If the current (fetched) node equals goal state, return the path and print the total number of moves.
5.	Else, find neighbor nodes that aren't in visited set and add them to the set.
 Number of moves to reach goal state: 16. 

Using DFS Algorithm (Inference based on trial run) 
As evidenced by the output, DFS returns a feasible output for the Knights Puzzle but it is not guaranteed have the minimum number of moves. DFS algorithm run time was about 269964 ms and the number of moves to achieve goal state was 172. Since BFS was able to reach the goal state in 16 moves. BFS is more suited to this problem than DFS. 

Output Comparison: 
Algorithm	Execution Time	Number of moves to goal state
A*	59 ms	16
BFS	5 ms	16
DFS	269964 ms	172

 
