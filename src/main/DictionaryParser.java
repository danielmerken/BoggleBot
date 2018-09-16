package main;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
/**
 * @author DanielMerken
 */
public class DictionaryParser {
	
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
