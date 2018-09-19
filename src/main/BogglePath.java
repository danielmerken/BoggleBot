package main;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a path of through the spaces of a BoggleBoard. Tracks what spaces
 * the path visits, the corresponding word formed by said spaces on the 
 * BoggleBoard, and the score value of the word. Each space may only be 
 * traversed once by a given BogglePath. A given point in the list must be
 * adjacent to the next and previous point in the list.
 * @author Daniel Merken <dcm58@uw.edu>
 */
public class BogglePath {
	/**
	 * Contains all points of this path
	 */
	private List<Point> points;
	/**
	 * Word formed by this path
	 */
	private String word;
	/**
	 * Point value of this path
	 */
	private int score;
	/**
	 * Boggle Board that this path is contained in
	 */
	private BoggleBoard board;
	
	/**
	 * Creates a new empty path in the given board with the given starting 
	 * point
	 * @param board The board this path will be contained in
	 * @param start The starting point of this path
	 */
	public BogglePath(BoggleBoard board, Point start) {
		points = new ArrayList<Point>();
		points.add(start);
		word = board.get(start).toLowerCase();
		score = 0;
		this.board = board;
	}
	
	/**
	 * Constructs a copy of the given BogglePath
	 * @param path The path to construct a copy of
	 */
	public BogglePath(BogglePath path) {
		points = new ArrayList<Point>(path.points);
		word = path.word;
		score = path.score;
		board = path.board;
	}
	
	/**
	 * Adds the given point to the end of this path
	 * @param p Point to be added to the end of this path
	 */
	public void addPoint(Point p) {
		if (containsPoint(p)) {
			throw new IllegalArgumentException("This path may not traverse a"
					+ " given space more than once");
		}
		if (!board.getAdjPoints(getLastPoint()).contains(p)) {
			throw new IllegalArgumentException("Specified point to be added is"
					+ " not adjacent to previous point");
		}
		points.add(p);
		word += board.get(p).toLowerCase();
		score++;
	}
	
	/**
	 * Returns the list of points this path contains
	 * @return A list of points this path contains in the same order as the
	 * 		   path
	 */
	public List<Point> getPoints() {
		return new ArrayList<Point>(points);
	}
	
	/**
	 * Returns the word that this path corresponds to in the Boggle board
	 * @return The word that this path corresponds to in the Boggle board
	 */
	public String getWord() {
		return word;
	}
	
	/**
	 * Returns the score that this path's word corresponds to
	 * @return returns the score that this path's word corresponds to
	 */
	public int getScore() {
		return score;
	}
	
	/**
	 * Returns the Boggle board this path is contained in
	 * @return the Boggle board this path is contained in
	 */
	public BoggleBoard getBoard() {
		return board;
	}
	
	/**
	 * Returns the last point in this path
	 * @return the last word in this path
	 */
	public Point getLastPoint() {
		return points.get(points.size() - 1);
	}
	
	/**
	 * Returns true iff the a given point is contained in this path
	 * @param The point to be checked whether it is contained in this path
	 * @return True if the given point is contained in this path, else returns
	 * false
	 */
	public boolean containsPoint(Point p) {
		return points.contains(p);
	}
	
	@Override
	public String toString() {
		return word + " (" + score + ")";
	}
}
