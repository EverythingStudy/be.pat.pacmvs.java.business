package cn.staitech.anno.constant;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Container {

    // 定义一个基于多线程 的 hashmap
    public static final Map<String, ArrayList<Integer>> FILE_MAP = new ConcurrentHashMap<>();

}
