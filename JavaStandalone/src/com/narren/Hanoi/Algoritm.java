package com.narren.Hanoi;

import java.util.Scanner;

class Algorithm {

    public static void main(String args[]) throws Exception {
        Scanner sc = new Scanner(System.in);

        int jj = sc.nextInt();
        int anss[]= new int[jj];

        for(int test_case = 0; test_case < jj; test_case++) {

            int n = sc.nextInt();

            int arr1[] = new int[n];
            int arr2[] = new int[n];

            for (int i = 0; i < n; i++) {
                arr1[i] = sc.nextInt();
            }

            for (int i = 0; i < n; i++) {
                arr2[i] = sc.nextInt();
            }       

            int answer=0;


            boolean tt=false;
            int b[][] = new int[3][n];
            int c[] = new int[3];

            for (int i = 0; i < n; i++) {
                b[arr1[i]-1][c[arr1[i]-1]] = i;
                c[arr1[i]-1]++;
            }

            for (int i = 0; i < 3; i++) {
                for (int j = 1; j < c[i]; j++) {
                    if((b[i][j]-b[i][j-1])%2==0) tt=true;
                }
            }

            for (int i = 0; i < 3; i++) {c[i]=0;}

            for (int i = 0; i < n; i++) {
                b[arr2[i]-1][c[arr2[i]-1]] = i;
                c[arr2[i]-1]++;
            }

            for (int i = 0; i < 3; i++) {
                for (int j = 1; j < c[i]; j++) {
                    if((b[i][j]-b[i][j-1])%2==0) tt=true;
                }
            }
            answer=0;
            if (tt){answer=0;}else{

                boolean t=false;

                if (arr1[0]!=arr2[0]) {
                    if (arr1[0] == 1)
                    {
                        answer=1;

                    }else
                    {
                        answer=0; 

                    }
                }else

                    if (arr1[0]==1)t=false;else t=true;

                for (int i = 1; i < n; i++) {
                    if (arr1[i] != arr2[i]){


                        if ((((arr1[i] != arr1[i-1]&&arr2[i] == arr2[i-1])&&t)||((arr2[i] != arr2[i-1]&&arr1[i]==arr1[i-1])&&!t))) 
                            answer=1;

                        break;
                    }

                    if (arr1[i]!=arr1[i-1]) t=!t;

                }
            }
            // Print the answer to standard output(screen).
            //System.out.println(""+answer);
            anss[test_case]=answer;
        }
        for(int test_case = 0; test_case < jj; test_case++) {
            System.out.println("Case #"+(test_case+1));
            System.out.println(""+anss[test_case]);

        }

        sc.close();
    }
}