package com.cn.mcc.utils;

import org.ansj.domain.*;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by yyzc on 2018/6/29.
 */
public class Analysis {

    public static String  ansjSeg(String str) {
        String texts="";
        //只关注这些词性的词
        Set<String> expectedNature = new HashSet<String>() {{
            add("n");add("v");add("vd");add("vn");add("vf");
            add("vx");add("vi");add("vl");add("vg");
            add("nt");add("nz");add("nw");add("nl");
            add("ng");add("userDefine");add("wh");add("r");add("ns");
        }};
        //String str = "欢迎使用ansj_seg,基于java语言开发的轻量级的中文分词工具包!" ;
        org.ansj.domain.Result result = ToAnalysis.parse(str); //分词结果的一个封装，主要是一个List<Term>的terms
        System.out.println(result.getTerms());

        List<Term> terms = result.getTerms(); //拿到terms
        System.out.println(terms.size());

        for(int i=0; i<terms.size(); i++) {
            String word = terms.get(i).getName(); //拿到词
            String natureStr = terms.get(i).getNatureStr(); //拿到词性
            if(expectedNature.contains(natureStr)) {
                // System.out.println(word + ":" + natureStr);
                System.out.print(word + ":" + natureStr+",");
                texts+=word + ":" + natureStr+",";
            }
        }
        return texts;
    }
}
