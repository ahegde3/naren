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
