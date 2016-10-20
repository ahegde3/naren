package sotong;

/**
 * 
 * Refer to the screen shots with the same name
 * 
Sample Input

5
3 100
75 45 80
30 55 95
2 100
65 90
20 30
5 150
35 105 100 45 75
115 75 55 35 105
7 150
70 95 15 65 85 75 55
105 80 10 90 115 110 45
8 200
35 30 50 80 70 15 10 40
70 20 20 85 65 40 25 50

Expected output
15
-1
25
-1
45
 * @author naren
 *
 */
public class SamsungTire {

	public static void main(String[] args) {
		int[] num = new int[]{1,2,3,4};
		getPermutation(new int[]{1,1,1,1}, new int[4], 0, num);
	}
	static void getPermutation(int[] count, int[] result, int level, int[] num) {
		for(int i = 0; i < count.length; i++) {
			if(count[i] > 0) {
				result[level] = num[i];
				getPermutation(getNewArray(count, i), result, level + 1, num);
			}
		}
		for(int i = 0; i < count.length; i++)
			System.out.print(result[i]);
		System.out.println();
	}
	
	static int[] getNewArray(int[] in, int j) {
		int[] newArr = new int[in.length];
		for(int i = 0; i < in.length; i++) {
			if(i == j) {
				newArr[i] = in[i] - 1;
			} else
				newArr[i] = in[i];
		}
		return newArr;
	}
}
