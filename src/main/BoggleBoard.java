package main;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BoggleBoard {
	char[][] board;
	List<List<Point>> solutions;
	
	public BoggleBoard(char[][] board) {
		this.board = board;
		solutions = new ArrayList<List<Point>>();
	}
	
	public BoggleBoard(int width, int height) {
		if (height < 1 || width < 1) {
			throw new IllegalArgumentException("Width and height must be "
					+ "positive");
		}
		board = new char[height][width];
		Random rand = new Random();
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				board[i][j] = (char) ((int) 'a' + rand.nextInt(26));
			}
		}
	}
	
	public void addSolution(List<Point> solution) {
		for (Point p : solution) {
			if (p.getX() > getWidth() || p.getX() < 0 || p.getY() > getHeight()
					|| p.getY() < 0) {
				throw new IllegalArgumentException("All points in solution "
						+ "must be contained within board");
			}
		}
		solutions.add(solution);
	}
	
	public char get(Point p) {
		if (p.getX() > getWidth() || p.getX() < 0 || p.getY() > getHeight() 
				|| p.getY() < 0) {
			throw new IllegalArgumentException("Location specified is out of "
					+ "range");
		}
		return board[p.getX()][p.getY()];
	}
	
	public char get(int x, int y) {
		if () {
			throw new IllegalArgumentException("Location specified is out of "
					+ "range");
		}
		return board[x][y];
	}
	
//	public int getWidth() {
//		return board[0].length;
//	}
//	
//	public int getHeight() {
//		return board.length;
//	}
	
	public containsPoint(int x, int y) {
		return x < getWidth() && x >= 0 && y < getHeight() && y >= 0;
	}
	
	public containsPoint(Point p) {
		return p.getX() < getWidth() && p.getX() >= 0 && p.getY() < getHeight() && p.getY() >= 0;
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
