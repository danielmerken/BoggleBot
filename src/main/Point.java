package main;
/**
 * @author Daniel Merken <dcm58@uw.edu>
 */
public class Point {
	/**
	 * x-coordinate of this point
	 */
	private int x;
	/**
	 * y-coordinate of this point
	 */
	private int y;
	
	/**
	 * Constructs a new point with the given coordinates
	 * 
	 * @param x The x-coordinate of the point to be constructed
	 * @param y The y-coordinate of the point to be constructed
	 */
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns the x-coordinate of this point
	 * 
	 * @return the x-coordinate of this point
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Returns the y-coordinate of this point
	 * 
	 * @return the y-coordinate of this point
	 */
	public int getY() {
		return y;
	}
	
	 @Override
	 public int hashCode() {
		 int hashCode = x;
		 hashCode += 31 * y;
		 return hashCode;
	 }
	 
	 @Override
	 public boolean equals(Object o) {
		 if (!(o instanceof Point)) {
			 return false;
		 }
		 Point p = (Point) o;
		 return p.x == this.x && p.y == this.y;
	 }
}
