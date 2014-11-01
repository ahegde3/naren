import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;


public class Reverse {

	public static void main(String[] args) throws Exception {
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		Scanner sc = new Scanner(System.in);
		for (int i = 0; i < 10; i++) {
			int number = sc.nextInt();
			String binary = toUnsignedString(number, 1);
			String reverse = doReverse(binary);
			int result = parseInt(reverse, 2);//integerfrmbinary(reverse);
			System.out.println("#" + (i+1) + " " + result);
		}
	}
	private static String doReverse(String input) {
		StringBuilder str = new StringBuilder(input);
		str = str.reverse();
		return str.toString();
	}
	final static char[] digits = {
		'0' , '1' , '2' , '3' , '4' , '5' ,
		'6' , '7' , '8' , '9' , 'a' , 'b' ,
		'c' , 'd' , 'e' , 'f' , 'g' , 'h' ,
		'i' , 'j' , 'k' , 'l' , 'm' , 'n' ,
		'o' , 'p' , 'q' , 'r' , 's' , 't' ,
		'u' , 'v' , 'w' , 'x' , 'y' , 'z'
	};
	private static String toUnsignedString(int i, int shift) {
		char[] buf = new char[32];
		int charPos = 32;
		int radix = 1 << shift;
		int mask = radix - 1;
		do {
			buf[--charPos] = digits[i & mask];
			i >>>= shift;
		} while (i != 0);

		return new String(buf, charPos, (32 - charPos));
	}

	public static int integerfrmbinary(String str){
	    double j=0;
	    for(int i=0;i<str.length();i++){
	        if(str.charAt(i)== '1'){
	         j=j+ Math.pow(2,str.length()-1-i);
	     }

	    }
	    return (int) j;
	}
	
	static int parseInt(String s, int radix)
			throws NumberFormatException
			{
		if (s == null) {
			throw new NumberFormatException("null");
		}

		if (radix < Character.MIN_RADIX) {
			throw new NumberFormatException("radix " + radix +
					" less than Character.MIN_RADIX");
		}

		if (radix > Character.MAX_RADIX) {
			throw new NumberFormatException("radix " + radix +
					" greater than Character.MAX_RADIX");
		}

		int result = 0;
		boolean negative = false;
		int i = 0, len = s.length();
		int limit = -Integer.MAX_VALUE;
		int multmin;
		int digit;

		if (len > 0) {
			char firstChar = s.charAt(0);
			if (firstChar < '0') { // Possible leading "-"
				if (firstChar == '-') {
					negative = true;
					limit = Integer.MIN_VALUE;
				} else
					throw new NumberFormatException(s);

				if (len == 1) // Cannot have lone "-"
					throw new NumberFormatException(s);
				i++;
			}
			multmin = limit / radix;
			while (i < len) {
				// Accumulating negatively avoids surprises near MAX_VALUE
				digit = Character.digit(s.charAt(i++),radix);
				if (digit < 0) {
					throw new NumberFormatException(s);
				}
				if (result < multmin) {
					throw new NumberFormatException(s);
				}
				result *= radix;
				if (result < limit + digit) {
					throw new NumberFormatException(s);
				}
				result -= digit;
			}
		} else {
			throw new NumberFormatException(s);
		}
		return negative ? result : -result;
			}

}
