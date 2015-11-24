import random

N=1000
K=30

name1 = 'asc%d.txt'%(N)
f = open(name1, 'w')

f.write("%s %s\n"%(N,K))
f.write(" ".join([str(i+1) for i in range(N)]))
f.write(".")
f.close()

name2 = 'const%d.txt'%(N)
f = open(name2, 'w')

f.write("%s %s\n"%(N,K))
f.write(" ".join(["1"] * N))
f.write(".")
f.close()

name3 = 'rand%d.txt'%(N)
f = open(name3, 'w')

f.write("%s %s\n"%(N,K))
f.write(" ".join([str(random.randint(1,400)) for i in range(N)]))
f.write(".")
f.close()

name1 = 'desc%d.txt'%(N)
f = open(name1, 'w')

f.write("%s %s\n"%(N,K))
f.write(" ".join([str(i) for i in range(N,0,-1)]))
f.write(".")
f.close()
