package com.narren.smallestArea;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeSet;
//You can include standard Java language libraries that you want use.//

public class Algorithm{

    public void solution(String inputFilePath, String usrOutputFilePath ) throws IOException{

        File infile = new File(inputFilePath);
        File outfile = new File(usrOutputFilePath );
        BufferedReader r = new BufferedReader(new FileReader(infile));
        BufferedWriter w = new BufferedWriter(new FileWriter(outfile));
        int t = Integer.parseInt(r.readLine());
        for (int c = 0; c < t; ++c) {
            w.write("Case# " + String.valueOf(c + 1));
            w.write('\n');
            solve(r,w);
        }
        w.flush();
        w.close();
    }

    int INF = 1 << 29;

    public static void main(String[] args) {
        try {
            new Algorithm().solution("input", "output");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private void solve(BufferedReader r, BufferedWriter w) throws IOException {
        String[] input = r.readLine().split(" ");
        int N = Integer.parseInt(input[0]);
        int K = Integer.parseInt(input[1]);

        int[] need = new int[K];
        for (int i = 0; i < K; ++i) {
            need[i] = Integer.parseInt(r.readLine());
        }
        ArrayList<LinkedList<Integer>> automata = new ArrayList<LinkedList<Integer>>();
        for (int i = 0; i < K; ++i) {
            automata.add(new LinkedList<Integer>());
        }
        int ret = INF;
        boolean reach = false;
        TreeSet<Integer> positions = new TreeSet<Integer>();
        for (int i = 0; i < N; ++i) {
            String[] pair = r.readLine().split(" ");
            int kind = Integer.parseInt(pair[0]);
            int pos = Integer.parseInt(pair[1]);
            LinkedList<Integer> history = automata.get(kind - 1);
            history.add(pos);
            positions.add(pos);
            if (history.size() < need[kind - 1]) {
                continue;
            } else if (history.size() == need[kind - 1] && !reach) {
                boolean reachnow = true;
                for (int j = 0; j < K; ++j) {
                    int hiscount = automata.get(j).size();
                    if (hiscount < need[j]) {
                        reachnow = false;
                        break;
                    }
                }
                if (reachnow) reach = true;
                else continue;
            } else if (history.size() > need[kind - 1]) {
                positions.remove(history.remove());
            }
            automata.set(kind - 1, history);
            if (!reach) continue;
            ret = Math.min(ret, pos - positions.first() + 1);
        }
        if (ret == INF) ret = -1;
        w.write(String.valueOf(ret));
        w.write('\n');
    }
}