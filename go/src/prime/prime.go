package misc

type primeTester struct {
	primes []bool
}

const primeLimit = 60000000
var tester = newPrimeTester(primeLimit)

// Return new PrimeTester constructed using sieve of Erasthones
func newPrimeTester(limit int) *primeTester {
	t := primeTester{make([]bool,limit+1)}
	
	// Initialize everything as prime
	for i,_ := range t.primes {
		t.primes[i] = true
	}
	t.primes[0], t.primes[1] = false, false

	for i:=2;i<=limit;i++ {
		if t.primes[i] {
			for j := i + i; j <= limit; j = j + i {
				t.primes[j] = false
			}
		}
	}

	return &t
}

func IsPrime(n int) bool {
	return n < len(tester.primes) && tester.primes[n]
}


func GetPrimesIterator() (func () int) {
	i := 2

	return func() int {
		if i >= len(tester.primes) {
			return -1
		}

		current := i
		i++
		for ; i < len(tester.primes) && !IsPrime(i); i++ { }
		return current
	}
}

func histogram(slice []int) (hist map[int]int) {
	hist = map[int]int {}
	for _, x := range slice {
		hist[x] += 1
	}

	return
}

func getPrimeFactors(n int) []int {
	if IsPrime(n) {
		return []int{n}
	} else if n == 1 {
		return nil
	}

	nextPrime := GetPrimesIterator()
	for prime := nextPrime(); ; prime = nextPrime() {
		if n % prime == 0 {
			factors := []int{prime}
			return append(factors, getPrimeFactors(n/prime)...)
		}
	}

	return nil
}


// Return a map of p_i->x_i representing the prime factors of a number n:
//  n = (p_1 ** x_1) * ... * (p_n ** x_n)
func GetPrimeFactorsMap(n int) map[int]int {
	return histogram(getPrimeFactors(n))
}

func GetNthPrime(n int) int {
	getNextPrime := GetPrimesIterator()

	for i := 1; i < n; i++ {
		getNextPrime()
	}

	return getNextPrime()
}
