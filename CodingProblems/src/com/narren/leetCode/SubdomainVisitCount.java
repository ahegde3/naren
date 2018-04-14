package com.narren.leetCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class SubdomainVisitCount {
	
	public static void main(String[] args) {
		SubdomainVisitCount svc = new SubdomainVisitCount();
		System.out.println(svc.subdomainVisits(new String[]{"654 yaw.lmm.ca", "1225 lmm.ca"}));
	}
	
	public List<String> subdomainVisits(String[] cpdomains) {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		List<String> list = new ArrayList<String>();

		for(String domain : cpdomains) {
			String[] input = domain.split(" ");
			
			if(map.containsKey(input[1])) {
				int c = map.get(input[1]);
				map.put(input[1], c + new Integer(input[0]));
			} else {
				map.put(input[1], new Integer(input[0]));
			}
			String dom = "";
			for(int i = input[1].length() - 1; i >= 0; i--) {
				if(input[1].charAt(i) == '.') {
					dom = input[1].substring(i + 1);
					if(map.containsKey(dom)) {
						int c = map.get(dom);
						map.put(dom, c + new Integer(input[0]));
					} else {
						map.put(dom, new Integer(input[0]));
					}
				}
			} 
		}

		for(Entry<String, Integer> e : map.entrySet()) {
			list.add("" + e.getValue() + " " + e.getKey());
		}

		return list;
	}
}
