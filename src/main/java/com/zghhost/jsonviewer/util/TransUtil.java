package com.zghhost.jsonviewer.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.regex.Pattern;

/**
 * @author guohua.zhang@in.com.cn
 * @Date 2019/5/15 11:21
 */
public class TransUtil {
    public static JSONObject getJsonObject(String json){
        if(json.startsWith("[")){
            return JSONArray.parseArray(json).getJSONObject(0);
        }else if(json.startsWith("{")){
            return JSONObject.parseObject(json);
        }else{
            throw new RuntimeException("Can only woke on JSONArray or JSONObject!");
        }
    }

    public static String underlineName(String name){
        if(name == null){
            return null;
        }
        StringBuffer sb = new StringBuffer();
        boolean firstchar = true;
        for(char ch : name.toCharArray()){
            if(Character.isUpperCase(ch)){
                if(!firstchar){
                    sb.append('_');
                }
                ch = Character.toLowerCase(ch);
            }
            sb.append(ch);
            firstchar = false;
        }
        return sb.toString();
    }

    public static String transEN2ZH(String name){
        //http://fy.iciba.com/ajax.php?a=fy&f=en&t=zh&w=Company%20Account%20Number
        if(name == null){
            return null;
        }

        try {
            String src = underlineName(name).replaceAll("_"," ");
            String jsonres = HttpUtil.doHttpGet("http://fy.iciba.com/ajax.php?a=fy&f=en&t=zh&w="+src,1000,1000);
            JSONObject job = JSONObject.parseObject(jsonres);
            String transedString = job.getJSONObject("content").getString("out");
            if(transedString == null){
                //说明是单词或者词组 和翻译的返回结果不同
                transedString = job.getJSONObject("content").getString("word_mean");
                transedString = PatternUtil.getValueWithRex(transedString," ([^;]*)");
            }
            if(transedString == null){
                System.out.println(jsonres);
            }
            return transedString;
        }catch (Exception e){
            return null;
        }
    }
}
