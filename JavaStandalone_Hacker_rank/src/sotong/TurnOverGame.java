package sotong;

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

}
