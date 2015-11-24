import sys
import re
import Queue
 
MAX_A = 100000
 
# We can think of this problem in terms of a graph. Let there be
# N vertices 1...N and let there be a path from vertex i to every vertex
# j where i+1 <= j <= i+k. Also, let every vertex i have a weight a[i]. 
# Given this find the minimum product vertex weights for a path from 1 to
# N.
def get_min_product_path(n,k,a):
    frontier = Queue.PriorityQueue()
    marked = set()
    frontier.put((a[0], 0))
    marked.add(0)
    
    while (True):
        dist, node = frontier.get()
        if node == n-1:
            return dist
        minimum = MAX_A
        for i in range(min(node + k, n-1), node, -1):
            if (not i in marked and a[i] < minimum):
                minimum = a[i]
                frontier.put((dist * a[i], i))
                marked.add(i)
 
    assert False
    
 
if __name__=="__main__":
    lines = sys.stdin.readlines()
    n, k = [int(x) for x in lines[0].split()]
 
    lines[1] = lines[1].replace('.','')
    a = [int(x) for x in lines[1].split()]
    # print n,k,a
    print get_min_product_path(n,k,a) % 1000000007
