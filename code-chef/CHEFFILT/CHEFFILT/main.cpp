//
//  main.cpp
//  CHEFFILT
//
//  Created by Andrew Spencer on 12/12/15.
//  Copyright © 2015 Andrew Spencer. All rights reserved.
//

#include <iostream>
#include <iomanip>
#include <string>
#include <bitset>

int stringToInt(std::string & s) {
    int temp = 0;
    for (int i = 0; i < s.size(); i++) {
        temp <<= 1;
        if (s[i] == 'b' || s[i] == '+') {
            temp |= 1;
        }
    }
    
    return temp;
}

int main(int argc, const char * argv[]) {
    int T;
    std::cin >> T;
    for (int i = 0; i < T; i++) {
        std::string mystring;
        std::cin >> mystring;
        std::cout << std::hex << stringToInt(mystring) << std::endl;
        int n;
        std::cin >> n;
        std::cout << n << std::endl;
        for (int j = 0; j < n; j++) {
            std::string s;
            std::cin >> s;
            std::cout << std::hex << stringToInt(s) << std::endl;
        }
    }
    
    int specialValue = 3;
    std::cout << "Special value = " << specialValue << std::endl;
    for (int i = 0; i < 8; i++) {
        for (int j = 0; j < 8; j++) {
            for (int k = 0; k < 8; k++) {
                if ((i ^ j ^ k) == specialValue) {
                    std::cout << std::bitset<3>(i) << " " << std::bitset<3>(j) << " " << std::bitset<3>(k) << std::endl;
                }
            }
        }
    }
}
