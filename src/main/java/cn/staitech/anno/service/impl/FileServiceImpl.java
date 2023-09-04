package cn.staitech.anno.service.impl;

import cn.staitech.anno.constant.Container;
import cn.staitech.anno.domain.file.Chunk;
import cn.staitech.anno.service.FileService;
import cn.staitech.anno.utils.FileUtils;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.tools.DiagnosticListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@Service
public class FileServiceImpl implements FileService {

    private static final String zipUrl = "/home/uploadPath/zipFile/";

//    String zipUrl =  "D:\\zip";


    @Override
    public String mergeChunk(Chunk chunk) throws Exception {
        // 切片名称
        // 压缩包文件地址
//        String zipUrl = "/home/uploadPath/zipFile/";


        //切片文件夹
        // 创建空文件夹
        File zipFile = new File(zipUrl);
        if(!zipFile.exists()){
            FileUtils.createFolder(zipUrl);
        }

        String zipFIleUrl = zipUrl + chunk.getFileName();
        File file = new File(zipFIleUrl);
        if (!file.exists()) {
            FileUtils.createNewzip(zipFIleUrl);
        }
        try (
                InputStream fis = chunk.getFile().getInputStream();
                RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            int len = -1;
            // byte[] buffer = new byte[1024*4];
            byte[] buffer = new byte[1024 * 4 * 10];
            // 指针移动到当前块开始写的位置，chunk.getChunkNumber()是指当前是第几块，减一后乘
            // 以每个块的大小 得到前面块的偏移量，即当前块的起始位置
            raf.seek((chunk.getChunkNumber()) * chunk.getChunkSize());
            //log.info(" ---------------------------> seek:{}", (chunk.getChunkNumber()) * chunk.getChunkSize());
            //把当前块的内容写入
            // java.util.ConcurrentModificationException: null,并发修改异常
            while ((len = fis.read(buffer)) != -1) {
                raf.write(buffer, 0, len);
            }
        } catch (IOException e) {
        }
        return zipFIleUrl;
    }


    public String upload(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            return null;
        }
        //获取文件名
        String fileName = file.getOriginalFilename();
        //获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //获取文件大小
        int fileSize = (int) file.getSize();
        //文件路径
        String path = zipUrl + "\\" + fileName;
        //文件存储路径:保存到数据库
//        String filePath = date+"/"+fileName;
        java.io.File dest = new java.io.File(path);
        // 判断路径是否存在，如果不存在则创建
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            //保存文件
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(path));
            outputStream.write(file.getBytes());
            outputStream.flush();
            outputStream.close();
            return path;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }


//    private static void merge(String dest, File files) {
//        // TODO 自动生成的方法存根
//        String filename = files.getName();
//        filename = files.getName().substring(0, filename.lastIndexOf("-"));
//        System.out.println(filename + ">>>>");
//        try {
//            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dest + File.separator + filename));
//            BufferedInputStream bis = null;
//            byte bytes[] = new byte[1024 * 1024];
//            int len = -1;
//            bis = new BufferedInputStream(new FileInputStream(files));
//            while ((len = bis.read(bytes)) != -1) {
//                bos.write(bytes, 0, len);
//            }
//        } catch (FileNotFoundException e) {
//            // TODO 自动生成的 catch 块
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO 自动生成的 catch 块
//            e.printStackTrace();
//        }
//    }


    private static void merge(String dest, File files) {
        // TODO 自动生成的方法存根
        String filename = files.getName();
        System.out.println(files.getName());
        filename = files.getName().substring(0, filename.lastIndexOf("-"));
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dest + File.separator + filename));
            BufferedInputStream bis = null;
            byte bytes[] = new byte[1024 * 1024];
            int len = -1;

            bis = new BufferedInputStream(new FileInputStream(files));
            while ((len = bis.read(bytes)) != -1) {
                bos.write(bytes, 0, len);
            }
        } catch (FileNotFoundException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (IOException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
    }


    private static Boolean merge1(Chunk chunk, File files, String dest) {
//         TODO 自动生成的方法存根
        String filename = files.getName();
        filename = files.getName().substring(0, filename.lastIndexOf("-"));
        try (
                BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(files.toPath()));
                RandomAccessFile raf = new RandomAccessFile(new FileOutputStream(dest + File.separator + filename).toString(), "rw")) {
            int len = -1;
            // byte[] buffer = new byte[1024*4];
            byte[] buffer = new byte[1024 * 4 * 10];
            // 指针移动到当前块开始写的位置，chunk.getChunkNumber()是指当前是第几块，减一后乘
            // 以每个块的大小 得到前面块的偏移量，即当前块的起始位置
            raf.seek((chunk.getChunkNumber()) * chunk.getChunkSize());

            //log.info(" ---------------------------> seek:{}", (chunk.getChunkNumber()) * chunk.getChunkSize());
            //把当前块的内容写入
            // java.util.ConcurrentModificationException: null,并发修改异常
            while ((len = bis.read(buffer)) != -1) {
                raf.write(buffer, 0, len);
            }
        } catch (IOException e) {

            return false;
        }
        return true;
    }

}
