package com.cn.mcc.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * Description:
 * Author: mervin
 * CreateTime：2017-05-31 09:32
 */
public abstract class BaseController {

    protected  int pageno;
    protected  int pagesize;
    protected HttpServletRequest request;

    public static String RESULT_MESSAGE_SUCCESS="200";  //成功
    public static String RESULT_MESSAGE_ERROR="0"; //失败

    public void set(HttpServletRequest request){
        this.request = request;
        this.pagesize = getIntParameter("pagesize", 15);
        this.pageno =  getIntParameter("pageno", 1);
    }

    public void set(HttpServletRequest request , Object obj){
        this.set(request);
    }
    public Result result(String result, String message) {
        return result(result, message, null);
    }

    public Result result(String result, String message, Object obj) {
        return new Result(result, message, obj);
    }

    public Result ok(long total, String message, Object obj) {
        return result(RESULT_MESSAGE_SUCCESS, total, message, obj);
    }

    public Result ok(String message, Object obj) {
        return result(RESULT_MESSAGE_SUCCESS, 0, message, obj);
    }

    public Result error(String message, Object obj) {
        return result(RESULT_MESSAGE_ERROR, message, obj);
    }

    public Result result(String result, long total, String message, Object obj) {
        return new Result(result, total, message, obj);
    }


    public int getIntParameter(String key , int defaultValue){
        if(key != null && !"".equals(key)){
            String str = request.getParameter(key);
            if(str!=null && !"".equals(str)){
                if(request.getParameter(key).matches("[0-9]+")){
                    return Integer.parseInt(request.getParameter(key));
                }
            }
        }
        return defaultValue;
    }


}
