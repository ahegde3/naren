package sotong;

/**
 * 
There is a mobile piece and a stationary piece on the N×M chessboard.
The available moves of the mobile piece are the same as set out in the image below.
<<Refer to chess.png file in this directory>>
You need to capture the stationary piece by moving the mobile piece with the minimum amount of moves.

Write a program to find out the minimum number moves to catch a piece. 

Time limit:1 second (java: 2 seconds)

[Input]
Several test cases can be included in the inputs. T, the number of cases is given in the first row of the inputs. After that, the test cases as many as T (T = 20) are given in a row. 
N, the numbers of the rows and M, the number of columns of the chessboard are given in the first row of each test case. 
R & C is the location information of the attacking piece and S & K is the location of the defending pieces and are given in the row at the second line. However, the location of the uppermost end of the left end is (1, 1)

[Output]
Output the minimum number of movements to catch a defending piece at the first line of each test case. If not moveable, output equals -1. 

[I/O Example]

Input 
2
9 9
3 5 2 8
20 20
2 3 7 9

Output
2

5

Sample input
5
10 10
1 1 10 10
20 20
2 3 7 9
30 30
2 15 29 29
40 40
2 3 1 40
45 45
40 10 27 40

 * @author nsbisht
 *
 */
public class Chess {

}
