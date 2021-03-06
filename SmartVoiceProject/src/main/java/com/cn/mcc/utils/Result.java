package com.cn.mcc.utils;

/**
 * Description:
 * Author: mervin
 * CreateTime：2017-05-31 09:33
 */
public class Result {
    private String statusCode;//200 成功；0 失败
    private String message;
    private Object data;
    private long total;

    public Result() {
    }

    public Result(String statusCode, String message, Object data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public Result(String statusCode,long total, String message, Object data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
        this.total=total;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    /**
     * 重置函数
     */
    private void reset(){
        this.statusCode ="";
        this.data="";
        this.total=0;
        this.message="";
    }
    /**
     * 返回成功的结果
     * @param message
     */

}
