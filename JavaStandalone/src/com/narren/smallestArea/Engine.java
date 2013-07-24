package com.narren.smallestArea;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Occurs;

public class Engine {

    private int smallestArea = -1;
    private int[] occurenceRequired;
    private int[] occurenceCount;
    private Entry[] input;
    private int minMatches = 0;
    private int cutOff = 0;

    public void solution (String inputFilePath, String usrOutputFilePath) throws IOException {

        BufferedWriter writer = null;
        try {
            File output = new File(usrOutputFilePath);
            writer = new BufferedWriter(new FileWriter(output));

            BufferedReader stdin = new BufferedReader(new FileReader(new File(inputFilePath)));
            int testCases = Integer.parseInt(stdin.readLine());
            for (int i = 1; i <= testCases; i++) {
                Engine engine = new Engine();
                String[] firstLine = stdin.readLine().split(" ");
                int n = Integer.parseInt(firstLine[0]);
                int k = Integer.parseInt(firstLine[1]);
                int smallestArea = -1;
                engine.input = new Entry[n];
                engine.cutOff = n;
                engine.occurenceRequired = new int[k];
                engine.occurenceCount = new int[k];
                for (int j = 0; j < k; j++) {
                    int matches = Integer.parseInt(stdin.readLine());
                    engine.minMatches += matches;
                    engine.occurenceRequired[j] = matches;
                }
                for (int j = 0; j < n; j++) {
                    String[] entry = stdin.readLine().split(" ");
                    engine.input[j] = engine.new Entry(Integer.parseInt(entry[0]),
                            Integer.parseInt(entry[1]));
                }
                smallestArea = engine.compute();
                writer.write("Case# " + i + "\n");
                if (i < testCases) {
                    writer.write(smallestArea + "\n");
                } else {
                    writer.write("" + smallestArea);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    
    }
    public static void main(String[] args) {
        try {
            new Engine().solution("input", "output");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public int compute() {
        for(int i = 0; i <= (input.length - minMatches); i++) {
            if (minMatches <= (cutOff - i)) {
                computeInside(i);
            } else {
                
            }
            occurenceCount = new int[occurenceRequired.length];
        }
        return smallestArea;
    }

    private void computeInside(int startFrom) {
        int startIndex = input[startFrom].position;
        int occurence = 0;
        for (int i = startFrom; i < cutOff; i++ ) {
            occurence = 0;
            if(occurenceCount[(input[i].element) - 1] != 0) {
                occurence = occurenceCount[(input[i].element) - 1];
            }
            occurenceCount[(input[i].element) - 1] = ++ occurence;
            if (minMatches <= (i - startFrom) && checkIfDone()) {
                computeSmallestArea(startIndex, input[i].position);
                cutOff = i + 1;
                break;
            }
        }
    }

    private boolean checkIfDone() {
        for (int i = 0; i < occurenceRequired.length; i++) {
            int ocurrences = 0;
            if (occurenceCount[i] != 0) {
                ocurrences = occurenceCount[i];
            }

            if ( ocurrences < occurenceRequired[i]) {
                return false;
            }

        }

        return true;
    }

    private void computeSmallestArea(int s, int t) {
        int area = (t - s) + 1;
        smallestArea = (smallestArea == -1) ? area : smallestArea;
        smallestArea = (smallestArea > area) ? area : smallestArea;
    }

    class Entry {

        public int position;
        public int element;

        public Entry(int e, int p) {
            element = e;
            position = p;
        }
    }

}
