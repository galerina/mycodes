import sys
import re
import heapq
import itertools
import math

def get_min_product_path(n,k,a):
    pq = []
    for i in range(1,min(k+1,n)):
        shortest_path = a[0]*a[i]
        # shortest_path = a[0]*a[i]
        heapq.heappush(pq,(shortest_path,i))

    for i in range(k+1,n-1):
        while i - pq[0][1] > k:
            heapq.heappop(pq)
        shortest_path = a[i]*pq[0][0]
        if i - pq[0][1] == k:
            heapq.heapreplace(pq,(shortest_path,i))
        else:
            heapq.heappush(pq,(shortest_path,i))

    return a[n-1]*pq[0][0]


if __name__=="__main__":
    lines = sys.stdin.readlines()
    n, k = [int(x) for x in lines[0].split()]

    lines[1] = lines[1].replace('.','')
    a = [int(x) for x in lines[1].split()]
    min_product = get_min_product_path(n,k,a) % 1000000007
    print min_product
