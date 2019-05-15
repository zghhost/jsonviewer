package com.zghhost.jsonviewer.util;

import java.io.*;
import java.nio.channels.FileChannel;

/**
 * @author guohua.zhang
 * 测评常用IO操作
 */
public class IOUtil {
    /**
     * InputStream 写入 文件
     * @param is
     * @param file
     * @throws Exception
     */
    public static void is2file(InputStream is, File file)throws Exception{
        BufferedOutputStream fileBOS = new BufferedOutputStream(new FileOutputStream(file));
        BufferedInputStream resBIS = new BufferedInputStream(is);
        try{
            int len=-1;
            byte[] b=new byte[1024];
            while((len=resBIS.read(b))!=-1){
                fileBOS.write(b,0,len);
            }
        }catch(IOException e){
            throw e;
        }finally {
            fileBOS.close();
            resBIS.close();
        }
    }

    /**
     * 将字符串 写入 文件
     * @param str
     * @param file
     * @throws IOException
     */
    public static void str2file(String str,File file)throws Exception{
        is2file(new ByteArrayInputStream(str.getBytes()),file);
    }


    /**
     * exception 写入 文件
     * @param exp
     * @param file
     * @throws IOException
     */
    public static void exception2file(Exception exp,File file){
        try{
            exp.printStackTrace(new PrintStream(new FileOutputStream(file)));
        }catch(Exception e){
        }
    }

    public static String exception2str(Exception exp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try{
            exp.printStackTrace(new PrintStream(baos));
        }catch(Exception e){
        }
        return baos.toString();
    }

    /**
     * 复制文件
     * @param src
     * @param targetPath
     * @throws IOException
     */
    public static void copy(File src,String targetPath)throws Exception{
        if(!targetPath.endsWith(File.separator)){
            targetPath += File.separator;
        }
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(src).getChannel();
            outputChannel = new FileOutputStream(targetPath+src.getName()).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            inputChannel.close();
            outputChannel.close();
        }
    }

    /**
     * 移动文件
     * @param src
     * @param targetPath
     * @throws IOException
     */
    public static void movie(File src,String targetPath)throws Exception{
        copy(src,targetPath);
        src.delete();
    }

    /**
     * 从文件读取内容到字符串
     * @param file
     * @return
     * @throws IOException
     */
    public static String readAsStr(File file)throws Exception{
        return is2str(new FileInputStream(file));
    }

    public static String is2str(InputStream is)throws Exception{
        return is2str(is,"utf-8");
    }

    public static String is2str(InputStream is,String charsetString)throws Exception{
        return is2str(is,charsetString,null);
    }

    public static String is2str(InputStream is,String charsetString,String lineSeparator)throws Exception{
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(is,charsetString));
        try{
            String line = null;
            while((line = br.readLine()) != null){
                if(sb.length() > 0 && lineSeparator != null){
                    sb.append(lineSeparator);
                }
                sb.append(line);
            }
        }catch(Exception e){
            throw e;
        }finally {
            br.close();
        }
        return sb.toString();
    }

    public static void main(String[] args)throws IOException{
    }
}
