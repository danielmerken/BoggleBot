package main;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
/**
 * Class that contains a parallelized method to solve a board. 
 * 
 * @author Daniel Merken <dcm58@uw.edu>
 */
public class BoggleBot {
	private static final ForkJoinPool POOL = new ForkJoinPool();
	
	/**
	 * Finds all words in the given dictionary that are contained in the
	 * given boggle board and stores results in the supplied BoggleBoard
	 * 
	 * @param dictionary Contains a dictionary of which words to search for
	 * @param board The board in which to search for words
	 */
	public static void solveBoard(Trie dictionary, BoggleBoard board) {
		for (int i = 0; i < board.getWidth(); i++) {
			for (int j = 0; j < board.getHeight(); j++) {
				Point p = new Point(i, j);
				if (dictionary.containsPrefix("" + board.get(p))) {
					List<Point> path = new ArrayList<Point>();
					path.add(p);
					POOL.invoke(new BoggleBotTask(dictionary, board, path, 
							"" + board.get(p)));
				}
			}
		}
	}
	
	private static class BoggleBotTask extends RecursiveAction {
		
		private Trie dictionary;
		private BoggleBoard board;
		private List<Point> currPath;
		private String prefix;
		
		public BoggleBotTask(Trie dictionary, BoggleBoard board, 
				List<Point> currPath, String prefix) {
			this.dictionary = dictionary;
			this.board = board;
			this.currPath = currPath;
			this.prefix = prefix;
		}
		
		@Override
		protected void compute() {
			Point lastPoint = currPath.get(currPath.size() - 1);
			if (dictionary.containsWord(prefix)) {
				board.addSolution(currPath);
			}
			List<Point> adjPoints = board.getAdjPoints(lastPoint);
			List<BoggleBotTask> threads = new ArrayList<BoggleBotTask>();
			for (Point p : adjPoints) {
				if (!currPath.contains(p) && 
						dictionary.containsPrefix(prefix + board.get(p))) {
					List<Point> nextPath = new ArrayList<Point>(currPath);
					nextPath.add(p);
					threads.add(new BoggleBotTask(dictionary, board, nextPath,
							prefix + board.get(p)));
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
