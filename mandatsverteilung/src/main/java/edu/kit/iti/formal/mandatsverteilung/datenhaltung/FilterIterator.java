package edu.kit.iti.formal.mandatsverteilung.datenhaltung;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Abstrake Klasse für Iteratoren, die anderen Iterator filtern.
 */
public abstract class FilterIterator<T> implements Iterator<T> {

	private T next;

	@Override
	public boolean hasNext() {
		return next != null;
	}

	/**
	 * Returns the next element
	 * 
	 * @return the next element
	 */
	protected abstract T internalFindNext();

	/**
	 * Von der Unterklasse aufzurufen, sobald internalFindNextReady bereit ist,
	 * das erste Element zu liefern.
	 */
	protected void internalFindNextReady() {
		this.next = internalFindNext();
	}

	@Override
	public T next() {
		if (next == null) {
			throw new NoSuchElementException();
		}
		T result = next;
		next = internalFindNext();
		return result;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
