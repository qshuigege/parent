package com.example.demo.test.sort;

import java.util.*;

public class StringSort {
    public static void main(String[] args) {
        String s = "如果你是爱上我的沧桑你要想一想爱情不是你想象幸福可能是奢望";
        char[] chars = s.toCharArray();
        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < chars.length; i++) {
            if (map.containsKey(chars[i])){
                map.put(chars[i], map.get(chars[i])+1);
            }else {
                map.put(chars[i], 1);
            }
        }
        System.out.println(map);

        List<Map.Entry<Character, Integer>> list = new ArrayList<>();
        list.addAll(map.entrySet());
        System.out.println(list);
        Collections.sort(list, new Comparator<Map.Entry<Character, Integer>>() {
            @Override
            public int compare(Map.Entry<Character, Integer> o1, Map.Entry<Character, Integer> o2) {
                return o2.getValue()-o1.getValue();
            }
        });
        //Collections.sort(list, (o1, o2) -> o2.getValue()-o1.getValue());
        System.out.println(list);
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<Character, Integer> temp : list){
            for (int i = 0; i < temp.getValue(); i++) {
                sb.append(temp.getKey());
            }
        }
        System.out.println(sb);
    }
}
