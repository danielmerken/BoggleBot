package test;
import main.BoggleBoard;
import main.BoggleBot;
import main.DictionaryParser;
import main.Trie;

public class ParallelSpeedUpTest {
	private static final int[] BOARD_SIZES = {4, 10, 20, 50, 100};
	private static final int NUM_RUNS = 3;
	
	public static void main(String[] args) {
		Trie dictionary = DictionaryParser.parseDictionary(3);
		
		// JVM warmup
		for (int i = 0; i < 10; i++) {
			BoggleBoard board = new BoggleBoard(20, 20);
			BoggleBot.solveBoard(dictionary, board);
			BoggleBot.solveBoardLinear(dictionary, board);
		}
				
		for (int size : BOARD_SIZES) {
			System.out.println("Board size: " + size + " x " + size);
			long totalParallelTime = 0;
			long totalLinearTime = 0;
			for (int i = 0; i < NUM_RUNS; i++) {
				BoggleBoard board = new BoggleBoard(size, size);
				long startTime = System.currentTimeMillis();
				BoggleBot.solveBoard(dictionary, board);
				totalParallelTime += System.currentTimeMillis() - startTime;
				startTime = System.currentTimeMillis();
				BoggleBot.solveBoardLinear(dictionary, board);
				totalLinearTime += System.currentTimeMillis() - startTime;
			}
			System.out.println("\tParralel: " + (totalParallelTime / NUM_RUNS) + " ms");
			System.out.println("\tLinear: " + (totalLinearTime / NUM_RUNS) + " ms");

		}
	}
}
