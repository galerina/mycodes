import sys
import re
import heapq
import itertools
import math

def get_min_product_path(n,k,a):
    s = [0]*n
    pq = []
    s[0] = a[0]
    for i in range(1,min(k+1,n)):
        shortest_path = a[0]*a[i]
        s[i] = shortest_path
        # shortest_path = a[0]*a[i]
        heapq.heappush(pq,(math.log(a[0])+math.log(a[i]),i))

    for i in range(k+1,n):
        while i - pq[0][1] > k:
            heapq.heappop(pq)
        shortest_path = (a[i]*s[pq[0][1]]) % 1000000007
        s[i] = shortest_path
        heap_value = math.log(a[i]) + pq[0][0]
        if i - pq[0][1] == k:
            heapq.heapreplace(pq,(heap_value,i))
        else:
            heapq.heappush(pq,(heap_value,i))

    return s[n-1]


if __name__=="__main__":
    lines = sys.stdin.readlines()
    n, k = [int(x) for x in lines[0].split()]

    lines[1] = lines[1].replace('.','')
    a = [int(x) for x in lines[1].split()]
    min_product = get_min_product_path(n,k,a) % 1000000007
    print min_product
