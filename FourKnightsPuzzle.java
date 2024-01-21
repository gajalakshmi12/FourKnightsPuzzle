import java.util.*;

class Node implements Comparable<Node> {
	char[][] board;
	Node parent;
	int costSoFar;
	int estimatedCost;
	int totalCost; // costSoFar + estimatedCost

	public Node(char[][] board, Node parent, int g, int h, int f) {
		this.board = new char[board.length][board[0].length];
		for (int i = 0; i < board.length; i++) {
			this.board[i] = Arrays.copyOf(board[i], board[i].length);
		}
		this.parent = parent;
		this.costSoFar = g;
		this.estimatedCost = h;
		this.totalCost = f;
	}

	@Override
	public int compareTo(Node other) {
		return Integer.compare(totalCost, other.totalCost);
	}
}

public class FourKnightsPuzzle {
	/*
	 * Initial state of the chess board 
	 *    B E B 
	 *    E E E 
	 *    W E W
	 * 
	 * Goal State to be reached through A* or Branch and Bound 
	 *    W E W
	 *    E E E 
	 *    B E B
	 * 
	 * Where B -> Black Knight W -> White Knight E -> Empty Cell
	 */
	static char[][] startState = { { 'B', 'E', 'B' }, { 'E', 'E', 'E' }, { 'W', 'E', 'W' } };

	static char[][] goalState = { { 'W', 'E', 'W' }, { 'E', 'E', 'E' }, { 'B', 'E', 'B' } };

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		Node initNodeAStar = new Node(startState, null, 0, calculateHeuristic(startState),
				calculateHeuristic(startState));
		/* A* Method call */
		Node resNodeAStar = aStar(initNodeAStar);

		System.out.println("Execution time for A* Algorithm: " + (System.currentTimeMillis() - startTime) + " ms");
		System.out
				.println("Total moves for A* Algorithm: " + (resNodeAStar != null ? resNodeAStar.costSoFar : 0));
        
		/* Stores and prints path for each position visited by a Knight during A* Implementation */
		if (resNodeAStar != null) {
			List<Node> path = new ArrayList<>();
			Node currNode = resNodeAStar;
			while (currNode != null) {
				path.add(currNode);
				currNode = currNode.parent;
			}
			Collections.reverse(path);			
			for (int move = 0; move < path.size(); move++) {
				System.out.println("Move " + move + " (A*):");
				char[][] state = path.get(move).board;
				for (char[] row : state) {
					for (char i : row) {
						System.out.print(i + " ");
					}
					System.out.println();
				}
				System.out.println();
			}
		} else {
			System.out.println("No solution found for A* Search");
		}

		startTime = System.currentTimeMillis();
		Node initNodeBFS = new Node(startState, null, 0, 0, 0);
		/* BFS Method call */
		Node resNodeBFS = breadthFirstSearch(initNodeBFS);

		System.out.println("Execution time for BFS: " + (System.currentTimeMillis() - startTime) + " ms");
		System.out.println("Total moves for BFS: " + (resNodeBFS != null ? resNodeBFS.costSoFar : 0));
		
		/* Stores and prints path for each position visited by a Knight during BFS Implementation */
		if (resNodeBFS != null) {
			List<Node> path = new ArrayList<>();
			Node currNode = resNodeBFS;
			while (currNode != null) {
				path.add(currNode);
				currNode = currNode.parent;
			}
			Collections.reverse(path);
			for (int move = 0; move < path.size(); move++) {
				System.out.println("Move " + move + " (BFS):");
				char[][] state = path.get(move).board;
				for (char[] row : state) {
					for (char i : row) {
						System.out.print(i + " ");
					}
					System.out.println();
				}
				System.out.println();
			}
		} else {
			System.out.println("No solution found for BFS");
		}
	}

	/* Calculates Manhattan Distance */
	public static int calculateDist(int x1, int x2, int y1, int y2) {
		int dist = Math.abs(x2 - x1) + Math.abs(y2 - y1);
		return dist;
	}

	/* Calculates Heuristic value */
	public static int calculateHeuristic(char[][] board) {
		int heuristic = 0;
		int rows = board.length;
		int cols = board[0].length;

		/* Iterate through the entire chess board for each row and column value */
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				char cell = board[i][j];
				/*
				 * Column indices for the goal positions is 0 or 2. If j is not 0, assign column
				 * index as 2
				 */
				int columnIndex = (j == 0) ? 0 : 2;
				if (cell == 'E') {
					continue;
					/* Calculating total heuristics for each knight on the board */
				} else if (cell == 'W') {
					heuristic += calculateDist(i, 2, j, columnIndex);
				} else { // square == 'B'
					heuristic += calculateDist(i, 0, j, columnIndex);
				}
			}
		}
		return heuristic;
	}

	/* Check if the Knight move is valid */
	public static boolean isValidMove(int x, int y, char[][] board) {
		return (x >= 0) && (x < board.length) && (y >= 0) && (y < board[0].length);
	}

	/*
	 * Fetches a new board which is branched due to the changes made by moving the
	 * Knights. A Knight can only be moved to a position if that particular cell is
	 * unoccupied. The current cell is marked as empty
	 */
	public static char[][] fetchNewBoard(char[][] board, int currRow, int currCol, int newRow, int newCol,
			char cellValue) {
		char[][] newBoard = new char[board.length][board[0].length];
		for (int r = 0; r < board.length; r++) {
			newBoard[r] = Arrays.copyOf(board[r], board[r].length);
		}
		newBoard[currRow][currCol] = 'E';
		newBoard[newRow][newCol] = cellValue;
		return newBoard;
	}

	/* Returns a List of all Neighbor Nodes on the board for a given Node */
	public static List<Node> findNeighborNodes(Node node) {
		char[][] board = node.board;

		/*
		 * Identifies all possible moves according to standard chess rules for a Knight
		 */
		int[][] possibleMoves = { { -1, -2 }, { -1, 2 }, { 1, -2 }, { 1, 2 }, { -2, -1 }, { -2, 1 }, { 2, -1 },
				{ 2, 1 } };
		List<Node> neighborsList = new ArrayList<>();
		int totRows = board.length;
		int totCols = board[0].length;

		for (int currRow = 0; currRow < totRows; currRow++) {
			for (int currCol = 0; currCol < totCols; currCol++) {
				char cellValue = board[currRow][currCol];
				if (cellValue == 'E') {
					continue;
				}
				for (int[] move : possibleMoves) {
					int newRow = currRow + move[0];
					int newCol = currCol + move[1];
					if (isValidMove(newRow, newCol, board) && board[newRow][newCol] == 'E') {
						char[][] newBoard = fetchNewBoard(board, currRow, currCol, newRow, newCol, cellValue);
						Node newNode = new Node(newBoard, node, node.costSoFar + 1, calculateHeuristic(newBoard),
								node.costSoFar + 1 + calculateHeuristic(newBoard));
						neighborsList.add(newNode);
					}
				}
			}
		}
		return neighborsList;
	}

	/*
	 * Implementation of the solution using A* algorithm
	 * 
	 * A* algorithm uses a heuristic function at each step to determine the lowest
	 * cost to reach the goal state. f(n) = Cost so Far + Estimated cost to reach
	 * the goal
	 * 
	 */
	public static Node aStar(Node start) {
		/* Implemented through a priority queue to automatically sort based on cost */
		PriorityQueue<Node> openList = new PriorityQueue<>();
		/*
		 * Make an open list containing only the starting node and declare an empty
		 * closed list
		 */
		Set<String> closedList = new HashSet<>();
		openList.add(start);

		while (!openList.isEmpty()) {
			Node current = openList.poll();
			/* If current node equals goal state, return the current Node and exit search */
			if (Arrays.deepEquals(current.board, goalState)) {
				return current;
			}
			closedList.add(Arrays.deepToString(current.board));
			/* If neighbor node is not present in closed list, add neighbor to open list */
			for (Node neighbor : findNeighborNodes(current)) {
				if (!closedList.contains(Arrays.deepToString(neighbor.board))) {
					openList.add(neighbor);
				}
			}
		}

		return null; // No solution found
	}

	/*
	 * Implementation of Branch and Bound Search - Breadth First Search Algorithm
	 * 
	 * BFS returns the least number of moves for any search. It uses a queue to
	 * decide which node is to be expanded. It doesn't keep track of estimated cost to goal. 
	 * 
	 */
	public static Node breadthFirstSearch(Node start) {
		Queue<Node> queue = new LinkedList<>();
		@SuppressWarnings({ "rawtypes", "unchecked" })
		/* Stores a set of nodes that are currently explored */
		Set<String> visited = new HashSet();

		queue.offer(start);
		visited.add(Arrays.deepToString(start.board));

		while (!queue.isEmpty()) {
			Node currentNode = queue.poll();
			
			/* If current node equals goal state, return the current Node and exit search */
			if (Arrays.deepEquals(currentNode.board, goalState)) {
				return currentNode;
			}

			for (Node neighbor : findNeighborNodes(currentNode)) {
				String neighborStr = Arrays.deepToString(neighbor.board);
				/* If neighbor is not present in the set, add neighbor to queue and set */
				if (!visited.contains(neighborStr)) {
					queue.offer(neighbor);
					visited.add(neighborStr);
				}
			}
		}

		return null; // No solution found
	}
}
