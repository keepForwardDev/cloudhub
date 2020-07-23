package com.cloud.hub.utils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @Author: jaxMine
 * @Date: 2019/12/31 16:49
 */
public class ZipUtil {

    public static final Charset GBK = Charset.forName("gbk");
    /**
     * 列出zip中所有文件（非目录）
     * @param f
     * @return
     * @throws IOException
     */
    public static List<String> listZipFile(File f) throws IOException {
        ZipFile file = new ZipFile(f,GBK);
        Predicate<ZipEntry> isFile = ze -> !ze.isDirectory();
        List<ZipEntry> result = file.stream()
                .filter(isFile).collect(Collectors.toList());
        List<String> list= new ArrayList<>();
        result.stream().forEach(r-> {
            list.add(r.getName());
        });
        file.close();
        return list;
    }

    /**
     * 根据zip中的entryName 获取zip中该文件的 输入流
     * @param f
     * @param entryName
     * @return
     * @throws IOException
     */
    public static InputStream getFileByEntryName(File f, String entryName) throws IOException {
        ZipFile file = new ZipFile(f,GBK);
        Predicate<ZipEntry> entryPredicate = ze -> ze.getName().equals(entryName);
        List<ZipEntry> result = file.stream().filter(entryPredicate).collect(Collectors.toList());
        if (result.size()>0) {
            return file.getInputStream(result.get(0));
        }
        return null;
    }

    /**
     * 压缩为zip
     * @param file
     * @param out 文件输出流
     * @throws IOException
     */
    public static void zip(List<File> file , OutputStream out) throws IOException{
        int index=1; // 防止文件重名
        int len; // 每次存放读取文件的字节内容
        byte[] buffer = new byte[1024]; // 每次读取文件的缓存
        FileInputStream fis = null;
        ZipOutputStream zipout= new ZipOutputStream(out,GBK);
        for (File f: file) {
            fis = new FileInputStream(f);
            ZipEntry ze = new ZipEntry(index+"-"+f.getName());
            ze.setSize(f.length());
            zipout.putNextEntry(ze);
            while ((len = fis.read(buffer)) != -1) {
                zipout.write(buffer, 0, len);
            }
            fis.close();
        }
        zipout.flush();
        zipout.close();
    }
}
