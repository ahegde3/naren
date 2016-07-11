//package com.samsung.android.emailcommon;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Environment;
//
//public class HtmlImagePickerCommon {
//	
//	final static private String PREFIX = "Email";
//	final static public int THUMB_PER_MSG = 3;
//	
//	static public File getDir(long accountId) {
//		File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + PREFIX + File.separator + 
//				String.valueOf(accountId) + File.separator);
//	    	if (!dir.exists()) {
////	    		dir = new File (Environment.getExternalStorageDirectory() + File.separator + PREFIX + File.separator);
////	    		dir.createNewFile();
////	    		dir = new File (dir + File.separator + String.valueOf(accountId) + File.separator);
//	    		dir.mkdirs();
//	    	}
//	    return dir;
//	}
//	
//	static public List<Bitmap> getBitmap(long accountId, long messageId) {
//		ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap> ();
//		for (int i = 0; i < THUMB_PER_MSG; i ++) {
//			File bf = new File(getThumbFilePath(accountId, messageId, i));
//			if (bf.exists()) {
//				Bitmap bitmap = null;
//				try {
//					InputStream is = new FileInputStream(bf);
//					bitmap = BitmapFactory.decodeStream(is);
//					bitmaps.add(bitmap);
//				} catch (FileNotFoundException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		return bitmaps;
//	}
//	
//	
//	
//	static public void deleteFile(long accountId, long messageId) {
//		for (int i = 0; i < THUMB_PER_MSG; i ++) {
//			File bf = new File(getThumbFilePath(accountId, messageId, i));
//			if (bf.exists())
//				bf.delete();
//		}
//	}
//	
//	static public void deleteFile(long accountId) {
//		StringBuilder b = new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath());
//		b.append(File.separator).append(PREFIX).append(File.separator).append(accountId);
//		File bf = new File(b.toString());
//		if (bf.exists())
//			bf.delete();
//	}
//	
//	static public void deleteFile() {
//		StringBuilder b = new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath());
//		b.append(File.separator).append(PREFIX);
//		File bf = new File(b.toString());
//		if (bf.exists()) 
//			deleteRecursive(bf);
//		
//	}
//	
//	static private void deleteRecursive(File fileOrDirectory) {
//	    if (fileOrDirectory.isDirectory()){
//	        File list[] = fileOrDirectory.listFiles();
//	        if(list == null) return;
//	        for (File child : fileOrDirectory.listFiles())
//	        	deleteRecursive(child);
//	    }
//	    fileOrDirectory.delete();
//	}
//	
//	static public String getThumbFilePath(long accountId, long messageId, int num) {
//		StringBuilder b = new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath());
//		b.append(File.separator).append(PREFIX).append(File.separator).append(accountId).append(File.separator).append(messageId);
//		b.append("_").append(num);
//		return b.toString();
//	}
//}
