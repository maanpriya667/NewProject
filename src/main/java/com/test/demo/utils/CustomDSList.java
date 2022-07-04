package com.test.demo.utils;

import java.util.List;

public class CustomDSList<T> {
    private List<T> list;

    private Integer counter = 0;

    public CustomDSList(List<T> list) {
        this.list = list;
    }

    public T getDS() {
        if (counter + 1 >= list.size()) {
            counter = -1;
        }
        return list.get(++counter);
    }
}