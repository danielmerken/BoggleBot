package main;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
/**
 * @author Daniel Merken <dcm58@uw.edu>
 */
public class BoggleBoard {
	/**
	 * Stores characters that make up board
	 */
	char[][] board;
	/**
	 * Stores solutions to this board. Solutions are represented as an ordered 
	 * list of points corresponding to characters on this board that make up a
	 * word 
	 */
	List<List<Point>> solutions;
	
	/**
	 * Creates a board out of the given 2D array of characters
	 * 
	 * @param board Characters that will make up the given board
	 * @throws IllegalArgumentException if supplied board has width or height 
	 * 		   of 0 or if rows of supplied board are of inconsistent length
	 */
	public BoggleBoard(char[][] board) {
		if (board.length == 0 || board[0].length == 0) {
			throw new IllegalArgumentException("Board must have number of "
					+ "rows and cols greater than 0");
		}
		for (char[] row : board) {
			if (row.length != board[0].length) {
				throw new IllegalArgumentException(
						"Rows of provided board must be of consistent length");
			}
		}
		this.board = board;
		solutions = new ArrayList<List<Point>>();
	}
	
	/**
	 * Creates a boggle board filled with random characters with the specified
	 * dimensions.
	 * 
	 * @param width Width of board to be created
	 * @param height Height of board to be created
	 * @throws IllegalArgumentException if width or height are less than 1
	 */
	public BoggleBoard(int width, int height) {
		if (height < 1 || width < 1) {
			throw new IllegalArgumentException("Width and height must be "
					+ "greater than 0");
		}
		board = new char[height][width];
		Random rand = new Random();
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				board[i][j] = (char) ((int) 'a' + rand.nextInt(26));
			}
		}
		solutions = new ArrayList<List<Point>>();
	}
	
	/**
	 * Stores a specified solution to this board
	 * 
	 * @param solution Ordered list of points representing a solution. The
	 * 				   character corresponding to each point in the list 
	 * 				   forms the word the solution represents.
	 * @throws IllegalArgumentException if the solution is empty or if the
	 *         solution contains the same point multiple times or if the
	 * 		   provided list contains two points in a row that are not
	 * 		   adjacent in this board
	 * @throws IndexOutOfBoundsException if any point in the solution does not
	 * 		   fall within this board
	 */
	public synchronized void addSolution(List<Point> solution) {
		if (solution.isEmpty()) {
			throw new IllegalArgumentException("Invalid solution: Cannot have "
					+ "a solution of length 0");
		}
		Set<Point> solutionSet = new HashSet<Point>(solution);
		if (solutionSet.size() != solution.size()) {
			throw new IllegalArgumentException("Invalid solution: Contains "
					+ "a single point multiple times");
		}
		if (!containsPoint(solution.get(0))) {
			throw new IndexOutOfBoundsException("Invalid solution: Solution "
					+ "path out of bounds");
		}
		for (int i = 1; i < solution.size(); i++) {
			if (!containsPoint(solution.get(i))) {
				throw new IndexOutOfBoundsException("Invalid solution: Solution"
						+ " path out of bounds");
			}
			if (!getAdjPoints(solution.get(i - 1)).contains(solution.get(i))) {
				throw new IllegalArgumentException("Invalid solution: Solution"
						+ " path jumps between non-adjacent spaces");
			}
		}
		solutions.add(solution);
	}
	
	/**
	 * Return a list of lists of points that represent every solution stored in 
	 * this board. Each list of points represent a word, with each point 
	 * corresponding to a character on the board that forms the word.
	 * 
	 * @return a list of lists of points representing all solutions stored in
	 *         this board
	 */
	public List<List<Point>> getSolutionPoints() {
		return solutions;
	}
	
	/**
	 * Returns every solution stored by this board as a list of strings
	 * 
	 * @return a list of words that represent every solution found in this 
	 *         board
	 */
	public List<String> getSolutionWords() {
		List<String> result = new ArrayList<String>();
		for (List<Point> solutionPath : solutions) {
			String currWord = "";
			for (Point p : solutionPath) {
				currWord += get(p);
			}
			result.add(currWord);
		}
		return result;
	}
	
	/**
	 * Gets the character located at the specified point on this board
	 * 
	 * @param p Location of character to be returned
	 * @return The character at the specified point
	 */
	public char get(Point p) {
		return get(p.getX(), p.getY());
	}
	
	/**
	 * Gets the character located at the specified coordinates on this board
	 * 
	 * @param x X-coordinate of point to be returned
	 * @param y Y-coordinate of point to be returned
	 * @return The character at the specified coordinates
	 */
	public char get(int x, int y) {
		if (!containsPoint(x, y)) {
			throw new IndexOutOfBoundsException("Location specified is out of "
					+ "this board's range");
		}
		return board[x][y];
	}
	
	/**
	 * Returns the width, or number of columns, of this board
	 * 
	 * @return the width of this board
	 */
	public int getWidth() {
		return board[0].length;
	}
	
	/**
	 * Returns the height, or number of rows, of this board
	 * 
	 * @return the height of this board
	 */
	public int getHeight() {
		return board.length;
	}
	
	/**
	 * Returns true if the specified coordinates lie within this board, else
	 * false
	 * 
	 * @param x The x-coordinate to be checked
	 * @param y The y-coordinate to be checked
	 * @return True if the specified coordinates lie within this board, else 
	 * 		   false
	 */
	public boolean containsPoint(int x, int y) {
		return x < getWidth() && x >= 0 && y < getHeight() && y >= 0;
	}
	
	/**
	 * Returns true if the specified point lies within this board, else false
	 * 
	 * @param p The point to be checked
	 * @return True if the specified point lies within this board, else false
	 */
	public boolean containsPoint(Point p) {
		return containsPoint(p.getX(), p.getY());
	}
	
	/**
	 * Returns a list of points in all directions adjacent to the given point
	 * 
	 * @param p The point that will have it's adjacent neighbors returned
	 * @return A list of points located in all directions adjacent to the 
	 *         given point 
	 */
	public List<Point> getAdjPoints(Point p) {
		List<Point> adjPoints = new ArrayList<Point>();
		Point adjPoint = new Point(p.getX() + 1, p.getY());
		if (containsPoint(adjPoint)) adjPoints.add(adjPoint);
		adjPoint = new Point(p.getX() - 1, p.getY());
		if (containsPoint(adjPoint)) adjPoints.add(adjPoint);
		adjPoint = new Point(p.getX(), p.getY() + 1);
		if (containsPoint(adjPoint)) adjPoints.add(adjPoint);
		adjPoint = new Point(p.getX(), p.getY() - 1);
		if (containsPoint(adjPoint)) adjPoints.add(adjPoint);
		adjPoint = new Point(p.getX() + 1, p.getY() + 1);
		if (containsPoint(adjPoint)) adjPoints.add(adjPoint);
		adjPoint = new Point(p.getX() - 1, p.getY() - 1);
		if (containsPoint(adjPoint)) adjPoints.add(adjPoint);
		adjPoint = new Point(p.getX() - 1, p.getY() + 1);
		if (containsPoint(adjPoint)) adjPoints.add(adjPoint);
		adjPoint = new Point(p.getX() + 1, p.getY() - 1);
		if (containsPoint(adjPoint)) adjPoints.add(adjPoint);
		return adjPoints;
	}
	
	@Override
	public String toString() {
		String result = "";
		for (char[] row : board) {
			for (char c : row) {
				result += c + " ";
			}
			result += '\n';
		}
		return result;
	}
}
