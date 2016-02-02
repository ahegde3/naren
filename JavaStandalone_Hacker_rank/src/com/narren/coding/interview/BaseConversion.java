package com.narren.coding.interview;

public class BaseConversion {

	public static void main(String[] args) {
		System.out.println(baseConversion("615", 7, 13));
	}
	private static String baseConversion(String in, int b1, int b2) {
		int input = StringToInt.toInt(in);
		int i = 0;
		int basedInt = 0;
		while (input > 9) {
			int temp = input % 10;
			input = input / 10;

			basedInt += Math.pow(b1, i) * temp;
			i++;
		}

		basedInt += Math.pow(b1, (i)) * input;

		String res = "";
		while (basedInt >= b2) {
			int temp = basedInt % b2;
			basedInt = basedInt / b2;
			switch (temp) {
			case 10:
				res = "A" + res;
				break;
			case 11:
				res = "B" + res;
				break;
			case 12:
				res = "C" + res;
				break;
			case 13:
				res = "D" + res;
				break;
			case 14:
				res = "E" + res;
				break;
			case 15:
				res = "F" + res;
				break;

			default:
				res = temp + res;
				break;
			}
		}
		res = basedInt + res;
		
		return res;

	}
}
