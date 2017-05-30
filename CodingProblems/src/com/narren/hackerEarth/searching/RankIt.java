package com.narren.hackerEarth.searching;

import java.util.Arrays;
import java.util.Scanner;

/**
 * You have been given an array 
A
A consisting of 
N
N integers. All the elements in this array 
A
A are unique. You have to answer some queries based on the elements of this array. Each query will consist of a single integer 
x
x. You need to print the rank based position of this element in this array considering that the array is 
1
1 indexed. The rank based position of an element in an array is its position in the array when the array has been sorted in ascending order.

Note: It is guaranteed that all the elements in this array are unique and for each 
x
x belonging to a query, value 
′
x
′
′x′ shall exist in the array

Input Format

The first line consists of a single integer 
N
N denoting the size of array 
A
A. The next line contains 
N
N unique integers, denoting the content of array 
A
A. The next line contains a single integer 
q
q denoting the number of queries. Each of the next 
q
q lines contains a single integer 
x
x denoting the element whose rank based position needs to be printed.

Output Format

You need to print 
q
q integers denoting the answer to each query.
 * 
 * @author naren
 *
 */
public class RankIt {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in); 
		int N = sc.nextInt();
		long[] arr = new long[N];
		for(int i = 0; i < N; i++) {
			arr[i] = sc.nextLong();
		}
		int q = sc.nextInt();
		Arrays.sort(arr);
		while(q > 0) {
			int n = sc.nextInt();
			System.out.println(findRank(arr, n, 0, arr.length - 1));
			q--;
		}
	}
	
	static int findRank(long[] arr, int q, int s, int e) {
        if(s >= e) {
            return s;
        }
        int mid = (e - s) / 2;
        if(q == arr[s]) {
            return s;
        } else if(q == arr[mid]) {
            return mid;
        } else if(q == arr[e]) {
            return e;
        }

        if(q < mid) {
            return findRank(arr, q, s, mid);
        } else {
            return findRank(arr, q, mid, e);
        }
		
	}
}
