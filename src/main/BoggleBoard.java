package main;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
/**
 * Class representing a Boggle board and its solutions. Boards can be randomly
 * generated or specified. Contains methods for adding and getting solutions,
 * getting characters at certain points on the board, getting adjacent points
 * on the board, and checking whether a point is contained within the board.
 * 
 * @author Daniel Merken <dcm58@uw.edu>
 */
public class BoggleBoard {
	private static final Random RANDOM = new Random();
	
	/**
	 * Stores characters that make up board
	 */
	String[][] board;
	/**
	 * Stores solutions to this board. Solutions are represented as a path
	 * through the board
	 */
	List<BogglePath> solutions;
	
	/**
	 * Creates a board out of the given 2D array of characters
	 * 
	 * @param board Characters that will make up the given board
	 * @throws IllegalArgumentException if supplied board has width or height 
	 * 		   of 0 or if rows of supplied board are of inconsistent length or
	 * 		   if the board contains any strings that are not a single letter
	 * 		   or Qu
	 */
	public BoggleBoard(String[][] board) {
		if (board.length == 0 || board[0].length == 0) {
			throw new IllegalArgumentException("Board must have number of "
					+ "rows and cols greater than 0");
		}
		for (String[] row : board) {
			if (row.length != board[0].length) {
				throw new IllegalArgumentException(
						"Rows of provided board must be of consistent length");
			}
			for (String letter : row) {
				if (!(letter.equals("Qu") || (letter.length() == 1 && 
						!letter.equals("Q") && 
						Character.isUpperCase(letter.charAt(0))))) {
					throw new IllegalArgumentException("Board can only contain "
							+ "capital letters (except for \"Q\") and \"Qu\"");
				}
			}
		}
		this.board = board;
		solutions = Collections.synchronizedList(new ArrayList<BogglePath>());
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
		board = generateRandomBoard(width, height);
		solutions = Collections.synchronizedList(new ArrayList<BogglePath>());
	}
	
	/**
	 * If the provided solution is not already stored in this board, stores the
	 * solution in this board. Else, does nothing.
	 * 
	 * @param solution A BogglePath representing a valid word in
	 * 		  this board
	 * @throws IllegalArgumentException If the provided BogglePath is not a
	 *         path of this board
	 */
	public synchronized void addSolution(BogglePath solution) {
		if (this != solution.getBoard()) {
			throw new IllegalArgumentException("Provided solution is not a path"
					+ " in this board");
		}
		if (!solutions.contains(solution)) {
			solutions.add(solution);
		}
	}
	
	/**
	 * Returns a list of BogglePaths that represent every solution stored in this
	 * board.
	 * 
	 * @return A list of BogglePaths. Each BogglePath represents one solution 
	 * 		   word found in this board
	 */
	public List<BogglePath> getSolutions() {
		return new ArrayList<BogglePath>(solutions);
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
		List<List<Point>> result = new ArrayList<List<Point>>();
		for (BogglePath solutionPath : solutions) {
			result.add(solutionPath.getPoints());
		}
		return result;
	}
	
	/**
	 * Returns every solution stored by this board as a list of strings
	 * 
	 * @return a list of words that represent every solution found in this 
	 *         board
	 */
	public synchronized List<String> getSolutionWords() {
		List<String> result = new ArrayList<String>();
		for (BogglePath solutionPath : solutions) {
			result.add(solutionPath.getWord());
		}
		return result;
	}
	
	/**
	 * Takes a path of points and returns the word that results from following
	 * that path in order on this board.
	 * 
	 * @param path A series of points representing a path
	 * @return The word corresponding to the path on this board
	 */
	public String getWord(List<Point> path) {
		String result = "";
		for (Point p : path) {
			result += get(p);
		}
		return result;
	}
	
	/**
	 * Gets the character located at the specified point on this board
	 * 
	 * @param p Location of character to be returned
	 * @return The character at the specified point
	 */
	public String get(Point p) {
		return get(p.getX(), p.getY());
	}
	
	/**
	 * Gets the character located at the specified coordinates on this board
	 * 
	 * @param x X-coordinate of point to be returned
	 * @param y Y-coordinate of point to be returned
	 * @return The character at the specified coordinates
	 */
	public String get(int x, int y) {
		if (!containsPoint(x, y)) {
			throw new IndexOutOfBoundsException("Location specified is out of "
					+ "this board's range");
		}
		return board[y][x];
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
		for (String[] row : board) {
			for (String letter : row) {
				result += letter + " ";
			}
			result += '\n';
		}
		return result;
	}
	
	/**
	 * Generates a 2d array filled with random upper case letters, representing
	 * a Boggle board of a specified width and height. "Q"s will be replaced  
	 * with "Qu".
	 * @param width Width of the 2d array to be generated
	 * @param height Height of the 2d array to be generated
	 * @return A 2d array of strings filled with random upper case letters
	 * 		   ("Q" will be replaced with "Qu")
	 */
	public static String[][] generateRandomBoard(int width, int height) {
		if (height < 1 || width < 1) {
			throw new IllegalArgumentException("Width and height must be "
					+ "greater than 0");
		}
		String[][] board = new String[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				board[i][j] = generateRandomBoggleLetter();
			}
		}
		return board;
	}
	
	/**
	 * Generates a 2d array filled with random upper case letters, representing
	 * a Boggle board of a specified width and height. "Q"s will be replaced  
	 * with "Qu".
	 * @param width Width of the 2d array to be generated
	 * @param height Height of the 2d array to be generated
	 * @return A 2d array of strings filled with random upper case letters
	 * 		   ("Q" will be replaced with "Qu")
	 */
	public static String generateRandomBoggleLetter() {
		String result = "" + (char) ((int) 'A' + RANDOM.nextInt(26));
		if (result.equals("Q")) {
			result = "Qu";
		}
		return result;
	}
}
