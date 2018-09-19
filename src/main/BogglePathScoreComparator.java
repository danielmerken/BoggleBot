package main;

import java.util.Comparator;

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