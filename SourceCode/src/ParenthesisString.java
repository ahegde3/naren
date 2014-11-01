
public class ParenthesisString {

	
	private void arrange (int[] input) {
		// ( - 1
		// ) - 0
		int counter = 0;
		for (int i = 0; i< input.length; i++) {
			if (input[i] == 1) {
				++counter;
			} else {
				--counter;
			}
			if (counter < 0) {
				// swap
				if (input[i] == 1) {
					
				}
			}
		}
	}
}
