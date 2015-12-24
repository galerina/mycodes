import sys

def get_sum(n1,n2,m):
    smaller_pile_count = min(n1,n2)
    difference = max(n1,n2) - smaller_pile_count

    if m*(m+1)/2 <= smaller_pile_count:
        to_remove = m*(m+1)/2
        return n1+n2-(2 * to_remove)

    to_remove = 0
    saved = 0
    i = m
    while i > 0:
        remaining = smaller_pile_count - to_remove
        if i > remaining:
            to_remove += remaining
            break
        else:
            factor = smaller_pile_count / i
            low = i - factor
            to_remove += ((i * i + i) / 2) - ((low * low + low) / 2)

    return n1+n2-(2 * to_remove)

if __name__=="__main__":
    lines = sys.stdin.readlines()
    t = int(lines[0])

    for i in range(t):
        n1,n2,m = [int(x) for x in lines[i+1].split()]
        print get_sum(n1,n2,m)
