package main;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class BoggleBot {
	private static final ForkJoinPool POOL = new ForkJoinPool();
	
	private Trie dictionary;
	private BoggleBoard board;
	
	public BoggleBot(Trie dictionary, BoggleBoard board) {
		this.dictionary = dictionary;
		this.board = board;
	}
	
	public void solveBoard() {
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
			List<Point> adjPoints = new ArrayList<Point>();
			Point adjPoint = new Point(lastPoint.getX() + 1, lastPoint.getY());
			if (validSpace(adjPoint)) adjPoints.add(adjPoint);
			adjPoint = new Point(lastPoint.getX() - 1, lastPoint.getY());
			if (validSpace(adjPoint)) adjPoints.add(adjPoint);
			adjPoint = new Point(lastPoint.getX(), lastPoint.getY() + 1);
			if (validSpace(adjPoint)) adjPoints.add(adjPoint);
			adjPoint = new Point(lastPoint.getX(), lastPoint.getY() - 1);
			if (validSpace(adjPoint)) adjPoints.add(adjPoint);
			List<BoggleBotTask> threads = new ArrayList<BoggleBotTask>();
			for (Point p : adjPoints) {
				List<Point> nextPath = new ArrayList<Point>(currPath);
				nextPath.add(p);
				threads.add(new BoggleBotTask(dictionary, board, nextPath,
						prefix + board.get(p)));
			}
			for (int i = 0; i < threads.size() - 1; i++) {
				threads.get(i).fork();
			}
			threads.get(threads.size() - 1).compute();
		}
		
		private boolean validSpace(Point p) {
			return !currPath.contains(p) && p.getX() >= 0 && p.getY() >= 0 &&
					p.getX() < board.getWidth() && p.getY() < board.getHeight()
					&& dictionary.containsPrefix(prefix + board.get(p));
		}
	}
}
