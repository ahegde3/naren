package com.narren.ShortestSubstring;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
//You can include standard Java language libraries that you want use.//

public class Algorithm{

    public void solution(String inputFilePath, String usrOutputFilePath ) throws IOException{

        File infile = new File(inputFilePath);
        File outfile = new File(usrOutputFilePath );
        BufferedReader r = new BufferedReader(new FileReader(infile));
        BufferedWriter w = new BufferedWriter(new FileWriter(outfile));
        int C = Integer.parseInt(r.readLine());
        for (int t = 0; t < C; ++t) {
            w.write("Case# " + String.valueOf(t + 1));
            w.write('\n');
            HashMap<String, Integer> count = new HashMap<String, Integer>();
            int N = Integer.parseInt(r.readLine());
            for (int i = 0; i < N; ++i) {
                String str = r.readLine();
                for (int j = 0; j < str.length(); ++j) {
                    for (int k = j + 1; k <= str.length(); ++k) {
                        String subst = str.substring(j, k);
                        if (count.containsKey(subst)) {
                            if (count.get(subst) == i) continue;
                            count.put(subst, 2 * N); // does not care more than twice
                        } else {
                            count.put(subst, i);
                        }
                    }
                }
            }
            int ret[] = new int[N];
            Arrays.fill(ret, 2000);
            for (Map.Entry<String, Integer> entry : count.entrySet()) {
                int pos = entry.getValue();
                if (pos < N) {
                    ret[pos] = Math.min(ret[pos], entry.getKey().length());
                }
            }
            for (int i = 0; i < N; ++i) {
                w.write(String.valueOf(ret[i]));
                w.write('\n');
            }
        }
        w.flush();
        w.close();
    }

    public static void main(String[] args) {
        
    }
}