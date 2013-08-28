package com.narren.hackerrank;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class JourneyToMoon {

	public static void main(String[] args) {

		int N;
		int I = 0;
		List<Set<Integer>> setList;

		Scanner in = new Scanner(System.in);
		N = in.nextInt();
		I = in.nextInt();
		setList = new ArrayList<Set<Integer>>(I);

		for (int i = 0; i < I; i++) {
			Set<Integer> s1 = new HashSet<Integer>();
			s1.add(in.nextInt());
			s1.add(in.nextInt());
			setList.add(s1);
			}
		perfomeComputation(setList);
	}
    private static void perfomeComputation(List<Set<Integer>> setList) {
        for (int i = 0; i < setList.size(); i++) {
            for (int j = i+1; j < setList.size(); j++) {
                if (!Collections.disjoint(setList.get(i), setList.get(j))) {
                    setList.get(i).addAll(setList.get(j));
                    setList.remove(j);
                }
            }
        }
        int reduction = 0;
        int totalElement = 0;
        for (int i = 0; i < setList.size(); i++) {
        	totalElement += setList.get(i).size();
        	reduction += findCombination(setList.get(i).size(), 2);
        }
        int totalCombination = findCombination(totalElement, 2);
        int effectiveCombination = totalCombination - reduction;
        System.out.println(effectiveCombination);
    }
    private static int findCombination(int n, int r) {
    	int numerator = factorial(n);
    	int denominator = factorial(r) * factorial(n-r);
    	return (numerator/denominator);
    }
    private static int factorial(int n) {
        int fact = 1; // this  will be the result
        for (int i = 1; i <= n; i++) {
            fact *= i;
        }
        return fact;
    }
}
