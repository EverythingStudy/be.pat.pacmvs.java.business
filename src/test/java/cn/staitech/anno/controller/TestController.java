package cn.staitech.anno.controller;

import cn.staitech.anno.constant.CommonConstant;
import cn.staitech.anno.domain.Image;
import cn.staitech.anno.service.AnnotationService;
import cn.staitech.anno.service.ProjectRoleService;
import cn.staitech.anno.service.PythonOpenSlideService;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysProjectRole;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static cn.staitech.anno.aspect.LogFileAspect.response;


/**
 * @author 王峰
 * @date 2022/11/1 14:10
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    /**
     * 输出名称
     */
    private static final String FILE_NAME = "output_dict.txt";
    private static final List<String> typeLists = Arrays.asList("java.lang.Integer", "java.lang.Double", "java.lang.Float", "java.lang.Long", "java.lang.Short", "java.lang.Byte", "java.lang.Boolean", "java.lang.Char", "java.lang.String", "int", "double", "long", "short", "byte", "boolean", "char", "float");
    private static final int LENGTH = 5000;
    private static final Random random = new Random();
    @Resource
    PythonOpenSlideService pythonOpenSlideService;
    @Resource
    ProjectRoleService projectRoleService;
    @Resource
    private AnnotationService annotationService;
    @Resource
    private RedissonClient client;
    @Resource(name = "redissonClient")
    private RedissonClient redissonClient;

    private static Boolean createFolder(String folderName) throws Exception {
        File file = new File(folderName);
        if (!file.exists() && !file.isDirectory()) {
            if (file.mkdir()) {
                return true;
            } else {
                throw new Exception(MessageSource.M("FILE_DOWNLOAD_ERROR"));
            }
        }
        return true;
    }

    /**
     * Object转Map
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, Object> getObjectToMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<String, Object>(16);
        Class<?> cla = obj.getClass();
        Field[] fields = cla.getDeclaredFields();
        for (Field field : fields) {

            field.setAccessible(true);
            String keyName = field.getName();
            Object value = field.get(obj);
            if (value == null) {
                value = "";
            }
            map.put(keyName, value);
        }
        return map;
    }

    public static Map<?, ?> objectToMap(Object obj) {

        if (obj == null) {
            return null;
        }
        return new org.apache.commons.beanutils.BeanMap(obj);
    }

    public static Map<String, Object> getFieldsValue(Object obj) {
        //通过反射获取所有的字段，getFileds()获取public的修饰的字段
        //getDeclaredFields获取private protected public修饰的字段

        Map<String, Object> map = new HashMap<>(16);
        Field[] fields = obj.getClass().getDeclaredFields();
        String typeName = obj.getClass().getTypeName();
        for (String t : typeLists) {
            if (t.equals(typeName)) {
                return map;
            }
        }
        for (Field f : fields) {
            //在反射时能访问私有变量
            f.setAccessible(true);
            try {
                for (String str : typeLists) {
                    //这边会有问题，如果实体类里面继续包含实体类，这边就没法获取。
                    //可以通递归的方式去处理实体类包含实体类的问题。
                    if (f.getType().getName().equals(str)) {
                        map.put(f.getName(), f.get(obj));
                    }
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public static void main(String[] args) throws Exception {
        Map<String, Object> map = new HashMap<>(16);
        map.put("userId", 1);
        map.put("userName", "admin");
        Object o = map;
        System.out.println(o);
        System.out.println(getFieldsValue(o));

    }

    private static String getRandomString(List<String> list) {
        String tm;
        int s = random.nextInt(list.size());
        tm = list.get(s);
        return tm;
    }

    @GetMapping("/api")
    public R sendMsg() {
        return R.ok(null, "测试多语言#ABC");
    }

    @GetMapping("/sTest")
    public R structure() {
        return R.ok(null, "测试是否构建");
    }


    //Object转Map
//    public static Map<String, Object> objectToMap(Object object){
//        Map<String,Object> dataMap = new HashMap<>(16);
//        Class<?> clazz = object.getClass();
//        for (Field field : clazz.getDeclaredFields()) {
//            try {
//                field.setAccessible(true);
//                dataMap.put(field.getName(),field.get(object));
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
//        return dataMap;
//    }


//    public static Map<?, ?> objectToMap(Object obj) {
//        if(obj == null)
//            return null;
//        return new org.apache.commons.beanutils.BeanMap(obj);
//    }

    /**
     * 获取Redis自增ID
     *
     * @return
     */
    @GetMapping("/sid")
    public Long getAndAddLong(String res) {
//        return CacheUtils.getAndAddLong("measure:test_id_incr1", 1L);
//        return client.getAtomicLong(res).getAndAdd(1L);
        System.out.println(redissonClient + ">>>>");

        RBucket<String> rBucket = redissonClient.getBucket("str");
//// 设置value和key的有效期
        rBucket.set("张三", 30, TimeUnit.SECONDS);
        Object o = rBucket.get();
        System.out.println(o);

        return 1L;
    }

    @GetMapping("/createFile")
    public String createFile(String folderName) throws Exception {
        if (createFolder(folderName)) {
            return "成功";
        } else {
            return "失败";
        }
    }

    /**
     * 获取用户ID
     *
     * @return
     */
    @GetMapping("/uid")
    public Long getUserId() {
        return SecurityUtils.getUserId();
    }

    /**
     * 获取用户ID
     *
     * @return
     */
    @RequestMapping("/python/{imageId}")
    public Image python(@PathVariable("imageId") Long imageId) {
        return pythonOpenSlideService.getImageDetail(imageId);
    }

    @GetMapping("/pr/add/{projectId}/{createBy}")
    public List<SysProjectRole> projectRoleAdd(@PathVariable("projectId") Long projectId, @PathVariable("createBy") Long createBy) {
        // 默认3个角色
        return projectRoleService.addProjectRoles(projectId, createBy);
    }

    @PostMapping("test/11")
    public String test1125(Long projectId, String req) throws Exception {
        boolean res = annotationService.getPermission(projectId, req);
        if (!res) {
            return "失败";
        } else {
            return "ok";
        }
    }

    public String getFileName(Long slideId, String fileSuffix) {
        String updateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "Annotation_" + slideId + CommonConstant.GLIDE_LINE + updateTime + fileSuffix;
    }

    @GetMapping("/setStr")
    public Long setStr(String res, Long userId, String userName) {

        RBucket<String> rBucket = redissonClient.getBucket("slideId" + ":" + res);
        // 设置value和key的有效期
        Map<String, Object> map = new HashMap<>(16);
        map.put("userId", userId);
        map.put("userName", userName);
        // System.out.println(String.valueOf(map) + ">>>>>>>>>>>>>>>>>>>>>>>>>>>");
        rBucket.set(String.valueOf(map), 300, TimeUnit.MINUTES);

        Object o = rBucket.get();
        System.out.println(o);
        return 1L;
    }

    @GetMapping("/getStr")
    public String getStr(String res) throws Exception {


        RKeys keys = redissonClient.getKeys();


        Iterable<String> keysByPattern = keys.getKeysByPattern("slideId:" + "*");

        for (String i : keysByPattern) {

            String o = client.getBucket(i).get().toString();
            System.out.println(o);

//            JSONObject parseObject = JSONArray.parseObject(o);
//            System.out.println(parseObject);
//            JSONObject obj = parseObject.getJSONObject("obj");
//            String uid = obj.getString("uid");

        }

        return "ok";
    }

    /**
     * 删除
     *
     * @param res
     * @return
     */
    @GetMapping("/delStr")
    public boolean delStr(String res) {
        redissonClient.getKeys().delete(res);
        // Object o = rBucket.get();
        // System.out.println(o);
        return true;
    }

    @GetMapping("/acquire")
    public boolean acquire(String req) {
        RLock rLock = redissonClient.getLock(req);
        rLock.lock();
        try {
            // 尝试5秒内获取锁，如果获取到了，最长60秒自动释放
            boolean res = rLock.tryLock(5, TimeUnit.MINUTES);
            if (res) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("获取锁失败，失败原因：" + e.getMessage());
        }
        return false;
    }

    @GetMapping("/acquire1")
    public boolean acquire1(String req) {
        RLock lock = redissonClient.getLock(req);
        try {
            // 尝试加锁，最多等待10秒，上锁以后10秒自动解锁
            if (lock.tryLock(10, 10, TimeUnit.SECONDS)) {
                try {
                    //处理
                    // logger.info("tryLock thread---{}, lock:{}", Thread.currentThread().getId(), lock);
                } catch (Exception e) {
                } finally {
                    //解锁
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            //处理
            //保留中断发生的证据，以便调用栈中更高层的代码能知道中断，并对中断作出响应
            Thread.currentThread().interrupt();
        }
        return true;
    }

    @GetMapping("/release")
    public boolean release(String req) {
        RLock rLock = redissonClient.getLock(req);
        try {
            //尝试5秒内获取锁，如果获取到了，最长60秒自动释放
            boolean res = rLock.tryLock(5L, 60L, TimeUnit.SECONDS);
            if (res) {
                // 进行锁释放
                rLock.unlock();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println("获取锁失败，失败原因：" + e.getMessage());
        }
        return true;

    }

    @GetMapping("/setHash")
    public Long setHash(String res, Long userId, String userName) {
        RMap<Object, Object> rMap = redissonClient.getMap("slideId" + ":" + res);
        rMap.put("userId", userId);
        rMap.put("userName", userName);
        rMap.expire(500, TimeUnit.MINUTES);
        String mValue = (String) rMap.get("userName");
        System.out.println(mValue);
        return 1L;
    }

    @GetMapping("/delHash")
    public boolean delHash(String slideId) {
        Long res = redissonClient.getKeys().delete(slideId);
        System.out.println(res + ">>>>");
        return res > 0;
    }

    @GetMapping("/strFile")
    public String StrFile(String str) throws IOException {
        if (!Optional.ofNullable(str).isPresent()) {
            return MessageSource.M("ARGUMENT_INVALID");
        }
        List<String> SOURCE = new ArrayList<>();
        for (int i = 0; i < str.length(); i++) {
            SOURCE.add(str.substring(i, i + 1));
        }
        List<String> unicodeList = new ArrayList(SOURCE);
        List<String> outputList = new ArrayList<String>();
        Collections.sort(unicodeList);
        response.reset();
        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + FILE_NAME);

        String encoding = "UTF-8";
        int repeatCount = 0;

        try {
//            FileOutputStream fileOutputStream = new FileOutputStream(file);
            int i = 0;
            while (i < LENGTH) {
                String tmp = "";
                int width = random.nextInt(7) + 4;
                for (int j = 0; j < width; j++) {
                    tmp = tmp + getRandomString(unicodeList);
                }
                if (!outputList.contains(tmp)) {
                    i++;
                    outputList.add(tmp);
                    outputStream.write(tmp.getBytes(encoding));
                    if (i < LENGTH) {
                        //最后一行不输入回车
                        outputStream.write('\n');
                    }
                    repeatCount = 0;
                } else {
                    repeatCount++;
                    System.out.println("重复生成的字符串当前行数--->" + i + " 内容---> " + tmp);
                    if (repeatCount == 10000) {
                        System.out.println("连续重复次数超过10000次 已达到最大行数 无法继续生成");
                        break;
                    }
                }
            }
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // 清空response

//            outputStream.write(jsonString.getBytes());
            // 关闭流
            outputStream.close();
        } catch (Exception e) {
            log.error("下载过程发生异常", e);
            return "下载过程发生异常";
        }
        return "操作成功";
    }

    @GetMapping("/i18n")
    public String i18n() {
        String welcome = MessageSource.M("STATISTIC_COUNT");
        log.info("STATISTIC_COUNT{}", welcome);

        welcome = MessageSource.M("welcome");
        log.info("welcome{}", welcome);

        return welcome;
    }

    @GetMapping("/i18n1")
    public String i18n1() {
        String welcome = MessageSource.getMessage("welcome");
        System.out.println(welcome);
        return welcome;
    }
}
