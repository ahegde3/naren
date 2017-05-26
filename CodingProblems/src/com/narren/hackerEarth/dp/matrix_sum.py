#!/usr/bin/python2.7

N = input()
M = input()

a = [[0] * M] * N

for x in rang (0, N):
    for y in range(0, M):
        a[x][y] = input()

T = input()
while T > 0:
    X = raw_input()
    Y = raw_input()
    do_process(X, Y)
    T -= 1

def do_process(x, y):
        res = 0;
        for i in range(0, x - 1):
            for j in range(0, y - 1):
                res += a[i][j]
        
        print res
        