package cn.staitech.anno.constant;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Container {

    // 定义一个基于多线程 的 hashmap
    public static final Map<Long, ArrayList<Integer>> FILE_MAP = new ConcurrentHashMap<>();


    public static final Map<Integer, File> HASH_MAP = new HashMap<Integer, File>();

    public static final Map<Long, Map<Integer, File>> SPECIAL_MAP = new HashMap<Long, Map<Integer, File>>();
}
