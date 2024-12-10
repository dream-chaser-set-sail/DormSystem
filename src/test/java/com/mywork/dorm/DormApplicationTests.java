package com.mywork.dorm;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class DormApplicationTests {

    @Test
    void contextLoads() {
        char a = '\u2122';
        char b = '\u03c0';
        char c = '\u00a9';

        BigDecimal aa = BigDecimal.valueOf(3.1415926535);
        BigDecimal bb = BigDecimal.valueOf(3.28);

        double aa1 = 3.1415926535;
        double bb1 = 3.28;

        double cc1 = aa1+bb1;
        BigDecimal cc = aa.add(bb);
//        System.out.println(cc);
//        System.out.println(cc1);

        int i = 123456789;
        float t = i;
        System.out.println(t);
    }

    @Test
    void test1(){
        int factorial = factorial(5);
        System.out.println(factorial);
    }

    public int factorial(int n) {
        if (n == 1) {
            return 1;
        }

        return n * factorial(n-1);
    }
}
