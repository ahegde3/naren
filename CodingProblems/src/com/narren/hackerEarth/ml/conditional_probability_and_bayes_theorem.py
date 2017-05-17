#!/usr/bin/python2.7
'''
Bob has an important meeting tomorrow and he has to reach office on time in morning. His general mode of transport is by car and on a
regular day (no car trouble) the probability that he will reach on time is pot. The probability that he might have car trouble is 
pct. If the car runs into trouble he will have to take a train and only 2 trains out of the available 
N trains will get him to office on time.

Input format

First line: 
pct
Second line: 
pot
Third line: 
N
Output format

Probability he will reach in time, rounded to six decimal digits

SAMPLE INPUT 
0.2
0.3
10
SAMPLE OUTPUT 
0.280000
'''

pct = input()
pot = input()
N = input()

## no car trobule and reach office on time
nct = (1 - pct) * pot

## car trouble and reach office on time
ct = (pct) * (float)(2.0 / N)

prob = nct + ct
print("%.6f" % prob)
