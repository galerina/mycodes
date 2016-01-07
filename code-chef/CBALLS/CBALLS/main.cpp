//
//  main.cpp
//  CBALLS
//
//  Created by Andrew Spencer on 1/6/16.
//  Copyright © 2016 Andrew Spencer. All rights reserved.
//

#include <iostream>
#include <fstream>
#include <vector>
#include <string.h>
#include <algorithm>

const int MAX_NUMBER_OF_BALLS_IN_BUCKET = 10000;

void initPrimesVector(std::vector<int> & v, int max=MAX_NUMBER_OF_BALLS_IN_BUCKET) {
    v.clear();
    
    bool notPrime[max+1];
    memset(notPrime, 0, max+1);
    
    notPrime[0] = true;
    notPrime[1] = true;
    
    int currentPrime = 2;
    
    while (currentPrime <= max) {
        for (int i = currentPrime + currentPrime; i <= max; i += currentPrime) {
            notPrime[i] = true;
        }
        
        for (currentPrime += 1; currentPrime <= max && notPrime[currentPrime]; currentPrime++) {}
    }
    
    for (int i = 0; i <= max; i++) {
        if (!notPrime[i]) {
            v.push_back(i);
        }
    }
}

int getMinExtraBalls(std::vector<int> & buckets, std::vector<int> & primes) {
    int extraBallsCount = 0;
    
    // Normalize to non-decreasing order
    for (int i = 1; i < buckets.size(); i++) {
        if (buckets[i] < buckets[i-1]) {
            extraBallsCount += (buckets[i-1] - buckets[i]);
            buckets[i] = buckets[i-1];
        }
    }

    std::vector<int> extraBallSums(primes.size(), 0);
    
    for (int i = 0; i < primes.size(); i++) {
        for (auto bucket : buckets) {
            int remainder = bucket % primes[i];
            int extra = 0;
            if (remainder > 0) {
                extra = primes[i] - (bucket % primes[i]);
            }
            extraBallSums[i] += extra;
        }
    }
    
    auto minIter = std::min_element(extraBallSums.begin(), extraBallSums.end());
    extraBallsCount += *minIter;
    
    return extraBallsCount;
}

int main(int argc, const char * argv[]) {
    if (getenv("CIN_WORKAROUND_FILENAME")) {
        std::ifstream arq(getenv("CIN_WORKAROUND_FILENAME"));
        std::cin.rdbuf(arq.rdbuf());
    }
    
    std::vector<int> primes;
    initPrimesVector(primes);
    
    int T;
    std::cin >> T;
    for (int i = 0; i < T; i++) {
        int N;
        std::cin >> N;
        std::vector<int> buckets;
        for (int j = 0; j < N; j++) {
            int x;
            std::cin >> x;
            buckets.push_back(x);
        }
        
        std::cout << getMinExtraBalls(buckets, primes) << std::endl;
    }
    
    // TEST
    /*
    std::vector<int> v;
    initPrimesVector(v, 20);
    for (auto prime : v) {
        std::cout << prime << std::endl;
    }
     */
}
