#!/usr/bin/python2.7
'''
# Read input from stdin and provide input before running code

print 'Hi, %s.' % name
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
