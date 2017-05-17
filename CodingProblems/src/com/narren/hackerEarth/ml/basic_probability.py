#!/usr/bin/python2.7
'''
Mike wants to go fishing this weekend to nearby lake. His neighbour Alice (is the one Mike was hoping to ask out since long time) is also
planing to go to the same spot for fishing this weekend. The probability that it will rain this weekend is 
p1. There are two possible ways to reach the fishing spot (bus or train). The probability that Mike will take the bus is 
pmb and that Alice will take the bus is 
pab. Travel plans of both are independent of each other and rain.

What is the probability 
prs that Mike and Alice meet each other only (should not meet in bus or train) in a romantic setup (on a lake in rain)?

Input format

FIrst line: 
pmb
Second line: 
pab
Third line: 
p1
Output format

prs, rounded up to six decimal places.
'''
p_mb = input()
p_ab = input()
p1 = input()

'''
Alice goes by bus and Mike doesnt OR Mike goes by bus and Alice doesnt. 
'''
p_e = (p_mb * (1 - p_ab)) + (p_ab * (1 - p_mb))

'''
Now it should rain AND p_e
'''
res = p_e * p1
print("%.6f" % res)
