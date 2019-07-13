package com.example.demo.test.sort;

public class Test {
    public static void main(String[] args) {
        //冒泡排序
        int[] arr = new int[]{5,1,4,2,3,99,33,44,123,9,3,4,2,33};
        System.out.print("排序前：");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]+" ");
        }
        int temp;
        for (int i = 0; i < arr.length; i++) {
            for (int j = i+1; j < arr.length; j++) {
                if (arr[i]>arr[j]){
                    temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
        }
        System.out.print("\n排序后：");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]+" ");
        }
    }
}
