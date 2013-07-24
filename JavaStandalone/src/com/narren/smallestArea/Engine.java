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

public class Engine {

    private int smallestArea = -1;
    private Map<Integer, Integer> occurenceRequired = new HashMap<Integer, Integer>();
    private Map<Integer, Integer> occurenceCount = new HashMap<Integer, Integer>();
    private Entry[] input;

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
                int element = 1;
                int smallestArea = -1;
                engine.input = new Entry[n];
                for (int j = 1; j <= k; j++) {
                    engine.occurenceRequired.put(element++,
                            Integer.parseInt(stdin.readLine()));
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
        for(int i = 0; i < input.length; i++) {
            computeInside(input, i);
            occurenceCount.clear();
        }
        return smallestArea;
    }

    private void computeInside(Entry[] input, int startFrom) {
        int startIndex = input[startFrom].position;
        int occurence = 0;
        for (int i = startFrom; i < input.length; i++ ) {
            occurence = 0;
            if(occurenceCount.get(input[i].element) != null) {
                occurence = occurenceCount.get(input[i].element);
            }
            occurenceCount.put(input[i].element, ++ occurence);
            if (checkIfDone()) {
                computeSmallestArea(startIndex, input[i].position);
                break;
            }
        }
    }

    private boolean checkIfDone() {
        for (Map.Entry<Integer, Integer> entry : occurenceRequired.entrySet()) {
            int ocurrences = 0;
            if (occurenceCount.get(entry.getKey()) != null) {
                ocurrences = occurenceCount.get(entry.getKey());
            }

            if ( ocurrences < entry.getValue()) {
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
