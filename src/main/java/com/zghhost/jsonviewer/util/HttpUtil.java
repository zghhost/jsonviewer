package com.zghhost.jsonviewer.util;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author guohua.zhang@zhaopin.com.cn
 * @Date 2019/5/10 20:40
 */
public class HttpUtil {
    public static String doHttpGet(String urls)throws Exception{
        return IOUtil.is2str(doHttpGetAsStream(urls,1000*60,1000*60));
    }

    public static String doHttpGet(String urls,int connectionTimeout,int readTimeout)throws Exception{
        return IOUtil.is2str(doHttpGetAsStream(urls,connectionTimeout,readTimeout));
    }

    public static void doDownload(String urls,String path,String fileName)throws Exception{
        InputStream is = doHttpGetAsStream(urls,1000*60,1000*60);
        File newFile = new File(path.concat(File.separator).concat(fileName));
        IOUtil.is2file(is,newFile);
    }

    public static InputStream doHttpGetAsStream(String urls,int connectionTimeout,int readTimeout)throws Exception{
        return doHttpGetAsStream(urls, Header.COMMON.headers(),connectionTimeout,readTimeout);
    }

    public static InputStream doHttpGetAsStream(String urls,HttpUtil.Header header,int connectionTimeout,int readTimeout)throws Exception{
        return doHttpGetAsStream(urls, header.headers(),connectionTimeout,readTimeout);
    }

    public static InputStream doHttpGetAsStream(String urls,Map<String,String> headers,int connectionTimeout,int readTimeout)throws Exception{
        HttpURLConnection conn = (HttpURLConnection)new URL(urls).openConnection();
        conn.setConnectTimeout(connectionTimeout);
        conn.setReadTimeout(readTimeout);
        headers.forEach((k,v)->{
            conn.setRequestProperty(k,v);
        });
        return conn.getInputStream();
    }

    public enum Header{
        /***/
        COMMON{
            @Override
            Map<String, String> headers() {
                Map<String,String> map = new HashMap<String,String>();
                map.put("Accept"," text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
                map.put("Accept-Language"," zh-CN,zh;q=0.9,en;q=0.8");
                map.put("Cache-Control"," max-age=0");
                map.put("Connection"," keep-alive");
                map.put("Host"," m.ximalaya.com");
                map.put("Upgrade-Insecure-Requests"," 1");
                map.put("User-Agent"," Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1");
                return map;
            }
        }
        ;

        abstract Map<String,String> headers();
    }
}
