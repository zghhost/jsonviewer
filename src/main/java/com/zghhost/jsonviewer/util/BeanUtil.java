package com.zghhost.jsonviewer.util;

import java.util.Arrays;

/**
 * @author guohua.zhang@zhaopin.com.cn
 * @Date 2019/5/11 09:53
 */
public class BeanUtil {
    public static void getter(Class<?> clazz,String objname){
        Arrays.stream(clazz.getDeclaredMethods()).forEach(m -> {
            if(m.getName().startsWith("set")){
                System.out.println(objname+"."+m.getName()+"(\"\");");
            }
        });
    }
}
