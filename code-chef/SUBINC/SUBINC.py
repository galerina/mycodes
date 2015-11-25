import sys

# Separate array into non-decreasing subarrays of maximal
# length. For every non-decreasing subarray of length l, add
# l(l+1)/2 to the count
def count_subarrays(a):
    curr_len = 0
    last_elem = float("inf")
    count = 0

    for i in a:
        if last_elem <= i:
            curr_len += 1
            last_elem = i
        else:
            count += curr_len * (curr_len + 1) / 2
            curr_len = 1
            last_elem = i

    count += curr_len * (curr_len + 1) / 2
    return count

if __name__=="__main__":
    lines = sys.stdin.readlines()
    t = int(lines[0])

    for i in range(t):
        n = int(lines[1+i*2])
        a = [int(x) for x in lines[2+i*2].split()]
        print count_subarrays(a)
