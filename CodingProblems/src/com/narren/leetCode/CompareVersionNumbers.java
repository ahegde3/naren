package com.narren.leetCode;
/**
 * 
Compare two version numbers version1 and version2.
If version1 > version2 return 1, if version1 < version2 return -1, otherwise return 0.

You may assume that the version strings are non-empty and contain only digits and the . character.
The . character does not represent a decimal point and is used to separate number sequences.
For instance, 2.5 is not "two and a half" or "half way to version three", it is the fifth second-level revision of the second first-level revision.

Here is an example of version numbers ordering:

0.1 < 1.1 < 1.2 < 13.37
 * 
 * @author naren
 *
 */
public class CompareVersionNumbers {
	
	public static void main(String[] args) {
		CompareVersionNumbers cvn = new CompareVersionNumbers();
		System.out.println(cvn.compareVersion("1.0.0.1", "1"));
	}
	public int compareVersion(String version1, String version2) {
		String[] v1 = version1.split("\\.");

		String[] v2 = version2.split("\\.");

		
        int l = v1.length > v2.length ? v1.length : v2.length;
        
        for(int i = 0; i < l; i++) {
        Integer v1m = i < v1.length ? new Integer(v1[i]) : 0;
		Integer v2m = i < v2.length ? new Integer(v2[i]) : 0;
        if(v1m > v2m) {
			return 1;
		}

		if(v1m < v2m) {
			return -1;
		}    
        }
        
        return 0;
	}
}
