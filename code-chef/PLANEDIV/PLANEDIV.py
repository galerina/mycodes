import sys
import collections
import fractions

def count_perfect(lines):
    special_set = set()
    hist = collections.defaultdict(set)
    for (a,b,c) in lines:
        if b == 0:
            if c == 0:
                special_set.add(0)
            else:
                special_set.add(float(a)/c)
        else:
            hist[float(a)/b].add(float(c)/b)

    if len(hist.values()) > 0:
        max_normal = len(max(hist.values(), key=len))
    else:
        max_normal = 0
    return max(max_normal, len(special_set))

if __name__=="__main__":
    lines = sys.stdin.readlines()
    t = int(lines[0])

    line_idx = 1
    for i in range(t):
        n = int(lines[line_idx])
        line_eqs = []
        for j in range(n):
            line_eqs.append(tuple([int(x) for x in lines[line_idx+j+1].split()]))
        print count_perfect(line_eqs)
        line_idx += n + 1
