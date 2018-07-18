package com.cn.mcc.bean.Recognizer;

import java.util.List;

/**
 * Created by yyzc on 2018/6/13.
 */
public class Root {
    private int sn;

    private boolean ls;

    private int bg;

    private int ed;

    private List<Ws> ws ;

    public void setSn(int sn){
        this.sn = sn;
    }
    public int getSn(){
        return this.sn;
    }
    public void setLs(boolean ls){
        this.ls = ls;
    }
    public boolean getLs(){
        return this.ls;
    }
    public void setBg(int bg){
        this.bg = bg;
    }
    public int getBg(){
        return this.bg;
    }
    public void setEd(int ed){
        this.ed = ed;
    }
    public int getEd(){
        return this.ed;
    }
    public void setWs(List<Ws> ws){
        this.ws = ws;
    }
    public List<Ws> getWs(){
        return this.ws;
    }
}
