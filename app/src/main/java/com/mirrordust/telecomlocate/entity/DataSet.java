package com.mirrordust.telecomlocate.entity;

import io.realm.RealmObject;

/**
 * Created by LiaoShanhe on 2017/07/24/024.
 */

public class DataSet extends RealmObject {
    private String name;
    private long index;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "DataSet{" +
                "name='" + name + '\'' +
                ", index=" + index +
                '}';
    }
}
