package main;

import java.util.Comparator;

public class BogglePathWordComparator implements Comparator<BogglePath> {

	@Override
	public int compare(BogglePath o1, BogglePath o2) {
		return o1.getWord().compareTo(o2.getWord());
	}
	
}