package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import main.BoggleBoard;

public class BoggleBoardTest {
	
	private BoggleBoard board;
	
	@Before
	public void initialize() {
		char[][] letters = {{'a', 'b', 'c'},
							{'d', 'e', 'f'},
							{'g', 'h', 'i'}};
		board = new BoggleBoard(letters);
	}
	
	/**
	 * Test that a randomly created board will only contain letters
	 */
	@Test
	public void constructRandomBoard() {
		// Get string of a random board, remove all whitespace
		String boardString = new BoggleBoard(3, 3).toString()
				.replaceAll("\\s+", "");
		for (int i = 0; i < boardString.length(); i++) {
			assertTrue('a' <= boardString.charAt(i) && 
					boardString.charAt(i) <= 'z');
		}
	}

	/**
	 * Test that the toString function prints the correct board
	 */
	@Test
	public void boardToString() {
		String answer = "a b c \nd e f \ng h i \n";
		assertEquals(answer, board.toString());
	}

}
