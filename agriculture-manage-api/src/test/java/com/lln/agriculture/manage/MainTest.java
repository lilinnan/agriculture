package com.lln.agriculture.manage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/7 15:17
 */

public class MainTest {

    public static void main(String[] args) {
        StringBuilder stringBuilder = new StringBuilder("9999");
        stringBuilder.insert(stringBuilder.length() - 2, '.');
        System.out.println(stringBuilder);
    }

    public static List<String> test(List<List<String>> list, int index, List<String> data) {
        if (list.size() == 0) {
            return Collections.emptyList();
        }
        if (list.size() == 1) {
            return list.get(0);
        }
        if (index >= list.size()) {
            return data;
        }
        List<String> list1 = new ArrayList<>(data);
        List<String> list2 = list.get(index++);
        if (data.size() == 0) {
            list1 = list.get(index++);
            List<String> temp = list1;
            list1 = list2;
            list2 = temp;
        }
        data = new ArrayList<>();
        for (String s : list1) {
            for (String value : list2) {
                data.add(s + value);
            }
        }
        return test(list, index, data);
    }

    public void test(String[] args) {
        List<List<String>> list = new ArrayList<>();
        list.add(new ArrayList<>(Arrays.asList("a", "b", "c", "d")));
        list.add(Collections.emptyList());
        List<String> result = new ArrayList<>();
        System.out.println(test(list, 0, result));
    }


}
