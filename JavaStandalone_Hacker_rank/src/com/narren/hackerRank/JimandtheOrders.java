package com.narren.hackerRank;

import java.util.Scanner;

public class JimandtheOrders {

	static class Element {
		int index;
		int sum;
		int ordered;
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int len = sc.nextInt();
		Element[] arr = new Element[len];
		for (int i = 0; i < len ; i++) {
			int orderT = sc.nextInt();
			int timeT = sc.nextInt();
			Element e = new Element();
			e.index = i;
			e.sum = orderT + timeT;
			e.ordered = orderT;
			arr[i] = e;
		}
		quickSort(arr, 0, len - 1);
		for (int i = 0; i < len ; i++) {
			System.out.print((arr[i].index + 1) + " ");
		}
	}
	private static void quickSort(Element[] ar, int start, int end) {
		if(start < end) {
			int partitionIndex = partitionIndex(ar, start, end);
			quickSort(ar, start, partitionIndex - 1);
			quickSort(ar, partitionIndex + 1, end);
		}
	}
	
	private static int partitionIndex(Element[] list, int start, int end) {
		int partitionIndex = start;
		int pivot = end;
		for(int i = start; i < end; i++) {
			if (isSmall(list[i], list[pivot])) {
				swap(list, i, partitionIndex);
				partitionIndex++;
			}
		}
		swap(list, partitionIndex, pivot);
		return partitionIndex;
	}
	
	private static void swap(Element[] ar, int i, int j) {
		Element temp = ar[i];
		ar[i] = ar[j];
		ar[j] = temp;
	}
	
	private static boolean isSmall(Element e1, Element e2) {
		if (e1.sum < e2.sum) {
			return true;
		} else if (e1.sum == e2.sum) {
			return e1.ordered < e2.ordered;
		}
		return false;
	}
}
