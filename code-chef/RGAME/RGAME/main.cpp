//
//  main.cpp
//  RGAME
//
//  Created by Andrew Spencer on 1/4/16.
//  Copyright © 2016 Andrew Spencer. All rights reserved.
//

#include <iostream>
#include <vector>
#include <math.h>

#define ULONG unsigned long long

const ULONG DIVISOR = 1000000007;
const int MAX_N = 100000;
// const int MAX_A = 1000000000;

ULONG powersOf2Mod[MAX_N+1];

void calculatePowersOf2Mod() {
    ULONG current = 1;
    for (int i = 0; i <= MAX_N; i++) {
        powersOf2Mod[i] = current;
        current = (current << 1) % DIVISOR;
    }
}

ULONG getPowerOf2Mod(int exp) {
    return powersOf2Mod[exp];
}

ULONG getScoresSum(std::vector<ULONG> & v) {
    ULONG sum = 0;
    int N = static_cast<int>(v.size() - 1);
    
    ULONG partsSubTotal = 0;
    for (int i = 1; i <= N; i++) {
        if (i - 1 == 0) {
            partsSubTotal += 2 * v[i-1];
        } else {
            partsSubTotal += (getPowerOf2Mod(i - 1) * v[i-1]);
        }
        partsSubTotal %= DIVISOR;
        sum += ((getPowerOf2Mod(N-i) * ((v[i] * partsSubTotal) % DIVISOR))) % DIVISOR;
        sum %= DIVISOR;
    }
    
    return sum;
}

ULONG getScoresSumOrig(std::vector<ULONG> & v) {
    ULONG sum = 0;
    int N = static_cast<int>(v.size() - 1);
    
    for (int i = 1; i <= N; i++) {
        ULONG temp = 0;
        for (int j = 0; j < i; j++) {
            ULONG turnValue = (v[i] * v[j]) % DIVISOR;
            if (j == 0) {
                temp += (2 * turnValue);
            } else {
                temp += (getPowerOf2Mod(j) * turnValue);
            }
            temp %= DIVISOR;
        }
        sum += (getPowerOf2Mod(N-i) * temp);
        sum %= DIVISOR;
    }
    
    return sum;
}

int main(int argc, const char * argv[]) {
    calculatePowersOf2Mod();
    
    int T;
    std::cin >> T;
    for (int i = 0; i < T; i++) {
        int N;
        std::cin >> N;
        std::vector<ULONG> v;
        for (int j = 0; j < N + 1; j++) {
            int a;
            std::cin >> a;
            v.push_back(a);
        }
        
        std::cout << getScoresSum(v) << std::endl;
    }
    
    // Limits TEST
    /*
    std::vector<ULONG> v;
    for (int j = 0; j < 100000 + 1; j++) {
        int a = MAX_A ;
        v.push_back(a);
    }
    
    std::cout << getScoresSum(v) << std::endl;
    
    std::cout << getScoresSumOrig(v) << std::endl;
     */
    
}