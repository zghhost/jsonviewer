package com.zghhost.jsonviewer;

import com.zghhost.jsonviewer.controller.EntryController;
import com.zghhost.jsonviewer.util.IOUtil;
import com.zghhost.jsonviewer.util.TransUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @author guohua.zhang@zhaopin.com.cn
 * @Date 2019/5/15 14:14
 */
public class CommandEntry {
    private static String workpath = "/Users/wow/Downloads/response";
    public static void main(String[] args)throws Exception{
        File workdir = new File(workpath);
        for(File f : workdir.listFiles()){
            String fileName = f.getName();
            File xlsfile = new File(workpath+File.separator+fileName.split("\\.")[0]+".xls");
            if(xlsfile.exists()){
                xlsfile.delete();
            }
            try{
                String jsonString = IOUtil.is2str(new FileInputStream(f));
                System.out.println(jsonString);
                new EntryController().writeExcel(TransUtil.getJsonObject(jsonString),new FileOutputStream(xlsfile));
            }catch (Exception e){
                if(xlsfile.exists()){
                    xlsfile.delete();
                }
            }
        }
    }
}
