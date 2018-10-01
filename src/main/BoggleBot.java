package main;
import java.util.Stack;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
/**
 * Class that contains a parallelized and linear method to solve a board. 
 * 
 * @author Daniel Merken <dcm58@uw.edu>
 */
public class BoggleBot {
	private static final ForkJoinPool POOL = new ForkJoinPool();
	private static final int SEQUENTIAL_CUTOFF = 8;
	
	/**
	 * Finds all words in the given dictionary that are contained in the
	 * given boggle board and stores results in the supplied BoggleBoard
	 * in parallel
	 * 
	 * @param dictionary Contains a dictionary of which words to search for
	 * @param board The board in which to search for words
	 */
	public static void solveBoard(Trie dictionary, BoggleBoard board) {
		POOL.invoke(new BoggleBotTask(dictionary, board, 0, board.getWidth() * board.getHeight()));
	}
	
	/**
	 * Finds all words in the given dictionary that are contained in the
	 * given boggle board and stores results in the supplied BoggleBoard
	 * in linear
	 * 
	 * @param dictionary Contains a dictionary of which words to search for
	 * @param board The board in which to search for words
	 */
	public static void solveBoardLinear(Trie dictionary, BoggleBoard board) {
		for (int i = 0; i < board.getWidth(); i++) {
			for (int j = 0; j < board.getHeight(); j++) {
				solveBoardFromPoint(dictionary, board, new Point(i, j));
			}
		}
	}
	
	public static void solveBoardFromPoint(Trie dictionary, BoggleBoard board, Point start) {
		if (dictionary.containsPrefix(board.get(start))) {
			Stack<BogglePath> dfs = new Stack<BogglePath>();
			dfs.push(new BogglePath(board, start));
			while (!dfs.isEmpty()) {
				BogglePath currPath = dfs.pop();
				if (dictionary.containsWord(currPath.getWord())) {
					board.addSolution(currPath);
				}
				for (Point p : board.getAdjPoints(currPath.getLastPoint())) {
					if (!currPath.containsPoint(p) && 
							dictionary.containsPrefix(
									currPath.getWord() + board.get(p))) {
						BogglePath nextPath = new BogglePath(currPath);
						nextPath.addPoint(p);
						dfs.push(nextPath);
					}
				}
			}
		}
	}
	
	private static class BoggleBotTask extends RecursiveAction {
		
		private Trie dictionary;
		private BoggleBoard board;
		private int lo;
		private int hi;
		
		public BoggleBotTask(Trie dictionary, BoggleBoard board, int lo, int hi) {
			this.dictionary = dictionary;
			this.board = board;
			this.lo = lo;
			this.hi = hi;
		}
		
		@Override
		protected void compute() {
			if (hi - lo <= SEQUENTIAL_CUTOFF) {
				for (int i = lo; i < hi; i++) {
					solveBoardFromPoint(dictionary, board, 
							new Point(i / board.getWidth(), i % board.getWidth()));
				}
			} else {
				BoggleBotTask left = new BoggleBotTask(dictionary, board, lo, (hi + lo) / 2);
				BoggleBotTask right = new BoggleBotTask(dictionary, board, (hi + lo) / 2, hi); 
				left.fork();
				right.compute();
			}
		}
	}
}
