package com.narren.Hanoi;

public class TowerOfHanoi {
    private int size;
    private String fromPeg;
    private String toPeg;
    private String usingPeg;

    public TowerOfHanoi(int size, String fromPeg, 
			String toPeg, String usingPeg) {
	this.size = size;
	this.fromPeg = fromPeg;
	this.toPeg = toPeg;
	this.usingPeg = usingPeg;
    }

    public void solveTower() {
	// Print out some header information.
	System.out.println("Steps for solving " + size + 
			   " disk tower of hanoi puzzle, ");
	System.out.println("starting with discs on " + fromPeg +
			   " and moving discs to " + toPeg + ":");
	System.out.println();

	// Solve the problem by moving size disks from the
	// fromPeg to the toPeg using the usingPeg.
	solveTower(size, fromPeg, toPeg, usingPeg);             // C2
    }

    // Recursive helper method that prints the instructions for
    // moving numDisks from the fromPeg to the toPeg using the
    // usingPeg.
    private void solveTower(int num, String from, 
			    String to, String using) {

	if (num == 1) {
	    // Base Case: Move 1 disk...
	    System.out.println("Move disk from " + from +
			       " to " + to + ".");
	}
	else {
	    // Recursive Case: 
	    //   Move num-1 disks from the from peg to
	    //   the usingPeg using the toPeg.
	    solveTower(num-1, from, using, to);   // RC1

	    //   Move 1 disk from the fromPeg to the toPeg using
	    //   the usingPeg.
	    solveTower(1, from, to, using);            // RC2
	    
	    //   Move num-1 disks from the usingPeg to 
	    //   the toPeg using the fromPeg.
	    solveTower(num-1, using, to, from);   // RC3
	}
    }

    public static void main(String[] args) {
	TowerOfHanoi tower = new TowerOfHanoi(4,"A", "B", "C");
	tower.solveTower();// C1
    }
}
