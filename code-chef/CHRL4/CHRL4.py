import sys
import re
import Queue
import itertools
import CHRL4_pq
import time

def min_excluding(s,excluding):
    current_min = float('inf')
    for idx,elem in enumerate(s):
        if idx != excluding and elem < current_min:
            current_min = s[idx]
    return current_min

def get_min_product_path(n,k,a):
    s = [0]*(k+1)
    s[0] = a[0]
    for i in range(1,min(k+1,n)):
        s[i] = s[0]*a[i]

    for i in range(k+1,n):
        index = i % (k+1)
        # print i,min_excluding(s,index)
        s[index] = (a[i] * min_excluding(s,index))

    return s[(n-1)%(k+1)]

if __name__=="__main__":
    lines = sys.stdin.readlines()
    n, k = [int(x) for x in lines[0].split()]

    lines[1] = lines[1].replace('.','')
    a = [int(x) for x in lines[1].split()]
    # print n,k,a
    t0 = time.time()
    # min_product = CHRL4_pq.get_min_product_path(n,k,a) % 1000000007
    min_product = get_min_product_path(n,k,a) % 1000000007
    t1 = time.time()
    print min_product
    print t1-t0
