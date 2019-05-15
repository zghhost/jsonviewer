package com.zghhost.jsonviewer.util;

/**
 * @author guohua.zhang@zhaopin.com.cn
 * @Date 2019/5/14 16:39
 */
public class HtmlUtil {
    public static String geneBodyHtml(String body){
        return new StringBuilder()
                .append("<!DOCTYPE html>")
                .append("<head>")
                .append("</head>")
                .append("<body>")
                .append(body)
                .append("</body>").toString();
    }

    public static String geneErrorHtml(String errmsg){
        return geneBodyHtml(new StringBuilder()
            .append("<script>")
            .append("alert('").append(errmsg.replaceAll("'","")).append("');")
            .append("window.close();")
            .append("</script>")
            .toString()
        );
    }
}
