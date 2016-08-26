package sotong;

import java.util.Scanner;

/**
 * 
As in , there is a 4×4 sized table. In a grid of the table, there are white or black stones. When you choose a position of stone randomly, four stones adjacent to the up, down, left and right sides of the stone will turn to the opposite color like turning a white stone to a black & a black stone to a white. Let’s suppose this process as a calculation.﻿﻿

Using such a calculation, you want to change all the stones on the table into all whites or all blacks. Find out the minimum operation count at this time. 

Time limit: 1 second (java: 2 seconds)

[Input]
Several test cases can be included in the inputs. T, the number of cases is given in the first row of the inputs. After that, the test cases as many as T (T ≤ 30) are given in a row. 
Table info is given without blank over four rows per each test case. Colors are indicated like white for ‘w’ and black for ‘b’.

[Output]
Output the minimum operation count to change all colors as white or black on the first row per each test case. If not possible, output ‘impossible’.

[I/O Example]
Input
2
bwwb
bbwb
bwwb
bwww
bwbw
wwww
bbwb
bwwb

Output
4
impossible 


SAMPLE Input
5
bwbw
wwww
bbwb
bwwb
bwwb
bbwb
bwwb
bwww
wwww
wwww
wwww
wwww
bbbb
bbbb
bbbb
bbbb
bbbb
bwbb
bbbb
bbbb

// Explanation
Solution to first example.

Moves (row, column) indexed from 0, on ascii image moves are bold:

(0,2), (0,0), (3,1),  (1,1) 

 

bwwb   bwwb   wbwb   wbww   wwww 

bbwb   wbwb   bbwb   bbbw   wwww 

bwwb   wbwb   wbwb   wbww   wwww

bwww   wwww   wwww   wwww   wwww

 * @author naren
 *
 */
public class TurnOverGame {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt();
		sc.nextLine();
		while(T > 0) {
			String input = "";
			for(int i = 1; i < 5; i++) {
				String readInput = sc.nextLine();
				for(int j = 0; j < 4; j++) {
					input += readInput.charAt(j) == 'b' ? 1 : 0;
				}
			}
			int inputNumber = Integer.parseInt(input, 2);
			T--;
		}
	}
	
	static void process(int input) {
		int curIndex = 0;
		int[] combination = new int[100000];
		boolean[] visited = new boolean[100000];
		while(true) {
			for(int i = 0, j = 1; i < 16; i++, j++) {
				visited[input] = true;
				int temp = getFlippedNumber(input, j);
			}
		}
	}
	
	static int getFlippedNumber(int input, int flipBit) {
		int xorNumber = 0;
		if(flipBit == 1 || flipBit == 5 || flipBit == 9 || flipBit == 13) {
			xorNumber |= 1 << (flipBit - 1);
		} else if (flipBit == 4 || flipBit == 8 || flipBit == 12 || flipBit == 16) {
			xorNumber |= 1 << (flipBit + 1);
		} else {
			if(flipBit + 1 < 17) {
				xorNumber |= 1 << (flipBit + 1);
			}
			if(flipBit - 1 > 0) {
				xorNumber |= 1 << (flipBit - 1);
			}	
		}
	
		if(flipBit + 4 < 17) {
			xorNumber |= 1 << (flipBit + 4);
		}
		if(flipBit - 4 > 0) {
			xorNumber |= 1 << (flipBit - 4);
		}
		return xorNumber;
	}
}
