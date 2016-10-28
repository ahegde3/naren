package com.narren.sotong;

import java.util.Scanner;

/**
 * 
Jessica is a girl who is loved by many boys. Jessica has an exam in a few days but hasn’t started exam study yet. To pass the exam, she has to read and memorize a very thick book. Just like many others, the author of the book had repeated the same contents through many pages of the book. Jessica, realizing such a fact, wants to read each content more than once but to read the minimal pages of the book. 

Eventually she has decided to find the consecutively minimal parts so that she can read all the contents on the book. The boys who adore her have already found out contents on each page to help her exam study. 

Now, it is item to find the least section that she wants. At this moment, contents on each page are regarded as non-negative integers. For example, if there are contents of 1 8 8 2 8 8 1 in order on each page of the book, you need to read the first four pages of 1 8 8 2 or the last four pages of 2 8 8 1 that make you read the whole contents. When the numbers of the contents on each page of the book are given, find the minimal scope of the book to pass the exam in order to help out Jessica. 

Time limit: 10 second (java: 20 seconds)

[Input]
Several test cases can be included in the inputs. T, the number of cases is given in the first row of the inputs. After that, the test cases as many as T (T ≤ 30) are given in a row.
The number of the pages, N is given on the first row of each test case. (1 ≤ N ≤ 1000000)
The contents repeated on each page are given by being separated with a blank on the second row. 

[Output]
Output a length of the minimal section including all contents on the first row of each test case. 

[I/O Example] 

 Input 
2
7
1 8 8 2 8 8 1
10
8 10 6 1 10 9 9 7 5 1

Output
4
9 

Sample I/O

5
2
2 1
5
1 4 2 1 1
10
8 10 6 1 10 9 9 7 5 1
10
1 1 6 7 1 1 9 10 3 5
30
1 27 5 16 25 1 23 15 13 10 22 7 6 13 13 6 1 19 26 6 21 10 29 4 1 11 7 1 19 19

expected output
2
3
9
8
25

Summary:
find the count of the shortest  sequence which contains all occured numbers.
 * 
 * @author nsbisht
 *
 */
public class ReadingBooks {

	public static void main(String[] args) {
//		int[] input = new int[]{1,4,2,1,1};
//		System.out.println(process(input));
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt();
		while(T > 0) {
			int N = sc.nextInt();
			int[] input = new int[N];
			int maxNumber = 0;
			for(int i = 0; i < N; i++) {
				input[i] = sc.nextInt();
				maxNumber = Math.max(maxNumber, input[i]);
			}
			System.out.println(process(input, maxNumber));
			T--;
		}
	}
	static int process(int[] input, int max) {
		int minSeq = Integer.MAX_VALUE;
		boolean[] visited = new boolean[max + 1];
		int uniqueNumber = 0;
		for(int i : input) {
			if(!visited[i]) {
				visited[i] = true;
				uniqueNumber++;
			}
		}
		
		for(int i = 0; (input.length - i) >= uniqueNumber; i++ ) {
			int tempCount = 0;
			boolean allMeet = false;
			int allTouched = 0;
			for(int j = i; j < input.length; j++) {
				if(visited[input[j]]) {
					allTouched++;
				}
				visited[input[j]] = false;
				tempCount++;
				if(allTouched == uniqueNumber) {
					allMeet = true;
					break;
				}
			}
			if(allMeet) {
				minSeq = Math.min(minSeq, tempCount);
				populateVisited(input, visited);
			} else {
				break;
			}
			
		}
		return minSeq;
	}
	
	static void populateVisited(int[] input, boolean[] visited) {
		for(int i : input) {
			visited[i] = true;
		}
	}
}
