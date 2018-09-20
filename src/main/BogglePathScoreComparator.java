package main;

import java.util.Comparator;

/**
 * Comparator for BogglePaths that considers BogglePaths with lower scores as
 * lesser. If two paths have equal score, consider paths whose words occur
 * earlier in the alphabet as lesser.
 * 
 * @author Daniel Merken <dcm58@uw.edu>
 */
public class BogglePathScoreComparator implements Comparator<BogglePath> {

	@Override
	public int compare(BogglePath o1, BogglePath o2) {
		if (o1.getScore() == o2.getScore()) {
			return o1.getWord().compareTo(o2.getWord());
		} else {
			return -Integer.compare(o1.getScore(), o2.getScore());
		}
	}
	
}