package com.example.demo.test.logic;

import java.math.BigInteger;
import java.util.Scanner;

public class Test {

    public static void main(String[] args) {
        //计算s=1+11+111+1111+11...1
        Scanner input = new Scanner(System.in);
        System.out.print("请输入相加的数的个数：");
        int n = input.nextInt();
        //int n = 3;
        int a = 1;
        int s = 0;
        /*for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int temp = 10;
                if (j<1){
                    temp = 1;
                }else if(j<2){
                    temp = 10;
                }else {
                    for (int k = 0; k <= j; k++) {
                        temp = temp * 10;
                    }
                }
                s = s + a*temp;
            }
        }*/




        /*int temp = a;
        for (int i = 0; i < n; i++){
            if (i==0){
                s = s + a;
            }else {
                temp = temp + a*myPow(10,i);
                s = s + temp;
            }
        }*/

        int nval = 0;
        int ntemp = a;
        for (int i = 0; i < n; i++) {
            if (i==0){
                nval = a;
            }else {
                nval = ntemp + a*myPow(10, i);
                ntemp = nval;
            }
        }
        System.out.println("第"+n+"个位置的数为："+nval);

        /*Scanner input = new Scanner(System.in);
        System.out.print("Enter an int number:");
        int aaa = input.nextInt();
        int i = myPow(10, aaa);
        System.out.println(i);*/


        System.out.println("10的"+n+"次方是："+Math.pow(10, n));


    }


    public static int myPow(int a, int b){
        int t = a;
        for (int i = 1; i < b; i++) {
            t = t*a;
        }
        return t;
    }
}
