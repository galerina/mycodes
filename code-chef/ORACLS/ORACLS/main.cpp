//
//  main.cpp
//  ORACLS
//
//  Created by Andrew Spencer on 12/11/15.
//  Copyright © 2015 Andrew Spencer. All rights reserved.
//

#include <iostream>
#include <string>
#include <algorithm>

const int MAX_STRING_LEN = 2000;

int main(int argc, const char * argv[]) {
    int T;
    std::cin >> T;
    for (int i = 0; i < T; i++) {
        int stringsCnt;
        std::cin >> stringsCnt;
        
        int aMinCnt = MAX_STRING_LEN;
        int bMinCnt = MAX_STRING_LEN;
        for (int j = 0; j < stringsCnt; j++) {
            std::string mystring;
            std::cin >> mystring;
            int aCnt = 0, bCnt = 0;
            for (int index = 0; index < mystring.size(); index++) {
                if (mystring[index] == 'a') {
                    aCnt++;
                } else {
                    bCnt++;
                }
            }
            
            aMinCnt = std::min(aMinCnt, aCnt);
            bMinCnt = std::min(bMinCnt, bCnt);
        }
        
        std::cout << std::min(aMinCnt, bMinCnt) << std::endl;
    }
    return 0;
}
