package test;

import static org.junit.Assert.*;

import org.junit.Test;

import main.DictionaryParser;
import main.Trie;

public class DictionaryParserTest {

	/**
	 * Test that the dictionary parser correctly reads in all words in the
	 * dictionary
	 */
	@Test
	public void dictionaryParser() {
		Trie trie = DictionaryParser.parseDictionary("src/main/resources/dictionary.txt");
		assertTrue(trie.containsWord("apple"));
	}

}
