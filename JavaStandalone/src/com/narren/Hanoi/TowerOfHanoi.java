package com.narren.Hanoi;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Scanner;
import java.util.Stack;

public class TowerOfHanoi {
    private ArrayList<Stack<Integer>> gameRepresentation = new ArrayList<Stack<Integer>>(3);
    private ArrayList<Stack<Integer>> thirdDayGame = new ArrayList<Stack<Integer>>(3);

    // Recursive helper method that prints the instructions for
    // moving numDisks from the fromPeg to the toPeg using the
    // usingPeg.
    private int solveTower(int num, String from, 
			    String to, String using) {

    if (num > 11) {
        return 0;
    }
	if (num == 1) {
	    // Base Case: Move 1 disk...
//	    System.out.println("Move disk from " + from +
//			       " to " + to + ".");
	    int fromIndex = (from.equals("A")) ? 0 : (from.equals("B")) ? 1 : 2;
	    int toIndex = (to.equals("A")) ? 0 : (to.equals("B")) ? 1 : 2;
	    int element = gameRepresentation.get(fromIndex).pop();
	    gameRepresentation.get(toIndex).push(element);
	    if (readState()) {
	       return 1;
	    }
	}
	else {
	    // Recursive Case: 
	    //   Move num-1 disks from the from peg to
	    //   the usingPeg using the toPeg.
	    if (solveTower(num-1, from, using, to) == 1) {
	        return 1;
	    }

	    //   Move 1 disk from the fromPeg to the toPeg using
	    //   the usingPeg.
	    if (solveTower(1, from, to, using) == 1) {
	        return 1;
	    }
	    
	    //   Move num-1 disks from the usingPeg to 
	    //   the toPeg using the fromPeg.
	    if (solveTower(num-1, using, to, from) == 1) {
	        return 1;
	    }
	}
	return 0;
    }

    public static void move(int n, int startPole, int endPole) {
        if (n== 0){
          return; 
        }
        int intermediatePole = 6 - startPole - endPole;
        move(n-1, startPole, intermediatePole);
        System.out.println("Move " +n + " from " + startPole + " to " +endPole);
        move(n-1, intermediatePole, endPole);
      }
    
    public static void main(String[] args) {
//	TowerOfHanoi tower = new TowerOfHanoi(4,"A", "B", "C");
//	tower.solveTower();// C1
        //move(4, 1, 2);
/*        try {
            new TowerOfHanoi().solution("input", "output");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
        Scanner sc = new Scanner(System.in);
        TowerOfHanoi tower = null;
        int result = 0;
        int testCases = sc.nextInt();
        for (int i = 1; i <= testCases; i++) {
            int N = sc.nextInt();
            for (int j = 0; j < N; j++) {
                int dayTwo = sc.nextInt();
            }
            if (N < 11) {
                tower = new TowerOfHanoi();
                tower.thirdDayGame.add(new Stack<Integer>());
                tower.thirdDayGame.add(new Stack<Integer>());
                tower.thirdDayGame.add(new Stack<Integer>());
            }
            for (int j = 0; j < N; j++) {
                int dayThree = sc.nextInt();
                if (N < 11) {
                    tower.thirdDayGame.get(dayThree - 1).push(N - j);
                }
            }
            if (N < 11) {
                tower.gameRepresentation.add(new Stack<Integer>());
                tower.gameRepresentation.add(new Stack<Integer>());
                tower.gameRepresentation.add(new Stack<Integer>());

                
                for (int k = N ; k > 0 ; k--) {
                    tower.gameRepresentation.get(0).push(k);
                }
             
                result = tower.solveTower(N,"A", "B", "C");
            }

            System.out.println("Case #" + i);
            if (i < testCases) {
                System.out.println(result);
            } else {
                System.out.print("" + result);
            }
        }
    }

    private boolean readState() {
        boolean same = false;
        for (int i = 0 ; i < 3 ; i++) {
            Stack<Integer> tempStack = gameRepresentation.get(i);
            Stack<Integer> tempInput = thirdDayGame.get(i);
            int element = -1;
            int inputElement = -1;
            try {
                element = tempStack.peek();
                inputElement = tempInput.peek();
//                System.out.println("Peg:" + i + " disc:" + element);
//                System.out.println("Peg:" + i + " Idisc:" + inputElement);
                same = (element == inputElement);
                if (!same) {
                    break;
                }
            } catch (EmptyStackException e) {
                if (inputElement == -1) {
                    same = false;
                }
                continue;
            }
        }
        return same;
    }

}
