package sotong;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LocustSub {
	public static void main(String args[]) throws Exception {
        Scanner sc = new Scanner(System.in);
        //sc = new Scanner(new FileReader("src/top_coders/round_15/task_1/input.txt"));
        int t = Integer.parseInt(sc.next());
        for (int test_number = 1; test_number <= t; test_number++) {
            int N = Integer.parseInt(sc.next());
            List<int[]> floors = new ArrayList<int[]>(N);
            for (int floor = 0; floor < N - 1; ++floor) {
                int M = 2 * Integer.parseInt(sc.next());
                int[] lines = new int[M + 2];
                for (int i = 1; i <= M; ++i) {
                    lines[i] = Integer.parseInt(sc.next());
                }
                lines[M + 1] = 100000001;
                floors.add(lines);
            }

            //last floor
            int M = Integer.parseInt(sc.next());
            List<int[]> previousAll = new ArrayList<int[]>(M);
            for (int line = 0; line < M; ++line) {
                previousAll.add(new int[] {Integer.parseInt(sc.next()), Integer.parseInt(sc.next()), 1});
            }

            for (int floor = N - 2; floor >= 0; --floor) {
                List<int[]> currentAll = new ArrayList<int[]>();
                int previousI = 0;

                int[] currentFloor = floors.get(floor);
                M = currentFloor.length;
                boolean empty = true;
                for (int i = 0; i < M - 1; i++) {
                    if (empty) {
                        for (int j = previousI; j < previousAll.size(); ++j) {
                            int[] previous = previousAll.get(j);

                            if (previous[0] < currentFloor[i + 1] && previous[1] > currentFloor[i]) {
                                int[] needAdd = new int[]{Math.max(currentFloor[i], previous[0]), Math.min(currentFloor[i + 1], previous[1]), previous[2]};
                                if (!currentAll.isEmpty() && needAdd[2] == currentAll.get(currentAll.size() - 1)[2] && currentAll.get(currentAll.size() - 1)[1] == needAdd[0]) {
                                    currentAll.get(currentAll.size() - 1)[1] = needAdd[1];
                                } else {
                                    currentAll.add(needAdd);
                                }
                                if (previous[1] >= currentFloor[i + 1]) {
                                    previousI = j;
                                    break;
                                }
                            }
                        }
                    } else {
                        int value = Integer.MAX_VALUE;
                        for (int j = previousI; j < previousAll.size(); ++j) {
                            int[] previous = previousAll.get(j);
                            if (previous[0] < currentFloor[i + 1] && previous[1] > currentFloor[i]) {
                                value = Math.min(value, previous[2] + 1);
                                if (previous[1] >= currentFloor[i + 1]) {
                                    previousI = j;
                                    break;
                                }
                            }
                        }
                        if (value != Integer.MAX_VALUE) {
                            int[] needAdd = new int[]{currentFloor[i], currentFloor[i + 1], value};
                            if (!currentAll.isEmpty() && needAdd[2] == currentAll.get(currentAll.size() - 1)[2] && currentAll.get(currentAll.size() - 1)[1] == needAdd[0]) {
                                currentAll.get(currentAll.size() - 1)[1] = needAdd[1];
                            } else {
                                currentAll.add(needAdd);
                            }
                        }
                    }
                    empty = !empty;
                }
                previousAll = currentAll;
            }

            int result =  Integer.MAX_VALUE;
            for (int i = 0; i < previousAll.size(); ++i) {
                result = Math.min(result, previousAll.get(i)[2]);
            }
            System.out.println("Case #" + test_number);
            System.out.println(result);
        }
    }
}
