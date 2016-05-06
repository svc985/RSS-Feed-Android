package org.prikic.yafr.util;

import java.util.LinkedList;
import java.util.List;

public class Util {

    public static List<Object> getDummyList() {

        List<Object> list = new LinkedList<>();
        list.add("object 1");
        list.add("object 2");
        list.add("object 3");

        return list;
    }
}
