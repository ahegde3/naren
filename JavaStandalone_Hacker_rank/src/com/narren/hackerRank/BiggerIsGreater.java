package com.narren.hackerRank;

import java.util.Arrays;
import java.util.Scanner;

public class BiggerIsGreater {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int T = in.nextInt();
		for (int i = 0; i < T; i++) {
			String input = in.next();
			process(input);
		}
	}
	//abcdefghijklmnopqrstuvwxyz
	public static void process (String input) {
		int array[] = new int[input.length()];
		if (input.length() < 2) {
			System.out.println("no answer");
			return;
		}
		for (int j = 0; j < input.length(); j++) {
			array[j] = input.charAt(j);
		}

 		for (int i = (array.length - 1); i > 0; i--) {
			if (array[i] > array[i-1] && i-1 > 0) {
				//swap(array, i, i-1);
				if (i+1 < array.length) {
					//quickSort(array, i - 1, array.length - 1);
					
					int tempFirst = array[i-1];
					quickSort(array, i - 1, array.length - 1);
					boolean broke = false;
					for (int j = i - 1; j < array.length; j++) {
						if (tempFirst < array[j]) {
							broke = true;
							int first = array[j];
							for (int k = j; k >= i; k--) {
								array[k] = array[k-1];
							}
							array[i-1] = first;
							//swap(array, i-1, j);
							break;
						}
					}
					
					
				} else {
					swap(array, i, i-1);
				}
				System.out.println(convertCharToString(array));
				return;
			} 
			if (i-1 == 0) {
				if (array[i] <= array[i-1]) {
					System.out.println("no answer");
					return;
				}
				processReverse(array);
				return;
			}
		}
	}
	
	public static void processReverse (int[] arr) {
		int tempFirst = arr[0];
		Arrays.sort(arr);
		boolean broke = false;
		for (int j = 0; j < arr.length; j++) {
			if (tempFirst < arr[j]) {
				broke = true;
				swap(arr, 0, j);
				break;
			}
		}
		if (tempFirst == arr[0] || !broke) {
			System.out.println("no answer");
			return;
		}
		tempFirst = arr[0];
		arr[0] = 1;
		Arrays.sort(arr);
		arr[0] = tempFirst;
		System.out.println(convertCharToString(arr));
	}
	
	public static void swap (int[] arr, int index, int index2) {
		int temp = arr[index];
		arr[index] = arr[index2];
		arr[index2] = temp;
	}
	
	public static String convertCharToString(int[] arr) {
		String value = null;
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			str.append(Character.toString((char) arr[i]));
		}
		value = str.toString();
		return value;
	}
	
	static void quickSort(int[] ar, int start, int end) {
		if (start < end) {
			int partitonIndex = partition(ar, start, end);
			quickSort(ar, start, partitonIndex - 1);
			quickSort(ar, partitonIndex + 1, end);
		}
	}   

	static int partition(int[] ar, int start, int end) {
		int pivot = ar[end];
		int partitionIndex = start;
		for (int i = start; i < end; i++) {
			if (ar[i] <= pivot) {
				swap(ar, i, partitionIndex);
				partitionIndex++;
			}
		}
		swap(ar, end, partitionIndex);
		return partitionIndex;
	}
}
