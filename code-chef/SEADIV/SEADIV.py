import sys

import string
digs = string.digits + string.letters

def int2base(x, base):
  if x < 0: 
      sign = -1
  elif x == 0: 
      return digs[0]
  else: 
      sign = 1
  x *= sign
  digits = []
  while x:
      digits.append(digs[x % base])
      x /= base
  if sign < 0:
      digits.append('-')
  digits.reverse()
  return ''.join(digits)

def c_mod7l(a,b,l):
    c = a / b
    c %= pow(7,l)
    return int2base(c,7)    

if __name__=="__main__":
    lines = sys.stdin.readlines()
    t = int(lines[0])

    for i in range(t):
        a = int(lines[i*3+1],7)
        b = int(lines[i*3+2],7)
        l = int(lines[i*3+3],7)
        print c_mod7l(a,b,l)
