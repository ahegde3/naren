package sotong;

/**
 * For problem description and examples refer to screen shots in this folder
 * whose name starts with "ConstructionOfBaseStation_"
 * @author naren
 *
 */
public class ConstructionOfBaseStation {

	static Cell[] stack;
	public static void main(String[] args) {

	}

	public static void getNeighbors(int[][] arr, int w, int h, int wRow, int hCol) {
		Cell[] neighbors = new Cell[6];
		int i = 0;
		// We have two patterns for finding the neighbors, based on whether the row is even or odd
		if((wRow & 1) == 0) {
			//Even
			if(wRow - 1 >= 0) {
				neighbors[i++] = new Cell(wRow - 1, hCol);
			}
			if(wRow + 1 < w) {
				neighbors[i++] = new Cell(wRow + 1, hCol);
			}
			if(hCol - 1 >= 0) {
				neighbors[i++] = new Cell(wRow, hCol - 1);
			}
			if(hCol + 1 < h) {
				neighbors[i++] = new Cell(wRow, hCol + 1);
			}
			if(wRow - 1 >= 0 && hCol - 1 >= 0) {
				neighbors[i++] = new Cell(wRow - 1, hCol - 1);
			}
			if(wRow + 1 < w && hCol - 1 >= 0) {
				neighbors[i++] = new Cell(wRow + 1, hCol - 1);
			}
		}

	}


	static class Cell {
		int r;
		int c;
		public Cell(int x, int y) {
			r = x;
			c = y;
		}
	}
	static class Stack {
		static int head = 0;
		static int tail = 0;

		static boolean isEmpty() {
			return head == 0 && tail == 0;
		}

		static void push(Cell cell) {
			if(isEmpty()) {
				tail++;
			}
			head++;
			stack[head] = cell;
		}

		static Cell pop() {
			Cell cell = null;
			if(isEmpty()) {
				return null;
			}
			cell = stack[head];
			head--;
			if(head == tail) {
				head = 0;
				tail = 0;
			}
			return cell;
		}
	}
}
