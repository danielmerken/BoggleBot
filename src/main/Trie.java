package main;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple Trie data structure that stores strings as keys and a boolean
 * representing whether or not the corresponding string is a valid word. 
 * Includes methods for inserting strings and checking if a given prefix/word
 * is a valid prefix/word in the Trie.
 * 
 * @author Daniel Merken <dcm58@uw.edu>
 */

public class Trie {
	private TrieNode root;
	
	/**
	 * Class for a node representing a character or the root in a Trie. 
	 */
	public class TrieNode {
		/*
		 * Stores the children nodes of this node
		 */
		public Map<Character, TrieNode> children;
		/* 
		 * True iff the character this node represents the final character in
		 * a string
		 */
		public boolean validWord; 
		
		/**
		 * Constructs a new empty TrieNode
		 */
		public TrieNode() {
			validWord = false;
			children = new HashMap<Character, TrieNode>();
		}
	}
	
	/**
	 * Constructs a new empty Trie
	 */
	public Trie() {
		root = new TrieNode();
	}
	
	/**
	 * Inserts the given string, word, into this Trie
	 * @param word The string to be inserted into the Trie
	 */
	public void insert(String word) {
		word = word.toLowerCase();
		TrieNode currNode = root;
		for (int i = 0; i < word.length(); i++) {
			if (!currNode.children.containsKey(word.charAt(i))) {
				currNode.children.put(word.charAt(i), new TrieNode());
			}
			currNode = currNode.children.get(word.charAt(i));
		}
		currNode.validWord = true;
	}
	
	/**
	 * Determines whether or not a given string, prefix, is the prefix to any
	 * word contained in this Trie.
	 * @param prefix The string to be checked in this Trie
	 * @return True if the provided string is a prefix to any word in this Trie,
	 * else returns false
	 */
	public boolean containsPrefix(String prefix) {
		prefix = prefix.toLowerCase();
		TrieNode currNode = root;
		for (int i = 0; i < prefix.length(); i++) {
			if (!currNode.children.containsKey(prefix.charAt(i))) {
				return false;
			}
			currNode = currNode.children.get(prefix.charAt(i));
		}
		return true;
	}
	
	/**
	 * Determines whether or not a given string, word, is contained in this Trie
	 * @param word The string to be checked in this Trie
	 * @return True if this word is in this Trie, else returns false
	 */
	public boolean containsWord(String word) {
		word = word.toLowerCase();
		TrieNode currNode = root;
		for (int i = 0; i < word.length(); i++) {
			if (!currNode.children.containsKey(word.charAt(i))) {
				return false;
			}
			currNode = currNode.children.get(word.charAt(i));
		}
		return currNode.validWord;
	}
}
