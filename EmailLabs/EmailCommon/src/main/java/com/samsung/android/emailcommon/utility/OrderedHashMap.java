package com.samsung.android.emailcommon.utility;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OrderedHashMap<KEY, VALUE> implements java.util.Map<KEY, VALUE>{

	private ArrayList<KEY> mKeyList = new ArrayList<KEY> ();
	private ArrayList<Entry<KEY, VALUE>> mEntryList = new ArrayList<Entry<KEY, VALUE>> ();
	
	@Override
	public void clear() {
		mKeyList.clear();
		mEntryList.clear();
	}
	@Override
	public boolean containsKey(Object key) {
		return mKeyList.indexOf(key) >= 0;
	}
	
	@Override
	public boolean containsValue(Object value) {
		for (Entry<KEY, VALUE> e : mEntryList) {
			if (value == e.getValue())
				return true;
		}
		return false;
	}
	
	@Deprecated
	@Override
	public Set<Entry<KEY, VALUE>> entrySet() {
		return null;
	}
	
	public List<Entry<KEY, VALUE>> entryList() {
		return mEntryList;
	}
	@Override
	public VALUE get(Object key) {
		int index = mKeyList.indexOf(key);
		if (index < 0)
			return null;
		return mEntryList.get(index).getValue();
	}
	@Override
	public boolean isEmpty() {
		return mKeyList.isEmpty();
	}
	
	@Deprecated
	@Override
	public Set<KEY> keySet() {
		return null;
	}
	
	public List<KEY> keyList() {
		return mKeyList;
	}
	
	@Deprecated
	@Override
	public VALUE put(KEY key, VALUE value) {
		return null;
	}
	
	public void add(KEY key, VALUE value) {
		mKeyList.add(key);
		mEntryList.add(new AbstractMap.SimpleEntry<KEY, VALUE>(key, value));
	}
	@Deprecated
	@Override
	public void putAll(Map<? extends KEY, ? extends VALUE> arg0) {
	}
	
	@Override
	public VALUE remove(Object key) {
		int index = mKeyList.indexOf(key);
		if (index < 0)
			return null;
		mKeyList.remove(index);
		Entry<KEY, VALUE> e = mEntryList.remove(index);
		return e.getValue();		
	}
	
	public VALUE remove(int index) {
		mKeyList.remove(index);
		Entry<KEY, VALUE> e = mEntryList.remove(index);
		return e.getValue();		
	}
	@Override
	public int size() {
		return mKeyList.size();
	}
	
	@Deprecated
	@Override
	public Collection<VALUE> values() {
		return null;
	}
}
