package com.narren.sotong;

/**
 * 
A night shift is not the best thing in the world, but someone has to do it. There is a schedule of night shifts for N days.
To reduce the burden, our company made a contract with another company for R days of night shifts.
It is guaranteed that R < N and N is a multiple of R.
Our company has N-R men and each one has a distinct ID number between 1 and N-R.
The rule of the night shift schedule is as follows. 
  

? Rule 1: Each day one man is on a night shift. 
? Rule 2: For two men with ID numbers, the one with smaller ID number comes before the other in the night shift schedule. 
? Rule 3: Men from another company are on night shifts for R days and they have no ID number.
The following table shows an example of a night shift schedule with R=2 and N=4. Each man is represented by his ID number or X,
which means that the days shift is allocated to another company. 

Day  1  2  3  4
Man  X  1  2  X

Let M = N/R. The new company rule says that the days numbered by multiples of M should be allocated to another company.
The above table should be changed as follows.

Day  1  2  3  4
Man  1  X  2  X

While there is a new rule, nobody knows how to implement it. So we repeatedly pick two men and exchange
their night shift days. During this procedure, the rule of the night shift schedule should be followed.
In the above example, if we exchange two men at Day 1 and Day 2, we are able to get a new schedule under the new rule.
Consider another example with R=2 and N=6. 

Day  1  2  3  4  5  6
Man  1  X  X  2  3  4
 
After 3 exchanges we can get a new schedule:  
1.     Exchange two men atDay 2 and Day 4. 
2.     Exchange two men atDay 4 and Day 5.  
3.     Exchange two men atDay 5 and Day 6. 

Day  1  2  3  4  5  6
Man  1  2  X  3  4  X
 
Given an original night shift schedule, write a program to compute a new one following the rules and making the number
of exchange as small as possible. Time Limit: 1 seconds for 15 cases.  ?(java 2 second) (If your program exceeds this
time limit, the answers that have been already printed are ignored and the score becomes 0. So, it may be better to print a
wrong answer when a specific test case might cause your program to exceed the time limit. One guide for the time limit excess
would be the size of the input.)
  

[Input]
There can be more than one test case in the input file. The first line has C, the # of test cases. Then the totally C test
cases are provided in the following lines (1 = C = 15). Each test case starts with a line containing two integers, M and R
(1 = M, R = 100,000, 1 = M * R = 100,000). The following line contains M * R integers which describe the original night shift
schedule..If a day is allocated to our man, it is represented by his ID. Otherwise, it is represented by zero.  ?
The properties of the test cases are as follows: ?
? The first test case: M = 10 and R = 2.
? The second test case: M = 100 and R = 2.
? The third test case: M = 2 and R = 10.
? The fourth test case M = 2 and R = 100. 
? The 5fth ~ 10th test cases: N = 10,000.
? The 11th ~ 15th test cases: N = 100,000. 

[Output] 
For the T-th test case, Case #T should be printed out in the first line, followed by an integer which is the total number
of exchanges. In the following lines, each line represents one exchange in order. If you exchange men at Day i and Day j, print i and j.
It should be that i < j. For each test case, the number of exchanges sol that your program has found and a reference solution will be
compared to calculate the score.  If sol is larger than twice the reference solution, then the score will be zero. If sol is at most the
reference solution, then the scores will be given. 

[I/O Example]
Input 
3            ?  There are 3 test cases.
2 2          ?  Case 1
0 1 2 0
4 2         ?  Case 2
0 1 2 3 0 4 5 6
3 2         ?  Case 3
1 0 0 2 3 4

Output
Case #1
1
1 2
Case #2
6
1 2 
2 3 
3 4 
5 6
6 7
7 8
Case #3
3
2 4
4 5
5 6
 * 
 * @author nsbisht
 *
 */
public class NightShift {

}
