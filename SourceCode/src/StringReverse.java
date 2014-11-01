
public class StringReverse {
	private static String doReverse(String input) {
		int n = input.length() - 1;
		char [] reversedStr = input.toCharArray();
		for(int j = (n-1) >> 1; j >= 0; --j) {
			char temp1 = input.charAt(j);
			char temp2 = input.charAt(n-j);
			reversedStr[j] = temp2;
			reversedStr[n-j] = temp1;
		}
		return new String(reversedStr);
	}
	public static void main(String[] args) {
		System.out.println(doReverse("serpant"));
	}
}
