package main;

import java.util.Comparator;

/**
 * Comparator for BogglePaths that consider paths whose words occur earlier in 
 * the alphabet as lesser.
 * 
 * @author Daniel Merken <dcm58@uw.edu>
 */
public class BogglePathWordComparator implements Comparator<BogglePath> {

	@Override
	public int compare(BogglePath o1, BogglePath o2) {
		return o1.getWord().compareTo(o2.getWord());
	}
	
}