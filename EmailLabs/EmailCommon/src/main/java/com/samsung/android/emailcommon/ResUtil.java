package com.samsung.android.emailcommon;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

@SuppressLint("UseSparseArrays")
public class ResUtil {
	
	static protected ResUtil _inst = null;
	
	
	protected ResUtil() {
		
	}


	private Resources mResources = null;
//	private static String TAG = "ResourceUtil";
	private HashMap<Integer, Object> mPack = new HashMap<Integer, Object> ();
	
	static public ResUtil createInstance(Resources res) {
		
		if (_inst == null) {
			ResUtil ru = new ResUtil();
			ru.mResources = res;
			_inst = ru;
		}
		return (ResUtil)_inst;
	}
	
	public void destroyInstance() {
		mPack.clear();
	}
	
	
	public Drawable getDrawable(int resId) {
		if (mResources == null)
			return null;
		
		if (mPack.containsKey(resId)) {
			return (Drawable)mPack.get(resId);
		}
		Drawable obj = mResources.getDrawable(resId);
		mPack.put(resId,  obj);
		return obj;
	}
	
	
	public String getString(int resId) {
		if (mResources == null)
			return "";
		
		if (mPack.containsKey(resId)) {
			return (String) mPack.get(resId);
		}
		
		String str = mResources.getString(resId);
		mPack.put(resId,  str);
		return str;
	}
	

}
