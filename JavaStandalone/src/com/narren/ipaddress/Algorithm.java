package com.narren.ipaddress;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

class Algorithm {
    static String Answer1;
    static String Answer2;
    static String[] array = new String[100000 * 2];

    public static void main(String args[]) throws Exception {

        BufferedReader sc = new BufferedReader(new InputStreamReader(System.in));
        // sc = new BufferedReader(new FileReader("input.txt"));
        int T = Integer.parseInt(sc.readLine());
        String[] values;
        for (int test_case = 0; test_case < T; test_case++) {
            Answer1 = "";
            Answer2 = "";
            int pairs = Integer.parseInt(sc.readLine());
            for (int i = 0; i < pairs * 2; i++) {
                String line = sc.readLine();
                values = line.split(" ");
                array[i] = values[1] + "_" + values[2] + " " + values[0];
                i++;
                array[i] = values[2] + "_" + values[1] + " " + values[0];
            }
            Arrays.sort(array, 0, pairs * 2);
            for (int i = 0; i < pairs * 2 - 1; i++) {
                if (equals(array[i], array[i + 1])) {
                    Answer1 = split(array[i], 2)[1];
                    Answer2 = split(array[i + 1], 2)[1];
                    break;
                }
            }
            System.out.println("Case #" + (test_case + 1));
            if (Answer1.equals("")) {
                System.out.println("0");
            } else {
                if (Integer.parseInt(Answer1) > Integer.parseInt(Answer2)) {
                    System.out.println(Answer2 + " " + Answer1);
                } else {
                    System.out.println(Answer1 + " " + Answer2);
                }
            }
        }
    }

    public static String[] split(String input, int count) {
        String[] result = new String[count];
        char space = ' ';
        int lastIndex = 0;
        int spaceCounter = 0;
        for (int i = 0; i < input.length(); i++) {
            char digit = input.charAt(i);
            if (digit == space) {
                spaceCounter++;
                result[spaceCounter - 1] = input.substring(lastIndex, i);
                lastIndex = i + 1;
            }
        }
        result[count - 1] = input.substring(lastIndex);
        return result;
    }

    static public boolean equals(String s1, String s2) {
        char[] v1 = new char[s1.length()];
        s1.getChars(0, s1.length(), v1, 0);
        char[] v2 = new char[s2.length()];
        s2.getChars(0, s2.length(), v2, 0);
        int i = 0;
        while (v1[i] != ' ' || v2[i] != ' ') {
            if (v1[i] != v2[i])
                return false;
            i++;
        }
        return (v1[i] == ' ' && v2[i] == ' ');
    }
}