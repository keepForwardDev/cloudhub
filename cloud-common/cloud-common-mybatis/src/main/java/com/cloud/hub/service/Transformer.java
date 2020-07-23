package com.cloud.hub.service;

import java.util.ArrayList;
import java.util.List;

public interface Transformer<T,V> {

    /**
     * 转换实体为bean
     * @param t
     * @return
     */
    V convert(T t);

    default List<V> convert(List<T> list) {
        List<V> resultList = new ArrayList<>();
        list.forEach(t -> {
            resultList.add(convert(t));
        });
        return resultList;
    };

}
