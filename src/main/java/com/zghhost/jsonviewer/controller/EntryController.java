package com.zghhost.jsonviewer.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zghhost.jsonviewer.util.HtmlUtil;
import com.zghhost.jsonviewer.util.HttpUtil;
import com.zghhost.jsonviewer.util.TransUtil;
import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.Label;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * @author guohua.zhang@zhaopin.com.cn
 * @Date 2019/5/14 15:44
 */
@Controller("/")
public class EntryController {

    @RequestMapping
    public String index(){
        return "index";
    }

    @RequestMapping("parse")
    public void parse(HttpServletRequest req, HttpServletResponse res
            ,@RequestParam("json_text") String jsonText, @RequestParam("json_url") String jsonUrl)throws Exception{
        res.setHeader("Content-type","text/html;charset=UTF-8");

        boolean nullJsonText = jsonText == null || "".equals(jsonText.trim());
        boolean nullJsonUrl = jsonUrl == null || "".equals(jsonUrl.trim());
        if(nullJsonText && nullJsonUrl){
            res.getWriter().println(HtmlUtil.geneErrorHtml("请输入JSON TEXT或者JSON URL"));
        }else{
            JSONObject o = null;
            if(!nullJsonText){
                try {
                    o = TransUtil.getJsonObject(jsonText);
                }catch (Exception e){
                    res.getWriter().println(HtmlUtil.geneErrorHtml("无效的JSON TEXT"));
                }
            }else if(!nullJsonUrl){
                try {
                    o = TransUtil.getJsonObject(HttpUtil.doHttpGet(jsonUrl));
                }catch (Exception e){
                    res.getWriter().println(HtmlUtil.geneErrorHtml("无效的JSON URL"));
                }
            }
            try{
                res.setHeader("Content-type","multipart/form-data");
                res.setHeader("Content-Disposition", "attachment;fileName="+System.currentTimeMillis()+".xls");
                writeExcel(o,res.getOutputStream());
            }catch (Exception e){
                e.printStackTrace();
                res.setHeader("Content-type","text/html;charset=UTF-8");
                res.getWriter().println(HtmlUtil.geneErrorHtml("转储失败"));
            }
        }
    }

    public void writeExcel(JSONObject jsonObject,OutputStream os)throws Exception{
        WritableWorkbook wb = Workbook.createWorkbook(os);
        WritableSheet ws = wb.createSheet("sheet",0);
        writeEntity("anonymous",jsonObject,ws,2,1,null,null);
        wb.write();
        wb.close();
    }

    /**
     * @param objname
     * @param o
     * @param ws
     * @param row
     * @param col
     * @return 当前行号
     * @throws Exception
     */
    public int writeEntity(String objname,Object o,WritableSheet ws,int row,int col,String parentId,String relation)throws Exception{
        WritableFont fount = new WritableFont(WritableFont.ARIAL, 12,WritableFont.NO_BOLD, false);
        jxl.write.WritableCellFormat format = new jxl.write.WritableCellFormat(fount);
        format.setBorder(Border.ALL, BorderLineStyle.MEDIUM);

        int entityPoint,swellPoint;
        entityPoint = swellPoint = row;
        if(o instanceof JSONObject){

            objname = TransUtil.underlineName(objname);

            //表名
            ws.addCell(new Label(col,entityPoint,objname,format));
            ws.mergeCells(col,entityPoint,col+3,entityPoint);
            entityPoint++;

            //表头
            ws.addCell(new Label(col,entityPoint,"字段",format));
            ws.addCell(new Label(col+1,entityPoint,"类型",format));
            ws.addCell(new Label(col+2,entityPoint,"描述",format));
            ws.addCell(new Label(col+3,entityPoint,"关系",format));
            entityPoint++;

            //主键
            ws.addCell(new Label(col,entityPoint,"id",format));
            ws.addCell(new Label(col+1,entityPoint,"long",format));
            ws.addCell(new Label(col+2,entityPoint,"主键",format));
            ws.addCell(new Label(col+3,entityPoint,"",format));
            entityPoint++;

            if(parentId != null){
                //外建
                ws.addCell(new Label(col,entityPoint,parentId,format));
                ws.addCell(new Label(col+1,entityPoint,"long",format));
                ws.addCell(new Label(col+2,entityPoint,"外建",format));
                ws.addCell(new Label(col+3,entityPoint,(relation == null ? "" : relation),format));
                entityPoint++;
            }

            JSONObject jo = (JSONObject)o;

            for(String key : jo.keySet()){
                Object value = jo.get(key);
                if((value instanceof JSONObject) || (value instanceof JSONArray)){
                    int sheetPoint = writeEntity(key,value,ws,swellPoint,col+5,objname+"_id","ONE TO ONE");
                    swellPoint = sheetPoint + 1;
                }else{
                    String type = "varchar";

                    if(value.getClass() == java.math.BigDecimal.class){
                        type = "double";
                    }else if(value.getClass() == java.lang.Integer.class){
                        type = "int";
                    }else if(value.getClass() == java.lang.Long.class){
                        type = "long";
                    }
                    ws.addCell(new Label(col,entityPoint,TransUtil.underlineName(key),format));
                    ws.addCell(new Label(col+1,entityPoint,type,format));
                    ws.addCell(new Label(col+2,entityPoint,TransUtil.transEN2ZH(key),format));
                    ws.addCell(new Label(col+3,entityPoint,"",format));
                    entityPoint++;
                }

            }
        }else if(o instanceof JSONArray){
            JSONArray ja = (JSONArray)o;
            if(ja.get(0) instanceof JSONObject){
                swellPoint = writeEntity(objname,ja.getJSONObject(0),ws,row,col,parentId,"MANY TO ONE");
            }
        }

        return entityPoint > swellPoint ? entityPoint : swellPoint;
    }
}
