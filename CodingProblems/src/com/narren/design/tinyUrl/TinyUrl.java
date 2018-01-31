package com.narren.design.tinyUrl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TinyUrl {
	
	private void encodeUrl(String url) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] thedigest = md.digest(url.getBytes());
			long value = 0;
			for (int i = 0; i < 5; i++) {
			   value = (value << 8) + (thedigest[i] & 0xff);
			}
//			System.out.println(value);
			
			String map = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
			String turl = "";
			while(value > 0) {
				int temp = (int) (value % 62);
				turl += map.substring(temp, temp + 1); 
				value = value / 62;
			}
			System.out.println(turl);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		TinyUrl turl = new TinyUrl();
		turl.encodeUrl("https://twitter.com/IISuperwomanII?ref_src=twsrc%5Egoogle%7Ctwcamp%5Eserp%7Ctwgr%5Eauthor");
		turl.encodeUrl("https://twitter.com/IISuperwomanII?ref_src=twsrc%5Egoogle%7Ctwcamp%5Eserp%7Ctwgr%5Euthor");
		turl.encodeUrl("https://twitter.com/IISuperwomanII?ref_src=twsrc%5Egoogle%7Ctwcamp%5Eserp%7Ctwgr%5Eathor");
		turl.encodeUrl("https://twitter.com");
	}

}
