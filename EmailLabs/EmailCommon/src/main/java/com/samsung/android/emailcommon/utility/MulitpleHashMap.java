package com.samsung.android.emailcommon.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MulitpleHashMap<K, V> {
	HashMap<K, ArrayList<V>> mHashData = new HashMap<K, ArrayList<V>>();
	public void put (K k, V v) {
		if (!mHashData.containsKey(k)) {
			ArrayList<V> data = new ArrayList<V>();
			mHashData.put(k, data);
		}
		mHashData.get(k).add(v);
	}
	
	public List<V> get(K k) {
		return mHashData.get(k);
	}
}
