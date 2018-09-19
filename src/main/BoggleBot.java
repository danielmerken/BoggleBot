package main;
import java.util.ArrayList;
import java.util.List;
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
	
	/**
	 * Finds all words in the given dictionary that are contained in the
	 * given boggle board and stores results in the supplied BoggleBoard
	 * in parallel
	 * 
	 * @param dictionary Contains a dictionary of which words to search for
	 * @param board The board in which to search for words
	 */
	public static void solveBoard(Trie dictionary, BoggleBoard board) {
		for (int i = 0; i < board.getWidth(); i++) {
			for (int j = 0; j < board.getHeight(); j++) {
				Point p = new Point(i, j);
				if (dictionary.containsPrefix(board.get(p))) {
					BogglePath path = new BogglePath(board, p);
					POOL.invoke(new BoggleBotTask(dictionary, board, path));
				}
			}
		}
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
				if (dictionary.containsPrefix(board.get(i, j))) {
					Stack<BogglePath> dfs = new Stack<BogglePath>();
					dfs.push(new BogglePath(board, new Point(i, j)));
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
		}
	}
	
	private static class BoggleBotTask extends RecursiveAction {
		
		private Trie dictionary;
		private BoggleBoard board;
		private BogglePath currPath;
		
		public BoggleBotTask(Trie dictionary, BoggleBoard board, 
				BogglePath currPath) {
			this.dictionary = dictionary;
			this.board = board;
			this.currPath = currPath;
		}
		
		@Override
		protected void compute() {
			if (dictionary.containsWord(currPath.getWord())) {
				board.addSolution(currPath);
			}
			List<Point> adjPoints = board.getAdjPoints(currPath.getLastPoint());
			List<BoggleBotTask> threads = new ArrayList<BoggleBotTask>();
			for (Point p : adjPoints) {
				if (!currPath.containsPoint(p) && 
						dictionary.containsPrefix(
								currPath.getWord() + board.get(p))) {
					BogglePath nextPath = new BogglePath(currPath);
					nextPath.addPoint(p);
					threads.add(new BoggleBotTask(dictionary, board, nextPath));
				}
			}
			if (!threads.isEmpty()) {
				for (int i = 0; i < threads.size() - 1; i++) {
					threads.get(i).fork();
				}
				threads.get(threads.size() - 1).compute();
			}
		}
	}
}
