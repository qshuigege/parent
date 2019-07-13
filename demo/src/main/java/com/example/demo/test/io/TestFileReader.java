package com.example.demo.test.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.*;

public class TestFileReader {
    public static void main(String[] args) {
        try {
            String property = System.getProperty("user.dir");
            System.out.println(property);
            //FileReader fileReader = new FileReader("D:\\works\\workspace_intellij\\parent\\demo\\src\\main\\java\\com\\example\\demo\\test\\io\\test.txt");
            FileReader fileReader = new FileReader("demo/src/main/java/com/example/demo/test/io/test.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String temp;
            List<String> strList = new ArrayList<>();
            Map<String, String> map = new HashMap<>();
            int count = 0;
            while((temp = bufferedReader.readLine())!=null){
                String[] split = temp.split(" ");
                map.put(split[1], split[0]);
                strList.add(split[1]);
                System.out.println(temp);
                count++;
                System.out.println(count);
            }
            System.out.println("总共有"+count+"行。");

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            long[] arr = new long[strList.size()];
            Map<String, String> map2 = new HashMap<String, String>();
            for (int i = 0; i < strList.size(); i++) {
                Date d = format.parse(strList.get(i));
                arr[i] = d.getTime();
                map2.put(arr[i]+"", strList.get(i));
            }
            for (int i = 0; i < arr.length; i++) {
                System.out.println(arr[i]);
            }

            long tt;
            for (int i = 0; i < arr.length; i++) {
                for (int j = i+1; j < arr.length; j++) {
                   if (arr[i]>arr[j]){
                       tt = arr[i];
                       arr[i] = arr[j];
                       arr[j] = tt;
                   }
                }
            }
            strList.clear();
            for (int i = 0; i < arr.length; i++) {
                System.out.println(map2.get(arr[i]+"")+" "+map.get(map2.get(arr[i]+"")));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
