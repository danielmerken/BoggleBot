package main;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
/**
 * Class that contains a method that fills and returns a trie with the contents
 * of a specified file. A minimum length can be specified, meaning lines of 
 * insufficient length will be ignored (not added to the resulting trie).
 * 
 * @author DanielMerken
 */
public class DictionaryParser {
	
	/**
	 * Reads and stores every line in the given file into a trie, then returns
	 * said trie. Ignores lines in the file that are shorter than the specified
	 * minimum length.
	 * 
	 * @param filename Path to the file whose lines will be scanned in to the 
	 * 		  resulting trie
	 * @param minLength Minimum length of lines to be returned, shorter lines 
	 *        will be ignored
	 * @return A trie containing every line of text in the specified file that
	 *         is of at least the specified length
	 */
	public static Trie parseDictionary(String filename, int minLength) {
		BufferedReader reader = null;
		Trie result = new Trie();
		try {
			reader = new BufferedReader(new FileReader(filename));
			// Add one word at a time
			String inputLine;
			while ((inputLine = reader.readLine()) != null) {
				if (inputLine.length() >= minLength) {
					result.insert(inputLine);
				}
			}
		} catch (IOException e) {
			System.err.println(e.toString());
			e.printStackTrace(System.err);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					System.err.println(e.toString());
					e.printStackTrace(System.err);
				}
			}
		}
		return result;
	}
}
