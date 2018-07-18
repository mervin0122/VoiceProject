package com.cn.mcc.bean.Recognizer;

import java.util.List;

/**
 * Created by yyzc on 2018/6/13.
 */
public class Ws {
    private int bg;

    private List<Cw> cw ;

    public void setBg(int bg){
        this.bg = bg;
    }
    public int getBg(){
        return this.bg;
    }
    public void setCw(List<Cw> cw){
        this.cw = cw;
    }
    public List<Cw> getCw(){
        return this.cw;
    }

}
