package org.foi.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CollectionHelper {
    static public List<String> convertSetToList(Set<String> set) {
        List<String> list = new ArrayList<String>();
        list.addAll(set);
        return list;
    }
}