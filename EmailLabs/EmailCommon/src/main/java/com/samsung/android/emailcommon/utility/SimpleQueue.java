package com.samsung.android.emailcommon.utility;

import java.util.ArrayList;

public class SimpleQueue<E> {
	private ArrayList<E> mData = new ArrayList<E>();
	public void clear() {
		mData.clear();
	}
	
	public void push(E data) {
		if (data == null)
			return;
		mData.add(data);
	}
	
	synchronized public E pop() {
		E data;
		if (mData.size() < 1)
			return null;
		data = mData.get(0);
		mData.remove(0);
		return data;
	}
}
