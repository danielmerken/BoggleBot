package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.AbstractListModel;

public class SortableListModel<E> extends AbstractListModel<E> {
	List<E> list;
	
	public SortableListModel() {
		list = new ArrayList<E>();
	}
	
	public void set (List<E> list, Comparator<E> comparator) {
		this.list = new ArrayList<E>(list);
		Collections.sort(this.list, comparator);
	}
	
	public void sort(Comparator<E> comparator) {
		Collections.sort(list, comparator);
		fireContentsChanged(this, 0, list.size() - 1);
	}
	
	public void clear() {
		list.clear();
		fireContentsChanged(this, 0, 0);
	}

	@Override
	public int getSize() {
		return list.size();
	}

	@Override
	public E getElementAt(int index) {
		return list.get(index);
	}
}
