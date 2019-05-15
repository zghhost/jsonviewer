package com.zghhost.jsonviewer.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author guohua.zhang@zhaopin.com.cn
 * @Date 2019/3/30 15:36
 */
public class PatternUtil {

    public static String getValueWithRex(String src,String rex){
        return getValueWithRex(src,rex,0);
    }

    public static String getValueWithRex(String src,String rex,int groupIndex){
        Pattern p = Pattern.compile(rex);
        Matcher m = p.matcher(src);
        if(m.find()){
            return m.group(groupIndex);
        }
        return null;
    }

    public static void main(String[] args){
        System.out.println(getValueWithRex("而头条你好2042短信验证码124343","\\d+"));
    }
}
