package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import main.Trie;

public class TrieTest {
	
	Trie trie;
	
	@Before
	public void initialize() {
		trie = new Trie();
	}

	/**
	 * Test to make sure trie is initially empty
	 */
	@Test
	public void createEmptyTrie() {
		assertTrue(trie.containsPrefix(""));
		assertFalse(trie.containsWord(""));
		for (int i = 0; i < 26; i++) {
			assertFalse(trie.containsPrefix("" + (char) ('a' + i)));
			assertFalse(trie.containsWord("" + (char) ('a' + i)));
		}
	}

	/**
	 * Test to make sure adding a single word to trie works correctly
	 */
	@Test
	public void insertSingleWord() {
		trie.insert("california");
		assertTrue(trie.containsWord("california"));
		assertFalse(trie.containsWord("californ"));
	}
	
	/**
	 * Test to make sure words/prefixes are not case sensitive
	 */
	@Test
	public void notCaseSensitive() {
		trie.insert("calIforNia");
		assertTrue(trie.containsWord("CaliforniA"));
		assertTrue(trie.containsPrefix("CALi"));
	}
	
	/**
	 * Test to make sure that prefixes work correctly with a single word
	 */
	@Test
	public void prefixOfSingleWord() {
		trie.insert("california");
		for (int i = 1; i <= 10; i++) {
			assertTrue(trie.containsPrefix("california".substring(0, i)));
		}
		assertFalse(trie.containsPrefix("ali"));
	}
	
	/**
	 * Test to make sure that partially overlapping words work correctly
	 */
	@Test
	public void partiallyOverlappingWords() {
		trie.insert("california");
		trie.insert("call");
		trie.insert("calamari");
		assertTrue(trie.containsPrefix("cal"));
		assertTrue(trie.containsPrefix("call"));
		assertTrue(trie.containsPrefix("cala"));
		assertTrue(trie.containsPrefix("cali"));
		assertTrue(trie.containsPrefix("california"));
		assertTrue(trie.containsPrefix("call"));
		assertTrue(trie.containsPrefix("calamari"));
	}
	
	/**
	 * Test to make sure that completely overlapping words work correctly
	 */
	@Test
	public void fullyOverlappingWords() {
		trie.insert("alphabet");
		trie.insert("alpha");
		assertTrue(trie.containsPrefix("alpha"));
		assertTrue(trie.containsWord("alpha"));
		assertTrue(trie.containsPrefix("alph"));
		assertTrue(trie.containsPrefix("alphab"));
		assertTrue(trie.containsWord("alphabet"));
	}
	
}
